// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2024 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels ;


/**
 * Makes a room by constructing its various parts according to the description from file
 */

public class RoomMaker
{

	private final java.io.File roomFile ;

	public java.io.File getRoomFile () {  return this.roomFile ;  }

	private final Room roomToMake ;

	public RoomMaker( String nameOfRoomFile, Room makeMe )
	{
		this.roomFile = new java.io.File( GameMap.game_map_folder, nameOfRoomFile );
		this.roomToMake = makeMe ;

		// ....
	}

	private short tilesAlongX = -1 ;
	private short tilesAlongY = -1 ;

	public short getXSizeInTiles () {  return this.tilesAlongX ;  }
	public short getYSizeInTiles () {  return this.tilesAlongY ;  }

	private String roomScenery ;

	public String whichScenery () {  return this.roomScenery ;  }

	private String whichFloor ;

	public String whichKindOfFloor () {  return this.whichFloor ;  }

	/**
	 * Construct the room by the description from file
	 */
	public void makeRoom ()
	{
		// .....
	}

}
