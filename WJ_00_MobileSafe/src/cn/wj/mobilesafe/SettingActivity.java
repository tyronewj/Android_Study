package cn.wj.mobilesafe;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import cn.wj.mobilesafe.ui.SettingItemView;

public class SettingActivity extends Activity {

	private SettingItemView settingItemView;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		settingItemView = (SettingItemView) findViewById(R.id.siv_update);
		//软件开启的时候读取上次设置记录
		boolean status = sp.getBoolean("updateStatus", true);
		if (status) {
			settingItemView.setChecked(status);
//			settingItemView.setText("自动更新功能已开启");
		}else {
//			settingItemView.setText("自动更新功能已关闭");
		}
		settingItemView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 该语句等同于以下if语句
				// settingItemView.setChecked(!settingItemView.isChecked());
				Editor editor = sp.edit();
				if (settingItemView.isChecked()) {
					settingItemView.setChecked(false);
//					settingItemView.setText("自动更新功能已关闭");
				} else {
					settingItemView.setChecked(true);
//					settingItemView.setText("自动更新功能已开启");
				}
				//将本次设计记录存储文本中
				editor.putBoolean("updateStatus", settingItemView.isChecked());
				editor.commit();
			}
		});
	}
}
