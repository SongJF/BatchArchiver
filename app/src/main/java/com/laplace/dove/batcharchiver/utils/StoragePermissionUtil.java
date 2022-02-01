package com.laplace.dove.batcharchiver.utils;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;

public class StoragePermissionUtil {
    public final static int REQUEST_CODE = 3306;

    public static void tryGetPermittion(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if(!Environment.isExternalStorageManager()){
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s",activity.getApplicationContext().getPackageName())));
                activity.startActivityForResult(intent, REQUEST_CODE);
            }
        }else {
            ActivityCompat.requestPermissions(activity, new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        }
    }
}
