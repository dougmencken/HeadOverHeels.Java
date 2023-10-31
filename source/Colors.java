// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2023 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels ;

import java.awt.Color ;
import java.awt.image.BufferedImage ;


/**
 * The selection of colors and utilities to deal with colors
 */

public class Colors
{

	public static final Color white = new Color( 255, 255, 255, 0xff ) ;

	public static final Color black = new Color( 0, 0, 0, 0xff ) ;

	public static final Color blue = new Color( 0, 0, 255, 0xff ) ;
	public static final Color red = new Color( 255, 0, 0, 0xff ) ;
	public static final Color magenta = new Color( 255, 0, 255, 0xff ) ;
	public static final Color green = new Color( 0, 255, 0, 0xff ) ;
	public static final Color cyan = new Color( 0, 255, 255, 0xff ) ;
	public static final Color yellow = new Color( 255, 255, 0, 0xff ) ;

	public static final Color magenta1green = new Color( 255, 1, 255, 0xff ) ;  // can be used where the "pure" magenta means transparency

	public static final Color darkBlue = new Color( 0, 0, 127, 0xff ) ;

	public static final Color orange = new Color( 255, 127, 0, 0xff ) ;

	public static final Color gray50 = new Color( 127, 127, 127, 0xff ) ;       // 50% gray
	public static final Color gray75white = new Color( 191, 191, 191, 0xff ) ;  // gray 75% white 25% black
	public static final Color gray25white = new Color( 63, 63, 63, 0xff ) ;     // gray 25% white 75% black

	public static final Color reducedWhite = Colors.gray75white ;

	public static final Color reducedBlue = new Color( 0, 0, 191, 0xff ) ;
	public static final Color reducedRed = new Color( 191, 0, 0, 0xff ) ;
	public static final Color reducedMagenta = new Color( 191, 0, 191, 0xff ) ;
	public static final Color reducedGreen = new Color( 0, 191, 0, 0xff ) ;
	public static final Color reducedCyan = new Color( 0, 191, 191, 0xff ) ;
	public static final Color reducedYellow = new Color( 191, 191, 0, 0xff ) ;

	public static final Color lightBlue = new Color( 127, 127, 255, 0xff ) ;
	public static final Color lightRed = new Color( 255, 127, 127, 0xff ) ;
	public static final Color lightMagenta = new Color( 255, 127, 255, 0xff ) ;
	public static final Color lightGreen = new Color( 127, 255, 127, 0xff ) ;
	public static final Color lightCyan = new Color( 127, 255, 255, 0xff ) ;
	public static final Color lightYellow = new Color( 255, 255, 127, 0xff ) ;

	public static boolean isFullyTransparent( Color c )
	{
		return c.getAlpha() == 0 || ( c.getRed() == 255 && c.getGreen() == 0 && c.getBlue() == 255 ) ;
	}

	private Colors() {} // no instances, just static methods and constants

}
