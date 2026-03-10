// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2026 Douglas Mencken dougmencken@gmail.com
// Original game by Jon Ritman, Bernie Drummond and Guy Stevens, released by Ocean Software Ltd. in 1987
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels ;

import org.w3c.dom.Element ;
import org.w3c.dom.Node ;
import org.w3c.dom.NodeList ;

/**
 * Abstraction layer over DOM
 *
 * Example of use
 *    XElement root = new XElement( xml.getDocumentElement() );
 *    String heading = root.firstChild( "starring" ).firstChild( "character" ).getText( "heading" );
 *    int lives = root.firstChild( "starring" ).firstChild( "character" ).getInt( "lives", 0 );
 */

public class XElement {

	private Element element ;

	public XElement( Element e ) {
		if ( e == null ) throw new IllegalArgumentException( "null element" );
		this.element = e ;
	}

	/** @return the attribute value as a string, or the empty string */
	public String getAttribute( String name ) {
		return this.element.getAttribute( name );
	}

	/** @return a NodeList of elements (ELEMENT_NODEs only) with the given tag name */
	public NodeList elementNodesByTag( String tag ) {
		return this.element.getElementsByTagName( tag );
	}

	/** @return the first element with the given tag name, or null if there are no such elements */
	public Element firstElementByTag( String tag ) {
		NodeList nodes = elementNodesByTag( tag );
		return ( nodes.getLength() > 0 ) ? (Element) nodes.item( 0 ) : null ;
	}

	/** @return the first child XElement with that tag name, or the NullXElement instance */
	public XElement firstChild( String tag ) {
		Element element = firstElementByTag( tag );
		return ( element != null ) ? new XElement( element ) : nullXElement ;
	}

	/** @return the text content of the first element with that tag name, or null */
	public String getText( String tag ) {
		Element element = firstElementByTag( tag );
		return ( element != null ) ? XElement.getTextContentOf( element ) : null ;
	}

	/** @return the integer value of getText(tag), or def if it can’t be parsed into an integer */
	public int getInt( String tag, int def ) {
		try {
			return Integer.parseInt( getText( tag ) );
		} catch ( Exception e ) {
			return def ;
		}
	}

	/** @return true if this element is not an instance of NullXElement */
	public boolean exists() {  return true ;  }

	private static final XElement nullXElement = new NullXElement() ;

	private XElement() {  this.element = null ;  } // for use by the NullXElement private subclass only

	/**
	 * The very special case of a non-existent XElement
	 */
	private static final class NullXElement extends XElement {

		private NullXElement() {  super() ;  }

		public boolean exists() {  return false ;  }

		public String getAttribute( String name ) {  return "" ;  }

		public NodeList elementNodesByTag( String tag ) {
			return NullXElement.emptyNodeList ;
		}

		public Element firstElementByTag( String tag ) {  return null ;  }

		public XElement firstChild( String tag ) {
			return this ; // keep propagating the null object down the chain
		}

		public String getText( String tag ) {  return null ;  }

		public int getInt( String tag, int def ) {  return def ;  }

		public String toString() {  return "NullXElement, The" ;  }

		private static final NodeList emptyNodeList = new EmptyNodeList() ;

	}

	private static final class EmptyNodeList implements NodeList {

		public Node item( int i ) {  return null ;  }
		public int getLength() {  return 0 ;  }

	}

	/**
	 * Implements the DOM Level 3 getTextContent() method for a node
	 * @return the text content of the node and its descendants
	 */
	public static String getTextContentOf( Node node )
	{
		GrowingString out = GrowingStrings.newString() ;
		XElement.collectTextContent( node, out );
		return out.toString() ;
	}

	private static void collectTextContent( Node node, GrowingString text ) {
		if ( node == null ) return ;

		short nodeType = node.getNodeType() ;
		if ( nodeType == Node.TEXT_NODE || nodeType == Node.CDATA_SECTION_NODE )
			text.append( node.getNodeValue() ); // text nodes and CDATA section nodes are always leafs
		else
			for ( Node child = node.getFirstChild() ; child != null ; child = child.getNextSibling() )
				XElement.collectTextContent( child, text ) ;
	}

}
