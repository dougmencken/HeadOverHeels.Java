// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2025 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels ;


/**
 * A couple of strings
 */

public class PairOfStrings
{

	private final String first ;
	private final String second ;

	public String getFirst () {  return this.first ;  }
	public String getSecond () {  return this.second ;  }

	public PairOfStrings( String first, String second )
	{
		this.first = first ;
		this.second = second ;
	}

	public PairOfStrings( PairOfStrings that )  // the copy constructor
	{
		this.first = that.getFirst() ;
		this.second = that.getSecond() ;
	}

}
