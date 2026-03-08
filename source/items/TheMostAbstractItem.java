// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2026 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels.items ;

import head.over.heels.Mediated ;
import head.over.heels.NamedOffscreenImage ;
import head.over.heels.NoSuchPictureException ;

import head.over.heels.GrowingStrings ;
import head.over.heels.StringUtilities ;

import head.over.heels.behaviors.Behaviour ;

import java.util.Map ;
import java.util.Vector ;


/**
 * The most abstract item of the game
 */

public abstract class TheMostAbstractItem extends Mediated
{

	protected TheMostAbstractItem()
	{
		super() ;

		this.uniqueName = StringUtilities.makeRandomString( 22 );
		this.behavior = null ;
		this.currentSequence = "" ;
		this.currentFrame = 0 ;
	}

	// the copy constructor
	protected TheMostAbstractItem( TheMostAbstractItem itemToCopy )
	{
		this.uniqueName = itemToCopy.getUniqueName() + " copy" ;

		if ( itemToCopy.behavior == null )
			this.behavior = null ;
		else
			this.setBehaviourOf( itemToCopy.behavior.getName () );

		for ( String sequence : itemToCopy.frames.keySet() )
			for ( NamedOffscreenImage frame : itemToCopy.frames.get( sequence ) )
				addFrameTo( sequence, new NamedOffscreenImage( frame /* copy */ ) );

		this.currentSequence = itemToCopy.getCurrentFrameSequence() ;
		this.currentFrame = itemToCopy.getCurrentFrame() ;
	}

	// the name of this item by which it can be distinguished from any other item
	private String uniqueName ;

	public String getUniqueName () {  return this.uniqueName ;  }
	public void setUniqueName ( String name ) {  this.uniqueName = name ;  }

	/////public boolean isNamed () {  return this.uniqueName != null ;  } // ( is always named using makeRandomString )

	// the behaviour of item
	private Behaviour behavior = null ;

	public Behaviour getBehaviour () {  return this.behavior ;  }

	public void setBehaviourOf ( String name )
	{
		this.behavior = Behaviour.byName( name, this );
	}

	/**
	 * For an item with behavior, update that behavior programmatically
	 * @return true if the item can be updated thereafter (it didn’t disappear from the room)
	 */
	public boolean updateItem ()
	{
		return ( this.behavior != null ) ? this.behavior.update() : true ;
	}

	// the sequences of pictures of item
	private Map< String, Vector< NamedOffscreenImage > > frames = new java.util.HashMap< String, Vector< NamedOffscreenImage > > () ;

	protected void clearFrames ()
	{
		if ( this.frames != null ) this.frames.clear() ; // remove all elements from the frames map
	}

	// the current sequence of frames
	private String currentSequence = "" ;

	protected String getCurrentFrameSequence () {  return this.currentSequence ;  }

	/**
	 * The sequence of frames usually changes when the heading, aka angular orientation, changes
	 */
	protected void setCurrentFrameSequence ( String whatSequence ) {  this.currentSequence = whatSequence ;  }

	public int howManyFramesIn ( String sequence )
	{
		for ( String key : this.frames.keySet() )
			if ( key.equals( sequence ) ) return this.frames.get( key ).size() ;

		return 0 ;
        }

	public int howManyFramesInTheCurrentSequence () {  return howManyFramesIn( getCurrentFrameSequence() ) ;  }

	public int howManyFramesAtAll ()
	{
		int howManyFrames = 0 ;

		for ( String key : this.frames.keySet() )
			howManyFrames += this.frames.get( key ).size() ;

		return howManyFrames ;
	}

	// the current frame in the sequence
	private int currentFrame = 0 ;

	protected int getCurrentFrame () {  return this.currentFrame ;  }

	/**
	 * Override this method to tell is the item animated or not
	 */
	public abstract boolean isAnimated () ;

	protected NamedOffscreenImage getNthFrameIn ( String sequence, int n ) throws NoSuchPictureException
	{
		Vector< NamedOffscreenImage > framesIn = this.frames.get( sequence );
		if ( framesIn != null && n < framesIn.size() )
			return framesIn.elementAt( n );

		String message =
			GrowingStrings.newString( "there’s no " )
				.append( StringUtilities.toStringWithOrdinalSuffix( n ) ).append( " frame" )
				.append( " in " ).append( StringUtilities.putInQuotes( sequence ) )
				.append( " for " ).append( StringUtilities.putInQuotes( getUniqueName() ) )
				.toString() ;
		System.err.println( message );
		throw new NoSuchPictureException( message );
	}

	public NamedOffscreenImage getCurrentRawImageIn ( String sequence )
	{
		try {
			return getNthFrameIn( sequence, getCurrentFrame() ) ;
		} catch ( NoSuchPictureException x ) {
			System.err.println( x.getClass().getName() + ": " + x.getMessage() );
			return null ;
		}
	}

	public NamedOffscreenImage getCurrentRawImage () {  return getCurrentRawImageIn( getCurrentFrameSequence() ) ;  }

	/**
	 * Changes the shown frame (in the current sequence). Frames usually change when looping in
	 * the sequence of animation. However there’re some cases when frames are changed manually.
	 * As example, in the behavior of a spring stool the one frame is for rest and the other is
	 * for being fold
	 */
	public void changeFrameInTheCurrentSequence ( int newFrame )
	{
		if ( this.currentFrame == newFrame ) return ;

		if ( newFrame < howManyFramesInTheCurrentSequence() ) {
			this.currentFrame = newFrame ;
			// ....
		}
	}

	public void addFrameTo ( String sequence, NamedOffscreenImage frame )
	{
		if ( sequence == null || sequence.isEmpty() ) return ; // don’t add to ""

		if ( this.frames.get( sequence ) == null )
			this.frames.put( sequence, new Vector< NamedOffscreenImage >() );

		this.frames.get( sequence ).add( frame );

		if ( getCurrentFrameSequence().isEmpty() ) setCurrentFrameSequence( sequence );
	}

	public String toString ()
	{
		return "item " + super.toString() ;
	}

	public String whichClassOfItem ()
	{
		if ( this instanceof AvatarItem )
			return "avatar item" ;
		else
		if ( this instanceof FreeItem )
			return "free item" ;
		else
		if ( this instanceof GridItem )
			return "grid item" ;
		else
		if ( this instanceof DescribedItem )
			return "described item" ;
		else
		if ( this instanceof AnimatedItem )
			return "animated item" ;
		else
		if ( this instanceof TheMostAbstractItem )
			return "abstract item" ;
		else
			return "java.lang.Object" ;
	}

}
