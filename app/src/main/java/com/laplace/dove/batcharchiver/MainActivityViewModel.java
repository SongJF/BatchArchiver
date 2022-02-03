package com.laplace.dove.batcharchiver;

import static android.content.Context.MODE_PRIVATE;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {
    private static final String PREF_STORAGE = "pref_storage";
    private static final String PREF_KEY_SQL_CMD = "pref_key_sql_cmd";
    private MutableLiveData<List<String>> storagedSql;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        storagedSql = new MutableLiveData<>();
        storagedSql.setValue(loadStoragedSql());
    }

    private List<String> loadStoragedSql(){
        SharedPreferences preferences = getApplication().getSharedPreferences(PREF_STORAGE, MODE_PRIVATE);
        String jsonString = preferences.getString(PREF_KEY_SQL_CMD, null);
        if (jsonString == null){
            return new ArrayList<>();
        }
        return JSON.parseArray(jsonString, String.class);
    }

    private void saveStoragedSql(){
        SharedPreferences.Editor editor = getApplication().getSharedPreferences(PREF_STORAGE, MODE_PRIVATE).edit();
        String jsonString = JSON.toJSONString(storagedSql.getValue());
        editor.putString(PREF_KEY_SQL_CMD, jsonString);
        editor.apply();
    }

    public void delStoragedSql(String sql) {
        if (sql == null){
            return;
        }
        List<String> sqls = storagedSql.getValue();
        int idx = sqls.indexOf(sql);
        delStoragedSql(idx);
    }

    public void delStoragedSql(int index){
        List<String> sqls = storagedSql.getValue();
        if (index < 0 || index >= sqls.size()){
            return;
        }
        sqls.remove(index);
        saveStoragedSql();
    }

    public void addStoragedSql(String sql){
        if (sql == null){
            return;
        }

        List<String> sqls = storagedSql.getValue();
        sqls.add(sql);
        saveStoragedSql();
    }

    public LiveData<List<String>> getStoragedSql() {
        return storagedSql;
    }
}
