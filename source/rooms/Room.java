// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2025 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels.rooms ;

import head.over.heels.Drawable ;
import head.over.heels.Mediated ;

import head.over.heels.items.GridItem ;
import head.over.heels.items.FreeItem ;
import head.over.heels.items.Door ;
import head.over.heels.items.WallPiece ;
import head.over.heels.items.FloorTile ;

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

	/**
	 * The length of a single tile’s side
	 */
	public static final int single_tile_size = 16 ;

	// override in a subclass for other sizes but 16
	public short getSizeOfOneTile () {  return Room.single_tile_size ;  }

	private final String scenery ;

	/**
	 * @return one of blacktooth, jail, market, moon, byblos, egyptus, penitentiary, safari
	 */
	public String getScenery () {  return this.scenery ;  }

	// the kind o’floor which may be plain, mortal, or absent
	private final String floorKind ;

	public String getKindOfFloor () {  return this.floorKind ;  }

	public boolean hasFloor () {  return ! this.floorKind.equals( "absent" );  }

	private String roomColor = "white" ;

	public String getColour () {  return this.roomColor ;  }
	public void setColour ( String colour ) {  this.roomColor = colour ;  }

	// the connections of this room with other rooms on the map
	private ConnectedRooms connections = null ;

	public ConnectedRooms getConnections () {  return this.connections ;  }
	public void setConnections ( ConnectedRooms newLinks ) {  this.connections = newLinks ;  }

	// the free items in this room
	private Vector < FreeItem > freeItems = new Vector< FreeItem > ();

	public Vector< FreeItem > getFreeItems () {  return this.freeItems ;  }

	// the grid items
	private Vector < Vector < GridItem > > gridItems = new Vector< Vector < GridItem > > ();

	public Vector< Vector< GridItem > > getGridItems () {  return this.gridItems ;  }

	// the doors
	private Map < String, Door > doors = new java.util.HashMap< String, Door > ();

	public Door getDoorOn ( String side ) {  return this.doors.get( side ) ;  }
	public boolean hasDoorOn ( String side ) {  return getDoorOn( side ) != null ;  }

	// the pieces of wall
	private Vector < WallPiece > wallPieces = new Vector< WallPiece > ();

	// the tiles of floor
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

	public void removeFreeItemByUniqueName ( String whatName )
	{
		synchronized ( this.freeItems ) {
			FreeItem foundFreeItem = null ;

			for ( FreeItem item : this.freeItems )
				if ( item != null && item.isNamed() && item.getUniqueName().equals( whatName ) ) {
					foundFreeItem = item ;
					break ;
				}

			if ( foundFreeItem != null ) {
				System.out.println( "removing " + foundFreeItem.whichClassOfItem() + " “" + foundFreeItem.getUniqueName() + "”"
							+ " from room " + getNameOfRoomDescriptionFile() );

				this.freeItems.removeElement( foundFreeItem );

				getMediator().wantShadowFromFreeItem( foundFreeItem );
				getMediator().wantToMaskWithFreeItem( foundFreeItem );
			}
		}
	}

	public void removeGridItemByUniqueName ( String whatName )
	{
		synchronized ( this.gridItems ) {
			GridItem foundGridItem = null ;
			int inColumn = -2 ;

			for ( int column = 0 ; column < this.gridItems.size() ; ++ column )
				for ( GridItem item : this.gridItems.elementAt( column ) )
					if ( item != null && item.isNamed() && item.getUniqueName().equals( whatName ) ) {
						foundGridItem = item ;
						inColumn = column ;
						break ;
					}

			if ( foundGridItem != null && inColumn >= 0 ) {
				System.out.println( "removing " + foundGridItem.whichClassOfItem() + " “" + foundGridItem.getUniqueName() + "”"
							+ " from room " + getNameOfRoomDescriptionFile() );

				this.gridItems.elementAt( inColumn ).removeElement( foundGridItem );

				getMediator().wantShadowFromGridItem( foundGridItem );
				getMediator().wantToMaskWithGridItem( foundGridItem );
			}
		}
	}

}
