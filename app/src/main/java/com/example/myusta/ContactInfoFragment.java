package com.example.myusta;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.myusta.databinding.FragmentSecondBinding;

public class ContactInfoFragment extends Fragment {

    private FragmentSecondBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ContactInfoFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });

        binding.textviewSecond.setText("UNITED STATES TENNIS ASSOCIATION\n" +
                "70 West Red Oak Lane\n" +
                "White Plains, New York 10604\n" +
                "\n" +
                "Phone: (914) 696-7000");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}