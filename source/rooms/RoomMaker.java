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

		// the scenery that defines the room’s graphics
		String scenery = rootElement.getAttribute( "scenery" );

		short xCells = 0 ;
		short yCells = 0 ;
		String xCellsString = null ;
		String yCellsString = null ;

		// how many cells does it take up from north to south
		Node xCellsNode = rootElement.getElementsByTagName( "xTiles" ).item( 0 ); //// <-- rename "xTiles" to "cells-x" in room xmls
		if ( xCellsNode != null ) xCellsString = xCellsNode.getTextContent() ;
		try {
			if ( xCellsString != null ) xCells = Short.parseShort( xCellsString );
		} catch ( NumberFormatException e ) {}

		// how many cells does it take up from east to west
		Node yCellsNode = rootElement.getElementsByTagName( "yTiles" ).item( 0 ); //// <-- rename "yTiles" to "cells-y" in room xmls
		if ( yCellsNode != null ) yCellsString = yCellsNode.getTextContent() ;
		try {
			if ( yCellsString != null ) yCells = Short.parseShort( yCellsString );
		} catch ( NumberFormatException e ) {}

		// the kind of floor may be "plain", "mortal" or "absent"
		String whichFloor = null ;
		Node floorKindNode = rootElement.getElementsByTagName( "floorKind" ).item( 0 );
		if ( floorKindNode != null ) whichFloor = floorKindNode.getTextContent() ;

		// now the room can be constructed
		boolean isTriple = ( xCells > Room.max_single_room_size ) && ( yCells > Room.max_single_room_size );
		Room room = isTriple ? new TripleRoom( roomFile.getName(), xCells, yCells, scenery, whichFloor )
				     : new Room( roomFile.getName(), xCells, yCells, scenery, whichFloor );

		// room color as in the original Spectrum game
		Node colorNode = rootElement.getElementsByTagName( "color" ).item( 0 );
		if ( colorNode != null ) room.setColour( colorNode.getTextContent() );

		// ...

		// the floor
		RoomMaker.makeFloor( room, rootElement );

		// the walls
		Node wallsNode = rootElement.getElementsByTagName( "walls" ).item( 0 );
		if ( wallsNode != null && wallsNode.getNodeType() == Node.ELEMENT_NODE ) {
			Element wallsElement = (Element) wallsNode ;

			NodeList wallNodes = wallsElement.getElementsByTagName( "wall" );
			for ( int i = 0 ; i < wallNodes.getLength() ; ++ i ) {
				Node wallNode = wallNodes.item( i );
				if ( wallNode.getNodeType() == Node.ELEMENT_NODE ) {
					Element wallElement = (Element) wallNode ;

					WallPiece piece = RoomMaker.makeWallPiece( wallElement );
					if ( piece != null ) room.addWallSegment( piece );
				}
			}
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

	private static Door makeDoor( Element itemElement )
	{
		return null ;
	}

	private static WallPiece makeWallPiece( Element wallElement )
	{
		String xy = wallElement.getAttribute( "along" );
		if ( xy == null || xy.isEmpty() ) return null ;
		if ( ! xy.equals( "x" ) && ! xy.equals( "y" ) ) return null ;

		Node positionNode = wallElement.getElementsByTagName( "position" ).item( 0 );
		if ( positionNode == null ) return null ;

		Node pictureNode = wallElement.getElementsByTagName( "picture" ).item( 0 );
		if ( pictureNode == null ) return null ;

		String picture = pictureNode.getTextContent() ;

		try {
			int position = Integer.parseInt( positionNode.getTextContent() );

			return new WallPiece( xy.equals( "x" ), position, picture );
		}
		catch ( NumberFormatException e ) {}

		return null ;
	}

	private static void makeFloor( Room room, Element rootElement )
	{
		if ( room == null ) throw new IllegalArgumentException( "null room in RoomMaker.makeFloor" );
		if ( rootElement == null ) throw new IllegalArgumentException( "null rootElement in RoomMaker.makeFloor" );

		// read the list of floorless cells (for a triple room)
		if ( room instanceof TripleRoom ) {
			java.util.Set< IntegerPoint2D > floorlessCells = new java.util.HashSet< IntegerPoint2D >() ;

			NodeList nofloorNodes = rootElement.getElementsByTagName( "nofloor" );
			for ( int i = 0 ; i < nofloorNodes.getLength() ; ++ i ) {
				Node nofloorNode = nofloorNodes.item( i );
				if ( nofloorNode.getNodeType() == Node.ELEMENT_NODE ) {
					Element nofloorElement = (Element) nofloorNode ;

					String x = nofloorElement.getAttribute( "x" ) ;
					String y = nofloorElement.getAttribute( "y" ) ;

					try { // Integer.parseInt can throw NumberFormatException
						floorlessCells.add( new IntegerPoint2D( Integer.parseInt( x ), Integer.parseInt( y ) ) );
					} catch ( NumberFormatException e ) { }
				}
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

			Node floorNode = rootElement.getElementsByTagName( "floor" ).item( 0 );
			if ( floorNode != null && floorNode.getNodeType() == Node.ELEMENT_NODE ) {
				Element floorElement = (Element) floorNode ;

				NodeList tileNodes = floorElement.getElementsByTagName( "tile" );
				for ( int i = 0 ; i < tileNodes.getLength() ; ++ i ) {
					Node tileNode = tileNodes.item( i );
					if ( tileNode.getNodeType() == Node.ELEMENT_NODE ) {
						Element tileElement = (Element) tileNode ;

						Node xNode = tileElement.getElementsByTagName( "x" ).item( 0 );
						Node yNode = tileElement.getElementsByTagName( "y" ).item( 0 );
						Node pictureNode = tileElement.getElementsByTagName( "picture" ).item( 0 );

						if ( xNode != null && yNode != null && pictureNode != null ) {
							try { // Integer.parseInt can throw NumberFormatException
								int x = Integer.parseInt( xNode.getTextContent() );
								int y = Integer.parseInt( yNode.getTextContent() );

								NamedOffscreenImage tileImage = PoolOfPictures.getRecentPool().getPicture( pictureNode.getTextContent() );
								if ( tileImage != null )
									room.addFloorTile( new FloorTile( new IntegerPoint2D( x, y ), tileImage ) );
							}
							catch ( NumberFormatException e ) {}
						}
					}
				}
			}
		}
	}

}
