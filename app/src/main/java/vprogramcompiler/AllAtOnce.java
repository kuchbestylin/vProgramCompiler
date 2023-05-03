/**
* We hereby acknowledge that the work handed in is our own original work. If we
* have quoted from any other source this information has been correctly referenced. 
* We also declare that we have read the Namibia University of Science and Technology
* Policies on Academic Honesty and Integrity as indicated in my course outline and 
* the NUST general information and regulations - Yearbook 2023
*
* @author <220130051> <KUCHERERA TAKUDZWA> 
* @author <221052380> <UJAVA VATIRAIJE> 
* @author <221128220> <HAIKALI MAX> 
* @author <221030670> <AMUTENYA REJOICE>
* @author <220039828> <KALIMBWE JOEL>
*/

package vprogramcompiler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;

public class AllAtOnce {
    public static void main(String[] args) throws FileNotFoundException {
        // C:\Projects\Resources\vprogram.txt
        System.out.print("Enter file path: ");
        String filePath = scanner.nextLine();
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return;
        }
        String vProgram = sb.toString();

        System.out.println("A MINI COMPILER PROJECT FOR CTE711S");


        System.out.println("\n\n\n\nSTAGE1: COMPILER TECHNIQUES--> LEXICAL ANALYSIS-Scanner");
        System.out.println("SYMBOL TABLE COMPRISING ATTRIBUTES AND TOKENS: ");
        final HashMap<String, String> assemblyOperators = new HashMap<>(){
            {put("/", "DIV");}{put("*", "MUL");}{put("+", "ADD");}{put("-", "SUB");}
        };
        Utils utils = new Utils();
        Shared shared = new Shared();
        ArrayList<LanguageToken> tokens = new ArrayList<>();
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
        for (int i = 0; i < tokens.size(); i++){
            var token = tokens.get(i);
            System.out.println("Tocken #" + i + " : " + token.value + " (" + token.type + ")");
        }

        System.out.println("\n\n\n\n======STAGE2: COMPILER TECHNIQUES--> SYNTAX ANALYSIS-Parser");
        System.err.println("GET A DERIVATION FOR: " + vProgram);
        utils.syntaxAnalysis(vProgram, tokens, true);
        
        System.out.println("\n\n\n\nSTAGE3: COMPILER TECHNIQUES--> SEMANTIC ANALYSIS");
        boolean res = utils.semanticAnalysis(vProgram, tokens,true);
        if (res == false){
            System.out.println("Compilation Terminated With Errors");
            System.exit(0);
        }

        System.out.println("\n\n\n\nSTAGE4: COMPILER TECHNIQUES--> INTERMEDIATE CODE");
        var terms = shared.extractTerms(vProgram, tokens);
        System.out.println("ICR (Using BEDMAS): \n");
        for(LanguageToken term : terms){
            System.out.print(" ");
            System.out.printf("%s = %s %s %s%n", term.value, term.values[0].value, term.values[1].value, term.values[2].value);
        }
        System.out.println("\n\nCONCLUSION --> The expression was correctly generated in ICR");

        System.out.println("\n\n\n\n======STAGE5: CODE GENERATION");
        System.out.println("\nWE USE: LDA, MUL, ADD, SUB, STR");
        System.out.println("vProgram: [ " + vProgram + " ]\n");
        for (var term : terms) {
            System.out.printf("LDA %s%n", term.values[0].value);
            System.out.printf("%s %s%n", assemblyOperators.get(term.values[1].value), term.values[2].value);
            System.out.printf("STR %s%n", term.value);
        }

        System.out.println("\n\n\n\n======STAGE6: CODE OPTIMISATION");
        System.out.println("\nWE USE: LDA, MUL, ADD, SUB, STR");
        System.out.println("vProgram: [ " + vProgram + " ]");
        System.out.println("C.O Output: \n");
        for (var term : terms) {
            System.out.print(" ");
            System.out.printf("%s %s, %s, %s%n", assemblyOperators.get(term.values[1].value), term.value, term.values[0].value, term.values[2].value);
        }

        System.out.println("\n\n\n\n======STAGE7: TARGET MACHINE CODE");
        int count = 0;
        for (var term : terms) {
            System.out.print(shared.convertStringToBinary(term.values[1].value) + "\t");
            count ++;
            if(count == 4){
                System.out.println();
                count = 0;
            }
        }

        System.out.println("\n\n======END OF COMPILATION");



    }
}
