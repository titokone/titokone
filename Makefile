VERSION = 1.205
JARFILE = titokone-$(VERSION).jar
MANIFEST = META-INF/MANIFEST.MF
JAVAC = javac
JFLAGS = 


all: $(JARFILE)

%.class: %.java
	$(JAVAC) $(JFLAGS) $<

fi/hu/cs/titokone/Titokone.class: fi/hu/cs/ttk91/TTK91Core.class
	make -C fi/hu/cs/titokone/

$(JARFILE): fi/hu/cs/titokone/Titokone.class
	jar -cmf $(MANIFEST) $(JARFILE) *

assari: fi/hu/cs/titokone/AssariUI.class

clean:
	rm -f *.jar
	make -C fi/hu/cs/ttk91/ clean
	make -C fi/hu/cs/titokone/ clean
