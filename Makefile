VERSION = 1.204
JARFILE = titokone-$(VERSION).jar
MANIFEST = META-INF/MANIFEST.MF
JAVAC = javac
JFLAGS =


all: $(JARFILE)

%.class: %.java
	$(JAVAC) $(JFLAGS) $<

$(JARFILE): fi/hu/cs/ttk91/*.class fi/hu/cs/titokone/*.class
	jar -cmf $(MANIFEST) $(JARFILE) * 
