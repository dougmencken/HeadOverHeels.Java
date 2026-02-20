// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2026 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels ;

import java.awt.image.BufferedImage ;

import java.awt.Color ;
import java.awt.Graphics2D ;

import head.over.heels.Pictures ;
import head.over.heels.NoSuchPictureException ;
import head.over.heels.StringUtilities ;


public class OffscreenImage extends BufferedImage
{

	public OffscreenImage( int width, int height )
	{
		super( width, height, BufferedImage.TYPE_INT_ARGB );
	}

	public OffscreenImage( IntegerSize2D size )
	{
		super( size.getWidth(), size.getHeight(), BufferedImage.TYPE_INT_ARGB );
	}

	public OffscreenImage( BufferedImage toCopy ) // the copy constructor
	{
		this( toCopy != null ? toCopy.getWidth() : 1, toCopy != null ? toCopy.getHeight() : 1 );
		replicateImage( toCopy );
	}

	/**
	 * Takes a path and a file name to read an image from file
	 */
	public OffscreenImage( java.io.File path, String name ) throws NoSuchPictureException
	{
		this( /* make a copy */ Pictures.readFromFile( new java.io.File( path, name ) ) );

		if ( getWidth() == 1 && getHeight() == 1 ) // 1×1 pixel means an image wasn’t read
			throw new NoSuchPictureException( "can’t read image from file "
								+ StringUtilities.putInQuotes( name )
								+ " in " + path.getAbsolutePath() );
	}

	public IntegerSize2D getSize () {  return new IntegerSize2D( getWidth(), getHeight() ) ;  }

	public void fillWithColor ( java.awt.Color color )
	{
		Graphics2D g = super.createGraphics ();
		fillWithColor( color, g );
		g.dispose ();
	}

	private void fillWithColor ( java.awt.Color fillColor, Graphics2D g2d )
	{
		g2d.setColor( fillColor );
		g2d.fillRect( 0, 0, getWidth(), getHeight() );
	}

	public void fillWithTransparencyGrid () {  fillWithTransparencyGrid( 8 );  }

	public void fillWithTransparencyGrid ( int sizeOfTile )
	{
		if ( sizeOfTile < 1 )
			throw new IllegalArgumentException( "the size of transparency grid tile is " + sizeOfTile + ", which is less than 1" );

		final int lighterGrey = ( new Color( 0xcc, 0xcc, 0xcc, 0xff ) ).getRGB() ;
		final int darkerGrey = ( new Color( 0x80, 0x80, 0x80, 0xff ) ).getRGB() ;

		int doubleTile = sizeOfTile << 1 ;

		int width = getWidth ();
		int height = getHeight ();

		for ( int y = 0 ; y < height ; y ++ ) {
			for ( int x = 0 ; x < width ; x ++ )
			{
				boolean lighter = ( ( y % sizeOfTile ) == ( y % doubleTile ) && ( x % sizeOfTile ) != ( x % doubleTile ) ) ||
							( ( y % sizeOfTile ) != ( y % doubleTile ) && ( x % sizeOfTile ) == ( x % doubleTile ) ) ;

				setRGB( x, y, lighter ? lighterGrey : darkerGrey );
			}
		}
	}

	public void replicateImage( BufferedImage toCopy )
	{
		replicateImage( toCopy, Colours.makeTransparent( Colours.grey50 ) );
	}

	public void replicateImage( BufferedImage toCopy, java.awt.Color backColor )
	{
		if ( toCopy == null ) return ; // can’t replicate null

		Graphics2D g = super.createGraphics ();

		if ( toCopy.getWidth() < getWidth() || toCopy.getHeight() < getHeight() )
			fillWithColor( backColor, g );

		// the copying itself happens here
		g.drawImage( toCopy, 0, 0, null );

		g.dispose ();
	}

}
