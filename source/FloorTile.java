// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2024 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels ;


/**
 * A set of such tiles forms the floor of the room
 */

public class FloorTile extends ShadyAndMediated implements Drawable
{
	// the room’s grid cell where this tile is located
	private int cellX ;
	private int cellY ;

	// picture of the tile
	private final OffscreenImage rawImage ;

	// picture of the shaded tile
	private OffscreenImage shadyImage ;

	/**
	 * @param cx the X of the grid cell where the tile is
	 * @param cy the Y of the grid cell where the tile is
	 * @param graphicsOfTile the picture of the tile
	 */
	public FloorTile( int cx, int cy, OffscreenImage graphicsOfTile )
	{
		this.cellX = cx ;
		this.cellY = cy ;
		this.rawImage = graphicsOfTile ;
		this.shadyImage = new OffscreenImage( graphicsOfTile ); // copy the graphics

		setWantShadow( true );
	}

	/**
	 * Draw this tile of floor
	 */
	public void draw ( java.awt.Graphics2D g )
	{
		// ...
	}

}
