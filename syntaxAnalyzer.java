import java.util.List;

class syntaxAnalyzer {
    private List<String> keywords;

    public syntaxAnalyzer(List<String> keywords) {
        this.keywords = keywords;
    }

    public boolean analyze(List<Token> tokens) {
        int i = 0;
        while (i < tokens.size()) {
            Token token = tokens.get(i);

            if (token.type.equals("Keyword") && keywords.contains(token.value)) {
                if (i + 3 < tokens.size() &&
                    tokens.get(i + 1).type.equals("Identifier") &&
                    tokens.get(i + 2).value.equals("=") &&
                    (tokens.get(i + 3).type.equals("Number") || 
                     tokens.get(i + 3).type.equals("Identifier") || 
                     tokens.get(i + 3).type.equals("String") || 
                     tokens.get(i + 3).type.equals("Character"))) {
                    i += 4; // Skip processed tokens
                } else {
                    System.out.println("Syntax Error: Invalid assignment or declaration.");
                    return false;
                }
            } else if (token.type.equals("Keyword") && token.value.equals("kung")) {
                if (i + 5 < tokens.size() &&
                    tokens.get(i + 1).value.equals("(") &&
                    tokens.get(i + 2).type.equals("Identifier") &&
                    tokens.get(i + 3).type.equals("Operator") &&
                    tokens.get(i + 4).type.equals("Number") &&
                    tokens.get(i + 5).value.equals(")")) {
                    i += 6;
                } else {
                    System.out.println("Syntax Error: Invalid conditional structure.");
                    return false;
                }
            } else {
                i++;
            }
        }
        return true;
    }
}