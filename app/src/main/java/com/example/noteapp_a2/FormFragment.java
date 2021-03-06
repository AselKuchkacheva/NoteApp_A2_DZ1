package com.example.noteapp_a2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.noteapp_a2.models.Note;
import com.example.noteapp_a2.room.AppDataBase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class FormFragment extends Fragment {
    private EditText editText;
    public final static String RK_FORM = "rk_form";
    public Note noteBundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_form, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editText = view.findViewById(R.id.editText);
        if (getArguments() != null){
            getDataBundle();
        }
        view.findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
            private void save() {
                String text = editText.getText().toString().trim();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy,HH:mm", Locale.ROOT);
                String dateString = dateFormat.format(System.currentTimeMillis());
                if (noteBundle == null){
                    noteBundle = new Note(text, dateString);
                    saveToFirestore(noteBundle);
                    App.getAppDataBase().noteDao().insert(noteBundle);
                }else {
                    noteBundle.setTitle(text);
                    App.getAppDataBase().noteDao().update(noteBundle);
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable("note", noteBundle);
                getParentFragmentManager().setFragmentResult(RK_FORM, bundle);

            }
        });
    }

    private void saveToFirestore(Note noteBundle) {
        FirebaseFirestore.getInstance()
                .collection("notes")
                .add(noteBundle)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()){
                            close();
                            Toast.makeText(requireContext(), "Успешно", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    private void close() {
        NavController navController = Navigation.findNavController(requireActivity(),
                R.id.nav_host_fragment);
        navController.navigateUp();
    }

    private void getDataBundle() {
        noteBundle = (Note) getArguments().getSerializable("text_item");
        //Log.e("ololo", noteBundle.getTitle());
        editText.setText(noteBundle.getTitle());
    }
}