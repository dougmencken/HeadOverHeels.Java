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
import javax.swing.Timer ;

import java.awt.Color ;


class ContentOfGameWindow extends JComponent
{

	/**
	 * The buffer to draw on and then copy the whole buffer to the screen
	 */
	private volatile OffscreenImage whatToDraw ;

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

		g.drawImage( this.whatToDraw, 0, 0, this );

		///java.awt.Graphics2D g2d = (java.awt.Graphics2D) g.create ();
		///g2d.drawImage( getWhatToDraw(), 0, 0, this );
		///g2d.dispose ();
	}

	void startRepaintTimer ()
	{
		if ( this.repaintTimer == null ) {
			this.repaintTimer = new Timer( /* delay */ 10 /* milliseconds */
							, new java.awt.event.ActionListener () {
				public void actionPerformed( java.awt.event.ActionEvent e ) {
					repaint () ;
				}
			} ) ;
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
		OffscreenImage filled = new OffscreenImage( this.whatToDraw.getWidth (), this.whatToDraw.getHeight () );
		filled.fillWithColor( color );

		OffscreenImage result = fadeIn ? this.whatToDraw : filled ;

		if ( fadeIn ) {
			this.whatToDraw = filled ;
			repaint ();
		}

		RandomPixelFade transition = new RandomPixelFade ( /* from */ this.whatToDraw, /* to */ result );
		transition.go( );
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

			int toColor = ToRed ;

			public void mouseReleased( java.awt.event.MouseEvent e ) {
				toColor ++ ;
				if ( toColor > 7 ) toColor = 0 ;

				if      ( toColor ==   ToBlack ) randomPixelFadeOut( Color.black ) ;
				else if ( toColor ==    ToBlue ) randomPixelFadeOut( Color.blue ) ;
				else if ( toColor ==     ToRed ) randomPixelFadeOut( Color.red ) ;
				else if ( toColor == ToMagenta ) randomPixelFadeOut( Color.magenta ) ;
				else if ( toColor ==   ToGreen ) randomPixelFadeOut( Color.green ) ;
				else if ( toColor ==    ToCyan ) randomPixelFadeOut( Color.cyan ) ;
				else if ( toColor ==  ToYellow ) randomPixelFadeOut( Color.yellow ) ;
				else if ( toColor ==   ToWhite ) randomPixelFadeOut( Color.white ) ;
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

		this.preferences = new GamePreferences( "preferences.xml" );
		this.preferences.setScreenWidth( width );
		this.preferences.setScreenHeight( height );
		this.preferences.readPreferences ();

		setSize( this.preferences.getScreenWidth (), this.preferences.getScreenHeight () );
		setResizable( false );
		setLocationRelativeTo( null ); // to center this JFrame on the screen

		this.contentPane = new ContentOfGameWindow( getWidth(), getHeight() );
		setContentPane( this.contentPane );
	}

	public GameWindow ()
	{
		this( GamePreferences.constants.Default_Screen_Width, GamePreferences.constants.Default_Screen_Height );
	}

	public void quit ()
	{
		writePreferences ();
		setVisible( false );
		dispose ();
	}

	public void writePreferences ()
	{
		if ( this.preferences != null )
			this.preferences.writePreferences ();
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
