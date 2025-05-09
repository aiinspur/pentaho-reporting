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


package org.pentaho.reporting.engine.classic.core.states.process;

import org.pentaho.reporting.engine.classic.core.ReportProcessingException;

/**
 * This handler deferrs the event progression by one "advance" call, so that we can hopefully clean up the pages and
 * generate some page-events.
 *
 * @author Thomas Morgner.
 */
public class PendingPagesHandler implements AdvanceHandler {
  private AdvanceHandler handler;

  public PendingPagesHandler( final AdvanceHandler handler ) {
    if ( handler == null ) {
      throw new NullPointerException();
    }
    this.handler = handler;
  }

  public ProcessState advance( final ProcessState state ) throws ReportProcessingException {
    final ProcessState newState = state.deriveForAdvance();
    newState.getFlowController().getMasterRow().refresh();
    newState.getLayoutProcess().restart( newState );
    return newState;
  }

  public ProcessState commit( final ProcessState state ) throws ReportProcessingException {
    final ProcessState nstate = state.deriveForAdvance();
    nstate.setAdvanceHandler( handler );
    return handler.commit( nstate );
  }

  public boolean isFinish() {
    return handler.isFinish();
  }

  public int getEventCode() {
    return handler.getEventCode();
  }

  public static ProcessState create( final ProcessState state ) {
    if ( state.getAdvanceHandler() instanceof PendingPagesHandler ) {
      return state.deriveForAdvance();
    }
    final ProcessState newstate = state.deriveForAdvance();
    newstate.setAdvanceHandler( new PendingPagesHandler( newstate.getAdvanceHandler() ) );
    return newstate;
  }

  public boolean isRestoreHandler() {
    return true;
  }
}
