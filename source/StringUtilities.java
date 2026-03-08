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

	public static String reverseString( String in )
	{
		GrowingString out = GrowingStrings.newString() ;

		for ( int c = in.length() - 1 ; c >= 0 ; -- c )
			out.append( in.charAt( c ) );

		return out.toString() ;

		///return ( new StringBuffer( in ) ).reverse().toString() ;
	}

	public static String makeRandomString ( int length )
	{
		String characters = "0123456789"
					+ "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
						+ "abcdefghijklmnopqrstuvwxyz" ;
		int howManyChars = characters.length() ;

		GrowingString out = GrowingStrings.newString() ;
		java.util.Random random = new java.util.Random() ;

		for ( int i = 0 ; i < length; ++ i )
			out.append( characters.charAt( random.nextInt( howManyChars ) ) );

		return out.toString() ;
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
		String random = StringUtilities.makeRandomString( 67 );
		System.out.println( random );
		System.out.println( StringUtilities.reverseString( random ) );

		System.out.println( StringUtilities.fillGaps( "   - -   " ) );
		System.out.println( StringUtilities.fillGaps( " /     \\ " ) );
		System.out.println( StringUtilities.fillGaps( "|       |" ) );
		System.out.println( StringUtilities.fillGaps( " \\     / " ) );
		System.out.println( StringUtilities.fillGaps( "   - -   " ) );
	}

	private StringUtilities() {  super() ;  } // no instances

}
