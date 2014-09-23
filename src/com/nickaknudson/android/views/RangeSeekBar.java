package com.nickaknudson.android.views;

import java.math.BigDecimal;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ImageView;
import android.R;

/**
 * https://code.google.com/p/range-seek-bar/
 *
 * Widget that lets users select a minimum and maximum value on a given numerical range. The range value types can be one of Long, Double, Integer,
 * Float, Short, Byte or BigDecimal.<br />
 * <br />
 * Improved {@link MotionEvent} handling for smoother use, anti-aliased painting for improved aesthetics.
 *
 * @author Stephan Tittel (stephan.tittel@kom.tu-darmstadt.de)
 * @author Peter Sinnott (psinnott@gmail.com)
 * @author Thomas Barrasso (tbarrasso@sevenplusandroid.org)
 *
 * @param <T>
 *          The Number type of the range values. One of Long, Double, Integer, Float, Short, Byte or BigDecimal.
 */
public class RangeSeekBar<T extends Number> extends ImageView {
	private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Bitmap mThumbImage = BitmapFactory.decodeResource(getResources(), R.drawable.radiobutton_on_background);
	private Bitmap mThumbPressedImage = BitmapFactory.decodeResource(getResources(), R.drawable.radiobutton_on_background);
	private float mThumbWidth = mThumbImage.getWidth();
	private float mThumbHalfWidth = 0.5f * mThumbWidth;
	private float mThumbHalfHeight = 0.5f * mThumbImage.getHeight();
	private float mLineHeight = 0.3f * mThumbHalfHeight;
    private float mAlineHeight = 0.2f * mThumbHalfHeight;
    private float mPadding = mThumbHalfWidth;
	private final T mAbsoluteMinValue, mAbsoluteMaxValue;
	private final NumberType mNumberType;
	private final double mAbsoluteMinValuePrim, mAbsoluteMaxValuePrim;
	private double mNormalizedMinValue = 0d;
	private double mNormalizedMaxValue = 1d;
	private Thumb mPressedThumb = null;
	private boolean mNotifyWhileDragging = false;
	private OnRangeSeekBarChangeListener<T> mListener;
    private RectF mRect = new RectF();
    private RectF mArect = new RectF();
    /**
	 * Default color of a {@link RangeSeekBar}, #FF33B5E5. This is also known as "Ice Cream Sandwich" blue.
	 */
	private final int DEFAULT_COLOR = Color.argb(0xFF, 0x33, 0xB5, 0xE5);
	private int mProgressBackgroundColor = Color.GRAY;
	private int mProgressColor = DEFAULT_COLOR;
	/**
	 * An invalid pointer id.
	 */
	public static final int INVALID_POINTER_ID = 255;
	
	// Localized constants from MotionEvent for compatibility
	// with API < 8 "Froyo".
	public static final int ACTION_POINTER_UP = 0x6, ACTION_POINTER_INDEX_MASK = 0x0000ff00, ACTION_POINTER_INDEX_SHIFT = 8;
	
	private float mDownMotionX;
	private int mActivePointerId = INVALID_POINTER_ID;
	
	/**
	 * On touch, this offset plus the scaled value from the position of the touch will form the progress value. Usually 0.
	 */
	float mTouchProgressOffset;
	
	private int mScaledTouchSlop;
	private boolean mIsDragging;
	
	/**
	 * Creates a new RangeSeekBar.
	 *
	 * @param absoluteMinValue
	 *          The minimum value of the selectable range.
	 * @param absoluteMaxValue
	 *          The maximum value of the selectable range.
	 * @param context
	 * @throws IllegalArgumentException
	 *           Will be thrown if min/max value type is not one of Long, Double, Integer, Float, Short, Byte or BigDecimal.
	 */
	public RangeSeekBar(T absoluteMinValue, T absoluteMaxValue, Context context) throws IllegalArgumentException {
		super(context);
		this.mAbsoluteMinValue = absoluteMinValue;
		this.mAbsoluteMaxValue = absoluteMaxValue;
		mAbsoluteMinValuePrim = absoluteMinValue.doubleValue();
		mAbsoluteMaxValuePrim = absoluteMaxValue.doubleValue();
		mNumberType = NumberType.fromNumber(absoluteMinValue);
		
		// make RangeSeekBar focusable. This solves focus handling issues in case EditText widgets are being used along with the RangeSeekBar within
		// ScollViews.
		setFocusable(true);
		setFocusableInTouchMode(true);
		
		if (!isInEditMode()) {
			init();
		}
	}
	
