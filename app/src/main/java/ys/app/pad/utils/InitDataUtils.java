package ys.app.pad.utils;

import com.greendao.AnimalTypeClassifyInfoDao;
import com.greendao.AnimalTypeInfoDao;
import com.greendao.BackGoodsReasonInfoDao;
import com.greendao.EmployeeInfoDao;
import com.greendao.GoodTypeInfoDao;
import com.greendao.NumCardListInfoDao;
import com.greendao.ServiceTypeInfoDao;
import com.greendao.VipCardInfoDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ys.app.pad.db.GreenDaoUtils;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.model.AnimalTypeClassifyInfo;
import ys.app.pad.model.AnimalTypeInfo;
import ys.app.pad.model.BackGoodsReasonInfo;
import ys.app.pad.model.BaseListResult;
import ys.app.pad.model.EmployeeInfo;
import ys.app.pad.model.GoodTypeInfo;
import ys.app.pad.model.NumCardListInfo;
import ys.app.pad.model.ServiceTypeInfo;
import ys.app.pad.model.VipCardInfo;

/**
 * 数据初始化
 * Created by admin on 2017/6/17.
 */

public class InitDataUtils {

    private ApiClient<BaseListResult<ServiceTypeInfo>> mServiceTypeClient;
    private ApiClient<BaseListResult<EmployeeInfo>> mEmployeeListClient;
    private ApiClient<BaseListResult<BackGoodsReasonInfo>> mBackReasonClient;
    private ApiClient<BaseListResult> mClient;
    private ApiClient<BaseListResult<VipCardInfo>> mVipCardClient;
    private ApiClient<BaseListResult<AnimalTypeInfo>> mAnimalTypeClient;
    private ApiClient<BaseListResult<NumCardListInfo>> numCardClient;
    private int ss = 1000;
    private int mi = ss * 60;
    private int hh = mi * 60;
    private int dd = hh * 24;
    private final static int sIntervalTime = 60;//间隔网络请求时间

    private static class InitDataClassInstance {
        private static final InitDataUtils instance = new InitDataUtils();
    }

    public static InitDataUtils getInstance() {
        return InitDataClassInstance.instance;
    }

    private InitDataUtils() {
    }

    /**
     * 商品类型
     *
     * @return InitDataUtils 实例
     */
    public InitDataUtils getGoodsTypeHttp() {
        Logger.d("InitDataUtils ------->>>> getGoodsTypeHttp");
        long nowTime;
        long lastTime;
        GoodTypeInfoDao dao = GreenDaoUtils.getSingleTon().getmDaoSession().getGoodTypeInfoDao();
        if (dao != null) {
            nowTime = System.currentTimeMillis();
            List<GoodTypeInfo> userList = dao.queryBuilder().build().list();
            if (userList != null && !userList.isEmpty()) {
                GoodTypeInfo goodTypeInfo = userList.get(0);
                lastTime = goodTypeInfo.getRequestTime();
                long time = nowTime - lastTime;
                long intervalTime = time / mi;//分钟
                Logger.d("getGoodsTypeHttp ------->>>> 间隔时间 = "+ intervalTime);
                if (intervalTime > sIntervalTime) {//大于60分钟
                    loadGoodsTypeHttp();
                }
            } else {
                loadGoodsTypeHttp();
            }
        } else {
            loadGoodsTypeHttp();
        }
        return InitDataClassInstance.instance;
    }

