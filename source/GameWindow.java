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

import java.awt.Color ;


class ContentOfGameWindow extends JComponent
{

	/**
	 * The buffer to draw on and then copy the whole buffer to the screen
	 */
	private volatile OffscreenImage whatToDraw ;

	OffscreenImage getWhatToDraw () {  return whatToDraw ;  }

	class RepaintTimer extends javax.swing.Timer
	{
		RepaintTimer( JComponent repaintMe )
		{
			super( /* delay */ 10 /* milliseconds */ ,
				new java.awt.event.ActionListener () {
					public void actionPerformed( java.awt.event.ActionEvent e ) {
						if ( repaintMe != null )
							repaintMe.repaint ();
					}
				} ) ;
		}

		public void start () {
			System.out.println( "starting the repaint timer" );
			super.start ();
		}

		public void stop () {
			System.out.println( "stopping the repaint timer" );
			super.stop ();
		}
	}

	private RepaintTimer repaintTimer ;

	private ImageTransition transition ;


	ContentOfGameWindow ( int width, int height )
	{
		setBackground( Colours.reducedRed );
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

	public void update( java.awt.Graphics g )
	{
		paintComponent( g );
	}

	public void paint( java.awt.Graphics g )
	{
		paintComponent( g );
	}

	protected void paintComponent( java.awt.Graphics g )
	{
		super.paintComponent( g );

	String HEAD = "HEAD" ;
	String over = "over" ;
	String HEELS = "HEELS" ;

	String comical = "*COMICAL*" ;
	String dazzling = "*DAZZLING*" ;
	String enthralling = "*ENTHRALLING*" ;

	head.over.heels.gui.Font hohFont = new head.over.heels.gui.Font( "HoH", "fulvous", /* 2x height */ true ); // 😙
	head.over.heels.gui.Font redFont = new head.over.heels.gui.Font( "test", "red" );

	java.awt.image.BufferedImage hohImage = hohFont.composeImageOfString( "\"" + HEAD + " " + over + " " + HEELS + "\"" );

	java.awt.image.BufferedImage comicalImage = redFont.composeImageOfString( comical );
	java.awt.image.BufferedImage dazzlingImage = redFont.composeImageOfString( dazzling );
	java.awt.image.BufferedImage enthrallingImage = redFont.composeImageOfString( enthralling );

	java.awt.Graphics2D gg = this.whatToDraw.createGraphics ();

	int centerY = ( this.whatToDraw.getHeight() - ( hohImage.getHeight() + 33 + comicalImage.getHeight() + dazzlingImage.getHeight() + enthrallingImage.getHeight() ) ) >> 1 ;
	int atY = centerY ;

	gg.drawImage( hohImage, ( this.whatToDraw.getWidth() - hohImage.getWidth() ) >> 1, atY, this );
	atY += hohImage.getHeight() + 33 ;

	gg.drawImage( comicalImage, ( this.whatToDraw.getWidth() - comicalImage.getWidth() ) >> 1, atY, this );
	atY += comicalImage.getHeight();

	gg.drawImage( dazzlingImage, ( this.whatToDraw.getWidth() - dazzlingImage.getWidth() ) >> 1, atY, this );
	atY += dazzlingImage.getHeight();

	gg.drawImage( enthrallingImage, ( this.whatToDraw.getWidth() - enthrallingImage.getWidth() ) >> 1, atY, this );

	gg.dispose ();

		g.drawImage( this.whatToDraw, 0, 0, this );

		///java.awt.Graphics2D g2d = (java.awt.Graphics2D) g.create ();
		///g2d.drawImage( getWhatToDraw(), 0, 0, this );
		///g2d.dispose ();
	}

	void startRepaintTimer ()
	{
		if ( this.repaintTimer == null ) {
			this.repaintTimer = new RepaintTimer( this );
		}
		if ( ! this.repaintTimer.isRunning () )
			this.repaintTimer.start ();
		else	this.repaintTimer.restart ();
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
		if ( ! isTransitionFinished () )
			this.transition.waitForFinishing ();

		OffscreenImage filled = new OffscreenImage( this.whatToDraw.getWidth (), this.whatToDraw.getHeight () );
		filled.fillWithColor( color );

		OffscreenImage result = fadeIn ? this.whatToDraw : filled ;

		if ( fadeIn ) {
			this.whatToDraw = filled ;
			repaint ();
		}

		this.transition = new RandomPixelFade ( /* from */ this.whatToDraw, /* to */ result );
		this.transition.go( );
	}

	boolean isTransitionFinished ()
	{
		return ( this.transition != null ) ? this.transition.isFinished () : /* not ever started */ true ;
	}

	void waitForTransitionToFinish ()
	{
		while ( ! isTransitionFinished () )
			this.transition.waitForFinishing ();
	}

}


public class GameWindow extends JFrame
{

