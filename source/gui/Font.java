// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2023 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels.gui ;

import java.awt.image.BufferedImage ;

import head.over.heels.Colors ;
import head.over.heels.Pictures ;


/**
 * The font with letters from the picture file. Letters may have a non-white color or~and the double height
 */

public class Font
{

	public /*private*/ static BufferedImage imageOFont ;

	private static TableOfLetters lettersMapping ;

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

		// read the image of font once
		if ( Font.imageOFont == null ) {
			String nameOfFontFile = "gamedata" + java.io.File.separator + "font.png" ;
			Font.imageOFont = Pictures.readFromPNG( nameOfFontFile );
			if ( Font.imageOFont == null ) {
				System.err.println( "oops, can’t get the image of letters from file \"" + nameOfFontFile + "\"" );
				return ;
			}
		}

		// the image of font may have the magenta background
		Font.imageOFont = Pictures.cloneWithAlphaChannelAndReplacingColor( Font.imageOFont,
								java.awt.Color.magenta, new java.awt.Color( 255, 0, 255, /* alpha */ 0 ) ); ;

		// add the black tint
		BufferedImage blackLetters = Pictures.exactCopy( Font.imageOFont );
		Pictures.replaceColor( blackLetters, Colors.white, Colors.black );

		int tintOffset = 2 ;
		BufferedImage fontWithoutTint = Pictures.exactCopy( Font.imageOFont );


	java.io.File storageInHome = head.over.heels.FilesystemPaths.getGameStorageInHome ();
	java.io.File newFontImage = new java.io.File( storageInHome, "font.png" );
	Pictures.saveAsPNG( fontWithoutTint, newFontImage.getAbsolutePath () );


		{
			java.awt.Graphics2D fontGraphics = Font.imageOFont.createGraphics ();

			for ( int off = 1 ; off <= tintOffset ; off ++ )
				fontGraphics.drawImage( blackLetters, off, off, null );

			fontGraphics.drawImage( fontWithoutTint, 0, 0, null );
			fontGraphics.dispose ();
		}

	/* .................. */

		// read the list of letters once for all fonts
		Font.lettersMapping = new TableOfLetters( "gamedata" + java.io.File.separator + "letters.utf8" );
		System.out.println( lettersMapping );

		// stretch for the double height
		if ( doubleHeight )
			System.out.println( "I don't know how" );
	}

	// ?? //private Font( Font f ) {} // doesn't copy

}


/**
 * The mapping between the image of letter from the font's picture file and the UTF-8 code of this letter
 */
class TableOfLetters
{

	String [] letters ;

	TableOfLetters( String nameOFile )
	{
		java.io.File lettersFile = new java.io.File( nameOFile );
		if ( ! lettersFile.exists() || ! lettersFile.isFile() || ! lettersFile.canRead() ) {
			System.out.println( "can't read file \"" + nameOFile + "\" with the list of letters drawn in the font" );
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
		System.out.println( "🧐 file \"" + nameOFile + "\" lists " + howManyLetters + " letters" );

		this.letters = new String [ howManyLetters ];
		int inTable = 0;
		for ( int inBytes = 0 ; inBytes < bytesRead ; ) {
			short b = (short) ( ( (int) bytes[ inBytes ] ) & 0xff );
			if ( b == 0 ) {
				letters[ inTable ++ ] = "" ;
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
					this.letters[ inTable ] = new String( letter, "UTF-8" );
				} catch ( java.io.UnsupportedEncodingException e ) {
					this.letters[ inTable ] = "" ; // 🤔
				}
                                inTable ++ ;
                        }
		}
	}

	public String toString ()
	{
		if ( this.letters == null ) return "null" ;

		StringBuilder out = new StringBuilder( );
		String newline = System.getProperty( "line.separator" );

		for ( int i = 0 ; i < this.letters.length ; ++ i )
		{
			String letter = this.letters[ i ];

			short [] utf16 = TableOfLetters.letterToUtf16( letter );
			out.append( "\"" );
			for ( int c = 0 ; c < utf16.length ; c ++ ) {
				out.append( "\\u" + String.format( "%04x", utf16[ c ] ) );
			}
			out.append( "\"" );

			out.append( " /* " );
			out.append( "\"" );
			out.append( letter );
			out.append( "\"" );
			out.append( " */" );
			if ( letter.length() == 0 ) out.append( " " );

			out.append( " // utf8 { " );
			byte [] bytesUtf8 = TableOfLetters.letterToUtf8( letter );
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

}
