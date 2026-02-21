// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2026 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels ;

import java.io.File ;


/**
 * Various helper functions related to files
 */

public class FileUtilities
{

	public static File findFirstFileByNameRecursively( File folder, String name )
	{
		if ( name != null && folder != null && folder.exists() && folder.isDirectory() ) {
			for ( File entry : folder.listFiles() ) {
				if ( entry.isDirectory() ) {
					File found = findFirstFileByNameRecursively( entry, name );
					if ( found != null ) return found ;
					// if not found, continue searching
				}
				else if ( entry.getName().equals( name ) )
					return entry ;
			}
		}

		return null ;
	}

}
