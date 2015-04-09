package cn.wj.mobilesafe;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.HttpHandler;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;
import cn.wj.mobilesafe.utils.StreamTools;

/**
 * Splash页面的作用：
 * 
 * @author TyroneSean
 * 
 */
public class SplashActivity extends Activity {

	protected static final String TAG = "SplashActivity";
	protected static final int JSON_ERROR = 0;
	protected static final int NETWORK_ERROR = 1;
	protected static final int URL_ERROR = 2;
	protected static final int SHOW_UPDATE_DIALOG = 3;
	protected static final int ENTER_HOME = 4;
	private TextView tv_splash_version;
	private String description;
	private String apkurl;
	private SharedPreferences sp;
	private TextView tv_progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
		tv_splash_version.setText("版本:" + getVersionName());
		tv_progress = (TextView) findViewById(R.id.tv_progress);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		boolean status = sp.getBoolean("updateStatus", false);
		if (status) {
			// 检查升级
			System.out.println("---------检查更新-----");
			checkUpdate();
		} else {
			System.out.println("---------未检查更新-----");
			handler.postAtTime(new Runnable() {

				@Override
				public void run() {
					enterHome();
				}
			}, 2000);
		}
		// 页面跳转的时候的补间动画 （补间动画只有view才行）
		AlphaAnimation alpha = new AlphaAnimation(0.2f, 1.0f);
		alpha.setDuration(500);// 需要调用view.startAnimation才能生效，给该activity的xml文件增加id
		findViewById(R.id.rl_root_splash).startAnimation(alpha);
	}

	/**
	 * 检查是否有新版本，如有则升级
	 */
	private void checkUpdate() {
		new Thread() {
			public void run() {
				Message msg = Message.obtain();
				long startTime = System.currentTimeMillis();
				;
				try {

					// 由于编程时候在内网测试，而且多项目组编写，使用较为平凡，因此存放在配置文件中较为合适（在values文件夹中增加一个配置文件）
					// 获取配置文件中的url地址(Values文件中的常量，会在R文件中根据类型生成对应的id)
					URL url = new URL(getString(R.string.serverUrl));
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setRequestMethod("GET");
					// 注意，如果网络不正常，那么用户等待时间将是以下两个时间之和
					conn.setConnectTimeout(2000);
					conn.setReadTimeout(2000);
					int code = conn.getResponseCode();
					if (200 == code) {
						// 联网成功
						InputStream is = conn.getInputStream();
						// 将流转换成String
						String result = StreamTools.readFromStream(is);
						Log.i(TAG, result);
						JSONObject obj = new JSONObject(result);
						// 避免写错，尽量copy变量名
						String version = (String) obj.get("version");
						description = (String) obj.get("description");
						apkurl = (String) obj.get("apkurl");

						// 校验新版本
						if (!version.equals(getVersionName())) {
							// 版本不一致，进行升级,弹出一个对话框，提醒用户升级
							msg.what = SHOW_UPDATE_DIALOG;
						} else {
							// 版本一直，进到主页面
							msg.what = ENTER_HOME;
						}
					}
				} catch (MalformedURLException e) {
					msg.what = URL_ERROR;
					e.printStackTrace();
				} catch (IOException e) {
					msg.what = NETWORK_ERROR;
					e.printStackTrace();
				} catch (JSONException e) {
					msg.what = JSON_ERROR;
					e.printStackTrace();
				} finally {
					long endTime = System.currentTimeMillis();
					try {// 页面跳转前停止2秒
							// TODO 更改成2s
						long dTime = 500 - (endTime - startTime);
						if (dTime > 0) {
							sleep(dTime);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					handler.sendMessage(msg);
				}
			};
		}.start();
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case JSON_ERROR:// Json解析出错
				enterHome();
				Log.i(TAG,"SplashActivity:---JSON_ERROR");
				Toast.makeText(getApplicationContext(), "网络故障，页面跳转", 0)
				.show();
				break;
			case NETWORK_ERROR:// 网络异常
				enterHome();
				Log.i(TAG,"SplashActivity:---NETWORK_ERROR");
				Toast.makeText(getApplicationContext(), "网络故障，页面跳转", 0)
						.show();
				break;
			case URL_ERROR:// URL错误
				enterHome();
				Log.i(TAG,"SplashActivity:---URL_ERROR");
				Toast.makeText(getApplicationContext(), "URL_ERROR", 0).show();
				break;
			case SHOW_UPDATE_DIALOG:// 显示升级对话框
				showUpdateDialog();
				Log.i(TAG,"SplashActivity:---SHOW_UPDATE_DIALOG");
				Toast.makeText(getApplicationContext(), "SHOW_UPDATE_DIALOG", 0)
						.show();
				break;
			case ENTER_HOME:// 进入主页面
				enterHome();
				Log.i(TAG,"SplashActivity:---ENTER_HOME");
				break;

			default:
				break;
			}
		};
	};

	/**
	 * 得到版本名称
	 * 
	 * @return
	 */
	private String getVersionName() {
		// 用来管理手机的APK
		PackageManager pm = getPackageManager();
		try {
			// 得到指定APK的功能清单文件,可通过info得到Activities，provider，receiver....
			PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
			return info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}
	}

	protected void showUpdateDialog() {
		getApplicationContext();
		AlertDialog.Builder builder = new Builder(SplashActivity.this);
		builder.setTitle("提示升级");
		// builder.setCancelable(false);// 该设置，返回键无效，点击其他区域也无效，必须点击确定或取消，强制升级
		builder.setMessage(description);
		builder.setPositiveButton("确定升级", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// 下载安装包，并进行安装，此处需要增加读写SD卡权限
				// 首先检查SD卡已加载
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					FinalHttp fh = new FinalHttp();
					// 调用download方法开始下载
					 HttpHandler handler = fh.download(apkurl, // 这里是下载的路径
							"/mnt/sdcard/testapk.apk",// true:断点续传
														// false:不断点续传（全新下载）
							true, // 这是保存到本地的路径
							new AjaxCallBack<File>() {
								@Override
								public void onLoading(long count, long current) {
									tv_progress.setVisibility(View.VISIBLE);//设置TextView可见
									tv_progress.setText("下载进度：" + current + "/"+ count);
									int progress = (int) (current * 100 / count);
									Log.i(TAG, "SplashActivity:---下载进度：" + progress + "%");
								}

								@Override
								public void onFailure(Throwable t, int errorNo,
										String strMsg) {
									super.onFailure(t, errorNo, strMsg);
									Log.i(TAG, "SplashActivity:---下载失败");
									enterHome();
								}

								@Override
								public void onStart() {
									super.onStart();
									Log.i(TAG, "SplashActivity:---下载开始");
								}
								@Override
								public void onSuccess(File t) {
									Log.i(TAG, t == null ? "null" : t
											.getAbsoluteFile().toString());
									installApk(t);
								}
								/**
								 * 安装APK
								 * @param t
								 */
								private void installApk(File t) {
									Intent intent = new Intent();
									intent.setAction("android.intent.action.VIEW");
									intent.addCategory("android.intent.category.DEFAULT");
									intent.setDataAndType(Uri.fromFile(t),"application/vnd.android.package-archive");
									startActivity(intent);
								}
							});
					//调用stop()方法停止下载
//					 handler.stop();
				}
			}
		});
		builder.setNegativeButton("下次再说", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				enterHome();
				dialog.dismiss();
			}
		});
		// 该方法监听返回键及空白区域
		builder.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				dialog.dismiss();
				enterHome();
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	protected void enterHome() {
		Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
		startActivity(intent);
		finish();// 不希望还能返回该页面，则跳转的同时finish该页面
	}

}
