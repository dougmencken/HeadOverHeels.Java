// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2026 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels.items ;

import head.over.heels.Drawable ;
import head.over.heels.IntegerPoint2D ;


/**
 * The grid items are those which are placed in a single grid cell. They have the same
 * widths as the room grid cells have. Grid items are mostly static. Only the Z coordinate
 * of a grid item can be changed, but not the position along X and Y
 */

public class GridItem extends DescribedItem implements Drawable
{
	/**
	 * @param description the description of this item
	 * @param cell the cell where to place the item
	 * @param z the position on Z, or how far is the floor, in free units
	 * @param towards the angular orientation
	 */
	public GridItem( DescriptionOfItem description, IntegerPoint2D cell, int z, String towards )
	{
		super( description );

		this.cell = cell ;
		this.theZ = z ;

		this.orientation = towards ;
	}

	// the copy constructor
	public GridItem( GridItem that )
	{
		super( that );
		this.cell = new IntegerPoint2D( that.cell );
		this.theZ = that.theZ ;
		this.orientation = that.orientation ;
	}

	// the room’s cell
	private IntegerPoint2D cell ;

	/**
	 * @return the room’s cell where this item is placed
	 */
	public IntegerPoint2D getCell () {  return this.cell ;  }

	private int getCellX () {  return this.cell.getX() ;  }
	private int getCellY () {  return this.cell.getY() ;  }

	// the position along Z, or how far is the floor, in free units
	private int theZ ;

	// gives the position in 3-dimensional space in free units
	public int getZ () {  return this.theZ ;  }
	public int getX () {  return getCellX() * getWidthX() ;  } // the widths of a grid item are equal...
	public int getY () {  return ( getCellY() + 1 ) * getWidthY() - 1 ;  } // ...to the size of a single room’s cell

	/**
	 * Sets the position along Z (how far is the floor) in free units
	 */
	public void setZ ( int newZ ) {  this.theZ = newZ ;  }

	public void setX ( int newX ) {  /* don’t do anything here */  }
	public void setY ( int newY ) {  /* do nothing here */  }

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
