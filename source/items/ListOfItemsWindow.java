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

import java.awt.event.ActionEvent ;
import java.awt.event.ActionListener ;

import head.over.heels.gui.swing.CuteSwingButton ;
import head.over.heels.gui.swing.DeferredReshaper ;
import head.over.heels.gui.swing.FixedHeightLabel ;
import head.over.heels.gui.swing.TabbedPaneWithTabsInOneRow ;

import head.over.heels.StringUtilities ;


public class ListOfItemsWindow extends JFrame
{

	private JComboBox< String > theList ;

	private FixedHeightLabel itemWidthX = new FixedHeightLabel() ;
	private FixedHeightLabel itemWidthY = new FixedHeightLabel() ;
	private FixedHeightLabel itemHeight = new FixedHeightLabel() ;

	private FixedHeightLabel itemWeight = new FixedHeightLabel() ;
	private FixedHeightLabel itemSpeed = new FixedHeightLabel() ;

	private FixedHeightLabel itemIsMortal = new FixedHeightLabel() ;

	private FixedHeightLabel itemFramesFile = new FixedHeightLabel() ;
	private FixedHeightLabel itemFrameWidth = new FixedHeightLabel() ;
	private FixedHeightLabel itemFrameHeight = new FixedHeightLabel() ;

	private FixedHeightLabel itemDelayBetweenFrames = new FixedHeightLabel() ;

	private FixedHeightLabel itemShadowsFile = new FixedHeightLabel() ;
	private FixedHeightLabel itemWidthOfShadow = new FixedHeightLabel() ;
	private FixedHeightLabel itemHeightOfShadow = new FixedHeightLabel() ;

	private FixedHeightLabel itemSequenceOFrames = new FixedHeightLabel() ;
	private FixedHeightLabel itemOrientations = new FixedHeightLabel() ;
	private FixedHeightLabel itemExtraFrames = new FixedHeightLabel() ;

	private CuteSwingButton graphicsButton ;

	public ListOfItemsWindow ()
	{
		super( "items" );

		super.setResizable( false );
		super.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );

		JPanel panel = new JPanel() ; // will become the content pane eventually
		panel.setLayout( new javax.swing.BoxLayout( panel, javax.swing.BoxLayout.Y_AXIS ) );
		panel.setBorder( new javax.swing.border.EmptyBorder( 20, 20, 20, 20 ) ) ;

		this.theList = new JComboBox< String >( ItemDescriptions.descriptions().getAllKindsOfItems() );
		this.theList.setMaximumRowCount( 16 );
		this.theList.setSelectedItem( "headoverheels" );
		this.theList.addActionListener( new ActionListener( )
		{
			public void actionPerformed( ActionEvent e ) {
				updateLabels() ;
				smoothlyResizeTo( getPreferredSize(), 333, false /* top-left anchoring */ );
			}
		} );

		panel.add( this.theList );
		panel.add( javax.swing.Box.createVerticalStrut( 10 ) );

		this.resetLabels() ;

		ItemDescriptionPanel infoPanel = new ItemDescriptionPanel() ;
		{
			infoPanel.addTwoLabels( new FixedHeightLabel( "width x" ), this.itemWidthX );
			infoPanel.addTwoLabels( new FixedHeightLabel( "width y" ), this.itemWidthY );
			infoPanel.addTwoLabels( new FixedHeightLabel( "height" ), this.itemHeight );

			infoPanel.addTwoLabels( new FixedHeightLabel( "weight" ), this.itemWeight );
			infoPanel.addTwoLabels( new FixedHeightLabel( "speed" ), this.itemSpeed );

			infoPanel.addTwoLabels( new FixedHeightLabel( "is mortal?" ), this.itemIsMortal );

			infoPanel.addTwoLabels( new FixedHeightLabel( "frames file" ), this.itemFramesFile );
			infoPanel.addTwoLabels( new FixedHeightLabel( "frame width" ), this.itemFrameWidth );
			infoPanel.addTwoLabels( new FixedHeightLabel( "frame height" ), this.itemFrameHeight );

			infoPanel.addTwoLabels( new FixedHeightLabel( "delay between frames" ), this.itemDelayBetweenFrames );

			infoPanel.addTwoLabels( new FixedHeightLabel( "shadows file" ), this.itemShadowsFile );
			infoPanel.addTwoLabels( new FixedHeightLabel( "width of shadow" ), this.itemWidthOfShadow );
			infoPanel.addTwoLabels( new FixedHeightLabel( "height of shadow" ), this.itemHeightOfShadow );

			infoPanel.addTwoLabels( new FixedHeightLabel( "sequence o’ frames" ), this.itemSequenceOFrames );
			infoPanel.addTwoLabels( new FixedHeightLabel( "orientations" ), this.itemOrientations );
			infoPanel.addTwoLabels( new FixedHeightLabel( "extra frames" ), this.itemExtraFrames );
		}
		panel.add( infoPanel );

