// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2025 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels ;


/**
 * Counts milliseconds since the last go()
 */

public class Timer extends Object
{

	private long goTime ;

	public Timer( )
	{
		super( );
		this.goTime = System.currentTimeMillis() ;
	}

	public void go()
	{
		this.goTime = System.currentTimeMillis() ;
	}

	/**
	 * @return milliseconds since the last go()
	 */
	public long get()
	{
		return System.currentTimeMillis() - this.goTime ;
	}

	/**
	 * Set the timer to the value of another timer
	 */
	public void synchronizeWith( Timer anotherTimer )
	{
		this.goTime = anotherTimer.goTime ;
	}

}
