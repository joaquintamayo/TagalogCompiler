import java.util.*;
import java.util.regex.*;

public class lexicalAnalyzer {
    private static final String KEYWORD_PATTERN = "(bayt|maikli|bilang|mahaba|karakter|doble|lutang|booleano|tali|kung)";
    private static final String IDENTIFIER_PATTERN = "[a-zA-Z_][a-zA-Z0-9_]*";
    private static final String NUMBER_PATTERN = "\\b\\d+(\\.\\d+)?\\b";
    private static final String STRING_PATTERN = "\"(\\\\.|[^\"])*\""; 
    private static final String CHARACTER_PATTERN = "'(\\\\.|[^'])'"; 
    private static final String OPERATOR_PATTERN = "[+\\-*/=<>!(){};]";
    private static final String WHITESPACE_PATTERN = "\\s+";
    private static final String COMMENT_PATTERN = "(//.*|/\\*.*?\\*/)";

    public List<Token> analyze(String code) {
        List<Token> tokens = new ArrayList<>();
        Pattern pattern = Pattern.compile(
            COMMENT_PATTERN + "|" +
            STRING_PATTERN + "|" +  // Added string pattern
            CHARACTER_PATTERN + "|" +  // Added character pattern
            KEYWORD_PATTERN + "|" +
            IDENTIFIER_PATTERN + "|" +
            NUMBER_PATTERN + "|" +
            OPERATOR_PATTERN + "|" +
            WHITESPACE_PATTERN
        );
        Matcher matcher = pattern.matcher(code);

        while (matcher.find()) {
            String token = matcher.group();
            if (token.matches(COMMENT_PATTERN)) continue;
            else if (token.matches(STRING_PATTERN)) tokens.add(new Token("String", token)); // Match strings
            else if (token.matches(CHARACTER_PATTERN)) tokens.add(new Token("Character", token)); // Match characters
            else if (token.matches(KEYWORD_PATTERN)) tokens.add(new Token("Keyword", token));
            else if (token.matches(IDENTIFIER_PATTERN)) tokens.add(new Token("Identifier", token));
            else if (token.matches(NUMBER_PATTERN)) tokens.add(new Token("Number", token));
            else if (token.matches(OPERATOR_PATTERN)) tokens.add(new Token("Operator", token));
        }
        return tokens;
    }

    public List<String> getKeywords() {
        List<String> keywords = new ArrayList<>();
        String[] keywordArray = KEYWORD_PATTERN.split("\\|");
        keywords.addAll(Arrays.asList(keywordArray));
        return keywords;
    }
}