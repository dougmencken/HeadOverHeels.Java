// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2025 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels ;

/**
 * Manages the game, and nothing more. Doesn't play solitaire. Doesn't smoke. Anything at all. Really.
 */

public class GameManager extends java.lang.Object
{

	private static GameManager instance = null ;

	public static GameManager getInstance()
	{
		if ( GameManager.instance == null ) GameManager.instance = new GameManager( ) ;
		return GameManager.instance ;
	}

	// no ‘new GameManager’ outside the class itself
	private GameManager ()
	{
		this.chosenGraphicsSet = "present" ;
	}

	private String chosenGraphicsSet ;

	public String getChosenGraphicsSet () {  return this.chosenGraphicsSet ;  }
	public void setChosenGraphicsSet ( String newSet ) {  this.chosenGraphicsSet = newSet ;  }

	boolean usingPresentGraphicsSet () {  return this.chosenGraphicsSet != null && this.chosenGraphicsSet.equals( "present" ) ;  }
	boolean usingSimpleGraphicsSet () {  return this.chosenGraphicsSet != null && this.chosenGraphicsSet.equals( "simple" ) ;  }

}
