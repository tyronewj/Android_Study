package cn.wj.mobilesafe;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.wj.mobilesafe.utils.Md5Utils;

public class HomeActivity extends Activity {
	protected static final String TAG = "HomeActivity";
	private GridView gridView;
	private MyAdapter myAdapter;
	private String[] names = { "手机防盗", "通讯卫士", "软件管理", "进程管理", "流量统计", "手机杀毒",
			"缓存清理", "高级工具", "设置中心" };
	private int[] imgAddress = { R.drawable.safe, R.drawable.callmsgsafe,
			R.drawable.app, R.drawable.taskmanager, R.drawable.netmanager,
			R.drawable.trojan, R.drawable.sysoptimize, R.drawable.atools,
			R.drawable.settings };
	private SharedPreferences sp;
	private AlertDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		// GridView和ListView都继承了GroupView
		gridView = (GridView) findViewById(R.id.list_home);
		myAdapter = new MyAdapter();
		gridView.setAdapter(myAdapter);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent;
				switch (position) {
				case 0:// 获取sp中的密码
					if (isSetupPwd()) {
						showEnterDialog();
					} else {
						showSetupDialog();
					}
					break;
				case 8:
					intent = new Intent(HomeActivity.this,
							SettingActivity.class);
					startActivity(intent);
					break;

				default:
					break;
				}
			}
		});
	}

	/**
	 * 设置密码对话框
	 */
	protected void showSetupDialog() {

		AlertDialog.Builder builder = new Builder(HomeActivity.this);
		View view = View.inflate(HomeActivity.this,R.layout.dialog_setup_password, null);

		builder.setView(view);
		final EditText et_dialog_pwd = (EditText) view.findViewById(R.id.et_setup_pwd);
		final EditText et_dialog_pwd2 = (EditText) view.findViewById(R.id.et_setup_pwd2);
		Button but_cancel = (Button) view.findViewById(R.id.but_cancel);
		Button but_ok = (Button) view.findViewById(R.id.but_ok);

		but_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		but_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String pwd = et_dialog_pwd.getText().toString().trim();
				String pwd2 = et_dialog_pwd2.getText().toString().trim();
				if (TextUtils.isEmpty(pwd) || TextUtils.isEmpty(pwd2)) {
					// 密码为空，提醒用户
					Toast.makeText(HomeActivity.this, "密码不能为空，请再次输入！", 0).show();
					et_dialog_pwd.setText("");
					et_dialog_pwd2.setText("");
					return;//不再往下执行
				} else if (!pwd.equals(pwd2)) {
					// 密码不一致,直接返回
					Toast.makeText(HomeActivity.this, "密码不一致，请再次输入！", 0).show();
					et_dialog_pwd.setText("");
					et_dialog_pwd2.setText("");
					return;
				} else {
					// 密码一致，保存密码
					Log.i(TAG,TAG+"____密码设置成功，进入手机防盗页面!");
					//Toast.makeText(HomeActivity.this, "密码设置成功，进入手机防盗页面!！", 0).show();
					Editor editor = sp.edit();
					pwd = Md5Utils.getMD5(pwd);
					editor.putString("password", pwd);
					editor.commit();
					dialog.dismiss();
					//  跳转到手机防盗页面
					Intent intent = new Intent(HomeActivity.this,LostFindActivity.class);
					startActivity(intent);					
				}
			}
		});
//		dialog = builder.show();
		dialog = builder.create();
		//设置dialog距离父窗体四周的距离
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();
	}

	/**
	 * 输入密码对话框
	 */
	protected void showEnterDialog() {
		AlertDialog.Builder builder = new Builder(HomeActivity.this);
		View view = View.inflate(HomeActivity.this,R.layout.dialog_enter_password, null);

		builder.setView(view);
		final EditText et_dialog_pwd = (EditText) view.findViewById(R.id.et_setup_pwd);
		Button but_cancel = (Button) view.findViewById(R.id.but_cancel);
		Button but_ok = (Button) view.findViewById(R.id.but_ok);

		but_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		but_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String pwd = et_dialog_pwd.getText().toString().trim();
				if (TextUtils.isEmpty(pwd)) {
					// 密码为空，提醒用户
					Toast.makeText(HomeActivity.this, "密码不能为空，请再次输入！", 0).show();
					et_dialog_pwd.setText("");
					return;//不再往下执行
				} else {
					//读取记录的密码
					sp = getSharedPreferences("config", MODE_PRIVATE);
					String password = sp.getString("password", "");
					pwd = Md5Utils.getMD5(pwd);
					if (pwd.equals(password)) {
						Log.i(TAG,TAG+"____登录成功，进入手机防盗页面!");
//						Toast.makeText(HomeActivity.this, "登录成功，进入手机防盗页面!", 0).show();
						// 密码正确，跳转到手机防盗页面
						Intent intent = new Intent(HomeActivity.this,LostFindActivity.class);
						startActivity(intent);	
						dialog.dismiss();
					}else {
						Toast.makeText(HomeActivity.this, "密码不正确，请再次输入!", 0).show();
						et_dialog_pwd.setText("");
					}
				}
			}
		});
		dialog = builder.create();
		//设置dialog距离父窗体四周的距离
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();
	}

	protected boolean isSetupPwd() {
		String password = sp.getString("password", "");
		return !TextUtils.isEmpty(password);
	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return names.length;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
		//方式一：
			View view = View.inflate(HomeActivity.this,R.layout.list_home_item, null);
			ImageView iv = (ImageView) view.findViewById(R.id.iv_home_item);
			TextView tv = (TextView) view.findViewById(R.id.tv_home_item);
			tv.setText(names[position]);
			iv.setImageResource(imgAddress[position]);
			return view;
//		//方式二
//			TextView tv = new TextView(HomeActivity.this);
//			tv.setText("this is a sample");
//			return tv;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		System.out.println(getClass().getName()+"------------onStart----------");
		super.onStart();
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		System.out.println(getClass().getName()+"------------onStop----------");
		super.onStop();
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		System.out.println(getClass().getName()+"------------onPause----------");
		super.onPause();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		System.out.println(getClass().getName()+"------------onResume----------");
		super.onResume();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		System.out.println(getClass().getName()+"------------onDestroy----------");
		super.onDestroy();
	}
}
