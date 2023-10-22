// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2023 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels ;

import java.util.TreeMap ;

import java.io.File ;
import java.io.IOException ;

import javax.xml.parsers.DocumentBuilderFactory ;
import javax.xml.parsers.DocumentBuilder ;
import javax.xml.parsers.ParserConfigurationException ;

import org.w3c.dom.Document ;
import org.w3c.dom.Element ;
import org.w3c.dom.Node ;
import org.w3c.dom.NodeList ;
import org.xml.sax.SAXException ;

/**
 * All the descriptions of the game's items as read from items.xml
 */

public class ItemDescriptions
{

	private TreeMap < String, DescriptionOfItem > descriptionsOfItems ;

	private boolean alreadyRead ;

	public ItemDescriptions ()
	{
		this.descriptionsOfItems = new TreeMap < String, DescriptionOfItem > () ;
		this.alreadyRead = false ;
	}

	/**
	 * Load the descriptions of items from the XML file
	 */
	boolean readDescriptionsFromFile ( String nameOfXMLFile )
	{
		if ( this.alreadyRead ) return true ;

		File xmlFile = new File( nameOfXMLFile );
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
		} catch ( SAXException x ) {
			return false ;
		} catch ( IOException io ) {
			System.err.println( "can't read file \"" + nameOfXMLFile + "\" (" + xmlFile.getAbsolutePath () + ")" );
			return false ;
		}
		if ( xml == null ) return false ;

		Element root = xml.getDocumentElement() ;
		if ( root == null || root.getTagName() != "items" ) return false ;

		NodeList itemNodes = xml.getElementsByTagName( "item" );
		for ( int i = 0 ; i < itemNodes.getLength() ; i ++ )
		{
			Node itemNode = itemNodes.item( i );
			if ( itemNode.getNodeType() == Node.ELEMENT_NODE ) {
				Element itemElement = (Element) itemNode ;

				final String itemLabel = itemElement.getAttribute( "label" ) ; // the label of item
				DescriptionOfItem newDescription = new DescriptionOfItem ( itemLabel );

				// how long, in seconds, it falls
				double itemWeight = 0.0 ;

				NodeList weightNodes = itemElement.getElementsByTagName( "weight" );
				if ( weightNodes.getLength () > 0 ) {
					String weight = weightNodes.item( 0 ).getTextContent (); // in milliseconds
					try { // parseDouble can throw NumberFormatException
						itemWeight = Double.parseDouble( weight ) / 1000.0 ;
					} catch ( NumberFormatException e ) { }
				}

				newDescription.setWeight( itemWeight );

				// how many seconds this item moves one single isometric unit
				double itemSpeed = 0.0 ;

				NodeList speedNodes = itemElement.getElementsByTagName( "speed" );
				if ( speedNodes.getLength () > 0 ) {
					String speed = speedNodes.item( 0 ).getTextContent (); // in milliseconds
					try { // parseDouble can throw NumberFormatException
						itemSpeed = Double.parseDouble( speed ) / 1000.0 ;
					} catch ( NumberFormatException e ) { }
				}

				newDescription.setSpeed( itemSpeed );

				// mortal or harmless
				boolean isMortal = false ;

				NodeList mortalNodes = itemElement.getElementsByTagName( "mortal" );
				if ( mortalNodes.getLength () > 0 ) {
					String mortal = mortalNodes.item( 0 ).getTextContent ();
					if ( mortal.equals( "true" ) )
						isMortal = true ;
				}

				newDescription.setMortal( isMortal );

				// how many various orientations
				byte variousOrientations = 0 ;

				NodeList orientationsNodes = itemElement.getElementsByTagName( "orientations" );
				if ( orientationsNodes.getLength () > 0 ) {
					String orientations = orientationsNodes.item( 0 ).getTextContent ();
					try { // parseByte can throw NumberFormatException
						variousOrientations = Byte.parseByte( orientations );
					} catch ( NumberFormatException e ) { }
				}

				newDescription.setHowManyOrientations( variousOrientations );

				// delay, in seconds, between frames in the animation sequence
				double itemDelayBetweenFrames = 0.0 ;

				NodeList framesDelayNodes = itemElement.getElementsByTagName( "framesDelay" );
				if ( framesDelayNodes.getLength () > 0 ) {
					String framesDelay = framesDelayNodes.item( 0 ).getTextContent (); // in milliseconds
					try { // parseDouble can throw NumberFormatException
						itemDelayBetweenFrames = Double.parseDouble( framesDelay ) / 1000.0 ;
					} catch ( NumberFormatException e ) { }
				}

				newDescription.setDelayBetweenFrames( itemDelayBetweenFrames );

			/* .................. */

				// three spatial dimensions
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

				System.out.println( newDescription.toString() );

				this.descriptionsOfItems.put( itemLabel, newDescription );
			}
		}

	/* ..... */

		this.alreadyRead = true ;
		return true ;
	}

}
