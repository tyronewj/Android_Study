package cn.wj.mobilesafe.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
/**
 * 自定义TextView，天生具备焦点
 * @author TyroneSean
 */
public class FocusTextView extends TextView {
	public FocusTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public FocusTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FocusTextView(Context context) {
		super(context);
	}
	/**
	 * 当前并无焦点，只是欺骗android
	 */
	@Override
	public boolean isFocused() {
		//无论有误焦点，都返回true
		return true;
	}
}
