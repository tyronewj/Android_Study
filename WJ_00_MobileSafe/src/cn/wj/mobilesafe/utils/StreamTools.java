package cn.wj.mobilesafe.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class StreamTools {

	/**
	 * @param is 输入流
	 * @return 返回字符串
	 * @throws IOException
	 */
	public static String readFromStream(InputStream is) throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte [] buffer = new byte[1024];
		int len = -1;
		StringBuilder string = new StringBuilder();
		while ((len= is.read(buffer))!=-1) {
			baos.write(buffer, 0, len);
		}
		String result = baos.toString();
		is.close();
		baos.close();
		return result;
	}
}
