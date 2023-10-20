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

				String itemLabel = itemElement.getAttribute( "label" ) ; // the label of item
				DescriptionOfItem newDescription = new DescriptionOfItem ( itemLabel );

				System.out.println( "<item label=\"" + newDescription.getLabel () + "\">" );

				String widthX = itemElement.getElementsByTagName( "widthX" ).item( 0 ).getTextContent ();
				String widthY = itemElement.getElementsByTagName( "widthY" ).item( 0 ).getTextContent ();
				String height = itemElement.getElementsByTagName( "height" ).item( 0 ).getTextContent ();

				System.out.println( "   widthX = " + widthX );
				System.out.println( "   widthY = " + widthY );
				System.out.println( "   height = " + height );
			}
		}

	/* ..... */

		this.alreadyRead = true ;
		return true ;
	}

}
