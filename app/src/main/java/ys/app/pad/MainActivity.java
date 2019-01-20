package ys.app.pad;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import ys.app.pad.activity.AchievementActivity;
import ys.app.pad.activity.CollectMoneyActivity;
import ys.app.pad.activity.SaleActivity;
import ys.app.pad.activity.appointment.AppointmentMainActivity;
import ys.app.pad.activity.inventory.OutPutInventoryActivity;
import ys.app.pad.activity.manage.ManagerActivity;
import ys.app.pad.activity.vip.VipActivity;
import ys.app.pad.databinding.ActivityMainBinding;
import ys.app.pad.service.InitDataService;
import ys.app.pad.shangmi.printer.AidlUtil;
import ys.app.pad.shangmi.printer.BlueToothPrintUtil;
import ys.app.pad.shangmi.screen.utils.MainConnectUtils;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.utils.UpdateManager;
import ys.app.pad.utils.barcode.EncodingHandler;
import ys.app.pad.viewmodel.MainViewModel;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;
    private static final String[] array = {"收银", "会员", "库存", "预约", "业绩", "促销", "记录", "管理"};
    private MainViewModel mMainViewModel;


    private MainConnectUtils utils;
    private int id = 0;
    private UpdateManager updateManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        String title = SpUtil.getShopName();
        TextView tv_title = (TextView) findViewById(R.id.title_main);
        tv_title.setText(title);
        binding.setNameArray(array);
        mMainViewModel = new MainViewModel(this, binding);
        binding.setMainViewModel(mMainViewModel);
        Intent startIntent = new Intent(this, InitDataService.class);
        startService(startIntent);
//        if (SpUtil.getVersionModule()==1){
//            binding.rl3.setVisibility(View.GONE);
//            binding.rl5.setVisibility(View.GONE);
//        }
        setListenner();
        if (Build.MODEL.equals("T1mini")) {
            AidlUtil.getInstance().setTextToT1mini(null);
        } else {
            utils = MainConnectUtils.getInstance().setContext(this);
        }
           if ("D1".equals(Build.MODEL))BlueToothPrintUtil.getInstance().connectPrinterService(this);
//        updateManager = new UpdateManager(this);
//        updateManager.checkUpdate();



    }

    private void setListenner() {
        //收银
        binding.rl0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                turnToActivity(CollectMoneyActivity.class);
            }
        });
        //会员
        binding.rl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                turnToActivity(VipActivity.class);
