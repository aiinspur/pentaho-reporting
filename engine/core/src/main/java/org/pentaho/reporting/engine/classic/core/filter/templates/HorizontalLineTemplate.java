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


package org.pentaho.reporting.engine.classic.core.filter.templates;

import org.pentaho.reporting.engine.classic.core.ReportElement;
import org.pentaho.reporting.engine.classic.core.function.ExpressionRuntime;

import java.awt.geom.Line2D;

/**
 * Defines a horizontal line template. The line always has the width of 100 points. This implementation is used to cover
 * the common use of the line shape element. Use the scaling feature of the shape element to adjust the size of the
 * line.
 *
 * @author Thomas Morgner
 */
public class HorizontalLineTemplate extends AbstractTemplate {
  /**
   * Default Constructor.
   */
  public HorizontalLineTemplate() {
  }

  /**
   * Returns the template value, an horizontal line.
   *
   * @param runtime
   *          the expression runtime that is used to evaluate formulas and expressions when computing the value of this
   *          filter.
   * @param element
   * @return a horizontal line with a width of 100.
   */
  public Object getValue( final ExpressionRuntime runtime, final ReportElement element ) {
    return new Line2D.Float( 0, 0, 100, 0 );
  }
}
