// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2023 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels ;

import javax.swing.JFrame ;
import javax.swing.JComponent ;
import javax.swing.Timer;

import java.awt.Color ;


class ContentOfGameWindow extends JComponent
{

	/**
	 * The buffer to draw on and then copy the whole buffer to the screen
	 */
	private OffscreenImage whatToDraw ;

	OffscreenImage getWhatToDraw () {  return whatToDraw ;  }

	private Timer repaintTimer ;


	ContentOfGameWindow ( int width, int height )
	{
		setBackground( Color.red );
		setSize( width, height );

		startRepaintTimer ();
	}

	public void setSize ( int width, int height )
	{
		super.setSize( width, height );
		resizeBufferToDraw ( width, height );
	}

	void resizeBufferToDraw ( int width, int height )
	{
		if ( this.whatToDraw == null ) {
			this.whatToDraw = new OffscreenImage( width, height );
			this.whatToDraw.fillWithColor( getBackground () );
		} else
		   if ( this.whatToDraw.getWidth() != width || height != this.whatToDraw.getHeight() ) {
			this.whatToDraw = new OffscreenImage( this.whatToDraw, width, height, getBackground () );
		}
	}

	public void paint( java.awt.Graphics g )
	{
		paintComponent( g );
	}

	protected void paintComponent( java.awt.Graphics g )
	{
		super.paintComponent( g );

		g.drawImage( getWhatToDraw(), 0, 0, this );

		///java.awt.Graphics2D g2d = (java.awt.Graphics2D) g.create ();
		///g2d.drawImage( getWhatToDraw(), 0, 0, this );
		///g2d.dispose ();
	}

	void startRepaintTimer ()
	{
		this.repaintTimer = new Timer( /* delay */ 10 /* milliseconds */
						, new java.awt.event.ActionListener () {
			public void actionPerformed( java.awt.event.ActionEvent e ) {
				repaint () ;
			}
		} ) ;
		this.repaintTimer.start ();
	}

	void stopRepaintTimer ()
	{
		if ( this.repaintTimer != null && this.repaintTimer.isRunning() ) {
			this.repaintTimer.stop () ;
			this.repaintTimer = null ;
		}
	}

	void randomPixelFade( boolean fadeIn, Color color )
	{
		int width = getWhatToDraw().getWidth () ;
		int height = getWhatToDraw().getHeight () ;
		int howManyPixels = width * height ;
		//~//~//java.util.BitSet bits = new java.util.BitSet( howManyPixels ); // bit map of howManyPixels bits

		OffscreenImage filled = new OffscreenImage( width, height );
		filled.fillWithColor( color );

		OffscreenImage result = fadeIn ? this.whatToDraw : filled ;
		int [] pixelsOfResult = ( (java.awt.image.DataBufferInt) result.getRaster().getDataBuffer () ).getData() ;

		if ( fadeIn ) {
			this.whatToDraw = filled ;
			repaint ();
		}

		// the vector of pixels to copy
		//*//java.util.Vector< java.awt.Point > pixelsToCopy = new java.util.Vector< java.awt.Point >( howManyPixels );
		//*//for ( int y = 0 ; y < height ; y ++ )
			//*//for ( int x = 0 ; x < width ; x ++ )
				//*//pixelsToCopy.add( new java.awt.Point( x, y ) );

		java.util.Random random = new java.util.Random () ;

		// the set of copied pixels
		java.util.Set< java.awt.Point > copiedPixels = new java.util.HashSet< java.awt.Point >( howManyPixels );

		while ( copiedPixels.size() < howManyPixels ) //// ( pixelsToCopy.size() > 0 )
		{
			//*//int index = random.nextInt( pixelsToCopy.size() ); /* random between 0 and size - 1 */
			//*//java.awt.Point xy = pixelsToCopy.elementAt( index );
			//*//int x = xy.x ;
			//*//int y = xy.y ;
			//*//pixelsToCopy.removeElementAt( index );

			int x = random.nextInt( width  ); // random between 0 and  width - 1
			int y = random.nextInt( height ); // random between 0 and height - 1

			boolean unique = copiedPixels.add( new java.awt.Point( x, y ) );
			if ( unique )
				this.whatToDraw.setRGB( x, y, pixelsOfResult[ x + y * width ] );
		}

		this.whatToDraw = new OffscreenImage( result );
		repaint ();
	}

}


public class GameWindow extends JFrame
{

	private ContentOfGameWindow contentPane ;


	public GameWindow ()
	{
		super( "Foot and Mouth (Java)" );

		addMouseListener( new java.awt.event.MouseAdapter ()
		{
			boolean toggle = true ;

			public void mouseReleased( java.awt.event.MouseEvent e ) {
				toggle = ! toggle ;

				if ( toggle )	randomPixelFadeOut( Color.blue ) ;
				else		randomPixelFadeOut( Color.yellow ) ;
			}
		} ) ;
		addKeyListener( new java.awt.event.KeyAdapter ()
		{
			public void keyPressed( java.awt.event.KeyEvent e ) {
				/* do nothing */
			}
			public void keyReleased( java.awt.event.KeyEvent e ) {
				quit() ;
			}
			public void keyTyped( java.awt.event.KeyEvent e ) {
				quit() ;
			}
		} ) ;
		addWindowListener( new java.awt.event.WindowAdapter ()
		{
			public void windowClosing( java.awt.event.WindowEvent e ) {
				quit() ;
			}
		} ) ;

		setSize( 640, 480 );
		setLocationRelativeTo( null ); // to center this JFrame on the screen

		this.contentPane = new ContentOfGameWindow( getWidth(), getHeight() );
		setContentPane( this.contentPane );
	}

	public void quit ()
	{
		setVisible( false );
		dispose ();
	}

	public void dispose ()
	{
		this.contentPane.stopRepaintTimer ();

		System.out.println( );
		System.out.println( "bye :*" );

		super.dispose() ;
	}

	public void randomPixelFadeIn( Color fromColor )
	{
		this.contentPane.randomPixelFade( true, fromColor ) ;
	}

	public void randomPixelFadeOut( Color toColor )
	{
		this.contentPane.randomPixelFade( false, toColor ) ;
	}

}
