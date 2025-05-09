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


package org.pentaho.reporting.engine.classic.core.modules.parser.ext.readhandlers;

import org.pentaho.reporting.engine.classic.core.modules.parser.base.PropertyAttributes;
import org.pentaho.reporting.engine.classic.core.modules.parser.base.common.AbstractPropertyXmlReadHandler;
import org.pentaho.reporting.engine.classic.core.modules.parser.base.compat.CompatibilityMapperUtil;
import org.pentaho.reporting.engine.classic.core.modules.parser.ext.factory.base.ArrayClassFactory;
import org.pentaho.reporting.engine.classic.core.modules.parser.ext.factory.base.ClassFactory;
import org.pentaho.reporting.engine.classic.core.modules.parser.ext.factory.base.ClassFactoryCollector;
import org.pentaho.reporting.engine.classic.core.modules.parser.ext.factory.base.JavaBaseClassFactory;
import org.pentaho.reporting.engine.classic.core.modules.parser.ext.factory.base.URLClassFactory;
import org.pentaho.reporting.libraries.base.util.ObjectUtilities;
import org.pentaho.reporting.libraries.xmlns.parser.ParseException;
import org.xml.sax.SAXException;

public class ClassFactoryReadHandler extends AbstractPropertyXmlReadHandler {
  public ClassFactoryReadHandler() {
  }

  /**
   * Starts parsing.
   *
   * @param attrs
   *          the attributes.
   * @throws org.xml.sax.SAXException
   *           if there is a parsing error.
   */
  protected void startParsing( final PropertyAttributes attrs ) throws SAXException {
    String className = CompatibilityMapperUtil.mapClassName( attrs.getValue( getUri(), "class" ) );
    if ( className == null ) {
      throw new ParseException( "Attribute 'class' is missing.", getRootHandler().getDocumentLocator() );
    }

    // some legacy mappings (0.8.7 and before)
    if ( "org.jfree.xml.factory.objects.ArrayClassFactory".equals( className ) ) {
      className = ArrayClassFactory.class.getName();
    }
    if ( "org.jfree.xml.factory.objects.JavaBaseClassFactory".equals( className ) ) {
      className = JavaBaseClassFactory.class.getName();
    }
    if ( "org.jfree.xml.factory.objects.URLClassFactory".equals( className ) ) {
      className = URLClassFactory.class.getName();
    }

    final ClassFactoryCollector fc =
        (ClassFactoryCollector) getRootHandler().getHelperObject( ReportDefinitionReadHandler.CLASS_FACTORY_KEY );

    final ClassFactory factory =
        (ClassFactory) ObjectUtilities.loadAndInstantiate( className, getClass(), ClassFactory.class );
    if ( factory != null ) {
      factory.configure( getRootHandler().getParserConfiguration() );
      fc.addFactory( factory );
    }
  }

  /**
   * Returns the object for this element or null, if this element does not create an object.
   *
   * @return the object.
   */
  public Object getObject() {
    return null;
  }
}
