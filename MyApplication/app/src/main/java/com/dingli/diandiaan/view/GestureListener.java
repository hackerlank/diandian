package com.dingli.diandiaan.view;

import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

/**
 */
public class GestureListener extends SimpleOnGestureListener{
	private static final String TAG = "GestureListener";
	private final static int VERTICALMINISTANCE = 200 ;
	private final static int MINVELOCITY = 10 ;
	private GestureDetector gestureDetector;

	public GestureListener(Context context) {
		super();
		gestureDetector = new GestureDetector(context, this);
	}
	
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub


		if(e1.getX()-e2.getX() > VERTICALMINISTANCE && Math.abs(velocityX) > MINVELOCITY){
			left();
		}

		//
		if (e2.getX()-e1.getX() > VERTICALMINISTANCE && Math.abs(velocityX) > MINVELOCITY) {
			right();
		}
		return false;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @return
	 */
	public boolean left() {
		return false;
	}

	/**
	 * @return
	 */
	public boolean right() {
		return false;
	}

	public GestureDetector getGestureDetector() {
		return gestureDetector;
	}

	public void setGestureDetector(GestureDetector gestureDetector) {
		this.gestureDetector = gestureDetector;
	}
	
}
