// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2023 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels.gui ;


/**
 * The list of letters that the font draws, stored in a file
 */
public class LettersFile
{

	private java.util.Vector < String > letters ;

	public LettersFile ()
	{
		this.letters = generateListOfLetters () ;
	}

	public LettersFile( String nameOFile )
	{
		this( new java.io.File( nameOFile ) );
	}

	/**
	 * Reads the list of letters from the file
	 */
	public LettersFile( java.io.File file )
	{
		if ( ! file.exists() || ! file.isFile() || ! file.canRead() ) {
			System.out.println( "there's no file \"" + file.getPath () + "\" with the list of letters" );
			this.letters = generateListOfLetters () ;
			return ;
		}

		int lengthOFile = (int) file.length () ;
		byte [] bytes = new byte [ lengthOFile ];
		int bytesRead = 0 ;
		try ( java.io.FileInputStream stream = new java.io.FileInputStream( file ) )
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
		System.out.println( "🧐 file \"" + file.getPath () + "\" lists " + howManyLetters + " letters" );

		this.letters = new java.util.Vector < String > ( howManyLetters );

		for ( int inBytes = 0 ; inBytes < bytesRead ; ) {
			short b = (short) ( ( (int) bytes[ inBytes ] ) & 0xff );
			if ( b == 0 ) {
				this.letters.add( "" );
				inBytes ++ ;
			} else {
				byte [] letter = new byte [ 5 ];
				int byteInLetter = 0 ;
				do {
					letter[ byteInLetter ++ ] = (byte) b ;
					++ inBytes ;
					if ( inBytes == bytesRead ) break ;
					b = (short) ( ( (int) bytes[ inBytes ] ) & 0xff );
				}
				while ( ( ( b & 0x80 ) != 0 ) && ( ( b & 0xC0 ) != 0xC0 )
						&& ( byteInLetter < 4 ) && ( inBytes < bytesRead ) ) ;

				letter[ byteInLetter ] = 0 ; // end of string

				String newLetter = "" ;
				try {
					newLetter = new String( letter, "UTF-8" );
				} catch ( java.io.UnsupportedEncodingException e ) {/* 🤔 */}

				if ( ! newLetter.isEmpty() ) {
					if ( newLetter.charAt( 0 ) == ' ' ) newLetter = " " ;
					else newLetter = newLetter.trim (); // trim to add "a" not "a\0\0\0\0"
					if ( newLetter.charAt( 0 ) == '\u0022' ) newLetter = "\u005c\u0022" ;
				}

				this.letters.add( newLetter );
                        }
		}
	}

	public int howManyLetters ()
	{
		return letters.size ();
	}

	public String letterAt( int i )
	{
		return letters.elementAt( i );
	}

	public boolean writeTo( java.io.File file )
	{
		int howManyLetters = letters.size () ;
		int howManyBytes = 0 ;

		java.util.Vector < byte [] > lettersUtf8 = new java.util.Vector < byte [] > ( howManyLetters ) ;
		for ( String letter : this.letters ) {
			byte [] utf8 = LettersFile.letterToUtf8( letter );
			lettersUtf8.add( utf8 );
			howManyBytes += utf8.length ;
		}

		byte [] bytes = new byte [ howManyBytes ];
		int inBytes = 0 ;
		for ( byte [] utf8letter : lettersUtf8 )
			for ( int j = 0 ; j < utf8letter.length ; ++ j )
				bytes[ inBytes ++ ] = utf8letter[ j ];

		try ( java.io.FileOutputStream stream = new java.io.FileOutputStream( file ) )
		{
			stream.write( bytes );
		}
		catch ( java.io.IOException e ) {  return false ;  }

		return true ;
	}

