package com.laplace.dove.batcharchiver.ui.dialog;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.laplace.dove.batcharchiver.R;

public class ProgressView extends LinearLayoutCompat {
    public TextView titleText;
    public TextView progressText;
    public ProgressBar progressBar;

    public ProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.tool_progress_view, this);
        titleText = view.findViewById(R.id.tool_progress_view_title_text);
        progressText = view.findViewById(R.id.tool_progress_view_progress_text);
        progressBar = view.findViewById(R.id.tool_progress_view_progressbar);
    }

    public void setMax(int max){
        progressBar.setMax(max);
        updateProgressText();
    }

    public int getProgress(){
        return progressBar.getProgress();
    }

    public void setProgress(int progress){
        progressBar.setProgress(progress);
        updateProgressText();
    }

    public void setTitle(String title){
        titleText.setText(title);
    }

    private void updateProgressText(){
        String text = String.format(getResources().getConfiguration().getLocales().get(0),
                "%d/%d", progressBar.getProgress(), progressBar.getMax());
        progressText.setText(text);
    }
}
