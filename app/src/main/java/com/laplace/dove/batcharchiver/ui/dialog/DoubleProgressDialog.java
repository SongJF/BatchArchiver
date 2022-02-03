package com.laplace.dove.batcharchiver.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.laplace.dove.batcharchiver.R;

public class DoubleProgressDialog {
    public AlertDialog dialog;
    public ProgressView firstProgressView;
    public ProgressView secondProgressView;

    public DoubleProgressDialog(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_double_progress, null);
        this.firstProgressView = view.findViewById(R.id.dialog_double_progress_progress1);
        this.secondProgressView = view.findViewById(R.id.dialog_double_progress_progress2);

        dialog = new AlertDialog.Builder(context).setView(view).create();
        dialog.setTitle("DoubleProgressDialog");
    }

    public void show(){
        dialog.show();
    }

    public void dismiss(){
        dialog.dismiss();
    }

    public void firstProgressNext(String tilte){
        firstProgressView.setProgress(firstProgressView.getProgress() + 1);
        firstProgressView.setTitle(tilte);
    }

    public void secondProgressNext(String tilte){
        secondProgressView.setProgress(secondProgressView.getProgress() + 1);
        secondProgressView.setTitle(tilte);
    }
}
