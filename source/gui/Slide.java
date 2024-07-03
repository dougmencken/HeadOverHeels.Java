// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2024 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels.gui ;


/**
 * A container for user interface elements
 */

public class Slide extends Widget
{

	public void addWidget ( Widget theWidget )
	{
		// ...
		theWidget.setContainingSlide( this );
	}

	public boolean removeWidget ( Widget theWidget )
	{
		// ...
		return false ;
	}

	public void draw ( java.awt.Graphics2D g )
	{
		// ....
	}

	public void handleKey ( String key )
	{
		// .....
	}

}
