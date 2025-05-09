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


package org.pentaho.reporting.engine.classic.core.metadata;

import org.pentaho.reporting.engine.classic.core.Element;
import org.pentaho.reporting.engine.classic.core.filter.MessageFormatSupport;
import org.pentaho.reporting.engine.classic.core.function.Expression;
import org.pentaho.reporting.libraries.docbundle.BundleUtilities;
import org.pentaho.reporting.libraries.formula.parser.ParseException;
import org.pentaho.reporting.libraries.formula.util.FormulaUtil;
import org.pentaho.reporting.libraries.resourceloader.ResourceKey;
import org.pentaho.reporting.libraries.resourceloader.ResourceKeyCreationException;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;

import java.util.ArrayList;

public class DefaultReportPreProcessorPropertyCore implements ReportPreProcessorPropertyCore {
  private static final String[] EMPTY = new String[0];
  private static final ResourceReference[] EMPTY_RESOURCES = new ResourceReference[0];

  public DefaultReportPreProcessorPropertyCore() {
  }

  public String[] getReferencedFields( final ReportPreProcessorPropertyMetaData metaData, final Expression expression,
      final Object attributeValue ) {
    if ( expression == null ) {
      throw new NullPointerException();
    }
    if ( attributeValue == null ) {
      return EMPTY;
    }
    final String propertyRole = metaData.getPropertyRole();
    if ( "Field".equals( propertyRole ) ) {
      if ( attributeValue instanceof String[] ) {
        final String[] vals = (String[]) attributeValue;
        return (String[]) vals.clone();
      }
      return new String[] { String.valueOf( attributeValue ) };
    } else if ( "Message".equals( propertyRole ) ) {
      final String message = String.valueOf( attributeValue );
      final MessageFormatSupport messageFormatSupport = new MessageFormatSupport();
      messageFormatSupport.setFormatString( message );
      return messageFormatSupport.getFields();
    } else if ( "Formula".equals( propertyRole ) ) {
      final String formula = String.valueOf( attributeValue );
      try {
        final Object[] objects = FormulaUtil.getReferences( formula );
        final ArrayList list = new ArrayList();
        for ( int i = 0; i < objects.length; i++ ) {
          final Object object = objects[i];
          if ( object instanceof String ) {
            list.add( object );
          }
        }
        return (String[]) list.toArray( new String[list.size()] );
      } catch ( ParseException e ) {
        return EMPTY;
      }
    }
    return EMPTY;
  }

  public String[] getReferencedGroups( final ReportPreProcessorPropertyMetaData metaData, final Expression expression,
      final Object attributeValue ) {
    if ( expression == null ) {
      throw new NullPointerException();
    }
    if ( attributeValue == null ) {
      return EMPTY;
    }
    final String propertyRole = metaData.getPropertyRole();
    if ( "Group".equals( propertyRole ) ) {
      if ( attributeValue instanceof String[] ) {
        final String[] attrVal = (String[]) attributeValue;
        return (String[]) attrVal.clone();
      }
      return new String[] { String.valueOf( attributeValue ) };
    }
    return EMPTY;
  }

  public String[] getReferencedElements( final ReportPreProcessorPropertyMetaData metaData,
      final Expression expression, final Object attributeValue ) {
    if ( expression == null ) {
      throw new NullPointerException();
    }
    if ( attributeValue == null ) {
      return EMPTY;
    }
    final String propertyRole = metaData.getPropertyRole();
    if ( "ElementName".equals( propertyRole ) ) {
      if ( attributeValue instanceof String[] ) {
        final String[] attrVal = (String[]) attributeValue;
        return (String[]) attrVal.clone();
      }
      return new String[] { String.valueOf( attributeValue ) };
    }
    return EMPTY;
  }

  public ResourceReference[] getReferencedResources( final ReportPreProcessorPropertyMetaData metaData,
      final Expression expression, final Object attributeValue, final Element reportElement,
      final ResourceManager resourceManager ) {
    if ( expression == null ) {
      throw new NullPointerException();
    }
    if ( reportElement == null ) {
      throw new NullPointerException();
    }
    if ( resourceManager == null ) {
      throw new NullPointerException();
    }
    final String propertyRole = metaData.getPropertyRole();
    if ( "Content".equals( propertyRole ) ) {
      final ResourceKey contentBase = reportElement.getContentBase();
      final ResourceKey elementSource = reportElement.getDefinitionSource();
      if ( attributeValue instanceof ResourceKey ) {
        final ResourceKey path = (ResourceKey) attributeValue;
        final boolean linked = BundleUtilities.isSameBundle( elementSource, path );
        return new ResourceReference[] { new ResourceReference( path, linked ) };
      } else if ( attributeValue instanceof String && contentBase != null ) {
        try {
          // not a resource-key, so try to make one ..
          final ResourceKey path = resourceManager.deriveKey( contentBase, String.valueOf( attributeValue ) );
          // the content base may not point to a bundle location, so that the path does not point to a
          // bundle location as well. If linked is computed to false, we can be sure that the resource is loaded
          // from within the bundle, which means that the attribute value was a relative path inside the bundle.
          final boolean linked = BundleUtilities.isSameBundle( elementSource, path ) == false;
          return new ResourceReference[] { new ResourceReference( path, linked ) };
        } catch ( ResourceKeyCreationException rce ) {
          // ignore ..
        }
      } else if ( attributeValue != null ) {
        try {
          // not a resource-key, so try to make one ..
          final ResourceKey path = resourceManager.createKey( attributeValue );
          final boolean linked = BundleUtilities.isSameBundle( elementSource, path ) == false;
          return new ResourceReference[] { new ResourceReference( path, linked ) };
        } catch ( ResourceKeyCreationException rce ) {
          // ignore ..
        }
      }

    }
    return EMPTY_RESOURCES;
  }

  public String[] getExtraCalculationFields( final ReportPreProcessorPropertyMetaData metaData ) {
    return new String[0];
  }
}
