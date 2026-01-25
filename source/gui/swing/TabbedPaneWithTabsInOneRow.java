// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2026 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels.gui.swing ;

import javax.swing.JTabbedPane ;

import java.awt.Component ;
import java.awt.Dimension ;


public class TabbedPaneWithTabsInOneRow extends JTabbedPane
{

	public TabbedPaneWithTabsInOneRow( int tabPlacement ) {  super( tabPlacement );  }
	public TabbedPaneWithTabsInOneRow( ) {  super( JTabbedPane.TOP );  }

	public int getWidthOfAllTabs ()
	{
		int widthOfAllTabs = 0 ;
		for ( int n = 0 ; n < super.getTabCount() ; n ++ )
			widthOfAllTabs += super.getBoundsAt( n ).width ;

		return widthOfAllTabs ;
	}

	public int getHeightOfTabRow ()
	{
		int heightOfTabRow = 0 ;
		for ( int n = 0 ; n < super.getTabCount() ; n ++ )
			heightOfTabRow = Math.max( super.getBoundsAt( n ).height, heightOfTabRow );

		return heightOfTabRow ;
	}

	/* @Override */
	public Dimension getPreferredSize ()
	{
		int maxPreferredHeight = ( super.getSelectedComponent() == null ) ? 0 : super.getSelectedComponent().getPreferredSize().height ;
		for ( int n = 0 ; n < super.getTabCount() ; n ++ ) {
			Component tabComponent = super.getTabComponentAt( n ) ;
			if ( tabComponent != null )
				maxPreferredHeight = Math.max( maxPreferredHeight, tabComponent.getPreferredSize().height );
		}

		return new Dimension(
				Math.max( super.getPreferredSize().width, this.getWidthOfAllTabs() ),
				maxPreferredHeight + this.getHeightOfTabRow() );
	}

	/* @Override */
	public Dimension getMinimumSize ()
	{
		int minimumHeight = this.getHeightOfTabRow() ;
		if ( /* super.getSelectedIndex() >= 0 */ super.getSelectedComponent() != null )
			minimumHeight += super.getSelectedComponent().getMinimumSize().height ;

		return new Dimension(
				Math.max( super.getMinimumSize().width, this.getWidthOfAllTabs() ),
				minimumHeight );
	}

}
