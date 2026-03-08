// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2026 Douglas Mencken dougmencken@gmail.com
// Original game by Jon Ritman, Bernie Drummond and Guy Stevens, released by Ocean Software Ltd. in 1987
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels ;


/**
 * A mutable string interface compatible with both StringBuffer and StringBuilder (Java 5+)
 * covering the most commonly used methods
 *
 * @see GrowingStrings
 */

public interface GrowingString
{

	public GrowingString append( String s ) ;
	public GrowingString append( Object o ) ;

	public int length() ;
	public void setLength( int newLength ) ;

	public char charAt( int index ) ;

	public String toString () ;

}
