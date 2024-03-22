// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2024 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels ;

import java.util.Vector ;
import java.util.Map ;


/**
 * A game room
 */

public class Room extends Mediated implements Drawable
{
	// in which file is this room described
	private final String nameOfRoomDescriptionFile ;

	public String getNameOfRoomDescriptionFile () {  return this.nameOfRoomDescriptionFile ;  }

	// how big is this room in tiles
	private final short howManyTilesOnX ;
	private final short howManyTilesOnY ;

	public short getTilesOnX () {  return this.howManyTilesOnX ;  }
	public short getTilesOnY () {  return this.howManyTilesOnY ;  }

	private final String scenery ;

	/**
	 * @return one of blacktooth, jail, market, moon, byblos, egyptus, penitentiary, safari
	 */
	public String getScenery () {  return this.scenery ;  }

	// the kind o’floor which may be plain, mortal, or absent
	private final String floorKind ;

	public String getKindOfFloor () {  return this.floorKind ;  }

        public boolean hasFloor () {  return ! this.floorKind.equals( "absent" );  }

	// the connections of this room with other rooms on the map
	private ConnectedRooms connections = null ;

	public ConnectedRooms getConnections () {  return this.connections ;  }
	public void setConnections ( ConnectedRooms newLinks ) {  this.connections = newLinks ;  }

	// the free items in this room
	private Vector < FreeItem > freeItems = new Vector< FreeItem > ();

	// the grid items
	private Vector < Vector < GridItem > > gridItems = new Vector< Vector < GridItem > > ();

	// the doors
	private Map < String, Door > doors = new java.util.HashMap< String, Door > ();

	public Door getDoorOn ( String side ) {  return this.doors.get( side ) ;  }
	public boolean hasDoorOn ( String side ) {  return getDoorOn( side ) != null ;  }

	private Vector < WallPiece > wallPieces = new Vector< WallPiece > ();

	private Vector < FloorTile > floorTiles = new Vector< FloorTile > ();

	/**
	 * @param nameOfRoomFile the name of file with the description of this room
	 * @param xTiles the length along X, in tiles
	 * @param yTiles the length along Y, in tiles
	 * @param roomScenery the scenery such as moon or safari
	 * @param whichFloor the kind of floor
	 */
	public Room ( String nameOfRoomFile, short xTiles, short yTiles, String roomScenery, String whichFloor )
	{
		this.nameOfRoomDescriptionFile = nameOfRoomFile ;
		this.howManyTilesOnX = xTiles ;
		this.howManyTilesOnY = yTiles ;
		this.scenery = roomScenery ;
		this.floorKind = whichFloor ;
	}

	public Room ( String nameOfRoomFile )
	{
		RoomMaker maker = new RoomMaker( nameOfRoomFile, this );

		this.nameOfRoomDescriptionFile = maker.getRoomFile().getName() ;
		this.howManyTilesOnX = maker.getXSizeInTiles() ;
		this.howManyTilesOnY = maker.getYSizeInTiles ();
		this.scenery = maker.whichScenery ();
		this.floorKind = maker.whichKindOfFloor() ;

		maker.makeRoom () ;
	}

	public void draw ( java.awt.Graphics2D g )
	{
		/* ....... */
	}

}
