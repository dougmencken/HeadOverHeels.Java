// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2024 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels ;

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
 * All the descriptions of the game's items as read from items.xml
 */

public class ItemDescriptions
{

	private TreeMap < String, DescriptionOfItem >
	        descriptionsOfItems = new TreeMap < String, DescriptionOfItem > () ;

	private transient boolean alreadyRead = false ;


	public ItemDescriptions () { }

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

	/**
	 * Load the descriptions of items from the XML file
	 */
	boolean readDescriptionsFromFile ( File xmlFile )
	{
		if ( this.alreadyRead ) return true ;

		DocumentBuilder builder = null ;
		try {
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder() ;
		} catch ( ParserConfigurationException ex ) {
			return false ;
		}
		if ( builder == null ) return false ;

		Document xml = null ;
		try {
			if ( xmlFile.exists() && xmlFile.canRead() )
				xml = builder.parse( xmlFile );
			else
				System.out.println( "can't read file \"" + xmlFile.getPath () + "\"" );
		}
		catch ( org.xml.sax.SAXException x ) {  return false ;  }
		catch ( java.io.IOException io ) {  return false ;  }
		if ( xml == null ) return false ;

		Element root = xml.getDocumentElement() ;
		if ( root == null || root.getTagName() != "items" ) return false ;

		NodeList itemNodes = xml.getElementsByTagName( "item" );
		for ( int i = 0 ; i < itemNodes.getLength() ; i ++ )
		{
			Node itemNode = itemNodes.item( i );
			if ( itemNode.getNodeType() == Node.ELEMENT_NODE ) {
				Element itemElement = (Element) itemNode ;

				final String kindOfItem = itemElement.getAttribute( "kind" ) ; // the kind of item
				DescriptionOfItem newDescription = new DescriptionOfItem ( kindOfItem );

				// spatial dimensions
				int itemWidthX = 0 ;
				int itemWidthY = 0 ;
				int itemHeight = 0 ;

				String widthX = itemElement.getElementsByTagName( "widthX" ).item( 0 ).getTextContent ();
				try { // parseInt can throw NumberFormatException
					itemWidthX = Integer.parseInt( widthX );
				} catch ( NumberFormatException e ) { }
				newDescription.setWidthX( itemWidthX );

				String widthY = itemElement.getElementsByTagName( "widthY" ).item( 0 ).getTextContent ();
				try { // parseInt can throw NumberFormatException
					itemWidthY = Integer.parseInt( widthY );
				} catch ( NumberFormatException e ) { }
				newDescription.setWidthY( itemWidthY );

				String height = itemElement.getElementsByTagName( "height" ).item( 0 ).getTextContent ();
				try { // parseInt can throw NumberFormatException
					itemHeight = Integer.parseInt( height );
				} catch ( NumberFormatException e ) { }
				newDescription.setHeight( itemHeight );

				readDescriptionFurther( itemElement, newDescription );

				// and at last
				this.descriptionsOfItems.put( kindOfItem, newDescription );
			}
		}

		// and now the descriptions of doors
		// for a door there are three parts, and thus three times three dimensions

		NodeList doorNodes = xml.getElementsByTagName( "door" );
		for ( int i = 0 ; i < doorNodes.getLength() ; i ++ )
		{
			Node doorNode = doorNodes.item( i );
			if ( doorNode.getNodeType() == Node.ELEMENT_NODE ) {
				Element doorElement = (Element) doorNode ;

				String doorScenery = doorElement.getAttribute( "scenery" ) ;
				String doorAt = doorElement.getAttribute( "at" ) ;

				DescriptionOfDoor doorDescription = new DescriptionOfDoor ( doorScenery, doorAt );

				// the three parts of door
				DescriptionOfItem lintel = doorDescription.getLintel () ;
				DescriptionOfItem leftJamb = doorDescription.getLeftJamb ();
				DescriptionOfItem rightJamb = doorDescription.getRightJamb ();

				// and at last
				this.descriptionsOfItems.put(  leftJamb.getKind(), leftJamb );
				this.descriptionsOfItems.put( rightJamb.getKind(), rightJamb );
				this.descriptionsOfItems.put(    lintel.getKind(), lintel );
			}
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

		// how many milliseconds this item moves one single isometric unit
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

		NodeList pictureNodes = element.getElementsByTagName( "picture" );
		if ( pictureNodes.getLength () > 0
				&& pictureNodes.item( 0 ).getNodeType() == Node.ELEMENT_NODE )
		{
			Element picture = (Element) pictureNodes.item( 0 ) ;

			// the name of file with graphics for this item
			description.setNameOfPicturesFile( picture.getAttribute( "file" ) );

			// the width and height in pixels of a single frame
			Node widthNode = picture.getElementsByTagName( "width" ).item( 0 );
			if ( widthNode != null ) {
				try { // parseInt can throw NumberFormatException
					description.setWidthOfFrame( Integer.parseInt( widthNode.getTextContent () ) );
				} catch ( NumberFormatException e ) { }
			}
			Node heightNode = picture.getElementsByTagName( "height" ).item( 0 );
			if ( heightNode != null ) {
				try { // parseInt can throw NumberFormatException
					description.setHeightOfFrame( Integer.parseInt( heightNode.getTextContent () ) );
				} catch ( NumberFormatException e ) { }
			}
		} else
		{
			if ( description.getKind().startsWith( "invisible-wall" ) )
			{
				description.setNameOfPicturesFile( "" );
				description.setWidthOfFrame( 64 );
				description.setHeightOfFrame( 115 );
			}
		}

		// delay, in milliseconds, between frames in the animation sequence
		int itemDelayBetweenFrames = 0 ;

		NodeList betweenFramesNodes = element.getElementsByTagName( "betweenFrames" );
		if ( betweenFramesNodes.getLength () > 0 ) {
			String betweenFrames = betweenFramesNodes.item( 0 ).getTextContent ();
			try { // parseInt can throw NumberFormatException
				itemDelayBetweenFrames = Integer.parseInt( betweenFrames );
			} catch ( NumberFormatException e ) { }
		}

		description.setDelayBetweenFrames( itemDelayBetweenFrames );

		// shadows for this item

		NodeList shadowNodes = element.getElementsByTagName( "shadow" );
		if ( shadowNodes.getLength () > 0
				&& shadowNodes.item( 0 ).getNodeType() == Node.ELEMENT_NODE )
		{
			Element shadow = (Element) shadowNodes.item( 0 ) ;

			// the name of file with shadows for this item
			description.setNameOfShadowsFile( shadow.getAttribute( "file" ) );

			// the width and height in pixels of a single frame of the item’s shadow
			Node widthNode = shadow.getElementsByTagName( "width" ).item( 0 );
			if ( widthNode != null ) {
				try { // parseInt can throw NumberFormatException
					description.setWidthOfShadow( Integer.parseInt( widthNode.getTextContent () ) );
				} catch ( NumberFormatException e ) { }
			}
			Node heightNode = shadow.getElementsByTagName( "height" ).item( 0 );
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
			description.makeSequenceOFrames( 1 ) ; // then it's static

		// how many various orientations
		byte variousOrientations = 0 ;

		NodeList orientationsNodes = element.getElementsByTagName( "orientations" );
		if ( orientationsNodes.getLength () > 0 ) {
			String orientations = orientationsNodes.item( 0 ).getTextContent ();
			try { // parseByte can throw NumberFormatException
				variousOrientations = Byte.parseByte( orientations );
			} catch ( NumberFormatException e ) { }
		}

		description.setHowManyOrientations( variousOrientations );

		// how many extra frames, such as for jumping or blinking character
		short extraFrames = 0 ;

		NodeList extraFramesNodes = element.getElementsByTagName( "extraFrames" );
		if ( extraFramesNodes.getLength () > 0 ) {
			String extraFramesText = extraFramesNodes.item( 0 ).getTextContent ();
			try { // parseShort can throw NumberFormatException
				extraFrames = Short.parseShort( extraFramesText );
			} catch ( NumberFormatException e ) { }
		}

		description.setHowManyExtraFrames( extraFrames );
	}

}
