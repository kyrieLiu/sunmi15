package ys.app.pad.http;

/**
 * Created by dennis on 2017/1/11 10:11
 * for ebrun
 * you can contact me at : pengjianjun@ebrun.com
 * or see link for more details
 *
 * @version 1.0.0
 */

public interface Convert<T, R> {

    public R call(T t);

}
