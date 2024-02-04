// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2024 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels ;

public abstract class Item extends ShadyAndMediated
{

	// creates an item by a description
	public Item( DescriptionOfItem description, int z, String way )
	{
		this.descriptionOfItem = description ;
	}

	// the copy constructor
	public Item( Item item )
	{
		this.descriptionOfItem = item.descriptionOfItem ;
	}

	private DescriptionOfItem descriptionOfItem ;

	public DescriptionOfItem getDescriptionOfItem () {  return descriptionOfItem ;  }

}
