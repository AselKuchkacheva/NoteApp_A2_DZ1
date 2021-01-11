package com.example.noteapp_a2;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ProfileFragment extends Fragment {
    private ImageView imageView;

    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView = view.findViewById(R.id.imageAdd);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage.launch("image/*");

            }
                ActivityResultLauncher<String> getImage = registerForActivityResult(
                        new ActivityResultContracts.GetContent(),
                        (Uri result) -> {
                            Glide.with(requireActivity())
                                    .load(result)
                                    .centerCrop()
                                    .circleCrop()
                                    .into(imageView);
                        }
                        );
        });
    }
}