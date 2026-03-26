// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2026 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels.items ;

import head.over.heels.Mediated ;
import head.over.heels.IntegerPoint2D ;
import head.over.heels.NamedOffscreenImage ;
import head.over.heels.PoolOfPictures ;
import head.over.heels.StringUtilities ;
import head.over.heels.UnlikelyToHappenException ;


/**
 * A door to the room. It is really the three free items, the two jambs and the lintel
 */

public class Door extends Mediated
{
	// the door item’s kind is %scenery%-door-%on%
	private final String kindOfDoor ;

	public String getKind () {  return this.kindOfDoor ;  }

	// the room’s grid cell where this door is located
	private final IntegerPoint2D cell ;

	public IntegerPoint2D getCell () {  return this.cell ;  }

	// how far is this door from the ground
	private final int elevation ;

	public int getElevation () {  return this.elevation ;  }

	// on which side of the room is this door located
	// for a single room, the sides are south, west, north or east
	private final String onWhichSide ;

	public String getRoomSide () {  return this.onWhichSide ;  }

	/**
	 * @param kind the kind of the door
	 * @param cell the grid cell of the door
	 * @param z the position on Z, that’s how far from the ground
	 * @param on the side of the room where the door is
	 */
	public Door( String kind, IntegerPoint2D cell, int z, String on )
	{
		if ( kind == null ) throw new IllegalArgumentException( "null kind" );
		if ( cell == null ) throw new IllegalArgumentException( "null cell" );
		if ( on == null ) throw new IllegalArgumentException( "null on" );

		if ( kind.indexOf( "door" ) < 0 ) throw new IllegalArgumentException( "not a door kind" );

		this.kindOfDoor = kind ;
		this.cell = cell ;
		this.elevation = z ;
		this.onWhichSide = on ;

		// make sure the door graphics is on hand

		DescriptionOfItem whatIsLintel = ItemDescriptions.descriptions().getDescriptionByKind( kind + "~lintel" );

		if ( whatIsLintel == null ) {
			String message = "no description for the parts of " + kind ;
			System.err.println( message );
			throw new UnlikelyToHappenException( message ) ;
		}

		String doorImageFile = whatIsLintel.getNameOfFramesFile() ;
		NamedOffscreenImage pictureOfDoor = PoolOfPictures.getRecentPool().getPicture( doorImageFile );

		if ( pictureOfDoor == null ) {
			System.out.println( "the door graphics " + StringUtilities.putInQuotes( doorImageFile ) + " is absent" );

			// make an image filled with the transparency grid
			pictureOfDoor = new NamedOffscreenImage( DescriptionOfDoor.WIDTH_OF_DOOR_IMAGE, DescriptionOfDoor.HEIGHT_OF_DOOR_IMAGE );
			pictureOfDoor.fillWithTransparencyGrid() ;
			pictureOfDoor.setName( "transparency grid for absent image " + doorImageFile );

			PoolOfPictures.getRecentPool().putPicture( doorImageFile, pictureOfDoor );
		}
	}

}
