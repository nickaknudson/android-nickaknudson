/**
 * 
 */
package com.nickaknudson.android.animations;

/**
 * @author nick
 *
 */
public class AnimationState {
	
	private Float nextX = 0.0f;
	private Float nextY = 0.0f;
	private Float nextAlpha = 1.0f;
	private Float nextScaleX = 1.0f;
	private Float nextScaleY = 1.0f;
	
	/**
	 * @param x
	 * @param y
	 */
	public AnimationState(Float x, Float y) {
		nextX = x;
		nextY = y;
	}
	
	/**
	 * @param x
	 * @param y
	 */
	public AnimationState(Integer x, Integer y) {
		nextX = x.floatValue();
		nextY = y.floatValue();
	}
	
	/**
	 * @param x
	 * @return
	 */
	public Float shiftX(Float x) {
		Float temp;
		temp = nextX;
		nextX = x;
		return temp;
	}
	
	/**
	 * @param y
	 * @return
	 */
	public Float shiftY(Float y) {
		Float temp;
		temp = nextY;
		nextY = y;
		return temp;
	}
	
	/**
	 * @param alpha
	 * @return
	 */
	public Float shiftAlpha(Float alpha) {
		Float temp;
		temp = nextAlpha;
		nextAlpha = alpha;
		return temp;
	}
	
	/**
	 * @param x
	 * @return
	 */
	public Float shiftScaleX(Float x) {
		Float temp;
		temp = nextScaleX;
		nextScaleX = x;
		return temp;
	}
	
	/**
	 * @param y
	 * @return
	 */
	public Float shiftScaleY(Float y) {
		Float temp;
		temp = nextScaleY;
		nextScaleY = y;
		return temp;
	}
	
}
