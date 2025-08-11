// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2025 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels ;


/**
 * A couple of integers named x and y
 */

public class IntegerPoint2D
{

	private final int x ;
	private final int y ;

	public int getX () {  return this.x ;  }
	public int getY () {  return this.y ;  }

	public IntegerPoint2D( int first, int second )
	{
		this.x = first ;
		this.y = second ;
	}

	public IntegerPoint2D( IntegerPoint2D that )  // the copy constructor
	{
		this.x = that.getX ();
		this.y = that.getY ();
	}

}
