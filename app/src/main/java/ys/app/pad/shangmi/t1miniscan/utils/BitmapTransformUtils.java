package ys.app.pad.shangmi.t1miniscan.utils;

import android.graphics.Bitmap;

import com.sunmi.scan.Image;


/**
 * Created by KenMa on 2016/10/11.
 */
public class BitmapTransformUtils {

    public static Image getImageByBitmap(Bitmap bitmap) {
        Bitmap afterCropBitmap = cropBitmap(bitmap, 640, 480);//裁剪原图 解析的时候最大只能640*480
        //创建解码图像，并转换为原始灰度数据，注意图片是被旋转了90度的
        Image source = new Image(afterCropBitmap.getWidth(), afterCropBitmap.getHeight(), "Y800");
        source.setData(getYUVByBitmap(afterCropBitmap));//填充数据
        return source;
    }

    /**
     * 获取位图的YUV数据
     */
    private static byte[] getYUVByBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int size = width * height;

        int pixels[] = new int[size];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        byte[] data = rgb2YCbCr420(pixels, width, height);
        bitmap.recycle();
        bitmap = null;
        return data;
    }

    /**
     * 裁剪
     *
     * @param bitmap    原图
     * @param maxWidth  最大宽度
     * @param maxHeight 最大高度
     * @return 裁剪后的图
     */
    private static Bitmap cropBitmap(Bitmap bitmap, int maxWidth, int maxHeight) {
        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();
        int x = 0;
        int y = 0;
        if (w > maxWidth) {
            x = (w - maxWidth) / 2;
            w = maxWidth;
        }
        if (h > maxHeight) {
            y = (h - maxHeight) / 2;
            h = maxHeight;
        }
        return Bitmap.createBitmap(bitmap, x, y, w, h, null, false);
    }

    private static byte[] rgb2YCbCr420(int[] pixels, int width, int height) {
        int len = width * height;
        // yuv格式数组大小，y亮度占len长度，u,v各占len/4长度。
        byte[] yuv = new byte[len * 3 / 2];
        int y, u, v;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                // 屏蔽ARGB的透明度值
                int rgb = pixels[i * width + j] & 0x00FFFFFF;
                // 像素的颜色顺序为bgr，移位运算。
                int r = rgb & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = (rgb >> 16) & 0xFF;
                // 套用公式
                y = ((66 * r + 129 * g + 25 * b + 128) >> 8) + 16;
                u = ((-38 * r - 74 * g + 112 * b + 128) >> 8) + 128;
                v = ((112 * r - 94 * g - 18 * b + 128) >> 8) + 128;
                // rgb2yuv
                // y = (int) (0.299 * r + 0.587 * g + 0.114 * b);
                // u = (int) (-0.147 * r - 0.289 * g + 0.437 * b);
                // v = (int) (0.615 * r - 0.515 * g - 0.1 * b);
                // RGB转换YCbCr
                // y = (int) (0.299 * r + 0.587 * g + 0.114 * b);
                // u = (int) (-0.1687 * r - 0.3313 * g + 0.5 * b + 128);
                // if (u > 255)
                // u = 255;
                // v = (int) (0.5 * r - 0.4187 * g - 0.0813 * b + 128);
                // if (v > 255)
                // v = 255;
                // 调整
                y = y < 16 ? 16 : (y > 255 ? 255 : y);
                u = u < 0 ? 0 : (u > 255 ? 255 : u);
                v = v < 0 ? 0 : (v > 255 ? 255 : v);
                // 赋值
                yuv[i * width + j] = (byte) y;
                yuv[len + (i >> 1) * width + (j & ~1) + 0] = (byte) u;
                yuv[len + +(i >> 1) * width + (j & ~1) + 1] = (byte) v;
            }
        }
        return yuv;
    }
}
