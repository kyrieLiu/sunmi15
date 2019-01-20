package ys.app.pad.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.support.annotation.NonNull;

import ys.app.pad.R;

/**
 * Created by xyy on 17-7-15.
 */

public class ConfirmCheckoutDialog extends Dialog {

    public ConfirmCheckoutDialog(@NonNull Activity activity) {
        super(activity, R.style.ThemeCustomDialog);
        super.setContentView(R.layout.dialog_confirm_checkout);
    }
}
