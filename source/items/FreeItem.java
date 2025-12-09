// The Java port of the free and open source remake of the game “Head over Heels”
//
// Copyright © 2025 Douglas Mencken dougmencken@gmail.com
//
// This program is free software
// You may redistribute it and~or modify it under the terms of the GNU General Public License
// either version 3 of the License or at your option any later version

package head.over.heels.items ;

import head.over.heels.Drawable ;
import head.over.heels.Masky ;
import head.over.heels.TriBool ;


/**
 * Free items may be anywhere and move within the room, such as the player characters
 */

public class FreeItem extends DescribedItem implements Drawable, Masky
{
	/**
	 * @param description the description of this item
	 * @param x the position on X
	 * @param y the position on Y
	 * @param z the position on Z, or how far is the floor
	 * @param where the angular orientation
	 */
	public FreeItem( DescriptionOfItem description, int x, int y, int z, String where )
	{
		super( description );

		this.theX = x ;
		this.theY = y ;
		this.theZ = z ;

		this.heading = where ;
	}

	// the copy constructor
	public FreeItem( FreeItem that )
	{
		super( that );
		this.theX = that.theX ;
		this.theY = that.theY ;
		this.theZ = that.theZ ;
		this.heading = that.heading ;
	}

	// the position in 3-dimensional space of this item’s lower north-west point, in free units
	private int theX ;
	private int theY ;
	private int theZ ;

	public int getX () {  return this.theX ;  }
	public int getY () {  return this.theY ;  }
	public int getZ () {  return this.theZ ;  }

	public void setX ( int newX ) {  this.theX = newX ;  }
	public void setY ( int newY ) {  this.theY = newY ;  }
	public void setZ ( int newZ ) {  this.theZ = newZ ;  }

	// the angular orientation
	private String heading ;

	public String getHeading () {  return this.heading ;  }

	public void changeHeading ( String where )
	{
		if ( ! this.heading.equals( where ) ) {
			this.heading = where ;
			setCurrentFrameSequence( this.heading );
		}
	}

	public void toTheHeadingFrameSequence () {  setCurrentFrameSequence( getHeading() );  }

	@Override
	public void metamorphInto ( String newKind )
	{
		// the new kind may not have extra frames, like for blinking
		if ( isAtExtraFrame() ) toTheHeadingFrameSequence() ;

		super.metamorphInto( newKind );
	}

	// redo the mask or not
	private TriBool wantMask ;

	// implementing Masky
	public TriBool getWantMask () {  return this.wantMask ;  }
	public void setWantMask ( TriBool wanna ) {  this.wantMask = wanna ;  }

	public void setWantMaskFalse () {  this.wantMask.setFalse() ;  }
	public void setWantMaskTrue () {  this.wantMask.setTrue() ;  }
	public void setWantMaskNeither () {  this.wantMask.setNeither() ;  }

	/**
	 * Draw this free item
	 */
	public void draw ( java.awt.Graphics2D g )
	{
		// ...
	}

}
