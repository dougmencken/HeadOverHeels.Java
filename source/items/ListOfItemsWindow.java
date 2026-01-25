// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2026 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels.items ;

import javax.swing.JFrame ;
import javax.swing.JPanel ;

import javax.swing.JComboBox ;
import javax.swing.JLabel ;
import javax.swing.JTabbedPane ;

import head.over.heels.gui.swing.CuteSwingButton ;
import head.over.heels.gui.swing.TabbedPaneWithTabsInOneRow ;

import head.over.heels.StringUtilities ;


public class ListOfItemsWindow extends JFrame
{

	private JComboBox< String > theList ;

	private JLabel itemWidthX = new JLabel() ;
	private JLabel itemWidthY = new JLabel() ;
	private JLabel itemHeight = new JLabel() ;

	private JLabel itemWeight = new JLabel() ;
	private JLabel itemSpeed = new JLabel() ;

	private JLabel itemIsMortal = new JLabel() ;

	private JLabel itemFramesFile = new JLabel() ;
	private JLabel itemFrameWidth = new JLabel() ;
	private JLabel itemFrameHeight = new JLabel() ;

	private JLabel itemDelayBetweenFrames = new JLabel() ;

	private JLabel itemShadowsFile = new JLabel() ;
	private JLabel itemWidthOfShadow = new JLabel() ;
	private JLabel itemHeightOfShadow = new JLabel() ;

	private JLabel itemSequenceOFrames = new JLabel() ;
	private JLabel itemOrientations = new JLabel() ;
	private JLabel itemExtraFrames = new JLabel() ;

