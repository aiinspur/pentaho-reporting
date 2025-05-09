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


package org.pentaho.reporting.engine.classic.core.function;

import org.pentaho.reporting.engine.classic.core.DataFactory;
import org.pentaho.reporting.engine.classic.core.DataRow;
import org.pentaho.reporting.engine.classic.core.ResourceBundleFactory;
import org.pentaho.reporting.engine.classic.core.wizard.DataSchema;
import org.pentaho.reporting.libraries.base.config.Configuration;

import javax.swing.table.TableModel;

/**
 * The expression runtime encapsulates all properties of the current report processing run that might be needed to
 * successfully evaluate an expression. The runtime grants access to the DataRow, the TableModel of the current report
 * and the ProcessingContext.
 *
 * @author Thomas Morgner
 */
public interface ExpressionRuntime {
  /**
   * Returns the current data-row. The datarow can be used to access the computed values of all expressions and
   * functions and the current row in the tablemodel.
   *
   * @return the data-row.
   */
  public DataRow getDataRow();

  public DataSchema getDataSchema();

  public DataFactory getDataFactory();

  /**
   * Returns the report configuration that was used to initiate this processing run.
   *
   * @return the report configuration.
   */
  public Configuration getConfiguration();

  /**
   * Returns the resource-bundle factory of current processing context.
   *
   * @return the current resource-bundle factory.
   */
  public ResourceBundleFactory getResourceBundleFactory();

  /**
   * Grants access to the tablemodel was granted using report properties, now direct.
   *
   * @return the current tablemodel used in the report.
   */
  public TableModel getData();

  /**
   * Returns the number of the row in padded datasource that is currently being processed. This is equal to the current
   * report state's currentRow number.
   *
   * @return the current row number.
   */
  public int getCurrentRow();

  /**
   * Returns the row number of the raw datasource that is currently accessed.
   *
   * @return the raw access row number.
   */
  public int getCurrentDataItem();

  public int getCurrentGroup();

  public int getGroupStartRow( String groupName );

  public int getGroupStartRow( int groupIndex );

  /**
   * Returns the current export descriptor as returned by the OutputProcessorMetaData object. The output descriptor is a
   * simple string collections consisting of the following components: exportclass/type/subtype
   * <p/>
   * For example, the PDF export would be: pageable/pdf and the StreamHTML export would return table/html/stream
   *
   * @return the export descriptor.
   */
  public String getExportDescriptor();

  /**
   * Returns the current processing context.
   *
   * @return the processing context.
   */
  public ProcessingContext getProcessingContext();

  /**
   * A flag indicating that this report contains crosstabs.
   *
   * @return
   */
  public boolean isStructuralComplexReport();

  /**
   * A flag indicating that a crosstab is actively processed.
   *
   * @return
   */
  public boolean isCrosstabActive();
}
