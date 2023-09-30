package com.example.myusta;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.myusta.databinding.FragmentFirstBinding;

public class AboutUstaFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.textviewFirst.setText("The USTA is the national governing body for the sport of tennis and the leader in promoting and developing the sport’s growth on every level in the United States, from local communities to the crown jewel of the professional game, the US Open.\n" +
                "\n" +
                " \n" +
                "\n" +
                "The USTA is a progressive and diverse not-for-profit organization whose volunteers, professional staff and financial resources support a single mission: to promote and develop the growth of tennis. The USTA is the largest tennis organization in the world, with 17 geographical sections, more than 680,000 individual members and more than 7,000 organization members, thousands of volunteers and a professional staff dedicated to growing the game.\n" +
                "\n" +
                " \n" +
                "\n" +
                "To serve the sport at every level of play, the USTA recently debuted the USTA National Campus. The new Home of American Tennis, opened in January 2017, is a 100-court tennis facility, at Lake Nona in Orlando, Fla., that will redefine how the USTA delivers on its mission and provide a new vision for the future of tennis in the U.S.");
        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(AboutUstaFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}