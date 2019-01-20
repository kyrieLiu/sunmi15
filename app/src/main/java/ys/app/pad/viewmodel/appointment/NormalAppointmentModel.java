package ys.app.pad.viewmodel.appointment;

import android.content.Intent;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.greendao.ServiceTypeInfoDao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.functions.Action1;
import ys.app.pad.BaseFragmentViewModel;
import ys.app.pad.Constants;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.activity.AddAnimalActivity;
import ys.app.pad.activity.appointment.ServerAppointmentActivity;
import ys.app.pad.adapter.SelectSimpleAdapter;
import ys.app.pad.databinding.NormalAppointmentBinding;
import ys.app.pad.db.GreenDaoUtils;
import ys.app.pad.event.RxManager;
import ys.app.pad.fragment.appointment.NormalAppointmentFragment;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.model.AnimalInfo;
import ys.app.pad.model.AppointmentPointInfo;
import ys.app.pad.model.BaseListResult;
import ys.app.pad.model.BaseResult;
import ys.app.pad.model.EmployeeAppointmentInfo;
import ys.app.pad.model.SelectInfo;
import ys.app.pad.model.ServiceInfo;
import ys.app.pad.model.ServiceTypeInfo;
import ys.app.pad.widget.dialog.CustomDialog;
import ys.app.pad.widget.dialog.RequestDialog;
import ys.app.pad.widget.dialog.SelectDialog;
import ys.app.pad.widget.timeselector.TimeSelector;
import ys.app.pad.widget.timeselector.Utils.TextUtil;

/**
 * Created by liuyin on 2017/9/13.
 */

public class NormalAppointmentModel extends BaseFragmentViewModel {

    private NormalAppointmentFragment mFragment;

    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> userName = new ObservableField<>();
    public ObservableField<String> userPhone = new ObservableField<>();
    public ObservableField<String> petName = new ObservableField<>();
    public ObservableField<String> appointmentTime = new ObservableField<>();
    public ObservableField<String> server = new ObservableField<>();
    public ObservableField<String> serverMatter = new ObservableField<>();
    public ObservableField<String> frameTime = new ObservableField<>();
    public ObservableField<String> employeeName = new ObservableField<>("随机分配");

    private SelectDialog petSelectDialog;
    private TimeSelector appointmentTimeSelect;//日期选择
    private SelectDialog serverSelectDialog;

    private SelectDialog dayTimeSelect;//当天时间
    private SelectDialog employeeDialog;//员工选择

    private ApiClient<BaseListResult<AppointmentPointInfo>> timeClient;
    private ApiClient<BaseListResult<EmployeeAppointmentInfo>> employeeClient;
    private  ApiClient<BaseListResult<AnimalInfo>> petClient;
    private  ApiClient<BaseResult> confirmClient;
    private RequestDialog mRDialog;
    private CustomDialog mDeleteDialog;

    private List<AnimalInfo> petList;
    private AnimalInfo animalInfo;
    private List<EmployeeAppointmentInfo> employeeInfoList;
    private SelectInfo serverInfo;
    private SelectInfo employeeInfo;
    private RxManager mRxManager;

    private boolean isVisible;
    private ServiceInfo serverSelectInfo;
    private String todayDate;

    private Map<String, String> params = new HashMap<>();
    private SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");

    public NormalAppointmentModel(NormalAppointmentFragment mFragment, NormalAppointmentBinding mBinding,String date) {
        this.mFragment = mFragment;
        this.timeClient = new ApiClient<>();
        this.employeeClient = new ApiClient<>();
        this.petClient=new ApiClient<>();
        this.confirmClient=new ApiClient<>();
        formatDate(date);
        initView();
    }

    public void setVisibleToUser(boolean isVisible){
        this.isVisible=isVisible;
    }
    /**
     * 获取预约日期
     */
    private void formatDate(String date){
        String getMonth=date.substring(0,date.indexOf("月"));
        Date nowDate=new Date();
        SimpleDateFormat yearFormat=new SimpleDateFormat("yyyy");
        SimpleDateFormat monthFormat=new SimpleDateFormat("MM");
        int year=Integer.parseInt(yearFormat.format(nowDate));
        int month=Integer.parseInt(monthFormat.format(nowDate));
        if (Integer.parseInt(getMonth)<month){
            year++;
        }
        String formatDate=year+"年"+date;
        SimpleDateFormat standardFormat=new SimpleDateFormat("yyyy年MM月dd");
        try {
            nowDate=standardFormat.parse(formatDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        todayDate = format.format(nowDate);
        appointmentTime.set(todayDate);
    }
    private void initView() {
        if (mRxManager==null){
            mRxManager=new RxManager();
        }
        mRxManager.on(Constants.bus_type_http_result, new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                if(Constants.type_updateAnimal_success == integer) {
                    if (isVisible){
                        getAnimalHttp();
                    }
                }
            }
        });
    }



