// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2026 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels.rooms ;

import head.over.heels.IntegerPoint2D ;


/**
 * A triple room comprises three “single” rooms
 */

public class TripleRoom extends Room
{
	// floorless cells make this a triple room, not quadruple
	private java.util.Set< IntegerPoint2D > cellsWithoutFloor ;

	public java.util.Set< IntegerPoint2D > getCellsWithoutFloor () {  return this.cellsWithoutFloor ;  }
	public void setCellsWithoutFloor ( java.util.Set< IntegerPoint2D > floorlessCells ) {  this.cellsWithoutFloor = floorlessCells ;  }

	public TripleRoom ( String roomFile, short xCells, short yCells, String roomScenery, String whichFloor )
	{
		super( roomFile, xCells, yCells, roomScenery, whichFloor );

		if ( ! super.isTripleRoom() )
			throw new RoomIsNotTripleException( roomFile + " isn’t a triple room" ) ;
	}

	public boolean isTripleRoom () {  return true ;  }

	public boolean isSingleRoom () {  return false ;  }
	public boolean isDoubleRoomAlongX () {  return false ;  }
	public boolean isDoubleRoomAlongY () {  return false ;  }

}

class RoomIsNotTripleException extends RuntimeException
{
	public RoomIsNotTripleException( ) {  super( "room is not triple" ) ;  }
	public RoomIsNotTripleException( String message ) {  super( message ) ;  }
	public RoomIsNotTripleException( Appendable message ) {  super( message.toString() ) ;  }
}
