// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2023 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels.gui ;

/**
 * The font with letters from the picture file. Letters may have a non-white color or~and the double height
 */

public class Font
{

	private String fontName ;

	private String fontColor ;

	public Font( String name, String color )
	{
		this( name, color, false );
	}

	/**
	 * @param name the name of this font
	 * @param color the color of letters
	 * @param doubleHeight true for the double height stretching of letters
	 */
	public Font( String name, String color, boolean doubleHeight )
	{
		fontName = name ;
		fontColor = color ;

		// stretch for the double height
		if ( doubleHeight )
			System.out.println( "I don't know how" );
	}

	// ?? //private Font( Font f ) {} // doesn't copy

}
