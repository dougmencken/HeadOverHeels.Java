// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2024 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels.behaviors ;

import head.over.heels.AvatarItem ;


/**
 * Models the behavior of a character whose actions are controlled by the player
 */

public abstract class PlayerCharacter extends Behaviour
{

	public boolean update ()
	{
		// ....

		return true ;
	}

	/**
         * Updates the character’s behavior according to the player’s controls
         */
	public abstract void behave () ;

	protected PlayerCharacter( AvatarItem item, String name ) {  super( item, name );  }

}
