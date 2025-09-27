// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2025 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels.items ;

import java.util.Vector ;


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

	/**
	 * The sequence of item's frames for one orientation
	 */
	private Vector< Integer > sequenceOFrames = new Vector< Integer >() ;

	public int howManyFramesPerOrientation () {  return this.sequenceOFrames.size() ;  }

	int getFrameAt( int at ) {
		return ( at >= 0 && at < this.sequenceOFrames.size () ) ? this.sequenceOFrames.elementAt( at ) : 0 ;  }

	public boolean isSequenceOFramesSimple ()
	{
		for ( int i = 0 ; i < this.sequenceOFrames.size () ; i ++ )
			if ( this.sequenceOFrames.elementAt( i ) != i ) return false ;

		return true ;
	}

	void makeSequenceOFrames( int howMany )
	{
		if ( sequenceOFrames.size() > 0 ) sequenceOFrames.clear () ;

		for ( int j = 0 ; j < howMany ; j ++ )
			sequenceOFrames.add( j );
	}

	void setSequenceOFrames( Vector< Integer > newSequence )
	{
		if ( sequenceOFrames.size() > 0 ) sequenceOFrames.clear () ;
		this.sequenceOFrames = newSequence ;
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
	private short extraFrames = 0 ;

	public short howManyExtraFrames () {  return this.extraFrames ;  }
	public void setHowManyExtraFrames( short newExtraFrames ) {  this.extraFrames = newExtraFrames ;  }

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
				&& ( this.sequenceOFrames.equals( that.sequenceOFrames )
					|| ( this.sequenceOFrames.size() <= 1 && that.sequenceOFrames.size() <= 1 ) )
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
		if ( this.sequenceOFrames.size () > 1 )
			for ( Integer frame : this.sequenceOFrames )
				theClone.sequenceOFrames.add( frame );
		else
			theClone.sequenceOFrames.add( 0 ); // it’s just single 0 for a static item

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
			text.append( "<mortal>yes</mortal>" );
			text.append( newline );
		}

		if ( getNameOfFramesFile().length() > 0 ) {
			text.append( indent );
			text.append( "<graphics file=\"" + getNameOfFramesFile () + "\">" );
			text.append( newline );

			text.append( indent ); text.append( indent );
			text.append( "<width-of-frame>" + getWidthOfFrame () + "</width-of-frame>" );
			text.append( newline );

			text.append( indent ); text.append( indent );
			text.append( "<height-of-frame>" + getHeightOfFrame () + "</height-of-frame>" );
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
			if /* item is not static */ ( howManyFramesPerOrientation () > 1 ) {
				text.append( indent );
				text.append( "<frames>" + howManyFramesPerOrientation () + "</frames>" );
				text.append( newline );
			}
		} else
			for ( Integer frame : this.sequenceOFrames ) {
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
