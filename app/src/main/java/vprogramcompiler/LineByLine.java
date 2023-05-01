/**
* We hereby acknowledge that the work handed in is our own original work. If we
* have quoted from any other source this information has been correctly referenced. 
* We also declare that we have read the Namibia University of Scienceand Technology
* Policies on Academic Honesty and Integrity as indicated in my courseoutline and 
* the NUST general information and regulations - Yearbook 2023
*
* @author <220130051> <KUCHERERA TAKUDZWA> 
* @author <221052380> <UJAVA VATIRAIJE> 
* @author <221128220> <HAIKALI MAX> 
* @author <221030670> <AMUTENYA REJOICE>
* @author <220039828> <KALIMBWE JOEL>
*/
package vprogramcompiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;

public class LineByLine {

    public static void main(String[] args) {
        final HashMap<String, String> assemblyOperators = new HashMap<>(){
            {put("/", "DIV");}{put("*", "MUL");}{put("+", "ADD");}{put("-", "SUB");}
        };
        Utils utils = new Utils();
        Shared shared = new Shared();
        ArrayList<LanguageToken> tokens = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter program length (lines): ");
        int vProgramLength = sc.nextInt();
        String[] lines = new String[vProgramLength];

        for (int i = 0; i < vProgramLength; i++) {
            System.out.print("Enter String: ");
            lines[i] = new Scanner(System.in).nextLine();
        }
        sc.close();

        /*
         * This loop will iterate over all the lines of vProgram passed on through user input. It will tockenise
         * each program using the StringTockeniser class and store the token along with the token type i.e
         * <strong>identifier</strong> for the latter stages of the compilation process.
         */
        int programLine = 0;
        for (String vProgram : lines){
            programLine++;
            StringTokenizer st = new StringTokenizer(vProgram, " \t\n\r\f()+-/*=;,", true);
            while (st.hasMoreTokens()) {
                String token = st.nextToken();
                TokenType tokenType;
    
                if (token.equals("") || token.equals(" ") || token.equals("\n"))
                    continue;
    
                else if (shared.isKeyword(token)) 
                    tokenType = TokenType.Keyword;
    
                else if (shared.validIdentifier(token)) 
                    tokenType = TokenType.Identifier;
    
                else if (shared.isOperator(token.charAt(0))) 
                    tokenType = TokenType.Operator;
    
                else if (shared.isDelimiter(token.charAt(0))) 
                    tokenType = TokenType.Symbol;
    
                else 
                    tokenType = TokenType.Invalid;
    
                tokens.add(new LanguageToken(token, tokenType));
    
            }
    
            System.out.println("\n\n===================== Compilation for Program " + programLine + ": [" + vProgram + "] =====================");
            System.out.println("\n\n\n===================== STAGE 1: LEXICAL ANALYSIS (SCANNING) =====================");
            System.out.println("\nSymbol Table:\n");
            for (int i = 0; i < tokens.size(); i++){
                var token = tokens.get(i);
                System.out.println("Tocken #" + i + " : " + token.value + " (" + token.type + ")");
            }
    
            System.out.println("\nTotal number of tockens: " + tokens.size());
    
            System.out.println("\n===================== STAGE 2: SYNTAX ANALYSIS (PARSING) =====================\n");
            boolean derivationValid = utils.syntaxAnalysis(vProgram, tokens, false);
            if(!derivationValid)
                continue;
    
            System.out.println("\n===================== STAGE 3: SEMANTIC ANALYSIS =====================\n");
            utils.semanticAnalysis(vProgram, tokens,false);
    
            System.out.println("\n===================== STAGE 4: INTERMEDIATE CODE REPRESENTATION (ICR) =====================\n");
            var terms = shared.extractTerms(vProgram, tokens);
            System.out.println("vProgram: [ " + vProgram + " ]");
            System.out.println("ICR (Using BEDMAS): \n");
            for(LanguageToken term : terms){
                System.out.print(" ");
                System.out.printf("%s = %s %s %s%n", term.value, term.values[0].value, term.values[1].value, term.values[2].value);
            }
            System.out.println("\nCONCLUSION --> The expression was correctly generated in ICR");
    
            System.out.println("\n===================== STAGE 5: CODE GENERATION =====================");
            System.out.println("\nWE USE: LDA, MUL, ADD, SUB, STR");
            System.out.println("vProgram: [ " + vProgram + " ]\n");
            for (var term : terms) {
                System.out.printf("LDA %s%n", term.values[0].value);
                System.out.printf("%s %s%n", assemblyOperators.get(term.values[1].value), term.values[2].value);
                System.out.printf("STR %s%n", term.value);
            }
            System.out.println("\nCONCLUSION --> The expression was correctly generated as an ASSEMBLY");
            System.out.println("\n===================== STAGE 6: CODE OPTIMISATION =====================");
            System.out.println("\nWE USE: LDA, MUL, ADD, SUB, STR");
            System.out.println("vProgram: [ " + vProgram + " ]");
            System.out.println("C.O Output: \n");
            for (var term : terms) {
                System.out.print(" ");
                System.out.printf("%s %s, %s, %s%n", assemblyOperators.get(term.values[1].value), term.value, term.values[0].value, term.values[2].value);
            }
        
            System.out.println("\n===================== STAGE 7: TARGET MACHINE CODE =====================\n");
            int count = 0;
            for (var term : terms) {
                System.out.print(shared.convertStringToBinary(term.values[1].value) + "\t");
                count ++;
                if(count == 4){
                    System.out.println();
                    count = 0;
                }
            }

            System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n");
        }


        System.out.println("\n\n\n\n\n----------------------------------------------------------------------");
        System.out.println("\n===================== BUILD SUCCEEDED ================================");   
        System.out.println("\n----------------------------------------------------------------------");
    }
}
