// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2025 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels.items ;

import head.over.heels.Shady ;
import head.over.heels.NamedOffscreenImage ;
import head.over.heels.NoSuchPictureException ;

import java.util.Map ;
import java.util.Vector ;


public abstract class DescribedItem extends TheMostAbstractItem implements Shady
{

	// creates an item by description
	protected DescribedItem( DescriptionOfItem description )
	{
		if ( description == null ) throw new NullPointerException( "null description at the time of item construction" ) ;

		this.descriptionOfItem = description ;
		this.originalKind = description.getKind() ;
	}

	// the copy constructor
	protected DescribedItem( DescribedItem thatItem )
	{
		super( thatItem );

		this.descriptionOfItem = thatItem.getDescriptionOfItem() ;
		this.originalKind = thatItem.getOriginalKind() ;
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

	/**
	 * The original kind of item, while the current kind may change via metamorphosis
	 */
	private String originalKind ;

	public String getOriginalKind () {  return this.originalKind ;  }

	// the sequences of pictures of item’s shadow
	private Map< String, Vector< NamedOffscreenImage > > shadows = null ;

	public boolean hasShadow () {  return this.shadows != null && ! this.shadows.isEmpty() ;  }

	protected NamedOffscreenImage getNthShadowIn ( String sequence, int n ) throws NoSuchPictureException
	{
		throw new NoSuchPictureException() ;
	}

	// the position in 3-dimensional space of this item’s lower north-west point, in free units
	public abstract int getX () ;
	public abstract int getY () ;
	public abstract int getZ () ;

	public abstract void setX ( int newX ) ;
	public abstract void setY ( int newY ) ;
	public abstract void setZ ( int newZ ) ;

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

	// redo the shadow or not
	private boolean wantShadow = false ;

        // implementing Shady
	public boolean getWantShadow () {  return this.wantShadow ;  }
	public void setWantShadow ( boolean wanna ) {  this.wantShadow = wanna ;  }

}
