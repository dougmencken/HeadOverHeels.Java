// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2023 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels ;

import java.util.Vector ;


/**
 * The description of an item as read from items.xml
 */

public final class DescriptionOfItem
{

	private String label ;

	public String getLabel () {  return label ;  }

	/**
	 * The three spatial dimensions of the item, along the x, along the y, and height along the z
	 */
	private int widthX = 0 ;
	private int widthY = 0 ;
	private int height = 0 ;

	public int getWidthX () {  return widthX ;  }
	public void setWidthX ( int wx ) {  this.widthX = wx ;  }

	public int getWidthY () {  return widthY ;  }
	public void setWidthY ( int wy ) {  this.widthY = wy ;  }

	public int getHeight () {  return height ;  }
	public void setHeight ( int wz ) {  this.height = wz ;  }

	/**
	 * The weight of the item in seconds, higher for the bigger speed of falling, zero for no gravity (no falling)
	 */
	private double weight = 0.0 ;

	public double getWeight () {  return weight ;  }
	public void setWeight( double newWeight ) {  this.weight = newWeight ;  }

	/**
	 * The time in seconds the item takes to move one single isometric unit
	 */
	private double speed = 0.0 ;

	public double getSpeed () {  return speed ;  }
	public void setSpeed( double newSpeed ) {  this.speed = newSpeed ;  }

	/**
	 * When true, this item takes one life from the character on touch
	 */
	private boolean mortal = false ;

	public boolean isMortal() {  return mortal ;  }
	public void setMortal( boolean newMortal ) {  this.mortal = newMortal ;  }

	/**
	 * 1 if the item is the same on all sides, thus there’s only one orientation,
	 * 2 if there’re different images for south and west, or
	 * 4 when there’re different images for each orientation
	 */
	private byte orientations = 0 ;

	public byte howManyOrientations () {  return orientations ;  }
	public void setHowManyOrientations( byte newOrientations ) {  this.orientations = newOrientations ;  }

	/**
	 * The delay, in seconds, between frames in the animation sequence
	 */
	private double delayBetweenFrames = 0.0 ;

	public double getDelayBetweenFrames () {  return delayBetweenFrames ;  }
	public void setDelayBetweenFrames( double newDelay ) {  this.delayBetweenFrames = newDelay ;  }

	/**
	 * Width and height in pixels of each item’s frame
	 */
	private int widthOfFrame  = 0 ;
	private int heightOfFrame = 0 ;

	public int getWidthOfFrame () {  return widthOfFrame ;  }
	public void setWidthOfFrame( int newWidthOfFrame ) {  this.widthOfFrame = newWidthOfFrame ;  }

	public int getHeightOfFrame () {  return heightOfFrame ;  }
	public void setHeightOfFrame( int newHeightOfFrame ) {  this.heightOfFrame = newHeightOfFrame ;  }

	/**
	 * Width and height in pixels of each frame for the item’s shadow
	 */
	private int widthOfShadow  = 0 ;
	private int heightOfShadow = 0 ;

	public int getWidthOfShadow () {  return widthOfShadow ;  }
	public void setWidthOfShadow( int newWidthOfShadow ) {  this.widthOfShadow = newWidthOfShadow ;  }

	public int getHeightOfShadow () {  return heightOfShadow ;  }
	public void setHeightOfShadow( int newHeightOfShadow ) {  this.heightOfShadow = newHeightOfShadow ;  }

	/**
	 * Extra frames such as for jumping
	 */
	private short extraFrames = 0 ;

	public short howManyExtraFrames () {  return extraFrames ;  }
	public void setHowManyExtraFrames( short newExtraFrames ) {  this.extraFrames = newExtraFrames ;  }

	/**
	 * The sequence of item's frames for one orientation
	 */
	private Vector< Integer > sequenceOFrames = new Vector< Integer >() ;

	public int howManyFramesPerOrientation () {
		return ( sequenceOFrames != null ) ? sequenceOFrames.size () : 0 ;  }

	/**
	 * The file with images for this item
	 */
	private String nameOfFile = "" ;

	/**
	 * The file with shadows for this item
	 */
	private String nameOfShadowFile = "" ;

	private boolean partOfDoor = false ;

	public boolean isPartOfDoor () {  return partOfDoor ;  }

	/*
	 * Create new description of items with this label
	 */
	public DescriptionOfItem ( String itemLabel )
	{
		this.label = itemLabel ;
	}

	public String toString ()
	{
		StringBuilder text = new StringBuilder( );
		String newline = System.getProperty( "line.separator" );
		String indent = "    " ;

		text.append( "<item label=\"" + getLabel () + "\">" );
		text.append( newline );

		text.append( indent );
		text.append( "<widthX>" + getWidthX () + "</widthX>" );
		text.append( newline );

		text.append( indent );
		text.append( "<widthY>" + getWidthY () + "</widthY>" );
		text.append( newline );

		text.append( indent );
		text.append( "<height>" + getHeight () + "</height>" );
		text.append( newline );

	/* ............ */

		text.append( "</item>" );

		return text.toString() ;
	}

}
