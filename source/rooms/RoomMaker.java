// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2026 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels.rooms ;

import head.over.heels.items.Door ;
import head.over.heels.items.GridItem ;
import head.over.heels.items.FreeItem ;
import head.over.heels.items.WallPiece ;
import head.over.heels.items.FloorTile ;

import head.over.heels.IntegerPoint2D ;
import head.over.heels.NamedOffscreenImage ;
import head.over.heels.PoolOfPictures ;

import head.over.heels.XElement ;

import javax.xml.parsers.DocumentBuilder ;

import org.w3c.dom.Document ;
import org.w3c.dom.Element ;
import org.w3c.dom.Node ;
import org.w3c.dom.NodeList ;


/**
 * Makes a room by constructing its various parts by the description from file
 */

public class RoomMaker
{

	private final java.io.File roomFile ;

	public java.io.File getRoomFile () {  return this.roomFile ;  }

	private final Room roomToMake ;

	public Room getMadeRoom () {  return this.roomToMake ;  }

	public RoomMaker( String nameOfRoomFile )
	{
		this.roomFile = new java.io.File( GameMap.game_map_folder, nameOfRoomFile );
		System.out.println( "making " + nameOfRoomFile + " (" + this.roomFile.getPath() + ")" );
		this.roomToMake = RoomMaker.makeRoom( this.roomFile ) ;
	}

	// layer of the floor
	public static final int floor_z = -1 ;

	/**
	 * Construct the room by the description from file
	 *
	 * @return the room that was made, or null if the room could not be made
	 */
	public static Room makeRoom( java.io.File roomFile )
	{
		if ( ! roomFile.exists() || ! roomFile.canRead() ) {
			System.out.println( "can’t read file " + roomFile.getAbsolutePath() );
			return null ;
		}

		DocumentBuilder xmlDocumentBuilder = null ;
		try {
			xmlDocumentBuilder = javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder() ;
		} catch ( javax.xml.parsers.ParserConfigurationException ex ) {
			return null ;
		}
		if ( xmlDocumentBuilder == null ) return null ;

		Document roomXml = null ;
		try {
			roomXml = xmlDocumentBuilder.parse( roomFile );
		}
		catch ( org.xml.sax.SAXException x ) {  return null ;  }
		catch ( java.io.IOException io ) {  return null ;  }
		if ( roomXml == null ) return null ;

		Element rootElement = roomXml.getDocumentElement() ;
		if ( rootElement == null || ! rootElement.getTagName().equals( "room" ) ) return null ;

		XElement root = new XElement( rootElement );

		// the scenery that defines the room’s graphics
		String scenery = root.getAttribute( "scenery" );

		// how many cells it takes up from north to south
		int xCells = root.getInt( "cells-x", 0 );
		// how many cells it takes up from east to west
		int yCells = root.getInt( "cells-y", 0 );

		// the kind of floor may be "plain", "mortal" or "absent"
		String whichFloor = root.getText( "floorKind" ) ;

		// now the room can be constructed
		boolean isTriple = ( xCells > Room.max_single_room_size ) && ( yCells > Room.max_single_room_size );
		Room room = isTriple ? new TripleRoom( roomFile.getName(), xCells, yCells, scenery, whichFloor )
				     : new Room( roomFile.getName(), xCells, yCells, scenery, whichFloor );

		// the room colour as in the original Spectrum game
		String roomColor = root.getText( "color" ) ;
		if ( roomColor != null ) room.setColour( roomColor );

		// the items

		NodeList itemNodes = root.firstChild( "items" ).elementNodesByTag( "item" );
		for ( int i = 0 ; i < itemNodes.getLength() ; ++ i ) {
			XElement item = new XElement( (Element) itemNodes.item( i ) );

			String itemClass = item.getText( "class" );
			if ( itemClass.equals( "door" ) ) {
				Door door = RoomMaker.makeDoor( item );

				if ( door != null )
					room.addDoor( door );
				else
					System.out.println( "oops, can’t make door " + item.getText( "kind" ) );
			}
			// ....
		}

		// the floor
		RoomMaker.makeFloor( room, root );

		// the walls
		NodeList wallNodes = root.firstChild( "walls" ).elementNodesByTag( "wall" );
		for ( int i = 0 ; i < wallNodes.getLength() ; ++ i ) {
			Element wallElement = (Element) wallNodes.item( i ) ;
			WallPiece piece = RoomMaker.makeWallPiece( new XElement( wallElement ) );
			if ( piece != null ) room.addWallSegment( piece );
		}

		// ....

		return room ;
	}

	private static FreeItem makeFreeItem( Room room, Element itemElement )
	{
		return null ;
	}

	private static GridItem makeGridItem( Room room, Element itemElement )
	{
		return null ;
	}

	private static Door makeDoor( XElement doorElement )
	{
		int cellX = 0 ;
		int cellY = 0 ;
		int cellZ = RoomMaker.floor_z ;
		try {
			cellX = Integer.parseInt( doorElement.getAttribute( "x" ) );
			cellY = Integer.parseInt( doorElement.getAttribute( "y" ) );
			cellZ = Integer.parseInt( doorElement.getAttribute( "z" ) );
		}
		catch ( NumberFormatException x ) {
			return null ;
		}

		// elevation can’t be below the floor, that’s less than floor_z = -1
		int elevation = ( cellZ > RoomMaker.floor_z ) ? cellZ * Room.layer_height : RoomMaker.floor_z ;

		// the door item’s kind is %scenery%-door-%on%
		String kind = doorElement.getText( "kind" ) ;
		int doorInKind = kind.indexOf( "door-" );
		if ( doorInKind < 0 ) return null ;

		String doorOn = kind.substring( doorInKind + 5 );

		// for a narrow room, both doors have the same kind for “non-in-wall” graphics
		// for a triple room, the location of door is more specific
		String where = doorElement.getText( "where" );
		if ( where != null && where.length() > 0 ) doorOn = where ;

		return new Door( kind, new IntegerPoint2D( cellX, cellY ), elevation, doorOn );
	}

