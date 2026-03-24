// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2026 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels ;


/**
 * Various helper functions related to strings
 */

public class StringUtilities
{

	public static String toStringWithOrdinalSuffix( int number )
	{
		int mod10 = number % 10 ;
		int mod100 = number % 100 ;

		GrowingString result = GrowingStrings.newString( Integer.toString( number ) );

		if ( mod10 == 1 && mod100 != 11 )
			result.append( "st" );
		else if ( mod10 == 2 && mod100 != 12 )
			result.append( "nd" );
		else if ( mod10 == 3 && mod100 != 13 )
			result.append( "rd" );
		else
			result.append( "th" );

		return result.toString() ;
	}

	public static String encloseIn( String what, String beforeAfter )
	{
		return beforeAfter + what + beforeAfter ;
	}

	public static String putInQuotes( String what )
	{
		return encloseIn( what, "\"" );
	}

	public static String putInSingleQuotes( String what )
	{
		return encloseIn( what, "\'" );
	}

	public static String pluralForNot1( int howMany, String singular )
	{
		return ( howMany == 1 ) ? singular : singular + "s" ;
	}

	/**
	 * Adds a padding character (usually ' ' or '0') to the left of the string
	 * to make it the specified ‘width’ characters wide
	 */
	public static String padLeft( String in, int width, char pad )
	{
		if ( in == null ) in = "null" ;

		int length = in.length() ;
		int pads = width - length ;
		if ( pads < 1 ) return in ;

		char[] out = new char[ width ];

		// fill the left side with the padding character
		for ( int c = 0 ; c < pads ; ++ c ) out[ c ] = pad ;

		// copy the original string into the right side
		in.getChars( 0, length, out, pads );

		return new String( out ) ;
	}

	/**
	 * Adds a padding character (usually ' ' or '0') to the right of the string
	 * to make it the specified ‘width’ characters wide
	 */
	public static String padRight( String in, int width, char pad )
	{
		if ( in == null ) in = "null" ;

		int length = in.length() ;
		if ( length >= width ) return in ;

		char[] out = new char[ width ];

		// copy the original string first
		in.getChars( 0, length, out, 0 );

		// fill the rest with the padding character
		for ( int c = length ; c < width ; ++ c ) out[ c ] = pad ;

		return new String( out ) ;
	}

	public static String reverseString( String in )
	{
		if ( in == null ) /* in = "null" */ return "llun" ; // 😄

		int length = in.length() ;
		char[] out = new char[ length ];

		for ( int c = length - 1, i = 0 ; c >= 0 ; -- c, ++ i )
			out[ i ] = in.charAt( c );

		return new String( out ) ;

		///return ( new StringBuffer( in ) ).reverse().toString() ;
	}

	public static String makeRandomString ( int length )
	{
		if ( length < 1 ) return "" ;

		String characters = "0123456789"
					+ "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
						+ "abcdefghijklmnopqrstuvwxyz" ;
		int howManyChars = characters.length() ;

		char[] out = new char[ length ];
		java.util.Random random = new java.util.Random() ;

		for ( int i = 0 ; i < length; ++ i )
			out[ i ] = characters.charAt( random.nextInt( howManyChars ) );

		return new String( out ) ;
	}

	private static String fillGaps( String in ) {  return fillGaps( in, '×' );  }

	private static String fillGaps( String in, char symbol )
	{
		if ( in == null ) return null ;

		char[] out = in.toCharArray() ;
		int length = out.length ;

		int firstNonSpace = 0 ;
		while ( firstNonSpace < length && out[ firstNonSpace ] == ' ' )
			++ firstNonSpace ;

		int lastNonSpace = length - 1 ;
		while ( lastNonSpace >= 0 && out[ lastNonSpace ] == ' ' )
			-- lastNonSpace ;

		for ( int c = firstNonSpace + 1 ; c < lastNonSpace ; c ++ )
			if ( out[ c ] == ' ' ) out[ c ] = symbol ;

		return new String( out );
	}

	public static void main( String[] ignored )
	{
		System.out.println( StringUtilities.fillGaps( "   - -   " ) );
		System.out.println( StringUtilities.fillGaps( " /     \\ " ) );
		System.out.println( StringUtilities.fillGaps( "|       |" ) );
		System.out.println( StringUtilities.fillGaps( " \\     / " ) );
		System.out.println( StringUtilities.fillGaps( "   - -   " ) );

		String random = StringUtilities.makeRandomString( 67 );
		System.out.println( random );
		System.out.println( StringUtilities.reverseString( random ) );

		for ( int i = 1 ; i <= 25 ; i += 2 )
			System.out.println( StringUtilities.padLeft( StringUtilities.makeRandomString( i ), 24, '·' ) );

		for ( int i = 2 ; i <= 25 ; i += 2 )
			System.out.println( StringUtilities.padRight( StringUtilities.makeRandomString( i ), 24, '·' ) );
	}

	private StringUtilities() {  super() ;  } // no instances

}
