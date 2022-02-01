package com.laplace.dove.batcharchiver.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.laplace.dove.batcharchiver.R;

public class LoadingDialog {
    public AlertDialog dialog;
    private TextView textView;
    private ProgressBar progressBar;

    public LoadingDialog(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_loading, null);
        this.textView = view.findViewById(R.id.text_view);
        this.progressBar = view.findViewById(R.id.progress_bar);

        dialog = new AlertDialog.Builder(context).setView(view).create();
    }

    public void show(){
        dialog.show();
    }

    public void dismiss(){
        dialog.dismiss();
    }

    public void setText(String text){
        this.textView.setText(text);
    }
}
