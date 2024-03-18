// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2024 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels ;

public abstract class ShadyMediated extends Mediated implements Shady
{

	private boolean wantShadow ;

	public boolean getWantShadow () {  return wantShadow ;  }
	public void setWantShadow ( boolean wanna ) {  this.wantShadow = wanna ;  }

	public ShadyMediated ()
	{
		super() ;
		this.wantShadow = false ;
	}

}
