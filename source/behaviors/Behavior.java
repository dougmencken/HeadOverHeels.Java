// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2023 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels.behaviors ;

/**
 * Abstraction for the item's behavior. A game's item changes its activity in each cycle of update.
 * Different kinds of behavior define different ways of transition between these activities
 */

public abstract class Behavior
{

	private String nameOfBehavior = "abstract behavior" ;

	public String getNameOfBehavior () {  return nameOfBehavior ;  }

	/**
	 * Updates the item's behavior in each cycle
	 * @return true if the item is still alive after this update or false otherwise
	 */
        abstract boolean update () ;

}
