package stone.lang;

import java.io.IOException;

public class ParseException extends Exception {
    public ParseException(Token token) {

    }

    public  ParseException(String info, Token token) {

    }

    public ParseException(IOException e) {
        super(e);
    }

    public  ParseException(String info) {
        super(info);
    }

    private static String location(Token token) {
        if (token == Token.EOF)
            return "the last line";
        else
            return "\"" + token.getText() + "\" at line " + token.getLineNumber();
    }
}
