/**
 * 
 */
package com.nickaknudson.android.animations;

import com.nickaknudson.mva.callbacks.Callback;

import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * An animation that controls the alpha level of an object. Useful for fading
 * things in and out. This animation ends up changing the alpha property of a
 * {@link Transformation}
 * 
 */
public class AlphaChainAnimation extends Animation {
	
	private Float fromAlpha;
	private Float toAlpha;
	private AlphaChainAnimationCallback callback;

	/**
	 * Constructor to use when building an AlphaAnimation from code
	 * 
	 * @param toAlpha
	 *            Ending alpha value for the animation.
	 * @param callback 
	 */
	public AlphaChainAnimation(float toAlpha, AlphaChainAnimationCallback callback) {
		this.toAlpha = toAlpha;
		this.callback = callback;
	}

	/**
	 * Changes the alpha property of the supplied {@link Transformation}
	 */
	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		if(interpolatedTime > 0) {
			if(fromAlpha == null) fromAlpha = callback.fromAlpha(toAlpha);
			final float alpha = fromAlpha;
			t.setAlpha(alpha + ((toAlpha - alpha) * interpolatedTime));
		}
	}

	@Override
	public boolean willChangeTransformationMatrix() {
		return false;
	}

	@Override
	public boolean willChangeBounds() {
		return false;
	}

	/**
	 * @hide
	 *
	@Override
	public boolean hasAlpha() {
		return true;
	}
	
	/**
	 * @author nick
	 */
	public interface AlphaChainAnimationCallback extends Callback {
		/**
		 * @param toAlpha 
		 * @return
		 */
		public float fromAlpha(Float toAlpha);
	}
}
