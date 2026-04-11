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
import head.over.heels.Pictures ;
import head.over.heels.PoolOfPictures ;
import head.over.heels.StringUtilities ;
import head.over.heels.UnlikelyToHappenException ;

import head.over.heels.rooms.RoomMaker ;


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

	// useful for doors in big rooms
	private String onWhichSideOfTheFour () {
		String side = this.onWhichSide ;

		if ( side.startsWith( "south" ) ) /* south, southeast, southwest */ return "south" ;
		if ( side.startsWith( "north" ) ) /* north, northeast, northwest */ return "north" ;
		if ( side.startsWith( "west"  ) )  /* west, westnorth, westsouth */ return "west"  ;
		if ( side.startsWith(  "east" ) )  /* east, eastnorth, eastsouth */ return  "east" ;

		throw new UnlikelyToHappenException( "and where’s the " + StringUtilities.putInQuotes( side ) + " side?" );
	}

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

		if ( ! on.startsWith( "south" ) && ! on.startsWith( "west" )
				&& ! on.startsWith( "north" ) && ! on.startsWith( "east" ) )
			throw new IllegalArgumentException( "mysterious side " + StringUtilities.putInQuotes( on ) );

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

	private static final int unlimited = 1 << 22 ;

	private int  leftLimit = unlimited ;
	private int rightLimit = unlimited ;

	// the three items that make up the door
	private FreeItem lintel = null ;
	private FreeItem leftJamb = null ;
	private FreeItem rightJamb = null ;

	public FreeItem getLeftJamb () {
		if ( this.leftJamb == null ) {
			ItemDescriptions descriptions = ItemDescriptions.descriptions() ;
			PoolOfPictures imagePool = PoolOfPictures.getRecentPool() ;

			DescriptionOfItem whatIsLeftJamb = descriptions.getDescriptionByKind( getKind() + "~leftjamb" );
			if ( whatIsLeftJamb == null )
				throw new UnlikelyToHappenException( "no description for the left jamb of " + getKind() );

			String doorImageName = whatIsLeftJamb.getNameOfFramesFile() ;
			String leftJambName = "left jamb of " + doorImageName ;

			if ( imagePool.getPicture( leftJambName ) == null ) {
				DescriptionOfItem whatIsLintel = descriptions.getDescriptionByKind( getKind() + "~lintel" );
				if ( whatIsLintel == null )
					throw new UnlikelyToHappenException( "no description for the lintel of " + getKind() );

				// cut out the left jamb
				NamedOffscreenImage leftJambCut = Door.cutOutLeftJamb( imagePool.getPicture( doorImageName ),
							whatIsLeftJamb.getWidthX(), whatIsLeftJamb.getWidthY(), whatIsLeftJamb.getHeight(),
							whatIsLintel.getWidthY(), whatIsLintel.getHeight(),
							getRoomSide() );

				imagePool.putPicture( leftJambName, leftJambCut );
			}

			NamedOffscreenImage leftJambImage = imagePool.getPicture( leftJambName );
			if ( leftJambImage == null )
				throw new UnlikelyToHappenException( "nil image for the left jamb of " + getKind() );

			int cellX = getCell().getX() ;
			int cellY = getCell().getY() ;
			int oneCell = getMediator().getRoom().getSizeOfOneCell() ;

			int x = 0 ;
			int y = 0 ;

			if ( getRoomSide().startsWith( "north" ) ) {
				x = cellX * oneCell + whatIsLeftJamb.getWidthX() - 2 ;
				y = ( cellY + 2 ) * oneCell - 2 ;
				this.leftLimit = y + whatIsLeftJamb.getWidthY() ;
			}
			else if ( getRoomSide().startsWith( "south" ) ) {
				x = cellX * oneCell ;
				y = ( cellY + 2 ) * oneCell - 2 ;
				this.leftLimit = y + whatIsLeftJamb.getWidthY() ;
			}
			else if ( getRoomSide().startsWith( "east" ) ) {
				x = cellX * oneCell ;
				y = ( cellY + 1 ) * oneCell - 1 ;
				this.leftLimit = x + whatIsLeftJamb.getWidthX() ;
			}
			else if ( getRoomSide().startsWith( "west" ) ) {
				x = cellX * oneCell ;
				y = ( cellY + 1 ) * oneCell - whatIsLeftJamb.getWidthY() + 1 ;
				this.leftLimit = x + whatIsLeftJamb.getWidthX() ;
			}

			FreeItem leftJamb = new FreeItem( whatIsLeftJamb, x, y, RoomMaker.floor_z, onWhichSideOfTheFour() );
			leftJamb.setUniqueName( leftJamb.getKind() + "." + StringUtilities.makeRandomString( 8 ) );
			leftJamb.addFrameTo( leftJamb.getHeading(), new NamedOffscreenImage( leftJambImage ) );
			leftJamb.setMediator( getMediator() );

			this.leftJamb = leftJamb ;
		}

		return this.leftJamb ;
	}

	public FreeItem getRightJamb () {
		if ( this.rightJamb == null ) {
			ItemDescriptions descriptions = ItemDescriptions.descriptions() ;
			PoolOfPictures imagePool = PoolOfPictures.getRecentPool() ;

			DescriptionOfItem whatIsRightJamb = descriptions.getDescriptionByKind( getKind() + "~rightjamb" );
			if ( whatIsRightJamb == null )
				throw new UnlikelyToHappenException( "no description for the right jamb of " + getKind() );

			String doorImageName = whatIsRightJamb.getNameOfFramesFile() ;
			String rightJambName = "right jamb of " + doorImageName ;

			if ( imagePool.getPicture( rightJambName ) == null ) {
				DescriptionOfItem whatIsLintel = descriptions.getDescriptionByKind( getKind() + "~lintel" );
				if ( whatIsLintel == null )
					throw new UnlikelyToHappenException( "no description for the lintel of " + getKind() );

				// cut out the right jamb
				NamedOffscreenImage rightJambCut = Door.cutOutRightJamb( imagePool.getPicture( doorImageName ),
							whatIsRightJamb.getWidthX(), whatIsRightJamb.getWidthY(), whatIsRightJamb.getHeight(),
							whatIsLintel.getWidthX(), whatIsLintel.getHeight(),
							getRoomSide() );

				imagePool.putPicture( rightJambName, rightJambCut );
			}

			NamedOffscreenImage rightJambImage = imagePool.getPicture( rightJambName );
			if ( rightJambImage == null )
				throw new UnlikelyToHappenException( "nil image for the right jamb of " + getKind() );

			int cellX = getCell().getX() ;
			int cellY = getCell().getY() ;
			int oneCell = getMediator().getRoom().getSizeOfOneCell() ;

			int x = 0 ;
			int y = 0 ;

			if ( getRoomSide().startsWith( "north" ) ) {
				x = cellX * oneCell + whatIsRightJamb.getWidthX() - 2 ;
				y = cellY * oneCell + whatIsRightJamb.getWidthY() - 1 ;
				this.rightLimit = y ;
			}
			else if ( getRoomSide().startsWith( "south" ) ) {
				x = cellX * oneCell ;
				y = cellY * oneCell + whatIsRightJamb.getWidthY() - 1 ;
				this.rightLimit = y ;
			}
			else if ( getRoomSide().startsWith( "east" ) ) {
				x = ( cellX + 2 ) * oneCell - whatIsRightJamb.getWidthX() - 2 ;
				y = ( cellY + 1 ) * oneCell - 1 ;
				this.rightLimit = x ;
			}
			else if ( getRoomSide().startsWith( "west" ) ) {
				x = ( cellX + 2 ) * oneCell - whatIsRightJamb.getWidthX() - 2 ;
				y = ( cellY + 1 ) * oneCell - whatIsRightJamb.getWidthY() + 1 ;
				this.rightLimit = x ;
			}

			FreeItem rightJamb = new FreeItem( whatIsRightJamb, x, y, RoomMaker.floor_z, onWhichSideOfTheFour() );
			rightJamb.setUniqueName( rightJamb.getKind() + "." + StringUtilities.makeRandomString( 8 ) );
			rightJamb.addFrameTo( rightJamb.getHeading(), new NamedOffscreenImage( rightJambImage ) );
			rightJamb.setMediator( getMediator() );

			this.rightJamb = rightJamb ;
		}

		return this.rightJamb ;
	}

	public FreeItem getLintel () {
		if ( this.lintel == null ) {
			ItemDescriptions descriptions = ItemDescriptions.descriptions() ;
			PoolOfPictures imagePool = PoolOfPictures.getRecentPool() ;

			DescriptionOfItem whatIsLintel = descriptions.getDescriptionByKind( getKind() + "~lintel" );
			if ( whatIsLintel == null )
				throw new UnlikelyToHappenException( "no description for the lintel of " + getKind() );

			String doorImageName = whatIsLintel.getNameOfFramesFile() ;
			String lintelName = "lintel of " + doorImageName ;

			if ( imagePool.getPicture( lintelName ) == null ) {
				DescriptionOfItem whatIsLeftJamb = descriptions.getDescriptionByKind( getKind() + "~leftjamb" );
				DescriptionOfItem whatIsRightJamb = descriptions.getDescriptionByKind( getKind() + "~rightjamb" );
				if ( whatIsLeftJamb == null || whatIsRightJamb == null )
					throw new UnlikelyToHappenException( "no description for the parts of " + getKind() );

				// cut out the lintel
				NamedOffscreenImage lintelCut = Door.cutOutLintel( imagePool.getPicture( doorImageName ),
							whatIsLintel.getWidthX(), whatIsLintel.getWidthY(), whatIsLintel.getHeight(),
							whatIsLeftJamb.getWidthX(), whatIsLeftJamb.getWidthY(),
							whatIsRightJamb.getWidthX(), whatIsRightJamb.getWidthY(),
							getRoomSide() );

				imagePool.putPicture( lintelName, lintelCut );
			}

			NamedOffscreenImage lintelImage = imagePool.getPicture( lintelName );
			if ( lintelImage == null )
				throw new UnlikelyToHappenException( "nil image for the lintel of " + getKind() );

			int cellX = getCell().getX() ;
			int cellY = getCell().getY() ;
			int oneCell = getMediator().getRoom().getSizeOfOneCell() ;

			int x = 0 ;
			int y = 0 ;

			if ( getRoomSide().startsWith( "north" ) ) {
				x = cellX * oneCell + whatIsLintel.getWidthX() - 2 ;
				y = ( cellY + 2 ) * oneCell - 1 ;
			}
			else if ( getRoomSide().startsWith( "south" ) ) {
				x = cellX * oneCell ;
				y = ( cellY + 2 ) * oneCell - 1 ;
			}
			else if ( getRoomSide().startsWith( "east" ) ) {
				x = cellX * oneCell ;
				y = ( cellY + 1 ) * oneCell - 1 ;
			}
			else if ( getRoomSide().startsWith( "west" ) ) {
				x = cellX * oneCell ;
				y = ( cellY + 1 ) * oneCell - whatIsLintel.getWidthY() + 1 ;
			}

			FreeItem lintel = new FreeItem( whatIsLintel, x, y, RoomMaker.floor_z, onWhichSideOfTheFour() );
			lintel.setUniqueName( lintel.getKind() + "." + StringUtilities.makeRandomString( 8 ) );
			lintel.addFrameTo( lintel.getHeading(), new NamedOffscreenImage( lintelImage ) );
			lintel.setMediator( getMediator() );

			this.lintel = lintel ;
		}

		return this.lintel ;
	}

	/**
	 * Get the image of lintel from the image of door
	 */
	private static NamedOffscreenImage cutOutLintel ( NamedOffscreenImage doorImage,
								int widthX, int widthY, int height,
								int leftJambWidthX, int leftJambWidthY,
								int rightJambWidthX, int rightJambWidthY,
								String on ) {
		int lintelWidth = ( widthX + widthY ) << 1  ;
		int lintelHeight = height + widthY + widthX ;

		NamedOffscreenImage lintel = new NamedOffscreenImage( lintelWidth, lintelHeight );

		if ( on.startsWith( "north" ) || on.startsWith( "south" ) ) {
			int copiedHeight = height + widthX ;
			lintel.copyThePartOf( doorImage, 0, 0, 0, 0, lintelWidth, copiedHeight );

			int noPixel = lintelWidth - ( ( rightJambWidthX + rightJambWidthY ) << 1 ) + 1 ;
			int yStart = noPixel ;
			int yEnd = noPixel - 1 ;

			int firstX = lintelWidth ;
			for ( int y = copiedHeight ; y < lintelHeight ; y ++ ) {
				for ( int x = firstX ; x > 0 ; x -- )
				{
					int xPixel = x - 1 ;
					if ( y != copiedHeight && xPixel == noPixel ) {
						if ( noPixel > yEnd ) noPixel -- ;
						else {
							yStart += 2 ;
							noPixel = yStart ;
						}
					}
					else if ( y < height + ( widthX << 1 ) || xPixel < yEnd )
						lintel.setPixelAt( xPixel, y, doorImage.getPixelAt( xPixel, y ) );
				}
				firstX -= 2 ;
			}
		}
		else {
			int copiedHeight = height + widthY ;
			lintel.copyThePartOf( doorImage, 0, 0, 0, 0, lintelWidth, copiedHeight );

			int noPixel = ( ( leftJambWidthX + leftJambWidthY ) << 1 ) - 2 ;
			int yStart = noPixel ;
			int yEnd = noPixel + 1 ;

			int firstX = 0 ;
			for ( int y = copiedHeight ; y < lintelHeight ; y ++ ) {
				for ( int x = firstX ; x < lintelWidth ; x ++ )
				{
					if ( y != copiedHeight && x == noPixel ) {
						if ( noPixel < yEnd ) noPixel ++ ;
						else {
							yStart -= 2 ;
							noPixel = yStart ;
						}
					}
					else if ( y < height + ( widthY << 1 ) || x > yEnd )
						lintel.setPixelAt( x, y, doorImage.getPixelAt( x, y ) );
				}
				firstX += 2 ;
			}
		}

		lintel.setName( "lintel of " + doorImage.getName() );
		return lintel ;
	}

	/**
	 * Get the image of left jamb from the image of door
	 */
	private static NamedOffscreenImage cutOutLeftJamb ( NamedOffscreenImage doorImage,
								int widthX, int widthY, int height,
								int lintelWidthY, int lintelHeight,
								String on ) {
		boolean ns = on.startsWith( "north" ) || on.startsWith( "south" ) ;
		int widthCorrection = ns ? 7 : 0 ;
		int yCorrection = ns ? -1 : 0 ;

		NamedOffscreenImage leftJamb = new NamedOffscreenImage(
							(( widthX + widthY ) << 1) + widthCorrection,
							height + widthY + widthX );
		leftJamb.copyThePartOf( doorImage,
					yCorrection, lintelHeight + lintelWidthY - widthY + yCorrection,
					0, 0,
					leftJamb.getWidth(), leftJamb.getHeight() );

		if ( Door.grayscale_jambs ) Pictures.toGrayscale( leftJamb );

		leftJamb.setName( "left jamb of " + doorImage.getName() );
		return leftJamb ;
	}

	/**
	 * Get the image of right jamb from the image of door
	 */
	private static NamedOffscreenImage cutOutRightJamb ( NamedOffscreenImage doorImage,
								int widthX, int widthY, int height,
								int lintelWidthX, int lintelHeight,
								String on ) {
		boolean ns = on.startsWith( "north" ) || on.startsWith( "south" ) ;
		int widthCorrection = ns ? 0 : 7 ;
		int yCorrection = ns ? 0 : -2 ;

		NamedOffscreenImage rightJamb = new NamedOffscreenImage(
							(( widthX + widthY ) << 1) + widthCorrection,
							height + widthY + widthX );
		rightJamb.copyThePartOf( doorImage,
					 doorImage.getWidth() - rightJamb.getWidth(), lintelHeight + lintelWidthX - widthY + yCorrection,
					 0, 0,
					 rightJamb.getWidth(), rightJamb.getHeight() );

		if ( Door.grayscale_jambs ) Pictures.toGrayscale( rightJamb );

		rightJamb.setName( "right jamb of " + doorImage.getName() );
		return rightJamb ;
	}

	private static final boolean grayscale_jambs = true ;

}