    /**
     * 获取会员下的宠物
     */
    public void getAnimalHttp() {
        if (TextUtil.isEmpty(userName.get())){
            Toast.makeText(mFragment.getActivity(),"请输入顾客姓名",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtil.isEmpty(userPhone.get())){
            Toast.makeText(mFragment.getActivity(),"请输入顾客手机号",Toast.LENGTH_SHORT).show();
            return;
        }

        params.clear();
        params.put("headOfficeId",SpUtil.getHeadOfficeId()+"");
        params.put("userName",userName.get());
        params.put("userPhone",userPhone.get());
        showRDialog();
        petClient.reqApi("queryAnimal", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseListResult<AnimalInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<AnimalInfo> result) {
                        hideRDialog();
                        if (result.isSuccess()) {
                            petList = result.getData();
                            if (petList.size()==1){
                                animalInfo=petList.get(0);
                                petName.set(animalInfo.getName());
                            }else{
                                animalInfo=null;
                                petName.set("");
                                setPet();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideRDialog();
                        super.onError(e);
                    }
                });
    }



    public void setPet() {
        if (petList.size()==0){
            showAddAnimalDialog();
            return;
        }
        final List<SelectInfo> genderInfos = new ArrayList<>();
        SelectInfo selectInfo;
        for (AnimalInfo info:petList){
            selectInfo=new SelectInfo(info.getName(),info.getId()+"");
            genderInfos.add(selectInfo);
        }
        SelectSimpleAdapter adapter = new SelectSimpleAdapter(mFragment.getActivity(), genderInfos);
        petSelectDialog = new SelectDialog(mFragment.getActivity(), adapter);
        petSelectDialog.setListenner(new SelectDialog.SelectListenner() {
            @Override
            public void onSelect(int position) {
                SelectInfo genderInfo = genderInfos.get(position);
                petName.set(genderInfo.getName());
                animalInfo=petList.get(position);
            }
        });
        petSelectDialog.show();
    }

    /**
     * 新增宠物
     */
    public void addPet(){
        if (TextUtil.isEmpty(userName.get())){
            Toast.makeText(mFragment.getActivity(),"请输入顾客姓名",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtil.isEmpty(userPhone.get())){
            Toast.makeText(mFragment.getActivity(),"请输入顾客手机号",Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(mFragment.getActivity(), AddAnimalActivity.class);
        intent.putExtra("userName",userName.get());
        intent.putExtra("userPhone",userPhone.get());
        mFragment.getActivity().startActivity(intent);
    }


    /**
     * 添加宠物对话框
     */
    private void showAddAnimalDialog(){
        if (mDeleteDialog == null) {
            mDeleteDialog = new CustomDialog(mFragment.getActivity());
            mDeleteDialog.setContent("会员账户下暂无宠物,是否添加宠物?");
            mDeleteDialog.setCancelVisiable();
        }
        mDeleteDialog.setOkVisiable(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDeleteDialog.dismiss();
                Intent intent = new Intent(mFragment.getActivity(), AddAnimalActivity.class);
                intent.putExtra("userName",userName.get());
                intent.putExtra("userPhone",userPhone.get());
                mFragment.getActivity().startActivity(intent);
            }
        });
        mDeleteDialog.show();
    }


    /**
     * 预约时间选择
     */
    public void selectAppointmentTime() {
        if (appointmentTimeSelect == null) {
            String today=format.format(new Date());
            appointmentTimeSelect = new TimeSelector(mFragment.getActivity(), new TimeSelector.ResultHandler() {
                @Override
                public void handle(String time) {
                    appointmentTime.set( time.substring(0,10));
                    frameTime.set("");
                }
            }, today+" 00:00", "2050-12-31 24:00");
            appointmentTimeSelect.setMode(TimeSelector.MODE.YMD);//只显示 年月日
        }
        appointmentTimeSelect.show();
    }

    /**
     * 选择服务
     */
    public void selectServer() {
        if (serverSelectDialog == null) {
            final List<SelectInfo> genderInfos = new ArrayList<>();
            ServiceTypeInfoDao serviceTypeInfoDao = GreenDaoUtils.getSingleTon().getmDaoSession().getServiceTypeInfoDao();
            List<ServiceTypeInfo> serviceTypeInfos = serviceTypeInfoDao.loadAll();
            SelectInfo selectInfo;
            for (ServiceTypeInfo info : serviceTypeInfos) {
                selectInfo = new SelectInfo(info.getName(), info.getId() + "");
                genderInfos.add(selectInfo);
            }
            SelectSimpleAdapter adapter = new SelectSimpleAdapter(mFragment.getActivity(), genderInfos);
            serverSelectDialog = new SelectDialog(mFragment.getActivity(), adapter);
            serverSelectDialog.setListenner(new SelectDialog.SelectListenner() {
                @Override
                public void onSelect(int position) {
                    serverInfo = genderInfos.get(position);
                    server.set(serverInfo.getName());
                    serverSelectInfo=null;
                    serverMatter.set("");
                    employeeInfo=null;
                    employeeName.set("随机分配");
                    frameTime.set("");
                }
            });
        }
        serverSelectDialog.show();
    }

    public void selectServerMatter() {
        String id = "";
        if (serverInfo != null) {
            id = serverInfo.getType();
        } else {
            Toast.makeText(mFragment.getActivity(), "请先选择服务", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent=new Intent(mFragment.getActivity(), ServerAppointmentActivity.class);
        intent.putExtra("serverID",id);
        mFragment.startActivityForResult(intent,10);

    }


    /**
     *选择服务返回数据
     */
    public void activityResult(int requestCode, int resultCode, Intent data){
        if (requestCode==10){
            if (resultCode==10){
                Bundle bundle=data.getExtras();
                serverSelectInfo = (ServiceInfo) bundle.getSerializable("serverInfo");
                serverMatter.set(serverSelectInfo.getName());
            }
        }
    }
    /**
     * 选择员工
     */
    public void selectEmployee() {
        if (serverInfo == null) {
            Toast.makeText(mFragment.getActivity(), "请选择服务事项", Toast.LENGTH_SHORT).show();
            return;
        }

        params.clear();
        params.put("headOfficeId", SpUtil.getHeadOfficeId() + "");
        params.put("branchId", SpUtil.getBranchId() + "");
        params.put("productTypeId", serverInfo.getType());
        showRDialog();
        employeeClient.reqApi("selectBespeakSetupUser", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseListResult<EmployeeAppointmentInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<EmployeeAppointmentInfo> result) {
                        hideRDialog();
                        if (result.isSuccess()) {
                            employeeInfoList = result.getData();
                            showEmployeeDialog();
                        } else {
                            Toast.makeText(mFragment.getActivity(), result.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        hideRDialog();
                        Toast.makeText(mFragment.getActivity(), "网络异常", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void showEmployeeDialog(){
        final List<SelectInfo> genderInfos = new ArrayList<>();
        SelectInfo selectInfo;
        for (EmployeeAppointmentInfo info : employeeInfoList) {
            selectInfo = new SelectInfo(info.getUserName(), info.getUserId()+"");
            genderInfos.add(selectInfo);
        }
        SelectSimpleAdapter adapter = new SelectSimpleAdapter(mFragment.getActivity(), genderInfos);
        employeeDialog = new SelectDialog(mFragment.getActivity(), adapter);
        employeeDialog.setListenner(new SelectDialog.SelectListenner() {
            @Override
            public void onSelect(int position) {
                employeeInfo = genderInfos.get(position);
                employeeName.set(employeeInfo.getName());
                frameTime.set("");
            }
        });
        employeeDialog.show();
    }

    /**
     * 选择当天时间
     */
    public void selectFrameTime() {
        if (serverInfo == null) {
            Toast.makeText(mFragment.getActivity(), "请先选择服务事项", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtil.isEmpty(appointmentTime.get())){
            Toast.makeText(mFragment.getActivity(), "请选择预约时间", Toast.LENGTH_SHORT).show();
            return;
        }
        params.clear();
        params.put("headOfficeId", SpUtil.getHeadOfficeId() + "");
        params.put("branchId", SpUtil.getBranchId() + "");
        params.put("productTypeId", serverInfo.getType());
        if (employeeInfo==null){
            params.put("userId","0");
        }else{
            params.put("userId",employeeInfo.getType());
        }
        params.put("appointmentDay", appointmentTime.get());
        showRDialog();
        timeClient.reqApi("selectBespeakSetupPoint", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseListResult<AppointmentPointInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<AppointmentPointInfo> result) {
                        hideRDialog();
                        if (result.isSuccess()) {
                            List<AppointmentPointInfo> data = result.getData();
                            if (data!=null&&data.size()>0){
                                showFrameTimeDialog(data);
                            }else{
                                Toast.makeText(mFragment.getActivity(), "请设置员工排班信息", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(mFragment.getActivity(), result.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        hideRDialog();
                        Toast.makeText(mFragment.getActivity(), "网络异常", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showFrameTimeDialog(List<AppointmentPointInfo> data) {
        final List<SelectInfo> genderInfos = new ArrayList<>();
        SelectInfo selectInfo;
        for (AppointmentPointInfo info : data) {
            if (info.getCount()>0){
                selectInfo = new SelectInfo(info.getIdentification(), "");
                genderInfos.add(selectInfo);
            }

        }
        SelectSimpleAdapter adapter = new SelectSimpleAdapter(mFragment.getActivity(), genderInfos);
        dayTimeSelect = new SelectDialog(mFragment.getActivity(), adapter);
        dayTimeSelect.setListenner(new SelectDialog.SelectListenner() {
            @Override
            public void onSelect(int position) {
                String time = genderInfos.get(position).getName();
                frameTime.set(time);
            }
        });
        dayTimeSelect.show();
    }




    /**
     * 确认预约
     */
    public void confirmAppointment() {

        if (TextUtil.isEmpty(userName.get())){
            Toast.makeText(mFragment.getActivity(), "请输入顾客姓名", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtil.isEmpty(userPhone.get())){
            Toast.makeText(mFragment.getActivity(), "请输入顾客电话", Toast.LENGTH_SHORT).show();
            return;
        }

        if (animalInfo==null){
            Toast.makeText(mFragment.getActivity(), "请选择宠物", Toast.LENGTH_SHORT).show();
            return;
        }
        if (serverInfo==null){
            Toast.makeText(mFragment.getActivity(), "请选择服务", Toast.LENGTH_SHORT).show();
            return;
        }
        if (serverSelectInfo==null){
            Toast.makeText(mFragment.getActivity(), "请选择服务事项", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtil.isEmpty(appointmentTime.get())){
            Toast.makeText(mFragment.getActivity(), "请选择预约时间", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtil.isEmpty(frameTime.get())){
            Toast.makeText(mFragment.getActivity(), "请选择当天时间", Toast.LENGTH_SHORT).show();
            return;
        }
        params.clear();
        if (employeeInfo==null){
            params.put("userName",employeeName.get());
            params.put("userId","0");
        }else{
            params.put("userId",employeeInfo.getType());
            params.put("userName",employeeInfo.getName());
        }
        params.put("headOfficeId", SpUtil.getHeadOfficeId() + "");
        params.put("branchId", SpUtil.getBranchId() + "");
        params.put("shopId",SpUtil.getShopId());
        params.put("vipUserId","0");
        params.put("vipUserPhone",userPhone.get());
        params.put("vipUserName",userName.get());
        if (animalInfo==null){
            params.put("petName",petName.get());
        }else{
            params.put("petId",animalInfo.getId()+"");
            params.put("petName",animalInfo.getName());
            params.put("petTypeId",animalInfo.getType()+"");
            params.put("petType2Id",animalInfo.getVarieties()+"");
            params.put("petType2Name",animalInfo.getVarietiesName());
            params.put("petAge",animalInfo.getAge()+"");
        }


        params.put("productTypeId",serverInfo.getType());
        params.put("productTypeName",serverInfo.getName());
        params.put("productType2Id",serverSelectInfo.getId()+"");
        params.put("productType2Name",serverSelectInfo.getName());
        params.put("bespeakDay", appointmentTime.get());
        params.put("bespeakPoint",frameTime.get());
        params.put("state","1");
        showRDialog();
        confirmClient.reqApi("insertBespeak", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult result) {
                        hideRDialog();
                        if (result.isSuccess()) {
                            mRxManager.post(Constants.bus_type_http_result,Constants.type_addAppointment_success);
                            Toast.makeText(mFragment.getActivity(), "预约成功", Toast.LENGTH_SHORT).show();
                            mFragment.getActivity().finish();
                        } else {
                            Toast.makeText(mFragment.getActivity(), result.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        hideRDialog();
                        Toast.makeText(mFragment.getActivity(), "网络异常", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void showRDialog() {
        if (mRDialog == null) {
            mRDialog = new RequestDialog(mFragment.getActivity());
        }
        mRDialog.show();
    }

    private void hideRDialog() {
        if (mRDialog != null) {
            mRDialog.hide();
        }
    }

    public void clear() {
        petSelectDialog = null;
        serverSelectDialog = null;
        appointmentTimeSelect = null;
        dayTimeSelect=null;
        if (mRDialog!=null){
            mRDialog.dismiss();
            mRDialog = null;
        }
        employeeDialog=null;
        employeeClient=null;
        if (mRxManager!=null){
            mRxManager.clear();
            mRxManager=null;
        }
    }
}
