// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2024 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels ;

public class TooManyColoursException extends java.lang.Exception
{

	private TooManyColoursException( )
	{
		super( "too many colors" ) ;
		this.howMany = -1 /* unknown */ ;
		this.atMost = -2 /* unknown */ ;
	}

	public TooManyColoursException( int howMany, int atMost )
	{
		super( howMany + " various colors is more than " + atMost );
		this.howMany = howMany ;
		this.atMost = atMost ;
	}

	private int howMany ;
	private int atMost ;

	public int getHowMany() {  return this.howMany ;  }
	public int getAtMost() {  return this.atMost ;  }

}