    private void loadGoodsTypeHttp() {
        if (mClient == null) {
            mClient = new ApiClient<>();
        }
        Map<String, String> params = new HashMap<>();
        params.put("branchId",SpUtil.getBranchId()+"");
        params.put("headOfficeId",SpUtil.getHeadOfficeId()+"");

        mClient.reqApi("goodsType", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .saveData(new Callback<BaseListResult>() {
                    @Override
                    public void onSuccess(BaseListResult result) {
                        if (result.isSuccess()) {
                            long nowTime = System.currentTimeMillis();
                            GoodTypeInfoDao dao = GreenDaoUtils.getSingleTon().getmDaoSession().getGoodTypeInfoDao();
                            List<GoodTypeInfo> list = result.getData();
                            if (list != null && !list.isEmpty()) {
                                for (GoodTypeInfo info : list) {
                                    info.setRequestTime(nowTime);
                                }
                            }
                            dao.deleteAll();
                            dao.insertInTx(list);
                        }
                    }
                })
                .updateUI(new Callback<BaseListResult>() {
                    @Override
                    public void onSuccess(BaseListResult result) {
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                });
    }

    /**
     * 服务类型
     *
     * @return InitDataUtils 实例
     */
    public InitDataUtils getServiceTypeHttp() {
        Logger.d("InitDataUtils ------->>>> getServiceTypeHttp");
        long nowTime;
        long lastTime;
        ServiceTypeInfoDao dao = GreenDaoUtils.getSingleTon().getmDaoSession().getServiceTypeInfoDao();
        if (dao != null) {
            nowTime = System.currentTimeMillis();
            List<ServiceTypeInfo> list = dao.queryBuilder().build().list();
            if (list != null && !list.isEmpty()) {
                ServiceTypeInfo info = list.get(0);
                lastTime = info.getRequestTime();
                long time = nowTime - lastTime;
                long intervalTime = time / mi;//分钟
                Logger.d("getServiceTypeHttp ------->>>> 间隔时间 = "+ intervalTime);
                if (intervalTime > sIntervalTime) {//大于60分钟
                    loadServiceTypeHttp();
                }
            } else {
                loadServiceTypeHttp();
            }
        } else {
            loadServiceTypeHttp();
        }
        return InitDataClassInstance.instance;
    }

    private void loadServiceTypeHttp() {
        if (mServiceTypeClient == null) {
            mServiceTypeClient = new ApiClient<>();
        }
        Map<String, String> params = new HashMap<>();
        params.put("branchId",SpUtil.getBranchId()+"");
        params.put("headOfficeId",SpUtil.getHeadOfficeId()+"");

        mServiceTypeClient.reqApi("serviceType", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .saveData(new Callback<BaseListResult<ServiceTypeInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<ServiceTypeInfo> result) {
                        if (result.isSuccess()) {
                            long nowTime = System.currentTimeMillis();
                            ServiceTypeInfoDao dao = GreenDaoUtils.getSingleTon().getmDaoSession().getServiceTypeInfoDao();
                            List<ServiceTypeInfo> list = result.getData();
                            if (list != null && !list.isEmpty()) {
                                for (ServiceTypeInfo info : list) {
                                    info.setRequestTime(nowTime);
                                }
                            }
                            dao.deleteAll();
                            dao.insertInTx(list);
                        }
                    }
                })
                .updateUI(new Callback<BaseListResult<ServiceTypeInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<ServiceTypeInfo> info) {
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                });
    }

    /**
     * 获取VipList
     *
     * @return InitDataUtils 实例
     */
    public InitDataUtils getVipCardHttp() {
        Logger.d("InitDataUtils ------->>>> getVipCardHttp");
        long nowTime;
        long lastTime;
        VipCardInfoDao dao = GreenDaoUtils.getSingleTon().getmDaoSession().getVipCardInfoDao();
        if (dao != null) {
            nowTime = System.currentTimeMillis();
            List<VipCardInfo> list = dao.queryBuilder().build().list();
            if (list != null && !list.isEmpty()) {
                VipCardInfo info = list.get(0);
                lastTime = info.getRequestTime();
                long time = nowTime - lastTime;
                long intervalTime = time / mi;//分钟
                Logger.d("getVipCardHttp ------->>>> 间隔时间 = "+ intervalTime);
                if (intervalTime > sIntervalTime) {//大于60分钟
                    loadVipCardHttp();
                }
            } else {
                loadVipCardHttp();
            }
        } else {
            loadVipCardHttp();
        }
        return InitDataClassInstance.instance;
    }

