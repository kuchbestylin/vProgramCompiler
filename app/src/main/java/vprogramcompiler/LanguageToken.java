package vprogramcompiler;

/**
* 
* The LanguageToken class represents a single token in a programming language, with a value, a type and an optional array of sub-tokens.
*/
class LanguageToken {
    String value;
    TokenType type;
    LanguageToken[] values;

    public LanguageToken(String value, TokenType tokenType, LanguageToken[] values) {
        this.value = value;
        this.type = tokenType;
        this.values = values;
    }

    LanguageToken(String value, TokenType tokenType) {
        this(value, tokenType, new LanguageToken[]{});
    }
}