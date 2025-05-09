/*! ******************************************************************************
 *
 * Pentaho
 *
 * Copyright (C) 2024 by Hitachi Vantara, LLC : http://www.pentaho.com
 *
 * Use of this software is governed by the Business Source License included
 * in the LICENSE.TXT file.
 *
 * Change Date: 2029-07-20
 ******************************************************************************/


package org.pentaho.reporting.designer.core.editor.parameters;

import org.pentaho.reporting.designer.core.util.table.ElementMetaDataTable;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.util.EventObject;

/**
 * Todo: Document me!
 * <p/>
 * Date: 10.05.2010 Time: 16:20:14
 *
 * @author Thomas Morgner.
 */
public class InstantEditingTable extends ElementMetaDataTable {
  private static class InstantEditingTableCellEditor implements TableCellEditor {
    private TableCellEditor backend;

    private InstantEditingTableCellEditor( final TableCellEditor backend ) {
      if ( backend == null ) {
        throw new NullPointerException();
      }
      this.backend = backend;
    }

    public Component getTableCellEditorComponent( final JTable table,
                                                  final Object value,
                                                  final boolean isSelected,
                                                  final int row,
                                                  final int column ) {
      return backend.getTableCellEditorComponent( table, value, isSelected, row, column );
    }

    public Object getCellEditorValue() {
      return backend.getCellEditorValue();
    }

    public boolean isCellEditable( final EventObject anEvent ) {
      return true;
    }

    public boolean shouldSelectCell( final EventObject anEvent ) {
      return true;
    }

    public boolean stopCellEditing() {
      return backend.stopCellEditing();
    }

    public void cancelCellEditing() {
      backend.cancelCellEditing();
    }

    public void addCellEditorListener( final CellEditorListener l ) {
      backend.addCellEditorListener( l );
    }

    public void removeCellEditorListener( final CellEditorListener l ) {
      backend.removeCellEditorListener( l );
    }
  }

  public InstantEditingTable() {
  }

  public TableCellEditor getCellEditor( final int row, final int viewColumn ) {
    final TableCellEditor tableCellEditor = super.getCellEditor( row, viewColumn );
    if ( tableCellEditor == null ) {
      return null;
    }
    return new InstantEditingTableCellEditor( tableCellEditor );
  }
}
