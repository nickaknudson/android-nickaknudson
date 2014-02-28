package com.nickaknudson.android.animations;

import android.view.View;
import android.view.animation.Animation;

import com.nickaknudson.mva.callbacks.Callback;

/**
 * @author nick
 */
public interface AnimationCallback extends Callback {
	/**
	 * @param view
	 * @param animation
	 */
	public void onAnimation(View view, Animation animation);
}
