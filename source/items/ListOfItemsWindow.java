// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2025 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels.items ;

import javax.swing.JFrame ;
import javax.swing.JPanel ;

import javax.swing.JButton ;
import javax.swing.JCheckBox ;
import javax.swing.JComboBox ;
import javax.swing.JLabel ;

import head.over.heels.Colours ;
import head.over.heels.StringUtilities ;


public class ListOfItemsWindow extends JFrame
{

	private JComboBox< String > theList = null ;

	private JLabel itemWidthX ;
	private JLabel itemWidthY ;
	private JLabel itemHeight ;

	private JLabel itemWeight ;
	private JLabel itemSpeed ;

	private JLabel itemMortal ;

	private JLabel itemFramesFile ;
	private JLabel itemFrameWidth ;
	private JLabel itemFrameHeight ;

	private JLabel itemDelayBetweenFrames ;

	private JLabel itemShadowsFile ;
	private JLabel itemWidthOfShadow ;
	private JLabel itemHeightOfShadow ;

	private JLabel itemSequenceOFrames ;

	private JLabel itemOrientations ;
	private JLabel itemExtraFrames ;

	private JButton graphicsButton ;

	public ListOfItemsWindow ()
	{
		super( "items" );

		super.setResizable( false );
		super.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );

		JPanel panel = new JPanel() ;
		panel.setLayout( new javax.swing.BoxLayout( panel, javax.swing.BoxLayout.Y_AXIS ) );
		panel.setBorder( new javax.swing.border.EmptyBorder( 20, 20, 20, 20 ) ) ;
		super.add( panel );

		this.theList = new JComboBox< String >( ItemDescriptions.descriptions().getAllKindsOfItems() );
		this.theList.setMaximumRowCount( 16 );
		this.theList.setSelectedItem( "headoverheels" );
		this.theList.addActionListener( new java.awt.event.ActionListener( )
		{
			public void actionPerformed( java.awt.event.ActionEvent e ) {
				updateComponents ();
			}
		} );

		panel.add( this.theList );

		panel.add( javax.swing.Box.createVerticalStrut( 10 ) );

		JPanel infoPanel = new JPanel() ;
		infoPanel.setLayout( new java.awt.GridLayout( /* rows */ 16, /* columns */ 2, /* h gap */ 20, /* v gap */ 5 ) );
		{
			this.itemWidthX = new JLabel( "(width-x)" );
			this.itemWidthY = new JLabel( "(width-y)" );
			this.itemHeight = new JLabel( "(height)" );
			this.itemWeight = new JLabel( "(weight)" );
			this.itemSpeed = new JLabel( "(speed)" );

			infoPanel.add( new JLabel( "width x" ) );
			infoPanel.add( this.itemWidthX );
			infoPanel.add( new JLabel( "width y" ) );
			infoPanel.add( this.itemWidthY );
			infoPanel.add( new JLabel( "height" ) );
			infoPanel.add( this.itemHeight );

			infoPanel.add( new JLabel( "weight" ) );
			infoPanel.add( this.itemWeight );
			infoPanel.add( new JLabel( "speed" ) );
			infoPanel.add( this.itemSpeed );

			this.itemMortal = new JLabel( "(mortal?)" );
			infoPanel.add( new JLabel( "mortal" ) );
			infoPanel.add( this.itemMortal );

			this.itemFramesFile = new JLabel( "(frames-file)" );
			this.itemFrameWidth = new JLabel( "(frame-width)" );
			this.itemFrameHeight = new JLabel( "(frame-height)" );

			infoPanel.add( new JLabel( "frames file" ) );
			infoPanel.add( this.itemFramesFile );
			infoPanel.add( new JLabel( "frame width" ) );
			infoPanel.add( this.itemFrameWidth );
			infoPanel.add( new JLabel( "frame height" ) );
			infoPanel.add( this.itemFrameHeight );

			this.itemDelayBetweenFrames = new JLabel( "(delay-between-frames)" );
			infoPanel.add( new JLabel( "delay between frames" ) );
			infoPanel.add( this.itemDelayBetweenFrames );

			this.itemShadowsFile = new JLabel( "(shadows-file)" );
			this.itemWidthOfShadow = new JLabel( "(width-of-shadow)" );
			this.itemHeightOfShadow = new JLabel( "(height-of-shadow)" );

			infoPanel.add( new JLabel( "shadows file" ) );
			infoPanel.add( this.itemShadowsFile );
			infoPanel.add( new JLabel( "width of shadow" ) );
			infoPanel.add( this.itemWidthOfShadow );
			infoPanel.add( new JLabel( "height of shadow" ) );
			infoPanel.add( this.itemHeightOfShadow );

			this.itemSequenceOFrames = new JLabel( "(sequence-of-frames)" );
			infoPanel.add( new JLabel( "sequence o’ frames" ) );
			infoPanel.add( this.itemSequenceOFrames );

			this.itemOrientations = new JLabel( "(orientations)" );
			infoPanel.add( new JLabel( "orientations" ) );
			infoPanel.add( this.itemOrientations );

			this.itemExtraFrames = new JLabel( "(extra-frames)" );
			infoPanel.add( new JLabel( "extra frames" ) );
			infoPanel.add( this.itemExtraFrames );
		}
		panel.add( infoPanel );

