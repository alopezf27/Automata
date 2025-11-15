package parser;

public enum TokenType {
    SYMBOL,       // letras, números, u otros operandos
    STAR,         // *
    PLUS,         // +  (inteligente: alternancia o cerradura)
    QUESTION,     // ?
    OR,           // |  (alternancia)
    LPAREN,       // (
    RPAREN,       // )
    CONCAT,       // · (se inserta automáticamente)
    END           // fin de la expresión antes del '.'
}
