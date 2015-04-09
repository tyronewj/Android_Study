package cn.wj.mobilesafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class Setup3Activity extends BasicSetupActivity {
	protected static final String TAG = "Setup3Activity";
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
	}

	@Override
	void prePage() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(Setup3Activity.this,Setup2Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.left2right_enter, R.anim.left2right_out);
	}

	@Override
	void nextPage() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(Setup3Activity.this,Setup4Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.right2left_enter, R.anim.right2left_out);
	}
}
