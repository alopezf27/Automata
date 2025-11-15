package parser;

public class Token {
    public TokenType type;
    public String value;
    public int position;

    public Token(TokenType type, String value, int position) {
        this.type = type;
        this.value = value;
        this.position = position;
    }
}
