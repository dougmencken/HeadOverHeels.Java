// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2024 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels ;

import java.awt.image.BufferedImage ;
import java.awt.Color ;


/**
 * The selection of utilities to deal with pictures
 */

public class Pictures
{

	public static BufferedImage exactCopy ( BufferedImage picture )
	{
		if ( picture == null ) throw new IllegalArgumentException ();

		java.awt.image.ColorModel colorModel = picture.getColorModel() ;
		return new BufferedImage( colorModel, picture.copyData( null ), colorModel.isAlphaPremultiplied (), null );
	}

	public static boolean saveAsPNG ( BufferedImage picture, String where )
	{
		return saveAsPNG( picture, new java.io.File( where ) );
	}

	public static boolean saveAsPNG ( BufferedImage picture, java.io.File file )
	{
		try {
			return javax.imageio.ImageIO.write( picture, "png", file );
		} catch ( java.io.IOException e ) {  return false ;  }
	}

	public static BufferedImage readFromFile ( String what )
	{
		return readFromFile( new java.io.File( what ) );
	}

	public static BufferedImage readFromFile ( java.io.File file )
	{
		try {
			return javax.imageio.ImageIO.read( file ) ;
		} catch ( java.io.IOException e ) {  return null ;  }
	}

	public static void toGrayscale ( BufferedImage picture )
	{
		if ( picture == null ) throw new IllegalArgumentException ();

		synchronized ( picture ) {
			for ( int y = 0 ; y < picture.getHeight () ; y ++ ) {
				for ( int x = 0 ; x < picture.getWidth () ; x ++ )
				{
					Color original = new Color( picture.getRGB( x, y ) );

					// skip the fully transparent pixels
					if ( Colours.isFullyTransparent( original ) ) continue ;

					/* imagine the color as the linear geometric vector c { r, g, b }
					   this color turns into the shade of gray r=g=b =w with vector b { w, w, w }
					   the lengths of vectors are c•c = rr + gg + bb and b•b = ww + ww + ww = 3ww
					   the converted vector has the same length as the original
					   for the same lengths
							sqrt ( c•c ) = sqrt ( b•b )
							sqrt( rr + gg + bb ) = sqrt( 3 ) * w
							w = sqrt( ( rr + gg + bb ) / 3 )
					*/
					double   red = (double) original.getRed() ;
					double green = (double) original.getGreen() ;
					double  blue = (double) original.getBlue() ;
					double    ww = ( red * red + green * green + blue * blue ) / 3.0 ;

					int gray = (int) Math.round( Math.sqrt( ww ) );
					Color grayed = new Color( gray, gray, gray, original.getAlpha() );

					picture.setRGB( x, y, grayed.getRGB() );
			}	}
		}
	}

	public static void colorizeWhite( BufferedImage picture, Color to )
	{
		Pictures.replaceColor( picture, Color.white, to );
	}

	public static void colorizeBlack( BufferedImage picture, Color to )
	{
		Pictures.replaceColor( picture, Color.black, to );
	}

	public static void replaceColor( BufferedImage picture, Color from, Color to )
	{
		if ( picture == null || to == null || from == null ) throw new IllegalArgumentException ();

		if ( to.equals( from ) ) return ;

		synchronized ( picture ) {
			for ( int y = 0 ; y < picture.getHeight () ; y ++ ) {
				for ( int x = 0 ; x < picture.getWidth () ; x ++ )
				{
					Color pixel = new Color( picture.getRGB( x, y ) );
					if ( pixel.equals( from ) )
						picture.setRGB( x, y, to.getRGB() );

					/* if ( pixel.getRed() == from.getRed()
							&& pixel.getGreen() == from.getGreen()
								&& pixel.getBlue() == from.getBlue() ) {
						Color alphaKept = new Color( to.getRed(), to.getGreen(), to.getBlue(), pixel.getAlpha() );
						picture.setRGB( x, y, alphaKept.getRGB() );
					} */
			}	}
		}
	}

