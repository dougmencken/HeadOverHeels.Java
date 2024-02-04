// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2024 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels ;

import java.io.File ;

import javax.xml.parsers.DocumentBuilder ;

import org.w3c.dom.Element ;
import org.w3c.dom.Node ;


public class GamePreferences
{
	interface constants
	{
		public static final int Smallest_Screen_Width  = 640 ;
		public static final int Smallest_Screen_Height = 480 ;

		public static final int Default_Screen_Width   = Smallest_Screen_Width ;
		public static final int Default_Screen_Height  = Smallest_Screen_Height ;
	}

	public int screenWidth  = constants.Default_Screen_Width ;
	public int screenHeight = constants.Default_Screen_Height ;

	public int getScreenWidth () {  return this.screenWidth ;  }
	public int getScreenHeight () {  return this.screenHeight ;  }

	public void setScreenWidth ( int width )
	{
		if ( width < constants.Smallest_Screen_Width )
			width = constants.Smallest_Screen_Width ;

		this.screenWidth = width ;
	}

	public void setScreenHeight ( int height )
	{
		if ( height < constants.Smallest_Screen_Height )
			height = constants.Smallest_Screen_Height ;

		this.screenHeight = height ;
	}

	private static boolean keepTheCurrentWidthOfScreen  = false ;
	private static boolean keepTheCurrentHeightOfScreen = false ;

	// it's used by "main" when the --width= or --height= options are set
	// which apply over the values from the preferences file
	public static void  keepThisWidthOfScreen () {  keepTheCurrentWidthOfScreen = true ;  }
	public static void keepThisHeightOfScreen () {  keepTheCurrentHeightOfScreen = true ;  }

	/**
	 * preferences.xml
	 */
	private File preferencesFile ;

	private DocumentBuilder xmlDocumentBuilder ;

	/**
	 * @param fileName the full path to file where the preferences are stored to read from and write to
	 */
	public GamePreferences( String fileName )
	{
		// it resides in the game storage in the home path
		this.preferencesFile = new File( FilesystemPaths.getGameStorageInHome(), fileName );

		try {
			this.xmlDocumentBuilder = javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder() ;
		} catch ( javax.xml.parsers.ParserConfigurationException ex ) { }
	}

	public boolean isOkayToRead ()
	{
		return this.preferencesFile.exists() && this.preferencesFile.isFile() && this.preferencesFile.canRead() ;
	}

	public boolean isOkayToWrite ()
	{
		File storageInHome = this.preferencesFile.getParentFile ();
		return storageInHome.exists() && storageInHome.isDirectory() && storageInHome.canWrite() ;
	}

	/**
	 * read the game's preferences from file
	 */
	public boolean readPreferences ()
	{
		if ( ! this.isOkayToRead () ) return false ;

		if ( this.xmlDocumentBuilder == null ) return false ;

		org.w3c.dom.Document preferences = null ;
		try {
			preferences = this.xmlDocumentBuilder.parse( this.preferencesFile );
		}
		catch ( org.xml.sax.SAXException x ) {  return false ;  }
		catch ( java.io.IOException io ) {  return false ;  }
		if ( preferences == null ) return false ;

		Element root = preferences.getDocumentElement() ;
		if ( root == null || root.getTagName() != "preferences" ) return false ;

		Node videoNode = preferences.getElementsByTagName( "video" ).item( 0 );
		if ( videoNode != null && videoNode.getNodeType() == Node.ELEMENT_NODE ) {
			Element videoElement = (Element) videoNode ;

			String width = "0" ;
			Node widthNode = videoElement.getElementsByTagName( "width" ).item( 0 );
			if ( widthNode != null ) width = widthNode.getTextContent ();

			int theWidth = 0 ;
			try { // parseInt can throw NumberFormatException
				theWidth = Integer.parseInt( width );
			} catch ( NumberFormatException e ) { }

			String height = "0" ;
			Node heightNode = videoElement.getElementsByTagName( "height" ).item( 0 );
			if ( heightNode != null ) height = heightNode.getTextContent ();

			int theHeight = 0 ;
			try { // parseInt can throw NumberFormatException
				theHeight = Integer.parseInt( height );
			} catch ( NumberFormatException e ) { }

			if ( GamePreferences.keepTheCurrentWidthOfScreen )
				GamePreferences.keepTheCurrentWidthOfScreen = false ;
			else
				this.setScreenWidth( theWidth );

			if ( GamePreferences.keepTheCurrentHeightOfScreen )
				GamePreferences.keepTheCurrentHeightOfScreen = false ;
			else
				this.setScreenHeight( theHeight );
		}

		return true ;
	}

	public boolean writePreferences ()
	{
		if ( ! this.isOkayToWrite () ) return false ;

		if ( this.xmlDocumentBuilder == null ) return false ;

		org.w3c.dom.Document preferences = this.xmlDocumentBuilder.newDocument ();

		// the root element
		Element root = preferences.createElement( "preferences" );
		preferences.appendChild( root );

		Element video = preferences.createElement( "video" );
		root.appendChild( video );

		Element videoWidth = preferences.createElement( "width" );
		videoWidth.setTextContent( String.valueOf( this.screenWidth ) );
		video.appendChild( videoWidth );

		Element videoHeight = preferences.createElement( "height" );
		videoHeight.setTextContent( String.valueOf( this.screenHeight ) );
		video.appendChild( videoHeight );

		try ( java.io.FileOutputStream outputStream = new java.io.FileOutputStream( this.preferencesFile ) )
		{
			javax.xml.transform.dom.DOMSource source = new javax.xml.transform.dom.DOMSource( preferences );
			javax.xml.transform.stream.StreamResult result = new javax.xml.transform.stream.StreamResult( outputStream );

			javax.xml.transform.Transformer transformer = javax.xml.transform.TransformerFactory.newInstance().newTransformer() ;
			transformer.setOutputProperty( javax.xml.transform.OutputKeys.INDENT, "yes" );
			transformer.transform( source, result );
		}
		catch ( javax.xml.transform.TransformerException e ) {  return false ;  }
		catch ( java.io.IOException e ) {  return false ;  }

		return true ;
	}

}
