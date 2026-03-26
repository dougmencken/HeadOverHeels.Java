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

	public void putPicture( NamedOffscreenImage image )
	{
		this.putPicture( image != null ? image.getName() : null, image );
	}

	public void putPicture( String name, NamedOffscreenImage image )
	{
		if ( image != null ) {
			String key = PoolOfPictures.keyByName( name ) ;
			this.pictures.put( key, image );
			System.out.println( "image " + StringUtilities.putInQuotes( image.getName() ) + " added to the pool"
						+ " as " + StringUtilities.putInSingleQuotes( key ) );
		} else
			this.forgetPicture( name ); // putPicture( name, null ) does forgetPicture( name )
	}

	public NamedOffscreenImage getPicture( String name )
	{
		NamedOffscreenImage picture = this.pictures.get( PoolOfPictures.keyByName( name ) ) ;

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
					this.putPicture( picture ); // add the read image to the pool
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
		String key = PoolOfPictures.keyByName( name );
		System.out.println( "removing " + StringUtilities.putInSingleQuotes( key ) + " from the image pool" );
		return this.pictures.remove( key );
	}

	public boolean hasPicture( String name )
	{
		return this.pictures.get( PoolOfPictures.keyByName( name ) ) != null ;
		//  or this.pictures.containsKey( keyByName( name ) )
	}

	public void clear () {  this.pictures.clear() ;  }

	public static String whichGraphicsSet ()
	{
		return GameManager.getInstance().getChosenGraphicsSet() ;
	}

	private static String keyByName ( String name )
	{
		if ( name == null ) name = "null" ;
		return PoolOfPictures.whichGraphicsSet() + ":" + name ;
	}

	public static final File gfx_in_gamedata = new File( Storage.getPathToGameData(), "gfx" );

}
