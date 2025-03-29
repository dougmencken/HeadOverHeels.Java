// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2025 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels ;

public class NoSuchPictureException extends NoSuchThingException
{

	public NoSuchPictureException( ) {  super( "there’s no such picture" ) ;  }
	public NoSuchPictureException( java.lang.String message ) {  super( message ) ;  }
	public NoSuchPictureException( java.lang.Appendable message ) {  super( message ) ;  }

}
