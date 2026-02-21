// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2026 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels ;

import java.util.HashMap ;
import java.io.File ;


/**
 * A storage of pictures that are read from files once and can be used later whenever needed
 */

public class PoolOfPictures
{

	private static PoolOfPictures recentPool = null ;

	public static PoolOfPictures getRecentPool()
	{
		if ( PoolOfPictures.recentPool == null ) new PoolOfPictures() ;
		return PoolOfPictures.recentPool ;
	}

	/* pictures are stored here as name-image pairs */
	private final HashMap< String, NamedOffscreenImage > pictures ;

	private PoolOfPictures ()
	{
		this.pictures = new HashMap< String, NamedOffscreenImage > () ;

		// the last created pool is the recent
		PoolOfPictures.recentPool = this ;
	}

	public void putPicture( String name, NamedOffscreenImage image )
	{
		if ( image != null ) {
			this.pictures.put( PoolOfPictures.keyByFileName( name ), image );
			System.out.println( "image " + StringUtilities.putInQuotes( image.getName() ) + " added to the pool" );
		} else
			this.forgetPicture( name ); // putPicture( name, null ) is the same as forgetPicture( name )
	}

	public NamedOffscreenImage getPicture( String name )
	{
		NamedOffscreenImage picture = this.pictures.get( PoolOfPictures.keyByFileName( name ) ) ;

		if ( picture == null ) {
		// try to read it from file
			File gfxFolder = new File( PoolOfPictures.gfx_in_gamedata, PoolOfPictures.whichGraphicsSet() );
			File graphicsFile = FileUtilities.findFirstFileByNameRecursively( gfxFolder, name );
			if ( graphicsFile != null ) {
				try {
					picture = new NamedOffscreenImage( graphicsFile.getParentFile(), graphicsFile.getName() );
				} catch ( NoSuchPictureException ex ) {  picture = null ;  }

				if ( picture != null ) {
					///picture.setName( name ); // (redundant) name is already set by the constructor
					this.putPicture( name, picture ); // add the read image to the pool
				}
			}
		}

		return picture ;
	}

	/**
	 * @return the image associated with ‘name’ before it is forgotten, or null
	 */
	public NamedOffscreenImage forgetPicture( String name )
	{
		System.out.println( "removing " + StringUtilities.putInQuotes( name ) + " from the image pool" );
		return this.pictures.remove( PoolOfPictures.keyByFileName( name ) );
	}

	public boolean hasPicture( String name )
	{
		return this.pictures.get( PoolOfPictures.keyByFileName( name ) ) != null ;
		//  or this.pictures.containsKey( keyByFileName( name ) )
	}

	public void clear () {  this.pictures.clear() ;  }

	public static String whichGraphicsSet ()
	{
		return GameManager.getInstance().getChosenGraphicsSet() ;
	}

	private static String keyByFileName ( String fileName )
	{
		return PoolOfPictures.whichGraphicsSet() + ":" + fileName ;
	}

	public static final File gfx_in_gamedata = new File( Storage.getPathToGameData(), "gfx" );

}
