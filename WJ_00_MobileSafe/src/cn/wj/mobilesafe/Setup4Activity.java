package cn.wj.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class Setup4Activity extends BasicSetupActivity {
	protected static final String TAG = "Setup4Activity";
	private SharedPreferences sp;
	private CheckBox cb_safe;
	private TextView tv_desc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);
		cb_safe = (CheckBox) findViewById(R.id.cb_safe);
		tv_desc = (TextView) findViewById(R.id.tv_desc);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		boolean configed = sp.getBoolean("configed", false);
		cb_safe.setChecked(configed);
		// TODO 对文字描述进行初始化
		if (configed) {
			tv_desc.setText("你已开启防盗功能");
		} else
			tv_desc.setText("你未开启防盗功能");
	}

	public void checked(View view) {
		cb_safe.setChecked(!cb_safe.isChecked());
		// TODO 对文字描述进行实时修改
		if (cb_safe.isChecked()) {
			tv_desc.setText("你已开启防盗功能");
		} else
			tv_desc.setText("你未开启防盗功能");

	}

	@Override
	void prePage() {
		Intent intent = new Intent(Setup4Activity.this, Setup3Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.left2right_enter,
				R.anim.left2right_out);
	}

	@Override
	void nextPage() {
		Intent intent = new Intent(Setup4Activity.this, LostFindActivity.class);
		Editor editor = sp.edit();
		editor.putBoolean("configed", cb_safe.isChecked());
		editor.commit();
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.right2left_enter,
				R.anim.right2left_out);
	}
}
