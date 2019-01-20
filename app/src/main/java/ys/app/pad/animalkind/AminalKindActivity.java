package ys.app.pad.animalkind;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.greendao.AnimalTypeClassifyInfoDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ys.app.pad.BaseActivity;
import ys.app.pad.R;
import ys.app.pad.callback.OnItemClickListener;
import ys.app.pad.db.GreenDaoUtils;
import ys.app.pad.model.AnimalTypeClassifyInfo;


public class AminalKindActivity extends BaseActivity {

    private EditText et_filter;
    private RecyclerView recyclerView;
    private SideView sideView;
    private List<AnimalTypeClassifyInfo> typeList;
    private List<AnimalTypeClassifyInfo> filterDateList = new ArrayList<AnimalTypeClassifyInfo>();

    //汉字转化为拼音的类
    private CharacterParser characterParser;
    //根据拼音来排列ListVIew里面的数据类
    private PinyinComparator pinyinComparator;
    private PetSelectAdapter adapter;
    private ImageView iv_noneData;
    private char letter='A';

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_book);
        et_filter= (EditText) findViewById(R.id.et_address_book_filter);
        recyclerView = (RecyclerView) findViewById(R.id.lv_address_book);
        sideView= (SideView) findViewById(R.id.as_address_book_side_view);
        iv_noneData= (ImageView) findViewById(R.id.noneDataIv);
        setBackVisiable();

        String categoryID=getIntent().getStringExtra("categoryID");
        AnimalTypeClassifyInfoDao animalInfoDao = GreenDaoUtils.getmDaoSession().getAnimalTypeClassifyInfoDao();
        QueryBuilder<AnimalTypeClassifyInfo> qb = animalInfoDao.queryBuilder();
        qb.where(AnimalTypeClassifyInfoDao.Properties.Type.eq(categoryID));
        typeList =qb.list();
        if (typeList !=null&& typeList.size()>0){
            initPage();
        }

    }
    public void initPage() {

        // 实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        initData();
    }


    private void initData() {
        for (int i = 0; i < typeList.size(); i++) {
            // 汉字转化为拼音
            String pinyin = characterParser.getSelling(typeList.get(i).getName());
            String sortString = null;
            if (!pinyin.equals("") && pinyin != null) {
                sortString = pinyin.substring(0, 1).toUpperCase();
                if (sortString.matches("[A-Z]")) {
                    typeList.get(i).setSortLetters(sortString.toUpperCase());
                } else {
                    typeList.get(i).setSortLetters("#");
                }
            }
        }
        setTouchListener();

    }


    /**
     * 根据输入框的值来过滤数据并更新ListView
     */
    private void filterData(String filterStr) {


        if (TextUtils.isEmpty(filterStr)) {
            updateTypeList();
        } else {
            filterDateList.clear();
            for (AnimalTypeClassifyInfo sortModel : typeList) {
                String name = sortModel.getName();
                if (name.indexOf(filterStr.toString()) != -1
                        || characterParser.getSelling(name).startsWith(
                        filterStr.toString())) {
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        if (filterDateList.size()>0){
            iv_noneData.setVisibility(View.GONE);
            adapter.updateListView(filterDateList);
        }else{
            iv_noneData.setVisibility(View.VISIBLE);
        }
    }


    private void setTouchListener() {
        // 根据a-z进行排序源数据
        Collections.sort(typeList, pinyinComparator);
        //创建适配器
        adapter = new PetSelectAdapter(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this,4));
        recyclerView.setAdapter(adapter);

        adapter.setListener(new OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                AnimalTypeClassifyInfo info=  adapter.getAnimalType(position);
                Intent intent=new Intent();
                Bundle bundle=new Bundle();
                bundle.putSerializable("typeInfo",info);
                intent.putExtras(bundle);
                setResult(10,intent);
                finish();
            }
        });

        updateTypeList();

        et_filter.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
        });
        // 设置右侧触摸监听
        sideView.setOnTouchingLetterChangedListener(new SideView.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                letter=s.charAt(0);
                updateTypeList();
            }
        });
    }

private void  updateTypeList(){
        filterDateList.clear();
    for (int i = 0; i < typeList.size(); i++) {
        AnimalTypeClassifyInfo info=typeList.get(i);
        String sortStr = info.getSortLetters();
        char firstChar = sortStr.toUpperCase().charAt(0);
        if (firstChar == letter) {
            filterDateList.add(info);
        }
    }
    if (filterDateList.size()>0){
        adapter.updateListView(filterDateList);
        iv_noneData.setVisibility(View.GONE);
    }else{
        iv_noneData.setVisibility(View.VISIBLE);
    }
}


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
