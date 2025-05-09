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


package org.pentaho.reporting.engine.classic.core.modules.misc.datafactory;

import org.pentaho.reporting.engine.classic.core.DataFactory;
import org.pentaho.reporting.engine.classic.core.StaticDataRow;
import org.pentaho.reporting.engine.classic.core.metadata.DataFactoryMetaData;
import org.pentaho.reporting.engine.classic.core.modules.misc.datafactory.sql.JndiConnectionProvider;
import org.pentaho.reporting.engine.classic.core.modules.misc.datafactory.sql.SQLReportDataFactory;
import org.pentaho.reporting.engine.classic.core.testsupport.DataSourceTestBase;

public class SqlDataFactoryJndiIT extends DataSourceTestBase {
  private static final String[][] QUERIES_AND_RESULTS = new String[][] { { "SELECT * FROM Customers", "query-1.txt" } };

  public SqlDataFactoryJndiIT() {
  }

  public SqlDataFactoryJndiIT( final String s ) {
    super( s );
  }

  public void testMetaDataJndi() {
    final JndiConnectionProvider drc = new JndiConnectionProvider();
    drc.setConnectionPath( "sampledata" );

    final SQLReportDataFactory sqlReportDataFactory = new SQLReportDataFactory( drc );
    final DataFactoryMetaData metaData = sqlReportDataFactory.getMetaData();
    assertEquals( "Name property set, so display name must be test", "sampledata", metaData
        .getDisplayConnectionName( sqlReportDataFactory ) );
    sqlReportDataFactory.setQuery( "test", "SELECT * FROM TABLE" );

    assertNotNull( "QueryHash must exist", metaData.getQueryHash( sqlReportDataFactory, "test", new StaticDataRow() ) );

    final SQLReportDataFactory sqlReportDataFactory2 = new SQLReportDataFactory( drc );
    sqlReportDataFactory2.setQuery( "test", "SELECT * FROM TABLE2" );

    assertNotEquals( "Physical Queries do not match, so query hash must be different", metaData.getQueryHash(
        sqlReportDataFactory, "test", new StaticDataRow() ), ( metaData.getQueryHash( sqlReportDataFactory2, "test",
        new StaticDataRow() ) ) );

    sqlReportDataFactory2.setQuery( "test2", "SELECT * FROM TABLE" );
    final Object qh1 = metaData.getQueryHash( sqlReportDataFactory, "test", new StaticDataRow() );
    final Object qh2 = metaData.getQueryHash( sqlReportDataFactory2, "test2", new StaticDataRow() );
    assertEquals( "Physical Queries match, so queries are considered the same", qh1, qh2 );

    final JndiConnectionProvider drc2 = new JndiConnectionProvider();
    drc.setConnectionPath( "sampledata2" );
    final SQLReportDataFactory sqlReportDataFactory3 = new SQLReportDataFactory( drc2 );
    sqlReportDataFactory3.setQuery( "test", "SELECT * FROM TABLE2" );
    assertNotEquals( "Connections do not match, so query hash must be different", metaData.getQueryHash(
        sqlReportDataFactory, "test", new StaticDataRow() ), ( metaData.getQueryHash( sqlReportDataFactory3, "test",
        new StaticDataRow() ) ) );

    sqlReportDataFactory3.setQuery( "test2", "SELECT * FROM TABLE" );
    assertNotEquals( "Connections do not match, so queries are considered the same", metaData.getQueryHash(
        sqlReportDataFactory, "test", new StaticDataRow() ), metaData.getQueryHash( sqlReportDataFactory3, "test2",
        new StaticDataRow() ) );
  }

  public void testSaveAndLoad() throws Exception {
    runSaveAndLoad( QUERIES_AND_RESULTS );
  }

  public void testDerive() throws Exception {
    runDerive( QUERIES_AND_RESULTS );
  }

  public void testSerialize() throws Exception {
    runSerialize( QUERIES_AND_RESULTS );
  }

  public void testQuery() throws Exception {
    runTest( QUERIES_AND_RESULTS );
  }

  public static void main( String[] args ) throws Exception {
    final SqlDataFactoryJndiIT test = new SqlDataFactoryJndiIT();
    test.setUp();
    test.runGenerate( QUERIES_AND_RESULTS );
  }

  protected DataFactory createDataFactory( final String query ) {
    final JndiConnectionProvider drc = new JndiConnectionProvider( "SampleData", null, null );
    final SQLReportDataFactory sqlReportDataFactory = new SQLReportDataFactory( drc );
    sqlReportDataFactory.setQuery( "default", query );
    return sqlReportDataFactory;
  }
}
