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

import java.lang.reflect.Method ;


public class XElement {

	private static final Method getTextContent_method = get_getTextContent_method_of_Node() ;

	private static Method get_getTextContent_method_of_Node () {
		try {
			return org.w3c.dom.Node.class.getMethod( "getTextContent", new Class[0] );
		} catch ( Exception e ) {
			return null ;
		}
	}

	private Element element ;

	public XElement( Element e ) {
		if ( e == null ) throw new IllegalArgumentException( "null element" );
		this.element = e ;
	}

	public String getAttribute( String name ) {
		return this.element.getAttribute( name );
	}

	public NodeList nodesByTag( String tag ) {
		return this.element.getElementsByTagName( tag );
	}

	public Element firstElementByTag( String tag )
	{
		NodeList nodes = nodesByTag( tag );

		if ( nodes.getLength() > 0 && nodes.item( 0 ).getNodeType() == Node.ELEMENT_NODE )
			return (Element) nodes.item( 0 ) ;

		return null ;
	}

	public String getText( String tag )
	{
		Node node = nodesByTag( tag ).item( 0 );
		if ( node == null ) return null ;

		if ( getTextContent_method != null ) {
			try {
				return (String) getTextContent_method.invoke( node, new Object[0] );
			} catch ( Exception e ) {}
		}

		return XElement.getTextContent( node ) ;
	}

	public int getInt( String tag, int def ) {
		try {
			return Integer.parseInt( getText( tag ) );
		} catch ( Exception e ) {
			return def ;
		}
	}

	/**
	 * Implements the DOM Level 3 getTextContent() method for a node
	 * @return the text content of the node and its descendants
	 */
	public static String getTextContent( Node node )
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
