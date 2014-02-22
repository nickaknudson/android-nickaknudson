/**
 * 
 */
package com.nickaknudson.android.animations;

import com.nickaknudson.mva.callbacks.Callback;

import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * @author nick
 *
 */
public class PositionAnimation extends Animation {
	
	private Float toX;
	private Float toY;
	private Float fromX;
	private Float fromY;
	private PositionAnimationCallback callback;

	/**
	 * @param toX
	 * @param toY
	 * @param callback 
	 */
	public PositionAnimation(float toX, float toY, PositionAnimationCallback callback) {
		this.toX = toX;
		this.toY = toY;
		this.callback = callback;
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		if(fromX == null) fromX = callback.fromX();
		if(fromY == null) fromY = callback.fromY();
		float dx = 0;
		float dy = 0;
		if (fromX != toX) {
			//dx = fromX + ((toX - fromX) * interpolatedTime);
			dx = ((toX - fromX) * interpolatedTime);
		}
		if (fromY != toY) {
			//dy = fromY + ((toY - fromY) * interpolatedTime);
			dy = ((toY - fromY) * interpolatedTime);
		}
		t.getMatrix().setTranslate(dx, dy);
	}
	
	/**
	 * @author nick
	 */
	public interface PositionAnimationCallback extends Callback {
		/**
		 * @return
		 */
		public float fromX();
		/**
		 * @return
		 */
		public float fromY();
	}
}
