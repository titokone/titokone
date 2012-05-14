
JAVAC = javac
JFLAGS =

.SUFFIXES: .java .class
.java.class:
	$(JAVAC) -cp ../../../../../ $(JFLAGS) $*.java

SOURCES = \
	Translations_en.java \
	Translations_fi.java

all: classes

classes: $(SOURCES:.java=.class)

clean:
	rm -f *.class

