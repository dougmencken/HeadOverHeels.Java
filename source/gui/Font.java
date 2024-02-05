// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2024 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels.gui ;

import java.awt.Color ;
import java.awt.image.BufferedImage ;

import head.over.heels.Colours ;
import head.over.heels.Pictures ;
import head.over.heels.FilesystemPaths ;


/**
 * The font with letters from the picture file. The letters have colour and can be double height
 */

public class Font
{
	/**
	 * The mapping between the letter and the textual bitmap of this letter
	 */
	private static java.util.Map < String /* letter */, String [] /* bitmap */ > letterToImage ;

	private String fontName ;
	private Color fontColor ;

	private int spacingX ;
	private int spacingY ;

	public int getSpacingX () {  return this.spacingX ;  }
	public int getSpacingY () {  return this.spacingY ;  }

	public void setSpacingX ( int newHorizontalSpacing ) {  this.spacingX = newHorizontalSpacing ;  }
	public void setSpacingY ( int newVerticalSpacing ) {  this.spacingY = newVerticalSpacing ;  }

	public static final int Default_Spacing_H = 3 ;
	public static final int Default_Spacing_V = 5 ;

	private boolean doubleHeight = false ;

	public static final int Shadow_Shift = 2 ;

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
		this.fontColor = Colours.byName( color );

		this.doubleHeight = doubleHeight ;

		this.spacingY = spaceY ;
		this.spacingX = spaceTwitter ;

