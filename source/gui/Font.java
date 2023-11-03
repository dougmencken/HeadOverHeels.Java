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
	/**
	 * The mapping between the letter and the textual bitmap of this letter
	 */
	private static java.util.Map < String /* letter */, String [] /* bitmap */ > letterToImage ;

	private String fontName ;
	private String fontColor ;

	private int spacingX ;
	private int spacingY ;

	public int getSpacingX () {  return this.spacingX ;  }
	public int getSpacingY () {  return this.spacingY ;  }

	public void setSpacingX ( int newHorizontalSpacing ) {  this.spacingX = newHorizontalSpacing ;  }
	public void setSpacingY ( int newVerticalSpacing ) {  this.spacingY = newVerticalSpacing ;  }

	public static final int Default_Spacing_H = 3 ;
	public static final int Default_Spacing_V = 5 ;

	private String wildLetter = "?" ;

	public void setWildLetter( String newWild ) {  this.wildLetter = newWild ;  }

	public Font( String name, String color )
	{
		this( name, color, false, Default_Spacing_H, Default_Spacing_V );
	}

	public Font( String name, String color, boolean doubleHeight )
	{
		this( name, color, doubleHeight, Default_Spacing_H, Default_Spacing_V );
	}

	public Font( String name, String color, int spaceTwitter /* spaceX */, int spaceY )
	{
		this( name, color, false, spaceTwitter, spaceY );
	}

	/**
	 * @param name the name of this font
	 * @param color the color of letters
	 * @param doubleHeight true for the double height stretching of letters
	 * @param spaceTwitter or spaceX, the horizontal spacing between letters
	 * @param spaceY the vertical spacing
	 */
	public Font( String name, String color, boolean doubleHeight, int spaceTwitter /* spaceX */, int spaceY )
	{
		this.fontName = name ;
		this.fontColor = color ;

		this.spacingY = spaceY ;
		this.spacingX = spaceTwitter ;

		// the mapping is filled once for all the instances of the font
		if ( Font.letterToImage != null ) return ;

		BufferedImage imageOFont = null ;
		{
			// read the image of font
			String nameOfFontFile = "gamedata" + java.io.File.separator + "font.png" ;
			BufferedImage fontFromPNG = Pictures.readFromPNG( nameOfFontFile );
			if ( fontFromPNG == null ) {
				System.err.println( "oops, can’t get the image of letters from file \"" + nameOfFontFile + "\"" );
				return ;
			}

			// recolor the font image in "black on transparent"

			imageOFont = Pictures.cloneAsARGBWithReplacingColor( fontFromPNG,
								java.awt.Color.magenta, new java.awt.Color( 255, 0, 255, /* alpha */ 0 ) );
								// maybe it's some old version of the font's image with the magenta background
			// white to black
			Pictures.colorizeWhite( imageOFont, java.awt.Color.black );
		}

		imageOFont = Pictures.cloneAsIndexedColor( imageOFont );

		java.io.File newFontPNG = new java.io.File( head.over.heels.FilesystemPaths.getGameStorageInHome (), "font.new.png" );
		Pictures.saveAsPNG( imageOFont, newFontPNG.getAbsolutePath () );

		final int fontImageWidth = imageOFont.getWidth() ;
		final int fontImageHeight = imageOFont.getHeight() ;

		// the size of the font image is 272 x 609 pixels,
		// or 16 x 21 letters 17 x 29 pixels each, it's "monospaced" yeah ;)

		// metrics are
		//      17 x 29 = 14 x 25 the letter itself + the space between letters 3 x 29,
		//      height 29 = 1 above + 4 + 16 + 5 + 3 below
		//           baseline is at +8 (+5) lines
		//           (cap) height is 16
		//           ascent is at baseline + cap height + 1, three lines

		final int singleCharWidth = 17 ; // 14 + spacing 3
		final int singleCharHeight = 29 ; // space 1 above + ( 3 + 1 ) ascent + 16 + 5 descent + spacing 3 below

		// decompose the font into the letters

		final int lettersPerLine = 16 ;
		final int linesInFont = 21 ;

		if ( fontImageHeight != singleCharHeight * linesInFont )
			System.out.println( "the height " + fontImageHeight + " of the font image"
						+ " isn't equal to " + ( singleCharHeight * linesInFont )
							+ " = the height of a single letter " + singleCharHeight
								+ " x " + linesInFont + " lines" );

		if ( fontImageWidth != singleCharWidth * lettersPerLine )
			System.out.println( "the width " + fontImageWidth + " of the font image"
						+ " isn't equal to " + ( singleCharWidth * lettersPerLine )
							+ " = the width of a single letter " + singleCharWidth
								+ " x " + lettersPerLine + " letters in a line" );

		BufferedImage [] letters = new BufferedImage[ linesInFont * lettersPerLine ];

		final int charStepX = fontImageWidth / lettersPerLine ;
		final int charStepY = fontImageHeight / linesInFont ;

		final int spacing = 3 ;
		final int lineWidth = charStepX - spacing ;
		final int yShift = 1 ;
		final int netHeight = charStepY - spacing - yShift ;

		int i = 0 ;
		for ( int y = 0 ; y < fontImageHeight ; y += charStepY )
			for ( int x = 0 ; x < fontImageWidth; x += charStepX )
				letters[ i ++ ] = Pictures.cloneAsIndexedColor (
							Pictures.cloneSubpictureAsARGB (
								Pictures.cloneSubpictureAsARGB ( imageOFont, x, y, charStepX, charStepY ),
									0, yShift, lineWidth, netHeight ) );

		// generate the textual bitmaps
		java.util.Vector < String [] > lettersInStrings = new java.util.Vector < String [] > ( letters.length ) ;

		for ( int ii = 0 ; ii < letters.length ; ii ++ )
		{
			String [] letterIn = new String [ netHeight ];
			int atLine = 0 ;

			BufferedImage imageOfLetter = letters[ ii ];
			if ( imageOfLetter.getType() == BufferedImage.TYPE_BYTE_BINARY
					&& imageOfLetter.getColorModel() instanceof java.awt.image.IndexColorModel
						&& imageOfLetter.getColorModel().getPixelSize() == 1 )
			{
				/////Pictures.listColorModelIfIndexed( letter );

				byte [] bitmap = ( (java.awt.image.DataBufferByte) imageOfLetter.getRaster().getDataBuffer() ).getData() ;
				StringBuilder line = new StringBuilder( );
				for ( int b = 0 ; b < bitmap.length ; ++ b ) {
					String binary = String.format( "%8s", Integer.toBinaryString( bitmap[ b ] & 0xff ) ).replace( ' ', '0' );

					int transPixel = ( (java.awt.image.IndexColorModel) imageOfLetter.getColorModel() ).getTransparentPixel ();
					if ( transPixel == 0 )
						binary = binary.replace( '0', ' ' );
					else if ( transPixel == 1 )
						binary = binary.replace( '1', ' ' );

					line.append( binary );
					/////System.out.print( String.format( "(%02x)", bitmap[ b ] ) );

					if ( ( b + 1 ) % ( 1 + ( ( lineWidth - 1 ) >> 3 ) ) == 0 ) {
						line.setLength( lineWidth );
						letterIn[ atLine ++ ] = line.toString() ;
						line.setLength( 0 );
						/////System.out.println() ;
					}
				}

				lettersInStrings.add ( letterIn );
			}
		}

		if ( letters.length != lettersInStrings.size () )
			throw new RuntimeException( "letters.length != lettersInStrings.size ()" );

		// read the list of letters
		LettersFile listOfLetters = new LettersFile( "gamedata" + java.io.File.separator + "letters.utf8" );

		// at last make and fill the mapping
		Font.letterToImage = new java.util.TreeMap < String, String [] > () ;
		int howManyLetters = lettersInStrings.size ();
		for ( int index = 0 ; index < howManyLetters ; ++ index )
			Font.letterToImage.put( listOfLetters.letterAt( index ).trim (), // without trim() 'a\0' will not be equal to 'a\0\0\0\0\0'
						lettersInStrings.elementAt( index ) );

		for ( String letter : Font.letterToImage.keySet() )
			System.out.print( letter );
		System.out.println( );
		System.out.println( listOfLetters );


	java.io.File storageInHome = head.over.heels.FilesystemPaths.getGameStorageInHome ();

	System.out.println ();
	for ( int jj = 0 ; jj < lettersInStrings.size () ; jj ++ ) {
		String [] lines = lettersInStrings.elementAt( jj );
		BufferedImage ofLetter = bitmapInStringsToImage( lines, 1, 2, Font.Default_Spacing_H, Font.Default_Spacing_V );
		java.io.File letterPNG = new java.io.File( storageInHome, "letter" + jj + ".png" );
		Pictures.saveAsPNG( ofLetter, letterPNG.getAbsolutePath () );
		for ( int ll = 0 ; ll < lines.length ; ++ ll )
			System.out.println( "\"" + lines[ ll ] + "\"" );
		System.out.println ();
	}

	listOfLetters.writeTo( new java.io.File( storageInHome, "letters.new.utf8" ) );


/////		// stretch for the double height
/////		if ( doubleHeight )
/////			System.out.println( "move it somewhere" );
	}

	public BufferedImage getImageOfString( String text )
	{
		char [] letters = text.toCharArray() ;
		int howManyLetters = letters.length ;
		while ( howManyLetters > 0 && letters[ howManyLetters - 1 ] == 0 ) {  -- howManyLetters ;  }

		int width = 0 ;
		int height = 0 ;
		BufferedImage [] images = new BufferedImage [ howManyLetters ];
		for ( int i = 0 ; i < howManyLetters ; ++ i ) {
			BufferedImage image = getImageOfLetter( String.valueOf( letters[ i ] ) );
			images[ i ] = image ;

			width += image.getWidth ();
			if ( height < image.getHeight () ) height += image.getHeight ();
		}

		BufferedImage imageOfString = new BufferedImage( width, height, BufferedImage.TYPE_INT_ARGB );

		java.awt.Graphics2D g = imageOfString.createGraphics ();
		int atX = 0 ;
		for ( int i = 0 ; i < howManyLetters ; ++ i ) {
			g.drawImage( images[ i ], atX, 0, null );
			atX += images[ i ].getWidth ();
		}
		g.dispose ();

		return imageOfString ;
	}

	public BufferedImage getImageOfLetter( String letter )
	{
		BufferedImage image = Font.imageOf( letter, this.spacingX, this.spacingY );
		return ( image != null ) ? image : Font.imageOf( this.wildLetter, this.spacingX, this.spacingY );
	}

	public static BufferedImage imageOf( String letter, int hSpace, int vSpace )
	{
		if ( Font.letterToImage == null ) return null ;
		if ( ! Font.letterToImage.containsKey( letter ) ) return null ;

		return bitmapInStringsToImage( Font.letterToImage.get( letter ), hSpace, vSpace );
	}

	public static BufferedImage bitmapInStringsToImage( String [] lines )
	{
		return bitmapInStringsToImage( lines, 0, 0, 0, 0 );
	}

	public static BufferedImage bitmapInStringsToImage( String [] lines, int spaceTwitter, int spaceY )
	{
		return bitmapInStringsToImage( lines, 0, 0, spaceTwitter, spaceY );
	}

	public static BufferedImage bitmapInStringsToImage( String [] lines, int xShift, int yShift, int spaceTwitter, int spaceY )
	{
		if ( lines == null ) return null ;

		int bitsHeight = lines.length ;
		int bitsWidth  = lines[ 0 ].length ();
		int  width = xShift + bitsWidth  + spaceTwitter ;
		int height = yShift + bitsHeight + spaceY ;

		int [] colors = new int [] {  0xff000000 ,    // opaque black
		                              0x00ffffff  };  // transparent white

		java.awt.image.IndexColorModel colorModel
						= new java.awt.image.IndexColorModel (
							/* bits per pixel */ 1,
							/* color map size */ 2, /* map */ colors, /* first entry in map */ 0,
							/* has alpha */ true,
							/* transIndex */ 1,
							/* transferType */ java.awt.image.DataBuffer.TYPE_BYTE );

		BufferedImage image = new BufferedImage( width, height, BufferedImage.TYPE_BYTE_BINARY, colorModel );

		// at first fill it with transparency
		for ( int y = 0 ; y < height ; y ++ )
			for ( int x = 0 ; x < width ; x ++ )
				image.setRGB( x, y, /* transparent */ colors[ 1 ] ) ;

		for ( int y = 0 ; y < bitsHeight ; y ++ )
			for ( int x = 0 ; x < bitsWidth ; x ++ )
				if ( lines[ y ].charAt( x ) != ' ' )
					image.setRGB( x + xShift, y + yShift, /* opaque */ colors[ 0 ] ) ;

		return image ;
	}

}


/**
 * The list of letters that the font draws, stored in a file
 */
class LettersFile
{

	private java.util.Vector < String > letters ;

	LettersFile( String nameOFile )
	{
		java.io.File lettersFile = new java.io.File( nameOFile );
		if ( ! lettersFile.exists() || ! lettersFile.isFile() || ! lettersFile.canRead() ) {
			System.out.println( "can't read file \"" + nameOFile + "\" with the list of letters that the font draws" );
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

		this.letters = new java.util.Vector < String > ( howManyLetters );

		for ( int inBytes = 0 ; inBytes < bytesRead ; ) {
			short b = (short) ( ( (int) bytes[ inBytes ] ) & 0xff );
			if ( b == 0 ) {
				this.letters.add( "" );
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
					this.letters.add( new String( letter, "UTF-8" ) );
				} catch ( java.io.UnsupportedEncodingException e ) {
					this.letters.add( "" ); // 🤔
				}
                        }
		}
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

		out.append( this.letters.size () );
		out.append( " letters" );
		out.append( newline );

		for ( String letter : this.letters )
		{
			short [] utf16 = LettersFile.letterToUtf16( letter );
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

}