		panel.add( javax.swing.Box.createVerticalStrut( 10 ) );

		this.graphicsButton = new CuteSwingButton( "🖼 graphics" );
		this.graphicsButton.addActionListener( new ActionListener ()
		{
			public void actionPerformed( ActionEvent ae ) {
				showItemGraphics() ;
			}
		} );

		panel.add( this.graphicsButton );

		super.setContentPane( panel );

		this.updateLabels() ;

		///this.resizeToPreferred() ;
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

		// update the preferred size
		getContentPane().setPreferredSize( null );
		( (JPanel) getContentPane() ).revalidate() ;
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

	public void resizeToPreferred ()
	{
		java.awt.Dimension preferred = super.getPreferredSize() ;
		super.setSize( preferred.width, preferred.height );
	}

	private javax.swing.Timer smoothResizeTimer = null ;

	/**
	 * @param  desiredSize      the desired final size of the frame
	 * @param  duration         transition duration in milliseconds
	 * @param  anchorToCenter   center anchoring if true, top-left anchoring if false
	 */
	public void smoothlyResizeTo (	java.awt.Dimension desiredSize,
					int duration /* in milliseconds */,
					final boolean anchorToCenter )
	{
		if ( this.smoothResizeTimer != null )
			if ( this.smoothResizeTimer.isRunning() )
				this.smoothResizeTimer.stop() ; // stop previous transition

		final java.awt.Rectangle from = super.getBounds() ;

		java.awt.Point toPoint = from.getLocation() ;

		// optional center anchoring
		if ( anchorToCenter ) {
			toPoint.x += ( from.width - desiredSize.width ) >> 1 ;
			toPoint.y += ( from.height - desiredSize.height ) >> 1 ;
		}

		final java.awt.Rectangle to = new java.awt.Rectangle( toPoint, desiredSize );

		final int fps = 50 ;
		final int delay = 1000 / fps ;
		final int steps = Math.max( 1, duration / delay );

		this.smoothResizeTimer = new javax.swing.Timer( delay, new ActionListener() {

				private int step = 0 ;

				public void actionPerformed( ActionEvent ae ) {
					this.step ++ ;
					float t = this.step / (float) steps ;

					// easing with slope(t)
					///float slope = t ; // linear
					///float slope = (float)( -Math.cos(Math.PI * t) / 2.0 + 0.5 ) ;
					float slope = 1 - (1 - t)*(1 - t) ;

					///int x = (int)( from.x + (to.x - from.x) * slope );
					///int y = (int)( from.y + (to.y - from.y) * slope );
					int x = from.x ;
					int y = from.y ;
					int w = (int)( from.width  + ( to.width - from.width ) * slope );
					int h = (int)( from.height + (to.height - from.height) * slope );

					if ( anchorToCenter ) {
						x += ( from.width - w ) >> 1 ;
						y += ( from.height - h ) >> 1 ;
					}

					setBounds( x, y, w, h );

					if ( this.step >= steps ) {
						// timer is the source of the event
						( (javax.swing.Timer) ae.getSource() ).stop() ;

						// final deferred correction
						javax.swing.SwingUtilities.invokeLater(
							new DeferredReshaper( ListOfItemsWindow.this, to.getLocation(), to.getSize() )
						) ;
					}
				}
		} );

		this.smoothResizeTimer.start () ;
	}

