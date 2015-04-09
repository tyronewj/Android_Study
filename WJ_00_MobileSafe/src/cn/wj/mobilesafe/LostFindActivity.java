package cn.wj.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class LostFindActivity extends Activity {
	private SharedPreferences sp ;
	private String TAG = "LostFindActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		// 判断是否进行了设置向导的设置，没有则进入设置向导页面，否则进入该页面
		boolean config = sp.getBoolean("configed", false);
		if (config) {
			Log.i(TAG , "进入手机防盗页面");
			setContentView(R.layout.activity_lostfind);
			
		}else {
			Log.i(TAG , "设置向导未设置,进入设置向导页面");
			//还未设置，进入设置向导页面
			Intent intent = new Intent(LostFindActivity.this,Setup1Activity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.left2right_enter, R.anim.left2right_out);
		}
	}
	
	public void rebackSetup(View view){
		Intent intent = new Intent(LostFindActivity.this,Setup1Activity.class);
		startActivity(intent);
	}
}
