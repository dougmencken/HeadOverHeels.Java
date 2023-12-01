// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2023 Douglas Mencken dougmencken@gmail.com
// Original game by Jon Ritman, Bernie Drummond and Guy Stevens, released by Ocean Software Ltd. in 1987
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels ;

import java.util.Vector ;
import java.util.HashMap ;

class KnownOption
{

	private String theOption ;

	private boolean hasValue ;

	private String theDescription ;

	public KnownOption( String option, boolean has, String about )
	{
		this.theOption = option ;
		this.hasValue = has ;
		this.theDescription = about ;
	}

	public String getOption () {  return theOption ;  }

	public boolean withValue () {  return hasValue ;  }

	public String getDescription () {  return theDescription ;  }

}

public final class main
{

	private static Vector < KnownOption > knownOptions ;

	public static final int EXIT_SUCCESS = 0 ;

	public static final String current_version = "0.4dev" ;

	public static void main( String [] arguments )
	{
		System.out.println( "Head over Heels" );
		System.out.println( "the free and open source remake (in Java)" );
		System.out.println( "version " + current_version );
		System.out.println( );

		knownOptions = new Vector < KnownOption > () ;
		knownOptions.add( new KnownOption( "help", false, "shows this text which describes all the known options" ) );
		knownOptions.add( new KnownOption( "width", true, "the width of the game's screen, default is the value from preferences or =640" ) );
		knownOptions.add( new KnownOption( "height", true, "the height of the game's screen, default is the value from preferences or =480" ) );
		knownOptions.add( new KnownOption( "begin-game", false, "no menus, just begin the new game" ) );

		int  width = 256 ;
		int height = 192 ;

		boolean newGameNoGui = false ;

		if ( arguments.length > 0 )
		{
			HashMap < String, String > options = new HashMap < String, String > () ;

			for ( int a = 0 ; a < arguments.length ; ++ a )
			{
				String arg = arguments[ a ];
				if ( arg.charAt( 0 ) == '-' && arg.charAt( 1 ) == '-' ) {
					String option = arg.substring( 2 );
					String value = "" ;

					int whereIsEqu = option.indexOf( '=' );
					if ( whereIsEqu > 0 )
					{
						if ( ( whereIsEqu + 1 ) < option.length() ) {
							value = option.substring( whereIsEqu + 1 );

							if ( value.charAt( 0 ) == '\"' && value.charAt( value.length() - 1 ) == '\"' )
								value = value.substring( 1, value.length() - 2 );
						}

						option = option.substring( 0, whereIsEqu );
					}

					options.put( option, value.trim () );
				}
			}

			for ( String option : options.keySet() )
			{
				String value = options.get( option );

				boolean knownOption = false ;
				for ( KnownOption ko : knownOptions )
					if ( option.equals( ko.getOption() ) ) {
						knownOption = true ;
						break ;
					}

				if ( knownOption ) {
					System.out.print( "got option \"" + option + "\"" );
					if ( ! value.isEmpty () ) System.out.print( " with value \"" + value + "\"" );
					System.out.println( );
				} else
					System.out.println( "got unknown option \"" + option + "\"" );
			}

			if ( options.containsKey( "help" ) )
			{
				System.out.println( );
				System.out.println( "The options are" );
				for ( KnownOption ko : knownOptions ) {
					System.out.print( "     " );
					System.out.print( "--" + ko.getOption () );
					if ( ko.withValue () ) System.out.print( "=(value)" );
					System.out.print( " — " );
					System.out.print( ko.getDescription () );
					System.out.println( );
				}

				// and don't run the game, just exit
				System.exit( EXIT_SUCCESS );
			}

			if ( options.containsKey( "width" ) )
			{
				try { // parseInt can throw NumberFormatException
					width = Integer.parseInt( options.get( "width" ) );
					GamePreferences.keepThisWidthOfScreen ();
				} catch ( NumberFormatException e ) { }
			}

			if ( options.containsKey( "height" ) )
			{
				try { // parseInt can throw NumberFormatException
					height = Integer.parseInt( options.get( "height" ) );
					GamePreferences.keepThisHeightOfScreen();
				} catch ( NumberFormatException e ) { }
			}

			if ( options.containsKey( "begin-game" ) )
				newGameNoGui = true ;
		}

	/**/// temporary lines
	ItemDescriptions descriptions = new ItemDescriptions( ) ;
	descriptions.readDescriptionsFromFile( new java.io.File( FilesystemPaths.getPathToGameData(), "items.xml" ) ) ;
	/**/// ❌ ✔️

		GameWindow window = new GameWindow( width, height ) ;
		window.setVisible( true );
		window.randomPixelFadeIn( java.awt.Color.black );

	String musicFile = "music" + java.io.File.separator + "freedom.ogg" ;
	SoundManager.getInstance().play( new java.io.File( FilesystemPaths.getPathToGameData(), musicFile ), /* loop */ true );

	}

	public static void quit ()
	{
		SoundManager.getInstance().stopAll ();

		System.out.println( );
		System.out.println( "bye :*" );
	}

}
