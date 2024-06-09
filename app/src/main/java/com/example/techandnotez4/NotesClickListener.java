package com.example.techandnotez4;

import androidx.cardview.widget.CardView;

public interface NotesClickListener {
    void onClick(NotesModel notesModel);
    void onLongClick(NotesModel notesModel, CardView cardView);
}
