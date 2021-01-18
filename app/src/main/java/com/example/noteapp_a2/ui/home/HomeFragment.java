package com.example.noteapp_a2.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteapp_a2.FormFragment;
import com.example.noteapp_a2.OnItemClickListener;
import com.example.noteapp_a2.Prefs;
import com.example.noteapp_a2.R;
import com.example.noteapp_a2.models.Note;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class HomeFragment extends Fragment{
    private RecyclerView recyclerView;
    private NoteAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new NoteAdapter();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.ROOT);
        String dateString = dateFormat.format(System.currentTimeMillis());
        for (int i = 1; i < 15; i++) {
            adapter.addItem(new Note("Элемент " + i, dateString));
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_clear_pref, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_clear_pref){
            new Prefs(requireContext()).deletePrefSettings();
            openBoardFragment();
        }
        return super.onOptionsItemSelected(item);
    }

    private void openBoardFragment() {
        NavController navController = Navigation.findNavController(requireActivity(),
                R.id.nav_host_fragment);
        navController.navigate(R.id.boardFragment);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
        view.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openForm();
            }
        });
        setFragmentListener();
        initList();
    }
    private void initList() {
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Note note = adapter.getItem(position);
                Toast.makeText(requireContext(), note.getTitle(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLongClick(int position) {
                alertDialog(position);
            }

            private void alertDialog(int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.alert_delete)
                        .setMessage("Удалит этот элемент со списка.")
                        .setPositiveButton(R.string.alert_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.deleteItem(position);
                    }
                });
                builder.setNegativeButton(R.string.alert_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.create().show();
            }
        });
    }

    private void setFragmentListener() {
        getParentFragmentManager().setFragmentResultListener(
                FormFragment.RK_FORM, getViewLifecycleOwner(), new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        Note note = (Note) result.getSerializable("note");
                        adapter.addItem(note);
                        Log.e("ololo", "text = " + result.getString("text"));
                    }
                }
        );
    }
    private void openForm() {
        NavController navController = Navigation.findNavController(requireActivity(),
                R.id.nav_host_fragment);
        navController.navigate(R.id.formFragment);
    }
}