		if ( Font.letterToImage == null ) // once for all the instances of the font
		{
			BufferedImage font = Font.readImageOFont( new java.io.File( FilesystemPaths.getPathToGameData(), "font.png" ) );
			java.util.Vector < String [] > imagesOfLetters = Font.decomposeImageOFont( font );
			LettersFile listOfLetters = new LettersFile( );
					// = new LettersFile( new java.io.File( FilesystemPaths.getPathToGameData(), "letters.utf8" ) );
			Font.fillTheMapping( listOfLetters, imagesOfLetters );
		}
	}

	private static BufferedImage readImageOFont( java.io.File imageFile )
	{
		if ( imageFile == null ) return null ;

		BufferedImage fontFromFile = Pictures.readFromFile( imageFile );
		if ( fontFromFile == null ) {
			System.err.println( "oops, can’t get the image of letters from file \"" + imageFile.getPath() + "\"" );
			return null ;
		}

		return Pictures.cloneAsIndexedColor( fontFromFile );
	}

	private static java.util.Vector < String [] > decomposeImageOFont( BufferedImage imageOFont )
	{
		final int fontImageWidth = imageOFont.getWidth() ;
		final int fontImageHeight = imageOFont.getHeight() ;

		// the size of the font image is 272 x 609 pixels,
		// or 16 x 21 letters 17 x 29 pixels each, it's "monospaced" yeah ;)

		// metrics are
		//      17 x 29 = 14 x 25 the letter itself + the space between letters 3 x 29,
		//      height 29 = 1 above + 4 + 16 + 5 + 3 below
		//           baseline is at 5th (8th) line
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

					if ( ( b + 1 ) % ( 1 + ( ( lineWidth - 1 ) >> 3 ) ) == 0 ) {
						line.setLength( lineWidth );
						letterIn[ atLine ++ ] = line.toString() ;
						line.setLength( 0 );
					}
				}

				lettersInStrings.add ( letterIn );
			}
		}

		return lettersInStrings ;
	}

	private static void fillTheMapping ( LettersFile listOfLetters, java.util.Vector < String [] > imagesOfLetters )
	{
		Font.letterToImage = new java.util.TreeMap < String, String [] > () ;

		int howManyLetters = imagesOfLetters.size ();
		for ( int index = 0 ; index < howManyLetters ; ++ index ) {
			String letter = listOfLetters.letterAt( index ) ;
			if ( ! letter.isEmpty () )
				Font.letterToImage.put( letter, imagesOfLetters.elementAt( index ) );
		}
	}

	public BufferedImage composeImageOfString( String text )
	{
		return composeImageOfString( text, true, this.doubleHeight );
	}

	public BufferedImage composeImageOfString( String text, boolean withShadow )
	{
		return composeImageOfString( text, withShadow, this.doubleHeight );
	}

	/**
	 * @param text the string of text to render using the font
	 * @param withShadow true to add shadow to the letters
	 * @param with2xHeight true to stretch the letters twice in height
	 */
	private BufferedImage composeImageOfString( String text, boolean withShadow, boolean with2xHeight )
	{
		char [] letters = text.toCharArray() ;
		int howManyLetters = letters.length ;
		while ( howManyLetters > 0 && letters[ howManyLetters - 1 ] == 0 ) {  -- howManyLetters ;  }

		BufferedImage [] images = new BufferedImage [ howManyLetters ];
		BufferedImage [] shadows = new BufferedImage [ howManyLetters ];
		int width = 0 ;
		int height = 0 ;

		for ( int i = 0 ; i < howManyLetters ; ++ i ) {
			String letter = String.valueOf( letters[ i ] );

			BufferedImage image = getImageOfLetter( letter );
			if ( with2xHeight ) image = Pictures.cloneWithTwiceTheHeight( image );
			images[ i ] = image ;

			width += image.getWidth ();
			if ( height < image.getHeight () ) height = image.getHeight ();

			if ( withShadow ) {
				BufferedImage shadow = getImageOfLetterInBlack( letter );
				shadows[ i ] = with2xHeight ? Pictures.cloneWithTwiceTheHeight( shadow ) : shadow ;
			}
		}

		if ( withShadow ) {
			width += Font.Shadow_Shift ;
			height += Font.Shadow_Shift ;
		}

		BufferedImage imageOfString = new BufferedImage( width, height, BufferedImage.TYPE_INT_ARGB );

		java.awt.Graphics2D g = imageOfString.createGraphics ();
		int atX = 0 ;
		for ( int i = 0 ; i < howManyLetters ; ++ i ) {
			if ( withShadow )
				g.drawImage( shadows[ i ], atX + Font.Shadow_Shift, Font.Shadow_Shift, null );

			g.drawImage( images[ i ], atX, 0, null );

			atX += images[ i ].getWidth ();
		}
		g.dispose ();

		return imageOfString ;
	}

	public BufferedImage getImageOfLetter( String letter )
	{
		BufferedImage image = Font.imageOf( letter, this.fontColor, this.spacingX, this.spacingY );
		return ( image != null ) ? image : Font.imageOf( this.wildLetter, this.fontColor, this.spacingX, this.spacingY );
	}

	public BufferedImage getImageOfLetterInBlack( String letter )
	{
		BufferedImage image = Font.imageOf( letter, Color.black, this.spacingX, this.spacingY );
		return ( image != null ) ? image : Font.imageOf( this.wildLetter, Color.black, this.spacingX, this.spacingY );
	}

	public static BufferedImage imageOf( String letter, Color color, int hSpace, int vSpace )
	{
		if ( letter.isEmpty () ) return null ;
		if ( Font.letterToImage == null ) return null ;
		if ( ! Font.letterToImage.containsKey( letter ) ) return null ;

		return bitmapInStringsToImage( Font.letterToImage.get( letter ), color, hSpace, vSpace );
	}

	public static BufferedImage bitmapInStringsToImage( String [] lines )
	{
		return bitmapInStringsToImage( lines, 0, 0, 0, 0 );
	}

	public static BufferedImage bitmapInStringsToImage( String [] lines, int spaceTwitter, int spaceY )
	{
		return bitmapInStringsToImage( lines, 0, 0, spaceTwitter, spaceY );
	}

	/**
	 * The default palette is opaque black 0xff000000 on 0x00ffffff transparent white
	 */
	public static BufferedImage bitmapInStringsToImage( String [] lines, int xShift, int yShift, int spaceTwitter, int spaceY )
	{
		return bitmapInStringsToImage( lines, Color.black, xShift, yShift, spaceTwitter, spaceY );
	}

	public static BufferedImage bitmapInStringsToImage( String [] lines, Color color )
	{
		return bitmapInStringsToImage( lines, color, 0, 0, 0, 0 );
	}

	public static BufferedImage bitmapInStringsToImage( String [] lines, Color color, int spaceTwitter, int spaceY )
	{
		return bitmapInStringsToImage( lines, color, 0, 0, spaceTwitter, spaceY );
	}

	public static BufferedImage bitmapInStringsToImage( String [] lines, Color color,
								int xShift, int yShift, int spaceTwitter, int spaceY )
	{
		if ( lines == null ) return null ;

		int bitsHeight = lines.length ;
		int bitsWidth  = lines[ 0 ].length ();
		int  width = xShift + bitsWidth  + spaceTwitter ;
		int height = yShift + bitsHeight + spaceY ;

		int fore = color.getRGB ();
		int back = ~ fore ; // flip da bits
		int [] palette = new int [] {  fore, back  };

		java.awt.image.IndexColorModel colorModel
						= new java.awt.image.IndexColorModel (
							/* bits per pixel */ 1,
							/* color map size */ 2, /* map */ palette, /* first entry in map */ 0,
							/* has alpha */ true,
							/* transIndex */ 1,
							/* transferType */ java.awt.image.DataBuffer.TYPE_BYTE );

		BufferedImage image = new BufferedImage( width, height, BufferedImage.TYPE_BYTE_BINARY, colorModel );

		// at first fill it with transparency
		for ( int y = 0 ; y < height ; y ++ )
			for ( int x = 0 ; x < width ; x ++ )
				image.setRGB( x, y, /* transparent */ palette[ 1 ] ) ;

		for ( int y = 0 ; y < bitsHeight ; y ++ )
			for ( int x = 0 ; x < bitsWidth ; x ++ )
				if ( lines[ y ].charAt( x ) != ' ' )
					image.setRGB( x + xShift, y + yShift, /* opaque */ palette[ 0 ] ) ;

		return image ;
	}

	public static StringBuilder dumpTextualBitmap( String prefix, String [] bitmap, String suffix )
	{
		StringBuilder out = new StringBuilder( );

		if ( bitmap == null ) {
			out.append( "null" );
			return out ;
		}

		for ( int l = 0 ; l < bitmap.length ; ++ l )
			out.append( prefix ).append( bitmap[ l ] ).append( suffix ).append( l )
						.append( System.getProperty( "line.separator" ) );

		return out ;
	}

	private static void composeAndWriteImageOFont( java.io.File newFile )
	{
		if ( Font.letterToImage == null ) return ;

		// compose the image of font

		final int lettersPerLine = 16 ; // 32 ;
		final int spacing = 3 ;
		final int yShift = 1 ;

		LettersFile listOfLetters = new LettersFile( );
		int lettersThere = listOfLetters.howManyLetters ();
		int linesInFont = ( ( lettersThere - 1 ) / lettersPerLine ) + 1 ;

		String [] someLetter = Font.letterToImage.get( "O" );
		int heightOfLetter = someLetter.length ;
		int  widthOfLetter = someLetter[ 0 ].length ();

		int stepX = widthOfLetter + spacing ;
		int stepY = heightOfLetter + spacing + yShift ;

		int [] palette = new int []  {	0xff000000 ,	// opaque black
						0x00ffffff  };	// transparent white

		java.awt.image.IndexColorModel colorModel
						= new java.awt.image.IndexColorModel (
							/* bits per pixel */ 1,
							/* color map size */ 2, /* map */ palette, /* first entry in map */ 0,
							/* has alpha */ true,
							/* transIndex */ 1,
							/* transferType */ java.awt.image.DataBuffer.TYPE_BYTE );

		BufferedImage imageOFont = new BufferedImage( lettersPerLine * stepX, linesInFont * stepY,
									BufferedImage.TYPE_BYTE_BINARY, colorModel );
		// fill it with transparency
		java.awt.Graphics2D g = imageOFont.createGraphics ();
		g.setColor( new Color( palette[ 1 ] ) );
		g.fillRect( 0, 0, imageOFont.getWidth (), imageOFont.getHeight () );
		g.dispose ();

		// paint the letters

		int letterX = 0 ;
		int letterY = 0 ;

		for ( int l = 0 ; l < lettersThere ; )
		{
			String letter = listOfLetters.letterAt( l );
			if ( ! letter.isEmpty() ) {
				String [] lines = Font.letterToImage.get( letter );

				for ( int y = 0 ; y < heightOfLetter ; y ++ )
					for ( int x = 0 ; x < widthOfLetter ; x ++ )
						if ( lines[ y ].charAt( x ) != ' ' )
							imageOFont.setRGB( letterX + x, letterY + yShift + y, /* opaque */ palette[ 0 ] ) ;
			}

			if ( ( ++ l ) % lettersPerLine == 0 ) {
				letterX = 0 ;
				letterY += stepY ;
			} else
				letterX += stepX ;
		}

		// and at last
		Pictures.saveAsPNG( imageOFont, newFile );
	}

	public static void main( String [] ignored )
	{
		LettersFile.main( null );

		Font testFont = new Font( "test", "vivid yellow" );

		// print all the letters drawn in the font
		StringBuilder letters = new StringBuilder( );
		for ( String letter : Font.letterToImage.keySet() )
			letters.append( letter );
		System.out.println( letters );

		BufferedImage allTheLetters = testFont.composeImageOfString( letters.toString () );
		Pictures.saveAsPNG( allTheLetters, new java.io.File( FilesystemPaths.getGameStorageInHome (), "Font.test.png" ) );

		// dump the images of letters as lines of text
		for ( String letter : Font.letterToImage.keySet() ) {
			System.out.println();
			System.out.println( "|" + letter + "|" );
			System.out.println();
			System.out.println( Font.dumpTextualBitmap( ".add( \"", Font.letterToImage.get( letter ), "\" ); // " ) );
		}

		{
			///String [] lines = Font.letterToImage.get( "’" );
			///System.out.println( Font.dumpTextualBitmap( "\t\t\tnewlines[ l++ ] = \"", lines, "\" ; // " ) );

			/* String [] newlines = new String [ lines.length ] ;
			int l = 0 ;
			newlines[ l++ ] = "              " ; // 0
			newlines[ l++ ] = "              " ; // 1
			newlines[ l++ ] = "     0000     " ; // 2
			newlines[ l++ ] = "     0000     " ; // 3
			newlines[ l++ ] = "     0000     " ; // 4
			newlines[ l++ ] = "      000     " ; // 5
			newlines[ l++ ] = "      000     " ; // 6
			newlines[ l++ ] = "     000      " ; // 7
			newlines[ l++ ] = "     00       " ; // 8
			newlines[ l++ ] = "              " ; // 9
			newlines[ l++ ] = "              " ; // 10
			newlines[ l++ ] = "              " ; // 11
			newlines[ l++ ] = "              " ; // 12
			newlines[ l++ ] = "              " ; // 13
			newlines[ l++ ] = "              " ; // 14
			newlines[ l++ ] = "              " ; // 15
			newlines[ l++ ] = "              " ; // 16
			newlines[ l++ ] = "              " ; // 17
			newlines[ l++ ] = "              " ; // 18
			newlines[ l++ ] = "              " ; // 19
			newlines[ l++ ] = "              " ; // 20
			newlines[ l++ ] = "              " ; // 21
			newlines[ l++ ] = "              " ; // 22
			newlines[ l++ ] = "              " ; // 23
			newlines[ l++ ] = "              " ; // 24

			Font.letterToImage.put( "’", newlines ); */
		}

		Font.composeAndWriteImageOFont( new java.io.File( FilesystemPaths.getGameStorageInHome (), "font.new.png" ) );
	}

}
