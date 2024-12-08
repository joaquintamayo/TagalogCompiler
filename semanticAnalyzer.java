import java.util.HashMap;
import java.util.List;
import java.util.Map;

class semanticAnalyzer {
    private List<String> keywords;

    public semanticAnalyzer(List<String> keywords) {
        this.keywords = keywords;
    }

    public boolean analyze(List<Token> tokens) {
        Map<String, String> symbolTable = new HashMap<>();
        boolean valid = true;

        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);

            if (token.type.equals("Keyword") && keywords.contains(token.value)) {
                String type = token.value;
                if (i + 3 < tokens.size() && tokens.get(i + 1).type.equals("Identifier") &&
                    tokens.get(i + 2).value.equals("=") &&
                    tokens.get(i + 3).type.equals("Number")|| tokens.get(i + 3).type.equals("String") || tokens.get(i + 3).type.equals("Character")) {

                    String identifier = tokens.get(i + 1).value;
                    String value = tokens.get(i + 3).value;

                    if (symbolTable.containsKey(identifier)) {
                        System.out.println("Semantic Error: Variable '" + identifier + "' is already declared.");
                        valid = false;
                    } else if (!isValidValueForType(type, value)) {
                        System.out.println("Semantic Error: Type mismatch, value '" + value + "' is not of type '" + type + "'.");
                        valid = false;
                    } else {
                        symbolTable.put(identifier, type);
                    }
                    i += 4; 
                } else {
                    System.out.println("Semantic Error: Invalid assignment or declaration.");
                    valid = false;
                }
            } else if (token.type.equals("Keyword") && token.value.equals("kung")) {
                if (i + 5 < tokens.size() && tokens.get(i + 1).value.equals("(") &&
                    tokens.get(i + 2).type.equals("Identifier") &&
                    tokens.get(i + 3).type.equals("Operator") &&
                    tokens.get(i + 4).type.equals("Number") &&
                    tokens.get(i + 5).value.equals(")")) {

                    String variableName = tokens.get(i + 2).value;
                    if (!symbolTable.containsKey(variableName)) {
                        System.out.println("Semantic Error: Variable '" + variableName + "' is not declared.");
                        valid = false;
                    }
                    i += 6;
                } else {
                    System.out.println("Semantic Error: Invalid conditional statement.");
                    valid = false;
                }
            } else {
                i++;
            }
        }
        return valid;
    }

    private static boolean isValidValueForType(String type, String value) {
        switch (type) {
            case "bilang": return value.matches("\\d+");
            case "lutang": return value.matches("\\d+(\\.\\d+)?");
            case "doble": return value.matches("\\d+(\\.\\d+)?");
            case "karakter": return value.matches("'.'");
            case "booleano": return value.equals("true") || value.equals("false");
            case "tali": return value.startsWith("\"") && value.endsWith("\"");
            default: return false;
        }
    }
}
