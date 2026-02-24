// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2026 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels.behaviors ;


public class Activity
{

	private final String letters ;

	public final boolean equals( Object that ) {
		return ( that instanceof Activity ) ? this.equals( (Activity) that ) : false ;
	}
	public final boolean equals( Activity that ) {
		return ( that == this ) || that.letters.equals( this.letters );
	}

	public final int hashCode () {  return 293339 + this.letters.hashCode() ;  }

	/* constants */

	public static final Activity  Waiting		= new Activity( "waiting" );
	public static final Activity  Blinking		= new Activity( "blinking" );

	public static final Activity  Moving		= new Activity( "moving" );

	public static final Activity  Automoving	= new Activity( "automoving" );

	public static final Activity  Jumping		= new Activity( "jumping" );
	public static final Activity  Falling		= new Activity( "falling" );
	public static final Activity  Gliding		= new Activity( "gliding" );

	public static final Activity  MetLethalItem	= new Activity( "met a lethal item" );
	public static final Activity  Vanishing		= new Activity( "vanishing" );

	protected Activity( String text ) {
		if ( text == null ) throw new IllegalArgumentException( "hey how could one do new Activity(null)" ) ;
		this.letters = text ;
	}

}

class ActivityOfElevator extends Activity
{

	protected ActivityOfElevator( String text ) {  super( text );  }

	public static final ActivityOfElevator  GoingDown	= new ActivityOfElevator( "elevator is going down" );
	public static final ActivityOfElevator  GoingUp		= new ActivityOfElevator( "elevator is going up" );
	public static final ActivityOfElevator  ReachedBottom	= new ActivityOfElevator( "elevator reached bottom" );
	public static final ActivityOfElevator  ReachedTop	= new ActivityOfElevator( "elevator reached top" );

}
