package stone.lang;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.regex.*;

public class Lexer {
    public static String regexPattern =
            "\\s*((//.*)|([0-9]+)|(\"(\\\\\"|\\\\\\\\|\\\\n|[^\"])*\")"
                    + "|[A-Za-z][A-Za-z0-9]*|==|<=|>=|&&|\\|\\||\\p{Punct})?";
    private Pattern pattern = Pattern.compile(regexPattern);
    private ArrayList<Token> queue = new ArrayList<>();
    private boolean hasMore;
    private LineNumberReader reader;

    public Lexer(Reader reader) {
        hasMore = true;
        this.reader = new LineNumberReader(reader);
    }

    public Token read() throws ParseException {
        if (fillQueue(0))
            return queue.remove(0);
        else
            return Token.EOF;
    }

    public Token peek(int i) throws ParseException {
        if (fillQueue(i))
            return queue.get(i);
        else
            return  Token.EOF;
    }

    private boolean fillQueue(int i) throws ParseException {
        while (i >= queue.size()) {
            if (hasMore)
                readLine();
            else
                return false;
        }
        return true;
    }

    protected void readLine() throws ParseException {
        String line;
        try {
            line = reader.readLine();
        } catch (IOException e) {
            throw new ParseException(e);
        }
        if (line == null) {
            hasMore = false;
            return;
        }
        int lineNumber = reader.getLineNumber();
        Matcher matcher = pattern.matcher(line);
        matcher.useTransparentBounds(true).useAnchoringBounds(false);
        int position = 0;
        int endPosition = line.length();
        while (position < endPosition) {
            matcher.region(position, endPosition);
            if (matcher.lookingAt()) {
                addToken(lineNumber, matcher);
                position = matcher.end();
            }
            else {
                throw new ParseException("bad token at line " + lineNumber);
            }
        }
        queue.add(new IdentifierToken(lineNumber, Token.EOL));
    }

    private void addToken(int lineNumber, Matcher matcher) {
        String m = matcher.group(1);
        if (m != null) {
            if (matcher.group(2) == null) {
                Token token;
                if (matcher.group(3) != null)
                    token = new NumberToken(lineNumber, Integer.parseInt(m));
                else if (matcher.group(4) != null)
                    token = new StringToken(lineNumber, toStringLiteral(m));
                else
                    token = new IdentifierToken(lineNumber, m);
                queue.add(token);
            }
        }
    }

    protected String toStringLiteral(String source) {
        StringBuilder builder = new StringBuilder();
        int length = source.length() - 1;
        for (int i = 1; i < length; i++) {
            char c = source.charAt(i);
            if (c == '\\' && i + 1 < length) {
                int next = source.charAt(i + 1);
                if (next == 0 || next == '\\') //TODO: Next check to be verified.
                    c = source.charAt(++i);
                else if (next == 'n') {
                    ++i;
                    c = '\n';
                }
            }
            builder.append(c);
        }
        return builder.toString();
    }

    protected static class NumberToken extends Token {
        private int value;

        protected NumberToken(int line, int value) {
            super(line);
            this.value = value;
        }

        public boolean isNumber() { return true; }
        public String getText() { return Integer.toString(value); }
        public int getNumber() { return value; }
    }

    protected  static class StringToken extends Token {
        private String literal;
        public StringToken(int line, String string) {
            super(line);
            literal = string;
        }

        public boolean isString() { return true; }
        public String getLiteral() { return literal; }
    }

    protected static class IdentifierToken extends Token {
        private String text;
        protected IdentifierToken(int line, String identifier) {
            super(line);
            text = identifier;
        }

        public  boolean isIdentifier() { return true; }
        public  String getText() { return text; }
    }
}
