package cn.wj.mobilesafe.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.wj.mobilesafe.R;

/**
 * @author TyroneSean
 *需要在配置文件中设置CheckBox android:clickable="false"，否则单独点击该空间，信号无法传回
 */
public class SettingItemView extends RelativeLayout {

	private TextView tv_setting_title;
	private TextView tv_setting_description;
	private CheckBox cd_setting_status;
	private String title;
	private String desc_on;
	private String desc_off;

	private void initView(Context context) {
		View.inflate(context, R.layout.setting_item_view, this);
		tv_setting_title = (TextView) findViewById(R.id.tv_setting_title);
		tv_setting_description = (TextView) findViewById(R.id.tv_setting_description);
		cd_setting_status = (CheckBox) findViewById(R.id.cd_setting_status);

	}

	public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	/**
	 * 带有两个参数的构造函数，布局文件使用的时候调用
	 * @param context
	 * @param attrs
	 */
	public SettingItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
//		System.out.println(attrs.getAttributeValue(4));
		title = attrs.getAttributeValue("http://schemas.android.com/apk/res/cn.wj.mobilesafe", "title");
		desc_on = attrs.getAttributeValue("http://schemas.android.com/apk/res/cn.wj.mobilesafe", "desc_on");
		desc_off = attrs.getAttributeValue("http://schemas.android.com/apk/res/cn.wj.mobilesafe", "desc_off");
		
		tv_setting_title.setText(title);
		if (cd_setting_status.isChecked()) {
			tv_setting_description.setText(desc_on);
		}else {
			tv_setting_description.setText(desc_off);
		}
	}

	/**
	 * 带有一个参数的构造函数，new 的时候使用
	 * @param context
	 */
	public SettingItemView(Context context) {
		super(context);
		initView(context);
	}

	public boolean isChecked() {
		return cd_setting_status.isChecked();
	}

	public void setChecked(boolean status) {
		cd_setting_status.setChecked(status);
		if (status) {
			tv_setting_description.setText(desc_on);
		}else {
			tv_setting_description.setText(desc_off);
		}
	}

//	public void setText(String string) {
//		tv_setting_description.setText(string);
//	}

}
