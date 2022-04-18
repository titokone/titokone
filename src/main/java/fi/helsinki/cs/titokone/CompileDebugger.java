// Copyright © 2004-2006 University of Helsinki, Department of Computer Science
// Copyright © 2012 various contributors
// This software is released under GNU Lesser General Public License 2.1.
// The license text is at http://www.gnu.org/licenses/lgpl-2.1.html

package fi.helsinki.cs.titokone;

import fi.helsinki.cs.titokone.devices.DeviceNames;

/**
 * This class is used by compiler when it compiles code. For each line
 * compiled compiler asks CompileDebugger to create a CompileInfo object and
 * passes it to the GUIBrain.
 */
public class CompileDebugger {

    /**
     * This field contains current CompileInfo object.
     */
    private CompileInfo info;

/*----------- Constructor -----------*/

    /**
     * This is the only constructor for CompileDebugger. It is called
     * when compiler is created.
     */
    public CompileDebugger() {
    }

/*----------- Compiler instructions -----------*/

    /**
     * This method tells that an EQU was found and it is added to the
     * symboltable.
     *
     * @param name  String containing name of the symbol.
     * @param value Int containing the value.
     */
    public void foundEQU(String name, int value) {
        info.setSymbolFound();
        info.setSymbolName(name, value);
    }

    /**
     * This method tells debugger that a DS compiler instruction was found
     * and it is added to the symboltable.
     *
     * @param name String containing name of the symbol.
     */
    public void foundDS(String name) {
        info.setSymbolFound();
        info.setSymbolName(name);
    }

    /**
     * This method tells debugger that a DC compiler instruction was found
     * and it is added to the symboltable.
     *
     * @param name String containing name of the symbol.
     */
    public void foundDC(String name) {
        info.setSymbolFound();
        info.setSymbolName(name);
    }

    /**
     * This Method  tells debugger that a symbol was used as an
     * address.
     *
     * @param name String containing name of the symbol.
     */
    public void foundSymbol(String name) {
        info.setSymbolFound();
        Integer value;

        if ((value = SvcNames.lookupIgnoringCase(name)) != null ||
                (value = DeviceNames.lookupIgnoringCase(name)) != null) {
            info.setSymbolName(name, value);
        } else {
            info.setSymbolName(name);
        }
    }

    /**
     * This method tells that for given label points to given line.
     *
     * @param name       String containing name of the symbol.
     * @param lineNumber Int containing the linenumber of the label.
     */
    public void foundLabel(String name, int lineNumber) {
        info.setLabelDefined(name, lineNumber);
    }

    /**
     * This method sets the compiled value of a line during
     * the second round of compilation.
     */
    public void setBinary(int binary) {
        info.setLineBinary(binary);
    }

    /**
     * This method tells debugger that first round of compilation is
     * in progres and line wasn't empty. It creates CompileInfo object
     * and sets its phase to 1, lineNumber and lineContents fields.
     *
     * @param lineNumber   Number of the compiled line.
     * @param lineContents String containing the symbolic command.
     */
    public void firstPhase(int lineNumber, String lineContents) {
        info = new CompileInfo(CompileInfo.FIRST_ROUND, lineNumber, lineContents);
    }

    /**
     * This method is used when all lines are checked in the first
     * phase of compilation and compiler is setting symbols and
     * labels.
     */
    public void firstPhase() {
        info = new CompileInfo(CompileInfo.FINALIZING_FIRST_ROUND);
    }

    /**
     * This method is used when all DC and DS are defined and
     * compiler is ready to move to the second phase. Compiler tells
     * debugger what are code lines and then what is dataArea in memory
     * and what it contains. GUIBrain then redraws GUI and writes
     * codelines leaving binary cells empty. Then it draws data area
     * where number of first data line is codeArea.length
     *
     * @param codeArea    String array containing codelines.
     * @param dataArea    String array containing data.
     * @param symbolTable 2-dimensional String array containing the symbol table.
     */
    public void finalFirstPhase(String[] codeArea, String[] dataArea, String[][] symbolTable) {
        info.setInstructions(codeArea);
        info.setData(dataArea);
        info.setSymbolTable(symbolTable);
    }

    /**
     * This method sets the comment to the compileInfo.
     */
    public void setComment(String message) {
        info.setComments(message);
    }

    /**
     * This method sets the status info to the compileInfo.
     */
    public void setStatusMessage(String message) {
        info.setStatusMessage(message);
    }

    /**
     * This method tells debugger that the second round of
     * compilation is in progress. It creates CompileInfo object and sets
     * its phase to 3.
     *
     * @param lineNumber   number of the compiled line.
     * @param lineContents Contents of the line.
     */
    public void secondPhase(int lineNumber, String lineContents) {
        info = new CompileInfo(CompileInfo.SECOND_ROUND, lineNumber, lineContents);
    }

    /**
     * This method tells debugger that final phase of compilation is
     * in progress. It creates CompileInfo object and sets its phase to 4.
     */
    public void finalPhase() {
        info = new CompileInfo(CompileInfo.FINALIZING);
    }

    /**
     * This method returns the created CompileInfo-object. It sets
     * comments in the CompileInfo and then returns it.
     */
    public CompileInfo lineCompiled() {
        return info;
    }
}

