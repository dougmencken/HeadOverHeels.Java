// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2025 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels ;

import java.awt.image.BufferedImage ;


public class NamedOffscreenImage extends OffscreenImage
{

	private String theName = "image-" + StringUtilities.makeRandomString( 12 ) ;

	public String getName () {  return this.theName ;  }
	public void setName ( String name ) {  this.theName = name ;  }

	public NamedOffscreenImage( int width, int height ) {  super( width, height );  }

	public NamedOffscreenImage( IntegerDimensions2D size ) {  super( size );  }

	public NamedOffscreenImage( java.io.File path, String fileName ) throws NoSuchPictureException
	{
		super( path, fileName );
		setName( fileName );
	}

	public NamedOffscreenImage( BufferedImage toCopy ) // the copy constructor
	{
		super( toCopy );
		setName( "copied-" + getName() );
	}

	public NamedOffscreenImage( NamedOffscreenImage toCopy ) // the copy constructor
	{
		super( toCopy );
		setName( "copy of " + toCopy.getName() );
	}

}