//                Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.mipmap.erweima);
//                Bitmap bitmap1= BitmapUtil.generateBitmap("1234568984",9,120,120);
//                //Bitmap bitmap1= setImgSize(bitmap,120,120);
//                Bitmap bitmap2 = BitmapUtil.generateBitmap("96cl228527", 8, 600, 300);//最多十二位,否则扫不出来
//                Bitmap all=mergeBitmap_LR(bitmap1,bitmap2,false);
//                //binding.imageView.setImageBitmap(all);
//                BlueToothPrintUtil.getInstance().printLabel(all);
//                BlueToothPrintUtil.getInstance().sendReceiptWithResponse();
            }
        });
        //库存
        binding.rl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMainViewModel.changeSecMenu();
            }
        });
        //预约
        binding.rl3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                turnToActivity(AppointmentMainActivity.class);
            }
        });
        //业绩
        binding.rl4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                turnToActivity(AchievementActivity.class);

            }
        });
        //促销
        binding.rl5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                turnToActivity(SaleActivity.class);
            }
        });
        //出入库记录
        binding.rl6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                turnToActivity(OutPutInventoryActivity.class);
            }
        });
        //管理
        binding.rl7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnToActivity(ManagerActivity.class);
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (utils!=null)
        utils.onDestroy();
        mMainViewModel.destroy();
        if ("D1".equals(Build.MODEL)) BlueToothPrintUtil.getInstance().onDestroy();


    }

    public static Bitmap createQRCode(String str, int widthAndHeight)
            throws WriterException {
        Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        BitMatrix matrix = new MultiFormatWriter().encode(str,
                BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = BLACK;
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }


    protected static Bitmap encodeAsBitmap(String contents,
                                           BarcodeFormat format, int desiredWidth, int desiredHeight) {
        final int WHITE = 0xFFFFFFFF;
        final int BLACK = 0xFF000000;

        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result = null;
        try {
            result = writer.encode(contents, format, desiredWidth,
                    desiredHeight, null);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    private Bitmap createBarCode(String key) {
        Bitmap qrCode = null;
        try {
            //qrCode = EncodingHandler.createBarCode(key, 200, 80);
            qrCode = EncodingHandler.createBarCode(key, 600, 300);
        } catch (Exception e) {
            Toast.makeText(this,"输入的内容条形码不支持！",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return qrCode;
    }

    /**
     * 生成二维码
     * @param key
     */
    private Bitmap create2Code(String key) {
        Bitmap qrCode=null;
        try {
            //qrCode= EncodingHandler.create2Code(key, 350);
            qrCode= EncodingHandler.create2Code(key, 120);
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return qrCode;
    }
    static final int BLACK = 0xff000000;
    static final int WHITE = 0xFFFFFFFF;
    private Bitmap getBarCode(String str, Integer width1, Integer height1){
        Bitmap bitmap = null;
        try {
            BitMatrix matrix = new MultiFormatWriter().encode(str, BarcodeFormat.UPC_A, width1, height1, getEncodeHintMap());
            int width = matrix.getWidth();
            int height = matrix.getHeight();
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                int offset = y * width;
                for (int x = 0; x < width; x++) {
                    if(matrix.get(x,y)){
                        pixels[offset + x] =BLACK; //上面图案的颜色
                    }else{
                        pixels[offset + x] =WHITE;//底色
                    }
                }
            }
             bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            Log.e("hongliang","width:"+bitmap.getWidth()+" height:"+bitmap.getHeight());
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 获得设置好的编码参数
     * @return 编码参数
     */
    private static Hashtable<EncodeHintType, Object> getEncodeHintMap() {
        Hashtable<EncodeHintType, Object> hints= new Hashtable<EncodeHintType, Object>();
        //设置编码为utf-8
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        // 设置QR二维码的纠错级别——这里选择最高H级别
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        return hints;
    }

    private Bitmap getOtherBarCode(String content,  int width, int height) {
        Bitmap bitmap=null;
        MultiFormatWriter qrCodeWriter = new MultiFormatWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "GBK");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
//		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.MARGIN, 1);
        try {
            BitMatrix encode = qrCodeWriter.encode(content, BarcodeFormat.UPC_A, width, height, hints);
            int[] pixels = new int[width * height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (encode.get(j, i)) {
                        pixels[i * width + j] = 0x00000000;
                    } else {
                        pixels[i * width + j] = 0xffffffff;
                    }
                }
            }
             bitmap=Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public  Bitmap mergeBitmap_LR(Bitmap leftBitmap, Bitmap rightBitmap, boolean isBaseMax) {

        if (leftBitmap == null || leftBitmap.isRecycled()
                || rightBitmap == null || rightBitmap.isRecycled()) {
            return null;
        }
        int height = 0; // 拼接后的高度，按照参数取大或取小
        if (isBaseMax) {
            height = leftBitmap.getHeight() > rightBitmap.getHeight() ? leftBitmap.getHeight() : rightBitmap.getHeight();
        } else {
            height = leftBitmap.getHeight() < rightBitmap.getHeight() ? leftBitmap.getHeight() : rightBitmap.getHeight();
        }

        // 缩放之后的bitmap
        Bitmap tempBitmapL = leftBitmap;
        Bitmap tempBitmapR = rightBitmap;

        if (leftBitmap.getHeight() != height) {
            tempBitmapL = Bitmap.createScaledBitmap(leftBitmap, (int)(leftBitmap.getWidth()*1f/leftBitmap.getHeight()*height), height, false);
        } else if (rightBitmap.getHeight() != height) {
            tempBitmapR = Bitmap.createScaledBitmap(rightBitmap, (int)(rightBitmap.getWidth()*1f/rightBitmap.getHeight()*height), height, false);
        }

        // 拼接后的宽度
        int width = tempBitmapL.getWidth() + tempBitmapR.getWidth();

        // 定义输出的bitmap
        Bitmap bitmap = Bitmap.createBitmap(width+20, height+7, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(getResources().getColor(R.color.white));

        // 缩放后两个bitmap需要绘制的参数
        Rect leftRect = new Rect(0, 0, tempBitmapL.getWidth(), tempBitmapL.getHeight());
        Rect rightRect  = new Rect(0, 0, tempBitmapR.getWidth(), tempBitmapR.getHeight());

        // 右边图需要绘制的位置，往右边偏移左边图的宽度，高度是相同的
        Rect rightRectT  = new Rect(tempBitmapL.getWidth()+10, 0, width+10, height);

        canvas.drawBitmap(tempBitmapL, leftRect, leftRect, null);
        canvas.drawBitmap(tempBitmapR, rightRect, rightRectT, null);
        return bitmap;
    }
    public Bitmap setImgSize(Bitmap bm, int newWidth ,int newHeight){
        // 获得图片的宽高.
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例.
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数.
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片.
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateManager.showDownloadDialog();
                } else {
                    //Toast.makeText(this,getResources().getText(R.string.open_update_permission),Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }
}