		panel.add( javax.swing.Box.createVerticalStrut( 10 ) );

		this.graphicsButton = new JButton( "🖼 graphics" );
		final int rounding_radius = 24 ;
		this.graphicsButton.setBorder( new RoundedCornerBorder( rounding_radius, this.graphicsButton ) );
		this.graphicsButton.setContentAreaFilled( false ); // don’t draw the default background for button
		this.graphicsButton.setOpaque( true ); // draw background
		this.graphicsButton.setBackground( Colours.white );
		this.graphicsButton.setForeground( Colours.black );
		this.graphicsButton.setAlignmentX( java.awt.Component.CENTER_ALIGNMENT );

		this.graphicsButton.setUI( new javax.swing.plaf.basic.BasicButtonUI ()
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

		this.graphicsButton.addMouseListener( new java.awt.event.MouseAdapter ()
		{
			java.awt.Color foreColor = graphicsButton.getForeground() ;
			java.awt.Color backColor = graphicsButton.getBackground() ;

			private boolean pressed = false ;
			private boolean exited = false ;

			public void mousePressed( java.awt.event.MouseEvent me ) {
				this.pressed = true ;
				updateButtonColors ();
			}
			public void mouseReleased( java.awt.event.MouseEvent me ) {
				if ( this.pressed && ! this.exited )
					showItemGraphics() ;

				this.pressed = false ;
				updateButtonColors ();
			}
			public void mouseEntered( java.awt.event.MouseEvent me ) {
				this.exited = false ;
				updateButtonColors ();
			}
			public void mouseExited( java.awt.event.MouseEvent me ) {
				this.exited = true ;
				updateButtonColors ();
			}
			private void updateButtonColors ()
			{
				graphicsButton.setBackground( ( this.pressed && ! this.exited ) ? this.foreColor : this.backColor );
				graphicsButton.setForeground( ( this.pressed && ! this.exited ) ? this.backColor : this.foreColor );
			}
		} );

		panel.add( this.graphicsButton );

		this.updateComponents() ;

		java.awt.GraphicsConfiguration gconfig = super.getGraphicsConfiguration() ;
		java.awt.Rectangle bounds = gconfig.getBounds() ;
		super.setLocation( ( bounds.width << 1 ) / 3, bounds.height >> 2 );
	}

