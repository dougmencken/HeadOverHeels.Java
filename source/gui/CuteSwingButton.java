// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2025 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels.gui ;

import javax.swing.JButton ;
import javax.swing.AbstractButton ;

import java.awt.event.ActionEvent ;
import java.awt.event.ActionListener ;
import java.awt.event.MouseEvent ;
import java.awt.event.MouseListener ;

import java.awt.Color ;
import head.over.heels.Colours ;

import java.util.Vector ;


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

		super.setModel( new CuteSwingButtonModel() );

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

				super.paint( g, c );
			}
		} );

		super.setEnabled( false ); // no default ActionEvent when clicked
		super.addMouseListener( this );
	}

	public void setEnabled ( boolean enabled ) {
		if ( ! enabled )
			throw new IllegalArgumentException( this.getClass().getName() + " cannot be set disabled" );
	}

	public boolean isEnabled () {  return true ;  }

	public void setActionCommand ( String command ) {  getModel().setActionCommand( command );  }
	public String getActionCommand () {  return getModel().getActionCommand() ;  }

	public void addActionListener ( ActionListener l ) {  getModel().addActionListener( l );  }
	public void removeActionListener ( ActionListener l ) {  getModel().removeActionListener( l );  }

	public void mouseEntered( MouseEvent me ) {
		getModel().setRollover( true );
		updateButtonColors ();
	}

	public void mouseExited( MouseEvent me ) {
		getModel().setRollover( false );
		updateButtonColors ();
	}

	public void mousePressed( MouseEvent me ) {
		getModel().setPressed( getModel().isRollover() );
		updateButtonColors ();
	}

	public void mouseReleased( MouseEvent me ) {
		if ( getModel().isRollover() ) {
			ActionEvent newActionEvent = new ActionEvent(
				this, ActionEvent.ACTION_PERFORMED,
				getActionCommand(),
				System.currentTimeMillis(), ///java.awt.EventQueue.getMostRecentEventTime(),
				me.getModifiersEx()
			);
			if ( getModel() instanceof CuteSwingButtonModel )
				( (CuteSwingButtonModel) getModel() ).fireActionPerformed( newActionEvent );
			else
				super.fireActionPerformed( newActionEvent );
		}

		getModel().setPressed( false );
		updateButtonColors ();
	}

	private void updateButtonColors ()
	{
		super.setBackground( ( getModel().isPressed() && getModel().isRollover() ) ? this.foreColor : this.backColor );
		super.setForeground( ( getModel().isPressed() && getModel().isRollover() ) ? this.backColor : this.foreColor );
	}

	public void mouseClicked( MouseEvent me ) {}

}


class CuteSwingButtonModel implements javax.swing.ButtonModel
{

	private boolean pushed = false ;
	private boolean rollover = false ;

	private String actionCommand = null ;
	private Vector< ActionListener > actionListeners = new Vector< ActionListener >() ;

	CuteSwingButtonModel( ) {}

	public void setRollover ( boolean over ) {  this.rollover = over ;  }
	public boolean isRollover () {  return this.rollover ;  }

	public void setPressed ( boolean pressed ) {  this.pushed = pressed ;  }
	public boolean isPressed () {  return this.pushed ;  }

	public void setSelected ( boolean selected ) {}
	public boolean isSelected () {  return false ;  }

	public void setArmed ( boolean armed ) {}
	public boolean isArmed () {  return false ;  }

	public void setEnabled ( boolean enabled ) {}
	public boolean isEnabled () {  return true ;  } // always enabled

	public void setActionCommand ( String command ) {  this.actionCommand = command ;  }
	public String getActionCommand () {  return this.actionCommand ;  }

	public void addActionListener ( ActionListener l ) {  this.actionListeners.add( l );  }
	public void removeActionListener ( ActionListener l ) {  this.actionListeners.remove( l );  }

	void fireActionPerformed ( ActionEvent e ) {
		for ( ActionListener l : this.actionListeners )
			l.actionPerformed( e );
	}

	public void addChangeListener ( javax.swing.event.ChangeListener l ) {}
	public void removeChangeListener ( javax.swing.event.ChangeListener l ) {}
	///void fireStateChanged () {}

	public void addItemListener ( java.awt.event.ItemListener l ) {}
	public void removeItemListener ( java.awt.event.ItemListener l ) {}
	///void fireItemStateChanged ( java.awt.event.ItemEvent e ) {}

	public void setGroup ( javax.swing.ButtonGroup g ) {}
	public javax.swing.ButtonGroup getGroup () {  return null ;  }

	public void setMnemonic ( int mnemonic ) {}
	public int getMnemonic () {  return 0 ;  }

	public Object[] getSelectedObjects() {  return null ;  }

}


class RoundedCornerBorder implements javax.swing.border.Border
{

	private int borderRadius ;
	private AbstractButton forButton ;

	RoundedCornerBorder( int radius, AbstractButton button )
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
