// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2026 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels.gui.swing ;

import java.awt.Window ;

import java.awt.Point ;
import java.awt.Dimension ;

import javax.swing.SwingUtilities ;

/**
 * As written in the java.awt.Window class documentation,
 *   “ the location and size of top-level windows (including Windows, Frames, and Dialogs) are under
 *     the control of the desktop's window management system. Calls to setLocation, setSize, and setBounds
 *     are requests (not directives) which are forwarded to the window management system ”
 *
 * So it’s the operating system’s window manager that decides where to place the window,
 * and it may ignore or change the desired location. Sometimes after using setSize or setBounds,
 * the window’s position on the screen changes unexpectedly, becoming not equal to the requested
 * coordinates, because the window manager applies its correction after everything Swing does.
 * That’s when the DeferredReshaper class comes in handy
 *
 * Typical use
 *      setPreferredSize( desiredSize );
 *      SwingUtilities.invokeLater(new DeferredReshaper( this, getLocation(), getPreferredSize(), 2 ));
 *
 * DeferredReshaper takes the window, the desired location and size, and optionally the number
 * of event loop cycles (in case one isn’t enough) to invoke setBounds on the given window.
 * The famous SwingUtilities.invokeLater method run()s a Runnable object after all pending
 * user interface events have been processed (at the end of an event loop cycle)
 */

public class DeferredReshaper implements Runnable {

	private final Window window ;

	private final int x, y ;
	private final int w, h ;

	private int remainingRuns ;

	public DeferredReshaper( Window window, Point at, Dimension size ) {
		this( window, at, size, 1 );
	}

	/**
	 * @param  window   the window to reshape (setBounds on)
	 * @param  at       the desired location or null for the current location of the window
	 * @param  size     the desired size or null for the current size of the window
	 * @param  times    how many times to request reshaping
	 */
	public DeferredReshaper( Window window, Point at, Dimension size, int times )
	{
		if ( window == null ) throw new IllegalArgumentException( "can’t reshape null" );
		this.window = window ;

		if ( at == null ) at = window.getLocation() ;
		if ( size == null ) size = window.getSize() ;

		this.x = at.x ;			// copy the Point and Dimension
		this.y = at.y ;			// objects to the primitives
		this.w = size.width ;		// to avoid surprises if those
		this.h = size.height ;		// objects get mutated elsewhere

		this.remainingRuns = ( times > 1 ) ? times : 1 ;
	}

	/* @Override */
	public void run () {
		this.window.setBounds( this.x, this.y, this.w, this.h );

		-- this.remainingRuns ;

		if ( this.remainingRuns > 0 )
			// reschedule itself
			SwingUtilities.invokeLater( this );
	}

}
