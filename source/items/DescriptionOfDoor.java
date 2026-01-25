// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2026 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels.items ;


/**
 * The description of a door
 *
 * For a door, the item kind is %scenery%-door-%on% with the image file %scenery%-door-%on%.png
 */

public class DescriptionOfDoor extends DescriptionOfItem
{
	// the three parts of door are the lintel, the left jamb and the right jamb
	private DescriptionOfItem lintel ;
	private DescriptionOfItem leftJamb ;
	private DescriptionOfItem rightJamb ;

	public DescriptionOfItem getLintel () {  return this.lintel ;  }
	public DescriptionOfItem getLeftJamb () {  return this.leftJamb ;  }
	public DescriptionOfItem getRightJamb () {  return this.rightJamb ;  }

	private final String scenery ;

	public String getScenery () {  return this.scenery ;  }

	private final String doorOn ;

	public String getWhereOn () {  return this.doorOn ;  }

	public static final int  WIDTH_OF_DOOR_IMAGE = 82 ;
	public static final int HEIGHT_OF_DOOR_IMAGE = 140 ;

	public DescriptionOfDoor ( String sceneryOfDoor, String where )
	{
		super( /* door item’s kind is %scenery%-door-%on% */ sceneryOfDoor + "-door-" + where );

		this.scenery = sceneryOfDoor ;
		this.doorOn = where ;

		super.setNameOfFramesFile( super.getKind() + ".png" );
		super.setWidthOfFrame( DescriptionOfDoor.WIDTH_OF_DOOR_IMAGE );
		super.setHeightOfFrame( DescriptionOfDoor.HEIGHT_OF_DOOR_IMAGE );
		super.setHowManyOrientations( (byte) 1 );

		this.lintel = this.cloneAsLintel() ;
		this.leftJamb = this.cloneAsLeftJamb() ;
		this.rightJamb = this.cloneAsRightJamb() ;
	}

	private DescriptionOfItem cloneAsLintel()
	{
		DescriptionOfItem descriptionOfLintel = super.clone() ;
		descriptionOfLintel.setKind( super.getKind () + "~lintel" );
		descriptionOfLintel.markAsPartOfDoor () ;

		final int lintelSmallerWidth = 9 ;
		final int lintelBroaderWidth = 32 ;
		if ( this.doorOn.equals( "north" ) || this.doorOn.equals( "south" ) ) {
			descriptionOfLintel.setWidthX( lintelSmallerWidth );
			descriptionOfLintel.setWidthY( lintelBroaderWidth );
		} else
		   if ( this.doorOn.equals( "west" ) || this.doorOn.equals( "east" ) ) {
			descriptionOfLintel.setWidthX( lintelBroaderWidth );
			descriptionOfLintel.setWidthY( lintelSmallerWidth );
		}
		descriptionOfLintel.setHeight( 51 );

		return descriptionOfLintel ;
	}

	private DescriptionOfItem cloneAsLeftJamb()
	{
		DescriptionOfItem descriptionOfLeftJamb = super.clone() ;
		descriptionOfLeftJamb.setKind( super.getKind () + "~leftjamb" );
		descriptionOfLeftJamb.markAsPartOfDoor() ;

		descriptionOfLeftJamb.setWidthX( 9 );
		descriptionOfLeftJamb.setWidthY( 9 );
		descriptionOfLeftJamb.setHeight( 48 );

		return descriptionOfLeftJamb ;
	}

	private DescriptionOfItem cloneAsRightJamb()
	{
		DescriptionOfItem descriptionOfRightJamb = super.clone() ;
		descriptionOfRightJamb.setKind( super.getKind () + "~rightjamb" );
		descriptionOfRightJamb.markAsPartOfDoor() ;

		descriptionOfRightJamb.setWidthX( 9 );
		descriptionOfRightJamb.setWidthY( 9 );
		descriptionOfRightJamb.setHeight( 48 );

		return descriptionOfRightJamb ;
	}

	public boolean equals( DescriptionOfDoor that )
	{
		return this.scenery.equals( that.scenery ) && this.doorOn.equals( that.doorOn ) ;
				/* && this.leftJamb.equals( that.leftJamb ) && this.rightJamb.equals( that.rightJamb )
					&& this.lintel.equals( that.lintel ) ; */
	}

	public String toString ()
	{
		return "<door on=\"" + this.doorOn + "\" scenery=\"" + this.scenery + "\"/>" ;
	}

}
