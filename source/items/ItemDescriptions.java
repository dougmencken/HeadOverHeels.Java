// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2026 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels.items ;

import head.over.heels.Storage ;
import head.over.heels.StringUtilities ;
import head.over.heels.UnlikelyToHappenException ;
import head.over.heels.XElement ;

import java.util.TreeMap ;

import java.io.File ;

import javax.xml.parsers.DocumentBuilderFactory ;
import javax.xml.parsers.DocumentBuilder ;
import javax.xml.parsers.ParserConfigurationException ;

import org.w3c.dom.Document ;
import org.w3c.dom.Element ;
import org.w3c.dom.Node ;
import org.w3c.dom.NodeList ;


/**
 * All the descriptions of the game items, read from items.xml
 */

public class ItemDescriptions
{

	private static ItemDescriptions theDescriptions = null ;

	public static ItemDescriptions descriptions ()
	{
		if ( ItemDescriptions.theDescriptions == null ) new ItemDescriptions() ;
		return ItemDescriptions.theDescriptions ;
	}

	private static final boolean write_new_items_xml = false /* true */ ;

	/**
	 * Item descriptions are stored here as one-to-one mapping of an item’s kind to a description
	 */
	private TreeMap < String, DescriptionOfItem > descriptionsOfItems
			= new TreeMap < String, DescriptionOfItem > () ;

	private transient boolean alreadyRead = false ;

	private ItemDescriptions( )
	{
		if ( ItemDescriptions.theDescriptions != null )
			throw new UnlikelyToHappenException( "instantiating ItemDescriptions via the no-argument constructor a second time, why?" );

		ItemDescriptions.theDescriptions = this ;

		readDescriptions ();
	}

	// useful for comparing item descriptions from different files
	//
	public ItemDescriptions( File descriptionsFile )
	{
		readDescriptionsFromFile( descriptionsFile );
	}

	public boolean equals( Object that )
	{
		return ( that instanceof ItemDescriptions ) ? this.equals( (ItemDescriptions) that ) : false ;
	}

	/**
	 * See if both this and that describe the same items alike
	 */
	public boolean equals( ItemDescriptions that )
	{
		if ( that == null ) return false ;

		if ( this.descriptionsOfItems.size () != that.descriptionsOfItems.size () )
			return false ;

		for ( String kind : this.descriptionsOfItems.keySet() )
		{
			DescriptionOfItem thisDescription = this.descriptionsOfItems.get( kind );
			DescriptionOfItem thatDescription = that.descriptionsOfItems.get( kind );

			if ( thatDescription == null // get() returns null when the key is not in the map
				|| ! thisDescription.equals( thatDescription ) ) return false ;
		}

		return true ;
	}

	public DescriptionOfItem getDescriptionByKind ( String kind )
	{
		// auto-read the item descriptions file if it hasn’t been done before
		if ( ! this.alreadyRead )
			readDescriptions() ;

		DescriptionOfItem theDescription = this.descriptionsOfItems.get( kind ) ;

		if ( theDescription == null )
			System.out.println( "the description of the item kind " + StringUtilities.putInQuotes( kind ) + " is absent" ) ;

		return theDescription ;
	}

	public String[] getAllKindsOfItems ()
	{
		if ( this.descriptionsOfItems == null || this.descriptionsOfItems.size() == 0 ) return null ;

		java.util.Vector< String > allKinds = new java.util.Vector< String >( this.descriptionsOfItems.size() );
		for ( String kind : this.descriptionsOfItems.keySet() )
			allKinds.add( kind );

		return allKinds.toArray( new String[ allKinds.size() ] );
	}

	public static final File the_file_full_of_item_descriptions
					= new File( Storage.getPathToGameData(), "items.xml" ) ;

	public boolean readDescriptions ()
	{
		return readDescriptionsFromFile( ItemDescriptions.the_file_full_of_item_descriptions ) ;
	}

