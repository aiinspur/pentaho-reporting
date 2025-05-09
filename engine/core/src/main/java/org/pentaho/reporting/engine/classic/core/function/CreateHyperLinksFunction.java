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

import org.pentaho.reporting.engine.classic.core.ReportElement;
import org.pentaho.reporting.engine.classic.core.style.ElementStyleKeys;
import org.pentaho.reporting.libraries.base.util.ObjectUtilities;

/**
 * Adds hyperlinks to all elements with the name specified in 'element'. The link target is read from a specified field.
 * The column referenced by this field should contain URLs or Strings.
 *
 * @author Thomas Morgner
 * @deprecated add style expressions to the 'href-target' and 'href-window' instead. It is much easier and less
 *             confusing.
 */
public class CreateHyperLinksFunction extends AbstractElementFormatFunction {
  /**
   * The field name from where to read the hyper-link target.
   */
  private String field;
  /**
   * The field from where to read the link target-window specifier.
   */
  private String windowField;
  /**
   * The constant target window. This property is used if no window field was set.
   */
  private String target;

  /**
   * Default Constructor.
   */
  public CreateHyperLinksFunction() {
  }

  /**
   * Returns the field name from where to read the hyper-link target.
   *
   * @return the name of the field.
   */
  public String getField() {
    return field;
  }

  /**
   * Defines the field name from where to read the hyper-link target.
   *
   * @param field
   *          a field name.
   */
  public void setField( final String field ) {
    this.field = field;
  }

  /**
   * Returns the target window. This property is used if no window field was set. This is only meaningful for HTML
   * exports.
   *
   * @return the target window string.
   */
  public String getTarget() {
    return target;
  }

  /**
   * Defines the target window. This property is used if no window field was set. This is only meaningful for HTML
   * exports.
   *
   * @param target
   *          the target window string.
   */
  public void setTarget( final String target ) {
    this.target = target;
  }

  /**
   * Returns the datarow-field from where to read the target window. This is only meaningful for HTML exports.
   *
   * @return the fieldname from where to read the target window string.
   */
  public String getWindowField() {
    return windowField;
  }

  /**
   * Defines the datarow-field from where to read the target window. This is only meaningful for HTML exports.
   *
   * @param windowField
   *          the fieldname from where to read the target window string.
   */
  public void setWindowField( final String windowField ) {
    this.windowField = windowField;
  }

  protected boolean evaluateElement( final ReportElement e ) {
    if ( ObjectUtilities.equal( e.getName(), getElement() ) ) {
      String hrefLinkTarget = null;
      final Object targetRaw = getDataRow().get( getField() );
      if ( targetRaw != null ) {
        hrefLinkTarget = String.valueOf( targetRaw );
      }

      if ( hrefLinkTarget == null ) {
        return false;
      }

      final String windowField = getWindowField();
      final String window;
      if ( windowField != null ) {
        final Object windowRaw = getDataRow().get( windowField );
        if ( windowRaw != null ) {
          window = String.valueOf( windowRaw );
        } else {
          window = null;
        }
      } else {
        window = getTarget();
      }

      e.getStyle().setStyleProperty( ElementStyleKeys.HREF_TARGET, hrefLinkTarget );
      if ( window != null ) {
        e.getStyle().setStyleProperty( ElementStyleKeys.HREF_WINDOW, window );
      }
      return true;
    }
    return false;
  }
}
