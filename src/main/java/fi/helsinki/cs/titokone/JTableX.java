// Copyright © 2004-2006 University of Helsinki, Department of Computer Science
// Copyright © 2012 various contributors
// This software is released under GNU Lesser General Public License 2.1.
// The license text is at http://www.gnu.org/licenses/lgpl-2.1.html

package fi.helsinki.cs.titokone;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * This class is basically just normal JTable with added functionality.
 */
public class JTableX extends JTable {
    /**
	 *
	 */
	private static final long serialVersionUID = -7424739136090735650L;
    protected int[] selectedRows = {0};
    protected boolean areRowsSelected = false;
    protected int tryCounter = 0;
    protected String[] columnToolTips;

    public JTableX(TableModel dm) {
        super(dm);
        columnToolTips = new String[getColumnCount()];
    }


    public void setToolTipTextForColumns(String[] toolTips) {
        columnToolTips = toolTips;
    }

    @Override
    public String getToolTipText(MouseEvent e) {
        java.awt.Point p = e.getPoint();
        int index = columnModel.getColumnIndexAtX(p.x);
        int realIndex = columnModel.getColumn(index).getModelIndex();
        return columnToolTips[realIndex];
    }

    /**
     * Returns the length of text in pixels in certain cell. The font and its
     * attributes are taken into account.
     *
     * @param row    Row.
     * @param column Column.
     * @return Length of text in pixels.
     */
    public int getTextLength(int row, int column) {
        Font tblFont = this.getFont();
        Graphics tblGraphics = this.getGraphics();
        FontMetrics tblFontMetrics = this.getFontMetrics(tblFont);
        int marginLength = this.getColumnModel().getColumnMargin();

        String cellValue = (String) (this.getValueAt(row, column));
        if (cellValue == null) {
            cellValue = "";
        }

        int textLength = (int) (tblFontMetrics.getStringBounds(cellValue, tblGraphics).getWidth()) + 1;

        return textLength + 2 * marginLength;
    }

    /**
     * Returns a value, which is the length of the longest text in a certain column.
     *
     * @param column Number of the column. 0 is the leftmost, next to the right is 1,
     *               and so on.
     * @return The length of the longest text in the given column, in pixel.
     */
    public int getMaxTextLengthInColumn(int column) {

        int maxLength = 0;
        int rowForMaxLength = 0;

        for (int i = 0; i < this.getRowCount(); i++) {
            String str = (String) ((DefaultTableModel) getModel()).getValueAt(i, column);
            if (str.length() > maxLength) {
                maxLength = str.length();
                rowForMaxLength = i;
            }
        }
        return getRowCount() != 0 ? getTextLength(rowForMaxLength, column) : 0;
    }

    /**
     * Sets the column at index col to selected or deselected
     * based on the value of select.
     */
    public void selectRow(int row) {
        this.unselectAllRows();
        this.setRowSelectionInterval(row, row);
    }

    public void unselectAllRows() {
        this.clearSelection();
    }
}