	private ContentOfGameWindow contentPane ;

	private GamePreferences preferences ;


	public GameWindow ( int width, int height )
	{
		super( "Foot and Mouth (Java)" );

		addMouseListener( new java.awt.event.MouseAdapter ()
		{
			static final int ToBlack   = 0 ;
			static final int ToBlue    = 1 ;
			static final int ToRed     = 2 ;
			static final int ToMagenta = 3 ;
			static final int ToGreen   = 4 ;
			static final int ToCyan    = 5 ;
			static final int ToYellow  = 6 ;
			static final int ToWhite   = 7 ;
			static final int ToGray    = 8 ;

			int toColor = ToRed ;

			public void mouseReleased( java.awt.event.MouseEvent e ) {
				++ toColor ;
				if ( toColor > 8 ) toColor = 0 ;

				if      ( toColor ==   ToBlack ) randomPixelFadeOut( Colours.black ) ;
				else if ( toColor ==    ToBlue ) randomPixelFadeOut( Colours.reducedBlue ) ;
				else if ( toColor ==     ToRed ) randomPixelFadeOut( Colours.reducedRed ) ;
				else if ( toColor == ToMagenta ) randomPixelFadeOut( Colours.magenta ) ;
				else if ( toColor ==   ToGreen ) randomPixelFadeOut( Colours.green ) ;
				else if ( toColor ==    ToCyan ) randomPixelFadeOut( Colours.cyan ) ;
				else if ( toColor ==  ToYellow ) randomPixelFadeOut( Colours.yellow ) ;
				else if ( toColor ==   ToWhite ) randomPixelFadeOut( Colours.white ) ;
				else if ( toColor ==    ToGray ) randomPixelFadeOut( Colours.grey75white ) ;
			}
		} ) ;
		addKeyListener( new java.awt.event.KeyAdapter ()
		{
			public void keyReleased( java.awt.event.KeyEvent e ) {
				quitGame ();
			}
			public void keyTyped( java.awt.event.KeyEvent e ) {
				/* do nothing */
			}
		} ) ;
		addWindowListener( new java.awt.event.WindowAdapter ()
		{
			public void windowClosing( java.awt.event.WindowEvent e ) {
				quitGame ();
			}
		} ) ;
		/* addFocusListener( new java.awt.event.FocusListener ()
		{
			public void focusGained( java.awt.event.FocusEvent e ) {
				System.out.println( "the window got focus" );
			}
			public void focusLost( java.awt.event.FocusEvent e ) {
				System.out.println( "the window lost focus" );
			}
		} ) ; */

		this.preferences = new GamePreferences( "preferences.xml" );
		this.preferences.setScreenWidth( width );
		this.preferences.setScreenHeight( height );
		this.preferences.readPreferences ();

		setSize( this.preferences.getScreenWidth (), this.preferences.getScreenHeight () );
		setResizable( false );
		setLocationRelativeTo( null ); // to center this JFrame on the screen
		setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );

		this.contentPane = new ContentOfGameWindow( getWidth(), getHeight() );
		setContentPane( this.contentPane );
	}

	public GameWindow ()
	{
		this( GamePreferences.constants.Default_Screen_Width,
				GamePreferences.constants.Default_Screen_Height );
	}

	public void quitGame ()
	{
		writePreferences ();
		randomPixelFadeOut( Color.black );
		finishPaintingContent ();
		dispose ();
	}

	private void writePreferences ()
	{
		if ( this.preferences != null )
			this.preferences.writePreferences ();
	}

	private void finishPaintingContent ()
	{
		// if there's any unfinished transition, wait for it to complete
		while ( ! this.contentPane.isTransitionFinished () )
			this.contentPane.waitForTransitionToFinish ();

		this.contentPane.stopRepaintTimer ();
	}

	public void dispose ()
	{
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
