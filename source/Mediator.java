// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2024 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels ;

import java.util.Set ;


/**
 * Intermediary between the items in the room
 */

public class Mediator
{

	// the room where this mediator deals
	private final Room room ;

	public Room getRoom () {  return this.room ;  }

	// the collected collisions
	private Set < String > collisions = new java.util.HashSet< String > ();

	public boolean isThereAnyCollision () {  return ! this.collisions.isEmpty() ;  }
	public int howManyCollisions () {  return this.collisions.size() ;  }
	public void clearCollisions () {  this.collisions.clear() ;  }

	// the character yet controlled by the player
	private AvatarItem activeCharacter ;

	public AvatarItem getActiveCharacter () {  return this.activeCharacter ;  }

	public Mediator ( Room room )
	{
		this.room = room ;
	}

}
