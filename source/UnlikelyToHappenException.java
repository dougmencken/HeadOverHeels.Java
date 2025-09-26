// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2025 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels ;

/**
 * Something unexpected, although possible, happened
 */

public class UnlikelyToHappenException extends java.lang.RuntimeException
{

	public UnlikelyToHappenException( ) {  super( "unlikely to happen" ) ;  }
	public UnlikelyToHappenException( java.lang.String message ) {  super( message ) ;  }
	public UnlikelyToHappenException( java.lang.Appendable message ) {  super( message.toString() ) ;  }

}
