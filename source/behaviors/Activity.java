// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2024 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels.behaviors ;


public class Activity
{

	private String letters ;

	private Activity( String text ) {  this.letters = text ;  }

	public boolean equals ( Activity that ) {  return that.letters.equals( this.letters );  }

	/* constants */

	public static final Activity  Waiting		= new Activity( "waiting" );
	public static final Activity  Blinking		= new Activity( "blinking" );

	public static final Activity  MovingNorth	= new Activity( "moving north" );
	public static final Activity  MovingSouth	= new Activity( "moving south" );
	public static final Activity  MovingEast	= new Activity( "moving east" );
	public static final Activity  MovingWest	= new Activity( "moving west" );

	public static final Activity  MovingNortheast	= new Activity( "moving northeast" );
	public static final Activity  MovingNorthwest	= new Activity( "moving northwest" );
	public static final Activity  MovingSoutheast	= new Activity( "moving southeast" );
	public static final Activity  MovingSouthwest	= new Activity( "moving southwest" );

	public static final Activity  AutomovingNorth	= new Activity( "automoving north" );
	public static final Activity  AutomovingSouth	= new Activity( "automoving south" );
	public static final Activity  AutomovingEast	= new Activity( "automoving east" );
	public static final Activity  AutomovingWest	= new Activity( "automoving west" );

	public static final Activity  GoingDown		= new Activity( "going down" );
	public static final Activity  GoingUp		= new Activity( "going up" );

}
