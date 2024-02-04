// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2024 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels ;

import java.awt.image.BufferedImage ;

import java.awt.Graphics2D ;


public class OffscreenImage extends BufferedImage
{

	public OffscreenImage( int width, int height )
	{
		super( width, height, BufferedImage.TYPE_INT_ARGB );
	}

	public OffscreenImage( OffscreenImage toCopy ) // the copy constructor
	{
		this( toCopy, toCopy.getWidth (), toCopy.getHeight (), new java.awt.Color( 0, 0, 0, /* alpha */ 0 ) );
	}

	public OffscreenImage( OffscreenImage toCopy, int newWidth, int newHeight, java.awt.Color backColor )
	{
		this( newWidth, newHeight );

		Graphics2D g = super.createGraphics ();

		if ( toCopy.getWidth () < newWidth || toCopy.getHeight () < newHeight ) {
			g.setColor( backColor );
			g.fillRect( 0, 0, getWidth(), getHeight() );
		}

		// the copying itself happens here
		g.drawImage( toCopy, 0, 0, null );

		g.dispose ();
	}

	public void fillWithColor ( java.awt.Color fillColor )
	{
		Graphics2D g = super.createGraphics ();

		g.setColor( fillColor );
		g.fillRect( 0, 0, getWidth(), getHeight() );

		g.dispose ();
	}

}
