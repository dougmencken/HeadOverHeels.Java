// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2025 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels.items ;

import head.over.heels.Storage ;
import head.over.heels.StringUtilities ;
import head.over.heels.UnlikelyToHappenException ;

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

	private static final boolean write_new_items_xml = true ;
	private static final boolean parse_previous_format = true ;

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
		if ( root == null || root.getTagName() != "items" ) return false ;

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

		NodeList itemNodes = xml.getElementsByTagName( "item" );
		for ( int i = 0 ; i < itemNodes.getLength() ; i ++ )
		{
			Node itemNode = itemNodes.item( i );
			if ( itemNode.getNodeType() == Node.ELEMENT_NODE ) {
				Element itemElement = (Element) itemNode ;

				final String kindOfItem = itemElement.getAttribute( "kind" ) ; // the kind of item
				DescriptionOfItem newDescription = new DescriptionOfItem ( kindOfItem );

				// spatial dimensions
				int xWidth = 0 ;
				int yWidth = 0 ;
				int height = 0 ;

				Node xWidthNode = itemElement.getElementsByTagName( "width-x" ).item( 0 );
				Node yWidthNode = itemElement.getElementsByTagName( "width-y" ).item( 0 );
				Node heightNode = itemElement.getElementsByTagName( "height" ).item( 0 );

				if ( xWidthNode == null && ItemDescriptions.parse_previous_format )
					xWidthNode = itemElement.getElementsByTagName( "widthX" ).item( 0 );
				if ( yWidthNode == null && ItemDescriptions.parse_previous_format )
					yWidthNode = itemElement.getElementsByTagName( "widthY" ).item( 0 );

				if ( xWidthNode != null ) {
					try { // parseInt can throw NumberFormatException
						xWidth = Integer.parseInt( xWidthNode.getTextContent () );
					} catch ( NumberFormatException e ) { }
				}
				if ( yWidthNode != null ) {
					try { // parseInt can throw NumberFormatException
						yWidth = Integer.parseInt( yWidthNode.getTextContent () );
					} catch ( NumberFormatException e ) { }
				}
				if ( heightNode != null ) {
					try { // parseInt can throw NumberFormatException
						height = Integer.parseInt( heightNode.getTextContent () );
					} catch ( NumberFormatException e ) { }
				}

				newDescription.setWidthX( xWidth );
				newDescription.setWidthY( yWidth );
				newDescription.setHeight( height );

				readDescriptionFurther( itemElement, newDescription );

				if ( ItemDescriptions.write_new_items_xml && newItemsXml != null ) {
					newItemsXml.println( newDescription.toString() );
					newItemsXml.println() ;
				}

				// and at last
				this.descriptionsOfItems.put( kindOfItem, newDescription );
			}
		}

		// now make the descriptions of doors

		String[] where = { "north", "east", "south", "west" } ;
		String[] sceneries = { "jail", "blacktooth", "market", "moon",
					"byblos", "egyptus", "penitentiary", "safari" } ;

		for ( String doorScenery : sceneries ) {
			for ( String doorOn : where ) {
				DescriptionOfDoor doorDescription = new DescriptionOfDoor( doorScenery, doorOn );

				/**** if ( ItemDescriptions.write_new_items_xml && newItemsXml != null ) {
					newItemsXml.println( doorDescription.toString() );
					newItemsXml.println() ;
				} ****/

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

	private void readDescriptionFurther( Element element, DescriptionOfItem description )
	{
		if ( element == null || description == null ) return ;

		// how long, in milliseconds, it falls
		int itemWeight = 0 ;

		NodeList weightNodes = element.getElementsByTagName( "weight" );
		if ( weightNodes.getLength () > 0 ) {
			String weight = weightNodes.item( 0 ).getTextContent ();
			try { // parseInt can throw NumberFormatException
				itemWeight = Integer.parseInt( weight );
			} catch ( NumberFormatException e ) { }
		}

		description.setWeight( itemWeight );

		// how many milliseconds this item moves one free unit
		int itemSpeed = 0 ;

		NodeList speedNodes = element.getElementsByTagName( "speed" );
		if ( speedNodes.getLength () > 0 ) {
			String speed = speedNodes.item( 0 ).getTextContent ();
			try { // parseInt can throw NumberFormatException
				itemSpeed = Integer.parseInt( speed );
			} catch ( NumberFormatException e ) { }
		}

		description.setSpeed( itemSpeed );

		// mortal or harmless
		boolean isMortal = false ;

		NodeList mortalNodes = element.getElementsByTagName( "mortal" );
		if ( mortalNodes.getLength () > 0 ) {
			String mortal = mortalNodes.item( 0 ).getTextContent ();
			if ( mortal.equals( "yes" ) )
				isMortal = true ;
		}

		description.setMortal( isMortal );

		// graphics for this item

		NodeList graphicsNodes = element.getElementsByTagName( "graphics" );
		if ( graphicsNodes.getLength() == 0 && ItemDescriptions.parse_previous_format ) {
			NodeList pictureNodes = element.getElementsByTagName( "picture" );
			if ( pictureNodes.getLength() > 0 ) graphicsNodes = pictureNodes ;
		}
		if ( graphicsNodes.getLength () > 0
				&& graphicsNodes.item( 0 ).getNodeType() == Node.ELEMENT_NODE )
		{
			Element graphics = (Element) graphicsNodes.item( 0 ) ;

			// the name of file with graphics for this item
			description.setNameOfFramesFile( graphics.getAttribute( "file" ) );

			// the width and height in pixels of a single frame
			Node widthNode = graphics.getElementsByTagName( "width-of-frame" ).item( 0 );
			if ( widthNode == null && ItemDescriptions.parse_previous_format )
				widthNode = graphics.getElementsByTagName( "width" ).item( 0 );
			if ( widthNode != null ) {
				try { // parseInt can throw NumberFormatException
					description.setWidthOfFrame( Integer.parseInt( widthNode.getTextContent () ) );
				} catch ( NumberFormatException e ) { }
			}
			Node heightNode = graphics.getElementsByTagName( "height-of-frame" ).item( 0 );
			if ( heightNode == null && ItemDescriptions.parse_previous_format )
				heightNode = graphics.getElementsByTagName( "height" ).item( 0 );
			if ( heightNode != null ) {
				try { // parseInt can throw NumberFormatException
					description.setHeightOfFrame( Integer.parseInt( heightNode.getTextContent () ) );
				} catch ( NumberFormatException e ) { }
			}
		} else
		{
			if ( description.getKind().startsWith( "invisible-wall" ) )
			{
				description.setNameOfFramesFile( "" );
				description.setWidthOfFrame( 64 );
				description.setHeightOfFrame( 115 );
			}
		}

		// delay, in milliseconds, between frames in the animation sequence
		int delayBetweenFrames = 0 ;

		NodeList delayBetweenFramesNodes = element.getElementsByTagName( "delay-between-frames" );
		if ( delayBetweenFramesNodes.getLength() == 0 && ItemDescriptions.parse_previous_format ) {
			NodeList betweenFramesNodes = element.getElementsByTagName( "betweenFrames" );
			if ( betweenFramesNodes.getLength() > 0 ) delayBetweenFramesNodes = betweenFramesNodes ;
		}
		if ( delayBetweenFramesNodes.getLength () > 0 ) {
			String delayInMilliseconds = delayBetweenFramesNodes.item( 0 ).getTextContent ();
			try { // parseInt can throw NumberFormatException
				delayBetweenFrames = Integer.parseInt( delayInMilliseconds );
			} catch ( NumberFormatException e ) { }
		}

		description.setDelayBetweenFrames( delayBetweenFrames );

		// shadows for this item

		NodeList shadowsNodes = element.getElementsByTagName( "shadows" );
		if ( shadowsNodes.getLength() == 0 && ItemDescriptions.parse_previous_format ) {
			NodeList shadowNodes_oldformat = element.getElementsByTagName( "shadow" );
			if ( shadowNodes_oldformat.getLength() > 0 ) shadowsNodes = shadowNodes_oldformat ;
		}
		if ( shadowsNodes.getLength () > 0
				&& shadowsNodes.item( 0 ).getNodeType() == Node.ELEMENT_NODE )
		{
			Element shadows = (Element) shadowsNodes.item( 0 ) ;

			// the name of file with shadows for this item
			description.setNameOfShadowsFile( shadows.getAttribute( "file" ) );

			// the width and height in pixels of a single frame of the item’s shadow
			Node widthNode = shadows.getElementsByTagName( "width-of-shadow" ).item( 0 );
			if ( widthNode == null && ItemDescriptions.parse_previous_format )
				widthNode = shadows.getElementsByTagName( "width" ).item( 0 );
			if ( widthNode != null ) {
				try { // parseInt can throw NumberFormatException
					description.setWidthOfShadow( Integer.parseInt( widthNode.getTextContent () ) );
				} catch ( NumberFormatException e ) { }
			}
			Node heightNode = shadows.getElementsByTagName( "height-of-shadow" ).item( 0 );
			if ( heightNode == null && ItemDescriptions.parse_previous_format )
				heightNode = shadows.getElementsByTagName( "height" ).item( 0 );
			if ( heightNode != null ) {
				try { // parseInt can throw NumberFormatException
					description.setHeightOfShadow( Integer.parseInt( heightNode.getTextContent () ) );
				} catch ( NumberFormatException e ) { }
			}
		}

		// the sequence of frames for an orientation may be either simple 0,1,2,... or custom
		Node framesNode = element.getElementsByTagName( "frames" ).item( 0 );
		if ( framesNode != null )
		{
			try { // parseInt can throw NumberFormatException
				description.makeSequenceOFrames( Integer.parseInt( framesNode.getTextContent () ) ) ;
			} catch ( NumberFormatException e ) { }
		} else
		{
			NodeList frameNodes = element.getElementsByTagName( "frame" );
			try {
				// the custom sequence
				java.util.Vector< Integer > customSequence = new java.util.Vector< Integer >() ;

				for ( int i = 0 ; i < frameNodes.getLength() ; i ++ ) {
					Node frameNode = frameNodes.item( i );
					customSequence.add( Integer.parseInt( frameNode.getTextContent () ) ) ;
				}
				if ( customSequence.size() > 0 )
					description.setSequenceOFrames( customSequence ) ;
			}
			catch ( NumberFormatException e ) { }
		}

		// ... if neither
		if ( description.howManyFramesPerOrientation () == 0 )
			description.makeSequenceOFrames( 1 ) ; // then it’s static

		// how many various orientations
		NodeList orientationsNodes = element.getElementsByTagName( "orientations" );
		if ( orientationsNodes.getLength () > 0 ) {
			String orientations = orientationsNodes.item( 0 ).getTextContent ();
			try { // parseByte can throw NumberFormatException
				description.setHowManyOrientations( Byte.parseByte( orientations ) );
			} catch ( NumberFormatException e ) { }
		}

		// how many extra frames, such as for jumping or blinking character
		NodeList extraFramesNodes = element.getElementsByTagName( "extra-frames" );
		if ( extraFramesNodes.getLength() == 0 && ItemDescriptions.parse_previous_format ) {
			NodeList extraFramesNodes_oldformat = element.getElementsByTagName( "extraFrames" );
			if ( extraFramesNodes_oldformat.getLength() > 0 ) extraFramesNodes = extraFramesNodes_oldformat ;
		}
		if ( extraFramesNodes.getLength () > 0 ) {
			String extraFrames = extraFramesNodes.item( 0 ).getTextContent ();
			try { // parseInt can throw NumberFormatException
				description.setHowManyExtraFrames( Integer.parseInt( extraFrames ) );
			} catch ( NumberFormatException e ) { }
		}
	}

}
