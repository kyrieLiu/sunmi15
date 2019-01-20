package ys.app.pad.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * 格式化辅助类
 *
 * @author dennis
 * @email dennis.pengjianjun@gmail.com
 * @since 1.0
 */
public class FormatHelper {

	/** 换行分隔符  */
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");

	private final static char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' }; 
	
	/**
	 * md5加密字符串
	 * @param str
	 * @return
	 */
	public static String md5(String str)
	{
	    try
	    {
	    	MessageDigest md = MessageDigest.getInstance("MD5");
	    	md.update(str.getBytes());
	    	byte[] hashBytes = md.digest();
	      
	    	int byteLen = hashBytes.length;
	    	StringBuffer sb = new StringBuffer();
	    	for (int i = 0; i < byteLen ; i++)
	    	{
	    		sb.append(HEX_DIGITS[hashBytes[i]>>>4 & 0xf]);
	    		sb.append(HEX_DIGITS[hashBytes[i] & 0xf]);
	    	}
	    	return sb.toString();
	    }
	    catch (NoSuchAlgorithmException e)
	    {
	    	Logger.e("getMD5 error", e);
	    }
	    return "";
	}
	
	
	
	
	
	
	
}
