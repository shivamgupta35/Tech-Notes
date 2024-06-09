package com.example.techandnotez4;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class Notes extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    DrawerLayout drawerLayout;
    RecyclerView recyclerView;
    NotesListAdapter notesListAdapter;
    List<NotesModel> notesModels = new ArrayList<>();
    RoomDB database;
    FloatingActionButton fab;
    FloatingActionButton refresh;
    SearchView searchView_home;
    NotesModel selectedNote;
    private final NotesClickListener notesClickListener = new NotesClickListener() {
        @Override
        public void onClick(NotesModel notesModel) {
            Intent intent = new Intent(Notes.this, NotesTaker.class);
            intent.putExtra("old_note", notesModel);
            startActivityForResult(intent, 102);
        }

        @Override
        public void onLongClick(NotesModel notesModel, CardView cardView) {
            selectedNote = new NotesModel();
            selectedNote = notesModel;
            showPopup(cardView);
        }
    };

    private static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    private void showPopup(CardView cardView) {
        PopupMenu popupMenu = new PopupMenu(this, cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        drawerLayout = findViewById(R.id.drawer_layout);
        recyclerView = findViewById(R.id.notesRV);
        fab = findViewById(R.id.addNote);
        refresh = findViewById(R.id.refresh);
        database = RoomDB.getInstance(this);
        notesModels = database.mainDAO().getAll();
        updateRecycler(notesModels);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Notes.this, NotesTaker.class);
                startActivityForResult(i, 101);
            }
        });
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Notes.this, Notes.class);
                startActivity(intent);
                finish();
            }
        });
        searchView_home = findViewById(R.id.searchView_home);
        searchView_home.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
    }

    private void filter(String newText) {
        List<NotesModel> filteredList = new ArrayList<>();
        for (NotesModel singleNote : notesModels) {
            if (singleNote.getTitle().toLowerCase().contains(newText.toLowerCase())
                    || singleNote.getNotes().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(singleNote);
            }
        }
        notesListAdapter.filterList(filteredList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode == Activity.RESULT_OK) {
                NotesModel new_notes = (NotesModel) data.getSerializableExtra("notesModel");
                database.mainDAO().insert(new_notes);
                notesModels.clear();
                notesModels.addAll(database.mainDAO().getAll());
                notesListAdapter.notifyDataSetChanged();
            }
        } else if (requestCode == 102) {
            if (resultCode == Activity.RESULT_OK) {
                NotesModel new_notes = (NotesModel) data.getSerializableExtra("notesModel");
                database.mainDAO().update(new_notes.getID(), new_notes.getTitle(), new_notes.getNotes(), new_notes.getDate());
                notesModels.clear();
                notesModels.addAll(database.mainDAO().getAll());
                notesListAdapter.notifyDataSetChanged();
            }
        }
    }

    private void updateRecycler(List<NotesModel> notesModels) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        notesListAdapter = new NotesListAdapter(Notes.this, notesModels, notesClickListener);
        recyclerView.setAdapter(notesListAdapter);
    }

    public void clickMenu(View view) {
        openDrawer(drawerLayout);
    }

    private void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void clickHome(View view) {
        redirectActivity(this, News.class);
    }

    public void clickNotes(View view) {
        redirectActivity(this, Notes.class);
    }

    public void clickLogOut(View view) {
        logout(this);
    }

    private void logout(Notes notes) {
        AlertDialog.Builder builder = new AlertDialog.Builder(notes);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout ?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                notes.finishAffinity();
                Intent i = new Intent(Notes.this, SignIn.class);
                startActivity(i);
                finish();
                ;
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void redirectActivity(Activity activity, Class aclass) {
        Intent i = new Intent(activity, aclass);
        activity.startActivity(i);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.pin:
                if (selectedNote.isPinned()) {
                    database.mainDAO().pin(selectedNote.getID(), false);
                    Toast.makeText(Notes.this, "Unpinned", Toast.LENGTH_SHORT).show();
                } else {
                    database.mainDAO().pin(selectedNote.getID(), true);
                    Toast.makeText(Notes.this, "Pinned", Toast.LENGTH_SHORT).show();
                }
                notesModels.clear();
                notesModels.addAll(database.mainDAO().getAll());
                notesListAdapter.notifyDataSetChanged();
                return true;

            case R.id.delete:
                database.mainDAO().delete(selectedNote);
                notesModels.remove(selectedNote);
                notesListAdapter.notifyDataSetChanged();
                Toast.makeText(Notes.this, "Note deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }
    }
}
