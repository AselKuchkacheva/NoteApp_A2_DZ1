package com.example.noteapp_a2.ui.board;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.noteapp_a2.OnItemClickListener;
import com.example.noteapp_a2.Prefs;
import com.example.noteapp_a2.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class BoardFragment extends Fragment {
    private Button buttonSkip;
    private ViewPager2 viewPager;
    private BoardAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_board, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager = view.findViewById(R.id.viewPager);
        buttonSkip = view.findViewById(R.id.btn_skip);
        adapter = new BoardAdapter();
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> { }).attach();
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position) {
                close();
            }

            @Override
            public void onLongClick(int position) {

            }
        });
        //кнопка "назад" <-- которая когда открыт BoardFragment делает выход из приложения в целом
        requireActivity().getOnBackPressedDispatcher().addCallback(
                getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        requireActivity().finish();
                    }
                }
        );
        buttonSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });
    }

    private void close() {
        Prefs prefs = new Prefs(requireContext());
        prefs.saveBoardStatus();
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        navController.navigateUp();
        navController.navigate(R.id.phoneFragment);
    }

}