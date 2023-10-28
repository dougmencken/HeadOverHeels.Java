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

	public DescriptionOfDoor ( String doorLabel )
	{
		super( doorLabel );

		super.setNameOfPicturesFile( doorLabel + ".png" );
		super.setWidthOfFrame( WIDTH_OF_DOOR_IMAGE );
		super.setHeightOfFrame( HEIGHT_OF_DOOR_IMAGE );
		super.setHowManyOrientations( (byte) 1 );

		// the door's label is %scenery%-door-%at%
		int doorInLabel = doorLabel.indexOf( "door-" );
		if ( doorInLabel > 0 /* it is found and isn't at the very beginning */ )
		{
			this.scenery = doorLabel.substring( 0, doorInLabel - 1 );
			this.doorAt = doorLabel.substring( doorInLabel + 5 );
		}

		this.lintel = this.cloneAsLintelOfDoor() ;
		this.leftJamb = this.cloneAsLeftJambOfDoor() ;
		this.rightJamb = this.cloneAsRightJambOfDoor() ;
	}

	private DescriptionOfItem cloneAsLintelOfDoor()
	{
		DescriptionOfItem descriptionOfLintel = super.clone() ;
		descriptionOfLintel.setLabel( super.getLabel () + "~lintel" );
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
		descriptionOfLeftJamb.setLabel( super.getLabel () + "~leftjamb" );
		descriptionOfLeftJamb.markAsPartOfDoor() ;

		descriptionOfLeftJamb.setWidthX( 9 );
		descriptionOfLeftJamb.setWidthY( 9 );
		descriptionOfLeftJamb.setHeight( 48 );

		return descriptionOfLeftJamb ;
	}

	private DescriptionOfItem cloneAsRightJambOfDoor()
	{
		DescriptionOfItem descriptionOfRightJamb = super.clone() ;
		descriptionOfRightJamb.setLabel( super.getLabel () + "~rightjamb" );
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
		return "<door label=\"" + this.scenery + "-door-" + this.doorAt + "\"/>" ;
	}

}
