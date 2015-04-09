package cn.wj.mobilesafe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import cn.wj.mobilesafe.ui.SettingItemView;

public class Setup2Activity extends BasicSetupActivity {
	protected static final String TAG = "Setup2Activity";
	private SettingItemView siv_bind_sim;
//	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);//此句执行后，那么可访问父类中的实例化的sp
		
		setContentView(R.layout.activity_setup2);
		siv_bind_sim = (SettingItemView) findViewById(R.id.siv_bind_sim);
		String sim = sp.getString("sim", "");
		siv_bind_sim.setChecked(!TextUtils.isEmpty(sim));
		siv_bind_sim.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Editor editor = sp.edit();
				if (siv_bind_sim.isChecked()) {
					siv_bind_sim.setChecked(false);
					// SIM卡未绑定，移除sp中的信息
					editor.putString("sim", "");
				} else {
					siv_bind_sim.setChecked(true);
					// TODO 绑定SIM卡,写入SIM卡序列号
					editor.putString("sim", getSerializedNum());
					Log.i(TAG, TAG+"SIM_Num:"+getSerializedNum());
				}
				editor.commit();
			}
		});
	}

	private String getSerializedNum(){
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getSimSerialNumber();
	}

	@Override
	void prePage() {
		Intent intent = new Intent(Setup2Activity.this,Setup1Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.left2right_enter, R.anim.left2right_out);
	}

	@Override
	void nextPage() {
		// 如果sim卡绑定，那么跳转到下一页面，否则弹出吐司提醒进行绑定
				String sim = sp.getString("sim", "");
				if (TextUtils.isEmpty(sim)) {
					// sim卡未绑定
					Toast.makeText(Setup2Activity.this, "SIM卡未绑定个，请绑定后进行下一步操作~~~", 0).show();
					return;
				} else {
					// sim卡已绑定，进行下一步，页面跳转
					Intent intent = new Intent(Setup2Activity.this,Setup3Activity.class);
					startActivity(intent);
					finish();
					overridePendingTransition(R.anim.right2left_enter, R.anim.right2left_out);
				}
	}
}
