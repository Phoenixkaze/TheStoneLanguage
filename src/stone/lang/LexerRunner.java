package stone.lang;

public class LexerRunner {
    public static void main(String[] args) throws ParseException {
        Lexer lexer = new Lexer(new CodeDialog());
        for (Token token; (token = lexer.read()) != Token.EOF;)
            System.out.println("=>" + token.getText());
    }
}
