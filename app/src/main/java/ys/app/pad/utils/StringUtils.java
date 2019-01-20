/**
 * Copyright ® 2007-2014 ebrun.com Co. Ltd.
 * All right reserved.
 */
package ys.app.pad.utils;

import android.widget.TextView;

/**
 * Utility class for working with and manipulating Strings.
 */
public final class StringUtils {
    // utility class
    private StringUtils() {
    }

    /**
     * Returns {@code true} if the given string is null, empty, or comprises only
     * whitespace characters, as defined by {@link Character#isWhitespace(char)}.
     *
     * @param string The String that should be examined.
     * @return {@code true} if {@code string} is null, empty, or consists of
     * whitespace characters only
     */
    public static boolean isEmptyOrWhitespace(String string) {
        if (string == null) {
            return true;
        }
        int length = string.length();
        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(string.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isPhone(String string) {
        if (string == null) {
            return false;
        }
        int length = string.length();
        if (length > 6) {
            return true;
        }
        return false;
    }

    public static int parseInt(String string, int defaultValue) {
        if (string != null) {
            try {
                return Integer.parseInt(string);
            } catch (NumberFormatException nfe) {
                // ignore
            }
        }
        return defaultValue;
    }

    public static long parseLong(String string, long defaultValue) {
        if (string != null) {
            try {
                return Long.parseLong(string);
            } catch (NumberFormatException nfe) {
                // ignore
            }
        }
        return defaultValue;
    }

    public static double parseDouble(String string, double defaultValue) {
        if (string != null) {
            try {
                return Double.parseDouble(string);
            } catch (NumberFormatException nfe) {
                // ignore
            }
        }
        return defaultValue;
    }


    public static boolean isValidMoney(String string) {
        if (string != null) {
            try {
                return Double.parseDouble(string) > 0;
            } catch (NumberFormatException nfe) {
                return false;
            }
        }
        return false;
    }

    public static boolean isEmpty(CharSequence cs) {
        return (cs == null) || (cs.length() == 0);
    }

    public static boolean isNotEmpty(CharSequence cs) {
        return !isEmpty(cs);
    }

    public static void filtNull(TextView textView, String s) {
        if (s != null) {
            textView.setText(s);
        } else {
            textView.setText(filtNull(s));
        }
    }


    public static void filtNull(TextView textView, String s1, String s2) {
        if (s1 != null && s2 != null) {
            textView.setText(s1 + " " + s2);
        } else {
            textView.setText(filtNull(s1) + filtNull(s2));
        }

    }

    //判断过滤单个string为null
    public static String filtNull(String s) {
        if (s != null) {
            return s;
        } else {
            s = "null";
        }
        return s;
    }


    //判断单个对象不为null
    public static boolean filtObjectNull(Object object) {
        if (object != null) {
            return false;
        } else {
            return true;
        }
    }


    public static String replaceBlank(String en) {
        return en == null ? null : en.replace(" ", "");
    }

    public static String hihtPhone(String phone) {
        if (isEmptyOrWhitespace(phone)) {
            return "";
        } else {
            if (phone.length() > 9) {
                return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
            } else {
                return "";
            }
        }
    }
}
