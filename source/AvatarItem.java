// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2024 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels ;

import head.over.heels.behaviors.Behaviour ;
import head.over.heels.behaviors.PlayerCharacter ;


/**
 * An avatar is a game character controlled by the player
 */

public class AvatarItem extends FreeItem
{

	public AvatarItem( DescriptionOfItem description, int x, int y, int z, String heading )
	{
		super( description, x, y, z, heading );
	}

	// the copy constructor
	public AvatarItem( AvatarItem that )
	{
		super( that );
	}

	/**
	 * Updates the character’s behavior according to the player’s controls
	 */
	public void behaveCharacter ()
	{
		Behaviour behavior = getBehaviour() ;
		if ( behavior != null && behavior instanceof PlayerCharacter )
			( (PlayerCharacter) behavior ).behave ();
	}

}
