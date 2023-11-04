// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2023 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels ;


/**
 * The description of a door
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

	private String scenery = "none" ;

	public String getScenery () {  return this.scenery ;  }

	private String doorAt = "void" ;

	public String getWhereIsDoor () {  return this.doorAt ;  }

	public static final int  WIDTH_OF_DOOR_IMAGE = 82 ;
	public static final int HEIGHT_OF_DOOR_IMAGE = 140 ;

	public DescriptionOfDoor ( String sceneryOfDoor, String where )
	{
		super( /* item's kind is %scenery%-door-%at% */ sceneryOfDoor + "-door-" + where );

		this.scenery = sceneryOfDoor ;
		this.doorAt = where ;

		super.setNameOfPicturesFile( super.getKind() + ".png" );
		super.setWidthOfFrame( WIDTH_OF_DOOR_IMAGE );
		super.setHeightOfFrame( HEIGHT_OF_DOOR_IMAGE );
		super.setHowManyOrientations( (byte) 1 );

		this.lintel = this.cloneAsLintelOfDoor() ;
		this.leftJamb = this.cloneAsLeftJambOfDoor() ;
		this.rightJamb = this.cloneAsRightJambOfDoor() ;
	}

	private DescriptionOfItem cloneAsLintelOfDoor()
	{
		DescriptionOfItem descriptionOfLintel = super.clone() ;
		descriptionOfLintel.setKind( super.getKind () + "~lintel" );
		descriptionOfLintel.markAsPartOfDoor () ;

		final int lintelSmallerWidth = 9 ;
		final int lintelBroaderWidth = 32 ;
		if ( this.doorAt.equals( "north" ) || this.doorAt.equals( "south" ) ) {
			descriptionOfLintel.setWidthX( lintelSmallerWidth );
			descriptionOfLintel.setWidthY( lintelBroaderWidth );
		} else
		   if ( this.doorAt.equals( "west" ) || this.doorAt.equals( "east" ) ) {
			descriptionOfLintel.setWidthX( lintelBroaderWidth );
			descriptionOfLintel.setWidthY( lintelSmallerWidth );
		}
		descriptionOfLintel.setHeight( 51 );

		return descriptionOfLintel ;
	}

	private DescriptionOfItem cloneAsLeftJambOfDoor()
	{
		DescriptionOfItem descriptionOfLeftJamb = super.clone() ;
		descriptionOfLeftJamb.setKind( super.getKind () + "~leftjamb" );
		descriptionOfLeftJamb.markAsPartOfDoor() ;

		descriptionOfLeftJamb.setWidthX( 9 );
		descriptionOfLeftJamb.setWidthY( 9 );
		descriptionOfLeftJamb.setHeight( 48 );

		return descriptionOfLeftJamb ;
	}

	private DescriptionOfItem cloneAsRightJambOfDoor()
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
		return this.scenery.equals( that.scenery ) && this.doorAt.equals( that.doorAt ) ;
				/* && this.leftJamb.equals( that.leftJamb ) && this.rightJamb.equals( that.rightJamb )
					&& this.lintel.equals( that.lintel ) ; */
	}

	public String toString ()
	{
		return "<door at=\"" + this.doorAt + "\" scenery=\"" + this.scenery + "\"/>" ;
	}

}
