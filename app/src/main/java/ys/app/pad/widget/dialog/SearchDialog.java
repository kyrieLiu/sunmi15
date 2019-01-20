package ys.app.pad.widget.dialog;

import android.app.Activity;
import android.app.Dialog;

import ys.app.pad.R;

/**
 * Created by admin on 2017/6/8.
 */

public class SearchDialog extends Dialog {

    public SearchDialog(Activity activity) {
        super(activity, R.style.ThemeCustomDialog);
        super.setContentView(R.layout.dialog_search);
    }
}
