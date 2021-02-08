package com.example.noteapp_a2.ui.home;

import android.app.AlertDialog;
import android.app.Application;
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

import com.example.noteapp_a2.App;
import com.example.noteapp_a2.FormFragment;
import com.example.noteapp_a2.OnItemClickListener;
import com.example.noteapp_a2.Prefs;
import com.example.noteapp_a2.R;
import com.example.noteapp_a2.models.Note;
import com.example.noteapp_a2.room.AppDataBase;
import com.example.noteapp_a2.room.NoteDao;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private NoteAdapter adapter;
    private List<Note> list;
    private boolean isEditing = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new NoteAdapter();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy,HH:mm", Locale.ROOT);
        String dateString = dateFormat.format(System.currentTimeMillis());
        setHasOptionsMenu(true);
        adapter.setList(loadData());
    }

    private List<Note> loadData() {

        if (App.getPrefs().isSortedAZ())
            list = App.getAppDataBase().noteDao().sortAZ();
        if (App.getPrefs().isSortedDate())
            list = App.getAppDataBase().noteDao().sortDate();
        else list = App.getAppDataBase().noteDao().getAll();

        return list;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_clear_pref, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.item_menu_clear) {
            App.getPrefs().deletePrefSettings();
            openBoardFragment();
        }
        if (item.getItemId() == R.id.menu_sort_a_z) {
            if (App.getPrefs().isSortedAZ() ) {
                App.getPrefs().notSortAZ();
            }else
                App.getPrefs().sortAZ();

            adapter.setNewList(loadData());
        }

        if (item.getItemId() == R.id.menu_sort_date) {
            if (!App.getPrefs().isSortedDate() )
                App.getPrefs().sortDate();
            else App.getPrefs().notSortDate();

            adapter.setNewList(loadData());
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
                isEditing = false;
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
                Bundle bundle = new Bundle();
                bundle.putSerializable("text_item", note);
                isEditing = true;
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.formFragment, bundle);
                //Toast.makeText(requireContext(), note.getTitle(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLongClick(int position) {
                alertDialog(position);
            }

            private void alertDialog(int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.alert_delete)
                        .setMessage("Удалить этот элемент из списка.")
                        .setPositiveButton(R.string.alert_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                App.getAppDataBase().noteDao().delete(adapter.getItem(position));
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
                        if (isEditing) {
                            adapter.updateElement(adapter.getPosition(note), note);
                        } else {
                            adapter.addItem(note);
                        }
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