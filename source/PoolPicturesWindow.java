// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2026 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels ;

import javax.swing.JFrame ;
import javax.swing.JPanel ;

import javax.swing.JComboBox ;
import javax.swing.JLabel ;
import javax.swing.ImageIcon ;

import head.over.heels.gui.swing.SmoothResizer ;


public class PoolPicturesWindow extends JFrame implements PoolListener, java.awt.event.ActionListener
{
	private PoolOfPictures thePool ;

	private JComboBox< String > listOfNames ;

	private JLabel picture ;
	private JLabel nameOfPicture ;

	public PoolPicturesWindow( PoolOfPictures pool )
	{
		super( "pictures in the pool" );

		super.setResizable( false );
		super.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );

		JPanel panel = new JPanel() ; // will become the content pane eventually
		panel.setLayout( new javax.swing.BoxLayout( panel, javax.swing.BoxLayout.Y_AXIS ) );
		panel.setBorder( new javax.swing.border.EmptyBorder( 20, 20, 20, 20 ) ) ;

		String[] allNames = new String[ pool.howManyPictures() ];
		if ( allNames.length > 0 ) {
			java.util.SortedSet< String > sortedKeys = new java.util.TreeSet< String >( pool.getAllPictures().keySet() );
			allNames = sortedKeys.toArray( allNames );
		}

		this.listOfNames = new JComboBox< String >( allNames );

		this.listOfNames.setMaximumRowCount( 20 );
		this.listOfNames.setSelectedIndex( allNames.length - 1 );
		this.listOfNames.addActionListener( this );

		panel.add( this.listOfNames );
		panel.add( javax.swing.Box.createVerticalStrut( 20 ) );

		JPanel pictureWithName = new JPanel( new java.awt.BorderLayout( /* hgap */ 10, /* vgap */ 10 ) ) ;

		NamedOffscreenImage selectedImage = pool.pictureByKey( this.listOfNames.getSelectedItem().toString() );
		this.picture = new JLabel( new ImageIcon( selectedImage ), JLabel.CENTER );
		this.nameOfPicture = new JLabel( selectedImage.getName(), JLabel.CENTER );
		pictureWithName.add( this.picture, java.awt.BorderLayout.CENTER );
		pictureWithName.add( this.nameOfPicture, java.awt.BorderLayout.SOUTH );

		panel.add( pictureWithName );
		panel.add( javax.swing.Box.createVerticalStrut( 10 ) );

		super.setContentPane( panel );

		pool.addPoolListener( this );
		this.thePool = pool ;
	}

	public void poolChanged( PoolEvent event ) {
		if ( event == null ) return ;

		String[] newNames = new String[ this.thePool.howManyPictures() ];
		if ( newNames.length > 0 ) {
			java.util.SortedSet< String > sortedKeys = new java.util.TreeSet< String >( this.thePool.getAllPictures().keySet() );
			newNames = sortedKeys.toArray( newNames );
		}

		javax.swing.DefaultComboBoxModel< String > newModel = new javax.swing.DefaultComboBoxModel< String >( newNames );
		newModel.setSelectedItem( this.listOfNames.getSelectedItem() );
		this.listOfNames.setModel( newModel );
	}

	public void actionPerformed( java.awt.event.ActionEvent e ) {
		NamedOffscreenImage selectedImage = this.thePool.pictureByKey( this.listOfNames.getSelectedItem().toString() );
		this.picture.setIcon( new ImageIcon( selectedImage ) );
		this.nameOfPicture.setText( selectedImage.getName() );

		SmoothResizer.smoothlyResize( this, super.getPreferredSize(), 250, true /* center anchoring */ );
	}

	public void dispose () {
		this.thePool.removePoolListener( this );
		super.dispose() ;
	}

}
