// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2026 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels.items ;

import head.over.heels.Drawable ;
import head.over.heels.ShadyMediated ;

import head.over.heels.IntegerPoint2D ;
import head.over.heels.NamedOffscreenImage ;


/**
 * A set of such tiles forms the floor of the room
 */

public class FloorTile extends ShadyMediated implements Drawable
{
	/**
	 * @param cell the grid cell where where the tile is
	 * @param graphicsOfTile the picture of the tile
	 */
	public FloorTile( IntegerPoint2D cell, NamedOffscreenImage graphicsOfTile )
	{
		this.cell = cell ;

		if ( graphicsOfTile == null ) throw new IllegalArgumentException( "null floor tile graphics" );
		this.rawImage = graphicsOfTile ;

		this.refreshShadedImage() ;
	}

	// the room’s grid cell where this tile is located
	private final IntegerPoint2D cell ;

	public IntegerPoint2D getCell () {  return this.cell ;  }

	///private int getCellX () {  return this.cell.getX() ;  }
	///private int getCellY () {  return this.cell.getY() ;  }

	// picture of the tile
	private final NamedOffscreenImage rawImage ;

	// picture of the shaded tile
	private NamedOffscreenImage shadedImage ;

	public NamedOffscreenImage getShadedImage () {  return this.shadedImage ;  }

	public void setShadedImage ( NamedOffscreenImage shaded ) {
		super.setWantShadow( false );

		this.shadedImage = shaded ; // just ‘=’ without copying the graphics via shadedImage.replicateImage( shaded )
		this.shadedImage.setName( "shaded " + this.rawImage.getName() );
	}

	public void refreshShadedImage () {
		if ( this.shadedImage == null )
			this.shadedImage = new NamedOffscreenImage( this.rawImage.getSize() );

		if ( super.getWantShadow() || this.shadedImage.getName().startsWith( "fresh copy" ) )
			return ; // is fresh already or is in the process of shading

		this.shadedImage.replicateImage( this.rawImage ); // copy the graphics
		this.shadedImage.setName( "fresh copy of " + this.rawImage.getName() );

		super.setWantShadow( true );
	}

	/**
	 * Draw this tile of floor
	 */
	public void draw ( java.awt.Graphics2D g )
	{
		// ...
	}

}
