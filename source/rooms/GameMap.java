// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2025 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels.rooms ;

import head.over.heels.Storage ;

import java.io.File ;

import java.util.HashMap ;
import java.util.TreeSet ;

import javax.xml.parsers.DocumentBuilderFactory ;
import javax.xml.parsers.DocumentBuilder ;
import javax.xml.parsers.ParserConfigurationException ;

import org.w3c.dom.Document ;
import org.w3c.dom.Element ;
import org.w3c.dom.Node ;
import org.w3c.dom.NodeList ;


public class GameMap
{

	public static final File game_map_folder = new File( Storage.getPathToGameData(), "map" );

	private GameMap( )
	{
		this( new File( GameMap.game_map_folder, "map.xml" ) );
	}

	private GameMap( File mapFile )
	{
		readMap( mapFile );
	}

	/**
	 * Read the game map from an XML file
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
		if ( root == null || ! root.getTagName().equals( "map" ) ) return false ;

		this.linksBetweenRooms = new HashMap< String, ConnectedRooms > () ;

		NodeList roomNodes = xml.getElementsByTagName( "room" );
		int howManyRooms = roomNodes.getLength() ;

		System.out.print( "reading the game map" );
		System.out.print( " consisting of " + howManyRooms + " rooms" );
		System.out.println( " from " + mapFile.getAbsolutePath() );

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

	private boolean checkCoherence () {  return checkCoherence( null ) ;  }

	/**
	 * When there’s room B below some room A, then for coherence
	 * room A needs to be above B as well. The same for a room
	 * on the east | north | west | south of some room C : that room
	 * accordingly has C on the west | south | east | north
	 */
	private boolean checkCoherence ( java.io.PrintStream out )
	{
		if ( out != null ) out.println( "checking coherence of the game map" );
		String indent = "  " ;

		TreeSet< TwoJoiningRooms > joiningRooms = new TreeSet< TwoJoiningRooms >( ) ;

		for ( String room : this.linksBetweenRooms.keySet() ) {
			ConnectedRooms connectedTo = this.linksBetweenRooms.get( room );
			if ( connectedTo == null ) continue ;

			HashMap< String /* how */, String /* room */ > connections = connectedTo.getConnections() ;
			for ( String howJoined : connections.keySet() )
				joiningRooms.add( new TwoJoiningRooms( room, connections.get( howJoined ), howJoined ) );
		}

		MutuallyJoinedRooms joined = null ;
		do {
			joined = null ;

			for ( TwoJoiningRooms link : joiningRooms ) {
				java.util.SortedSet< TwoJoiningRooms > tailOfLinks = joiningRooms.tailSet( link, /* not including link */ false );
				for ( TwoJoiningRooms otherLink : tailOfLinks ) {
					if ( link.isReciprocalWith( otherLink ) ) {
						joined = new MutuallyJoinedRooms( link, otherLink );
						break ;
					}
				}

				if ( joined != null ) break ;
			}

			if ( joined != null )
			{
				if ( out != null ) {
					out.print( indent + joined.getFirst().getFirstRoom() + " <-- " ) ;
					out.print( joined.getSecond().getHowJoined() + " & " + joined.getFirst().getHowJoined() );
					out.print( " --> " + joined.getSecond().getFirstRoom() );
					out.println() ;
				}

				joiningRooms.remove( joined.getFirst() );
				joiningRooms.remove( joined.getSecond() );
			}
		} while ( joined != null /* there’s some mutually joined pair of rooms */ ) ;

		int incoherencies = joiningRooms.size() ;
		if ( out != null ) out.println( "there are " + incoherencies + " map incoherencies" );

		if ( incoherencies > 0 ) {
			if ( out != null ) out.println( "incoherencies are" );

			for ( TwoJoiningRooms link : joiningRooms ) {
				if ( out != null ) out.println( indent + link );
			}

			return false ;
		}

		return true ;
	}

	public String toString ()
	{
		if ( this.linksBetweenRooms == null ) return "the game map hasn’t been read yet" ;

		StringBuilder out = new StringBuilder( );
		String newline = System.getProperty( "line.separator" );

		out.append( "the game map" );

		int howManyRooms = this.linksBetweenRooms.keySet().size() ;
		out.append( " consists of " ).append( howManyRooms ).append( " rooms" );
		out.append( newline );

		TreeSet< String > sortedKeys = new TreeSet< String >( this.linksBetweenRooms.keySet() );
		for ( String room : sortedKeys ) {
			out.append( "  " ).append( "room " + room + " has " ) ;

			ConnectedRooms connectedTo = this.linksBetweenRooms.get( room );
			int howMany = ( connectedTo != null ) ? connectedTo.howMany() : 0 ;
			if ( howMany > 0 ) {
				out.append( howMany + ( howMany == 1 ? " connection" : " connections" ) );
				out.append(" ~ ") ;
				out.append( connectedTo.toString() );
			} else
				out.append( "*no* connections" );

			out.append( newline );
		}

		return out.toString() ;
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

	public static void main ( String [] ignored )
	{
		GameMap theMap = new GameMap( );
		System.out.println( theMap.toString() );
		theMap.checkCoherence( System.out ) ;
	}

}
