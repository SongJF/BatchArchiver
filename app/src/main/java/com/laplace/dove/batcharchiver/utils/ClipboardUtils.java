package com.laplace.dove.batcharchiver.utils;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;

import java.util.List;

public class ClipboardUtils {
    public static void toClipBoard(Activity activity, List<String> strings){
        String txt = String.join("\n", strings);
        paste(activity, txt);
    }

    private static void paste(Activity activity, String txt){
        ClipboardManager myClipboard;
        myClipboard = (ClipboardManager)activity.getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("text", txt);
        myClipboard.setPrimaryClip(clipData);
    }
}
