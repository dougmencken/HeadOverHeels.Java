// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2026 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels.items ;

import head.over.heels.Timer ;


public abstract class AnimatedItem extends TheMostAbstractItem
{

	protected AnimatedItem( long delay )
	{
		super( );
		this.backwardsMotion = false ;
		this.delayBetweenFrames = delay ;
		this.resetAnimation() ;
	}

	protected AnimatedItem( ) {  this( /* the default delay between frames */ 20 );  }

	// the copy constructor
	protected AnimatedItem( AnimatedItem thatItem )
	{
		super( thatItem );
		this.backwardsMotion = thatItem.isAnimatedBackwards() ;
		this.delayBetweenFrames = thatItem.getDelayBetweenFrames() ;
		this.resetAnimation() ;
	}

	public void animate ()
	{
		if ( ! isAnimated() ) return ;

		// is it time to change frame
		if ( this.animationTimer.get() > getDelayBetweenFrames() )
		{
			int nextFrame = getCurrentFrame() ;

			if ( ! isAnimatedBackwards() ) {
				// forwards motion
				if ( isAnimationFinished() )
					nextFrame = firstFrame() ;
				else
					nextFrame ++ ;
			} else {
				// backwards motion
				if ( isAnimationFinished() )
					nextFrame = lastFrame() ;
				else
					nextFrame -- ;
			}

			super.changeFrameInTheCurrentSequence( nextFrame );

			this.animationTimer.go() ; // reset the timer
		}
	}

	public boolean isAnimated () {  return true ;  }

	protected int firstFrame () {  return 0 ;  }

	protected int lastFrame ()
	{
		int howMany = super.howManyFramesInTheCurrentSequence() ;
		return ( howMany > 0 ) ? howMany - 1 : 0 ;
	}

	// true to reverse the animation sequence
	private boolean backwardsMotion ;

	public boolean isAnimatedBackwards () {  return this.backwardsMotion ;  }

	/**
	 * Animate from the first to the last frame, which is by default
	 */
	public void doForwardsMotion ()
	{
		this.backwardsMotion = false ;
		super.changeFrameInTheCurrentSequence( firstFrame() );
	}

	/**
	 * Animate from the last to the first frame, backwards
	 */
	public void doBackwardsMotion ()
	{
		this.backwardsMotion = true ;
		super.changeFrameInTheCurrentSequence( lastFrame() );
	}

	public boolean isAnimationFinished ()
	{
		if ( ! isAnimatedBackwards() )
			return getCurrentFrame() == lastFrame() ;
		else
			return getCurrentFrame() == firstFrame() ;
        }

	protected void setCurrentFrameSequence ( String whatSequence )
	{
		super.setCurrentFrameSequence( whatSequence );
		resetAnimation() ;
	}

	protected void resetAnimation ()
	{
		super.changeFrameInTheCurrentSequence( isAnimatedBackwards() ? lastFrame() : firstFrame() );
		this.animationTimer.go() ; // reset the timer
	}

	private long delayBetweenFrames ; // in milliseconds

	public long getDelayBetweenFrames () {  return this.delayBetweenFrames ;  }
	public void setDelayBetweenFrames ( long delay ) {  this.delayBetweenFrames = delay ;  }

	private Timer animationTimer = new Timer() ;

}