	public static BufferedImage cloneAsARGBWithReplacingColor( BufferedImage picture, Color from, Color to )
	{
		if ( picture == null || from == null || to == null ) throw new IllegalArgumentException ();

		BufferedImage copy = new BufferedImage( picture.getWidth (), picture.getHeight (), BufferedImage.TYPE_INT_ARGB );

		synchronized ( picture ) {
			for ( int y = 0 ; y < picture.getHeight () ; y ++ ) {
				for ( int x = 0 ; x < picture.getWidth () ; x ++ )
				{
					int argb = picture.getRGB( x, y );
					if ( argb == from.getRGB() )
						copy.setRGB( x, y, to.getRGB() );
					else
						copy.setRGB( x, y, argb );
			}	}
		}

		return copy ;
	}

	public static BufferedImage cloneAsARGB ( BufferedImage picture )
	{
		if ( picture == null ) throw new IllegalArgumentException ();

		BufferedImage copy ;
		synchronized ( picture ) {
			copy = new BufferedImage( picture.getWidth (), picture.getHeight (),
							/* picture.getType () */ BufferedImage.TYPE_INT_ARGB );
			for ( int y = 0 ; y < picture.getHeight () ; y ++ )
				for ( int x = 0 ; x < picture.getWidth () ; x ++ )
					copy.setRGB( x, y, picture.getRGB( x, y ) );
		}

		return copy ;
	}

	public static BufferedImage cloneSubpictureAsARGB ( BufferedImage picture, int x, int y, int w, int h )
	{
		if ( picture == null ) throw new IllegalArgumentException ();

		BufferedImage part ;
		synchronized ( picture ) {
			part = new BufferedImage( w, h, BufferedImage.TYPE_INT_ARGB );
			for ( int iy = 0 ; iy < h ; iy ++ )
				for ( int ix = 0 ; ix < w ; ix ++ )
					part.setRGB( ix, iy, picture.getRGB( x + ix, y + iy ) );
		}

		return part ;
	}

	public static BufferedImage cloneAsIndexedColor ( BufferedImage picture )
	{
		java.util.Set < Integer > colors
			= new java.util.TreeSet < Integer > (	/*new java.util.Comparator < Integer > ()
								{
									public int compare( Integer one, Integer other ) {
										long unsignedOne = one.longValue() & 0xffffffffL ;
										long unsignedOther = other.longValue() & 0xffffffffL ;
										     if ( unsignedOne < unsignedOther ) return -1 ;
										else if ( unsignedOne > unsignedOther ) return  1 ;
										return 0 ;
									}
								}*/
							    ) ;
		int indexOfTransparent = -1 ;

		int  width = picture.getWidth ();
		int height = picture.getHeight ();

		synchronized ( picture ) {
			for ( int y = 0 ; y < height ; y ++ )
				for ( int x = 0 ; x < width ; x ++ ) {
					int argb = picture.getRGB( x, y );
					if ( ( ( argb >> 24 ) & 0xff ) == 0 ) indexOfTransparent = colors.size ();
					colors.add( argb );
				}
		}

		int howManyColors = colors.size ();
		if ( howManyColors > 256 )
			throw new IllegalArgumentException( "the picture has " + howManyColors + " various colors, it's more than 256" );

		Object [] array = colors.toArray ();
		int [] colorMap = new int [ howManyColors ];
		for ( int i = 0 ; i < howManyColors ; ++ i )
			colorMap[ i ] = ( (Integer) array[ i ] ).intValue ();

		int bits = 8 ;
		     if ( howManyColors <=  2 ) bits = 1 ;
		else if ( howManyColors <=  4 ) bits = 2 ;
		else if ( howManyColors <= 16 ) bits = 4 ;

		java.awt.image.IndexColorModel indexedColors
						= new java.awt.image.IndexColorModel (
							/* bits per pixel */ bits,
							/* size */ howManyColors, /* colors */ colorMap, /* first index in colors */ 0,
							/* has alpha */ indexOfTransparent >= 0,
							/* transIndex */ indexOfTransparent,
							/* transferType */ java.awt.image.DataBuffer.TYPE_BYTE );

		int imageType = ( bits < 8 ) ? BufferedImage.TYPE_BYTE_BINARY : BufferedImage.TYPE_BYTE_INDEXED ;
		BufferedImage newPicture = new BufferedImage( width, height, imageType, indexedColors );

		synchronized ( picture ) {
			for ( int y = 0 ; y < height ; y ++ )
				for ( int x = 0 ; x < width ; x ++ )
					newPicture.setRGB( x, y, picture.getRGB( x, y ) );
		}

		return newPicture ;
	}

