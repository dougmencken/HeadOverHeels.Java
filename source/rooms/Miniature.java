// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2025 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels.rooms ;

import java.awt.Color ;

import head.over.heels.Colours ;
import head.over.heels.Drawable ;
import head.over.heels.IntegerPoint2D ;
import head.over.heels.IntegerDimensions2D ;
import head.over.heels.NamedOffscreenImage ;

import head.over.heels.items.Door ;


/**
 * A miniature of game’s room
 */

public class Miniature implements Drawable
{

	public Miniature( Room roomForMiniature )
		{  this( roomForMiniature, Miniature.the_default_size_of_tile, 0, 0 ) ;  }

	public Miniature( Room roomForMiniature, int leftX, int topY )
		{  this( roomForMiniature, Miniature.the_default_size_of_tile, leftX, topY ) ;  }

	public Miniature( Room roomForMiniature, byte singleTileSize, int leftX, int topY )
	{
		this.theRoom = roomForMiniature ;
		setSizeOfTile( singleTileSize );
		setDrawingOffset( leftX, topY );
	}

	private NamedOffscreenImage theImage = null ;

	private Room theRoom ;

	public Room getRoom () {  return this.theRoom ;  }

	public IntegerPoint2D getOriginOfRoom ()
	{
		return new IntegerPoint2D( getRoom().getTilesOnY() * ( getSizeOfTile() << 1 ), 0 ) ;
	}

	private byte sizeOfTile ; // 2 .. 16

	public static final byte the_default_size_of_tile = 3 ;

	public byte getSizeOfTile () {  return this.sizeOfTile ;  }

	public void setSizeOfTile ( byte newSize )
	{
		     if ( newSize < 2 ) newSize = 2 ;
		else if ( newSize > 16 ) newSize = 16 ;

		if ( newSize != this.sizeOfTile ) {
			this.sizeOfTile = newSize ;
			binTheImage() ;
		}
	}

	private IntegerPoint2D drawingOffset ;

	public IntegerPoint2D getDrawingOffset () {  return this.drawingOffset ;  }

	public void setDrawingOffset ( IntegerPoint2D newOffset ) {  this.drawingOffset = newOffset ;  }
	public void setDrawingOffset ( int offsetX, int offsetY ) {  setDrawingOffset( new IntegerPoint2D( offsetX, offsetY ) ) ;  }

	private IntegerPoint2D northDoorEasternCorner = null ;
	private IntegerPoint2D eastDoorNorthernCorner = null ;
	private IntegerPoint2D southDoorEasternCorner = null ;
	private IntegerPoint2D westDoorNorthernCorner = null ;

	protected IntegerDimensions2D calculateSize ()
	{
		int tilesX = getRoom().getTilesOnX ();
		int tilesY = getRoom().getTilesOnY ();

		int height = ( tilesX + tilesY ) * getSizeOfTile() ;
		int width = height << 1 ;

		return new IntegerDimensions2D( width, height ) ;
	}

	protected void composeImage ()
	{
		if ( this.theImage == null ) {
			this.theImage = new NamedOffscreenImage( calculateSize() );
			this.theImage.setName( "Miniature of room " + getRoom().getNameOfRoomDescriptionFile()
						+ " with " + getSizeOfTile() + " pixel long tiles" );
		}

		int tilesX = getRoom().getTilesOnX ();
		int tilesY = getRoom().getTilesOnY ();
		int firstTileX = 0 ;
		int firstTileY = 0 ;
		int lastTileX = tilesX - 1 ;
		int lastTileY = tilesY - 1 ;

		Door eastDoor = getRoom().getDoorOn( "east" );
		Door southDoor = getRoom().getDoorOn( "south" );
		Door northDoor = getRoom().getDoorOn( "north" );
		Door westDoor = getRoom().getDoorOn( "west" );

		Door eastnorthDoor = getRoom().getDoorOn( "eastnorth" );
		Door eastsouthDoor = getRoom().getDoorOn( "eastsouth" );
		Door southeastDoor = getRoom().getDoorOn( "southeast" );
		Door southwestDoor = getRoom().getDoorOn( "southwest" );
		Door northeastDoor = getRoom().getDoorOn( "northeast" );
		Door northwestDoor = getRoom().getDoorOn( "northwest" );
		Door westnorthDoor = getRoom().getDoorOn( "westnorth" );
		Door westsouthDoor = getRoom().getDoorOn( "westsouth" );

		if ( northDoor != null || northeastDoor != null || northwestDoor != null )
			firstTileX ++ ;

		if ( eastDoor != null || eastnorthDoor != null || eastsouthDoor != null )
			firstTileY ++ ;

		if ( southDoor != null || southeastDoor != null || southwestDoor != null )
			-- lastTileX ;

		if ( westDoor != null || westnorthDoor != null || westsouthDoor != null )
			-- lastTileY ;

		boolean narrowRoomAlongX = ( lastTileY == firstTileY + 1 ) ;
		boolean narrowRoomAlongY = ( lastTileX == firstTileX + 1 ) ;

		final Color roomColor = Colours.byName( getRoom().getColour () );

		// .....
	}

	public void draw ( java.awt.Graphics2D g )
	{
		if ( this.theImage == null ) composeImage() ;

		// draw the image of miniature
		g.drawImage( this.theImage, this.drawingOffset.getX(), this.drawingOffset.getY(), null );

		// ....
	}

	private void binTheImage () {  this.theImage = null ;  }

}
