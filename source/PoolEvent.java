// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2026 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels ;


public class PoolEvent extends java.util.EventObject {

	private final String message ;

	public String getMessage() {  return this.message ;  }

	public PoolEvent( Object source, String message ) {
		super( source );
		this.message = message ;
	}

}
