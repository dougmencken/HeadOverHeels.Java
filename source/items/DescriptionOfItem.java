// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2026 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels.items ;


/**
 * The description of an item as read from items.xml
 */

public class DescriptionOfItem implements Cloneable
{

	private String kind ;

	public String getKind () {  return this.kind ;  }
	protected void setKind ( String newKind ) {  this.kind = newKind ;  }

	/**
	 * The three spatial dimensions (widths) of the item, along the x, along the y, and height along the z
	 */
	private int widthX = 0 ;
	private int widthY = 0 ;
	private int height = 0 ;

	public int getWidthX () {  return this.widthX ;  }
	public void setWidthX ( int wx ) {  this.widthX = wx ;  }

	public int getWidthY () {  return this.widthY ;  }
	public void setWidthY ( int wy ) {  this.widthY = wy ;  }

	public int getHeight () {  return this.height ;  }
	public void setHeight ( int wz ) {  this.height = wz ;  }

	/**
	 * The weight of the item in milliseconds, higher for the bigger speed of falling, zero for no gravity (no falling)
	 */
	private int weight = 0 ;

	public int getWeight () {  return this.weight ;  }
	public void setWeight( int newWeight ) {  this.weight = newWeight ;  }

	/**
	 * The time in milliseconds the item takes to move one free unit
	 */
	private int speed = 0 ;

	public int getSpeed () {  return this.speed ;  }
	public void setSpeed( int newSpeed ) {  this.speed = newSpeed ;  }

	/**
	 * When true, this item takes one life from the character on touch
	 */
	private boolean mortal = false ;

	public boolean isMortal() {  return this.mortal ;  }
	public void setMortal( boolean newMortal ) {  this.mortal = newMortal ;  }

	/**
	 * The file with graphics for this item
	 */
	private String nameOfFramesFile = "" ;

	public String getNameOfFramesFile () {  return this.nameOfFramesFile ;  }
	public void setNameOfFramesFile ( String newFramesFile ) {  this.nameOfFramesFile = newFramesFile ;  }

	/**
	 * The width and height in pixels of a single frame of the item’s image
	 */
	private int widthOfFrame  = 0 ;
	private int heightOfFrame = 0 ;

	public int getWidthOfFrame () {  return this.widthOfFrame ;  }
	public void setWidthOfFrame( int newWidthOfFrame ) {  this.widthOfFrame = newWidthOfFrame ;  }

	public int getHeightOfFrame () {  return this.heightOfFrame ;  }
	public void setHeightOfFrame( int newHeightOfFrame ) {  this.heightOfFrame = newHeightOfFrame ;  }

	/**
	 * The delay, in milliseconds, between frames in the animation sequence
	 */
	private int delayBetweenFrames = 0 ;

	public int getDelayBetweenFrames () {  return this.delayBetweenFrames ;  }
	public void setDelayBetweenFrames( int newDelay ) {  this.delayBetweenFrames = newDelay ;  }

	/**
	 * The file with shadows for this item
	 */
	private String nameOfShadowsFile = "" ;

	public String getNameOfShadowsFile () {  return this.nameOfShadowsFile ;  }
	public void setNameOfShadowsFile ( String newShadowsFile ) {  this.nameOfShadowsFile = newShadowsFile ;  }

	/**
	 * The width and height in pixels of a single frame of the item’s shadow
	 */
	private int widthOfShadow  = 0 ;
	private int heightOfShadow = 0 ;

	public int getWidthOfShadow () {  return this.widthOfShadow ;  }
	public void setWidthOfShadow( int newWidthOfShadow ) {  this.widthOfShadow = newWidthOfShadow ;  }

	public int getHeightOfShadow () {  return this.heightOfShadow ;  }
	public void setHeightOfShadow( int newHeightOfShadow ) {  this.heightOfShadow = newHeightOfShadow ;  }

	// the frame sequence is just the single 0 for a static item
	private static final int[] single_frame_sequence = new int[]{ 0 } ;

	/**
	 * The sequence of item’s frames for one orientation
	 */
	private int[] sequenceOFrames = single_frame_sequence ;

	public int howManyFramesPerOrientation () {  return this.sequenceOFrames.length ;  }

