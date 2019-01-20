package ys.app.pad.nfc;


/**
 * 
 * @author houj
 *
 */
public final class HEX {

	/**
	 * HEX字符串转换为字节数组
	 * 
	 * @param s
	 * @return
	 */
	public static byte[] hexToBytes(String s) {
		s = s.toUpperCase();
		int len = s.length() / 2;
		int ii = 0;
		byte[] bs = new byte[len];
		char c;
		int h;
		for (int i = 0; i < len; i++) {
			c = s.charAt(ii++);
			if (c <= '9') {
				h = c - '0';
			} else {
				h = c - 'A' + 10;
			}
			h <<= 4;
			c = s.charAt(ii++);
			if (c <= '9') {
				h |= c - '0';
			} else {
				h |= c - 'A' + 10;
			}
			bs[i] = (byte) h;
		}
		return bs;
	}

	private final static char[] CS = "0123456789ABCDEF".toCharArray();

	/**
	 * 字节数组转换为HEX字符�?
	 * 
	 * @param bs
	 * @return
	 */
	public static String bytesToHex(byte[] bs) {
		char[] cs = new char[bs.length * 2];
		int io = 0;
		for (int n : bs) {
			cs[io++] = CS[(n >> 4) & 0xF];
			cs[io++] = CS[(n >> 0) & 0xF];
		}
		return new String(cs);
	}

	public static String bytesToHex(byte[] bs, int pos, int len) {
		char[] cs = new char[len * 2];
		int io = 0;
		len += pos;
		for (int i = pos; i < len; i++) {
			int n = bs[i];
			cs[io++] = CS[(n >> 4) & 0xF];
			cs[io++] = CS[(n >> 0) & 0xF];
		}
		return new String(cs);
	}

	public static String bytesToHex(byte[] bs, char gap) {
		char[] cs = new char[bs.length * 3];
		int io = 0;
		for (int n : bs) {
			cs[io++] = CS[(n >> 4) & 0xF];
			cs[io++] = CS[(n >> 0) & 0xF];
			cs[io++] = gap;
		}
		return new String(cs);
	}

	/**
	 * 字节数组转换为CPP数组格式
	 * 
	 * @param bs
	 * @param bytePerLine
	 * @return
	 */
	public static String bytesToCppHex(byte[] bs, int bytePerLine) {

		if (bytePerLine <= 0 || bytePerLine >= 65536) {
			bytePerLine = 65536;
		}
		int lines = 0;
		if (bytePerLine < 65536) {
			lines = (bs.length + bytePerLine - 1) / bytePerLine;
		}

		char[] cs = new char[bs.length * 5 + lines * 3];
		int io = 0;
		int ic = 0;
		for (int n : bs) {
			cs[io++] = '0';
			cs[io++] = 'x';
			cs[io++] = CS[(n >> 4) & 0xF];
			cs[io++] = CS[(n >> 0) & 0xF];
			cs[io++] = ',';
			if (bytePerLine < 65536) {
				if (++ic >= bytePerLine) {
					ic = 0;
					cs[io++] = '/';
					cs[io++] = '/';
					cs[io++] = '\n';
				}
			}
		}
		if (bytePerLine < 65536) {
			if (io < cs.length) {
				cs[io++] = '/';
				cs[io++] = '/';
				cs[io++] = '\n';
			}
		}
		return new String(cs);
	}

	public static String toLeHex(int n, int byteCount) {
		char[] rs = new char[byteCount * 2];
		int io = 0;
		for (int i = 0; i < byteCount; i++) {
			rs[io++] = CS[(n >> 4) & 0xF];
			rs[io++] = CS[(n >> 0) & 0xF];
			n >>>= 8;
		}
		return new String(rs);
	}

	public static String toBeHex(int n, int byteCount) {
		char[] rs = new char[byteCount * 2];
		int io = 0;
		n <<= (32 - byteCount * 8);
		for (int i = 0; i < byteCount; i++) {
			rs[io++] = CS[(n >> 28) & 0xF];
			rs[io++] = CS[(n >> 24) & 0xF];
			n <<= 8;
		}
		return new String(rs);
	}
}