	private static WallPiece makeWallPiece( XElement wallElement )
	{
		String xy = wallElement.getAttribute( "along" );
		if ( xy == null || xy.isEmpty() ) return null ;
		if ( ! xy.equals( "x" ) && ! xy.equals( "y" ) ) return null ;

		int position = wallElement.getInt( "position", far_far_away );
		if ( far_far_away == position ) return null ;

		String picture = wallElement.getText( "picture" ) ;
		if ( picture == null ) return null ;

		return new WallPiece( xy.equals( "x" ), position, picture );
	}

	private static void makeFloor( Room room, XElement rootElement )
	{
		if ( room == null ) throw new IllegalArgumentException( "null room in RoomMaker.makeFloor" );
		if ( rootElement == null ) throw new IllegalArgumentException( "null rootElement in RoomMaker.makeFloor" );

		// read the list of floorless cells (for a triple room)
		if ( room instanceof TripleRoom ) {
			java.util.Set< IntegerPoint2D > floorlessCells = new java.util.HashSet< IntegerPoint2D >() ;

			NodeList nofloorNodes = rootElement.elementNodesByTag( "nofloor" );
			for ( int i = 0 ; i < nofloorNodes.getLength() ; ++ i ) {
				Element nofloorElement = (Element) nofloorNodes.item( i ) ;

				String x = nofloorElement.getAttribute( "x" ) ;
				String y = nofloorElement.getAttribute( "y" ) ;

				try { // Integer.parseInt can throw NumberFormatException
					floorlessCells.add( new IntegerPoint2D( Integer.parseInt( x ), Integer.parseInt( y ) ) );
				} catch ( NumberFormatException e ) { }
			}

			if ( ! floorlessCells.isEmpty () ) {
				System.out.println( "how many floorless cells? " + floorlessCells.size() );
				( (TripleRoom) room ).setCellsWithoutFloor( floorlessCells );
			}
                }

		if ( ! room.getScenery().isEmpty() ) {
			// make the floor by its kind (plain or mortal) and the room’s scenery, without listing every tile

			///Door eastDoor = room.getDoorOn( "east" );
			///Door southDoor = room.getDoorOn( "south" );
			///Door northDoor = room.getDoorOn( "north" );
			///Door westDoor = room.getDoorOn( "west" );

			///Door eastnorthDoor = room.getDoorOn( "eastnorth" );
			///Door eastsouthDoor = room.getDoorOn( "eastsouth" );
			///Door southeastDoor = room.getDoorOn( "southeast" );
			///Door southwestDoor = room.getDoorOn( "southwest" );
			///Door northeastDoor = room.getDoorOn( "northeast" );
			///Door northwestDoor = room.getDoorOn( "northwest" );
			///Door westnorthDoor = room.getDoorOn( "westnorth" );
			///Door westsouthDoor = room.getDoorOn( "westsouth" );

			String tileFilename = room.getScenery() + '-' ;

			if ( room.hasFloor() )
				tileFilename += room.isFloorMortal() ? "mortalfloor" : "floor" ;
			else
				tileFilename += "nofloor" ;

			tileFilename += ".png" ;

			int lastCellX = room.getCellsAlongX() - 1 ;
			int lastCellY = room.getCellsAlongY() - 1 ;

			for ( int x = 0 ; x <= lastCellX ; ++ x ) {
				for ( int y = 0 ; y <= lastCellY ; ++ y )
				{
					IntegerPoint2D cell = new IntegerPoint2D( x, y );

					boolean addTile = true ;

					if ( room instanceof TripleRoom )
						if ( ( (TripleRoom) room ).getCellsWithoutFloor().contains( cell ) )
							addTile = false ;

					if ( addTile ) {
						NamedOffscreenImage tileImage = PoolOfPictures.getRecentPool().getPicture( tileFilename );
						if ( tileImage != null )
							room.addFloorTile( new FloorTile( cell, tileImage ) );
						else
							System.out.println( "can’t get image " + tileFilename );
					}
				}
			}
		}
		else {
			// for each floor tile its position (x,y) and image file name are listed

			XElement floorElement = rootElement.firstChild( "floor" ) ;
			if ( floorElement.exists() ) {
				NodeList tileNodes = floorElement.elementNodesByTag( "tile" );
				for ( int i = 0 ; i < tileNodes.getLength() ; ++ i ) {
					XElement tileElement = new XElement( (Element) tileNodes.item( i ) );

					int x = tileElement.getInt( "x", far_far_away );
					int y = tileElement.getInt( "y", far_far_away );
					String picture = tileElement.getText( "picture" );

					if ( x != far_far_away && y != far_far_away && picture != null ) {
						NamedOffscreenImage tileImage = PoolOfPictures.getRecentPool().getPicture( picture );
						if ( tileImage != null )
							room.addFloorTile( new FloorTile( new IntegerPoint2D( x, y ), tileImage ) );
					}
				}
			}
		}
	}

	private static final int far_far_away = 1 << 21 ;

}
