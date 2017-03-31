import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



	public class Lexer{
		
			
		
			public enum TokenType {
				// Token types cannot have underscores
				// Add more token type as necessary
				NUMBER("-?[0-9]+"), WHITESPACE("[ \t\f\r\n]+"), LPAREN("\\("), RPAREN("\\)"),
				CREATE("CREATE|create"), DROP("DROP|drop"), DATABASE("DATABASE|database"), SAVE("SAVE|save"),
				LOAD("LOAD|load"),COMMA(","), TABLE("TABLE|table"), INSERT("INSERT|insert"),
				CONVERT("CONVERT|convert"), INPUT("INPUT|input"), DELETE("DELETE|delete"),
				FROM("FROM|from"), INTO("INTO|into"), VALUES("VALUES|values"), AS("AS|as"),
				ASTK("\\*"), SELECT("SELECT|select"), TSELECT("TSELECT|tselect"), ID("[\\w]+"), ERROR(".+");
				
				public final String pattern;

				private TokenType(String pattern) {
					this.pattern = pattern;
				}
			}

			public  static class Token {
				public TokenType type;
				public String data;

				public Token(TokenType type, String data) {
					this.type = type;
					this.data = data;
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
				Pattern tokenPatterns = Pattern.compile(new String(tokenPatternsBuffer.substring(1)));

				// Begin matching tokens
				Matcher matcher = tokenPatterns.matcher(input);
				while (matcher.find()) {
					if (matcher.group(TokenType.NUMBER.name()) != null) {
						tokens.add(new Token(TokenType.NUMBER, matcher.group(TokenType.NUMBER.name())));
						continue;
					} else if (matcher.group(TokenType.WHITESPACE.name()) != null){
						//tokens.add(new Token(TokenType.WHITESPACE, matcher.group(TokenType.WHITESPACE.name())));
						continue; 
					}else if (matcher.group(TokenType.COMMA.name()) != null){
						tokens.add(new Token(TokenType.COMMA, matcher.group(TokenType.COMMA.name())));
						
						continue; 
					}else if (matcher.group(TokenType.CONVERT.name()) != null){
						tokens.add(new Token(TokenType.CONVERT, matcher.group(TokenType.CONVERT.name())));
						continue; 
					}else if (matcher.group(TokenType.CREATE.name()) != null){
						tokens.add(new Token(TokenType.CREATE, matcher.group(TokenType.CREATE.name())));
						continue; 
					}else if (matcher.group(TokenType.DATABASE.name()) != null){
						tokens.add(new Token(TokenType.DATABASE, matcher.group(TokenType.DATABASE.name())));
						continue; 
					}else if (matcher.group(TokenType.DELETE.name()) != null){
						tokens.add(new Token(TokenType.DELETE, matcher.group(TokenType.DELETE.name())));
						continue; 
					}else if (matcher.group(TokenType.DROP.name()) != null){
						tokens.add(new Token(TokenType.DROP, matcher.group(TokenType.DROP.name())));
						continue; 
					}else if (matcher.group(TokenType.FROM.name()) != null){
						tokens.add(new Token(TokenType.FROM, matcher.group(TokenType.FROM.name())));
						continue; 
					}else if (matcher.group(TokenType.INPUT.name()) != null){
						tokens.add(new Token(TokenType.INPUT, matcher.group(TokenType.INPUT.name())));
						continue; 
					}else if (matcher.group(TokenType.INSERT.name()) != null){
						tokens.add(new Token(TokenType.INSERT, matcher.group(TokenType.INSERT.name())));
						continue; 
					}else if (matcher.group(TokenType.INTO.name()) != null){
						tokens.add(new Token(TokenType.INTO, matcher.group(TokenType.INTO.name())));
						continue; 
					}else if (matcher.group(TokenType.LPAREN.name()) != null){
						tokens.add(new Token(TokenType.LPAREN, matcher.group(TokenType.LPAREN.name())));
						continue; 
					}else if (matcher.group(TokenType.RPAREN.name()) != null){
						tokens.add(new Token(TokenType.RPAREN, matcher.group(TokenType.RPAREN.name())));
						continue; 
					}else if (matcher.group(TokenType.LOAD.name()) != null){
						tokens.add(new Token(TokenType.LOAD, matcher.group(TokenType.LOAD.name())));
						continue; 
					}else if (matcher.group(TokenType.SAVE.name()) != null){
						tokens.add(new Token(TokenType.SAVE, matcher.group(TokenType.SAVE.name())));
						continue; 
					}else if (matcher.group(TokenType.SELECT.name()) != null){
						tokens.add(new Token(TokenType.SELECT, matcher.group(TokenType.SELECT.name())));
						continue; 
					}else if (matcher.group(TokenType.TABLE.name()) != null){
						tokens.add(new Token(TokenType.TABLE, matcher.group(TokenType.TABLE.name())));
						continue; 
					}else if (matcher.group(TokenType.TSELECT.name()) != null){
						tokens.add(new Token(TokenType.TSELECT, matcher.group(TokenType.TSELECT.name())));
						continue; 
					}else if (matcher.group(TokenType.VALUES.name()) != null){
						tokens.add(new Token(TokenType.VALUES, matcher.group(TokenType.VALUES.name())));
						continue; 
					 
					} else if (matcher.group(TokenType.ID.name()) != null) {
						tokens.add(new Token(TokenType.ID, matcher.group(TokenType.ID.name())));
						continue;
	               }
	                else if (matcher.group(TokenType.ASTK.name()) != null) {
						tokens.add(new Token(TokenType.ASTK, matcher.group(TokenType.ASTK.name())));
						continue;
	               }else if (matcher.group(TokenType.ERROR.name()) != null) {
	            	   System.out.println("Error: ** is not recognized");
						continue;
	               }


				}

				return tokens;
			}
			
		}