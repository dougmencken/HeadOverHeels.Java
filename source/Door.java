// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2024 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels ;


/**
 * A door to the room. It is really the three free items, the two jambs and the lintel
 */

public class Door ///implements Mediated
{

	private String kindOfDoor ;

	public String getKind () {  return this.kindOfDoor ;  }

	public Door( String kind )
	{
		this.kindOfDoor = kind ;
	}

}
