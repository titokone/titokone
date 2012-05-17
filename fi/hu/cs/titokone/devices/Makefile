
JAVAC = javac
JFLAGS = -encoding utf-8
.PHONY: devices
.SUFFIXES: .java .class
.java.class:
	$(JAVAC) -cp ../../../../../ $(JFLAGS) $*.java

SOURCES = \
    InvalidIODevice.java \
    ZeroIODevice.java \
    RandomIODevice.java \
    MMU.java \
    AddressMappingIODevice.java \
    Pic.java \
    UART.java

all: classes 


classes: $(SOURCES:.java=.class)

clean:
	rm -f *.class

