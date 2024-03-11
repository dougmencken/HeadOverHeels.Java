// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2024 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels.gui ;

import java.awt.image.BufferedImage ;
import java.util.Vector ;


/**
 * The menu presents the user with a list of options to choose from
 *
 * options are chosen by the Up Arrow or Q / Down Arrow or A keys
 * and are selected by using the Enter or Space key
 */

public class Menu extends Widget
{
	/**
	 * Options that make up the menu
	 */
	private Vector < Label > options ;

	/**
	 * The chosen option
	 */
	private Label activeOption ;

	public void draw ( java.awt.Graphics2D g )
	{
		// ...
	}

	public void handleKey ( String key )
	{
		// ...
	}

	/**
	 * Pick the previous option of this menu
	 */
	protected void previousOption ()
	{
		// ...
	}

	/**
	 * Pick the next option of this menu
	 */
	protected void nextOption ()
	{
		// ...
	}

	/**
	 * The picture to show before a menu option
	 */
	private static BufferedImage pictureBeforeOption = null ;

	/**
	 * The picture to show before a chosen menu option
	 */
	private static BufferedImage pictureBeforeChosenOption = null ;

	/**
	 * The picture to show before a chosen but not double height menu option
	 */
	private static BufferedImage pictureBeforeChosenOptionSingle = null ;

	private static void makePicturesBeforeOptions ()
	{	makePicturesBeforeOptions( Font.Shadow_Shift, Font.Shadow_Shift );	}

	private static void makePicturesBeforeOptions ( int offsetForTintX, int offsetForTintY )
	{
		// ...
	}

	// .....

}
