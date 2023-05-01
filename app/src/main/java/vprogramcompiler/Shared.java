package vprogramcompiler;

import java.util.ArrayList;
import java.util.Collections;

public class Shared {
        /**
     * This is a helper method that takes in a string literal and convert it to its binary represantation
     * by iteration through each character and converting it to its ascii Value and then from ascii value
     * to its integer representation.
     * 
     * @param str
     * @return
     */
    public String stringToBinary(String str) {
        StringBuilder binary = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            int asciiValue = (int) c;
            String binaryValue = Integer.toBinaryString(asciiValue);
            binary.append(String.format("%8s", binaryValue).replace(' ', '0'));
        }
        return binary.toString();
    }

    /**
    * 
    * Converts a given string into a binary representation.
    * @param input the input string to be converted
    * @return a binary string representation of the input string
    */
    public String convertStringToBinary(String input) {
        StringBuilder result = new StringBuilder();
        char[] chars = input.toCharArray();
        for (char aChar : chars) {
            result.append(
                    String.format("%8s", stringToBinary(String.valueOf(aChar)))
                            .replaceAll(" ", "0")
            );
        }
        return result.toString();
    }

    /**
    * 
    * Determines whether a given character is a delimiter or not.
    * @param ch the character to check
    * @return true if the character is a delimiter (i.e. a whitespace, punctuation mark or other special symbol), false otherwise
    */
    public boolean isDelimiter(Character ch) {
        return ch == ' ' || ch == '+' || ch == '-' || ch == '*' ||
                ch == '/' || ch == ',' || ch == ';' || ch == '>' ||
                ch == '<' || ch == '=' || ch == '(' || ch == ')' ||
                ch == '[' || ch == ']' || ch == '{' || ch == '}';

    }

    /**
    * 
    * Determines whether a given character is an operator or not.
    * @param ch The character to check.
    * @return true if the given character is an operator (+, -, *, /, or =), false otherwise.
    */
    public boolean isOperator(Character ch) {
        return ch == '+' || ch == '-' || ch == '*' ||
                ch == '/' || ch == '=';

    }

    /**
    * 
    * Checks if a given string is a valid identifier.
    * An identifier is considered valid if it consists of one or more alphabetic characters (case insensitive) and does not represent an integer.
    * @param str the string to check
    * @return true if the string is a valid identifier, false otherwise
    */
    public boolean validIdentifier(String str) {
        try {
            Integer.parseInt(str);
            return false;
        } catch (Exception e) {
            if(str.matches("[A-Za-z]"))
                return true;
            else
                return false;
        }

    }

    /**
     * This function checks if a tocken is a valid Keyword
     * 
     * @param str
     * @return
     */
    public boolean isKeyword(String str) {
        return str.equals("BEGIN") || str.equals("INTEG") || str.equals("LET") || str.equals("INPUT") ||
                str.equals("WRITE") || str.equals("END");

    }

    /**
    * 
    * Extracts terms from an input string by analyzing a list of LanguageTokens and
    * grouping together adjacent tokens with operators of increasing precedence, as
    * defined by the order of operators in the operatorOrder array.
    * @param input The input string to extract terms from.
    * @param tokens The list of LanguageTokens to analyze.
    * @return An ArrayList of LanguageTokens representing the extracted terms.
    */
    public ArrayList<LanguageToken> extractTerms(String input, ArrayList<LanguageToken> tokens) {
        String[] operatorOrder = {"/", "*", "+", "-"};
        ArrayList<LanguageToken> terms = new ArrayList<>();
        for (var operator : operatorOrder) {
            boolean foundOperator;
            do {
                foundOperator = false;
                ArrayList<Integer> tokenIndicesToRemove = new ArrayList<>();
                for (int j = 0; j < tokens.size(); j++) {
                    var token = tokens.get(j);
                    if (token.type == TokenType.Operator && token.value.equals(operator)) {
                        LanguageToken prevToken = tokens.get(j - 1);
                        LanguageToken nextToken = tokens.get(j + 1);
                        LanguageToken term = new LanguageToken(String.format("T%d", terms.size() + 1), TokenType.Term, new LanguageToken[]{prevToken, token, nextToken});
                        tokens.set(j, term);
                        tokenIndicesToRemove.add(j - 1);
                        tokenIndicesToRemove.add(j + 1);
                        terms.add(term);
                        foundOperator = true;
                        // Break early so that the token indices can be removed before evaluating the next operator
                        break;
                    }
                }
                Collections.reverse(tokenIndicesToRemove);
                for (int index : tokenIndicesToRemove) {
                    tokens.remove(index);
                }
            } while (foundOperator);
        }
        return terms;
    }
}
