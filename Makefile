VERSION = 1.300
JARFILE = titokone-$(VERSION).jar
MANIFEST = META-INF/MANIFEST.MF

.PHONY: ttk91 titokone assari

all: $(JARFILE)

ttk91:
	make -C fi/hu/cs/ttk91/

titokone: ttk91
	make -C fi/hu/cs/titokone/

$(JARFILE): titokone
	jar -cmf $(MANIFEST) $(JARFILE) *

assari: fi/hu/cs/titokone/AssariUI.class

clean:
	rm -f *.jar
	make -C fi/hu/cs/ttk91/ clean
	make -C fi/hu/cs/titokone/ clean
