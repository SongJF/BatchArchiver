package com.laplace.dove.batcharchiver.ui.tab.settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.laplace.dove.batcharchiver.MainActivityViewModel;
import com.laplace.dove.batcharchiver.R;
import com.laplace.dove.batcharchiver.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {

    private MainActivityViewModel mainActivityViewModel;
    private FragmentSettingsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mainActivityViewModel =
                new ViewModelProvider(getActivity()).get(MainActivityViewModel.class);

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final ListView listView = binding.sqlList;
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.dialog_delete_title);
                builder.setMessage(R.string.dialog_delete_msg);
                int index = i;
                builder.setPositiveButton(R.string.dialog_comfirm_button, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mainActivityViewModel.delStoragedSql(index);
                        ((ArrayAdapter) adapterView.getAdapter()).notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton(R.string.dialog_denine_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                });
                builder.create().show();
                return false;
            }
        });
        mainActivityViewModel.getStoragedSql().observe(getViewLifecycleOwner(), s -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    getContext(),
                    android.R.layout.simple_list_item_1,
                    mainActivityViewModel.getStoragedSql().getValue());
            binding.fab.setOnClickListener(view -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.dialog_add_sql_title);
                EditText editText =new EditText(getContext());
                editText.setHint(R.string.dialog_input_sql_hint);
                builder.setView(editText);
                builder.setPositiveButton(R.string.dialog_comfirm_button, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String text = editText.getText().toString();
                        if (!text.isEmpty()){
                            mainActivityViewModel.addStoragedSql(text);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
                builder.setNegativeButton(R.string.dialog_denine_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                });
                builder.create().show();
            });
            listView.setAdapter(adapter);
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}