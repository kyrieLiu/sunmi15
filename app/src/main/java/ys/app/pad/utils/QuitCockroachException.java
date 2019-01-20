/*
 *  Copyright ® 2017.   All right reserved.
 *
 *  Last modified 17-2-22 上午9:16
 *
 *
 */

package ys.app.pad.utils;

/**
 * @author Gemini Y
 * @Description:
 * @email xuyangyang@ebrun.com
 * @create 2017/2/22 09:16
 */

public final class QuitCockroachException extends RuntimeException {
    public QuitCockroachException(String message) {
        super(message);
    }
}