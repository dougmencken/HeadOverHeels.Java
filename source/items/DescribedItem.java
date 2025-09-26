// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2025 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels.items ;

import head.over.heels.NamedOffscreenImage ;
import head.over.heels.Pictures ;
import head.over.heels.PoolOfPictures ;
import head.over.heels.Shady ;
import head.over.heels.StringUtilities ;

import head.over.heels.NoSuchPictureException ;
import head.over.heels.UnlikelyToHappenException ;

import java.util.Map ;
import java.util.Vector ;

import java.awt.image.BufferedImage ;


public abstract class DescribedItem extends TheMostAbstractItem implements Shady
{

	// creates an item by description
	protected DescribedItem( DescriptionOfItem description )
	{
		super() ;

		if ( description == null ) throw new NullPointerException( "null description at the time of item construction" ) ;

		this.descriptionOfItem = description ;
		this.originalKind = description.getKind() ;

		super.setUniqueName( this.originalKind + "." + StringUtilities.makeRandomString( 12 ) );

		this.readGraphicsOfItem() ;
	}

	// the copy constructor
	protected DescribedItem( DescribedItem thatItem )
	{
		super( thatItem );

		this.descriptionOfItem = thatItem.getDescriptionOfItem() ;
		this.originalKind = thatItem.getOriginalKind() ;

		super.setUniqueName( "copy of " + thatItem.getUniqueName() );

		if ( thatItem.shadows == null )
			this.shadows = null ;
		else
			for ( String sequence : thatItem.shadows.keySet() )
				for ( NamedOffscreenImage shadow : thatItem.shadows.get( sequence ) )
					addShadowTo( sequence, new NamedOffscreenImage( shadow /* copy */ ) );
	}

	private DescriptionOfItem descriptionOfItem ;

	public DescriptionOfItem getDescriptionOfItem () {  return this.descriptionOfItem ;  }

	/**
	 * The three spatial dimensions (widths) of the item, along the x, along the y, and height along the z
	 */
	public int getWidthX () {  return this.descriptionOfItem.getWidthX() ;  }
	public int getWidthY () {  return this.descriptionOfItem.getWidthY() ;  }
	public int getHeight () {  return this.descriptionOfItem.getHeight() ;  }

	public String getKind () {  return this.descriptionOfItem.getKind() ;  }

	/**
	 * The original kind of item, while the current kind may change via metamorphosis
	 */
	private String originalKind ;

	public String getOriginalKind () {  return this.originalKind ;  }

	// the sequences of pictures of item’s shadow
	private Map< String, Vector< NamedOffscreenImage > > shadows = null ;

	public boolean hasShadow () {  return this.shadows != null && ! this.shadows.isEmpty() ;  }

	protected NamedOffscreenImage getNthShadowIn ( String sequence, int n ) throws NoSuchPictureException
	{
		throw new NoSuchPictureException() ;
	}

	public void addShadowTo ( String sequence, NamedOffscreenImage shadow )
	{
		if ( sequence == null || sequence.isEmpty() ) return ; // don’t add to ""

		if ( this.shadows.get( sequence ) == null )
			this.shadows.put( sequence, new Vector< NamedOffscreenImage >() );

		this.shadows.get( sequence ).add( shadow );
	}

	@Override
	protected void clearFrames ()
	{
		super.clearFrames() ;
		if ( this.shadows != null ) this.shadows.clear() ;
        }

	// the position in 3-dimensional space of this item’s lower north-west point, in free units
	public abstract int getX () ;
	public abstract int getY () ;
	public abstract int getZ () ;

	public abstract void setX ( int newX ) ;
	public abstract void setY ( int newY ) ;
	public abstract void setZ ( int newZ ) ;

	public boolean overlapsWith ( DescribedItem anotherItem )
	{
		return ( this.getX() < anotherItem.getX() + anotherItem.getWidthX() )
					&& ( anotherItem.getX() < this.getX() + this.getWidthX() )
			&& ( this.getY() > anotherItem.getY() - anotherItem.getWidthY() )
					&& ( anotherItem.getY() > this.getY() - this.getWidthY() )
			&& ( this.getZ() < anotherItem.getZ() + anotherItem.getHeight() )
					&& ( anotherItem.getZ() < this.getZ() + this.getHeight() ) ;
	}

	// whether to ignore that this item collides with something
	private boolean ignoreCollisions = false ;

	public void setIgnoreCollisions ( boolean ignore ) {  this.ignoreCollisions = ignore ;  }

	public boolean isIgnoringCollisions () {  return this.ignoreCollisions ;  }
	public boolean isNotIgnoringCollisions () {  return ! this.ignoreCollisions ;  }

	// redo the shadow or not
	private boolean wantShadow = false ;

        // implementing Shady
	public boolean getWantShadow () {  return this.wantShadow ;  }
	public void setWantShadow ( boolean wanna ) {  this.wantShadow = wanna ;  }

