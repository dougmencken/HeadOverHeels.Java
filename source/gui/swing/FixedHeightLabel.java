// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2026 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels.gui.swing ;

import javax.swing.JLabel ;

import java.awt.Dimension ;
import java.awt.Font ;


/**
 * A JLabel with a fixed height, where only the width can be adjusted dynamically.
 * It ensures a constant height across all layout calculations.
 *
 * In Swing layouts, especially with layout managers such as GridBagLayout or BoxLayout,
 * components are measured many times during layout passes. A JLabel may return slightly
 * different preferred heights, for example, when the text content changes (via setText).
 * These small variations can cause the container’s preferred size to alter between
 * layout passes. When used inside a top-level window (like JFrame), this may lead to
 * visible effects, such as window “jumping” and jittery resizing.
 *
 * This class overrides getPreferredSize(), getMinimumSize(), and getMaximumSize()
 * to guarantee height consistency across layout passes
 */

public class FixedHeightLabel extends JLabel
{
	private int fixedHeight = 0 ;

	public int getFixedHeight () {  return this.fixedHeight ;  }

	/**
	 * Sets a fixed height for this label
	 *
	 * @param  height	the desired height in pixels,
	 *			0 or a negative value cancels fixing
	 */
	public void setFixedHeight( int height ) {
		this.fixedHeight = ( height > 0 ) ? height : 0 ;
	}

	/**
	 * Cancels the fixed height constraint, turning this label into “just a” JLabel
	 */
	public void unfixHeight () {  this.fixedHeight = 0 ;  }

	/**
	 * Sets the constant height derived from the label font’s metrics
	 */
	public void fixFontMetricsHeight () {
		this.fixedHeight = super.getFontMetrics( super.getFont() ).getHeight() ;
	}

	// the no-argument constructor creates a pangram label
	public FixedHeightLabel( ) {  this( "quick nymph bugs vex fjord waltz" ) ;  }

	public FixedHeightLabel( String text ) {
		super( text );
		this.fixFontMetricsHeight() ;
	}

	public void setFont( Font font ) /* @Override */ {
		super.setFont( font );
		if ( this.fixedHeight > 0 ) fixFontMetricsHeight() ;
	}

	public Dimension getPreferredSize () /* @Override */ {
		Dimension preferred = super.getPreferredSize() ;

		if ( this.fixedHeight > 0 )
			preferred.height = this.fixedHeight ;

		return preferred ;
	}

	public Dimension getMinimumSize () /* @Override */ {
		Dimension minimum = super.getMinimumSize() ;

		if ( this.fixedHeight > 0 )
			minimum.height = this.fixedHeight ;

		return minimum ;
	}

	public Dimension getMaximumSize () /* @Override */ {
		Dimension maximum = super.getMaximumSize() ;

		if ( this.fixedHeight > 0 )
			maximum.height = this.fixedHeight ;

		return maximum ;
	}

}
