// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2026 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels ;


/**
 * A couple of integers representing a size in two-dimensional space
 */

public final class IntegerSize2D
{

	private final int widthX ;
	private final int widthY ;

	public int getWidthX () {  return this.widthX ;  }
	public int getWidthY () {  return this.widthY ;  }

	public int getWidth  () {  return this.widthX ;  }
	public int getHeight () {  return this.widthY ;  }

	public int getLengthX () {  return this.widthX ;  }
	public int getLengthY () {  return this.widthY ;  }

	public int getBreadth () {  return this.widthX ;  }
	public int getLength  () {  return this.widthY ;  }

	public int getFirst  () {  return this.widthX ;  }
	public int getSecond () {  return this.widthY ;  }

	public int[] getBoth ()  {  return new int[] { this.widthX, this.widthY } ;  }

	public IntegerSize2D( int first, int second )
	{
		this.widthX = first ;
		this.widthY = second ;
	}

	public IntegerSize2D( IntegerSize2D that )  // the copy constructor
	{
		this.widthX = that.getWidthX ();
		this.widthY = that.getWidthY ();
	}

	// is that object logically equal to this object
	public boolean equals( Object that ) {
		return ( that instanceof IntegerSize2D ) ? this.equals( (IntegerSize2D) that ) : false ;
	}
	public boolean equals( IntegerSize2D that ) {
		if ( this == that ) return true ;
		return ( this.getFirst() == that.getFirst() ) && ( this.getSecond() == that.getSecond() ) ;
	}

	// ensure that logically equal objects give the same hash code
	public int hashCode () {
		return ( this.getFirst() << 5 ) - this.getFirst() + this.getSecond() ; // hash = 31w + h
	}

}
