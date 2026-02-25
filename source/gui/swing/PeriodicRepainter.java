// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2026 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels.gui.swing ;

import javax.swing.JComponent ;

import java.awt.event.ActionEvent ;
import java.awt.event.ActionListener ;


public class PeriodicRepainter extends javax.swing.Timer implements ActionListener
{
	// the component for periodic repainting
	private JComponent repaintMe ;

	public PeriodicRepainter( JComponent component, int delayBetweenRepaints /* milliseconds */ )
	{
		super (	delayBetweenRepaints,
			null ); // ‘null’ here (not ‘this’) due to “cannot reference this before supertype constructor”

		this.repaintMe = component ;
		super.addActionListener( this );
	}

	public void actionPerformed( ActionEvent e ) {
		if ( this.repaintMe != null )
			this.repaintMe.repaint ();
	}

	public void start () {
		System.out.println( "starting the repaint timer" );
		super.start ();
	}

	public void stop () {
		System.out.println( "stopping the repaint timer" );
		super.stop ();
	}

}
