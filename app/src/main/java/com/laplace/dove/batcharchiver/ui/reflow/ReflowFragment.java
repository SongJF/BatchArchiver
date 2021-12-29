package com.laplace.dove.batcharchiver.ui.reflow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.laplace.dove.batcharchiver.R;
import com.laplace.dove.batcharchiver.databinding.FragmentReflowBinding;

public class ReflowFragment extends Fragment {

    private ReflowViewModel reflowViewModel;
    private FragmentReflowBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        reflowViewModel =
                new ViewModelProvider(this).get(ReflowViewModel.class);

        binding = FragmentReflowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textReflow;
        reflowViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}