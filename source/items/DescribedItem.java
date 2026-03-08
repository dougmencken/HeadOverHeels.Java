// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2026 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels.items ;

import head.over.heels.NamedOffscreenImage ;
import head.over.heels.Pictures ;
import head.over.heels.PoolOfPictures ;
import head.over.heels.Shady ;

import head.over.heels.GrowingStrings ;
import head.over.heels.StringUtilities ;

import head.over.heels.NoSuchPictureException ;
import head.over.heels.UnlikelyToHappenException ;

import java.util.Map ;
import java.util.Vector ;

import java.awt.image.BufferedImage ;


public abstract class DescribedItem extends AnimatedItem implements Shady
{

	// creates an item by description
	protected DescribedItem( DescriptionOfItem description )
	{
		super() ;

		if ( description == null ) throw new NullPointerException( "null description at the time of item construction" ) ;

		this.descriptionOfItem = description ;
		this.originalKind = description.getKind() ;

		super.setUniqueName( this.originalKind + "." + StringUtilities.makeRandomString( 12 ) );

		super.setDelayBetweenFrames( description.getDelayBetweenFrames() );

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

	/**
	 * Metamorph into another kind of item, such as into bubbles when a character teleports
	 */
	public void metamorphInto ( String newKind )
	{
		DescriptionOfItem newDescription = ItemDescriptions.descriptions().getDescriptionByKind( newKind );
		if ( newDescription == null ) {
			System.err.println( "item " + StringUtilities.putInQuotes( getUniqueName() )
						+ " can’t metamorph into a non-existent kind " + StringUtilities.putInQuotes( newKind ) );
			return ;
		}

		this.descriptionOfItem = newDescription ;

		readGraphicsOfItem ();
		resetAnimation () ;
	}

	public boolean isMetamorphed () {  return ! getKind().equals( getOriginalKind() ) ;  }

	@Override
	public boolean isAnimated ()
	{
		// an item with more than one frame per orientation is animated
		return getDescriptionOfItem().howManyFramesPerOrientation() > 1 ;
	}

	@Override
	protected int firstFrame ()
	{
		return isAtExtraFrame() ? getCurrentFrame() : super.firstFrame() ;
	}

	@Override
	protected int lastFrame ()
	{
		return isAtExtraFrame() ? getCurrentFrame() : super.lastFrame() ;
	}

	@Override
	public boolean isAnimationFinished ()
	{
		return isAtExtraFrame() ? true : super.isAnimationFinished() ;
	}

	public static final String extra_frames = "extra" ;

	protected boolean isAtExtraFrame () {  return getCurrentFrameSequence().equals( DescribedItem.extra_frames );  }

	protected String sequenceBySymmetry ( String sequence )
	{
		int howManyOrientations = getDescriptionOfItem().howManyOrientations() ;

		if ( howManyOrientations == 2 ) {
			if ( sequence.equals( "north" ) ) return "south" ;
			if ( sequence.equals( "east" ) ) return "west" ;
		}
		else if ( howManyOrientations == 1 )
			if ( sequence.equals( "west" ) || sequence.equals( "north" ) || sequence.equals( "east" ) )
				return "south" ;

		return sequence ;
	}

	protected NamedOffscreenImage getNthFrameIn ( String sequence, int n ) throws NoSuchPictureException
	{
		return super.getNthFrameIn( sequenceBySymmetry( sequence ), n );
	}

	// the sequences of pictures of item’s shadow
	private Map< String, Vector< NamedOffscreenImage > > shadows = null ;

	public boolean hasShadow () {  return this.shadows != null && ! this.shadows.isEmpty() ;  }

	protected NamedOffscreenImage getNthShadowIn ( String sequence, int n ) throws NoSuchPictureException
	{
		if ( ! hasShadow() ) {
			String message = StringUtilities.putInQuotes( getUniqueName() ) + " has no shadows" ;
			System.err.println( message );
			throw new NoSuchPictureException( message );
		}

		Vector< NamedOffscreenImage > shadowsIn = this.shadows.get( sequence );
		if ( shadowsIn == null ) shadowsIn = this.shadows.get( sequenceBySymmetry( sequence ) );
		if ( shadowsIn != null && n < shadowsIn.size() )
			return shadowsIn.elementAt( n );

		String message =
			GrowingStrings.newString( "there’s no " )
				.append( StringUtilities.toStringWithOrdinalSuffix( n ) ).append( " shadow" )
				.append( " in " ).append( StringUtilities.putInQuotes( sequence ) )
				.append( " for " ).append( StringUtilities.putInQuotes( getUniqueName() ) )
				.toString() ;
		System.err.println( message );
		throw new NoSuchPictureException( message );
	}

	public NamedOffscreenImage getCurrentImageOfShadowIn ( String sequence )
	{
		try {
			return getNthShadowIn( sequence, getCurrentFrame() ) ;
		} catch ( NoSuchPictureException x ) {
			System.err.println( x.getClass().getName() + ": " + x.getMessage() );
			return null ;
		}
	}

	public NamedOffscreenImage getCurrentImageOfShadow () {  return getCurrentImageOfShadowIn( getCurrentFrameSequence() ) ;  }

	public void addShadowTo ( String sequence, NamedOffscreenImage shadow )
	{
		if ( sequence == null || sequence.isEmpty() ) return ; // don’t add to ""

		if ( this.shadows == null )
			this.shadows = new java.util.HashMap< String, Vector< NamedOffscreenImage > > () ;

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

		if ( ! description.isPartOfDoor() && ! description.getNameOfFramesFile().isEmpty() ) {
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
		final String framesFile = description.getNameOfFramesFile() ;

		if ( frameWidth == 0 || frameHeight == 0 )
			throw new UnlikelyToHappenException( "zero frame width or height for item " + StringUtilities.putInQuotes( getKind() ) );
		if ( framesFile.isEmpty() )
			throw new UnlikelyToHappenException( "empty file name with frames for item " + StringUtilities.putInQuotes( getKind() ) );

		NamedOffscreenImage allTheFrames = PoolOfPictures.getRecentPool().getPicture( framesFile );

		if ( allTheFrames == null )
		// suddenly there’s no file with frames for this item
		{
			int framesAtAll = ( description.howManyFramesPerOrientation() * description.howManyOrientations() ) + description.howManyExtraFrames() ;

			// make a new image filled with the transparency grid
			allTheFrames = new NamedOffscreenImage( frameWidth * framesAtAll, frameHeight );
			allTheFrames.fillWithTransparencyGrid() ;
			allTheFrames.setName( "transparency grid for absent image " + framesFile );

			PoolOfPictures.getRecentPool().putPicture( framesFile, allTheFrames );
		}

		// cut the image into frames

		Vector< BufferedImage > rawFrames = new Vector< BufferedImage >() ;

		for ( int y = 0; y < allTheFrames.getHeight() ; y += frameHeight )
			for ( int x = 0; x < allTheFrames.getWidth() ; x += frameWidth )
				rawFrames.add( Pictures.cloneSubpictureAsARGB( allTheFrames, x, y, frameWidth, frameHeight ) );

		// split frames by orientations

		int howManyOrientations = description.howManyOrientations() ;
		String[] orientations = whatOrientations() ;

		int howManyFramesWithoutExtra = rawFrames.size() - description.howManyExtraFrames() ;
		if ( ( howManyFramesWithoutExtra % howManyOrientations != 0 ) || ( howManyOrientations > howManyFramesWithoutExtra ) )
			throw new UnlikelyToHappenException( "item " + StringUtilities.putInQuotes( getKind() )
								+ " has " + howManyOrientations + StringUtilities.pluralForNot1( howManyOrientations, " orientation" )
								+ " but " + howManyFramesWithoutExtra + StringUtilities.pluralForNot1( howManyFramesWithoutExtra, " frame" ) );

		int rawRow = howManyFramesWithoutExtra / howManyOrientations ;

		for ( int o = 0 ; o < howManyOrientations ; o ++ ) {
			for ( int f = 0 ; f < description.howManyFramesPerOrientation() ; f ++ )
			{
				NamedOffscreenImage frame = new NamedOffscreenImage( rawFrames.elementAt(( o * rawRow ) + description.getFrameAt( f )) );
				frame.setName( description.getKind() + " "
						+ StringUtilities.toStringWithOrdinalSuffix( f ) + " frame "
						+ "in " + orientations[ o ] );

				addFrameTo( orientations[ o ], frame );
			}
		}

		// add extra frames, if any

		for ( int extra = 0 ; extra < description.howManyExtraFrames() ; extra ++ ) {
			NamedOffscreenImage extraFrame = new NamedOffscreenImage( rawFrames.elementAt( extra + ( rawRow * howManyOrientations ) ) );
			extraFrame.setName( description.getKind () + " " + StringUtilities.toStringWithOrdinalSuffix( extra ) + " extra frame" );

			addFrameTo( DescribedItem.extra_frames, extraFrame );
		}
	}

	private void makeShadowFrames ()
	{
		DescriptionOfItem description = getDescriptionOfItem() ;

		final int shadowWidth = description.getWidthOfShadow() ;
		final int shadowHeight = description.getHeightOfShadow() ;
		final String shadowsFile = description.getNameOfShadowsFile() ;

		if ( shadowWidth == 0 || shadowHeight == 0 )
			throw new UnlikelyToHappenException( "zero width or height of shadow for item " + StringUtilities.putInQuotes( getKind() ) );
		if ( shadowsFile.isEmpty() )
			throw new UnlikelyToHappenException( "empty file name with shadows for item " + StringUtilities.putInQuotes( getKind() ) );

		NamedOffscreenImage allTheShadows = PoolOfPictures.getRecentPool().getPicture( shadowsFile );

		if ( allTheShadows == null )
		// suddenly there’s no file with shadows for this item
		{
			int framesAtAll = ( description.howManyFramesPerOrientation() * description.howManyOrientations() ) + description.howManyExtraFrames() ;

			// make a new image filled with the transparency grid
			allTheShadows = new NamedOffscreenImage( shadowWidth * framesAtAll, shadowHeight );
			allTheShadows.fillWithTransparencyGrid() ;
			allTheShadows.setName( "transparency grid for absent image " + shadowsFile );

			PoolOfPictures.getRecentPool().putPicture( shadowsFile, allTheShadows );
		}

		// cut the image of shadow into frames

		Vector< BufferedImage > rawShadows = new Vector< BufferedImage >() ;

		for ( int y = 0; y < allTheShadows.getHeight() ; y += shadowHeight )
			for ( int x = 0; x < allTheShadows.getWidth() ; x += shadowWidth )
				rawShadows.add( Pictures.cloneSubpictureAsARGB( allTheShadows, x, y, shadowWidth, shadowHeight ) );

		// split frames of shadow by orientations

		int howManyOrientations = description.howManyOrientations() ;
		String[] orientations = whatOrientations() ;

		int howManyShadowsWithoutExtra = rawShadows.size() - description.howManyExtraFrames() ;
		if ( ( howManyShadowsWithoutExtra % howManyOrientations != 0 ) || ( howManyOrientations > howManyShadowsWithoutExtra ) )
			throw new UnlikelyToHappenException( "item " + StringUtilities.putInQuotes( getKind() )
								+ " has " + howManyOrientations + StringUtilities.pluralForNot1( howManyOrientations, " orientation" )
								+ " but " + howManyShadowsWithoutExtra + StringUtilities.pluralForNot1( howManyShadowsWithoutExtra, " shadow" ) );

		int rawRow = howManyShadowsWithoutExtra / howManyOrientations ;

		for ( int o = 0 ; o < howManyOrientations ; o ++ ) {
			for ( int f = 0 ; f < description.howManyFramesPerOrientation() ; f ++ )
			{
				NamedOffscreenImage shadow = new NamedOffscreenImage( rawShadows.elementAt(( o * rawRow ) + description.getFrameAt( f )) );
				shadow.setName( description.getKind() + " "
						+ StringUtilities.toStringWithOrdinalSuffix( f ) + " shadow "
						+ "in " + orientations[ o ] );

				addShadowTo( orientations[ o ], shadow );
			}
		}

		// add extra frames of shadow, if any

		for ( int extra = 0 ; extra < description.howManyExtraFrames() ; extra ++ ) {
			NamedOffscreenImage extraShadow = new NamedOffscreenImage( rawShadows.elementAt( extra + ( rawRow * howManyOrientations ) ) );
			extraShadow.setName( description.getKind () + " " + StringUtilities.toStringWithOrdinalSuffix( extra ) + " extra shadow" );

			addShadowTo( DescribedItem.extra_frames, extraShadow );
		}
	}

	/** returns the various orientations of this item’s graphics
	 */
	private String[] whatOrientations ()
	{
		int howManyOrientations = getDescriptionOfItem().howManyOrientations() ;
		String[] orientations = new String[ howManyOrientations ];

		if ( howManyOrientations > 0 ) {
			orientations[ 0 ] = "south" ;

			if ( howManyOrientations > 1 ) /* south and west */ {
				orientations[ 1 ] = "west" ;

				if ( howManyOrientations == 4 ) /* south, west, north, east */ {
					orientations[ 2 ] = "north" ;
					orientations[ 3 ] = "east" ;
				}
			}
		}

                return orientations ;
        }

}
