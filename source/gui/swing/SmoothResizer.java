// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2026 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels.gui.swing ;


public class SmoothResizer {

	private static javax.swing.Timer smoothResizeTimer = null ;

	/**
	 * @param  window          the window to resize
	 * @param  desiredSize     the desired final size of the frame
	 * @param  duration        transition duration in milliseconds
	 * @param  anchorToCenter  center anchoring if true, top-left anchoring if false
	 */
	public static void smoothlyResize (  final java.awt.Window window,
						java.awt.Dimension desiredSize,
						int duration /* in milliseconds */,
						final boolean anchorToCenter  )
	{
		if ( smoothResizeTimer != null )
			if ( smoothResizeTimer.isRunning() )
				smoothResizeTimer.stop() ; // stop previous transition

		final java.awt.Rectangle from = window.getBounds() ;

		java.awt.Point toPoint = from.getLocation() ;

		// optional center anchoring
		if ( anchorToCenter ) {
			toPoint.x += ( from.width - desiredSize.width ) >> 1 ;
			toPoint.y += ( from.height - desiredSize.height ) >> 1 ;
		}

		final java.awt.Rectangle to = new java.awt.Rectangle( toPoint, desiredSize );

		final int fps = 50 ;
		final int delay = 1000 / fps ;
		final int steps = Math.max( 1, duration / delay );

		smoothResizeTimer = new javax.swing.Timer( delay, new java.awt.event.ActionListener() {

				private int step = 0 ;

				public void actionPerformed( java.awt.event.ActionEvent ae ) {
					this.step ++ ;
					float t = this.step / (float) steps ;

					// easing with slope(t)
					///float slope = t ; // linear
					///float slope = (float)( -Math.cos(Math.PI * t) / 2.0 + 0.5 ) ;
					float slope = 1 - (1 - t)*(1 - t) ;

					///int x = (int)( from.x + (to.x - from.x) * slope );
					///int y = (int)( from.y + (to.y - from.y) * slope );
					int x = from.x ;
					int y = from.y ;
					int w = (int)( from.width  + ( to.width - from.width ) * slope );
					int h = (int)( from.height + (to.height - from.height) * slope );

					if ( anchorToCenter ) {
						x += ( from.width - w ) >> 1 ;
						y += ( from.height - h ) >> 1 ;
					}

					window.setBounds( x, y, w, h );

					if ( this.step >= steps ) {
						// timer is the source of the event
						( (javax.swing.Timer) ae.getSource() ).stop() ;

						// final deferred correction
						javax.swing.SwingUtilities.invokeLater(
							new DeferredReshaper( window, to.getLocation(), to.getSize() )
						) ;
					}
				}
		} );

		smoothResizeTimer.start () ;
	}

}
