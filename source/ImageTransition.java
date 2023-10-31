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

	/**
	 * The transition is between these two images
	 */
	private BufferedImage imageFrom ;
	private BufferedImage imageTo ;

	protected BufferedImage getImageFrom () {  return this.imageFrom ;  }
	protected BufferedImage getImageTo () {  return this.imageTo ;  }

	public int  getWidth () {  return ( this.imageTo != null ) ? this.imageTo.getWidth () : 0 ;  }
	public int getHeight () {  return ( this.imageTo != null ) ? this.imageTo.getHeight () : 0 ;  }

	/**
	 * True when the transit() method has completed
	 */
	private boolean finished = false ;

	public boolean isFinished () {  return this.finished ;  }

	/**
	 * Constructs the new transition between two images, "from" or "before", and "to" or "after"
	 */
	public ImageTransition( BufferedImage before, BufferedImage after )
	{
		this.imageFrom = before ;
		this.imageTo = after ;

		this.transitionThread = new Thread( this );
	}

	/**
	 * Begins the transition process
	 */
	public void go ()
	{
		this.finished = false ;
		this.transitionThread.start ();
	}

	public synchronized void run ()
	{
		transit ();
		this.finished = true ;
		notifyAll() ;
	}

	public synchronized void waitForFinishing ()
	{
		try {
			while ( ! this.finished ) wait() ;
		}
		catch ( InterruptedException e ) {}
	}

	/**
	 * The transition itself, how it goes
	 */
	public abstract void transit () ;

}
