JFLAGS = -g
JC = javac
JRUN = java
.SUFFIXES: .java .class
.java.class:
        $(JC) $(JFLAGS) $*.java

CLASSES = \
        MainMenu.java \
        Lexer.java \
        DML.java \
        Query.java 

default: classes

classes: $(CLASSES:.java=.class)

clean:
        $(RM) *.class
run:
        $(JRUN) MainMenu

