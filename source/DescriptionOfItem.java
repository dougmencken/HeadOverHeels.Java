// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2023 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels ;

/**
 * The description of an item as read from items.xml
 */

public final class DescriptionOfItem
{

	private String label ;

	public String getLabel () {  return label ;  }

	/**
	 * The three spatial dimensions of the item, along the x, along the y, and height along the z
	 */
	private int widthX ;
	private int widthY ;
	private int height ;

	/**
	 * The weight of the item in seconds, higher for the bigger speed of falling, zero for no gravity (no falling)
	 */
	private double weight ;

	/**
	 * The time in seconds the item takes to move one single isometric unit
	 */
	private double speed ;

	public DescriptionOfItem ( String itemLabel )
	{
		this.label = itemLabel ;

		this.widthX = 0 ;
		this.widthY = 0 ;
		this.height = 0 ;

		this.weight = 0.0 ;
		this.speed = 0.0 ;
	}

}
