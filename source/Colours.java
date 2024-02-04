// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2024 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels ;

import java.awt.Color ;
import java.awt.image.BufferedImage ;


/**
 * The selection of colours and utilities to deal with colours
 */

public class Colours
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

	public static final Color grey50 = new Color( 127, 127, 127, 0xff ) ;       // half (50%) white half (50%) black
	public static final Color grey75white = new Color( 191, 191, 191, 0xff ) ;  // ¾ (or 75%) white ¼ (or 25%) black
	public static final Color grey25white = new Color( 63, 63, 63, 0xff ) ;     // ¼ (or 25%) white ¾ (or 75%) black

	public static final Color gray50 = grey50 ;
	public static final Color gray75white = grey75white ;
	public static final Color gray25white = grey25white ;

	public static final Color reducedWhite = Colours.grey75white ;

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
		return c.getAlpha() == 0 ; /// || /* the "pure" magenta */ ( c.getRed() == 255 && c.getGreen() == 0 && c.getBlue() == 255 ) ;
	}

	public static Color byName( String name )
	{
		if ( name.equals( "white" ) ) return Colours.white ;
		if ( name.equals( "black" ) ) return Colours.black ;

		if ( name.equals( "blue" ) ) return Colours.blue ;
		if ( name.equals( "red" ) ) return Colours.red ;
		if ( name.equals( "magenta" ) ) return Colours.magenta ;
		if ( name.equals( "green" ) ) return Colours.green ;
		if ( name.equals( "cyan" ) ) return Colours.cyan ;
		if ( name.equals( "yellow" ) ) return Colours.yellow ;

		if ( name.equals( "dark blue" ) || name.equals( "darkblue" ) || name.equals( "blue.dark" ) ) return Colours.darkBlue ;

		if ( name.equals( "orange" ) ) return Colours.orange ;

		if ( name.contains( "grey" ) ) name = name.replace( "grey", "gray" );

		if ( name.equals( "gray" ) || name.equals( "gray50" )
			|| name.equals( "50% gray" ) || name.equals( "gray 50%" )
				|| name.equals( "light black" ) || name.equals( "black.light" ) ) return Colours.grey50 ;

		if ( name.equals( "gray75" )
			|| name.equals( "gray 75% white" )
				|| name.equals( "reduced white" ) || name.equals( "white.reduced" ) ) return Colours.grey75white ;

		if ( name.equals( "gray25" ) || name.equals( "gray 25% white" ) ) return Colours.grey25white ;

		if ( name.contains( "reduced" ) ) {
			if ( name.equals( "reduced black" ) || name.equals( "black.reduced" ) ) return Colours.black ; // can't reduce it more

			if ( name.equals( "reduced blue" ) || name.equals( "blue.reduced" ) ) return Colours.reducedBlue ;
			if ( name.equals( "reduced red" ) || name.equals( "red.reduced" ) ) return Colours.reducedRed ;
			if ( name.equals( "reduced magenta" ) || name.equals( "magenta.reduced" ) ) return Colours.reducedMagenta ;
			if ( name.equals( "reduced green" ) || name.equals( "green.reduced" ) ) return Colours.reducedGreen ;
			if ( name.equals( "reduced cyan" ) || name.equals( "cyan.reduced" ) ) return Colours.reducedCyan ;
			if ( name.equals( "reduced yellow" ) || name.equals( "yellow.reduced" ) ) return Colours.reducedYellow ;
		}

		if ( name.contains( "light" ) ) {
			if ( name.equals( "light blue" ) || name.equals( "blue.light" ) ) return Colours.lightBlue ;
			if ( name.equals( "light red" ) || name.equals( "red.light" ) ) return Colours.lightRed ;
			if ( name.equals( "light magenta" ) || name.equals( "magenta.light" ) ) return Colours.lightMagenta ;
			if ( name.equals( "light green" ) || name.equals( "green.light" ) ) return Colours.lightGreen ;
			if ( name.equals( "light cyan" ) || name.equals( "cyan.light" ) ) return Colours.lightCyan ;
			if ( name.equals( "light yellow" ) || name.equals( "yellow.light" ) ) return Colours.lightYellow ;

			if ( name.equals( "light white" ) || name.equals( "white.light" ) ) return Colours.white ; // can't lighten it more
		}

		if ( name.equals( "lime green" ) ) return new java.awt.Color( 0x33, 0xd1, 0x3f );
		if ( name.equals( "vivid yellow" ) ) return new java.awt.Color( 0xff, 0xe4, 0x01 );
		if ( name.equals( "fulvous" ) ) return new java.awt.Color( 0xe4, 0x84, 0 );

		throw new IllegalArgumentException( "unknown colour \"" + name + "\" in Colours.byName" );
	}

	private Colours() {} // no instances, just static methods and constants

}