	private CuteSwingButton graphicsButton ;

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
				updateLabels ();
				pack() ;
			}
		} );

		panel.add( this.theList );
		panel.add( javax.swing.Box.createVerticalStrut( 10 ) );

		this.resetLabels() ;

		ItemDescriptionPanel infoPanel = new ItemDescriptionPanel() ;
		{
			infoPanel.addTwoLabels( new JLabel( "width x" ), this.itemWidthX );
			infoPanel.addTwoLabels( new JLabel( "width y" ), this.itemWidthY );
			infoPanel.addTwoLabels( new JLabel( "height" ), this.itemHeight );

			infoPanel.addTwoLabels( new JLabel( "weight" ), this.itemWeight );
			infoPanel.addTwoLabels( new JLabel( "speed" ), this.itemSpeed );

			infoPanel.addTwoLabels( new JLabel( "is mortal?" ), this.itemIsMortal );

			infoPanel.addTwoLabels( new JLabel( "frames file" ), this.itemFramesFile );
			infoPanel.addTwoLabels( new JLabel( "frame width" ), this.itemFrameWidth );
			infoPanel.addTwoLabels( new JLabel( "frame height" ), this.itemFrameHeight );

			infoPanel.addTwoLabels( new JLabel( "delay between frames" ), this.itemDelayBetweenFrames );

			infoPanel.addTwoLabels( new JLabel( "shadows file" ), this.itemShadowsFile );
			infoPanel.addTwoLabels( new JLabel( "width of shadow" ), this.itemWidthOfShadow );
			infoPanel.addTwoLabels( new JLabel( "height of shadow" ), this.itemHeightOfShadow );

			infoPanel.addTwoLabels( new JLabel( "sequence o’ frames" ), this.itemSequenceOFrames );
			infoPanel.addTwoLabels( new JLabel( "orientations" ), this.itemOrientations );
			infoPanel.addTwoLabels( new JLabel( "extra frames" ), this.itemExtraFrames );
		}
		panel.add( infoPanel );

		panel.add( javax.swing.Box.createVerticalStrut( 10 ) );

		this.graphicsButton = new CuteSwingButton( "🖼 graphics" );
		this.graphicsButton.addActionListener( new java.awt.event.ActionListener ()
		{
			public void actionPerformed( java.awt.event.ActionEvent ae ) {
				showItemGraphics() ;
			}
		} );

		panel.add( this.graphicsButton );

		this.updateLabels() ;
		super.pack() ;

		java.awt.GraphicsConfiguration gconfig = super.getGraphicsConfiguration() ;
		java.awt.Rectangle bounds = gconfig.getBounds() ;
		super.setLocation( ( bounds.width << 1 ) / 3, bounds.height >> 2 );
	}

	public void updateLabels ()
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

			this.itemIsMortal.setText( description.isMortal() ? "✔ yes" : "no" );

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
		}
		else
			this.resetLabels() ;
	}

	public void resetLabels ()
	{
		this.itemWidthX.setText( "𝔀𝓲𝓭𝓽𝓱-𝔁" );
		this.itemWidthY.setText( "𝔀𝓲𝓭𝓽𝓱-𝔂" );
		this.itemHeight.setText( "𝓱𝓮𝓲𝓰𝓱𝓽" );

		this.itemWeight.setText( "𝔀𝓮𝓲𝓰𝓱𝓽" );
		this.itemSpeed.setText( "𝓼𝓹𝓮𝓮𝓭" );

		this.itemIsMortal.setText( "𝓲𝓼-𝓶𝓸𝓻𝓽𝓪𝓵" );

		this.itemFramesFile.setText( "𝓯𝓻𝓪𝓶𝓮𝓼-𝓯𝓲𝓵𝓮" );
		this.itemFrameWidth.setText( "𝓯𝓻𝓪𝓶𝓮-𝔀𝓲𝓭𝓽𝓱" );
		this.itemFrameHeight.setText( "𝓯𝓻𝓪𝓶𝓮-𝓱𝓮𝓲𝓰𝓱𝓽" );

		this.itemDelayBetweenFrames.setText( "𝓭𝓮𝓵𝓪𝔂-𝓫𝓮𝓽𝔀𝓮𝓮𝓷-𝓯𝓻𝓪𝓶𝓮𝓼" );

		this.itemShadowsFile.setText( "𝓼𝓱𝓪𝓭𝓸𝔀𝓼-𝓯𝓲𝓵𝓮" );
		this.itemWidthOfShadow.setText( "𝔀𝓲𝓭𝓽𝓱-𝓸𝓯-𝓼𝓱𝓪𝓭𝓸𝔀" );
		this.itemHeightOfShadow.setText( "𝓱𝓮𝓲𝓰𝓱𝓽-𝓸𝓯-𝓼𝓱𝓪𝓭𝓸𝔀" );

		this.itemSequenceOFrames.setText( "𝓼𝓮𝓺𝓾𝓮𝓷𝓬𝓮-𝓸𝓯-𝓯𝓻𝓪𝓶𝓮𝓼" );
		this.itemOrientations.setText( "𝓸𝓻𝓲𝓮𝓷𝓽𝓪𝓽𝓲𝓸𝓷𝓼" );
		this.itemExtraFrames.setText( "𝓮𝔁𝓽𝓻𝓪-𝓯𝓻𝓪𝓶𝓮𝓼" );
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


class ItemDescriptionPanel extends JPanel
{

	ItemDescriptionPanel( )
	{
		super() ;
		super.setLayout( new java.awt.GridLayout( /* rows */ 16, /* columns */ 2, /* h gap */ 20, /* v gap */ 5 ) );
	}

	void addTwoLabels( JLabel first, JLabel second )
	{
		if ( first == null ) first = new JLabel( "➡️ 1st label is null ⬅️" );
		if ( second == null ) second = new JLabel( "➡️ 2nd label is null ⬅️" );

		super.add( first );
		super.add( second );
	}

}


class ItemGraphicsWindow extends JFrame
{

	ItemGraphicsWindow ( String kindOfItem, JFrame parentWindow )
	{
		super( StringUtilities.putInQuotes( kindOfItem ) + " graphics" );
		super.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );

		FreeItem item = new FreeItem( ItemDescriptions.descriptions().getDescriptionByKind( kindOfItem ), 0, 0, 0, "south" );
		int framesPerOrientation = item.getDescriptionOfItem().howManyFramesPerOrientation() ;

		TabbedPaneWithTabsInOneRow tabbedPane = new TabbedPaneWithTabsInOneRow( );

		String[] orientations = { "south", "west", "north", "east" } ;
		for ( String sequence : orientations )
		{
			ItemFramesPanel framesPanel = new ItemFramesPanel( framesPerOrientation );

			for ( int n = 0 ; n < framesPerOrientation ; ++ n ) {
				try {
					framesPanel.addImage( item.getNthFrameIn( sequence, n ) );
				} catch ( head.over.heels.NoSuchPictureException x ) {
					framesPanel.addText(
						"no " + StringUtilities.toStringWithOrdinalSuffix( n ) + " frame in " + StringUtilities.putInQuotes( sequence )
					);
				}
			}
			for ( int n = 0 ; n < framesPerOrientation ; ++ n ) {
				try {
					framesPanel.addImage( item.getNthShadowIn( sequence, n ) );
				} catch ( head.over.heels.NoSuchPictureException x ) {
					framesPanel.addText(
						"no " + StringUtilities.toStringWithOrdinalSuffix( n ) + " shadow in " + StringUtilities.putInQuotes( sequence )
					);
				}
			}

			tabbedPane.addTab( sequence, framesPanel );
		}

		int extraFrames = item.getDescriptionOfItem().howManyExtraFrames() ;
		if ( extraFrames > 0 ) {
			ItemFramesPanel extraFramesPanel = new ItemFramesPanel( extraFrames );

			for ( int n = 0 ; n < extraFrames ; ++ n ) {
				try {
					extraFramesPanel.addImage( item.getNthFrameIn( DescribedItem.extra_frames, n ) );
				} catch ( head.over.heels.NoSuchPictureException x ) {
					extraFramesPanel.addText( "no " + StringUtilities.toStringWithOrdinalSuffix( n ) + " extra frame" );
				}
			}
			for ( int n = 0 ; n < extraFrames ; ++ n ) {
				try {
					extraFramesPanel.addImage( item.getNthShadowIn( DescribedItem.extra_frames, n ) );
				} catch ( head.over.heels.NoSuchPictureException x ) {
					extraFramesPanel.addText( "no " + StringUtilities.toStringWithOrdinalSuffix( n ) + " extra shadow" );
				}
			}

			tabbedPane.addTab( DescribedItem.extra_frames, extraFramesPanel );
		}

		super.add( tabbedPane );
		this.pack() ;
		super.setLocation( parentWindow.getLocation().x, parentWindow.getLocation().y + parentWindow.getHeight() + 10 );
	}

	public void pack ()
	{
		// yep, do it twice
		super.pack() ;
		super.pack() ;
	}

}


class ItemFramesPanel extends JPanel
{

	private JPanel toAddTo ;

	ItemFramesPanel( int columns )
	{
		super( );

		this.toAddTo = new JPanel() ;
		this.toAddTo.setLayout( new java.awt.GridLayout( /* rows */ 2, columns, /* h gap */ 10, /* v gap */ 0 ) );

		super.setLayout( new java.awt.FlowLayout( java.awt.FlowLayout.CENTER ) );
		super.setBorder( new javax.swing.border.EmptyBorder( 20, 20, 20, 20 ) ) ;
		super.add( this.toAddTo );
	}

	/* @Override */
	public java.awt.Component add( java.awt.Component what )
	{
		///throw new UnsupportedOperationException( "use addImage or addText" );
		return this.toAddTo.add( what );
	}

	void addImage( java.awt.image.BufferedImage image )
	{
		this.toAddTo.add( new JLabel( new javax.swing.ImageIcon( image ), JLabel.CENTER ) );
	}

	void addText( String text )
	{
		this.toAddTo.add( new JLabel( text, JLabel.CENTER ) );
	}

}
