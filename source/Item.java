// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2024 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels ;

import head.over.heels.behaviors.Behaviour ;


public abstract class Item extends ShadyAndMediated
{

	// creates an item by a description
	public Item( DescriptionOfItem description )
	{
		this.descriptionOfItem = description ;
	}

	// the copy constructor
	public Item( Item item )
	{
		this.descriptionOfItem = item.descriptionOfItem ;

		if ( item.behavior == null )
			this.behavior = null ;
		else
			this.setBehaviourOf( item.behavior.getName () );
	}

	private DescriptionOfItem descriptionOfItem ;

	public DescriptionOfItem getDescriptionOfItem () {  return this.descriptionOfItem ;  }

	// the behaviour of item
	private Behaviour behavior = null ;

	public Behaviour getBehaviour () {  return this.behavior ;  }

	public void setBehaviourOf ( String name )
	{
		this.behavior = Behaviour.byName( name, this );
	}

	/**
	 * For an item with behavior, update that behavior programmatically
	 * @return true if the item can be updated thereafter (it didn’t disappear from the room)
	 */
	public boolean updateItem ()
	{
		return ( this.behavior != null ) ? this.behavior.update() : true ;
	}

	public String toString ()
	{
		return "item " + super.toString() ;
	}

}
