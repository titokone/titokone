package fi.hu.cs.titokone;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

/** This class is basically just normal JTable with added functionality.
*/
public class JTableX extends JTable {
  

//protected int selectedRow;
protected int[] selectedRows = { 0 };
protected boolean areRowsSelected = false;



public JTableX(TableModel dm) {
  super(dm);
  //setAutoResizeMode(AUTO_RESIZE_OFF);
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
  
  for (int i=0; i<this.getRowCount(); i++) {
    int lngth = getTextLength(i, column);
    
    if (lngth > maxLength) {
      maxLength = lngth;
    }
  }
  return maxLength;
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
}


