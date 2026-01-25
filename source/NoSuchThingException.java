// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2026 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels ;

/**
 * When throwing an exception is better than returning null
 */

public class NoSuchThingException extends java.lang.Exception
{

	public NoSuchThingException( ) {  super( "something is not present" ) ;  }
	public NoSuchThingException( java.lang.String message ) {  super( message ) ;  }
	public NoSuchThingException( java.lang.Appendable message ) {  super( message.toString() ) ;  }

}
