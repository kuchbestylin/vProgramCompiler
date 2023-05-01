package vprogramcompiler;

/**
* 
* TokenType is an enum that represents the type of a language token.
* <p>
* This enum has the following possible values:
* Keyword: represents a keyword in the language
* OpenBracket: represents an opening bracket in the language
* CloseBracket: represents a closing bracket in the language
* LeftBrace: represents a left brace in the language
* RightBrace: represents a right brace in the language
* Identifier: represents an identifier in the language
* Symbol: represents a symbol in the language
* Operator: represents an operator in the language
* Invalid: represents an invalid character in the language
* Term: represents a term in the Assembly Code language </p>
*/

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
    Term,
}
