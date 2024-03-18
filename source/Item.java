// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2024 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels ;

import head.over.heels.behaviors.Behaviour ;

import java.util.Vector ;


public abstract class Item extends ShadyMediated
{

	// creates an item by a description
	public Item( DescriptionOfItem description )
	{
		if ( description == null ) throw new NullPointerException( "null description at the time of item construction" ) ;

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

	 // the pictures of item
        Vector< OffscreenImage > frames = new Vector< OffscreenImage > ();

        // the pictures of item’s shadow
        Vector< OffscreenImage > shadows = new Vector< OffscreenImage > ();

	// number of the current frame for drawing this item
	private int currentFrame = 0 ;

	/**
	 * Changes the current frame. Frames usually change when the angular orientation changes
	 * or when looping in a sequence of animation. However there’re some cases when frames
	 * are changed manually. As example, in the behavior of a spring stool the one frame
	 * is for rest and the other is for fold
	 */
	void changeFrame ( int newFrame )
	{
		if ( this.currentFrame != newFrame ) {
			this.currentFrame = newFrame ;
			// ...
		}
	}

	protected int firstFrameWhenHeading ( String where )
	{
		if ( this.descriptionOfItem.howManyOrientations() > 1 ) {
			int orientOccident = 0 ;
			     if ( where.equals( "south" ) ) orientOccident = Frames.South ;
			else if ( where.equals( "west" ) )  orientOccident = Frames.West ;
			else
			 if ( this.descriptionOfItem.howManyOrientations() > 2 ) {
				     if ( where.equals( "east" ) )  orientOccident = Frames.East ;
				else if ( where.equals( "north" ) ) orientOccident = Frames.North ;
			}

			return this.descriptionOfItem.howManyFramesPerOrientation() * orientOccident ;
		}

		return 0 ;
	}

	public String toString ()
	{
		return "item " + super.toString() ;
	}

	static class Frames
	{
		static final int South = 0 ;
		static final int  West = 1 ;
		static final int North = 2 ;
		static final int  East = 3 ;
	}

}
