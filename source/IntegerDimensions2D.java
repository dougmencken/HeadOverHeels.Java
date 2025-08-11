// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2025 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels ;


/**
 * A couple of integers named width and height
 */

public class IntegerDimensions2D
{

	private final int width ;
	private final int height ;

	public int getWidth () {  return this.width ;  }
	public int getHeight () {  return this.height ;  }

	public IntegerDimensions2D( int first, int second )
	{
		this.width = first ;
		this.height = second ;
	}

	public IntegerDimensions2D( IntegerDimensions2D that )  // the copy constructor
	{
		this.height = that.getHeight ();
		this.width = that.getWidth ();
	}

}
