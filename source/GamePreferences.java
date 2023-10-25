// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2023 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels ;


interface constants
{
	public static final int Smallest_Screen_Width  = 640 ;
	public static final int Smallest_Screen_Height = 480 ;

	public static final int Default_Screen_Width   = Smallest_Screen_Width ;
	public static final int Default_Screen_Height  = Smallest_Screen_Height ;
}

public class GamePreferences
{

	private String preferencesFile ;

	public int screenWidth  = constants.Default_Screen_Width ;
	public int screenHeight = constants.Default_Screen_Height ;

	public int getScreenWidth () {  return this.screenWidth ;  }
	public int getScreenHeight () {  return this.screenHeight ;  }

	public void setScreenWidth ( int width ) {  this.screenWidth = width ;  }
	public void setScreenHeight ( int height ) {  this.screenHeight = height ;  }

	boolean keepTheCurrentWidthOfScreen  = false ;
	boolean keepTheCurrentHeightOfScreen = false ;

	public void keepThisWidthOfScreen ( boolean keep ) {  this.keepTheCurrentWidthOfScreen = keep ;  }
	public void keepThisHeightOfScreen ( boolean keep ) {  this.keepTheCurrentHeightOfScreen = keep ;  }

	/**
	 * @param fileName the full path to file where the preferences are stored to read from and write to
	 */
	public GamePreferences( String fileName )
	{
		this.preferencesFile = fileName ;
	}

	public boolean readPreferences ()
	{
		return false ;
	}

	public boolean writePreferences ()
	{
		return false ;
	}

}
