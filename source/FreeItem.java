// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2024 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels ;


/**
 * Free items may be anywhere and move within the room, such as player characters
 */

public class FreeItem extends Item
{
	/**
	 * @param description the description of this item
	 * @param x the position on X
	 * @param y the position on Y
	 * @param z the position on Z, or how far is floor
	 * @param heading the angular orientation
	 */
	public FreeItem( DescriptionOfItem description, int x, int y, int z, String heading )
	{
		super( description );

		this.theX = x ;
		this.theY = y ;
		this.theZ = z ;

		this.heading = heading ;
	}

	// the copy constructor
	public FreeItem( FreeItem that )
	{
		super( that );
		this.theX = that.theX ;
		this.theY = that.theY ;
		this.theZ = that.theZ ;
		this.heading = that.heading ;
	}

	// the position in 3-dimensional space
	private int theX ;
	private int theY ;
	private int theZ ;

	// the angular orientation
	private String heading ;

	public String getHeading () {  return this.heading ;  }

}
