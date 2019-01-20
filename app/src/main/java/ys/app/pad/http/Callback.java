/**
 * Copyright ® 2007-2014 ebrun.com Co. Ltd.
 * All right reserved.
 */
package ys.app.pad.http;


import android.util.Log;

/**
 * <p>the callback object, which be executed after the http request be handled.</p>
 * Created by dennis on 2017/1/4 16:43
 * for ebrun
 * you can contact me at : pengjianjun@ebrun.com
 * or see link for more details
 *
 * @version 1.0.0
 */
public abstract class Callback<T> {

	/**
	 * When the http request to succeed, the method be called.
	 * @param t  the response data which its data type only to be the child of the <code>Response</code>.
     */
	public abstract void onSuccess(T t);

	/**
	 * When the http request occur error, the method be called.
	 * @param e the throwable.
     */
	public void onError(Throwable e){
		// TODO  For the error, the default have some operations to tell the user. such as Toast , or display the error view.
		Log.e("pet","onError = "+e.getMessage());
	}

	/**
	 * Whether the http request is successful or not, and the method will be called.
	 */
	public void onCompleted(){

	}

}
