// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2025 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels.gui ;

import javax.swing.JButton ;

import java.awt.event.ActionEvent ;
import java.awt.event.MouseEvent ;
import java.awt.event.MouseListener ;

import java.awt.Color ;

import head.over.heels.Colours ;


public class CuteSwingButton extends JButton implements MouseListener
{

	public static final int rounding_radius = 24 ;

	private Color foreColor ;
	private Color backColor ;

	public void setForeground ( Color fore )
	{
		this.foreColor = fore ;
		super.setForeground( fore );
	}
	public void setBackground ( Color back )
	{
		this.backColor = back ;
		super.setBackground( back );
	}

	public CuteSwingButton ( String label )
	{
		super( label );

		super.setBorder( new RoundedCornerBorder( CuteSwingButton.rounding_radius, this ) );
		super.setAlignmentX( java.awt.Component.CENTER_ALIGNMENT );

		super.setContentAreaFilled( false ); // don’t draw the default background for button
		super.setOpaque( true ); // draw background

		this.setForeground( Colours.black );
		this.setBackground( Colours.white );

		super.setUI( new javax.swing.plaf.basic.BasicButtonUI ()
		{
			public void update( java.awt.Graphics g, javax.swing.JComponent c ) {
				if ( c.isOpaque() ) {
					java.awt.Graphics2D g2d = (java.awt.Graphics2D) g.create() ;
					g2d.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
					g2d.setColor( c.getBackground() );
					g2d.fillRoundRect( 0, 0, c.getWidth() - 1,c.getHeight() - 1, rounding_radius + 2, rounding_radius + 2 );
					g2d.dispose() ;
				}

				boolean wasDisabled = ! c.isEnabled() ;
				if ( wasDisabled ) c.setEnabled( true ) ; // draw a disabled button as enabled

				super.paint( g, c );

				if ( wasDisabled ) c.setEnabled( false ) ;
			}
		} );

		super.setEnabled( false ); // no default ActionEvent when clicked
		super.addMouseListener( this );
	}

	private boolean mouseButtonPressed = false ;
	private boolean mouseExited = false ;

	public void mousePressed( MouseEvent me ) {
		this.mouseButtonPressed = true ;
		updateButtonColors ();
	}

	public void mouseReleased( MouseEvent me ) {
		this.fireActionPerformed( new ActionEvent( this,  ActionEvent.ACTION_PERFORMED, super.getText() ) );

		this.mouseButtonPressed = false ;
		updateButtonColors ();
	}

	public void mouseEntered( MouseEvent me ) {
		this.mouseExited = false ;
		updateButtonColors ();
	}

	public void mouseExited( MouseEvent me ) {
		this.mouseExited = true ;
		updateButtonColors ();
	}

	private void updateButtonColors ()
	{
		super.setBackground( ( this.mouseButtonPressed && ! this.mouseExited ) ? this.foreColor : this.backColor );
		super.setForeground( ( this.mouseButtonPressed && ! this.mouseExited ) ? this.backColor : this.foreColor );
	}

	public void mouseClicked( MouseEvent me ) {}

	protected void fireActionPerformed( ActionEvent event )
	{
		if ( this.mouseButtonPressed && ! this.mouseExited )
			super.fireActionPerformed( event );
	}

}


class RoundedCornerBorder implements javax.swing.border.Border
{

	private int borderRadius ;
	private JButton forButton ;

	public RoundedCornerBorder( int radius, JButton button )
	{
		this.borderRadius = radius ;
		this.forButton = button ;
	}

	public void paintBorder( java.awt.Component c, java.awt.Graphics g, int x, int y, int width, int height )
	{
		java.awt.Graphics2D g2d = (java.awt.Graphics2D) g.create() ;
		g2d.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
		g2d.setStroke( new java.awt.BasicStroke( 2 ) ); // 2 pixels thick stroke
		g2d.setColor( this.forButton.getForeground() );
		g2d.draw( new java.awt.geom.RoundRectangle2D.Double( x, y, width - 1, height - 1, this.borderRadius, this.borderRadius ) );
		g2d.dispose() ;
	}

	public java.awt.Insets getBorderInsets( java.awt.Component c )
	{
		final int gap = this.borderRadius >> 2 ;
		return new java.awt.Insets( /* top */ gap, /* left */ gap << 1, /* bottom */ gap, /* right */ gap << 1 );
	}

	public boolean isBorderOpaque() {  return true ;  }

}
