// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2024 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels.behaviors ;

import head.over.heels.Item ;
import head.over.heels.AvatarItem ;
import head.over.heels.FreeItem ;


/**
 * Abstraction for the item’s behaviour. A game’s item changes its activity in each cycle of update.
 * Different kinds of behaviour define different transitions between these activities
 */

public abstract class Behaviour
{
	/**
	 * Updates the item’s behaviour programmatically
	 * @return true if the item can be updated thereafter (it didn’t disappear from the room)
	 */
	public abstract boolean update () ;

	/**
	 * protected, new behaviour can be created via Behaviour.byName( String, Item )
	 */
	protected Behaviour( Item item, String nameOfBehavior )
	{
		if ( item == null ) throw new NullPointerException( "an item than behaves can't be null" ) ;

		this.itemThatBehaves = item ;
		this.name = nameOfBehavior ;
	}

	/**
	 * The item that behaves
	 */
	private Item itemThatBehaves ;

	public Item getItem () {  return this.itemThatBehaves ;  }

	/**
	 * The name of behavior
	 */
	private String name ; // = "abstract behaviour"

	public String getName () {  return this.name ;  }

	private Activity currentActivity = Activity.Waiting ;

	public Activity getCurrentActivity () {  return this.currentActivity ;  }
	public void setCurrentActivity ( Activity newActivity ) {  changeActivityDueTo( newActivity, null );  }

	/**
	 * Another item that changed activity of this one
	 */
	private Item affectedBy = null ;

	public void changeActivityDueTo ( Activity newActivity, Item dueTo )
	{
		this.currentActivity = newActivity ;
		this.affectedBy = dueTo ;
	}

	public static Behaviour byName( String name, Item item )
	{
		if ( item instanceof AvatarItem ) {
			if ( name.equals( "Head" ) )
				return new CharacterHead( (AvatarItem) item );
			else
			if ( name.equals( "Heels" ) )
				return new CharacterHeels( (AvatarItem) item );
			else
			if ( name.equals( "Head over Heels" ) )
				return new CharacterHeadOverHeels( (AvatarItem) item );
		}
		else if ( item instanceof FreeItem ) {
			// ....
		}
		// none of the above
		else {
			if ( name.contains( "disappearing" ) )
				return new Volatile( item, name );
			else
			if ( name.isEmpty () || name.equals( "still" ) || name.equals( "bubbles" ) )
				return null ;
			else
				throw new IllegalArgumentException( "unknown behavior \"" + name + "\" for " + item.toString () );
		}

		return null ;
	}

}
