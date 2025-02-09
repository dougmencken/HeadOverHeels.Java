// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2025 Douglas Mencken dougmencken@gmail.com
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

		StringBuilder result = new StringBuilder() ;
		result.append( number );

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

	public static String putInQuotes( String in )
	{
		return "\"" + in + "\"" ;
	}

	public static String reverseString( String in )
	{
		/* StringBuilder out = new StringBuilder() ;

		for ( int c = in.length() - 1 ; c >= 0 ; -- c )
			out.append( in.charAt( c ) );

		return out.toString () ; */

		return ( new StringBuilder( in ) ).reverse().toString () ;
	}

	public static String makeRandomString ( int length )
	{
		String characters = "0123456789"
					+ "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
						+ "abcdefghijklmnopqrstuvwxyz" ;
		int howManyChars = characters.length() ;

		StringBuilder out = new StringBuilder() ;
		java.util.Random random = new java.util.Random() ;

		for ( int i = 0 ; i < length; ++ i )
			out.append( characters.charAt( random.nextInt( howManyChars ) ) );

		return out.toString() ;
	}

	public static void main( String[] ignored )
	{
		System.out.println( StringUtilities.makeRandomString( 16 ) );
	}

}