	public void showItemGraphics ()
	{
		Object chosenItem = this.theList.getSelectedItem() ;
		if ( chosenItem != null ) {
			final String kind = chosenItem.toString() ;
			javax.swing.SwingUtilities.invokeLater( new Runnable() {
				public void run() {
					ItemGraphicsWindow graphicsWindow = new ItemGraphicsWindow( kind, ListOfItemsWindow.this );
					graphicsWindow.setVisible( true );
				}
			} );
		}
	}

}


class ItemDescriptionPanel extends JPanel
{

	private int row ;

	ItemDescriptionPanel( )
	{
		super( new java.awt.GridBagLayout() );

		this.row = 0 ;
	}

	void addTwoLabels( FixedHeightLabel first, FixedHeightLabel second )
	{
		if ( first == null ) first = new FixedHeightLabel( "➡️ 1st label is null ⬅️" );
		if ( second == null ) second = new FixedHeightLabel( "➡️ 2nd label is null ⬅️" );

		int rowHeight = Math.max( first.getFontMetrics( first.getFont() ).getHeight(),
						second.getFontMetrics(second.getFont()).getHeight() );
		// left column
		{
			java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints() ;

			gbc.gridx = 0 ;
			gbc.gridy = this.row ;
			gbc.weightx = 0.9 ;
			gbc.weighty = 1 ;
			gbc.anchor = java.awt.GridBagConstraints.EAST ;
			gbc.fill = java.awt.GridBagConstraints.HORIZONTAL ;
			gbc.insets = new java.awt.Insets( /* top */ 3, /* left */ 20, /* bottom */ 3, /* right */ 10 );

			first.setHorizontalAlignment( javax.swing.SwingConstants.RIGHT );
			first.setFixedHeight( rowHeight );

			super.add( first, gbc );
		}
		// right column
		{
			java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints() ;

			gbc.gridx = 1 ;
			gbc.gridy = this.row ;
			gbc.weightx = 1 ;
			gbc.weighty = 1 ;
			gbc.anchor = java.awt.GridBagConstraints.WEST ;
			gbc.fill = java.awt.GridBagConstraints.HORIZONTAL ;
			gbc.insets = new java.awt.Insets( /* top */ 3, /* left */ 10, /* bottom */ 3, /* right */ 20 );

			second.setHorizontalAlignment( javax.swing.SwingConstants.LEFT );
			second.setFixedHeight( rowHeight );

			super.add( second, gbc );
		}

		this.row ++ ;
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
			ItemFramesAndAnimationPanel framesPanel = new ItemFramesAndAnimationPanel( framesPerOrientation, item.getDelayBetweenFrames() );

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
			ItemFramesAndAnimationPanel extraFramesPanel = new ItemFramesAndAnimationPanel( extraFrames, /* not animated */ 0 );

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


class ItemFramesAndAnimationPanel extends JPanel implements java.awt.event.MouseListener
{

	// 0 here makes the frame sequence not animated
	private int delayBetweenFrames ;

	ItemFramesAndAnimationPanel( int frames ) {  this( frames, AnimatedItem.default_delay_between_frames );  }

	private JPanel panelWithFrames ;
	private JPanel panelForAnimation ;

	private javax.swing.Timer animationTimer ;

	ItemFramesAndAnimationPanel( int frames, int delay )
	{
		super( );

		if ( frames < 1 )
			throw new IllegalArgumentException( "the number of frames (" + frames + ") is less than 1" );

		this.delayBetweenFrames = ( frames > 1 && delay > 0 ) ? delay : /* not animated */ 0 ;

		this.panelWithFrames = new JPanel() ;
		this.panelWithFrames.setLayout( new java.awt.GridLayout( /* rows */ 2, /* columns */ frames, /* h gap */ 10, /* v gap */ 0 ) );

		if ( this.delayBetweenFrames > 0 ) {
			this.panelForAnimation = new JPanel() ;
			this.panelForAnimation.setLayout( new java.awt.GridLayout( /* rows */ 2, /* columns */ 1, /* h gap */ 10, /* v gap */ 0 ) );
		}

		this.animationTimer = null ;

		super.setLayout( new java.awt.FlowLayout( java.awt.FlowLayout.CENTER ) );
		super.setBorder( new javax.swing.border.EmptyBorder( 20, 20, 20, 20 ) ) ;
		super.add( this.panelWithFrames );

		super.addMouseListener( this );
	}

	/* @Override */
	public java.awt.Component add( java.awt.Component what )
	{
		///throw new UnsupportedOperationException( "use addImage or addText" );
		return this.panelWithFrames.add( what );
	}

	JLabel addImage( java.awt.image.BufferedImage image )
	{
		return (JLabel) this.panelWithFrames.add( new JLabel( new javax.swing.ImageIcon( image ), JLabel.CENTER ) );
	}

	JLabel addText( String text )
	{
		return (JLabel) this.panelWithFrames.add( new JLabel( text, JLabel.CENTER ) );
	}

	public void mouseEntered( java.awt.event.MouseEvent e ) {}
	public void mouseExited( java.awt.event.MouseEvent e ) {}

	public void mousePressed( java.awt.event.MouseEvent e ) {}
	public void mouseReleased( java.awt.event.MouseEvent e ) {}

	/* it is invoked when a mouse button is pressed and then released
	   at the same coordinates within the same component
	*/
	public void mouseClicked( java.awt.event.MouseEvent me )
	{
		this.toggleFramesAndAnimation() ;
	}

	private boolean showingAnimation = false ;

	boolean isShowingAnimation () {  return this.showingAnimation ;  }

	void toggleFramesAndAnimation ()
	{
		if ( this.delayBetweenFrames == 0 ) return ;

		this.showingAnimation = ! this.showingAnimation ;

		if ( this.showingAnimation ) {
			super.remove( this.panelWithFrames );
			if ( this.animationTimer == null )
			{
				java.awt.Image[] frames = ItemFramesAndAnimationPanel.getImagesFromLabels( this.panelWithFrames.getComponents() );

				final int howManyFrames = frames.length ;
				if ( ( howManyFrames & 1 ) == 1 )
					throw new head.over.heels.UnlikelyToHappenException( "odd number of frames + shadows" );

				final javax.swing.ImageIcon[] icons = new javax.swing.ImageIcon[ howManyFrames ];
				for ( int n = 0 ; n < howManyFrames ; ++ n )
					icons[ n ] = new javax.swing.ImageIcon( frames[ n ] );

				final JLabel animationFrameLabel = new JLabel( icons[ 0 ], JLabel.CENTER );
				this.panelForAnimation.add( animationFrameLabel );

				final int shadowsBeginAt = howManyFrames >> 1 ;
				final JLabel animationShadowLabel = new JLabel( icons[ shadowsBeginAt ], JLabel.CENTER );
				this.panelForAnimation.add( animationShadowLabel );

				this.animationTimer
					= new javax.swing.Timer(
						delayBetweenFrames,
						new ActionListener ()
						{
							private int currentFrame = 0 ;

							public void actionPerformed( ActionEvent e ) {
								if ( ! panelForAnimation.isShowing() ) return ;

								this.currentFrame ++ ;
								if ( this.currentFrame == shadowsBeginAt ) currentFrame = 0 ;

								animationFrameLabel.setIcon( icons[ this.currentFrame ] );
								animationShadowLabel.setIcon( icons[ this.currentFrame + shadowsBeginAt ] );
							}
						} );
				this.animationTimer.start() ;
			}
			super.add( this.panelForAnimation );
		}
		else {
			super.remove( this.panelForAnimation );
			super.add( this.panelWithFrames );
		}

		super.revalidate() ;
		super.repaint() ;
	}

	private static final java.awt.Image[] getImagesFromLabels( java.awt.Component[] components )
	{
		if ( components == null ) return null ;

		java.util.Vector< java.awt.Image > images = new java.util.Vector< java.awt.Image >( components.length );

		for ( java.awt.Component component : components ) {
			if ( component instanceof JLabel ) {
				javax.swing.Icon labelIcon = ( (JLabel) component ).getIcon() ;
				if ( labelIcon instanceof javax.swing.ImageIcon ) {
					java.awt.Image image = ( (javax.swing.ImageIcon) labelIcon ).getImage() ;
					images.add( image );
				}
			}
		}

		return images.toArray( new java.awt.Image[ images.size() ] );
	}

}
