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


package org.pentaho.reporting.designer.core.util.table;

import org.pentaho.reporting.engine.classic.core.metadata.MetaData;

import java.io.Serializable;
import java.util.Locale;

public class GroupedName implements Serializable, Comparable {
  private String name;
  private String groupName;
  private MetaData metaData;

  public GroupedName( final MetaData metaData ) {
    this.metaData = metaData;
    this.name = metaData.getDisplayName( Locale.getDefault() );
    this.groupName = metaData.getGrouping( Locale.getDefault() );
  }

  public GroupedName( final MetaData metaData, final String name, final String groupName ) {
    this.metaData = metaData;
    if ( groupName == null ) {
      throw new NullPointerException();
    }
    if ( name == null ) {
      throw new NullPointerException();
    }
    this.name = name;
    this.groupName = groupName;
  }

  public String getName() {
    return name;
  }

  public void setName( final String name ) {
    this.name = name;
  }

  public String getGroupName() {
    return groupName;
  }

  public MetaData getMetaData() {
    return metaData;
  }

  public int compareTo( final Object o ) {
    final GroupedName other = (GroupedName) o;
    if ( other == null ) {
      return 1;
    }
    final int nameResult = name.compareTo( other.name );
    if ( nameResult != 0 ) {
      return nameResult;
    }

    return groupName.compareTo( other.groupName );
  }
}