	private final void init() {
		mScaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
	}
	
	public boolean isNotifyWhileDragging() {
		return mNotifyWhileDragging;
	}
	
	/**
	 * Should the widget notify the listener callback while the user is still dragging a thumb? Default is false.
	 *
	 * @param flag
	 */
	public void setNotifyWhileDragging(boolean flag) {
		this.mNotifyWhileDragging = flag;
	}
	
	/**
	 * Returns the absolute minimum value of the range that has been set at construction time.
	 *
	 * @return The absolute minimum value of the range.
	 */
	public T getmAbsoluteMinValue() {
		return mAbsoluteMinValue;
	}
	
	/**
	 * Returns the absolute maximum value of the range that has been set at construction time.
	 *
	 * @return The absolute maximum value of the range.
	 */
	public T getAbsoluteMaxValue() {
		return mAbsoluteMaxValue;
	}
	
	/**
	 * Returns the currently selected min value.
	 *
	 * @return The currently selected min value.
	 */
	public T getSelectedMinValue() {
		return normalizedToValue(mNormalizedMinValue);
	}
	
	/**
	 * Sets the currently selected minimum value. The widget will be invalidated and redrawn.
	 *
	 * @param value
	 *          The Number value to set the minimum value to. Will be clamped to given absolute minimum/maximum range.
	 */
	public void setSelectedMinValue(T value) {
		// in case mAbsoluteMinValue == mAbsoluteMaxValue, avoid division by zero when normalizing.
		if (0 == (mAbsoluteMaxValuePrim - mAbsoluteMinValuePrim)) {
			setNormalizedMinValue(0d);
		}
		else {
			setNormalizedMinValue(valueToNormalized(value));
		}
	}
	
	/**
	 * Returns the currently selected max value.
	 *
	 * @return The currently selected max value.
	 */
	public T getSelectedMaxValue() {
		return normalizedToValue(mNormalizedMaxValue);
	}
	
	/**
	 * Sets the currently selected maximum value. The widget will be invalidated and redrawn.
	 *
	 * @param value
	 *          The Number value to set the maximum value to. Will be clamped to given absolute minimum/maximum range.
	 */
	public void setSelectedMaxValue(T value) {
		// in case mAbsoluteMinValue == mAbsoluteMaxValue, avoid division by zero when normalizing.
		if (0 == (mAbsoluteMaxValuePrim - mAbsoluteMinValuePrim)) {
			setNormalizedMaxValue(1d);
		}
		else {
			setNormalizedMaxValue(valueToNormalized(value));
		}
	}
	
	/**
	 * Registers given listener callback to notify about changed selected values.
	 *
	 * @param listener
	 *          The listener to notify about changed selected values.
	 */
	public void setOnRangeSeekBarChangeListener(OnRangeSeekBarChangeListener<T> listener) {
		this.mListener = listener;
	}
	
	/**
	 * Values in HEX, ex 0xFF
	 *
	 * @param red
	 *          hex value for red
	 * @param green
	 *          hex value for green
	 * @param blue
	 *          hex value for blue
	 */
	public void setProgressBackgroundColor(int red, int green, int blue) {
		setProgressBackgroundColor(0xFF, red, green, blue);
	}
	
	/**
	 * Values in HEX, ex 0xFF
	 * 
	 * @param alpha
	 *          hex value for alpha
	 * @param red
	 *          hex value for red
	 * @param green
	 *          hex value for green
	 * @param blue
	 *          hex value for blue
	 */
	public void setProgressBackgroundColor(int alpha, int red, int green, int blue) {
		mProgressBackgroundColor = Color.argb(alpha, red, green, blue);
	}
	