	public static boolean listColorModelIfIndexed ( BufferedImage picture )
	{
		java.awt.image.ColorModel colors = picture.getColorModel ();
		if ( colors instanceof java.awt.image.IndexColorModel ) {
			listIndexColorModel( (java.awt.image.IndexColorModel) colors );
			return true ;
		}

		return false ;
	}

	public static void listIndexColorModel ( java.awt.image.IndexColorModel indexedColors )
	{
		int [] colorMapRGBs = new int[ indexedColors.getMapSize() ];
		indexedColors.getRGBs( colorMapRGBs );

		for ( int i = 0 ; i < colorMapRGBs.length ; ++ i ) {
			System.out.print( "indexed colors [ " + i + " ] = " + String.format( "0x%08x", colorMapRGBs[ i ] ) );
			if ( i == indexedColors.getTransparentPixel () ) System.out.print( " *transparent*" );
			System.out.println() ;
		}
	}

	public static BufferedImage cloneWithTwiceTheHeight ( BufferedImage before )
	{
		if ( before == null ) return null ;

		BufferedImage after ;
		synchronized ( before ) {
			int  width = before.getWidth ();
			int height = before.getHeight ();
			int type = before.getType ();

			if ( type == BufferedImage.TYPE_BYTE_BINARY || type == BufferedImage.TYPE_BYTE_INDEXED )
				after = new BufferedImage( width, height << 1, type, (java.awt.image.IndexColorModel) before.getColorModel() );
			else
				after = new BufferedImage( width, height << 1, type );

			for ( int y = 0 ; y < height ; y ++ )
				for ( int x = 0 ; x < width ; x ++ ) {
					after.setRGB( x, 2*y, before.getRGB( x, y ) );
					after.setRGB( x, 2*y + 1, before.getRGB( x, y ) );
				}
		}

		return after ;
	}

	private Pictures() {} // no instances

	public static void main( String [] arguments )
	{
		if ( arguments.length == 0 ) {
			System.out.println( "image files are expected as arguments" );
			return ;
		}

		for ( int a = 0 ; a < arguments.length ; ++ a ) {
			String nameOFile = arguments[ a ];

			BufferedImage image = Pictures.readFromFile( new java.io.File( FilesystemPaths.getPathToGameData(), nameOFile ) );
			if ( image == null ) {
				image = Pictures.readFromFile( new java.io.File( nameOFile ) );
				if ( image == null ) {
					System.out.println( "oops, can’t read the image from file \"" + nameOFile + "\"" );
					continue ;
				}
			}

			// replace magenta background with transparent white
			BufferedImage withRealTransparency = Pictures.cloneAsARGBWithReplacingColor( image,
									Color.magenta, new Color( 255, 255, 255, /* alpha */ 0 ) );
			// and white foreground with black
			BufferedImage withBlackForeground = Pictures.cloneAsARGBWithReplacingColor( withRealTransparency, Color.white, Color.black );

			// then convert it to the indexed colors
			BufferedImage newImage = withBlackForeground ;
			try {
				newImage = Pictures.cloneAsIndexedColor( withBlackForeground );
			}
			// if can't convert to indexed colors, it fails like "the picture has 29671 various colors, it's more than 256"
			catch ( IllegalArgumentException e ) { /* ignore it */ }

			int lastSeparatorAt = nameOFile.lastIndexOf( java.io.File.separatorChar );
			if ( lastSeparatorAt > 0 ) nameOFile = nameOFile.substring( lastSeparatorAt );
			int lastDotAt = nameOFile.lastIndexOf( '.' );
			String withoutSuffix = ( lastDotAt > 0 ) ? nameOFile.substring( 0, lastDotAt ) : nameOFile ;
			java.io.File newImageFile = new java.io.File( FilesystemPaths.getGameStorageInHome (), withoutSuffix + ".new.png" );
			if ( Pictures.saveAsPNG( newImage, newImageFile ) )
				System.out.println( "saved as PNG file \"" + newImageFile.getPath() + "\"" );
		}
	}

}