	private void readGraphicsOfItem ()
	{
		this.clearFrames() ;

		DescriptionOfItem description = getDescriptionOfItem() ;

		if ( ! description.isPartOfDoor() && ! description.getNameOfPicturesFile().isEmpty() ) {
			this.makeFrames() ;

			if ( description.getWidthOfShadow() > 0 && description.getHeightOfShadow() > 0 )
				this.makeShadowFrames() ;
		}
	}

	private void makeFrames ()
	{
		DescriptionOfItem description = getDescriptionOfItem() ;

		final int frameWidth = description.getWidthOfFrame() ;
		final int frameHeight = description.getHeightOfFrame() ;
		final String picturesFile = description.getNameOfPicturesFile() ;

		if ( frameWidth == 0 || frameHeight == 0 )
			throw new UnlikelyToHappenException( "zero frame width or height for item " + StringUtilities.putInQuotes( getKind() ) );
		if ( picturesFile.isEmpty() )
			throw new UnlikelyToHappenException( "empty graphics file name for item " + StringUtilities.putInQuotes( getKind() ) );

		NamedOffscreenImage allTheFrames = PoolOfPictures.getRecentPool().getPicture( picturesFile );

		if ( allTheFrames == null ) {
		// suddenly there’s no such image file
		// then make a fresh image with the dimensions ??of a single frame??? and filled with the transparency grid
			allTheFrames = new NamedOffscreenImage( frameWidth, frameHeight );
			allTheFrames.fillWithTransparencyGrid() ;
			allTheFrames.setName( "transparency grid for absent image " + picturesFile );
			PoolOfPictures.getRecentPool().putPicture( picturesFile, allTheFrames );
		}

		// decompose the image into frames

		Vector< BufferedImage > rawFrames = new Vector< BufferedImage >() ;

		for ( int y = 0; y < allTheFrames.getHeight() ; y += frameHeight ) {
			for ( int x = 0; x < allTheFrames.getWidth() ; x += frameWidth )
			{
				BufferedImage rawFrame = Pictures.cloneSubpictureAsARGB( allTheFrames, x, y, frameWidth, frameHeight );
				rawFrames.add( rawFrame );
			}
		}

		// split frames by orientations

		int howManyOrientations = description.howManyOrientations() ;
		Vector< String > orientations = whatOrientations() ;

		int howManyFramesWithoutExtra = rawFrames.size() - description.howManyExtraFrames() ;
		if ( howManyFramesWithoutExtra % howManyOrientations != 0 )
			throw new UnlikelyToHappenException( "item " + StringUtilities.putInQuotes( getKind() )
								+ " has " + howManyOrientations + " orientations "
								+ " but " + howManyFramesWithoutExtra + " frames for these orientations" );

		int rawRow = howManyFramesWithoutExtra / howManyOrientations ;

		for ( int o = 0 ; o < howManyOrientations ; o ++ ) {
			for ( int f = 0 ; f < description.howManyFramesPerOrientation() ; f ++ )
			{
				NamedOffscreenImage animationFrame = new NamedOffscreenImage( rawFrames.elementAt( ( o * rawRow ) + description.getFrameAt( f ) ) );
				animationFrame.setName( description.getKind() + " "
							+ StringUtilities.toStringWithOrdinalSuffix( f ) + " frame "
							+ "in " + orientations.elementAt( o ) );

				Pictures.saveAsPNG( animationFrame, new java.io.File( head.over.heels.Storage.getGameStorageInHome(), animationFrame.getName() + ".png" ) );

				addFrameTo( orientations.elementAt( o ), animationFrame );
			}
		}

		// add extra frames, if any

		for ( int extra = 0 ; extra < description.howManyExtraFrames() ; extra ++ ) {
			NamedOffscreenImage extraFrame = new NamedOffscreenImage( rawFrames.elementAt( extra + ( rawRow * howManyOrientations ) ) );
			extraFrame.setName( description.getKind () + " " + StringUtilities.toStringWithOrdinalSuffix( extra ) + " extra frame" );

			Pictures.saveAsPNG( extraFrame, new java.io.File( head.over.heels.Storage.getGameStorageInHome(), extraFrame.getName() + ".png" ) );

			addFrameTo( "extra", extraFrame );
		}
	}

	private void makeShadowFrames ()
	{
		// ....
	}

	/** returns the various orientations of this item’s graphics
	 */
	private Vector< String > whatOrientations ()
	{
		int howManyOrientations = getDescriptionOfItem().howManyOrientations() ;
		Vector< String > orientations = new Vector< String >( howManyOrientations );

		if ( howManyOrientations > 0 ) {
			orientations.add( "south" );

			if ( howManyOrientations > 1 ) /* south and west */ {
				orientations.add( "west" );

				if ( howManyOrientations > 2 ) /* south, west, north, east */ {
					orientations.add( "north" );
					orientations.add( "east" );
				}
			}
		}

                return orientations ;
        }

}