	public String toString ()
	{
		if ( this.letters == null ) return "null" ;

		StringBuilder out = new StringBuilder( );
		String newline = System.getProperty( "line.separator" );

		out.append( this.letters.size () ).append( " letters" ).append( newline );

		for ( String letter : this.letters )
		{
			out.append( "letters.add( " );
			short [] utf16 = LettersFile.letterToUtf16( letter );
			out.append( "\"" );
			if ( utf16.length > 0 && utf16[ 0 ] != 0 )
				for ( int c = 0 ; c < utf16.length ; c ++ )
					out.append( "\\u" + String.format( "%04x", utf16[ c ] ) );
			out.append( "\"" );
			out.append( " );" );

			if ( utf16.length > 0 && utf16[ 0 ] != 0 ) {
				out.append( " /* " ).append( "\"" );
				out.append( letter );
				out.append( "\"" ).append( " */" );
			}

			out.append( " // utf8 { " );
			byte [] bytesUtf8 = LettersFile.letterToUtf8( letter );
			for ( int b = 0 ; b < bytesUtf8.length ; b ++ ) {
				out.append( "0x" + String.format( "%02x", bytesUtf8[ b ] ) );
				if ( b + 1 < bytesUtf8.length ) out.append( ", " );
			}
			out.append( " }" );
			out.append( newline );
		}

		return out.toString() ;
	}

	public static byte [] letterToUtf8 ( String letter )
	{
		byte [] bytesUtf8 = new byte [] { 0 };

		// for an empty string, the result is an array with the single zero byte
		if ( letter.length () == 0 ) return bytesUtf8 ;

		try {
			bytesUtf8 = letter.getBytes( "UTF-8" );
		} catch ( java.io.UnsupportedEncodingException e ) {} // 😲

		int lastNonZero = bytesUtf8.length ;
		while ( lastNonZero > 0 && bytesUtf8[ -- lastNonZero ] == 0 ) { }

		int bytesToCopy = ( lastNonZero >= 0 ) ? 1 + lastNonZero : 1 ;
		byte [] toReturn = new byte [ bytesToCopy ];
		toReturn[ 0 ] = 0 ;

		if ( bytesUtf8.length >= bytesToCopy )
			System.arraycopy( bytesUtf8, 0, toReturn, 0, bytesToCopy );

		return toReturn ;
	}

	public static short [] letterToUtf16 ( String letter )
	{
		// for an empty string, the result is an array with the single 16-bit zero
		if ( letter.length () == 0 ) return new short [] { 0 };

		byte [] bytesUtf16 = new byte [] { 0, 0 };
		try {
			bytesUtf16 = letter.getBytes( "UTF-16BE" );
		} catch ( java.io.UnsupportedEncodingException e ) {/* 😑 */}

		int lastNonZero = bytesUtf16.length ;
		while ( lastNonZero > 0 && bytesUtf16[ -- lastNonZero ] == 0 ) {}

		int nonzeroLength16 = ( lastNonZero >> 1 ) + 1 ;
		short [] toReturn = new short [ nonzeroLength16 ];
		int index16 = 0 ;

		for ( int i8 = 0 ; i8 < lastNonZero ; i8 += 2 ) {
			int utf16 = ( ( bytesUtf16[ i8 ] & 0xff ) << 8 ) | ( bytesUtf16[ i8 + 1 ] & 0xff );
			toReturn[ index16 ++ ] = (short) utf16 ;
		}

		return toReturn ; // 🙄
	}

