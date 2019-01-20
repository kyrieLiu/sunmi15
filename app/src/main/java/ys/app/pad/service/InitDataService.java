package ys.app.pad.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import ys.app.pad.utils.InitDataUtils;
import ys.app.pad.utils.Logger;

/**
 * 初始化数据
 * Created by admin on 2017/6/17.
 */

public class InitDataService extends IntentService {


    public InitDataService(){
        super("InitDataService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.d("InitDataService ------->>>> onCreate()");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //Log.d("InitDataService","InitDataService ------->>>> onHandleIntent()    服务中分店ID=="+ SpUtil.getBranchId());
        InitDataUtils.getInstance()
                .getVipCardHttp()
                .getServiceTypeHttp()
                .getGoodsTypeHttp()
                .getEmployeeListHttp()
                .getBackReasonHttp()
                .getAnimalTypeHttp()
                .getNumCardHttp();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.d("InitDataService ------->>>> onDestroy()");
    }
}
