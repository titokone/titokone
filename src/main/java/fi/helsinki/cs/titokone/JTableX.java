package fi.helsinki.cs.titokone;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

/** This class is basically just normal JTable with added functionality.
*/
public class JTableX extends JTable {
  

//protected int selectedRow;
protected int[] selectedRows = { 0 };
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



public String getToolTipText(MouseEvent e) {
  String tip = null;
  java.awt.Point p = e.getPoint();
  int index = columnModel.getColumnIndexAtX(p.x);
  int realIndex = columnModel.getColumn(index).getModelIndex();
  return columnToolTips[realIndex];
}



/** Returns the length of text in pixels in certain cell. The font and its 
    attributes are taken into account.
    @param row Row.
    @param column Column.
    @return Lenght of text in pixels.
*/
public int getTextLength(int row, int column) {
 
  Font tblFont = this.getFont();
  Graphics tblGraphics = this.getGraphics();
  FontMetrics tblFontMetrics = this.getFontMetrics(tblFont);
  int marginLength = this.getColumnModel().getColumnMargin();
  
  String cellValue = (String)(this.getValueAt(row,column));
  if (cellValue == null) 
    cellValue = "";
    
  int textLength = (int)(tblFontMetrics.getStringBounds(cellValue, tblGraphics).getWidth()) + 1;
  
  return textLength + 2*marginLength;
  
}


/** Returns a value, which is the length of the longest text in a certain column.
    @param column Number of the column. 0 is the leftmost, next to the right is 1, 
                  and so on.
    @return The lenght of the longest text in the given column, in pixel.
*/
public int getMaxTextLengthInColumn(int column) {
  
  int maxLength = 0;
  int rowForMaxLength = 0;
  
  for (int i=0; i<this.getRowCount(); i++) {
    String str = (String)((DefaultTableModel)getModel()).getValueAt(i,column);
    if (str.length() > maxLength) {
      maxLength = str.length();
      rowForMaxLength = i;
    }
    
    /*int lngth = 0;
    
    if (str.length > 2)
      lngth = getTextLength(i, column);
    
    
    if (lngth > maxLength) {
      maxLength = lngth;
    }*/
  }
  return getRowCount() != 0 ? getTextLength(rowForMaxLength, column) : 0;
}


/** Sets the column at index col to selected or deselected
    based on the value of select.
*/
public void selectRow(int row) {
    this.selectedRows = new int[1];
    selectedRows[0] = row;
    this.areRowsSelected = true;
}

public void selectRows(int[] rows) {
    this.selectedRows = rows;
    this.areRowsSelected = true;
}


public void unselectAllRows() {
  this.areRowsSelected = false;
}


/** This method returns whether a particular cell is selected or not.
*/
public boolean isCellSelected(int row, int column) throws IllegalArgumentException {
  
  for (int i=0 ; i<selectedRows.length ; i++) {
    if (selectedRows[i] == row){
      if (areRowsSelected) {
        return true;
      }
      else {
        return false;
      }
    }
  }
  return false;
}

    /** This method is redefined to catch an odd exception we cannot 
	otherwise seem to affect. It calls the corresponding method 
	in JTable, catches an ArrayOutOfBoundsException and logs it. 
	It then tries again a moment later to avoid race conditions. 
	If after ten attempts the error still occurs, it gives up 
	and returns null, probably causing an exception upstream. */
    public Component prepareRenderer(TableCellRenderer renderer, int row, 
				     int column) {
	Logger logger;
	Component result;
	try {
	    result = super.prepareRenderer(renderer, row, column);
	    tryCounter = 0;
	    return result;
	}
	catch(ArrayIndexOutOfBoundsException ghostError) {
	    logger = Logger.getLogger(getClass().getPackage().getName());
	    logger.warning(new Message("Our JTable override is causing " +
				       "odd errors. Retrying in 100 " +
				       "milliseconds.").toString());
	    logger.fine(new Message("Full JTable error was: {0}",
				    ghostError.toString()).toString());
	    // Sleep for 100 milliseconds to avoid race conditions.
	    try {
		Thread.sleep(100); 
	    }
	    catch(InterruptedException noMoreSleeping) {}
	    tryCounter++;
	    // Try again.
	    if(tryCounter < 10)
		return prepareRenderer(renderer, row, column);
	    else {
		tryCounter = 0;
		return null;
	    }
	}
    }
}


