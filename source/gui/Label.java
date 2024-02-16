// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2024 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels.gui ;


/**
 * The line of text
 */

public class Label extends Widget
{

	public Label( String text )
	{
		this( text, new Font(), false );
	}

	public Label( String text, Font font )
	{
		this( text, font, false );
	}

	/**
	 * @param text the text of this label
	 * @param font the font to draw this label
	 * @param multicolor true for coloring letters in the cycle
	 */
	public Label( String text, Font font, boolean multicolor )
	{
		// ....
	}

	public void draw ()
	{
		// ...
	}

	public void handleKey ( String key )
	{
		// ...
	}

	// .....

}
