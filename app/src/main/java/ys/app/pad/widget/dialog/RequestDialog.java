package ys.app.pad.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;

import ys.app.pad.R;


/**
 * Created by LYY on 2016/6/10.
 */
public class RequestDialog extends Dialog {


    private String mUrlTag;

    public RequestDialog(Context context) {
        super(context, R.style.Theme_RequestDialog);
        super.setContentView(R.layout.request_dialog);
        Window dialogWindow = getWindow();
        dialogWindow.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        init();
    }

    public void setUrlTag(String mUrlTag) {
        this.mUrlTag = mUrlTag;
    }

    private void init() {
        this.setCanceledOnTouchOutside(true);
        this.setCancelable(true);
//        this.setOnCancelListener(new DisDialog());
    }

//    class DisDialog implements DialogInterface.OnCancelListener{
//        @Override
//        public void onCancel(DialogInterface dialog) {
//           StuApplication.getInstance().cancelPendingRequests(mUrlTag);
//        }
//    }
}
