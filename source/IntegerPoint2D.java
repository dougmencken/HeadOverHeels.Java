// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2026 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels ;


/**
 * A couple of integers named x and y
 */

public final class IntegerPoint2D
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

	// is that object logically equal to this object
	public boolean equals( Object that ) {
		return ( that instanceof IntegerPoint2D ) ? this.equals( (IntegerPoint2D) that ) : false ;
	}
	public boolean equals( IntegerPoint2D that ) {
		if ( this == that ) return true ;
		return ( this.getX() == that.getX() ) && ( this.getY() == that.getY() ) ;
	}

	// ensure that logically equal objects give the same hash code
	public int hashCode () {
		return ( this.getX() << 5 ) - this.getX() + this.getY() ; // hash = 31x + y
	}

}
