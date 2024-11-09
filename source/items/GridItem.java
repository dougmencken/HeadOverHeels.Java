// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2024 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels.items ;

import head.over.heels.Drawable ;
import head.over.heels.rooms.Room ;


/**
 * The grid items are those which are placed in a single grid cell. They have the same
 * widths as the grid cells have. Grid items are mostly static. Only the Z coordinate
 * of a grid item can be changed, but not the position on X and Y
 */

public class GridItem extends DescribedItem implements Drawable
{
	/**
	 * @param description the description of this item
	 * @param cx the X of the grid cell where to place the item
	 * @param cy the Y of the grid cell where to place the item
	 * @param z the position on Z, or how far is the floor, in free units
	 * @param where the angular orientation
	 */
	public GridItem( DescriptionOfItem description, short cx, short cy, int z, String where )
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
	private short cellX ;
	private short cellY ;

	/**
	 * Position along X of the room’s grid cell
	 */
	public short getCellX () {  return this.cellX ;  }

	/**
	 * Position along Y of the room’s grid cell
	 */
	public short getCellY () {  return this.cellY ;  }

	/**
	 * The length of the side of one room’s tile (cell)
	 */
	public short oneTileLong ()
	{
		return ( getMediator() != null ) ? getMediator().getRoom().getSizeOfOneTile() : Room.single_tile_size ;
	}

	// the position along Z, or how far is the floor, in free units
	private int theZ ;

	// gives the position in 3-dimensional space in free units
	public int getZ () {  return this.theZ ;  }
	public int getX () {  return this.cellX * getWidthX() ;  } // the widths of a grid item are equal to the size of a single room’s tile
	public int getY () {  return ( this.cellY + 1 ) * getWidthY() - 1 ;  }

	/**
	 * Sets the position along Z (how far is the floor) in free units
	 */
	private void setZ ( int newZ ) {  this.theZ = newZ ;  }

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
