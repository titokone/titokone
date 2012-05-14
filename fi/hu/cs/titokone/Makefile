
JAVAC = javac
JFLAGS =
.PHONY: translations
.SUFFIXES: .java .class
.java.class:
	$(JAVAC) -cp ../../../../ $(JFLAGS) $*.java

SOURCES = \
	Animator.java \
	Application.java \
	BinaryInterpreter.java \
	Binary.java \
	CompileDebugger.java \
	CompileInfo.java \
	Compiler.java \
	Control.java \
	DebugInfo.java \
	Display.java \
	FileHandler.java \
	GUIBrain.java \
	GUICompileSettingsDialog.java \
	GUIHTMLDialog.java \
	GUI.java \
	GUIRunSettingsDialog.java \
	GUISettingsDialog.java \
	GUIThreader.java \
	Interpreter.java \
	InvalidDefinitionException.java \
	InvalidSymbolException.java \
	JTableX.java \
	Loader.java \
	LoadInfo.java \
	MemoryLine.java \
	Message.java \
	Processor.java \
	RandomAccessMemory.java \
	Registers.java \
	ResourceLoadFailedException.java \
	RunDebugger.java \
	RunInfo.java \
	Settings.java \
	Source.java \
	SymbolicInterpreter.java \
	SymbolTable.java \
	Titokone.java \
	Translator.java

all: classes translations

translations:
	make -C translations

classes: $(SOURCES:.java=.class)

clean:
	rm -f *.class
	make -C translations clean

