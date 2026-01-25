// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2026 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels ;


/**
 * Presents a value that can be true, false, or neither
 */

public final class TriBool
{

	public static final byte trifalse = 0 ;
	public static final byte tritrue = ~trifalse ;
	public static final byte trineither = 0x55 /* 01010101 in binary */ ;

	private byte theValue ;

	public boolean isTrue () {  return this.theValue == TriBool.tritrue ;  }
	public boolean isFalse () {  return this.theValue == TriBool.trifalse ;  }
	public boolean isNeither () {  return this.theValue == TriBool.trineither ;  }

	public void setTrue () {  this.theValue = TriBool.tritrue ;  }
	public void setFalse () {  this.theValue = TriBool.trifalse ;  }
	public void setNeither () {  this.theValue = TriBool.trineither ;  }

	public TriBool( )
	{
		this.theValue = TriBool.trineither ;
	}

	public TriBool( byte value )
	{
		this.theValue = ( value == TriBool.trifalse || value == TriBool.tritrue ) ? value : TriBool.trineither ;
	}

	public TriBool( TriBool that )
	{
		this.theValue = that.theValue ;
	}

}
