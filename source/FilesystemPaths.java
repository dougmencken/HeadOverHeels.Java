// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2023 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels ;

import java.io.File ;

public class FilesystemPaths
{

	private static boolean printOnce = true ;

	public static File getGameStorageInHome ()
	{
		String homePath = System.getProperty( "user.home" );
		File storageInHome = new File( homePath, ".headoverheels.java" );
		if ( ! storageInHome.exists () ) storageInHome.mkdir() ;

		if ( printOnce ) {
			printOnce = false ;
			if ( storageInHome.exists() && storageInHome.isDirectory() )
				System.out.println( "the game storage is " + storageInHome.getAbsolutePath () );
			else
				System.err.println( "can't get the game storage in " + homePath );
		}

		return storageInHome ;
	}

	public static File getPathToGameData ()
	{
		return new File( "gamedata" );
	}

	private FilesystemPaths() {} // no instances

}
