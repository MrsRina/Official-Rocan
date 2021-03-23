package me.rina.turok.util;

import me.rina.turok.hardware.mouse.TurokMouse;

/**
 * @author SrRina
 * @since 27/07/20 at 12:45pm
 */
public class TurokRect {
	public float x;
	public float y;

	public float width;
	public float height;

	/**
	 * A tag to class, to state, name or tag.
	 */
	public String tag;

	/**
	 * The current docking.
	 */
	protected Dock docking = Dock.TopLeft;

	public TurokRect(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;

		this.width  = width;
		this.height = height;

		this.tag = "";
	}

	public TurokRect(String tag, float x, float y, float width, float height) {
		this.x = x;
		this.y = y;

		this.width  = width;
		this.height = height;

		this.tag = "";
	}

	public TurokRect(float x, float y) {
		this.x = x;
		this.y = y;

		this.width  = 0;
		this.height = 0;

		this.tag = "";
	}

	public TurokRect(String tag, float x, float y) {
		this.x = x;
		this.y = y;

		this.width  = 0;
		this.height = 0;

		this.tag = tag;		
	}

	/**
	 * Dock where the rect can hit, to get you use getDockHit();
	 */
	public enum Dock {
		TopLeft,    TopCenter,    TopRight,
		CenterLeft, Center, CenterRight,
		BottomLeft, BottomCenter, BottomRight;
	}

	public void copy(TurokRect rect) {
		this.x = rect.getX();
		this.y = rect.getY();

		this.width = rect.getWidth();
		this.height = rect.getHeight();
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public String getTag() {
		return tag;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public float getDistance(TurokRect rect) {
		float calculatedX = this.x - rect.getX();
		float calculatedY = this.y - rect.getY();

		float calculatedW = (this.x + this.width) - (rect.getX() + rect.getWidth());
		float calculatedH = (this.y + this.height) - (rect.getY() + rect.getHeight());

		return TurokMath.sqrt(calculatedX * calculatedW + calculatedY * calculatedH);
	}

	public float getDistance(float x, float y) {
		float calculatedX = this.x - x;
		float calculatedY = this.y - y;

		float calculatedW = (this.x + this.width) - x;
		float calculatedH = (this.y + this.height) - y;

		return TurokMath.sqrt(calculatedX * calculatedW + calculatedY * calculatedH);
	}

	public boolean collideWithMouse(TurokMouse mouse) {
		return mouse.getX() >= this.x && mouse.getX() <= (this.x + this.width) && mouse.getY() >= this.y && mouse.getY() <= (this.y + this.height);
	}

	public boolean collideWithRect(TurokRect rect) {
		return this.x <= (rect.getX() + rect.getWidth()) && (this.x + this.width) >= rect.getX() && this.y <= (rect.getY() + rect.getHeight()) && (this.y + this.height) >= rect.getY();
	}

	/**
	 * Return the current dock is hitting.
	 *
	 * @param display - The TurokDisplay to get current scale width and height.
	 * @param diff    - The difference hit, for example the diff of x and 0.
	 * @return        - Return the current dock enum.
	 */
	public Dock getDockHit(TurokDisplay display, int diff) {
		if (this.x <= diff) {
			if (this.docking == Dock.TopCenter || this.docking == Dock.TopRight) {
				this.docking = Dock.TopLeft;
			} else if (this.docking == Dock.Center || this.docking == Dock.CenterRight) {
				this.docking = Dock.CenterLeft;
			} else if (this.docking == Dock.BottomCenter || this.docking == Dock.BottomRight) {
				this.docking = Dock.BottomLeft;
			}
		}

		if (this.y <= diff) {
			if (this.docking == Dock.CenterLeft || this.docking == Dock.BottomLeft) {
				this.docking = Dock.TopLeft;
			} else if (this.docking == Dock.Center || this.docking == Dock.BottomCenter) {
				this.docking = Dock.TopCenter;
			} else if (this.docking == Dock.CenterRight || this.docking == Dock.BottomRight) {
				this.docking = Dock.TopRight;
			}
		}

		if (this.x >= ((display.getScaledWidth() / 2) - ((this.width + diff) / 2))) {
			if (this.docking == Dock.TopLeft || this.docking == Dock.TopRight) {
				this.docking = Dock.TopCenter;
			} else if (this.docking == Dock.CenterLeft || this.docking == Dock.CenterRight) {
				this.docking = Dock.Center;
			} else if (this.docking == Dock.BottomLeft || this.docking == Dock.BottomRight) {
				this.docking = Dock.BottomCenter;
			}
		}

		if (this.y >= ((display.getScaledHeight() / 2) - ((this.height + diff) / 2))) {
			if (this.docking == Dock.TopLeft || this.docking == Dock.BottomLeft) {
				this.docking = Dock.CenterLeft;
			} else if (this.docking == Dock.TopCenter || this.docking == Dock.BottomCenter) {
				this.docking = Dock.Center;
			} else if (this.docking == Dock.TopRight || this.docking == Dock.BottomRight) {
				this.docking = Dock.CenterRight;
			}
		}

		if (this.x + this.width >= (display.getScaledWidth() - (this.width + diff))) {
			if (this.docking == Dock.TopLeft || this.docking == Dock.TopCenter) {
				this.docking = Dock.TopRight;
			} else if (this.docking == Dock.CenterLeft || this.docking == Dock.Center) {
				this.docking = Dock.CenterRight;
			} else if (this.docking == Dock.BottomLeft || this.docking == Dock.BottomCenter) {
				this.docking = Dock.BottomRight;
			}
		}

		if (this.y + this.height >= (display.getScaledWidth() - (this.height + diff))) {
			if (this.docking == Dock.TopLeft || this.docking == Dock.CenterLeft) {
				this.docking = Dock.BottomLeft;
			} else if (this.docking == Dock.TopCenter || this.docking == Dock.Center) {
				this.docking = Dock.BottomCenter;
			} else if (this.docking == Dock.TopRight || this.docking == Dock.CenterRight) {
				this.docking = Dock.BottomRight;
			}
		}

		return this.docking;
	}

	public static boolean collideRectWith(TurokRect rect, TurokMouse mouse) {
		return rect.collideWithMouse(mouse);
	}

	public static boolean collideRectWith(TurokRect rect, TurokRect rect1) {
		return rect.collideWithRect(rect1);
	}
}