package com.laplace.dove.batcharchiver.ui.stepper;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laplace.dove.batcharchiver.R;
import com.laplace.dove.batcharchiver.databinding.FragmentReflowBinding;

public class ArchiveFragment extends Fragment {

    private ArchiveViewModel mViewModel;
    private FragmentReflowBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.archive_fragment, container, false);
    }
}