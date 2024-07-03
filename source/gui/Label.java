// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2024 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels.gui ;


/**
 * The line of text
 */

public class Label extends Widget
{

	// the text of this label
	private String text ;

	public String getText () {  return this.text ;  }
	public void setText( String newText ) {  this.text = newText ; update() ;  }

	// the font to draw this label
	private head.over.heels.gui.Font font ;

	public head.over.heels.gui.Font getFont () {  return this.font ;  }

	// if true the letters are colored in a cycle
	private boolean cyclicallyColoredLetters ;

	public boolean areLettersCyclicallyColored () {  return this.cyclicallyColoredLetters ;  }

	public Label( String text )
	{
		this( text, new head.over.heels.gui.Font(), false );
	}

	public Label( String text, head.over.heels.gui.Font font )
	{
		this( text, font, false );
	}

	/**
	 * @param theText the text of this label
	 * @param theFont the font to draw this label
	 * @param multicolor true for coloring the letters in a cycle
	 */
	public Label( String theText, head.over.heels.gui.Font theFont, boolean multicolor )
	{
		this.text = theText ;
		this.font = theFont ;
		this.cyclicallyColoredLetters = multicolor ;
	}

	public void update ()
	{
		// ....
	}

	public void draw ( java.awt.Graphics2D g )
	{
		// ...
	}

	public void handleKey ( String key )
	{
		// ...
	}

	// .....

}