	public static java.util.Vector < String > generateListOfLetters ()
	{
		java.util.Vector < String > letters = new java.util.Vector < String > ( 336 );

		// quotation mark " 0x22
		// letters.add( "\u005c\u0022" ); /* "\"" */ // utf8 { 0x22 }

		letters.add( "\u0020" ); /* " " */ // utf8 { 0x20 }
		letters.add( "\u0021" ); /* "!" */ // utf8 { 0x21 }
		letters.add( "\u005c\u0022" ); /* "\"" */ // utf8 { 0x22 }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "\u0024" ); /* "$" */ // utf8 { 0x24 }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "\u0026" ); /* "&" */ // utf8 { 0x26 }
		letters.add( "\u0027" ); /* "'" */ // utf8 { 0x27 }
		letters.add( "\u0028" ); /* "(" */ // utf8 { 0x28 }
		letters.add( "\u0029" ); /* ")" */ // utf8 { 0x29 }
		letters.add( "\u002a" ); /* "*" */ // utf8 { 0x2a }
		letters.add( "\u002b" ); /* "+" */ // utf8 { 0x2b }
		letters.add( "\u002c" ); /* "," */ // utf8 { 0x2c }
		letters.add( "\u002d" ); /* "-" */ // utf8 { 0x2d }
		letters.add( "\u002e" ); /* "." */ // utf8 { 0x2e }
		letters.add( "\u002f" ); /* "/" */ // utf8 { 0x2f }
		letters.add( "\u0030" ); /* "0" */ // utf8 { 0x30 }
		letters.add( "\u0031" ); /* "1" */ // utf8 { 0x31 }
		letters.add( "\u0032" ); /* "2" */ // utf8 { 0x32 }
		letters.add( "\u0033" ); /* "3" */ // utf8 { 0x33 }
		letters.add( "\u0034" ); /* "4" */ // utf8 { 0x34 }
		letters.add( "\u0035" ); /* "5" */ // utf8 { 0x35 }
		letters.add( "\u0036" ); /* "6" */ // utf8 { 0x36 }
		letters.add( "\u0037" ); /* "7" */ // utf8 { 0x37 }
		letters.add( "\u0038" ); /* "8" */ // utf8 { 0x38 }
		letters.add( "\u0039" ); /* "9" */ // utf8 { 0x39 }
		letters.add( "\u003a" ); /* ":" */ // utf8 { 0x3a }
		letters.add( "\u003b" ); /* ";" */ // utf8 { 0x3b }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "\u003f" ); /* "?" */ // utf8 { 0x3f }

		letters.add( "\u0040" ); /* "@" */ // utf8 { 0x40 }
		letters.add( "\u0041" ); /* "A" */ // utf8 { 0x41 }
		letters.add( "\u0042" ); /* "B" */ // utf8 { 0x42 }
		letters.add( "\u0043" ); /* "C" */ // utf8 { 0x43 }
		letters.add( "\u0044" ); /* "D" */ // utf8 { 0x44 }
		letters.add( "\u0045" ); /* "E" */ // utf8 { 0x45 }
		letters.add( "\u0046" ); /* "F" */ // utf8 { 0x46 }
		letters.add( "\u0047" ); /* "G" */ // utf8 { 0x47 }
		letters.add( "\u0048" ); /* "H" */ // utf8 { 0x48 }
		letters.add( "\u0049" ); /* "I" */ // utf8 { 0x49 }
		letters.add( "\u004a" ); /* "J" */ // utf8 { 0x4a }
		letters.add( "\u004b" ); /* "K" */ // utf8 { 0x4b }
		letters.add( "\u004c" ); /* "L" */ // utf8 { 0x4c }
		letters.add( "\u004d" ); /* "M" */ // utf8 { 0x4d }
		letters.add( "\u004e" ); /* "N" */ // utf8 { 0x4e }
		letters.add( "\u004f" ); /* "O" */ // utf8 { 0x4f }
		letters.add( "\u0050" ); /* "P" */ // utf8 { 0x50 }
		letters.add( "\u0051" ); /* "Q" */ // utf8 { 0x51 }
		letters.add( "\u0052" ); /* "R" */ // utf8 { 0x52 }
		letters.add( "\u0053" ); /* "S" */ // utf8 { 0x53 }
		letters.add( "\u0054" ); /* "T" */ // utf8 { 0x54 }
		letters.add( "\u0055" ); /* "U" */ // utf8 { 0x55 }
		letters.add( "\u0056" ); /* "V" */ // utf8 { 0x56 }
		letters.add( "\u0057" ); /* "W" */ // utf8 { 0x57 }
		letters.add( "\u0058" ); /* "X" */ // utf8 { 0x58 }
		letters.add( "\u0059" ); /* "Y" */ // utf8 { 0x59 }
		letters.add( "\u005a" ); /* "Z" */ // utf8 { 0x5a }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "\u005f" ); /* "_" */ // utf8 { 0x5f }

		letters.add( "\u0060" ); /* "`" */ // utf8 { 0x60 }
		letters.add( "\u0061" ); /* "a" */ // utf8 { 0x61 }
		letters.add( "\u0062" ); /* "b" */ // utf8 { 0x62 }
		letters.add( "\u0063" ); /* "c" */ // utf8 { 0x63 }
		letters.add( "\u0064" ); /* "d" */ // utf8 { 0x64 }
		letters.add( "\u0065" ); /* "e" */ // utf8 { 0x65 }
		letters.add( "\u0066" ); /* "f" */ // utf8 { 0x66 }
		letters.add( "\u0067" ); /* "g" */ // utf8 { 0x67 }
		letters.add( "\u0068" ); /* "h" */ // utf8 { 0x68 }
		letters.add( "\u0069" ); /* "i" */ // utf8 { 0x69 }
		letters.add( "\u006a" ); /* "j" */ // utf8 { 0x6a }
		letters.add( "\u006b" ); /* "k" */ // utf8 { 0x6b }
		letters.add( "\u006c" ); /* "l" */ // utf8 { 0x6c }
		letters.add( "\u006d" ); /* "m" */ // utf8 { 0x6d }
		letters.add( "\u006e" ); /* "n" */ // utf8 { 0x6e }
		letters.add( "\u006f" ); /* "o" */ // utf8 { 0x6f }
		letters.add( "\u0070" ); /* "p" */ // utf8 { 0x70 }
		letters.add( "\u0071" ); /* "q" */ // utf8 { 0x71 }
		letters.add( "\u0072" ); /* "r" */ // utf8 { 0x72 }
		letters.add( "\u0073" ); /* "s" */ // utf8 { 0x73 }
		letters.add( "\u0074" ); /* "t" */ // utf8 { 0x74 }
		letters.add( "\u0075" ); /* "u" */ // utf8 { 0x75 }
		letters.add( "\u0076" ); /* "v" */ // utf8 { 0x76 }
		letters.add( "\u0077" ); /* "w" */ // utf8 { 0x77 }
		letters.add( "\u0078" ); /* "x" */ // utf8 { 0x78 }
		letters.add( "\u0079" ); /* "y" */ // utf8 { 0x79 }
		letters.add( "\u007a" ); /* "z" */ // utf8 { 0x7a }
		letters.add( "\u007b" ); /* "{" */ // utf8 { 0x7b }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "\u007d" ); /* "}" */ // utf8 { 0x7d }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "" ); // utf8 { 0x00 }

		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "\u0160" ); /* "Š" */ // utf8 { 0xc5, 0xa0 }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "\u0152" ); /* "Œ" */ // utf8 { 0xc5, 0x92 }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "" ); // utf8 { 0x00 }

		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "\u00b7" ); /* "·" */ // utf8 { 0xc2, 0xb7 }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "\u0161" ); /* "š" */ // utf8 { 0xc5, 0xa1 }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "\u0153" ); /* "œ" */ // utf8 { 0xc5, 0x93 }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "\u0178" ); /* "Ÿ" */ // utf8 { 0xc5, 0xb8 }

		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "\u00a1" ); /* "¡" */ // utf8 { 0xc2, 0xa1 }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "\u2018" ); /* "‘" */ // utf8 { 0xe2, 0x80, 0x98 }
		letters.add( "\u2019" ); /* "’" */ // utf8 { 0xe2, 0x80, 0x99 }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "\u201c" ); /* "“" */ // utf8 { 0xe2, 0x80, 0x9c }
		letters.add( "\u201d" ); /* "”" */ // utf8 { 0xe2, 0x80, 0x9d }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "" ); // utf8 { 0x00 }

		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "\u00bf" ); /* "¿" */ // utf8 { 0xc2, 0xbf }

		letters.add( "\u00c0" ); /* "À" */ // utf8 { 0xc3, 0x80 }
		letters.add( "\u00c1" ); /* "Á" */ // utf8 { 0xc3, 0x81 }
		letters.add( "\u00c3" ); /* "Ã" */ // utf8 { 0xc3, 0x83 }
		letters.add( "\u00c4" ); /* "Ä" */ // utf8 { 0xc3, 0x84 }
		letters.add( "\u00c5" ); /* "Å" */ // utf8 { 0xc3, 0x85 }
		letters.add( "\u00c6" ); /* "Æ" */ // utf8 { 0xc3, 0x86 }
		letters.add( "\u00c7" ); /* "Ç" */ // utf8 { 0xc3, 0x87 }
		letters.add( "\u00c8" ); /* "È" */ // utf8 { 0xc3, 0x88 }
		letters.add( "\u00c9" ); /* "É" */ // utf8 { 0xc3, 0x89 }
		letters.add( "\u00ca" ); /* "Ê" */ // utf8 { 0xc3, 0x8a }
		letters.add( "\u00cb" ); /* "Ë" */ // utf8 { 0xc3, 0x8b }
		letters.add( "\u00cc" ); /* "Ì" */ // utf8 { 0xc3, 0x8c }
		letters.add( "\u00cd" ); /* "Í" */ // utf8 { 0xc3, 0x8d }
		letters.add( "\u00ce" ); /* "Î" */ // utf8 { 0xc3, 0x8e }
		letters.add( "\u00cf" ); /* "Ï" */ // utf8 { 0xc3, 0x8f }
		letters.add( "\u00d0" ); /* "Ð" */ // utf8 { 0xc3, 0x90 }
		letters.add( "\u00d1" ); /* "Ñ" */ // utf8 { 0xc3, 0x91 }
		letters.add( "\u00d2" ); /* "Ò" */ // utf8 { 0xc3, 0x92 }
		letters.add( "\u00d3" ); /* "Ó" */ // utf8 { 0xc3, 0x93 }
		letters.add( "\u00d4" ); /* "Ô" */ // utf8 { 0xc3, 0x94 }
		letters.add( "\u00d5" ); /* "Õ" */ // utf8 { 0xc3, 0x95 }
		letters.add( "\u00d6" ); /* "Ö" */ // utf8 { 0xc3, 0x96 }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "\u00d8" ); /* "Ø" */ // utf8 { 0xc3, 0x98 }
		letters.add( "\u00d9" ); /* "Ù" */ // utf8 { 0xc3, 0x99 }
		letters.add( "\u00da" ); /* "Ú" */ // utf8 { 0xc3, 0x9a }
		letters.add( "\u00db" ); /* "Û" */ // utf8 { 0xc3, 0x9b }
		letters.add( "\u00dc" ); /* "Ü" */ // utf8 { 0xc3, 0x9c }
		letters.add( "\u00dd" ); /* "Ý" */ // utf8 { 0xc3, 0x9d }
		letters.add( "\u00de" ); /* "Þ" */ // utf8 { 0xc3, 0x9e }
		letters.add( "\u00df" ); /* "ß" */ // utf8 { 0xc3, 0x9f }
		letters.add( "\u00e0" ); /* "à" */ // utf8 { 0xc3, 0xa0 }
		letters.add( "\u00e1" ); /* "á" */ // utf8 { 0xc3, 0xa1 }
		letters.add( "\u00e2" ); /* "â" */ // utf8 { 0xc3, 0xa2 }
		letters.add( "\u00e3" ); /* "ã" */ // utf8 { 0xc3, 0xa3 }
		letters.add( "\u00e4" ); /* "ä" */ // utf8 { 0xc3, 0xa4 }
		letters.add( "\u00e5" ); /* "å" */ // utf8 { 0xc3, 0xa5 }
		letters.add( "\u00e6" ); /* "æ" */ // utf8 { 0xc3, 0xa6 }
		letters.add( "\u00e7" ); /* "ç" */ // utf8 { 0xc3, 0xa7 }
		letters.add( "\u00e8" ); /* "è" */ // utf8 { 0xc3, 0xa8 }
		letters.add( "\u00e9" ); /* "é" */ // utf8 { 0xc3, 0xa9 }
		letters.add( "\u00ea" ); /* "ê" */ // utf8 { 0xc3, 0xaa }
		letters.add( "\u00eb" ); /* "ë" */ // utf8 { 0xc3, 0xab }
		letters.add( "\u00ec" ); /* "ì" */ // utf8 { 0xc3, 0xac }
		letters.add( "\u00ed" ); /* "í" */ // utf8 { 0xc3, 0xad }
		letters.add( "\u00ee" ); /* "î" */ // utf8 { 0xc3, 0xae }
		letters.add( "\u00ef" ); /* "ï" */ // utf8 { 0xc3, 0xaf }
		letters.add( "\u00f0" ); /* "ð" */ // utf8 { 0xc3, 0xb0 }
		letters.add( "\u00f1" ); /* "ñ" */ // utf8 { 0xc3, 0xb1 }
		letters.add( "\u00f2" ); /* "ò" */ // utf8 { 0xc3, 0xb2 }
		letters.add( "\u00f3" ); /* "ó" */ // utf8 { 0xc3, 0xb3 }
		letters.add( "\u00f4" ); /* "ô" */ // utf8 { 0xc3, 0xb4 }
		letters.add( "\u00f5" ); /* "õ" */ // utf8 { 0xc3, 0xb5 }
		letters.add( "\u00f6" ); /* "ö" */ // utf8 { 0xc3, 0xb6 }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "\u00f8" ); /* "ø" */ // utf8 { 0xc3, 0xb8 }
		letters.add( "\u00f9" ); /* "ù" */ // utf8 { 0xc3, 0xb9 }
		letters.add( "\u00fa" ); /* "ú" */ // utf8 { 0xc3, 0xba }
		letters.add( "\u00fb" ); /* "û" */ // utf8 { 0xc3, 0xbb }
		letters.add( "\u00fc" ); /* "ü" */ // utf8 { 0xc3, 0xbc }
		letters.add( "\u00fd" ); /* "ý" */ // utf8 { 0xc3, 0xbd }
		letters.add( "\u00fe" ); /* "þ" */ // utf8 { 0xc3, 0xbe }
		letters.add( "\u00ff" ); /* "ÿ" */ // utf8 { 0xc3, 0xbf }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "\u0104" ); /* "Ą" */ // utf8 { 0xc4, 0x84 }
		letters.add( "\u0105" ); /* "ą" */ // utf8 { 0xc4, 0x85 }
		letters.add( "\u0106" ); /* "Ć" */ // utf8 { 0xc4, 0x86 }
		letters.add( "\u0107" ); /* "ć" */ // utf8 { 0xc4, 0x87 }
		letters.add( "\u0118" ); /* "Ę" */ // utf8 { 0xc4, 0x98 }
		letters.add( "\u0119" ); /* "ę" */ // utf8 { 0xc4, 0x99 }
		letters.add( "\u0141" ); /* "Ł" */ // utf8 { 0xc5, 0x81 }
		letters.add( "\u0142" ); /* "ł" */ // utf8 { 0xc5, 0x82 }
		letters.add( "\u0143" ); /* "Ń" */ // utf8 { 0xc5, 0x83 }
		letters.add( "\u0144" ); /* "ń" */ // utf8 { 0xc5, 0x84 }
		letters.add( "\u015a" ); /* "Ś" */ // utf8 { 0xc5, 0x9a }
		letters.add( "\u015b" ); /* "ś" */ // utf8 { 0xc5, 0x9b }
		letters.add( "\u017b" ); /* "Ż" */ // utf8 { 0xc5, 0xbb }
		letters.add( "\u017c" ); /* "ż" */ // utf8 { 0xc5, 0xbc }
		letters.add( "\u0179" ); /* "Ź" */ // utf8 { 0xc5, 0xb9 }
		letters.add( "\u017a" ); /* "ź" */ // utf8 { 0xc5, 0xba }
		letters.add( "\u017d" ); /* "Ž" */ // utf8 { 0xc5, 0xbd }
		letters.add( "\u017e" ); /* "ž" */ // utf8 { 0xc5, 0xbe }
		letters.add( "\u010c" ); /* "Č" */ // utf8 { 0xc4, 0x8c }
		letters.add( "\u010d" ); /* "č" */ // utf8 { 0xc4, 0x8d }
		letters.add( "\u010e" ); /* "Ď" */ // utf8 { 0xc4, 0x8e }
		letters.add( "\u010f" ); /* "ď" */ // utf8 { 0xc4, 0x8f }
		letters.add( "\u013d" ); /* "Ľ" */ // utf8 { 0xc4, 0xbd }
		letters.add( "\u013e" ); /* "ľ" */ // utf8 { 0xc4, 0xbe }
		letters.add( "\u0164" ); /* "Ť" */ // utf8 { 0xc5, 0xa4 }
		letters.add( "\u0165" ); /* "ť" */ // utf8 { 0xc5, 0xa5 }
		letters.add( "\u0110" ); /* "Đ" */ // utf8 { 0xc4, 0x90 }
		letters.add( "\u0111" ); /* "đ" */ // utf8 { 0xc4, 0x91 }
		letters.add( "\u0147" ); /* "Ň" */ // utf8 { 0xc5, 0x87 }
		letters.add( "\u0148" ); /* "ň" */ // utf8 { 0xc5, 0x88 }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "" ); // utf8 { 0x00 }

		letters.add( "\u0410" ); /* "А" */ // utf8 { 0xd0, 0x90 }
		letters.add( "\u0411" ); /* "Б" */ // utf8 { 0xd0, 0x91 }
		letters.add( "\u0412" ); /* "В" */ // utf8 { 0xd0, 0x92 }
		letters.add( "\u0413" ); /* "Г" */ // utf8 { 0xd0, 0x93 }
		letters.add( "\u0414" ); /* "Д" */ // utf8 { 0xd0, 0x94 }
		letters.add( "\u0415" ); /* "Е" */ // utf8 { 0xd0, 0x95 }
		letters.add( "\u0416" ); /* "Ж" */ // utf8 { 0xd0, 0x96 }
		letters.add( "\u0417" ); /* "З" */ // utf8 { 0xd0, 0x97 }
		letters.add( "\u0418" ); /* "И" */ // utf8 { 0xd0, 0x98 }
		letters.add( "\u0419" ); /* "Й" */ // utf8 { 0xd0, 0x99 }
		letters.add( "\u041a" ); /* "К" */ // utf8 { 0xd0, 0x9a }
		letters.add( "\u041b" ); /* "Л" */ // utf8 { 0xd0, 0x9b }
		letters.add( "\u041c" ); /* "М" */ // utf8 { 0xd0, 0x9c }
		letters.add( "\u041d" ); /* "Н" */ // utf8 { 0xd0, 0x9d }
		letters.add( "\u041e" ); /* "О" */ // utf8 { 0xd0, 0x9e }
		letters.add( "\u041f" ); /* "П" */ // utf8 { 0xd0, 0x9f }
		letters.add( "\u0420" ); /* "Р" */ // utf8 { 0xd0, 0xa0 }
		letters.add( "\u0421" ); /* "С" */ // utf8 { 0xd0, 0xa1 }
		letters.add( "\u0422" ); /* "Т" */ // utf8 { 0xd0, 0xa2 }
		letters.add( "\u0423" ); /* "У" */ // utf8 { 0xd0, 0xa3 }
		letters.add( "\u0424" ); /* "Ф" */ // utf8 { 0xd0, 0xa4 }
		letters.add( "\u0425" ); /* "Х" */ // utf8 { 0xd0, 0xa5 }
		letters.add( "\u0426" ); /* "Ц" */ // utf8 { 0xd0, 0xa6 }
		letters.add( "\u0427" ); /* "Ч" */ // utf8 { 0xd0, 0xa7 }
		letters.add( "\u0428" ); /* "Ш" */ // utf8 { 0xd0, 0xa8 }
		letters.add( "\u0429" ); /* "Щ" */ // utf8 { 0xd0, 0xa9 }
		letters.add( "\u042a" ); /* "Ъ" */ // utf8 { 0xd0, 0xaa }
		letters.add( "\u042b" ); /* "Ы" */ // utf8 { 0xd0, 0xab }
		letters.add( "\u042c" ); /* "Ь" */ // utf8 { 0xd0, 0xac }
		letters.add( "\u042d" ); /* "Э" */ // utf8 { 0xd0, 0xad }
		letters.add( "\u042e" ); /* "Ю" */ // utf8 { 0xd0, 0xae }
		letters.add( "\u042f" ); /* "Я" */ // utf8 { 0xd0, 0xaf }
		letters.add( "\u0430" ); /* "а" */ // utf8 { 0xd0, 0xb0 }
		letters.add( "\u0431" ); /* "б" */ // utf8 { 0xd0, 0xb1 }
		letters.add( "\u0432" ); /* "в" */ // utf8 { 0xd0, 0xb2 }
		letters.add( "\u0433" ); /* "г" */ // utf8 { 0xd0, 0xb3 }
		letters.add( "\u0434" ); /* "д" */ // utf8 { 0xd0, 0xb4 }
		letters.add( "\u0435" ); /* "е" */ // utf8 { 0xd0, 0xb5 }
		letters.add( "\u0436" ); /* "ж" */ // utf8 { 0xd0, 0xb6 }
		letters.add( "\u0437" ); /* "з" */ // utf8 { 0xd0, 0xb7 }
		letters.add( "\u0438" ); /* "и" */ // utf8 { 0xd0, 0xb8 }
		letters.add( "\u0439" ); /* "й" */ // utf8 { 0xd0, 0xb9 }
		letters.add( "\u043a" ); /* "к" */ // utf8 { 0xd0, 0xba }
		letters.add( "\u043b" ); /* "л" */ // utf8 { 0xd0, 0xbb }
		letters.add( "\u043c" ); /* "м" */ // utf8 { 0xd0, 0xbc }
		letters.add( "\u043d" ); /* "н" */ // utf8 { 0xd0, 0xbd }
		letters.add( "\u043e" ); /* "о" */ // utf8 { 0xd0, 0xbe }
		letters.add( "\u043f" ); /* "п" */ // utf8 { 0xd0, 0xbf }
		letters.add( "\u0440" ); /* "р" */ // utf8 { 0xd1, 0x80 }
		letters.add( "\u0441" ); /* "с" */ // utf8 { 0xd1, 0x81 }
		letters.add( "\u0442" ); /* "т" */ // utf8 { 0xd1, 0x82 }
		letters.add( "\u0443" ); /* "у" */ // utf8 { 0xd1, 0x83 }
		letters.add( "\u0444" ); /* "ф" */ // utf8 { 0xd1, 0x84 }
		letters.add( "\u0445" ); /* "х" */ // utf8 { 0xd1, 0x85 }
		letters.add( "\u0446" ); /* "ц" */ // utf8 { 0xd1, 0x86 }
		letters.add( "\u0447" ); /* "ч" */ // utf8 { 0xd1, 0x87 }
		letters.add( "\u0448" ); /* "ш" */ // utf8 { 0xd1, 0x88 }
		letters.add( "\u0449" ); /* "щ" */ // utf8 { 0xd1, 0x89 }
		letters.add( "\u044a" ); /* "ъ" */ // utf8 { 0xd1, 0x8a }
		letters.add( "\u044b" ); /* "ы" */ // utf8 { 0xd1, 0x8b }
		letters.add( "\u044c" ); /* "ь" */ // utf8 { 0xd1, 0x8c }
		letters.add( "\u044d" ); /* "э" */ // utf8 { 0xd1, 0x8d }
		letters.add( "\u044e" ); /* "ю" */ // utf8 { 0xd1, 0x8e }
		letters.add( "\u044f" ); /* "я" */ // utf8 { 0xd1, 0x8f }

		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "\u0402" ); /* "Ђ" */ // utf8 { 0xd0, 0x82 }
		letters.add( "\u0408" ); /* "Ј" */ // utf8 { 0xd0, 0x88 }
		letters.add( "\u0409" ); /* "Љ" */ // utf8 { 0xd0, 0x89 }
		letters.add( "\u040a" ); /* "Њ" */ // utf8 { 0xd0, 0x8a }
		letters.add( "\u040b" ); /* "Ћ" */ // utf8 { 0xd0, 0x8b }
		letters.add( "\u040f" ); /* "Џ" */ // utf8 { 0xd0, 0x8f }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "" ); // utf8 { 0x00 }
		letters.add( "\u0452" ); /* "ђ" */ // utf8 { 0xd1, 0x92 }
		letters.add( "\u0458" ); /* "ј" */ // utf8 { 0xd1, 0x98 }
		letters.add( "\u0459" ); /* "љ" */ // utf8 { 0xd1, 0x99 }
		letters.add( "\u045a" ); /* "њ" */ // utf8 { 0xd1, 0x9a }
		letters.add( "\u045b" ); /* "ћ" */ // utf8 { 0xd1, 0x9b }
		letters.add( "\u045f" ); /* "џ" */ // utf8 { 0xd1, 0x9f }

		return letters ;
	}

	/**
	 * Writes the new, as is generated by this class, list of letters
	 */
	public static void main( String [] arguments )
	{
		String nameOfNewLettersFile = "letters.new.utf8" ;
		if ( arguments != null && arguments.length > 0 ) nameOfNewLettersFile = arguments[ 0 ] ;

		LettersFile theList = new LettersFile( );
		System.out.println( theList );

		theList.writeTo( new java.io.File( head.over.heels.FilesystemPaths.getGameStorageInHome (), nameOfNewLettersFile ) );
	}

}
