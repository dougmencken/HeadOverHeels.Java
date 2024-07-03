// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2024 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels.gui ;


/**
 * The foundation for creating elements of the user interface
 */

public abstract class Widget implements head.over.heels.Drawable, KeyHandler
{

	// where is this widget on the containing slide
	private int whereX ;
	private int whereY ;

	public int getX () {  return this.whereX ;  }
	public int getY () {  return this.whereY ;  }

	protected void setX ( int x ) {  this.whereX = x ;  }
	protected void setY ( int y ) {  this.whereY = y ;  }

	public void moveTo ( int x, int y ) {  setX( x ); setY( y );  }

	private Slide onWhichSlide = null ;

	public Slide getContainingSlide () {  return this.onWhichSlide ;  }
	public void setContainingSlide( Slide theSlide ) {  this.onWhichSlide = theSlide ;  }

	public boolean isOnSomeSlide() {  return this.onWhichSlide != null ;  }

	public Widget( )
	{
		this( 0, 0 );
	}

	public Widget( int x, int y )
	{
		this.whereX = x ;
		this.whereY = y ;
	}

	// .....

}
