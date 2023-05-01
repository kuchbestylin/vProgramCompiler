package main.java.vprogramcompiler;

public enum TokenType {
    Keyword,
    OpenBracket,
    CloseBracket,
    LeftBrace,
    RightBrace,
    Identifier,
    Symbol,
    Operator,
    Invalid,
    Term, // Only in Assembly Code
}
