// Copyright © 2004-2006 University of Helsinki, Department of Computer Science
// Copyright © 2012 various contributors
// This software is released under GNU Lesser General Public License 2.1.
// The license text is at http://www.gnu.org/licenses/lgpl-2.1.html

package fi.helsinki.cs.titokone;

/**
 *  Represents a compiled instruction. Basically just a wrapper to conveniently
 *  extract the instruction fields from the binary representation.
 * 
 *  TODO: At least RunInfo and MemoryInfo might have something in common with this class.
 */
public class Instruction {

    public Instruction(int binaryValue) {
        this.binaryValue = binaryValue;
    }

    private int binaryValue;

    public int getBinaryValue() {
        return binaryValue;
    }

    public int getOpcode() {
        return binaryValue >>> 24;
    }

    public int getRj() {
        return (binaryValue & 0xE00000) >>> 21;
    }

    public int getM() {
        return (binaryValue & 0x180000) >>> 19;
    }

    public int getRi() {
        return (binaryValue & 0x070000) >>> 16;
    }

    public int getAddr() {
        return (short) (binaryValue & 0xFFFF);
    }
    
    public String toColonString() {
        return getOpcode() + ":" + getRj() + ":" + getM() + ":" + getRi() + ":" + getAddr();
    }
}
