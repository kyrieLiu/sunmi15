package ys.app.pad.widget.dialog;


import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import ys.app.pad.R;

public class LoadingDialog extends Dialog{
	private Activity context;


	public LoadingDialog(Activity context) {
		super(context, R.style.ThemeCustomDialog);
		this.context=context;

//		Window dialogWindow = getWindow();
//		dialogWindow.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
		ini();
	}

	void ini() {
		/**
		 * "加载项"布局，此布局被添加到ListView的Footer中。
		 */
		LinearLayout contentView = new LinearLayout(context);
		contentView.setMinimumHeight(60);
		contentView.setGravity(Gravity.CENTER);
		contentView.setOrientation(LinearLayout.HORIZONTAL);

		/**
		 * 向"加载项"布局中添加一个圆型进度条。
		 */
		ImageView image = new ImageView(context);
		image.setImageResource(R.drawable.progress);
		Animation anim = AnimationUtils.loadAnimation(context,
				R.anim.rotate_repeat);
		LinearInterpolator lir = new LinearInterpolator();
		anim.setInterpolator(lir);
		image.setAnimation(anim);

		contentView.addView(image);
		setContentView(contentView);
		setCanceledOnTouchOutside(false);
		//this.setCancelable(false);
		

	}

//	@Override
//	public void onBackPressed() {
//		super.onBackPressed();
//		context.finish();
//	}
}
