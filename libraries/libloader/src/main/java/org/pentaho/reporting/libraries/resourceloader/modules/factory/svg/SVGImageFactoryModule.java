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


package org.pentaho.reporting.libraries.resourceloader.modules.factory.svg;

import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.DocumentLoader;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.gvt.GraphicsNode;
import org.pentaho.reporting.libraries.resourceloader.Resource;
import org.pentaho.reporting.libraries.resourceloader.ResourceData;
import org.pentaho.reporting.libraries.resourceloader.ResourceKey;
import org.pentaho.reporting.libraries.resourceloader.ResourceLoadingException;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;
import org.pentaho.reporting.libraries.resourceloader.SimpleResource;
import org.w3c.dom.svg.SVGDocument;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Creation-Date: 05.04.2006, 17:58:42
 *
 * @author Thomas Morgner
 */
public class SVGImageFactoryModule extends AbstractSVGFactoryModule {
  public SVGImageFactoryModule() {
  }

  public Resource create( final ResourceManager caller,
                          final ResourceData data,
                          final ResourceKey context )
    throws ResourceLoadingException {
    try {
      final long version = data.getVersion( caller );
      final HeadlessSVGUserAgent userAgent = new HeadlessSVGUserAgent();
      final DocumentLoader loader = new DocumentLoader( userAgent );
      final ResourceKey key = data.getKey();
      URL url = caller.toURL( key );
      if ( url == null ) {
        url = new File( "." ).toURI().toURL();
      }
      final SVGDocument document = (SVGDocument) loader.loadDocument
        ( url.toURI().toASCIIString(), data.getResourceAsStream( caller ) );
      final BridgeContext ctx = new BridgeContext( userAgent, loader );
      final GVTBuilder builder = new GVTBuilder();
      final GraphicsNode node = builder.build( ctx, document );
      final Rectangle2D bounds = node.getBounds();

      final SVGDrawable drawable = new SVGDrawable( node );
      final BufferedImage bi =
        new BufferedImage( (int) bounds.getWidth(), (int) bounds.getHeight(), BufferedImage.TYPE_INT_ARGB );
      final Graphics2D graphics = (Graphics2D) bi.getGraphics();
      drawable.draw( graphics, new Rectangle2D.Double( 0, 0, bounds.getWidth(), bounds.getHeight() ) );
      graphics.dispose();
      return new SimpleResource( data.getKey(), bi, Image.class, version );
    } catch ( IOException e ) {
      throw new ResourceLoadingException( "Failed to process SVG file", e );
    } catch ( URISyntaxException e ) {
      throw new ResourceLoadingException( "Failed to process SVG file", e );
    }
  }

}