	public void updateComponents ()
	{
		Object chosenItem = this.theList.getSelectedItem() ;
		if ( chosenItem != null ) {
			String kind = chosenItem.toString() ;
			DescriptionOfItem description = ItemDescriptions.descriptions().getDescriptionByKind( kind );

			this.itemWidthX.setText(String.valueOf( description.getWidthX() ));
			this.itemWidthY.setText(String.valueOf( description.getWidthY() ));
			this.itemHeight.setText(String.valueOf( description.getHeight() ));

			this.itemWeight.setText(String.valueOf( description.getWeight() ));
			this.itemSpeed.setText(String.valueOf( description.getSpeed() ));

			this.itemMortal.setText( description.isMortal() ? "✔ yes" : "no" );

			this.itemFramesFile.setText(StringUtilities.putInQuotes( description.getNameOfFramesFile() ));
			this.itemFrameWidth.setText(String.valueOf( description.getWidthOfFrame() ));
			this.itemFrameHeight.setText(String.valueOf( description.getHeightOfFrame() ));

			this.itemDelayBetweenFrames.setText(String.valueOf( description.getDelayBetweenFrames() ));

			this.itemShadowsFile.setText(StringUtilities.putInQuotes( description.getNameOfShadowsFile() ));
			this.itemWidthOfShadow.setText(String.valueOf( description.getWidthOfShadow() ));
			this.itemHeightOfShadow.setText(String.valueOf( description.getHeightOfShadow() ));

			StringBuilder sequenceOFrames = new StringBuilder() ;
			int framesPerOrientation = description.howManyFramesPerOrientation() ;
			for ( int n = 0 ; n < framesPerOrientation ; ) {
				sequenceOFrames.append(String.valueOf( description.getFrameAt( n ) ));
				if ( ++n != framesPerOrientation ) sequenceOFrames.append( "," );
			}
			sequenceOFrames.append( " " );
			if ( framesPerOrientation > 1 )
				sequenceOFrames.append( description.isSequenceOFramesSimple() ? "(simple)" : "(custom)" );
			else
				sequenceOFrames.append( "(static)" );

			this.itemSequenceOFrames.setText( sequenceOFrames.toString() );

			this.itemOrientations.setText(String.valueOf( description.howManyOrientations() ));
			this.itemExtraFrames.setText(String.valueOf( description.howManyExtraFrames() ));

			super.pack() ;
		}
	}

	public void showItemGraphics ()
	{
		Object chosenItem = this.theList.getSelectedItem() ;
		if ( chosenItem != null ) {
			String kind = chosenItem.toString() ;
			ItemGraphicsWindow graphicsWindow = new ItemGraphicsWindow( kind, this );
			graphicsWindow.setVisible( true );
		}
	}

}


class ItemGraphicsWindow extends JFrame
{

	public ItemGraphicsWindow ( String kindOfItem, JFrame parentWindow )
	{
		super( StringUtilities.putInQuotes( kindOfItem ) + " graphics" );
		super.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );

		FreeItem item = new FreeItem( ItemDescriptions.descriptions().getDescriptionByKind( kindOfItem ), 0, 0, 0, "south" );
		int framesPerOrientation = item.getDescriptionOfItem().howManyFramesPerOrientation() ;

		JPanel framesPanel = new JPanel() ;
		framesPanel.setBorder( new javax.swing.border.EmptyBorder( 20, 20, 20, 20 ) ) ;
		framesPanel.setLayout( new java.awt.GridLayout( /* rows */ 2, /* columns */ framesPerOrientation, /* h gap */ 10, /* v gap */ 0 ) );

		String currentSequence = item.getCurrentFrameSequence() ;
		for ( int n = 0 ; n < framesPerOrientation ; ++ n ) {
			try {
				JLabel imageLabel = new JLabel( new javax.swing.ImageIcon( item.getNthFrameIn( currentSequence, n ) ) );
				framesPanel.add( imageLabel );
			} catch ( head.over.heels.NoSuchPictureException x ) {
				framesPanel.add( new JLabel(
					"no " + StringUtilities.toStringWithOrdinalSuffix( n ) + " frame in " + StringUtilities.putInQuotes( currentSequence )
				) );
			}
		}
		for ( int n = 0 ; n < framesPerOrientation ; ++ n ) {
			try {
				JLabel imageLabel = new JLabel( new javax.swing.ImageIcon( item.getNthShadowIn( currentSequence, n ) ) );
				framesPanel.add( imageLabel );
			} catch ( head.over.heels.NoSuchPictureException x ) {
				framesPanel.add( new JLabel(
					"no " + StringUtilities.toStringWithOrdinalSuffix( n ) + " shadow in " + StringUtilities.putInQuotes( currentSequence )
				) );
			}
		}

		super.add( framesPanel );
		super.pack() ;

		super.setLocation( parentWindow.getLocation().x, parentWindow.getLocation().y + parentWindow.getHeight() + 10 );
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