	/**
	 * Read the descriptions of items from the XML file
	 */
	public boolean readDescriptionsFromFile ( File xmlFile )
	{
		if ( this.alreadyRead ) return true ;

		if ( ! xmlFile.exists() || ! xmlFile.canRead() ) {
			System.out.println( "can’t read file " + StringUtilities.putInQuotes( xmlFile.getPath() ) );
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
			xml = builder.parse( xmlFile );
		}
		catch ( org.xml.sax.SAXException x ) {  return false ;  }
		catch ( java.io.IOException io ) {  return false ;  }
		if ( xml == null ) return false ;

		Element root = xml.getDocumentElement() ;
		if ( root == null || ! root.getTagName().equals( "items" ) ) return false ;

		java.io.PrintStream newItemsXml = null ;
		if ( ItemDescriptions.write_new_items_xml ) {
			File newItemsXmlFile = new File( Storage.getGameStorageInHome(), "new.items.xml" );
			try {
				if ( ( newItemsXmlFile.exists() || newItemsXmlFile.createNewFile() ) && newItemsXmlFile.canWrite() )
					newItemsXml = new java.io.PrintStream( newItemsXmlFile );
			} catch ( Exception xc ) {
				System.err.println( xc.getClass().getName() + ": " + xc.getMessage() );
			}
		}

		if ( ItemDescriptions.write_new_items_xml && newItemsXml != null ) {
			newItemsXml.println( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" );
			newItemsXml.println() ;
			newItemsXml.println( "<items>" );
			newItemsXml.println() ;
		}

		NodeList itemNodes = root.getElementsByTagName( "item" );
		for ( int i = 0 ; i < itemNodes.getLength() ; i ++ )
		{
			XElement itemElement = new XElement( (Element) itemNodes.item( i ) );

			final String kindOfItem = itemElement.getAttribute( "kind" ) ;
			DescriptionOfItem newDescription = new DescriptionOfItem( kindOfItem ) ;

			// spatial dimensions
			newDescription.setWidthX( itemElement.getInt( "width-x", 0 ) );
			newDescription.setWidthY( itemElement.getInt( "width-y", 0 ) );
			newDescription.setHeight( itemElement.getInt( "height", 0 ) );

			readDescriptionFurther( itemElement, newDescription );

			if ( ItemDescriptions.write_new_items_xml && newItemsXml != null ) {
				newItemsXml.println( newDescription.toString() );
				newItemsXml.println() ;
			}

			// and at last
			this.descriptionsOfItems.put( kindOfItem, newDescription );
		}

		// now make the descriptions of doors

		String[] where = { "north", "east", "south", "west" } ;
		String[] sceneries = { "jail", "blacktooth", "market", "moon",
					"byblos", "egyptus", "penitentiary", "safari" } ;

		for ( String doorScenery : sceneries ) {
			for ( String doorOn : where ) {
				DescriptionOfDoor doorDescription = new DescriptionOfDoor( doorScenery, doorOn );

				// the three parts of door
				DescriptionOfItem lintel = doorDescription.getLintel () ;
				DescriptionOfItem leftJamb = doorDescription.getLeftJamb ();
				DescriptionOfItem rightJamb = doorDescription.getRightJamb ();

				this.descriptionsOfItems.put(  leftJamb.getKind(), leftJamb );
				this.descriptionsOfItems.put( rightJamb.getKind(), rightJamb );
				this.descriptionsOfItems.put(    lintel.getKind(), lintel );
			}
		}

		if ( ItemDescriptions.write_new_items_xml && newItemsXml != null ) {
			newItemsXml.println( "</items>" );
			newItemsXml.close ();
		}

		this.alreadyRead = true ;
		return true ;
	}

	private void readDescriptionFurther( XElement element, DescriptionOfItem description )
	{
		if ( element == null || description == null ) return ;

		// how long, in milliseconds, it falls
		description.setWeight( element.getInt( "weight", 0 ) );

		// how many milliseconds this item moves one free unit
		description.setSpeed( element.getInt( "speed", 0 ) );

		// mortal or harmless
		String mortality = element.getText( "is-mortal" ) ;
		description.setMortal( mortality != null && mortality.equals( "yes" ) );

		// graphics for this item

		Element graphicsElement = element.firstElementByTag( "graphics" );
		if ( graphicsElement != null ) {
			XElement graphics = new XElement( graphicsElement );

			// the name of file with graphics for this item
			description.setNameOfFramesFile( graphics.getAttribute( "file" ) );

			// the width and height in pixels of a single frame
			description.setWidthOfFrame( graphics.getInt( "frame-width", 0 ) );
			description.setHeightOfFrame( graphics.getInt( "frame-height", 0 ) );
		}
		else {
			if ( description.getKind().startsWith( "invisible-wall" ) ) {
				description.setNameOfFramesFile( "" );
				description.setWidthOfFrame( 64 );
				description.setHeightOfFrame( 115 );
			}
		}

		// delay, in milliseconds, between frames in the animation sequence
		description.setDelayBetweenFrames( element.getInt( "delay-between-frames", 0 ) );

		// shadows for this item

		Element shadowsElement = element.firstElementByTag( "shadows" );
		if ( shadowsElement != null ) {
			XElement shadows = new XElement( shadowsElement );

			// the name of file with shadows for this item
			description.setNameOfShadowsFile( shadows.getAttribute( "file" ) );

			// the width and height in pixels of a single frame of the item’s shadow
			description.setWidthOfShadow( shadows.getInt( "width-of-shadow", 0 ) );
			description.setHeightOfShadow( shadows.getInt( "height-of-shadow", 0 ) );
		}

		// the sequence of frames for an orientation may be either simple 0,1,2,... or custom
		int frames = element.getInt( "frames", 0 );
		if ( frames > 0 )
			description.setSimpleSequenceOFrames( frames );
		else {
			NodeList frameNodes = element.elementNodesByTag( "frame" );
			int howManyFrames = frameNodes.getLength() ;
			if ( howManyFrames > 1 ) {
				int[] customSequence = new int[ howManyFrames ] ;
				try {
					for ( int i = 0 ; i < howManyFrames ; ++ i )
						customSequence[ i ] = Integer.parseInt(XElement.getTextContentOf( frameNodes.item( i ) ));

					description.setSequenceOFrames( customSequence ) ;
				}
				catch ( NumberFormatException e ) {}
			}
		}

		// ... if neither then the item is static with the default
		//     single 0th frame, and the length of { 0 } array is 1
		if ( description.howManyFramesPerOrientation() < 1 )
			throw new UnlikelyToHappenException (
				"DescriptionOfItem guarantees that frames-per-orientation ≥ 1, how did it get to zero?" );

		// how many various orientations
		description.setHowManyOrientations( (byte) element.getInt( "orientations", 0 ) );

		// how many extra frames, such as for jumping or blinking character
		description.setHowManyExtraFrames( element.getInt( "extra-frames", 0 ) );
	}

}
