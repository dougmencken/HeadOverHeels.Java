// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2024 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels ;


/**
 * The grid items are those which are placed in a single grid cell. They have the same
 * widths as the grid cells have. Grid items are mostly static. Only the Z coordinate
 * of a grid item can be changed, but not the position on X and Y
 */

public class GridItem extends Item implements Drawable
{
	/**
	 * @param description the description of this item
	 * @param cx the X of the grid cell where the item is
	 * @param cy the Y of the grid cell where the item is
	 * @param z the position on Z, or how far is floor
	 * @param where the angular orientation
	 */
	public GridItem( DescriptionOfItem description, int cx, int cy, int z, String where )
	{
		super( description );

		this.cellX = cx ;
		this.cellY = cy ;
		this.theZ = z ;

		this.orientation = where ;
	}

	// the copy constructor
	public GridItem( GridItem that )
	{
		super( that );
		this.cellX = that.cellX ;
		this.cellY = that.cellY ;
		this.theZ = that.theZ ;
		this.orientation = that.orientation ;
	}

	// the room’s grid cell where this item is placed
	private int cellX ;
	private int cellY ;

        // the position on Z, or how far is floor, in free isometric units
	private int theZ ;

	// the angular orientation
	private String orientation ;

	public String getOrientation () {  return this.orientation ;  }

	/**
	 * Draw this grid item
	 */
	public void draw ( java.awt.Graphics2D g )
	{
		// ...
	}

}
