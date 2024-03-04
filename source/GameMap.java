// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2024 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels ;

import java.io.File ;
import java.util.HashMap ;

import javax.xml.parsers.DocumentBuilderFactory ;
import javax.xml.parsers.DocumentBuilder ;
import javax.xml.parsers.ParserConfigurationException ;

import org.w3c.dom.Document ;
import org.w3c.dom.Element ;
import org.w3c.dom.Node ;
import org.w3c.dom.NodeList ;


public class GameMap
{

	public static final File game_map_folder = new File( FilesystemPaths.getPathToGameData(), "map" );

	private GameMap( )
	{
		this( new File( GameMap.game_map_folder, "map.xml" ) );
	}

	private GameMap( File mapFile )
	{
		readMap( mapFile );
	}

	/**
	 * Compose the game map by an XML file
	 */
	private boolean readMap ( File mapFile )
	{
		if ( this.linksBetweenRooms != null ) return true ; // already read

		if ( ! mapFile.exists() || ! mapFile.canRead() ) {
			System.out.println( "can't read file " + mapFile.getAbsolutePath() );
			return false ;
		}

		DocumentBuilder builder = null ;
		try {
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder() ;
		} catch ( ParserConfigurationException ex ) {
			return false ;
		}
		if ( builder == null ) return false ;

		Document xml = null ;
		try {
			xml = builder.parse( mapFile );
		}
		catch ( org.xml.sax.SAXException x ) {  return false ;  }
		catch ( java.io.IOException io ) {  return false ;  }
		if ( xml == null ) return false ;

		Element root = xml.getDocumentElement() ;
		if ( root == null || root.getTagName() != "map" ) return false ;

		System.out.println( "reading the game map from " + mapFile.getAbsolutePath() );

		this.linksBetweenRooms = new HashMap< String, ConnectedRooms > () ;

		NodeList roomNodes = xml.getElementsByTagName( "room" );
		int howManyRooms = roomNodes.getLength() ;
		System.out.println( "the game map consists of the " + howManyRooms + " rooms" );

		for ( int roomNth = 0; roomNth < howManyRooms; ++ roomNth )
		{
			Node roomNode = roomNodes.item( roomNth );
			if ( roomNode.getNodeType() == Node.ELEMENT_NODE ) {
				Element roomElement = (Element) roomNode ;

				String fileOfRoom = roomElement.getAttribute( "file" ) ;
				ConnectedRooms connections = new ConnectedRooms() ;

				String [] howLinked = {	"north", "east", "south", "west",
							"above", "below", "teleport", "teleport2",
							"northeast", "northwest", "southeast", "southwest",
							"eastnorth", "eastsouth", "westnorth", "westsouth" } ;

				for ( int h = 0; h < howLinked.length; ++ h ) {
					NodeList linkedRoomNodes = roomElement.getElementsByTagName( howLinked[ h ] );
					if ( linkedRoomNodes.getLength () > 0 ) {
						String linkedRoom = linkedRoomNodes.item( 0 ).getTextContent ();
						connections.setConnectedRoomAt( howLinked[ h ], linkedRoom );
					}
				}

				this.linksBetweenRooms.put( fileOfRoom, connections );
			}
		}

		return true ;
	}

	/**
	 * Which room is connected to which
	 */
	private HashMap< String, ConnectedRooms > linksBetweenRooms = null ;

	/**
	 * All the game rooms
	 */
	private HashMap< String, Room > gameRooms = new HashMap< String, Room > () ;

	/**
	 * The room being drawn
	 */
	private Room activeRoom = null ;

	public Room getActiveRoom () {  return this.activeRoom ;  }
	public void setActiveRoom ( Room newRoom ) {  this.activeRoom = newRoom ;  }

	public static void main( String [] ignored )
	{
		GameMap theMap = new GameMap( );

		// ...
	}

}
