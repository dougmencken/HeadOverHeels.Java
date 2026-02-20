// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2026 Douglas Mencken dougmencken@gmail.com
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

import head.over.heels.IntegerPoint2D ;

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

	// how big is this room in cells
	private final short howManyCellsAlongX ;
	private final short howManyCellsAlongY ;

	public short getCellsAlongX () {  return this.howManyCellsAlongX ;  }
	public short getCellsAlongY () {  return this.howManyCellsAlongY ;  }

	// a room larger than this number of cells isn’t “single”
	public static final int max_single_room_size = 10 ;

	public boolean isSingleRoom () {  return getCellsAlongX() <= max_single_room_size && getCellsAlongY() <= max_single_room_size ;  }

	public boolean isTripleRoom () {  return getCellsAlongX() > max_single_room_size && getCellsAlongY() > max_single_room_size ;  }

	public boolean isDoubleRoomAlongX () {  return getCellsAlongX() > max_single_room_size && getCellsAlongY() <= max_single_room_size ;  }
	public boolean isDoubleRoomAlongY () {  return getCellsAlongX() <= max_single_room_size && getCellsAlongY() > max_single_room_size ;  }

	/**
	 * The length of a single cell’s side
	 */
	public static final int single_cell_size = 16 ;

	// override in a subclass for other sizes but 16
	public int getSizeOfOneCell () {  return Room.single_cell_size ;  }

	/**
	 * The scenery that defines the room’s graphics
	 */
	private final String scenery ;

	/**
	 * @return one of blacktooth, jail, market, moon, byblos, egyptus, penitentiary, safari
	 */
	public String getScenery () {  return this.scenery ;  }

	// the kind o’floor which may be plain, mortal, or absent
	private final String floorKind ;

	public String getKindOfFloor () {  return this.floorKind ;  }

	public boolean hasFloor () {  return ! this.floorKind.equals( "absent" );  }

	// the color of room in the original ZX Specturm game
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

	// the wall segments
	private Vector< WallPiece > wallSegments = new Vector< WallPiece > () ;

	// the tiles o’ floor
	private Map< IntegerPoint2D, FloorTile > floorTiles = new java.util.TreeMap< IntegerPoint2D, FloorTile > () ;

	/**
	 * @param nameOfRoomFile the name of file with the description of this room
	 * @param xCells the length along north–south, in cells
	 * @param yCells the length along east–west, in cells
	 * @param roomScenery the scenery such as moon or safari
	 * @param whichFloor the kind of floor
	 */
	public Room ( String nameOfRoomFile, short xCells, short yCells, String roomScenery, String whichFloor )
	{
		this.nameOfRoomDescriptionFile = nameOfRoomFile ;
		this.howManyCellsAlongX = xCells ;
		this.howManyCellsAlongY = yCells ;
		this.scenery = ( roomScenery != null ) ? roomScenery : "" ;
		this.floorKind = ( whichFloor != null ) ? whichFloor : "plain" /* ??? */ ;
	}

	public void draw ( java.awt.Graphics2D g )
	{
		/* ....... */
	}

	public void addFloorTile ( FloorTile tile )
	{
		if ( tile == null ) return ;

		tile.setMediator( getMediator() );

		///this.floorTiles.remove( tile.getCell() ); // (redundant) bin an old tile, if any
		this.floorTiles.put( tile.getCell(), tile ); // if there’s an old tile at the same cell, it is replaced
	}

	public void addWallSegment ( WallPiece piece )
	{
		if ( piece == null ) return ;

		piece.setMediator( getMediator() );
		// ....
	}

	public void addDoor ( Door door )
	{
		if ( door == null ) return ;

		door.setMediator( getMediator() );
		// ....
	}

	public void removeFreeItemByUniqueName ( String whatName )
	{
		synchronized ( this.freeItems ) {
			FreeItem foundFreeItem = null ;

			for ( FreeItem item : this.freeItems )
				if ( item != null && whatName.equals( item.getUniqueName() ) ) {
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
					if ( item != null && whatName.equals( item.getUniqueName() ) ) {
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