	int getFrameAt( int at ) {
		if ( at >= 0 && at < this.sequenceOFrames.length )
			return this.sequenceOFrames[ at ] ;
		else
			throw new IndexOutOfBoundsException( "at=" + at + " is out of 0 ≤ at < " + this.sequenceOFrames.length );
	}

	void setSequenceOFrames( int [] newSequence )
	{
		this.sequenceOFrames = ( newSequence != null && newSequence.length > 1 ) ? newSequence : single_frame_sequence ;
	}

	boolean isSequenceOFramesSimple ()
	{
		int[] sequence = this.sequenceOFrames ;
		int frames = sequence.length ;

		for ( int i = 0 ; i < frames ; i ++ )
			if ( sequence[ i ] != i ) return false ;

		return true ;
	}

	void setSimpleSequenceOFrames( int howMany )
	{
		setSequenceOFrames( ( howMany > 1 ) ? makeSimpleSequence( howMany ) : single_frame_sequence );
	}

	private static int[] makeSimpleSequence ( int length ) {
		if ( length < 1 ) length = 1 ;

		int[] simpleSequence = new int[ length ] ;
		for ( int j = 0 ; j < length ; ++ j )
			simpleSequence[ j ] = j ;

		return simpleSequence ;
	}

	/**
	 * 1 if the item is the same on all sides, thus there’s only one orientation,
	 * 2 if there’re different images for south and west, or
	 * 4 when there’re different images for each orientation
	 * 0 otherwise and for new descriptions
	 */
	private byte orientations = 0 ;

	public byte howManyOrientations () {  return this.orientations ;  }

	public void setHowManyOrientations( byte newOrientations )
	{
		if      ( newOrientations == 1 ) this.orientations = 1 ;
		else if ( newOrientations == 2 ) this.orientations = 2 ;
		else if ( newOrientations == 4 ) this.orientations = 4 ;
		else                             this.orientations = 0 ;
	}

	/**
	 * Extra frames such as for jumping or blinking
	 */
	private int extraFrames = 0 ;

	public int howManyExtraFrames () {  return this.extraFrames ;  }
	public void setHowManyExtraFrames( int newExtraFrames ) {  this.extraFrames = newExtraFrames ;  }

	// used when a door is disassembled into three parts
	private transient boolean partOfDoor = false ;

	public boolean isPartOfDoor () {  return this.partOfDoor ;  }
	protected void markAsPartOfDoor () {  this.partOfDoor = true ;  }

	/*
	 * Create new description of items of this kind
	 */
	public DescriptionOfItem ( String kindOfItem )
	{
		this.kind = kindOfItem ;
	}

	public boolean equals( Object that )
	{
		return ( that instanceof DescriptionOfItem ) ? this.equals( (DescriptionOfItem) that ) : false ;
	}

	public boolean equals( DescriptionOfItem that )
	{
		if ( that == null ) return false ;

		return	this.kind.equals( that.kind )
				&& this.widthX == that.widthX && this.widthY == that.widthY && this.height == that.height
				&& this.weight == that.weight && this.speed == that.speed
				&& this.mortal == that.mortal
				&& this.nameOfFramesFile.equals( that.nameOfFramesFile )
				&& this.widthOfFrame == that.widthOfFrame && this.heightOfFrame == that.heightOfFrame
				&& this.delayBetweenFrames == that.delayBetweenFrames
				&& this.nameOfShadowsFile.equals( that.nameOfShadowsFile )
				&& this.widthOfShadow == that.widthOfShadow && this.heightOfShadow == that.heightOfShadow
				&& ( java.util.Arrays.equals( this.sequenceOFrames, that.sequenceOFrames )
					|| ( this.sequenceOFrames.length == 1 && that.sequenceOFrames.length == 1 ) )
				&& this.orientations == that.orientations
				&& this.extraFrames == that.extraFrames
		;
	}

