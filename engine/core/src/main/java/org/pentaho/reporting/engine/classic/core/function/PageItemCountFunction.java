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

import org.pentaho.reporting.engine.classic.core.event.PageEventListener;
import org.pentaho.reporting.engine.classic.core.event.ReportEvent;

/**
 * An ItemCount function, that is reset to zero on every new page.
 *
 * @author Thomas Morgner
 */
public class PageItemCountFunction extends ItemCountFunction implements PageEventListener {
  /**
   * Default Constructor.
   */
  public PageItemCountFunction() {
  }

  /**
   * Handles the pageStartedEvent.
   *
   * @param event
   *          the report event.
   */
  public void pageStarted( final ReportEvent event ) {
    clear();
  }

  /**
   * Handles the pageFinishedEvent. This method is emtpy and only here as implementation side effect.
   *
   * @param event
   *          the report event.
   */
  public void pageFinished( final ReportEvent event ) {
  }
}
