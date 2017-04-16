JCC = javac

JFLAGS = -g

JRUN = java

PICO = pico

default: MainMenu.class

#-------------------------------------
MainMenu.class: MainMenu.java
	$(JCC) $(JFLAGS) MainMenu.java
#-------------------------------------
#-------------------------------------
Lexer.class: Lexer.java
	$(JCC) $(JFLAGS) Lexer.java
#-------------------------------------
#-------------------------------------
DML.class: DML.java
	$(JCC) $(JFLAGS) DML.java
#-------------------------------------
#-------------------------------------
Query.class: Query.java
	$(JCC) $(JFLAGS) Query.java
#-------------------------------------
clean:
	$(RM) *.class

run:
	$(JRUN) MainMenu

