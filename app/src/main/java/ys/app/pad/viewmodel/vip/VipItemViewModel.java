package ys.app.pad.viewmodel.vip;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;

import com.android.databinding.library.baseAdapters.BR;

import java.util.List;

import ys.app.pad.utils.SpUtil;
import ys.app.pad.model.AnimalInfo;
import ys.app.pad.model.VipInfo;
import ys.app.pad.utils.AppUtil;
import ys.app.pad.utils.StringUtils;

/**
 * Created by aaa on 2017/3/1.
 */

public class VipItemViewModel extends BaseObservable {
    private VipInfo model;
    private Context mContext;
    public ObservableField<String> obAnimal1Name = new ObservableField<String>();
    public ObservableField<String> obAnimal2Name = new ObservableField<String>();
    public ObservableField<String> obAnimal3Name = new ObservableField<String>();
    public ObservableField<String> obAnimal4Name = new ObservableField<String>();
    public ObservableField<String> money = new ObservableField<>();
    public ObservableField<String> creatShop = new ObservableField<>("");

    public VipItemViewModel(VipInfo model, Context context) {
        this.model = model;
        this.mContext = context;
        setModel(model);
    }


    @Bindable
    public VipInfo getModel() {
        return model;
    }

    public void setModel(VipInfo model) {
        this.model = model;
        List<AnimalInfo> petList = model.getPetList();
        if (petList!=null){
            if (petList.size() > 3) {
                obAnimal1Name.set(petList.get(0).getName());
                obAnimal2Name.set(petList.get(1).getName());
                obAnimal3Name.set(petList.get(2).getName());
                obAnimal4Name.set(" ... ");
            } else if (petList.size() == 3) {
                obAnimal1Name.set(petList.get(0).getName());
                obAnimal2Name.set(petList.get(1).getName());
                obAnimal3Name.set(petList.get(2).getName());
                obAnimal4Name.set("");
            } else if (petList.size() == 2) {
                obAnimal1Name.set(petList.get(0).getName());
                obAnimal2Name.set(petList.get(1).getName());
                obAnimal3Name.set("");
                obAnimal4Name.set("");
            } else if (petList.size() == 1) {
                obAnimal1Name.set(petList.get(0).getName());
                obAnimal2Name.set("");
                obAnimal3Name.set("");
                obAnimal4Name.set("");
            } else {
                obAnimal1Name.set("");
                obAnimal2Name.set("");
                obAnimal3Name.set("");
                obAnimal4Name.set("");
            }
        }

        if (StringUtils.isEmpty(model.getProductList())) {
            money.set(AppUtil.formatStandardMoney(model.getMoney()) + "元");
        } else {
            try {
                String[] productNum = model.getProductNum().split(",");
                String[] productName = model.getProductName().split(",");
                StringBuilder builder = new StringBuilder();
                for (int i = 1; i < productNum.length; i++) {
                    int productItemNum = Integer.parseInt(productNum[i]);

                    builder.append(productName[i] + "  " + productItemNum + "次  ");
                }
                money.set(builder.toString());
            } catch (Exception e) {

            }
        }
        if (SpUtil.getBranchId()==model.getBranchId()){
            creatShop.set("本店创建");
        }else{
            creatShop.set("");
        }
        notifyPropertyChanged(BR.model);
    }
}
