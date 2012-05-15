
JAVAC = javac
JFLAGS = -encoding utf-8

.SUFFIXES: .java .class
.java.class:
	$(JAVAC) -cp ../../../../ $(JFLAGS) $*.java

SOURCES = \
	TTK91Exception.java \
	TTK91RuntimeException.java \
	TTK91AddressOutOfBounds.java \
	TTK91Application.java \
	TTK91BadAccessMode.java \
	TTK91CompileException.java \
	TTK91CompileSource.java \
	TTK91Core.java \
	TTK91Cpu.java \
	TTK91DivisionByZero.java \
	TTK91ExecutionOverrun.java \
	TTK91FailedWrite.java \
	TTK91IntegerOverflow.java \
	TTK91InvalidDevice.java \
	TTK91InvalidOpCode.java \
	TTK91InvalidService.java \
	TTK91Memory.java \
	TTK91NoKbdData.java \
	TTK91NoStdInData.java \
	TTK91OutOfMemory.java \
	TTK91SymbolNotDefined.java \
	TTK91SyntaxError.java


all: classes

classes: $(SOURCES:.java=.class)

clean:
	rm -f *.class

