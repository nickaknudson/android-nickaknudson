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
public class PositionChainAnimation extends Animation {
	
	private Float toX;
	private Float toY;
	private Float fromX;
	private Float fromY;
	private PositionChainAnimationCallback callback;

	/**
	 * @param toX
	 * @param toY
	 * @param callback 
	 */
	public PositionChainAnimation(float toX, float toY, PositionChainAnimationCallback callback) {
		this.toX = toX;
		this.toY = toY;
		this.callback = callback;
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		if(interpolatedTime > 0) {
			if(fromX == null) fromX = callback.fromX(toX);
			if(fromY == null) fromY = callback.fromY(toY);
			//callback.fromY(interpolatedTime);
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
	}
	
	/**
	 * @author nick
	 */
	public interface PositionChainAnimationCallback extends Callback {
		/**
		 * @param toX 
		 * @return
		 */
		public float fromX(Float toX);
		/**
		 * @param toY 
		 * @return
		 */
		public float fromY(Float toY);
	}
}
