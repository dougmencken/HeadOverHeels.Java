// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2024 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels.items ;


public abstract class DescribedItem extends TheMostAbstractItem
{

	// creates an item by description
	protected DescribedItem( DescriptionOfItem description )
	{
		if ( description == null ) throw new NullPointerException( "null description at the time of item construction" ) ;

		this.descriptionOfItem = description ;
	}

	// the copy constructor
	protected DescribedItem( DescribedItem item )
	{
		super( item );

		this.descriptionOfItem = item.descriptionOfItem ;
	}

	private DescriptionOfItem descriptionOfItem ;

	public DescriptionOfItem getDescriptionOfItem () {  return this.descriptionOfItem ;  }

	/**
	 * The three spatial dimensions (widths) of the item, along the x, along the y, and height along the z
	 */
	public int getWidthX () {  return this.descriptionOfItem.getWidthX() ;  }
	public int getWidthY () {  return this.descriptionOfItem.getWidthY() ;  }
	public int getHeight () {  return this.descriptionOfItem.getHeight() ;  }

	public String getKind () {  return this.descriptionOfItem.getKind() ;  }

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

	/**
	 * The sequence in which frames for various orientations are presented in a graphics file
	 */
	static class Frames
	{

		static final int South = 0 ;
		static final int  West = 1 ;
		static final int North = 2 ;
		static final int  East = 3 ;

	}

	// the position in 3-dimensional space of this item’s lower north-west point, in free units
	public abstract int getX () ;
	public abstract int getY () ;
	public abstract int getZ () ;

	public boolean overlapsWith ( DescribedItem anotherItem )
	{
		return ( this.getX() < anotherItem.getX() + anotherItem.getWidthX() )
					&& ( anotherItem.getX() < this.getX() + this.getWidthX() )
			&& ( this.getY() > anotherItem.getY() - anotherItem.getWidthY() )
					&& ( anotherItem.getY() > this.getY() - this.getWidthY() )
			&& ( this.getZ() < anotherItem.getZ() + anotherItem.getHeight() )
					&& ( anotherItem.getZ() < this.getZ() + this.getHeight() ) ;
	}

	// whether to ignore that this item collides with something
	private boolean ignoreCollisions = false ;

	public void setIgnoreCollisions ( boolean ignore ) {  this.ignoreCollisions = ignore ;  }

	public boolean isIgnoringCollisions () {  return this.ignoreCollisions ;  }
	public boolean isNotIgnoringCollisions () {  return ! this.ignoreCollisions ;  }

}
