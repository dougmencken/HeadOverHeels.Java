// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2023 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels ;

public abstract class DrawableAndMediated implements Drawable, Mediated
{

	/* mediated */

	private Mediator mediator ;

	public void setMediator ( Mediator mediator ) {  this.mediator = mediator ;  }

	public Mediator getMediator () {  return this.mediator ;  }

	/* constructor */

	public DrawableAndMediated ()
	{
		this.mediator = null ;
	}

}