	/**
	 * Returns a copy of this DescriptionOfItem for which copy.equals(this) is true
	 */
	@Override
	public DescriptionOfItem clone() /* doesn't throw CloneNotSupportedException */
	{
		DescriptionOfItem theClone = new DescriptionOfItem( this.kind );

		theClone.widthX = this.widthX ;
		theClone.widthY = this.widthY ;
		theClone.height = this.height ;

		theClone.weight = this.weight ;
		theClone.speed = this.speed ;

		theClone.mortal = this.mortal ;

		theClone.nameOfFramesFile = new String( this.nameOfFramesFile );

		theClone.widthOfFrame = this.widthOfFrame ;
		theClone.heightOfFrame = this.heightOfFrame ;

		theClone.delayBetweenFrames = this.delayBetweenFrames ;

		theClone.nameOfShadowsFile = new String( this.nameOfShadowsFile );

		theClone.widthOfShadow = this.widthOfShadow ;
		theClone.heightOfShadow = this.heightOfShadow ;

		// copy the sequence of animation
		int howManyFrames = howManyFramesPerOrientation() ;
		if ( howManyFrames > 1 ) {
			int [] sequence = new int[ howManyFrames ] ;
			for ( int i = 0 ; i < howManyFrames ; ++ i )
				sequence[ i ] = this.sequenceOFrames[ i ] ;
			theClone.setSequenceOFrames( sequence );
		}

		theClone.orientations = this.orientations ;
		theClone.extraFrames = this.extraFrames ;

		return theClone ;
	}

	public String toString ()
	{
		StringBuilder text = new StringBuilder( );
		String newline = System.getProperty( "line.separator" );
		String indent = "    " ;

		text.append( "<item kind=\"" + getKind () + "\">" );
		text.append( newline );

		text.append( indent );
		text.append( "<width-x>" + getWidthX () + "</width-x>" );
		text.append( newline );

		text.append( indent );
		text.append( "<width-y>" + getWidthY () + "</width-y>" );
		text.append( newline );

		text.append( indent );
		text.append( "<height>" + getHeight () + "</height>" );
		text.append( newline );

		if ( getWeight () > 0 ) {
			text.append( indent );
			text.append( "<weight>" + getWeight () + "</weight>" );
			text.append( newline );
		}

		if ( getSpeed () > 0 ) {
			text.append( indent );
			text.append( "<speed>" + getSpeed () + "</speed>" );
			text.append( newline );
		}

		if ( isMortal () ) {
			text.append( indent );
			text.append( "<is-mortal>yes</is-mortal>" );
			text.append( newline );
		}

		if ( getNameOfFramesFile().length() > 0 ) {
			text.append( indent );
			text.append( "<graphics file=\"" + getNameOfFramesFile () + "\">" );
			text.append( newline );

			text.append( indent ); text.append( indent );
			text.append( "<frame-width>" + getWidthOfFrame () + "</frame-width>" );
			text.append( newline );

			text.append( indent ); text.append( indent );
			text.append( "<frame-height>" + getHeightOfFrame () + "</frame-height>" );
			text.append( newline );

			text.append( indent );
			text.append( "</graphics>" );
			text.append( newline );
		}

		if ( getDelayBetweenFrames () > 0 ) {
			text.append( indent );
			text.append( "<delay-between-frames>" + getDelayBetweenFrames () + "</delay-between-frames>" );
			text.append( newline );
		}

		if ( getNameOfShadowsFile().length() > 0 ) {
			text.append( indent );
			text.append( "<shadows file=\"" + getNameOfShadowsFile () + "\">" );
			text.append( newline );

			text.append( indent ); text.append( indent );
			text.append( "<width-of-shadow>" + getWidthOfShadow () + "</width-of-shadow>" );
			text.append( newline );

			text.append( indent ); text.append( indent );
			text.append( "<height-of-shadow>" + getHeightOfShadow () + "</height-of-shadow>" );
			text.append( newline );

			text.append( indent );
			text.append( "</shadows>" );
			text.append( newline );
		}

		if ( isSequenceOFramesSimple () ) {
			if /* item is not static */ ( howManyFramesPerOrientation() > 1 ) {
				text.append( indent );
				text.append( "<frames>" + howManyFramesPerOrientation() + "</frames>" );
				text.append( newline );
			}
		} else
			for ( int frame : this.sequenceOFrames ) {
				text.append( indent );
				text.append( "<frame>" + frame + "</frame>" );
				text.append( newline );
			}

		text.append( indent );
		text.append( "<orientations>" + howManyOrientations () + "</orientations>" );
		text.append( newline );

		if ( howManyExtraFrames () > 0 ) {
			text.append( indent );
			text.append( "<extra-frames>" + howManyExtraFrames () + "</extra-frames>" );
			text.append( newline );
		}

		text.append( "</item>" );

		return text.toString() ;
	}

}
