import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

public class CodeAnalyzerGUI extends JFrame {
    private JTextArea codeArea;
    private JTextArea resultArea;
    private JButton openFileButton;
    private JButton lexicalButton;
    private JButton syntaxButton;
    private JButton semanticButton;
    private JButton clearButton;
    private File selectedFile;
    private List<Token> tokens;

    public CodeAnalyzerGUI() {
        setTitle("Tagalog Compiler");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 1, 5, 5));

        openFileButton = new JButton("Open File");
        lexicalButton = new JButton("Lexical Analysis");
        syntaxButton = new JButton("Syntax Analysis");
        semanticButton = new JButton("Semantic Analysis");
        clearButton = new JButton("Clear");

        openFileButton.setBackground(Color.BLUE);
        lexicalButton.setBackground(Color.RED);
        syntaxButton.setBackground(Color.YELLOW);
        semanticButton.setBackground(Color.BLUE);
        clearButton.setBackground(Color.RED);

        openFileButton.setForeground(Color.WHITE);
        lexicalButton.setForeground(Color.WHITE);
        syntaxButton.setForeground(Color.BLACK);
        semanticButton.setForeground(Color.WHITE);
        clearButton.setForeground(Color.WHITE);

        Font buttonFont = new Font("Arial", Font.BOLD, 14);
        openFileButton.setFont(buttonFont);
        lexicalButton.setFont(buttonFont);
        syntaxButton.setFont(buttonFont);
        semanticButton.setFont(buttonFont);
        clearButton.setFont(buttonFont);

        buttonPanel.add(openFileButton);
        buttonPanel.add(lexicalButton);
        buttonPanel.add(syntaxButton);
        buttonPanel.add(semanticButton);
        buttonPanel.add(clearButton);

        JPanel textAreaPanel = new JPanel();
        textAreaPanel.setLayout(new BorderLayout());

        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBorder(BorderFactory.createTitledBorder("Results"));
        resultArea = new JTextArea(5, 50);
        resultArea.setEditable(false);
        JScrollPane resultScrollPane = new JScrollPane(resultArea);
        resultPanel.add(resultScrollPane, BorderLayout.CENTER);

        JPanel codePanel = new JPanel(new BorderLayout());
        codePanel.setBorder(BorderFactory.createTitledBorder("Code Input"));
        codeArea = new JTextArea(15, 50);
        codeArea.setEditable(false);
        JScrollPane codeScrollPane = new JScrollPane(codeArea);
        codePanel.add(codeScrollPane, BorderLayout.CENTER);

        textAreaPanel.add(resultPanel, BorderLayout.NORTH);
        textAreaPanel.add(codePanel, BorderLayout.CENTER);

        mainPanel.add(buttonPanel, BorderLayout.WEST);
        mainPanel.add(textAreaPanel, BorderLayout.CENTER);

        add(mainPanel);

        openFileButton.addActionListener(new OpenFileButtonListener());
        lexicalButton.addActionListener(new LexicalButtonListener());
        syntaxButton.addActionListener(new SyntaxButtonListener());
        semanticButton.addActionListener(new SemanticButtonListener());
        clearButton.addActionListener(new ClearButtonListener());

        updateButtonStates();
        setVisible(true);
    }

    private void updateButtonStates() {
        boolean hasCode = selectedFile != null && !codeArea.getText().trim().isEmpty();
        lexicalButton.setEnabled(hasCode);
        syntaxButton.setEnabled(hasCode && tokens != null && !tokens.isEmpty());
        semanticButton.setEnabled(hasCode && tokens != null && !tokens.isEmpty());
    }

    private class OpenFileButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(CodeAnalyzerGUI.this);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();
                try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
                    codeArea.setText("");
                    String line;
                    while ((line = reader.readLine()) != null) {
                        codeArea.append(line + "\n");
                    }
                    resultArea.setText("File loaded: " + selectedFile.getName() + "\n");
                } catch (Exception ex) {
                    resultArea.setText("Error loading file: " + ex.getMessage());
                }
                updateButtonStates();
            }
        }
    }

    private class LexicalButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (codeArea.getText().isEmpty()) {
                resultArea.setText("Code area is empty. Please load or input code.");
                return;
            }
            try {
                lexicalAnalyzer lexicalAnalyzer = new lexicalAnalyzer();
                tokens = lexicalAnalyzer.analyze(codeArea.getText());
                resultArea.setText("Lexical Analysis Output:\n");
                for (Token token : tokens) {
                    resultArea.append(token + "\n");
                }
                updateButtonStates();
            } catch (Exception ex) {
                resultArea.setText("Error during lexical analysis: " + ex.getMessage());
            }
        }
    }

    private class SyntaxButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (tokens == null || tokens.isEmpty()) {
                resultArea.setText("No tokens found. Perform lexical analysis first.");
                return;
            }
            try {
                lexicalAnalyzer lexer = new lexicalAnalyzer();
                List<String> keywords = lexer.getKeywords();

                syntaxAnalyzer synAnalyzer = new syntaxAnalyzer(keywords);
                boolean syntaxCorrect = synAnalyzer.analyze(tokens);

                resultArea.setText("Syntax Analysis Output:\n");
                resultArea.append(syntaxCorrect ? "Syntax is correct.\n" : "Syntax error detected.\n");
            } catch (Exception ex) {
                resultArea.setText("Error during syntax analysis: " + ex.getMessage());
            }
        }
    }

    private class SemanticButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (tokens == null || tokens.isEmpty()) {
                resultArea.setText("No tokens found. Perform lexical analysis first.");
                return;
            }
            try {
                lexicalAnalyzer lexer = new lexicalAnalyzer();
                List<String> keywords = lexer.getKeywords();

                semanticAnalyzer semAnalyzer = new semanticAnalyzer(keywords);
                boolean semanticCorrect = semAnalyzer.analyze(tokens);

                resultArea.setText("Semantic Analysis Output:\n");
                resultArea.append(semanticCorrect ? "Semantics are correct.\n" : "Semantic error detected.\n");
            } catch (Exception ex) {
                resultArea.setText("Error during semantic analysis: " + ex.getMessage());
            }
        }
    }

    private class ClearButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            codeArea.setText("");
            resultArea.setText("");
            tokens = null;
            selectedFile = null;
            updateButtonStates();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CodeAnalyzerGUI::new);
    }
}