// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2025 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels.rooms ;

import head.over.heels.items.AvatarItem ;
import head.over.heels.items.FreeItem ;
import head.over.heels.items.GridItem ;
import head.over.heels.items.DescribedItem ;

import java.util.Set ;
import java.util.Vector ;


/**
 * Intermediary between items in a room
 */

public class Mediator
{

	// the room where this mediator deals
	private final Room mediatedRoom ;

	public Room getRoom () {  return this.mediatedRoom ;  }

	// the character yet controlled by the player
	private AvatarItem activeCharacter = null ;

	public AvatarItem getActiveCharacter () {  return this.activeCharacter ;  }

	public Mediator ( Room room )
	{
		this.mediatedRoom = room ;
	}

	/**
	 * Look for an item in the room by its unique name
	 */
	public DescribedItem findItemByUniqueName( String whatName )
	{
		// first look for a free item
		Vector< FreeItem > allFreeItems = this.mediatedRoom.getFreeItems ();
		for ( FreeItem item : allFreeItems )
			if ( item != null && item.isNamed() && item.getUniqueName().equals( whatName ) )
				return item ;

		// then for a grid item
		Vector< Vector< GridItem > > allGridItems = this.mediatedRoom.getGridItems ();
		for ( int column = 0 ; column < allGridItems.size() ; ++ column )
			for ( GridItem item : allGridItems.elementAt( column ) )
				if ( item != null && item.isNamed() && item.getUniqueName().equals( whatName ) )
					return item ;

		return null ; // not found
	}

	/**
	 * Look for an item in the room by its kind.
	 * When there are several items of this kind, the first found one is returned
	 */
	public DescribedItem findItemOfKind ( String whichKind )
	{
		// first among free items
		Vector< FreeItem > allFreeItems = this.mediatedRoom.getFreeItems ();
		for ( FreeItem item : allFreeItems )
			if ( item != null && item.getKind().equals( whichKind ) )
				return item ;

		// then among grid items
		Vector< Vector< GridItem > > allGridItems = this.mediatedRoom.getGridItems ();
		for ( int column = 0 ; column < allGridItems.size() ; ++ column )
			for ( GridItem item : allGridItems.elementAt( column ) )
				if ( item != null && item.getKind().equals( whichKind ) )
					return item ;

		return null ; // not found
	}

	/**
	 * Look for an item in the room with that behavior
	 */
	public DescribedItem findItemBehavingAs ( String thatBehaviour )
	{
		// first in free items
		Vector< FreeItem > allFreeItems = this.mediatedRoom.getFreeItems ();
		for ( FreeItem item : allFreeItems )
			if ( item != null && item.getBehaviour() != null && item.getBehaviour().getName().equals( thatBehaviour ) )
				return item ;

		// then in grid items
		Vector< Vector< GridItem > > allGridItems = this.mediatedRoom.getGridItems ();
		for ( int column = 0 ; column < allGridItems.size() ; ++ column )
			for ( GridItem item : allGridItems.elementAt( column ) )
				if ( item != null && item.getBehaviour() != null && item.getBehaviour().getName().equals( thatBehaviour ) )
					return item ;

		return null ; // not found
	}

	private boolean needToSortGridItems = false ;
	private boolean needToSortFreeItems = false ;

	public void wantToMaskWithFreeItem( FreeItem item )
	{
		// .....
	}

	public void wantToMaskWithGridItem( GridItem item )
	{
		// ....
	}

	public void wantShadowFromGridItem( GridItem item )
	{
		// .....
	}

	public void wantShadowFromFreeItem( FreeItem item )
	{
		// ....
	}

	// the collected collisions
	private Set < String > collisions = new java.util.HashSet< String > () ;

	public boolean isThereAnyCollision () {  return ! this.collisions.isEmpty() ;  }
	public int howManyCollisions () {  return this.collisions.size() ;  }
	public void clearCollisions () {  this.collisions.clear() ;  }

	public boolean collectCollisionsWith ( String uniqueNameOfItem )
	{
		this.collisions.clear () ;

		DescribedItem thatItem = findItemByUniqueName( uniqueNameOfItem ) ;
		if ( thatItem == null ) return false ;
		if ( thatItem.isIgnoringCollisions() ) return false ;

		// look for collisions with free items
		Vector< FreeItem > allFreeItems = this.mediatedRoom.getFreeItems ();

		for ( FreeItem freeItem : allFreeItems )
			if ( freeItem != null && freeItem.isNotIgnoringCollisions()
					&& freeItem.isNamed() && freeItem.getUniqueName() != uniqueNameOfItem )
				if ( freeItem.overlapsWith( thatItem ) )
					collisions.add( freeItem.getUniqueName() );

		// look for collisions with grid items
		// ....

		return this.collisions.size() > 0 ;
	}

}
