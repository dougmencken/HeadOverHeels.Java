// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2024 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels ;

import java.util.HashMap ;


/**
 * Other rooms connected to the same room on the game’s map
 */

public class ConnectedRooms
{
	/**
	 * Connections to other rooms are stored here
	 */
	private HashMap < String /* where */, String /* file of room */ > connections ;

	public ConnectedRooms()
	{
		this.connections = new HashMap < String, String > () ;
	}

	/**
	 * Get the room connected to this one at
	 */
	public String getConnectedRoomAt ( String where )
	{
		String found = this.connections.get( where );
		return ( found != null ) ? found : "" ;
	}

	public void setConnectedRoomAt ( String where, String room )
	{
		     if ( where ==  "teleport" ) where = "via teleport" ;
		else if ( where == "teleport2" ) where = "via second teleport" ;

		this.connections.put( where, room );
	}

}
