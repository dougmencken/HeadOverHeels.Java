// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2025 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels.rooms ;

class TwoJoiningRooms implements Comparable
{

	private final String firstRoom ;
	private final String secondRoom ;
	private final String howJoined ;

	public String getFirstRoom () {  return this.firstRoom ;  }
	public String getSecondRoom () {  return this.secondRoom ;  }
	public String getHowJoined () {  return this.howJoined ;  }

	public TwoJoiningRooms( String first, String second, String howLinked )
	{
		this.firstRoom = first ;
		this.secondRoom = second ;
		this.howJoined = howLinked ;
	}

	public static String backwardsTo ( String to )
	{
		if ( to.equals( "south" ) ) return "north" ;
		if ( to.equals( "north" ) ) return "south" ;

		if ( to.equals( "east" ) ) return "west" ;
		if ( to.equals( "west" ) ) return "east" ;

		if ( to.equals( "above" ) ) return "below" ;
		if ( to.equals( "below" ) ) return "above" ;

		if ( to.contains( "teleport" ) ) return /* the same teletransport */ to ;

		throw new IllegalArgumentException( "what’s backwards to \"" + to + "\" ?" ) ;
	}

	/**
	 * Reduces a triple room link ( "southeast", "southwest", "northeast", "northwest",
	 * "eastnorth", "eastsouth", "westnorth", "westsouth" ) to its first part
	 */
	public static String untriplize ( String in )
	{
		if ( in.startsWith( "south" ) ) return "south" ;
		if ( in.startsWith( "north" ) ) return "north" ;
		if ( in.startsWith( "east" ) ) return "east" ;
		if ( in.startsWith( "west" ) ) return "west" ;

		return in ;
	}

	/**
	 * Is this connection the reverse of the other one
	 */
	public boolean isReciprocalWith ( TwoJoiningRooms that )
	{
		if ( that == null ) return false ;

		if ( this.firstRoom.equals( that.secondRoom ) && this.secondRoom.equals( that.firstRoom ) ) {
			String first2second = ( this.firstRoom.contains( "triple" ) ? TwoJoiningRooms.untriplize( this.howJoined ) : this.howJoined );
			String second2first = ( that.firstRoom.contains( "triple" ) ? TwoJoiningRooms.untriplize( that.howJoined ) : that.howJoined );

			if (  first2second.equals( TwoJoiningRooms.backwardsTo( second2first ) ) )
				return true ;
		}

		return false ;
	}

	public int compareTo ( Object that )
	{
		if ( that instanceof TwoJoiningRooms )
			return compareTo( (TwoJoiningRooms) that ) ;

		String thisClassName = this.getClass().getName() ;
		throw new ClassCastException( "can only compare " + thisClassName + " to " + thisClassName ) ;
	}

	public int compareTo ( TwoJoiningRooms that )
	{
		int first = this.firstRoom.compareTo( that.firstRoom );
		if ( first != 0 ) return first ;

		int second = this.secondRoom.compareTo( that.secondRoom );
		if ( second != 0 ) return second ;

		return this.howJoined.compareTo( that.howJoined );
	}

	public String toString ()
	{
		return this.firstRoom + ", " + this.secondRoom + ", " + this.howJoined ;
	}

}

class MutuallyJoinedRooms
{

	private final TwoJoiningRooms first ;
	private final TwoJoiningRooms second ;

	public TwoJoiningRooms getFirst () {  return this.first ;  }
	public TwoJoiningRooms getSecond () {  return this.second ;  }

	public MutuallyJoinedRooms( TwoJoiningRooms first, TwoJoiningRooms second ) {
		if ( ! first.isReciprocalWith( second ) )
			throw new IllegalArgumentException( "rooms are not joined mutually" );

		this.first = first ;
		this.second = second ;
	}

}
