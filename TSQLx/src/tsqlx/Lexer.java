package tsqlx;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
	public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
	public static final String ANSI_RESET = "\u001B[0m";

	public enum TokenType {
		// Token types cannot have underscores
		// Add more token type as necessary, Just make sure ID and Error are at
		// the end
		// Dont forget match condition in the lex() method
		WHITESPACE("[ \t\f\r\n]+"), LPAREN("\\("), RPAREN("\\)"), CREATE("CREATE"), DROP("DROP"), DATABASE(
				"DATABASE"), SAVE("SAVE"), LOAD("LOAD"), COMMA(","), TABLE("TABLE"), INSERT("INSERT"), CONVERT(
						"CONVERT"), INPUT("INPUT"), DELETE("DELETE"), FROM("FROM"), INTO("INTO"), VALUES("VALUES"), AS(
								"AS"), ASTK("\\*"), SELECT("SELECT"), TSELECT("TSELECT|tselect"), RELOP(
										"<=|>=|!=|=|<|>"), WHERE("WHERE"), STRING("Character|CHAR"), DECIMAL(
												"(\\d+\\.\\d+)"), INTEGER("INT"), DEC("DECIMAL|decimal"), STRLITERAL(
														"'(.*?)'"), LBRACK("\\["), RBRACK("\\]"), NOTNULL(
																"NOTNULL|notnull"), XSD("XSD|xsd"), XML("XML|xml"), DOT(
																		"\\."), TXT("TXT|txt"), SEMICOLON(";"), DATE(
																				"(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])-([19|20]?\\d{2,})"), NUMBER(
																						"-?[0-9]+"), ID(
																								"\\p{Alpha}+[[_]?\\p{Alnum}]?"), ERROR(
																										".+"); // ID("[\\w]+_"),

		public final String pattern;

		private TokenType(String pattern) {
			this.pattern = pattern;
		}
	}

	public static class Token {
		public TokenType type;
		public String data;

		public Token(TokenType type, String data) {
			if (type == TokenType.STRLITERAL) {
				this.data = data.replaceAll("^'|'$", "");

				this.type = type;
			} else {
				this.type = type;
				this.data = data;
			}
		}

		@Override
		public String toString() {
			return String.format("(%s %s)", type.name(), data);
		}
	}

	public static ArrayList<Token> lex(String input) {
		// The tokens to return
		ArrayList<Token> tokens = new ArrayList<Token>();

		// Lexer logic begins here
		StringBuffer tokenPatternsBuffer = new StringBuffer();
		for (TokenType tokenType : TokenType.values())
			tokenPatternsBuffer.append(String.format("|(?<%s>%s)", tokenType.name(), tokenType.pattern));
		Pattern tokenPatterns = Pattern.compile(new String(tokenPatternsBuffer.substring(1)), Pattern.CASE_INSENSITIVE);

		// Begin matching tokens
		Matcher matcher = tokenPatterns.matcher(input);
		int strIndex = 0;
		while (matcher.find()) {
			if (matcher.group(TokenType.COMMA.name()) != null) {
				tokens.add(new Token(TokenType.COMMA, matcher.group(TokenType.COMMA.name())));
				strIndex = matcher.end();
				continue;

			} else if (matcher.group(TokenType.WHITESPACE.name()) != null) {
				// tokens.add(new Token(TokenType.WHITESPACE,
				// matcher.group(TokenType.WHITESPACE.name())));
				strIndex = matcher.end();
				continue;
			} else if (matcher.group(TokenType.NOTNULL.name()) != null) {
				tokens.add(new Token(TokenType.NOTNULL, matcher.group(TokenType.NOTNULL.name())));
				strIndex = matcher.end();
				continue;
			} else if (matcher.group(TokenType.DATE.name()) != null) {
				tokens.add(new Token(TokenType.DATE, matcher.group(TokenType.DATE.name())));
				strIndex = matcher.end();
				continue;
			} else if (matcher.group(TokenType.NUMBER.name()) != null) {
				tokens.add(new Token(TokenType.NUMBER, matcher.group(TokenType.NUMBER.name())));
				strIndex = matcher.end();
				continue;
			} else if (matcher.group(TokenType.CONVERT.name()) != null) {
				tokens.add(new Token(TokenType.CONVERT, matcher.group(TokenType.CONVERT.name())));
				strIndex = matcher.end();
				continue;
			} else if (matcher.group(TokenType.CREATE.name()) != null) {
				tokens.add(new Token(TokenType.CREATE, matcher.group(TokenType.CREATE.name())));
				strIndex = matcher.end();
				continue;
			} else if (matcher.group(TokenType.DATABASE.name()) != null) {
				tokens.add(new Token(TokenType.DATABASE, matcher.group(TokenType.DATABASE.name())));
				strIndex = matcher.end();
				continue;
			} else if (matcher.group(TokenType.DELETE.name()) != null) {
				tokens.add(new Token(TokenType.DELETE, matcher.group(TokenType.DELETE.name())));
				strIndex = matcher.end();
				continue;
			} else if (matcher.group(TokenType.DROP.name()) != null) {
				tokens.add(new Token(TokenType.DROP, matcher.group(TokenType.DROP.name())));
				strIndex = matcher.end();
				continue;
			} else if (matcher.group(TokenType.FROM.name()) != null) {
				tokens.add(new Token(TokenType.FROM, matcher.group(TokenType.FROM.name())));
				strIndex = matcher.end();
				continue;
			} else if (matcher.group(TokenType.INPUT.name()) != null) {
				tokens.add(new Token(TokenType.INPUT, matcher.group(TokenType.INPUT.name())));
				strIndex = matcher.end();
				continue;
			} else if (matcher.group(TokenType.INSERT.name()) != null) {
				tokens.add(new Token(TokenType.INSERT, matcher.group(TokenType.INSERT.name())));
				strIndex = matcher.end();
				continue;
			} else if (matcher.group(TokenType.INTO.name()) != null) {
				tokens.add(new Token(TokenType.INTO, matcher.group(TokenType.INTO.name())));
				strIndex = matcher.end();
				continue;
			} else if (matcher.group(TokenType.LPAREN.name()) != null) {
				tokens.add(new Token(TokenType.LPAREN, matcher.group(TokenType.LPAREN.name())));
				strIndex = matcher.end();
				continue;
			} else if (matcher.group(TokenType.RPAREN.name()) != null) {
				tokens.add(new Token(TokenType.RPAREN, matcher.group(TokenType.RPAREN.name())));
				strIndex = matcher.end();
				continue;
			} else if (matcher.group(TokenType.LOAD.name()) != null) {
				tokens.add(new Token(TokenType.LOAD, matcher.group(TokenType.LOAD.name())));
				strIndex = matcher.end();
				continue;
			} else if (matcher.group(TokenType.SAVE.name()) != null) {
				tokens.add(new Token(TokenType.SAVE, matcher.group(TokenType.SAVE.name())));
				strIndex = matcher.end();
				continue;
			} else if (matcher.group(TokenType.SELECT.name()) != null) {
				tokens.add(new Token(TokenType.SELECT, matcher.group(TokenType.SELECT.name())));
				strIndex = matcher.end();
				continue;
			} else if (matcher.group(TokenType.TABLE.name()) != null) {
				tokens.add(new Token(TokenType.TABLE, matcher.group(TokenType.TABLE.name())));
				strIndex = matcher.end();
				continue;
			} else if (matcher.group(TokenType.TSELECT.name()) != null) {
				tokens.add(new Token(TokenType.TSELECT, matcher.group(TokenType.TSELECT.name())));
				strIndex = matcher.end();
				continue;
			} else if (matcher.group(TokenType.VALUES.name()) != null) {
				tokens.add(new Token(TokenType.VALUES, matcher.group(TokenType.VALUES.name())));
				strIndex = matcher.end();
				continue;
			} else if (matcher.group(TokenType.RELOP.name()) != null) {
				tokens.add(new Token(TokenType.RELOP, matcher.group(TokenType.RELOP.name())));
				strIndex = matcher.end();
				continue;
			} else if (matcher.group(TokenType.WHERE.name()) != null) {
				tokens.add(new Token(TokenType.WHERE, matcher.group(TokenType.WHERE.name())));
				strIndex = matcher.end();
				continue;
			} else if (matcher.group(TokenType.STRING.name()) != null) {
				tokens.add(new Token(TokenType.STRING, matcher.group(TokenType.STRING.name())));
				strIndex = matcher.end();
				continue;
			} else if (matcher.group(TokenType.INTEGER.name()) != null) {
				tokens.add(new Token(TokenType.INTEGER, matcher.group(TokenType.INTEGER.name())));
				strIndex = matcher.end();
				continue;
			} else if (matcher.group(TokenType.DEC.name()) != null) {
				tokens.add(new Token(TokenType.DEC, matcher.group(TokenType.DEC.name())));
				strIndex = matcher.end();
				continue;
			} else if (matcher.group(TokenType.STRLITERAL.name()) != null) {
				tokens.add(new Token(TokenType.STRLITERAL, matcher.group(TokenType.STRLITERAL.name())));
				strIndex = matcher.end();
				continue;
			} else if (matcher.group(TokenType.DECIMAL.name()) != null) {
				tokens.add(new Token(TokenType.DECIMAL, matcher.group(TokenType.DECIMAL.name())));
				strIndex = matcher.end();
				continue;

			} else if (matcher.group(TokenType.LBRACK.name()) != null) {
				tokens.add(new Token(TokenType.LBRACK, matcher.group(TokenType.LBRACK.name())));
				strIndex = matcher.end();
				continue;

			} else if (matcher.group(TokenType.RBRACK.name()) != null) {
				tokens.add(new Token(TokenType.RBRACK, matcher.group(TokenType.RBRACK.name())));
				strIndex = matcher.end();
				continue;

			} else if (matcher.group(TokenType.XSD.name()) != null) {
				tokens.add(new Token(TokenType.XSD, matcher.group(TokenType.XSD.name())));
				strIndex = matcher.end();
				continue;
			} else if (matcher.group(TokenType.XML.name()) != null) {
				tokens.add(new Token(TokenType.XML, matcher.group(TokenType.XML.name())));
				strIndex = matcher.end();
				continue;
			} else if (matcher.group(TokenType.DOT.name()) != null) {
				tokens.add(new Token(TokenType.DOT, matcher.group(TokenType.DOT.name())));
				strIndex = matcher.end();
				continue;
			} else if (matcher.group(TokenType.SEMICOLON.name()) != null) {
				tokens.add(new Token(TokenType.SEMICOLON, matcher.group(TokenType.SEMICOLON.name())));
				strIndex = matcher.end();
				continue;
			} else if (matcher.group(TokenType.ID.name()) != null) {
				tokens.add(new Token(TokenType.ID, matcher.group(TokenType.ID.name())));
				strIndex = matcher.end();
				continue;
			} else if (matcher.group(TokenType.ASTK.name()) != null) {
				tokens.add(new Token(TokenType.ASTK, matcher.group(TokenType.ASTK.name())));
				strIndex = matcher.end();
				continue;
			} else if (matcher.group(TokenType.ERROR.name()) != null) {
				System.out.println(ANSI_RED_BACKGROUND + "Error @ index--->"+ (strIndex + 1) + ANSI_RESET +"---> "
						+ matcher.group(TokenType.ERROR.name()));
				continue;
			}
		}

		return tokens;
	}

}