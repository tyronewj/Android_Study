package cn.wj.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;

public class Setup1Activity extends BasicSetupActivity {
	protected static final String TAG = "Setup1Activity";
	// 1.定义一个手势识别器
	private GestureDetector detector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup1);
		// 2.实例化这个手势识别器
		detector = new GestureDetector(Setup1Activity.this, new SimpleOnGestureListener(){
			/**
			 * 当手指滑动的时候触发该动作
			 */
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				if (e1.getX()-e2.getX()>200) {
					//TODO 转到下一页面
					Log.i(TAG, TAG+"手指从右往左滑动，显示下一页面");
					nextPage();
					return true;
				}else {
					//TODO 转到上一页面
					Log.i(TAG, TAG+"手指从左往右滑动，显示上一页面");
					return true;
				}
			}
			
		} );
	}

	//3.使用手势识别器
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		detector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}

	@Override
	void prePage() {
		// TODO Auto-generated method stub
		
	}

	@Override
	void nextPage() {
		Intent intent = new Intent(Setup1Activity.this, Setup2Activity.class);
		startActivity(intent);
		finish();
		//该方法必须在finish()或者startActivity()之后执行
		overridePendingTransition(R.anim.right2left_enter, R.anim.right2left_out);		
	}
	
	
	
}
