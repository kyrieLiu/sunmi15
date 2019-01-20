package ys.app.pad.activity.manage;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func3;
import ys.app.pad.BaseActivity;
import ys.app.pad.Constants;
import ys.app.pad.R;
import ys.app.pad.databinding.ActivityModifyPswBinding;
import ys.app.pad.viewmodel.manage.ModifyPswViewModel;

/**
 * Created by aaa on 2017/3/10.
 */

public class ModifyPswActivity extends BaseActivity {

    private ActivityModifyPswBinding binding;
    private ModifyPswViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_modify_psw);
        setBackVisiable();
        setTitle("修改登录密码");
        int intentFlag=getIntent().getIntExtra(Constants.intent_flag,0);//0代表正常修改密码,1代表忘记密码需要修改
        mViewModel = new ModifyPswViewModel(this, binding,intentFlag);
        binding.setViewModel(mViewModel);


        init();
        //setBgWhiteStatusBar();

    }


    private void init() {
        bindView();
    }


    private void bindView() {

        Observable<String> oldOb = Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(final Subscriber<? super String> subscriber) {
                binding.oldPswEt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        subscriber.onNext(s.toString());
                    }
                });
            }
        });

        Observable<String> newOb = Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(final Subscriber<? super String> subscriber) {
                binding.newPswEt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        subscriber.onNext(s.toString());
                    }
                });
            }
        });

        Observable<String> pswOb = Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(final Subscriber<? super String> subscriber) {
                binding.confirmPswEt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        subscriber.onNext(s.toString());
                    }
                });
            }
        });

        Observable.combineLatest(oldOb, newOb, pswOb, new Func3<String, String, String, Boolean>() {
            @Override
            public Boolean call(String oldPsw, String newPsw, String password) {
                return isPasswordValid(oldPsw) && isPasswordValid(newPsw) && isPasswordValid(password);
            }
        }).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean verify) {
                if (verify) {
                    binding.okBtn.setEnabled(true);
                } else {
                    binding.okBtn.setEnabled(false);
                }
            }
        });
    }


    private boolean isPasswordValid(String password) {
        return password.length() == 6;
    }

}
