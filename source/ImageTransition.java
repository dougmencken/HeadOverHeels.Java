// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2023 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels ;

import java.awt.image.BufferedImage ;


public abstract class ImageTransition implements Runnable
{

	private Thread transitionThread ;

	private BufferedImage imageFrom ;
	private BufferedImage imageTo ;

	protected BufferedImage getImageFrom () {  return this.imageFrom ;  }
	protected BufferedImage getImageTo () {  return this.imageTo ;  }

	public int  getWidth () {  return ( this.imageTo != null ) ? this.imageTo.getWidth () : 0 ;  }
	public int getHeight () {  return ( this.imageTo != null ) ? this.imageTo.getHeight () : 0 ;  }

	private boolean finished = false ;

	public boolean isFinished () {  return this.finished ;  }
	protected void markAsFinished () {  this.finished = true ;  }

	public ImageTransition( BufferedImage from, BufferedImage to )
	{
		this.imageFrom = from ;
		this.imageTo = to ;

		this.transitionThread = new Thread( this );
	}

	public void go ()
	{
		this.finished = false ;
		this.transitionThread.start ();
	}

	public void run ()
	{
		transit ();
	}

	public abstract void transit () ;

}