	/**
	 * Values in HEX, ex 0xFF
	 *
	 * @param red
	 *          hex value for red
	 * @param green
	 *          hex value for green
	 * @param blue
	 *          hex value for blue
	 */
	public void setProgressLineColor(int red, int green, int blue) {
		setProgressLineColor(0xFF, red, green, blue);
	}
	
	/**
	 * Values in HEX, ex 0xFF
	 * 
	 * @param alpha
	 *          hex value for alpha
	 * @param red
	 *          hex value for red
	 * @param green
	 *          hex value for green
	 * @param blue
	 *          hex value for blue
	 */
	public void setProgressLineColor(int alpha, int red, int green, int blue) {
		mProgressColor = Color.argb(alpha, red, green, blue);
	}
	
	public void setThumbImage(Bitmap thumbImage) {
		this.mThumbImage = thumbImage;
		this.mThumbPressedImage = thumbImage;
        reMesureElements();
	}
	
	public void setThumbPressedImage(Bitmap thumbPressedImage) {
		this.mThumbPressedImage = mThumbImage;
        reMesureElements();
	}
	
	/**
	 * Set progress bar height
	 * 
	 * @param height
	 *          is given in pixel
	 */
	public void setLineHeight(float height) {
		mLineHeight = height;
	}
	
	private void reMesureElements() {
        mThumbWidth = mThumbImage.getWidth();
        mThumbHalfWidth = 0.5f * mThumbWidth;
        mThumbHalfHeight = 0.5f * mThumbImage.getHeight();
        mLineHeight = 0.3f * mThumbHalfHeight;
        mPadding = mThumbHalfWidth;
	}
	
	/**
	 * Handles thumb selection and movement. Notifies listener callback on certain events.
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		if (!isEnabled())
			return false;
		
		int pointerIndex;
		
		final int action = event.getAction();
		switch (action & MotionEvent.ACTION_MASK) {
		
			case MotionEvent.ACTION_DOWN:
				// Remember where the motion event started
				mActivePointerId = event.getPointerId(event.getPointerCount() - 1);
				pointerIndex = event.findPointerIndex(mActivePointerId);
				mDownMotionX = event.getX(pointerIndex);
				
				mPressedThumb = evalPressedThumb(mDownMotionX);
				
				// Only handle thumb presses.
				if (mPressedThumb == null)
					return super.onTouchEvent(event);
				
				setPressed(true);
				invalidate();
				onStartTrackingTouch();
				trackTouchEvent(event);
				attemptClaimDrag();
				
				break;
			case MotionEvent.ACTION_MOVE:
				if (mPressedThumb != null) {
					
					if (mIsDragging) {
						trackTouchEvent(event);
					}
					else {
						// Scroll to follow the motion event
						pointerIndex = event.findPointerIndex(mActivePointerId);
						final float x = event.getX(pointerIndex);
						
						if (Math.abs(x - mDownMotionX) > mScaledTouchSlop) {
							setPressed(true);
							invalidate();
							onStartTrackingTouch();
							trackTouchEvent(event);
							attemptClaimDrag();
						}
					}
					
					if (mNotifyWhileDragging && mListener != null) {
						mListener.onRangeSeekBarValuesChanged(this, getSelectedMinValue(), getSelectedMaxValue());
					}
				}
				break;
			case MotionEvent.ACTION_UP:
				if (mIsDragging) {
					trackTouchEvent(event);
					onStopTrackingTouch();
					setPressed(false);
				}
				else {
					// Touch up when we never crossed the touch slop threshold
					// should be interpreted as a tap-seek to that location.
					onStartTrackingTouch();
					trackTouchEvent(event);
					onStopTrackingTouch();
				}
				
				mPressedThumb = null;
				invalidate();
				if (mListener != null) {
					mListener.onRangeSeekBarValuesChanged(this, getSelectedMinValue(), getSelectedMaxValue());
				}
				break;
			case MotionEvent.ACTION_POINTER_DOWN: {
				final int index = event.getPointerCount() - 1;
				// final int index = ev.getActionIndex();
				mDownMotionX = event.getX(index);
				mActivePointerId = event.getPointerId(index);
				invalidate();
				break;
			}
			case MotionEvent.ACTION_POINTER_UP:
				onSecondaryPointerUp(event);
				invalidate();
				break;
			case MotionEvent.ACTION_CANCEL:
				if (mIsDragging) {
					onStopTrackingTouch();
					setPressed(false);
				}
				invalidate(); // see above explanation
				break;
		}
		return true;
	}
	
	private final void onSecondaryPointerUp(MotionEvent ev) {
		final int pointerIndex = (ev.getAction() & ACTION_POINTER_INDEX_MASK) >> ACTION_POINTER_INDEX_SHIFT;
		
		final int pointerId = ev.getPointerId(pointerIndex);
		if (pointerId == mActivePointerId) {
			// This was our active pointer going up. Choose
			// a new active pointer and adjust accordingly.
			// TODO: Make this decision more intelligent.
			final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
			mDownMotionX = ev.getX(newPointerIndex);
			mActivePointerId = ev.getPointerId(newPointerIndex);
		}
	}
	
	private final void trackTouchEvent(MotionEvent event) {
		final int pointerIndex = event.findPointerIndex(mActivePointerId);
		final float x = event.getX(pointerIndex);
		
		if (Thumb.MIN.equals(mPressedThumb)) {
			setNormalizedMinValue(screenToNormalized(x));
		}
		else if (Thumb.MAX.equals(mPressedThumb)) {
			setNormalizedMaxValue(screenToNormalized(x));
		}
	}
	
	/**
	 * Tries to claim the user's drag motion, and requests disallowing any ancestors from stealing events in the drag.
	 */
	private void attemptClaimDrag() {
		if (getParent() != null) {
			getParent().requestDisallowInterceptTouchEvent(true);
		}
	}
	
