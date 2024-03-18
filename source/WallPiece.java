// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2024 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels ;


public class WallPiece extends Mediated implements Drawable
{
	// if true, then this piece is part of the wall along X, otherwise along Y
	private boolean alongX ;

	public boolean isAlongX () {  return   this.alongX ;  }
	public boolean isAlongY () {  return ! this.alongX ;  }

	// the position of this piece on the wall, the smaller the closer to the room’s origin
	public int position ;

	public int getPosition () {  return this.position ;  }

	// the graphics of this piece of the wall
	private final OffscreenImage image ;

	/**
	 * @param trueXfalseY is this a piece of the wall along X or not
	 * @param index where’s this piece on the wall, the number from zero onwards
	 * @param graphicsOfPiece the picture of the piece
	 */
	public WallPiece( boolean trueXfalseY, int index, OffscreenImage graphicsOfPiece )
	{
		this.alongX = trueXfalseY ;
		this.position = index ;
		this.image = graphicsOfPiece ;
	}

	/**
	 * Draw this piece of wall
	 */
	public void draw ( java.awt.Graphics2D g )
	{
		// ...
	}

}
