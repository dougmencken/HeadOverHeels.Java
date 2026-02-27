// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2026 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels.items ;

import head.over.heels.Drawable ;
import head.over.heels.ShadyMediated ;

import head.over.heels.IntegerPoint2D ;
import head.over.heels.NamedOffscreenImage ;

import head.over.heels.rooms.Mediator ;
import head.over.heels.rooms.Room ;


/**
 * A set of such tiles forms the floor of the room
 */

public class FloorTile extends ShadyMediated implements Drawable, Comparable
{
	/**
	 * @param cell the grid cell where the tile is
	 * @param graphicsOfTile the picture of the tile
	 */
	public FloorTile( IntegerPoint2D cell, NamedOffscreenImage graphicsOfTile )
	{
		if ( cell == null ) throw new IllegalArgumentException( "null cell" );
		this.cell = cell ;

		if ( graphicsOfTile == null ) throw new IllegalArgumentException( "null floor tile graphics" );
		this.rawImage = graphicsOfTile ;

		this.refreshShadedImage() ;
	}

	// the room’s grid cell where this tile is located
	private final IntegerPoint2D cell ;

	public IntegerPoint2D getCell () {  return this.cell ;  }

	/* @Override */
	public boolean equals ( Object that ) {
		return ( that instanceof FloorTile ) ? this.equals( (FloorTile) that ) : false ;
	}
	public boolean equals ( FloorTile that ) {
		if ( this == that ) return true ;
		return this.getCell().equals( that.getCell() );
	}

	private transient Integer hash = null ;

	/* @Override */
	public int hashCode () {
		if ( hash == null ) {
			final int seed = 359 ;
			int hash = ( seed << 5 ) - seed + this.getCell().getX() ; // 31×359 + cellX
			hash = ( hash << 5 ) - hash + this.getCell().getY() ; // 31 × ( 31×359 + cellX ) + cellY
			this.hash = Integer.valueOf( hash );
		}
		return this.hash.intValue() ;
	}

	// implementing Comparable
	public int compareTo ( Object that ) {
		if ( that instanceof FloorTile )
			return this.compareTo( (FloorTile) that );
		else
			throw new ClassCastException( "can’t compare " + this.getClass().getName() + " with " + that.getClass().getName() );
	}
	public int compareTo ( FloorTile that )
	{
		int y2y = Integer.valueOf( this.getCell().getY() ).compareTo( Integer.valueOf( that.getCell().getY() ) );
		if ( y2y != 0 ) return y2y ;
		return Integer.valueOf( this.getCell().getX() ).compareTo( Integer.valueOf( that.getCell().getX() ) );
	}

	// picture of the tile
	private final NamedOffscreenImage rawImage ;

	// picture of the shaded tile
	private NamedOffscreenImage shadedImage ;

	public NamedOffscreenImage getShadedImage () {  return this.shadedImage ;  }

	public void setShadedImage ( NamedOffscreenImage shaded ) {
		super.setWantShadow( false );

		this.shadedImage = shaded ; // just ‘=’ without copying the graphics via shadedImage.replicateImage( shaded )
		this.shadedImage.setName( "shaded " + this.rawImage.getName() );
	}

	public void refreshShadedImage () {
		if ( this.shadedImage == null )
			this.shadedImage = new NamedOffscreenImage( this.rawImage.getSize() );

		if ( super.getWantShadow() || this.shadedImage.getName().startsWith( "fresh copy" ) )
			return ; // is fresh already or is in the process of shading

		this.shadedImage.replicateImage( this.rawImage ); // copy the graphics
		this.shadedImage.setName( "fresh copy of " + this.rawImage.getName() );

		super.setWantShadow( true );
	}

	/* @Override */
	public void setMediator( Mediator mediator ) {
		super.setMediator( mediator );
		this.calculateOffset() ;
	}

	// the offset of this floor tile’s graphics within the room image
	private IntegerPoint2D offset = null ;

	private void calculateOffset ()
	{
		if ( super.getMediator() == null ) return ;

		Room room = super.getMediator().getRoom() ;
		int oneCell = room.getSizeOfOneCell() ;

		int offsetX = ( ( oneCell * ( getCell().getX() - getCell().getY() - 1 ) ) << 1 ) + 1 ;
		int offsetY = oneCell * ( getCell().getX() + getCell().getY() ) ;
		this.offset = new IntegerPoint2D( room.getOrigin().getX() + offsetX, room.getOrigin().getY() + offsetY );
	}

	/**
	 * Draw this tile o’ floor
	 */
	public void draw ( java.awt.Graphics2D g )
	{
		if ( this.shadedImage != null && this.offset != null )
			g.drawImage( this.shadedImage, this.offset.getX(), this.offset.getY(), null );
	}

}
