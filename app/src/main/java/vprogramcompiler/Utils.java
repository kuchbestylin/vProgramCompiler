package vprogramcompiler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Utils {

    
    /**
    *
    * Performs syntax analysis on the input code and returns a boolean value indicating whether the code is syntactically valid or not.
    * @param input The input code to be analyzed.
    * @param tokens An ArrayList of LanguageToken objects representing the tokens in the input code.
    * @return A boolean value indicating whether the input code is syntactically valid or not.
    * @throws IllegalArgumentException if the input code is empty or the tokens list is null.
    * @throws IllegalStateException if an unexpected keyword is encountered or an invalid expression is derived.
    * The syntax analysis checks the following:
    * If the program starts with an identifier or a keyword
    * If two operators or two identifiers are used together
    * If the operator is used in the correct position
    * If any of these conditions are not met, an appropriate error message is printed and the method returns false.
    * The method also generates a queue of possible derivations of the input code, and adds them to the queue of derivations3.
    */    
    public boolean syntaxAnalysis(String input, ArrayList<LanguageToken> tokens, boolean is) {
        boolean isAllAtOnce = (is) ? true : false;
        String replacement = "";
        LanguageToken prevToken = new LanguageToken(replacement, null);
        Queue<String> derivations = new LinkedList<String>();
        Queue<String> derivations3 = new LinkedList<String>();
        int index = 0;
        for (var token : tokens) {
            LanguageToken nextToken = (index < tokens.size()-1) ? tokens.get(index+1) : null;
            if(index == 0 && (token.type != TokenType.Identifier && token.type != TokenType.Keyword)){
                System.out.println("Syntax error: Program can only start with identifier or keyword");
                System.out.println("Error Caused by: " + token.value);
                return false;
            }
            if(prevToken.type == token.type && token.type == TokenType.Operator){
                System.out.println("Syntax Error: Cannot use more than one operators together");
                System.out.println("Error caused by : " + prevToken.value  + token.value);
                return false;
            }
            if(isAllAtOnce && index < tokens.size()){
                if(prevToken.type == token.type && token.type == TokenType.Identifier && nextToken.type != TokenType.Operator){
                    System.out.println("Syntax Error: Cannot use more than one identifiers together");
                    System.out.println("Error caused by : " + prevToken.value  + token.value);
                    return false;
                }
            }
            else{
                if(prevToken.type == token.type && token.type == TokenType.Identifier){
                    System.out.println("Syntax Error: Cannot use more than one identifiers together");
                    System.out.println("Error caused by : " + prevToken.value  + token.value);
                    return false;
                }
            }
            if (index == 1 && token.type != TokenType.Operator){
                System.out.println("Syntax error: Invalid Use of operator");
                System.out.println("Error Caused by: " + prevToken.value + " " + token.value);
                return false;
            }
            if(token.type == TokenType.Identifier){
                replacement += "E";
                derivations3.add(" E" + token.value + " ");
                derivations.add(replacement);
            }
            else if(token.type == TokenType.Operator){
                if(token.value.equals("=")){
                    replacement += "  " + token.value + "> " ;
                }
                else
                    replacement += "  " + token.value + " " ;
            }
            else if(token.type == TokenType.Keyword){
                System.out.println("No grammatical or production rule specified for keyword: (" + token.value + ")");
                return false;
            }
            else{
                System.out.println("Cannot derive invalid expression: " +input );
                System.out.println("Error caused by: " + token.value + "(" + token.type + ")");
                return false;
            }
            prevToken = token;
            index++;
        }

        System.out.println("Derivation for vProgram [ " + input + " ] :\n");
        int b = 0;
        for (String derivation : derivations) {
            if(derivation.length() <= 1) continue;
            else{
                if(b == 0)
                    System.out.println("(R1)\t" + derivation);
                else
                    System.out.println("(R2)\t" + derivation);
            }
            b++;
        }

        Queue<String> derivations4 = new LinkedList<String>();
        String rep = "";
        int pos = 0;
        boolean isSymbol = false;
        for(int i = 0; i < tokens.size(); i++){
            for (var t : tokens){
                if(pos == i && t.type != TokenType.Identifier)
                    isSymbol = true;
                if (t.type == TokenType.Identifier && pos <= i){
                    rep += "E" + t.value + " ";
                }
                else if (t.type == TokenType.Identifier){
                    rep += "E ";
                }
                else{
                    if(t.value.equals("="))
                        rep += t.value + "> ";
                    else
                        rep += t.value + " ";
                }
                pos++;
            }
            if(!isSymbol)
                derivations4.add(rep);
            rep = "";
            isSymbol = false;
            pos = 0;
        }
        for(var x : derivations4){
            System.out.println("(R3)\t" + x);
        }


        Queue<String> derivations5 = new LinkedList<String>();
        String rep3 = "";
        int pos2 = 0;
        boolean isSymbol2 = false;
        for(int i = 0; i < tokens.size(); i++){
            for (var t : tokens){
                if(pos2 == i && t.type != TokenType.Identifier)
                    isSymbol2 = true;
                if (t.type == TokenType.Identifier && pos2 <= i){
                    rep3 += t.value + " ";
                }
                else if (t.type == TokenType.Identifier){
                    rep3 += "E" + t.value + " ";
                }
                else{
                    if(t.value.equals("="))
                        rep3 += " " + t.value + "> ";
                    else
                        rep3 += " " + t.value + " ";
                }
                pos2++;
            }
            if(!isSymbol2)
                derivations5.add(rep3);
            rep3 = "";
            isSymbol2 = false;
            pos2 = 0;
        }
        int last = 0;
        for(var x : derivations5){
            if (last == derivations5.size() - 1)
                System.out.println("(R4)\t" + x + "\t\t The Final String");
            else
                System.out.println("(R4)\t" + x);

            last++;
        }
        return true;
    }

    /**
    * 
    * Performs semantic analysis on the input code and returns a boolean value indicating whether the code is semantically valid or not.
    * @param input The input code to be analyzed.
    * @param tokens An ArrayList of LanguageToken objects representing the tokens in the input code.
    * @return A boolean value indicating whether the input code is semantically valid or not.
    * @throws IllegalArgumentException if the input code is empty or the tokens list is null.
    * @throws IllegalStateException if an invalid character is encountered or the input program is not long enough.
    * The semantic analysis checks the following:
    * If two operators or two identifiers are used together
    * If any invalid characters are used in the code
    * If the input program has at least 3 characters
    * If there is at least one operator in the program
    * If any of these conditions are not met, an appropriate error message is printed and the method returns false.
    * If the code passes all the checks, the method prints a success message and returns true.
    */
    public boolean semanticAnalysis(String input, ArrayList<LanguageToken> tokens, boolean x) {
        boolean isAllAtOnce = (x) ? true : false;
        ArrayList<String> errors = new ArrayList<>();
        boolean foundOperator = false;
        for (int i = 0; i < tokens.size(); i++) {
            LanguageToken currentTocken = tokens.get(i);
            LanguageToken nextToken = !(i < tokens.size()-1) ? null : tokens.get(i+1) ;
            if (i > 0) {
                LanguageToken previousTocken = tokens.get(i - 1);
                if (previousTocken.type == TokenType.Operator && currentTocken.type == TokenType.Operator)
                    errors.add("Semantic error: Use of two operators together i.e */, +*, *-, etc, not permitted");
                if(isAllAtOnce == true && i < tokens.size()){
                    if (previousTocken.type == TokenType.Identifier && currentTocken.type == TokenType.Identifier && nextToken.type != TokenType.Operator)
                    errors.add("Semantic error: Use ot 2 identifiers together i.e B C, not allowed");
                }
                else{
                    if (previousTocken.type == TokenType.Identifier && currentTocken.type == TokenType.Identifier)
                    errors.add("Semantic error: Use ot 2 identifiers together i.e B C, not allowed");
                }
            
            }
            if (currentTocken.type == TokenType.Invalid) {
                errors.add(String.format("Symantic error: Forbidden character used: '%s'. Only single numbers [0-9] and operators [/,*,+,-] are allowed.", currentTocken.value));
            }
            if (currentTocken.type == TokenType.Operator) {
                foundOperator = true;
            }
        }
        if (tokens.size() < 3) {
            errors.add("Semantic error: Input Program should be more that 2 characters!");
        }
        if (!foundOperator) {
            errors.add("Semantic error: There is no operator in the program supplied [+,/,-,*]");
        }
        if (errors.size() > 0) {
            for (var error : errors) {
                System.out.printf("- %s%n", error);
            }
            System.out.println("\nCONCLUSION --> This expression is not syntactically and semantically correct");
            return false;
        } else {
            System.out.println("vProgram: [ " + input + " ]");
            System.out.println("\nCONCLUSION --> This expression is syntactically and semantically correct");
            return true;
        }
    }
}
