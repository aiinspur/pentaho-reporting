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


package org.pentaho.reporting.ui.datasources.olap4j;

import org.pentaho.reporting.engine.classic.core.designtime.DesignTimeContext;
import org.pentaho.reporting.engine.classic.extensions.datasources.olap4j.AbstractMDXDataFactory;
import org.pentaho.reporting.engine.classic.extensions.datasources.olap4j.SimpleBandedMDXDataFactory;
import org.pentaho.reporting.engine.classic.extensions.datasources.olap4j.connections.DriverConnectionProvider;
import org.pentaho.reporting.engine.classic.extensions.datasources.olap4j.connections.JndiConnectionProvider;
import org.pentaho.reporting.ui.datasources.jdbc.connection.DriverConnectionDefinition;
import org.pentaho.reporting.ui.datasources.jdbc.connection.JdbcConnectionDefinition;
import org.pentaho.reporting.ui.datasources.jdbc.connection.JndiConnectionDefinition;

import java.awt.*;
import java.util.Enumeration;
import java.util.Properties;

/**
 * @author Michael D'Amour
 */
public class SimpleBandedMdxDataSourceEditor extends SimpleOlap4JDataSourceEditor {
  public SimpleBandedMdxDataSourceEditor( final DesignTimeContext context ) {
    super( context );
  }

  public SimpleBandedMdxDataSourceEditor( final DesignTimeContext context, final Dialog owner ) {
    super( context, owner );
  }

  public SimpleBandedMdxDataSourceEditor( final DesignTimeContext context, final Frame owner ) {
    super( context, owner );
  }

  protected void init( final DesignTimeContext context ) {
    super.init( context );
    setTitle( Messages.getString( "SimpleBandedMdxDataSourceEditor.Title" ) );
  }

  protected String getDialogId() {
    return "Olap4JDataSourceEditor.SimpleBanded";
  }

  protected AbstractMDXDataFactory createDataFactory() {
    final JdbcConnectionDefinition connectionDefinition =
      (JdbcConnectionDefinition) getDialogModel().getConnections().getSelectedItem();

    if ( connectionDefinition instanceof JndiConnectionDefinition ) {
      final JndiConnectionDefinition jcd = (JndiConnectionDefinition) connectionDefinition;
      final JndiConnectionProvider provider = new JndiConnectionProvider();
      provider.setConnectionPath( jcd.getJndiName() );
      provider.setUsername( jcd.getUsername() );
      provider.setPassword( jcd.getPassword() );
      return new SimpleBandedMDXDataFactory( provider );
    } else if ( connectionDefinition instanceof DriverConnectionDefinition ) {
      final DriverConnectionDefinition dcd = (DriverConnectionDefinition) connectionDefinition;
      final DriverConnectionProvider provider = new DriverConnectionProvider();
      provider.setDriver( dcd.getDriverClass() );
      provider.setUrl( dcd.getConnectionString() );

      final Properties properties = dcd.getProperties();
      final Enumeration keys = properties.keys();
      while ( keys.hasMoreElements() ) {
        final String key = (String) keys.nextElement();
        provider.setProperty( key, properties.getProperty( key ) );
      }
      return new SimpleBandedMDXDataFactory( provider );
    } else {
      return null;
    }
  }
}
