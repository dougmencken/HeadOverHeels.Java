// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2023 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels ;

/**
 * The famous random pixel fade transition
 */
public class RandomPixelFade extends ImageTransition
{

	private int [] pixelsOfResult ;

	private java.awt.Point [] pixelsToCopy ;

	public RandomPixelFade( OffscreenImage from, OffscreenImage to ) // OffscreenImage not BufferedImage to be sure of TYPE_INT_ARGB
	{
		super( from, to );

		this.pixelsOfResult = ( (java.awt.image.DataBufferInt) to.getRaster().getDataBuffer() ).getData() ;

		int width = super.getWidth () ;
		int height = super.getHeight () ;
		int howManyPixels = width * height ;

		// precalculate the sequence of unique random pixels to copy
		this.pixelsToCopy = new java.awt.Point[ howManyPixels ];
		java.util.BitSet bits = new java.util.BitSet( howManyPixels );	// the bit map of howManyPixels bits
										// for the uniqueness of random pixels
		java.util.Random random = new java.util.Random () ;

		for ( int index = 0 ; index < howManyPixels ; )
		{
			int x = random.nextInt( width  ); // random between 0 and  width - 1
			int y = random.nextInt( height ); // random between 0 and height - 1

			if ( ! bits.get( x + y * width ) )
			{
				pixelsToCopy[ index ++ ] = new java.awt.Point( x, y );
				bits.set( x + y * width ) ;
			}
		}
	}

	public void transit ()
	{
		if ( isFinished () ) return ;

		int width = getWidth ();
		int height = getHeight ();
		int howManyPixels = width * height ;

		int chunk = ( howManyPixels >> 9 ) - 1 ;

		long before = System.currentTimeMillis () ;
		for ( int i = 0 ; i < howManyPixels ; i ++ )
		{
			int x = pixelsToCopy[ i ].x ;
			int y = pixelsToCopy[ i ].y ;

			getImageFrom().setRGB( x, y, pixelsOfResult[ x + y * width ] );

			try {
				if ( i % chunk == 0 )		// after painting a chunk
					Thread.sleep( 1 );	// wait a millisecond
			} catch ( InterruptedException e ) {}
		}
		long after = System.currentTimeMillis () ;

		double secondsInFade = (double) (after - before) / 1000.0 ;
		System.out.println( "random-pixel-faded in " + secondsInFade + " seconds" );
	}

}
