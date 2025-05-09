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


package org.pentaho.reporting.designer.core.editor.drilldown.basic;

import org.pentaho.reporting.designer.core.editor.drilldown.DrillDownParameterTable;
import org.pentaho.reporting.designer.core.editor.drilldown.model.DrillDownParameter;
import org.pentaho.ui.xul.XulComponent;
import org.pentaho.ui.xul.XulDomContainer;
import org.pentaho.ui.xul.dom.Element;
import org.pentaho.ui.xul.swing.SwingElement;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * This is a Xul-Binding for the parameter table. The table itself is not a Xul-Table, as its editors cannot be
 * replicated using Xul.
 *
 * @author Thomas Morgner.
 */
public class XulDrillDownParameterTable extends SwingElement implements XulComponent {
  private class ForwardChangeEventsHandler implements PropertyChangeListener {
    private ForwardChangeEventsHandler() {
    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source and the property that has changed.
     */
    public void propertyChange( final PropertyChangeEvent evt ) {
      changeSupport.firePropertyChange( evt.getPropertyName(), evt.getOldValue(), evt.getNewValue() );
    }
  }

  private DrillDownParameterTable table;

  public XulDrillDownParameterTable( final Element self,
                                     final XulComponent parent,
                                     final XulDomContainer domContainer,
                                     final String tagName ) {
    super( tagName );
    this.table = new DrillDownParameterTable();
    setManagedObject( table );

    table.addPropertyChangeListener( DrillDownParameterTable.DRILL_DOWN_PARAMETER_PROPERTY,
      new ForwardChangeEventsHandler() );
    table.addPropertyChangeListener( DrillDownParameterTable.HIDE_PARAMETER_UI_PARAMETER_PROPERTY,
      new ForwardChangeEventsHandler() );
  }

  public String[] getFilteredParameterNames() {
    return table.getFilteredParameterNames();
  }

  public void setFilteredParameterNames( final String[] names ) {
    table.setFilteredParameterNames( names );
  }

  public void setDrillDownParameter( final DrillDownParameter[] parameter ) {
    table.setDrillDownParameter( parameter );
  }

  public DrillDownParameter[] getDrillDownParameter() {
    return table.getDrillDownParameter();
  }

  public DrillDownParameterTable getTable() {
    return table;
  }

  public void layout() {
    // ignore
  }

  public void setDisabled( final boolean disabled ) {
    super.setDisabled( disabled );
    table.setEnabled( !disabled );
  }

  public String getTitle() {
    return table.getTitle();
  }

  public void setTitle( final String title ) {
    table.setTitle( title );
  }

  public boolean isAllowCustomParameter() {
    return table.isAllowCustomParameter();
  }

  public void setAllowCustomParameter( final boolean allowCustomParameter ) {
    table.setAllowCustomParameter( allowCustomParameter );
  }

  public boolean isShowRefreshButton() {
    return table.isShowRefreshButton();
  }

  public void setShowRefreshButton( final boolean showRefreshButton ) {
    table.setShowRefreshButton( showRefreshButton );
  }

  public final boolean getHideParameterUi() {
    return isHideParameterUi();
  }

  public boolean isHideParameterUi() {
    return table.isHideParameterUi();
  }

  public void setHideParameterUi( final boolean hideParameterUi ) {
    table.setHideParameterUi( hideParameterUi );
  }

  public final boolean getShowHideParameterUiCheckbox() {
    return isShowHideParameterUiCheckbox();
  }

  public boolean isShowHideParameterUiCheckbox() {
    return table.isShowHideParameterUiCheckbox();
  }

  public void setShowHideParameterUiCheckbox( final boolean showHideParameterUiCheckbox ) {
    table.setShowHideParameterUiCheckbox( showHideParameterUiCheckbox );
  }

  // xul is broken in how it interprets the bean specification ..
  public boolean getSingleTabMode() {
    return table.isSingleTabMode();
  }

  public boolean isSingleTabMode() {
    return table.isSingleTabMode();
  }

  public void setSingleTabMode( final boolean singleTabMode ) {
    table.setSingleTabMode( singleTabMode );
  }
}
