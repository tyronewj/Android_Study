package cn.wj.mobilesafe;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.Toast;

public abstract class BasicSetupActivity extends Activity {
	protected static final String TAG = "BasicSetupActivity";
	protected SharedPreferences sp ;//在此对SharedPreference进行声明，这样子类继承后即可直接使用
	// 1.定义一个手势识别器
	private GestureDetector detector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		sp = getSharedPreferences("config", MODE_PRIVATE);//在此次对sp进行实例化
		
		// 2.实例化这个手势识别器
		detector = new GestureDetector(this, new SimpleOnGestureListener() {
			/**
			 * 当手指滑动的时候触发该动作
			 * 	e1 The first down motion event that started the fling.
				e2 The move motion event that triggered the current onFling.
				velocityX The velocity of this fling measured in pixels per second along the x axis.
				velocityY The velocity of this fling measured in pixels per second along the y axis.
			 */
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,float velocityX, float velocityY) {
				//屏蔽斜滑的情形
				if (Math.abs((e1.getRawY()-e2.getRawY()))>100) {
					Toast.makeText(getApplicationContext(), "不能这样滑", 0).show();
					return true;
				}
				//屏蔽在x滑动缓慢的情形
				if (velocityX>200) {
					Toast.makeText(getApplicationContext(), "滑动太慢", 0).show();
				}
				//横向手势识别
				if (e1.getRawX() - e2.getRawX() > 200) {
					Log.i(TAG, TAG + "手指从右往左滑动，显示下一页面");
					nextPage();
					return true;
				} else {
					Log.i(TAG, TAG + "手指从左往右滑动，显示上一页面");
					prePage();
					return true;
				} 
			}

		});
	}

	abstract void prePage();

	abstract void nextPage();

	// 3.使用手势识别器
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		detector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
	public void next(View view) {
		nextPage();
	}
	public void pre(View view) {
		prePage();
	}
}
