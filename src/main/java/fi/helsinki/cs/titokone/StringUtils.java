// Copyright © 2004-2006 University of Helsinki, Department of Computer Science
// Copyright © 2012 various contributors
// This software is released under GNU Lesser General Public License 2.1.
// The license text is at http://www.gnu.org/licenses/lgpl-2.1.html

package fi.helsinki.cs.titokone;

public class StringUtils {

    /**
     * This method converts int values to binary-string. intToBinary(1,2) --> "01"
     *
     * @param value Int value to be converted.
     * @param bits  How many bits can be used .
     * @return String representation of a said Int.
     */
    public static String intToBinary(long value, int bits) {
/* if bits too few, i.e. 10,2 then result is "11" */
        char[] returnValue = new char[bits];
        boolean wasNegative = false;

        if (value < 0) {
            wasNegative = true;
            ++value;
            value = (value * -1);
        }


        for (int i = 0; i < bits; ++i) {
            returnValue[i] = '0';
        }

        for (int i = returnValue.length - 1; i > -1; --i) {
            if (value >= (int) Math.pow(2.0, i * 1.0)) {
                returnValue[returnValue.length - 1 - i] = '1';
                value = value - (int) Math.pow(2.0, i * 1.0);
            }
        }

        if (wasNegative) {
            for (int i = 0; i < returnValue.length; ++i) {
                if (returnValue[i] == '0') {
                    returnValue[i] = '1';
                } else {
                    returnValue[i] = '0';
                }
            }
        }

        return new String(returnValue);
    }

    /**
     * This method converts String that contains a binary to int. binaryToInt("01") --> 1
     *
     * @param binaryValue  String representing the binary, if other than {0,1} then null.
     * @param signIncluded Boolean value telling whether 11 is -1 or 3 i.e. will the leading
     *                     one be interpreted as sign-bit.
     * @return Int value of a Binary.
     */
    public static int binaryToInt(String binaryValue, boolean signIncluded) {
/*  returns 0 when error! exception perhaps? */
        boolean isNegative = false;
        int value = 0;

        if (signIncluded) {
            if (binaryValue.charAt(0) == '1') {
                isNegative = true;
                binaryValue = binaryValue.replace('1', '2');
                binaryValue = binaryValue.replace('0', '1');
                binaryValue = binaryValue.replace('2', '0');
            }
        }

        for (int i = 0; i < binaryValue.length(); ++i) {
            if (binaryValue.charAt(binaryValue.length() - 1 - i) == '1') {
                value = value + (int) Math.pow(2.0, i * 1.0);
            } else {
                if (binaryValue.charAt(binaryValue.length() - 1 - i) != '0') {
                    return 0;
                }
            }
        }

        if (isNegative) {
            value = (value + 1) * -1;
        }
        return value;
    }
}
