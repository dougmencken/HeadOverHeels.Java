// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2024 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels.gui ;


/**
 * The foundation for creating elements of the user interface
 */

public abstract class Widget implements head.over.heels.Drawable
{

	private int whereX ;
	private int whereY ;

	private boolean onScreen = false ;

	public Widget( )
	{
		this( 0, 0 );
	}

	public Widget( int x, int y )
	{
		this.whereX = x ;
		this.whereY = y ;
	}

	/**
	 * Subclasses may handle this event or pass it to some other widget
	 */
	public abstract void handleKey ( String key ) ;

	// .....

}
