// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2026 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels.rooms ;

import java.awt.Color ;

import java.util.Map ;

import head.over.heels.Colours ;
import head.over.heels.Drawable ;
import head.over.heels.IntegerPoint2D ;
import head.over.heels.IntegerSize2D ;
import head.over.heels.NamedOffscreenImage ;

import head.over.heels.items.Door ;


/**
 * A miniature of game’s room
 */

public class Miniature implements Drawable
{

	public Miniature( Room roomForMiniature )
		{  this( roomForMiniature, Miniature.the_default_square_size ) ;  }

	public Miniature( Room roomForMiniature, byte sizeOfSquare )
	{
		this.theRoom = roomForMiniature ;
		setSquareSize( sizeOfSquare );
		setDrawingOffset( 0, 0 );
	}

	private NamedOffscreenImage theImage = null ;

	private Room theRoom ;

	public Room getRoom () {  return this.theRoom ;  }

	public IntegerPoint2D getOriginOfRoom ()
	{
		return new IntegerPoint2D( getRoom().getCellsAlongY() * ( getSquareSize() << 1 ), 0 ) ;
	}

	// the size of a single square, in pixels
	private byte squareSize ; // 2 .. 16

	public static final byte the_default_square_size = 3 ;

	public byte getSquareSize () {  return this.squareSize ;  }

	public void setSquareSize ( byte newSize )
	{
		     if ( newSize < 2 ) newSize = 2 ;
		else if ( newSize > 16 ) newSize = 16 ;

		if ( newSize != this.squareSize ) {
			this.squareSize = newSize ;
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

	protected IntegerSize2D calculateSize ()
	{
		int height = ( getRoom().getCellsAlongX() + getRoom().getCellsAlongY() ) * getSquareSize() ;
		int width = height << 1 ;

		return new IntegerSize2D( width, height ) ;
	}

	protected void composeImage ()
	{
		if ( this.theImage == null ) {
			this.theImage = new NamedOffscreenImage( calculateSize() );
			this.theImage.setName( "Miniature of room " + getRoom().getNameOfRoomDescriptionFile()
						+ " with " + getSquareSize() + "-pixel squares" );
		}

		IntegerSize2D roomCells = getRoom().getSizeInCells() ;

		int firstCellX = 0 ;
		int firstCellY = 0 ;
		int lastCellX = roomCells.getWidthX() - 1 ;
		int lastCellY = roomCells.getWidthY() - 1 ;

		Map< String, Door > doors = new java.util.HashMap< String, Door >() ;

		String [] sides = { "south", "west", "north", "east" } ;
		for ( String side : sides )
			doors.put( side, getRoom().getDoorOn( side ) );

		String [] bigroomsides = { "northeast", "northwest", "eastnorth", "eastsouth",
						"southeast", "southwest", "westnorth", "westsouth" };
		for ( String side : bigroomsides )
			doors.put( side, getRoom().getDoorOn( side ) );

		if ( doors.get( "north" ) != null || doors.get( "northeast" ) != null || doors.get( "northwest" ) != null )
			firstCellX ++ ;

		if ( doors.get( "east" ) != null || doors.get( "eastnorth" ) != null || doors.get( "eastsouth" ) != null )
			firstCellY ++ ;

		if ( doors.get( "south" ) != null || doors.get( "southeast" ) != null || doors.get( "southwest" ) != null )
			-- lastCellX ;

		if ( doors.get( "west" ) != null || doors.get( "westnorth" ) != null || doors.get( "westsouth" ) != null )
			-- lastCellY ;

		boolean narrowRoomAlongX = ( lastCellY == firstCellY + 1 ) ;
		boolean narrowRoomAlongY = ( lastCellX == firstCellX + 1 ) ;

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