	/**
	 * This is called when the user has started touching this widget.
	 */
	void onStartTrackingTouch() {
		mIsDragging = true;
	}
	
	/**
	 * This is called when the user either releases his touch or the touch is canceled.
	 */
	void onStopTrackingTouch() {
		mIsDragging = false;
	}
	
	/**
	 * Ensures correct size of the widget.
	 */
	@Override
	protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = 200;
		if (MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(widthMeasureSpec)) {
			width = MeasureSpec.getSize(widthMeasureSpec);
		}
		int height = mThumbImage.getHeight();
		if (MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(heightMeasureSpec)) {
			height = Math.min(height, MeasureSpec.getSize(heightMeasureSpec));
		}
		setMeasuredDimension(width, height);
	}
	
	/**
	 * Draws the widget on the given canvas.
	 */
	@Override
	protected synchronized void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// draw seek bar background line
		mRect = new RectF(mPadding, 0.5f * (getHeight() - mLineHeight), getWidth() - mPadding, 0.5f * (getHeight() + mLineHeight));
		mPaint.setStyle(Style.FILL);
		mPaint.setColor(mProgressBackgroundColor);
		mPaint.setAntiAlias(true);
		canvas.drawRect(mRect, mPaint);

        // draw seek bar active range line
        mArect.set(mPadding, 0.5f * (getHeight() - mAlineHeight), getWidth() - mPadding, 0.5f * (getHeight() + mAlineHeight));
        mArect.left = normalizedToScreen(mNormalizedMinValue);
        mArect.right = normalizedToScreen(mNormalizedMaxValue);

		// draw seek bar active range line
		mRect.left = normalizedToScreen(mNormalizedMinValue);
		mRect.right = normalizedToScreen(mNormalizedMaxValue);
		
		// orange color
		mPaint.setColor(mProgressColor);
		canvas.drawRect(mArect, mPaint);
		
		// draw minimum thumb
		drawThumb(normalizedToScreen(mNormalizedMinValue), Thumb.MIN.equals(mPressedThumb), canvas);
		
		// draw maximum thumb
		drawThumb(normalizedToScreen(mNormalizedMaxValue), Thumb.MAX.equals(mPressedThumb), canvas);
	}
	
	/**
	 * Overridden to save instance state when device orientation changes. This method is called automatically if you assign an id to the RangeSeekBar
	 * widget using the {@link #setId(int)} method. Other members of this class than the normalized min and max values don't need to be saved.
	 */
	@Override
	protected Parcelable onSaveInstanceState() {
		final Bundle bundle = new Bundle();
		bundle.putParcelable("SUPER", super.onSaveInstanceState());
		bundle.putDouble("MIN", mNormalizedMinValue);
		bundle.putDouble("MAX", mNormalizedMaxValue);
		return bundle;
	}
	
	/**
	 * Overridden to restore instance state when device orientation changes. This method is called automatically if you assign an id to the RangeSeekBar
	 * widget using the {@link #setId(int)} method.
	 */
	@Override
	protected void onRestoreInstanceState(Parcelable parcel) {
		final Bundle bundle = (Bundle) parcel;
		super.onRestoreInstanceState(bundle.getParcelable("SUPER"));
		mNormalizedMinValue = bundle.getDouble("MIN");
		mNormalizedMaxValue = bundle.getDouble("MAX");
	}
	
	/**
	 * Draws the "normal" resp. "pressed" thumb image on specified x-coordinate.
	 *
	 * @param screenCoord
	 *          The x-coordinate in screen space where to draw the image.
	 * @param pressed
	 *          Is the thumb currently in "pressed" state?
	 * @param canvas
	 *          The canvas to draw upon.
	 */
	private void drawThumb(float screenCoord, boolean pressed, Canvas canvas) {
		canvas.drawBitmap(pressed ? mThumbPressedImage : mThumbImage, screenCoord - mThumbHalfWidth, (float) ((0.5f * getHeight()) - mThumbHalfHeight), mPaint);
	}
	
	/**
	 * Decides which (if any) thumb is touched by the given x-coordinate.
	 *
	 * @param touchX
	 *          The x-coordinate of a touch event in screen space.
	 * @return The pressed thumb or null if none has been touched.
	 */
	private Thumb evalPressedThumb(float touchX) {
		Thumb result = null;
		boolean minThumbPressed = isInThumbRange(touchX, mNormalizedMinValue);
		boolean maxThumbPressed = isInThumbRange(touchX, mNormalizedMaxValue);
		if (minThumbPressed && maxThumbPressed) {
			// if both thumbs are pressed (they lie on top of each other), choose the one with more room to drag. this avoids "stalling" the thumbs in a
			// corner, not being able to drag them apart anymore.
			result = (touchX / getWidth() > 0.5f) ? Thumb.MIN : Thumb.MAX;
		}
		else if (minThumbPressed) {
			result = Thumb.MIN;
		}
		else if (maxThumbPressed) {
			result = Thumb.MAX;
		}
		return result;
	}
	
	/**
	 * Decides if given x-coordinate in screen space needs to be interpreted as "within" the normalized thumb x-coordinate.
	 *
	 * @param touchX
	 *          The x-coordinate in screen space to check.
	 * @param normalizedThumbValue
	 *          The normalized x-coordinate of the thumb to check.
	 * @return true if x-coordinate is in thumb range, false otherwise.
	 */
	private boolean isInThumbRange(float touchX, double normalizedThumbValue) {
		return Math.abs(touchX - normalizedToScreen(normalizedThumbValue)) <= mThumbHalfWidth;
	}
	
	/**
	 * Sets normalized min value to value so that 0 <= value <= normalized max value <= 1. The View will get invalidated when calling this method.
	 *
	 * @param value
	 *          The new normalized min value to set.
	 */
	public void setNormalizedMinValue(double value) {
		mNormalizedMinValue = Math.max(0d, Math.min(1d, Math.min(value, mNormalizedMaxValue)));
		invalidate();
	}
	
	/**
	 * Sets normalized max value to value so that 0 <= normalized min value <= value <= 1. The View will get invalidated when calling this method.
	 *
	 * @param value
	 *          The new normalized max value to set.
	 */
	public void setNormalizedMaxValue(double value) {
		mNormalizedMaxValue = Math.max(0d, Math.min(1d, Math.max(value, mNormalizedMinValue)));
		invalidate();
	}
	
	/**
	 * Converts a normalized value to a Number object in the value space between absolute minimum and maximum.
	 *
	 * @param normalized
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private T normalizedToValue(double normalized) {
		return (T) mNumberType.toNumber(mAbsoluteMinValuePrim + normalized * (mAbsoluteMaxValuePrim - mAbsoluteMinValuePrim));
	}
	
	/**
	 * Converts the given Number value to a normalized double.
	 *
	 * @param value
	 *          The Number value to normalize.
	 * @return The normalized double.
	 */
	private double valueToNormalized(T value) {
		if (0 == mAbsoluteMaxValuePrim - mAbsoluteMinValuePrim) {
			// prevent division by zero, simply return 0.
			return 0d;
		}
		return (value.doubleValue() - mAbsoluteMinValuePrim) / (mAbsoluteMaxValuePrim - mAbsoluteMinValuePrim);
	}
	
	/**
	 * Converts a normalized value into screen space.
	 *
	 * @param normalizedCoord
	 *          The normalized value to convert.
	 * @return The converted value in screen space.
	 */
	private float normalizedToScreen(double normalizedCoord) {
		return (float) (mPadding + normalizedCoord * (getWidth() - 2 * mPadding));
	}
	
	/**
	 * Converts screen space x-coordinates into normalized values.
	 *
	 * @param screenCoord
	 *          The x-coordinate in screen space to convert.
	 * @return The normalized value.
	 */
	private double screenToNormalized(float screenCoord) {
		int width = getWidth();
		if (width <= 2 * mPadding) {
			// prevent division by zero, simply return 0.
			return 0d;
		}
		else {
			double result = (screenCoord - mPadding) / (width - 2 * mPadding);
			return Math.min(1d, Math.max(0d, result));
		}
	}
	
	/**
	 * Callback listener interface to notify about changed range values.
	 *
	 * @author Stephan Tittel (stephan.tittel@kom.tu-darmstadt.de)
	 *
	 * @param <T>
	 *          The Number type the RangeSeekBar has been declared with.
	 */
	public interface OnRangeSeekBarChangeListener<T> {
		public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, T minValue, T maxValue);
	}
	
	/**
	 * Thumb constants (min and max).
	 */
	private static enum Thumb {
		MIN, MAX
	};
	
	/**
	 * Utility enumaration used to convert between Numbers and doubles.
	 *
	 * @author Stephan Tittel (stephan.tittel@kom.tu-darmstadt.de)
	 *
	 */
	private static enum NumberType {
		LONG, DOUBLE, INTEGER, FLOAT, SHORT, BYTE, BIG_DECIMAL;
		
		public static <E extends Number> NumberType fromNumber(E value) throws IllegalArgumentException {
			if (value instanceof Long) {
				return LONG;
			}
			if (value instanceof Double) {
				return DOUBLE;
			}
			if (value instanceof Integer) {
				return INTEGER;
			}
			if (value instanceof Float) {
				return FLOAT;
			}
			if (value instanceof Short) {
				return SHORT;
			}
			if (value instanceof Byte) {
				return BYTE;
			}
			if (value instanceof BigDecimal) {
				return BIG_DECIMAL;
			}
			throw new IllegalArgumentException("Number class '" + value.getClass().getName() + "' is not supported");
		}

		public Number toNumber(double value) {
			switch (this) {
				case LONG:
					return Long.valueOf((long) value);
				case DOUBLE:
					return value;
				case INTEGER:
					return Integer.valueOf((int) value);
				case FLOAT:
					return Float.valueOf((float) value);
				case SHORT:
					return new Short((short) value);
				case BYTE:
					return new Byte((byte) value);
				case BIG_DECIMAL:
					return new BigDecimal(value);
			}
			throw new InstantiationError("can't convert " + this + " to a Number object");
		}
	}
}