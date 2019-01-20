package ys.app.pad.db;

import android.database.sqlite.SQLiteDatabase;

import com.greendao.DaoMaster;
import com.greendao.DaoSession;

import ys.app.pad.AppApplication;

/**
 * Created by aaa on 2017/3/21.
 */

public class GreenDaoUtils {
    private static DaoMaster.DevOpenHelper mHelper;
    private static SQLiteDatabase db;
    private static DaoMaster mDaoMaster;
    private static DaoSession mDaoSession;

    private static GreenDaoUtils greenDaoUtils;

    private GreenDaoUtils(){}

    public static GreenDaoUtils getSingleTon(){
        if (greenDaoUtils==null){
            greenDaoUtils=new GreenDaoUtils();
        }
        return greenDaoUtils;
    }

    private static void initGreenDao(){
        mHelper=new DaoMaster.DevOpenHelper(AppApplication.getAppContext(),"pet-db",null);
        db=mHelper.getWritableDatabase();
        mDaoMaster=new DaoMaster(db);
        mDaoSession=mDaoMaster.newSession();
    }

    public static DaoSession getmDaoSession() {
        if (mDaoMaster==null){
            initGreenDao();
        }
        return mDaoSession;
    }

    public static SQLiteDatabase getDb() {
        if (db==null){
            initGreenDao();
        }
        return db;
    }
}
