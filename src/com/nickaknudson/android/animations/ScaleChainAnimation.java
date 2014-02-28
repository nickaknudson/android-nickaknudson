/**
 * 
 */
package com.nickaknudson.android.animations;

import com.nickaknudson.mva.callbacks.Callback;

import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * An animation that controls the scale of an object. You can specify the point
 * to use for the center of scaling.
 * 
 */
public class ScaleChainAnimation extends Animation {

	private Float mFromX;
	private float mToX;
	private Float mFromY;
	private float mToY;

	private Float mPivotX;
	private Float mPivotY;

	private ScaleChainAnimationCallback callback;

	/**
	 * Constructor to use when building a ScaleAnimation from code
	 * 
	 * @param toX Horizontal scaling factor to apply at the end of the animation
	 * @param toY Vertical scaling factor to apply at the end of the animation
	 * @param callback
	 */
	public ScaleChainAnimation(float toX, float toY, ScaleChainAnimationCallback callback) {
		mToX = toX;
		mToY = toY;
		this.callback = callback;
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		if (mFromX == null)
			mFromX = callback.fromScaleX(mToX);
		if (mFromY == null)
			mFromY = callback.fromScaleY(mToY);
		if (mPivotX == null) 
			mPivotX = callback.pivotX();
		if (mPivotY == null) 
			mPivotY = callback.pivotY();
		float sx = 1.0f;
		float sy = 1.0f;
		float scale = getScaleFactor();

		if (mFromX != 1.0f || mToX != 1.0f) {
			sx = mFromX + ((mToX - mFromX) * interpolatedTime);
		}
		if (mFromY != 1.0f || mToY != 1.0f) {
			sy = mFromY + ((mToY - mFromY) * interpolatedTime);
		}

		if (mPivotX == 0 && mPivotY == 0) {
			t.getMatrix().setScale(sx, sy);
		} else {
			t.getMatrix().setScale(sx, sy, scale * mPivotX, scale * mPivotY);
		}
	}

	/**
	 * @author nick
	 */
	public interface ScaleChainAnimationCallback extends Callback {
		/**
		 * @param toScaleX 
		 * @return fromScaleX
		 */
		public float fromScaleX(Float toScaleX);

		/**
		 * @param toScaleY 
		 * @return fromScaleY
		 */
		public float fromScaleY(Float toScaleY);

		/**
		 * @return pivotX
		 */
		public Float pivotX();

		/**
		 * @return pivotY
		 */
		public Float pivotY();
	}
}
