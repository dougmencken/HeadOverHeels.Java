// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2025 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels.items ;

import head.over.heels.behaviors.Behaviour ;
import head.over.heels.behaviors.PlayerCharacter ;


/**
 * An avatar is a game character controlled by the player
 */

public class AvatarItem extends FreeItem
{

	public AvatarItem( DescriptionOfItem description, int x, int y, int z, String heading )
	{
		super( description, x, y, z, heading );
		characterToBehavior() ;
	}

	// the copy constructor
	public AvatarItem( AvatarItem that )
	{
		super( that );
		characterToBehavior() ;
	}

	// set the behavior according to the name of character
	private void characterToBehavior ()
	{
		if ( isHead() ) setBehaviourOf( "Head" );
		else if ( isHeels() ) setBehaviourOf( "Heels" );
		else if ( isHeadOverHeels() ) setBehaviourOf( "Head over Heels" ) ;
	}

	boolean isHead () {  return getOriginalKind().equalsIgnoreCase( "head" ) ;  }
	boolean isHeels () {  return getOriginalKind().equalsIgnoreCase( "heels" ) ;  }
	boolean isHeadOverHeels () {  return getOriginalKind().equalsIgnoreCase( "headoverheels" ) ;  }

	/**
	 * Updates the character’s behavior according to the player’s actions
	 */
	public void behaveCharacter ()
	{
		Behaviour behavior = getBehaviour() ;
		if ( behavior != null && behavior instanceof PlayerCharacter )
			( (PlayerCharacter) behavior ).behave ();
	}

	public boolean isActiveCharacter ()
	{
		if ( getMediator() == null || getMediator().getActiveCharacter() == null ) return false ;
		return getMediator().getActiveCharacter().getUniqueName().equals( getUniqueName() );
	}

	@Override
	public void metamorphInto ( String newKind )
	{
		// when the composite character morphs into bubbles, it’s actually double bubbles
		boolean doubleBubbles = ( isHeadOverHeels() && newKind == "bubbles" );
		super.metamorphInto( doubleBubbles ? "double-bubbles" : newKind );
	}

	///// with ‘caused by’ for debugging
	///public void metamorphInto ( String newKind, String causedBy )
	///{
	///	System.out.println( "metamorphosis of " + StringUtilities.putInQuotes( getUniqueName() )
	///				+ " into " + StringUtilities.putInQuotes( newKind )
	///				+ " caused by " + StringUtilities.putInSingleQuotes( initiatedBy ) );
	///	this.metamorphInto( newKind );
	///}

	/**
	 * When a character takes some item, the description of that item is stored here
	 */
	private DescriptionOfItem descriptionOfTakenItem = null ;
	private String behaviourOfTakenItem = "" ;

	public DescriptionOfItem getDescriptionOfTakenItem () {  return this.descriptionOfTakenItem ;  }
	public String getBehaviorOfTakenItem () {  return this.behaviourOfTakenItem ;  }

	public void putItemInTheBag ( String itsKind, String itsBehavior )
	{
		this.descriptionOfTakenItem = ItemDescriptions.descriptions().getDescriptionByKind( itsKind ) ;
		this.behaviourOfTakenItem = itsBehavior ;
	}

	public void emptyTheBag ()
	{
		this.descriptionOfTakenItem = null ;
		this.behaviourOfTakenItem = "" ;
	}

}
