// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2026 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels.items ;

import head.over.heels.Drawable ;
import head.over.heels.Mediated ;
import head.over.heels.IntegerPoint2D ;
import head.over.heels.NamedOffscreenImage ;
import head.over.heels.PoolOfPictures ;

import head.over.heels.rooms.Mediator ;
import head.over.heels.rooms.Room ;


/**
 * A segment of the room’s wall
 */

public class WallPiece extends Mediated implements Drawable
{
	// if true, then this piece is a segment of the wall along X, otherwise along Y
	private boolean alongX ;

	public boolean isAlongX () {  return   this.alongX ;  }
	public boolean isAlongY () {  return ! this.alongX ;  }

	// the position of this piece, the smaller the closer to the room’s origin
	private int position ;

	public int getPosition () {  return this.position ;  }

	// the name of the image file for this part of the wall
	private final String nameOfImageFile ;

	public String getNameOfImageFile () {  return this.nameOfImageFile ;  }

	private NamedOffscreenImage wallPieceImage ;

	// the offset of this wall piece’s graphics within the room image
	private IntegerPoint2D offset ;

	/**
	 * @param trueXfalseY is this a piece of the wall along X or not
	 * @param index where’s this piece on the wall, the number from zero onwards
	 * @param imageFile the name of file with graphics
	 */
	public WallPiece( boolean trueXfalseY, int index, String imageFile )
	{
		this.alongX = trueXfalseY ;
		this.position = index ;
		this.nameOfImageFile = imageFile ;
		this.offset = new IntegerPoint2D( 0, 0 );
	}

	/* @Override */
	public void setMediator( Mediator mediator ) {
		super.setMediator( mediator );
		this.calculateOffset() ;
	}

	/**
	 * Calculates the offset of this wall piece’s graphics
	 */
	private void calculateOffset ()
	{
		if ( super.getMediator() == null ) return ;

		Room room = super.getMediator().getRoom() ;
		int oneCell = room.getSizeOfOneCell() ;

		// ....
	}

	/**
	 * Draw this piece of wall
	 */
	public void draw ( java.awt.Graphics2D g )
	{
		NamedOffscreenImage image = PoolOfPictures.getRecentPool().getPicture( this.nameOfImageFile );
		if ( image != null )
			g.drawImage( image, this.offset.getX(), this.offset.getY(), null );
	}

}
