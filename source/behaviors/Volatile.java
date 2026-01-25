// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2026 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels.behaviors ;

import head.over.heels.items.DescribedItem ;


/**
 * The behavior of item that disappears for some reason, like
 * over time or when another item touches it
 */

public class Volatile extends Behaviour
{

	public boolean update ()
	{
		boolean present = true ;
		// ....
		present = false ;
		// .....
		return present ;
	}

	public Volatile( DescribedItem item, String name )
	{
		super( item, name );
		this.solid = false ;
	}

	/**
	 * is true when this item isn’t currently volatile, such as when a switch is toggled
	 */
	private boolean solid ;

	// string constants found in room description files

	public static final String on_contact = "vanishing on contact" ;
	public static final String when_above = "vanishing when some free dude is above" ;
	public static final String when_above_slower = "slowly " + when_above ; // "slowly vanishing when some free dude is above"
	public static final String as_Head_appears = "vanishing as soon as Head appears" ;
	public static final String after_a_while = "vanishing after a while" ;

}
