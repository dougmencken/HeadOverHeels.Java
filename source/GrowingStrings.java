// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2026 Douglas Mencken dougmencken@gmail.com
// Original game by Jon Ritman, Bernie Drummond and Guy Stevens, released by Ocean Software Ltd. in 1987
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels ;


/**
 * Factory class for creating GrowingString instances
 *
 * Automatically selects an implementation that either uses StringBuilder if present
 * or StringBuffer otherwise
 *
 * @see GrowingString
 */

public final class GrowingStrings {

	private static final boolean haveStringBuilder = haveStringBuilder() ;

	private static boolean haveStringBuilder() {
		try {
			Class.forName( "java.lang.StringBuilder" ) ;
			return true ;
		} catch ( Throwable fail ) {
			return false ;
		}
	}

	/**
	 * Creates an empty GrowingString
	 */
	public static GrowingString newString()
	{
		return ( haveStringBuilder )
				? new GrowingStrings.StringBuilderCover()
				: new GrowingStrings.StringBufferCover() ;
	}

	/**
	 * Creates a GrowingString containing the given initial string
	 */
	public static GrowingString newString( String initial )
	{
		return ( haveStringBuilder )
				? new GrowingStrings.StringBuilderCover( initial )
				: new GrowingStrings.StringBufferCover( initial ) ;
	}

	private GrowingStrings( ) {  super() ;  }


	/**
	 * Internal GrowingString implementation wrapping java.lang.StringBuilder
	 */
	private static final class StringBuilderCover implements GrowingString
	{
		private final StringBuilder growing ;

		StringBuilderCover() {
			this.growing = new StringBuilder() ;
		}
		StringBuilderCover( String initial ) {
			this.growing = new StringBuilder( initial ) ;
		}

		public GrowingString append( String s ) {
			this.growing.append( s );
			return this ;
		}
		public GrowingString append( Object o ) {
			this.growing.append( String.valueOf( o ) );
			return this ;
		}

		public int length() {
			return this.growing.length() ;
		}
		public void setLength( int newLength ) {
			this.growing.setLength( newLength );
		}

		public char charAt( int index ) {
			return this.growing.charAt( index );
		}

		public String toString () {
			return this.growing.toString() ;
		}
	}

	/**
	 * Internal GrowingString implementation wrapping java.lang.StringBuffer
	 */
	private static final class StringBufferCover implements GrowingString
	{
		private final StringBuffer growing ;

		StringBufferCover() {
			this.growing = new StringBuffer() ;
		}
		StringBufferCover( String initial ) {
			this.growing = new StringBuffer( initial ) ;
		}

		public GrowingString append( String s ) {
			this.growing.append( s );
			return this ;
		}
		public GrowingString append( Object o ) {
			this.growing.append( String.valueOf( o ) );
			return this ;
		}

		public int length() {
			return this.growing.length() ;
		}
		public void setLength( int newLength ) {
			this.growing.setLength( newLength );
		}

		public char charAt( int index ) {
			return this.growing.charAt( index );
		}

		public String toString () {
			return this.growing.toString() ;
		}
	}

}
