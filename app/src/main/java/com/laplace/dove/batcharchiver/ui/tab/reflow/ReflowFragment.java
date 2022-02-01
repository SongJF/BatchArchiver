package com.laplace.dove.batcharchiver.ui.tab.reflow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.snackbar.Snackbar;
import com.laplace.dove.batcharchiver.R;
import com.laplace.dove.batcharchiver.databinding.FragmentReflowBinding;

public class ReflowFragment extends Fragment {
    private FragmentReflowBinding binding;
    private FragmentManager fm;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentReflowBinding.inflate(inflater, container, false);
        final  Fragment thisFrag = this;
        View root = binding.getRoot();


        binding.cardArchive.setOnClickListener(view -> {
            NavController controller = NavHostFragment.findNavController(thisFrag);
            controller.navigate(R.id.action_reflow_to_archive_to_archiveFragment);
        });
        binding.cardArchiveFromDb.setOnClickListener(view -> {
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).show();
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}