VERSION = 1.205
JARFILE = titokone-$(VERSION).jar
MANIFEST = META-INF/MANIFEST.MF
JAVAC = javac
JFLAGS = 


all: $(JARFILE)

%.class: %.java
	$(JAVAC) $(JFLAGS) $<

fi/hu/cs/titokone/Titokone.class:
	make -C fi/hu/cs/titokone/

$(JARFILE): fi/hu/cs/titokone/Titokone.class
	jar -cmf $(MANIFEST) $(JARFILE) *

clean:
	rm -f *.jar
	make -C fi/hu/cs/titokone/ clean
