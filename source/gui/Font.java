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
		this.fontName = name ;
		this.fontColor = color ;

		TableOfLetters lettersMapping = new TableOfLetters( "gamedata" + java.io.File.separator + "letters.utf8" );
		System.out.println( lettersMapping );

		// stretch for the double height
		if ( doubleHeight )
			System.out.println( "I don't know how" );
	}

	// ?? //private Font( Font f ) {} // doesn't copy

}

class TableOfLetters
{

	/**
	 * The table of mapping between the letter from the font picture file and the UTF-8 code for this letter
	 */
	String [] tableOfLetters ;

	TableOfLetters( String nameOFile )
	{
		java.io.File lettersFile = new java.io.File( nameOFile );
		if ( ! lettersFile.exists() || ! lettersFile.isFile() || ! lettersFile.canRead() ) {
			System.out.println( "can't read file \"" + nameOFile + "\" with the table of letters" );
			return ;
		}

		int lengthOFile = (int) lettersFile.length () ;
		byte [] bytes = new byte [ lengthOFile ];
		int bytesRead = 0 ;
		try ( java.io.FileInputStream stream = new java.io.FileInputStream( lettersFile ) )
		{
			bytesRead = stream.read( bytes );
		}
		catch ( java.io.IOException e ) {  return ;  }

		// at first, count the letters
		int howManyLetters = 0 ;
		for ( int at = 0 ; at < bytesRead ; at ++ ) {
			short b = (short) ( ( (int) bytes[ at ] ) & 0xff );
			if ( ( b == 0 ) || ( ( b & 0x80 ) == 0 ) || ( ( b & 0xC0 ) == 0xC0 ) )
				howManyLetters++;
		}
		System.out.println( "🧐 file \"" + nameOFile + "\" has table for " + howManyLetters + " letters" );

		this.tableOfLetters = new String [ howManyLetters ];
		int inTable = 0;
		for ( int inBytes = 0 ; inBytes < bytesRead ; ) {
			short b = (short) ( ( (int) bytes[ inBytes ] ) & 0xff );
			if ( b == 0 ) {
				tableOfLetters[ inTable ++ ] = "" ;
				inBytes ++ ;
			} else {
				byte [] letter = new byte [ 5 ];
				int bytesInLetter = 0 ;
				do {
					letter[ bytesInLetter ++ ] = (byte) b ;
					b = (short) ( ( (int) bytes[ ++ inBytes ] ) & 0xff );
				}
				while ( ( ( b & 0x80 ) != 0 ) && ( ( b & 0xC0 ) != 0xC0 )
						&& ( bytesInLetter < 4 ) && ( inBytes < bytesRead ) ) ;

				letter[ bytesInLetter ] = 0 ; // end of string

				try {
					this.tableOfLetters[ inTable ] = new String( letter, "UTF-8" );
				} catch ( java.io.UnsupportedEncodingException e ) {
					this.tableOfLetters[ inTable ] = "" ; // 🤔
				}
                                inTable ++ ;
                        }
		}
	}

	public String toString ()
	{
		if ( tableOfLetters == null ) return "null" ;

		StringBuilder out = new StringBuilder( );
		String newline = System.getProperty( "line.separator" );

		for ( int i = 0 ; i < this.tableOfLetters.length ; ++ i )
		{
			String letter = this.tableOfLetters[ i ];
			try {
				byte [] bytesUtf16 = letter.getBytes( "UTF-16BE" );
				out.append( "\"" );
				if ( bytesUtf16.length > 2 && bytesUtf16[ 2 ] == 0 ) {
					int utf16 = ( ( bytesUtf16[ 0 ] & 0xff ) << 8 ) | ( bytesUtf16[ 1 ] & 0xff );
					out.append( "\\u" + String.format( "%04x", utf16 ) );
				} else if ( bytesUtf16.length == 0 ) {
					out.append( "\\u0" );
				} else	out.append( "🙄" );
				out.append( "\"" );
			} catch ( java.io.UnsupportedEncodingException e ) {  out.append( "😲" );  }

			if ( letter.length() == 0 ) out.append( "   " );
			out.append( " /* " );
			out.append( "\"" );
			out.append( letter );
			out.append( "\"" );
			out.append( " */" );
			if ( letter.length() == 0 ) out.append( " " );

			out.append( " // utf8 { " );
			if ( letter.length() > 0 ) {
				try {
					byte [] bytesUtf8 = TableOfLetters.letterToUtf8( letter );
					for ( int b = 0 ; b < bytesUtf8.length ; b ++ ) {
						out.append( "0x" + String.format( "%02x", bytesUtf8[ b ] ) );
						if ( b + 1 < bytesUtf8.length ) out.append( ", " );
					}
				} catch ( java.io.UnsupportedEncodingException e ) {  out.append( "😑" );  }
			} else	out.append( "0x0" );
			out.append( " }" );
			out.append( newline );
		}

		return out.toString() ;
	}

	public static byte [] letterToUtf8 ( String letter ) throws java.io.UnsupportedEncodingException
	{
		byte [] bytesUtf8 = letter.getBytes( "UTF-8" );
		int lastNonZero = bytesUtf8.length ;
		while ( bytesUtf8[ -- lastNonZero ] == 0 ) { }
		int nonzeroLength = lastNonZero + 1 ;

		byte [] toReturn = new byte [ nonzeroLength ];
		System.arraycopy( bytesUtf8, 0, toReturn, 0, nonzeroLength );

		return toReturn ;
	}

}
