package com.laplace.dove.batcharchiver.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.laplace.dove.batcharchiver.R;

public class DoubleProgressDialog {
    public AlertDialog dialog;
    public ProgressBar progressBar1;
    public ProgressBar progressBar2;
    public TextView textView1;
    public TextView textView2;

    public DoubleProgressDialog(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_double_progress, null);
        this.textView1 = view.findViewById(R.id.text_view1);
        this.progressBar1 = view.findViewById(R.id.progress_bar1);
        this.textView2 = view.findViewById(R.id.text_view2);
        this.progressBar2 = view.findViewById(R.id.progress_bar2);

        dialog = new AlertDialog.Builder(context).setView(view).create();
        dialog.setTitle("DoubleProgressDialog");
    }

    public void show(){
        dialog.show();
    }

    public void dismiss(){
        dialog.dismiss();
    }
}
