// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2026 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels.rooms ;

import java.util.HashMap ;


/**
 * Other rooms connected to the same room on the game’s map
 */

public class ConnectedRooms
{
	/**
	 * Connections to other rooms are stored here
	 */
	private final HashMap < String /* how */, String /* room */ > connections ;

	public HashMap< String, String > getConnections () {  return this.connections ;  }

	public ConnectedRooms()
	{
		this.connections = new HashMap< String, String > () ;
	}

	public int howMany ()
	{
		return ( this.connections != null ) ? this.connections.keySet().size() : 0 ;
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
		     if ( where.equals( "teleport"  ) ) where = "via teleport" ;
		else if ( where.equals( "teleport2" ) ) where = "via second teleport" ;

		this.connections.put( where, room );
	}

	public String toString ()
	{
		StringBuilder out = new StringBuilder( );

		boolean first = true ;
		for ( String where : this.connections.keySet() ) {
			if ( first )	first = false ;
			else		out.append( ", " );

			out.append( this.connections.get( where )
					+ ( where.equals( "above" ) || where.equals( "below" ) || where.startsWith( "via" ) ? " " : " on " )
					+ where );
		}

		return out.toString() ;
	}

}
