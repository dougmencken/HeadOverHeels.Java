// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2024 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels.items ;

import head.over.heels.Mediated ;
import head.over.heels.OffscreenImage ;

import head.over.heels.behaviors.Behaviour ;

import java.util.Vector ;


/**
 * The most abstract item of the game
 */

public abstract class TheMostAbstractItem extends Mediated
{

	protected TheMostAbstractItem() {  super() ;  }

	// the copy constructor
	protected TheMostAbstractItem( TheMostAbstractItem item )
	{
		if ( item.behavior == null )
			this.behavior = null ;
		else
			this.setBehaviourOf( item.behavior.getName () );
	}

	// the name of this item by which it can be distinguished from any other item
	private String uniqueName = null ;

	public String getUniqueName () {  return this.uniqueName ;  }
	public void setUniqueName ( String name ) {  this.uniqueName = name ;  }

	public boolean isNamed () {  return this.uniqueName != null ;  }

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

	// the pictures of item
	private Vector< OffscreenImage > frames = new Vector< OffscreenImage > ();

	// the pictures of item’s shadow
	private Vector< OffscreenImage > shadows = new Vector< OffscreenImage > ();

	// number of the current frame for drawing this item
	private int currentFrame = 0 ;

	/**
	 * Changes the current frame. Frames usually change when looping in the sequence of animation
	 * or when the angular orientation changes. However there’re some cases when frames are changed
	 * manually. As example, in the behavior of a spring stool the one frame is for rest
	 * and the other is for fold
	 */
	void changeFrame ( int newFrame )
	{
		if ( this.currentFrame != newFrame ) {
			this.currentFrame = newFrame ;
			// ...
		}
	}

	public String toString ()
	{
		return "item " + super.toString() ;
	}

	public String whichClassOfItem ()
	{
		String nameOfClass = getClass().getName() ;

		if ( nameOfClass.endsWith( "AvatarItem" ) )
			return "avatar item" ;
		else
		if ( nameOfClass.endsWith( "FreeItem" ) )
			return "free item" ;
		else
		if ( nameOfClass.endsWith( "GridItem" ) )
			return "grid item" ;
		else
		if ( nameOfClass.endsWith( "DescribedItem" ) )
			return "described item" ;
		else
			return "abstract item" ;
	}

}
