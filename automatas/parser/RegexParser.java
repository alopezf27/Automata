package parser;

import model.Nodo;
import java.util.*;

public class RegexParser {

    // -------------------------------------------------------------------------
    // EXCEPCIÓN CON POSICIÓN Y “SE ESPERABA”
    // -------------------------------------------------------------------------
    public static class RegexParseException extends Exception {
        public final int index;
        public final String expected;

        public RegexParseException(String msg, int index, String expected) {
            super(msg);
            this.index = index;
            this.expected = expected;
        }
    }

    // -------------------------------------------------------------------------
    // TOKENS GENERADOS POR EL LEXER
    // -------------------------------------------------------------------------
    private List<Token> tokens;
    private int pos;
    private Token current;

    // -------------------------------------------------------------------------
    // PARSE PRINCIPAL
    // -------------------------------------------------------------------------
    public Nodo parse(String expr) throws RegexParseException {
        this.tokens = tokenize(expr);
        this.tokens = insertConcatenation(tokens);
        this.pos = 0;
        this.current = tokens.get(0);

        Nodo root = parseAlternation();

        if (current.type != TokenType.END) {
            throw error("Expresión mal terminada", current.position, "fin de expresión");
        }

        return root;
    }

    // -------------------------------------------------------------------------
    // LEXER
    // -------------------------------------------------------------------------
    private List<Token> tokenize(String expr) throws RegexParseException {
        List<Token> result = new ArrayList<>();

        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);

            if (Character.isWhitespace(c)) continue;

            switch (c) {
                case '(':
                    result.add(new Token(TokenType.LPAREN, "(", i));
                    break;
                case ')':
                    result.add(new Token(TokenType.RPAREN, ")", i));
                    break;
                case '*':
                    result.add(new Token(TokenType.STAR, "*", i));
                    break;
                case '?':
                    result.add(new Token(TokenType.QUESTION, "?", i));
                    break;
                case '|':
                    result.add(new Token(TokenType.OR, "|", i));
                    break;
                case '+':
                    result.add(new Token(TokenType.PLUS, "+", i));
                    break;
                case '.':
                    result.add(new Token(TokenType.END, ".", i));
                    return result; 
                default:
                    result.add(new Token(TokenType.SYMBOL, Character.toString(c), i));
                    break;
            }
        }

        throw new RegexParseException("Se esperaba '.' al final", expr.length(), "'.'");
    }

    // -------------------------------------------------------------------------
    // INSERTAR CONCATENACIÓN AUTOMÁTICA
    // -------------------------------------------------------------------------
    private List<Token> insertConcatenation(List<Token> list) {
        List<Token> out = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            Token a = list.get(i);
            out.add(a);

            if (i + 1 < list.size()) {
                Token b = list.get(i + 1);

                boolean aIsOperand =
                        a.type == TokenType.SYMBOL ||
                        a.type == TokenType.RPAREN ||
                        a.type == TokenType.STAR ||
                        a.type == TokenType.PLUS ||
                        a.type == TokenType.QUESTION;

                boolean bIsOperand =
                        b.type == TokenType.SYMBOL ||
                        b.type == TokenType.LPAREN;

                if (aIsOperand && bIsOperand) {
                    out.add(new Token(TokenType.CONCAT, "·", b.position));
                }
            }
        }
        return out;
    }

    // -------------------------------------------------------------------------
    // CONSUMO DE TOKENS
    // -------------------------------------------------------------------------
    private Token consume(TokenType type) throws RegexParseException {
        if (current.type != type) {
            throw error("Token inesperado", current.position, "se esperaba " + type);
        }
        Token t = current;
        pos++;
        current = tokens.get(pos);
        return t;
    }

    private boolean check(TokenType t) {
        return current.type == t;
    }

    private boolean match(TokenType t) throws RegexParseException {
        if (check(t)) {
            consume(t);
            return true;
        }
        return false;
    }

    private RegexParseException error(String msg, int index, String expected) {
        return new RegexParseException(msg, index, expected);
    }

    // -------------------------------------------------------------------------
    // GRAMÁTICA DEL PARSER
    // alternation := concatenation ( (| OR +) concatenation )*
    // concatenation := unary ( CONCAT unary )*
    // unary := primary ( (* or +? or ?) )*
    // primary := SYMBOL | ( alternation )
    // -------------------------------------------------------------------------

    private Nodo parseAlternation() throws RegexParseException {
        Nodo node = parseConcatenation();

        while (current.type == TokenType.OR || isBinaryPlus()) {
            consume(current.type); // | o +
            Nodo right = parseConcatenation();
            node = new Nodo("+", node, right);
        }

        return node;
    }

    private boolean isBinaryPlus() throws RegexParseException {
        int idx = pos;
        if (idx < tokens.size()) {
            Token t = tokens.get(idx);

            if (t.type == TokenType.PLUS) {
                if (pos + 1 < tokens.size()) {
                    Token next = tokens.get(pos + 1);

                    boolean nextOperand =
                            next.type == TokenType.SYMBOL ||
                            next.type == TokenType.LPAREN;

                    if (nextOperand) return true;
                }
            }
        }
        return false;
    }

    private Nodo parseConcatenation() throws RegexParseException {
        Nodo node = parseUnary();

        while (current.type == TokenType.CONCAT) {
            consume(TokenType.CONCAT);
            Nodo right = parseUnary();
            node = new Nodo("·", node, right);
        }
        return node;
    }

    private Nodo parseUnary() throws RegexParseException {
        Nodo node = parsePrimary();

        while (true) {

            if (match(TokenType.STAR)) {
                node = new Nodo("*", node, null);
            }
            else if (match(TokenType.QUESTION)) {
                Nodo eps = new Nodo("ε", -1);
                node = new Nodo("+", eps, node);
            }
            else if (isUnaryPlus()) { 
                consume(TokenType.PLUS);
                Nodo kleene = new Nodo("*", node, null);
                node = new Nodo("·", node, kleene);
            }
            else break;
        }

        return node;
    }

    private boolean isUnaryPlus() throws RegexParseException {
        if (current.type != TokenType.PLUS) return false;

        Token prev = tokens.get(pos - 1);

        boolean prevOperand =
                prev.type == TokenType.SYMBOL ||
                prev.type == TokenType.RPAREN ||
                prev.type == TokenType.STAR ||
                prev.type == TokenType.QUESTION;

        if (!prevOperand) return false;

        Token next = tokens.get(pos + 1);
        boolean nextOperand =
                next.type == TokenType.SYMBOL ||
                next.type == TokenType.LPAREN;

        return !nextOperand;
    }

    private Nodo parsePrimary() throws RegexParseException {

        if (match(TokenType.SYMBOL)) {
            Token t = tokens.get(pos - 1);
            return new Nodo(t.value, t.position + 1);
        }

        if (match(TokenType.LPAREN)) {
            Nodo inside = parseAlternation();

            if (!match(TokenType.RPAREN)) {
                throw error("Falta ')'", current.position, "')'");
            }

            return inside;
        }

        throw error("Símbolo inválido", current.position, "operando o '('");
    }
}