    private void loadVipCardHttp() {

        if (mVipCardClient == null) {
            mVipCardClient = new ApiClient<>();
        }
        Map<String, String> params = new HashMap<>();
        params.put("branchId",SpUtil.getBranchId()+"");
        params.put("headOfficeId",SpUtil.getHeadOfficeId()+"");
        params.put("limit","1000");
        params.put("isClassification","0");

        mVipCardClient.reqApi("queryVipList", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .saveData(new Callback<BaseListResult<VipCardInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<VipCardInfo> result) {
                        if (result.isSuccess()) {
                            long nowTime = System.currentTimeMillis();
                            VipCardInfoDao dao = GreenDaoUtils.getSingleTon().getmDaoSession().getVipCardInfoDao();
                            List<VipCardInfo> list = result.getData();
                            if (list != null && !list.isEmpty()) {
                                for (VipCardInfo info : list) {
                                    info.setRequestTime(nowTime);
                                }
                            }
                            dao.deleteAll();
                            dao.insertInTx(list);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                }).updateUI(new Callback<BaseListResult<VipCardInfo>>() {
            @Override
            public void onSuccess(BaseListResult<VipCardInfo> vipCardInfoBaseListResult) {

            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }

    /**
     * 获取动物类型
     *
     * @return InitDataUtils 实例
     */
    public InitDataUtils getAnimalTypeHttp() {
        Logger.d("InitDataUtils ------->>>> getAnimalTypeHttp");
        long nowTime;
        long lastTime;
        AnimalTypeInfoDao dao = GreenDaoUtils.getSingleTon().getmDaoSession().getAnimalTypeInfoDao();
        if (dao != null) {
            nowTime = System.currentTimeMillis();
            List<AnimalTypeInfo> list = dao.queryBuilder().build().list();
            if (list != null && !list.isEmpty()) {
                AnimalTypeInfo info = list.get(0);
                lastTime = info.getRequestTime();
                long time = nowTime - lastTime;
                long intervalTime = time / mi;//分钟
                Logger.d("getAnimalTypeHttp ------->>>> 间隔时间 = "+ intervalTime);
                if (intervalTime > sIntervalTime) {//大于60分钟
                    loadAnimalTypeHttp();
                }
            } else {
                loadAnimalTypeHttp();
            }
        } else {
            loadAnimalTypeHttp();
        }

        return InitDataClassInstance.instance;
    }

    private void loadAnimalTypeHttp() {
        if (mAnimalTypeClient == null) {
            mAnimalTypeClient = new ApiClient<>();
        }
        Map<String, String> params = new HashMap<>();

        mAnimalTypeClient.reqApi("queryAnimalType", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .saveData(new Callback<BaseListResult<AnimalTypeInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<AnimalTypeInfo> result) {
                        if (result.isSuccess()) {
                            long nowTime = System.currentTimeMillis();
                            AnimalTypeInfoDao dao = GreenDaoUtils.getSingleTon().getmDaoSession().getAnimalTypeInfoDao();
                            AnimalTypeClassifyInfoDao infoDao = GreenDaoUtils.getSingleTon().getmDaoSession().getAnimalTypeClassifyInfoDao();
                            List<AnimalTypeInfo> list = result.getData();
                            dao.deleteAll();
                            infoDao.deleteAll();
                            if (list != null && !list.isEmpty()) {
                                for (AnimalTypeInfo info : list) {
                                    info.setRequestTime(nowTime);
                                    List<AnimalTypeClassifyInfo> varietiesList = info.getVarietiesList();
                                    if (varietiesList != null && !varietiesList.isEmpty()) {
                                        for (AnimalTypeClassifyInfo info1 : varietiesList) {
                                            info1.setRequestTime(nowTime);
                                        }
                                        infoDao.insertInTx(varietiesList);
                                    }
                                }
                            }
                            dao.insertInTx(list);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                }).updateUI(new Callback<BaseListResult<AnimalTypeInfo>>() {
            @Override
            public void onSuccess(BaseListResult<AnimalTypeInfo> animalTypeInfoBaseListResult) {

            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }

    /**
     * 雇员列表
     *
     * @return InitDataUtils 实例
     */
    public InitDataUtils getEmployeeListHttp() {
        Logger.d("InitDataUtils ------->>>> getEmployeeListHttp");
        long nowTime;
        long lastTime;
        EmployeeInfoDao dao = GreenDaoUtils.getSingleTon().getmDaoSession().getEmployeeInfoDao();
        if (dao != null) {
            nowTime = System.currentTimeMillis();
            List<EmployeeInfo> list = dao.queryBuilder().build().list();
            if (list != null && !list.isEmpty()) {
                EmployeeInfo info = list.get(0);
                lastTime = info.getRequestTime();
                long time = nowTime - lastTime;
                long intervalTime = time / mi;//分钟
                Logger.d("getEmployeeListHttp ------->>>> 间隔时间 = "+ intervalTime);
                if (intervalTime > sIntervalTime) {//大于60分钟
                    loadEmployeeListHttp();
                }
            } else {
                loadEmployeeListHttp();
            }
        } else {
            loadEmployeeListHttp();
        }

        return InitDataClassInstance.instance;
    }

    private void loadEmployeeListHttp() {
        if (mEmployeeListClient == null) {
            mEmployeeListClient = new ApiClient<>();
        }
        Map<String, String> params = new HashMap<>();
        params.put("branchId",SpUtil.getBranchId()+"");
        params.put("headOfficeId",SpUtil.getHeadOfficeId()+"");

        mEmployeeListClient.reqApi("queryEmployeeList", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .saveData(new Callback<BaseListResult<EmployeeInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<EmployeeInfo> result) {
                        if (result.isSuccess()) {
                            long nowTime = System.currentTimeMillis();
                            EmployeeInfoDao dao = GreenDaoUtils.getSingleTon().getmDaoSession().getEmployeeInfoDao();
                            List<EmployeeInfo> list = result.getData();
                            if (list != null && !list.isEmpty()) {
                                for (EmployeeInfo info : list) {
                                    info.setRequestTime(nowTime);
                                }
                            }
                            dao.deleteAll();
                            dao.insertInTx(list);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                })
                .updateUI(new Callback<BaseListResult<EmployeeInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<EmployeeInfo> result) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                });
    }

    /**
     * 退货原因
     *
     * @return InitDataUtils 实例
     */
    public InitDataUtils getBackReasonHttp() {
        Logger.d("InitDataUtils ------->>>> getBackReasonHttp");
        long nowTime;
        long lastTime;
        BackGoodsReasonInfoDao dao = GreenDaoUtils.getSingleTon().getmDaoSession().getBackGoodsReasonInfoDao();
        if (dao != null) {
            nowTime = System.currentTimeMillis();
            List<BackGoodsReasonInfo> list = dao.queryBuilder().build().list();
            if (list != null && !list.isEmpty()) {
                BackGoodsReasonInfo info = list.get(0);
                lastTime = info.getRequestTime();
                long time = nowTime - lastTime;
                long intervalTime = time / mi;//分钟
                Logger.d("getBackReasonHttp ------->>>> 间隔时间 = "+ intervalTime);
                if (intervalTime > sIntervalTime) {//大于60分钟
                    loadBackReasonHttp();
                }
            } else {
                loadBackReasonHttp();
            }
        } else {
            loadBackReasonHttp();
        }

        return InitDataClassInstance.instance;
    }

    private void loadBackReasonHttp(){
        if (mBackReasonClient == null){
            mBackReasonClient = new ApiClient<>();
        }
        Map<String, String> params = new HashMap<>();
        params.put("branchId",SpUtil.getBranchId()+"");
        params.put("headOfficeId",SpUtil.getHeadOfficeId()+"");

        mBackReasonClient.reqApi("backReasonList", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .saveData(new Callback<BaseListResult<BackGoodsReasonInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<BackGoodsReasonInfo> result) {
                        if (result.isSuccess()) {
                            long nowTime = System.currentTimeMillis();
                            BackGoodsReasonInfoDao dao = GreenDaoUtils.getSingleTon().getmDaoSession().getBackGoodsReasonInfoDao();
                            List<BackGoodsReasonInfo> list = result.getData();
                            if (list != null && !list.isEmpty()){
                                for (BackGoodsReasonInfo info : list){
                                    info.setRequestTime(nowTime);
                                }
                            }
                            dao.deleteAll();
                            dao.insertInTx(list);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                })
                .updateUI(new Callback<BaseListResult<BackGoodsReasonInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<BackGoodsReasonInfo> result) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                });
    }
    /**
     * 获取VipList
     *
     * @return InitDataUtils 实例
     */
    public InitDataUtils getNumCardHttp() {
        Logger.d("InitDataUtils ------->>>> getVipCardHttp");
        long nowTime;
        long lastTime;
        NumCardListInfoDao dao = GreenDaoUtils.getSingleTon().getmDaoSession().getNumCardListInfoDao();
        if (dao != null) {
            nowTime = System.currentTimeMillis();
            List<NumCardListInfo> list = dao.queryBuilder().build().list();
            if (list != null && !list.isEmpty()) {
                NumCardListInfo info = list.get(0);
                lastTime = info.getRequestTime();
                long time = nowTime - lastTime;
                long intervalTime = time / mi;//分钟
                Logger.d("getVipCardHttp ------->>>> 间隔时间 = "+ intervalTime);
                if (intervalTime > sIntervalTime) {//大于60分钟
                    loadNumCardHttp();
                }
            } else {
                loadNumCardHttp();
            }
        } else {
            loadNumCardHttp();
        }
        return InitDataClassInstance.instance;
    }

    private void loadNumCardHttp() {

        if (numCardClient == null) {
            numCardClient = new ApiClient<>();
        }
        Map<String, String> params = new HashMap<>();
        params.put("branchId",SpUtil.getBranchId()+"");
        params.put("headOfficeId",SpUtil.getHeadOfficeId()+"");
        params.put("limit","1000");

        numCardClient.reqApi("selectVipCardNumber", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .saveData(new Callback<BaseListResult<NumCardListInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<NumCardListInfo> vipCardInfoBaseListResult) {
                        if (vipCardInfoBaseListResult.isSuccess()) {
                            List<NumCardListInfo> list = vipCardInfoBaseListResult.getData();
                            NumCardListInfoDao dao = GreenDaoUtils.getSingleTon().getmDaoSession().getNumCardListInfoDao();
                            dao.deleteAll();
                            dao.insertInTx(list);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                })
                .updateUI(new Callback<BaseListResult<NumCardListInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<NumCardListInfo> result) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                });
    }

}
