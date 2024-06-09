package com.example.techandnotez4;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NewsDetail extends AppCompatActivity {
    String title, desc, content, imageURL, url;
    RoomDB database;
    private TextView titleTV, subDescTV, contentTV;
    private ImageView newsIV;
    private Button readNewsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        title = getIntent().getStringExtra("title");
        desc = getIntent().getStringExtra("desc");
        content = getIntent().getStringExtra("content");
        imageURL = getIntent().getStringExtra("image");
        url = getIntent().getStringExtra("url");
        titleTV = findViewById(R.id.TVTitle);
        subDescTV = findViewById(R.id.TVSubDesc);
        contentTV = findViewById(R.id.TVContent);
        newsIV = findViewById(R.id.IVNews);
        readNewsBtn = findViewById(R.id.BtnReadNews);
        database = RoomDB.getInstance(this);
        titleTV.setText(title);
        subDescTV.setText(desc);
        contentTV.setText(content);
        Picasso.get().load(imageURL).into(newsIV);
    }

    public void clickBack(View view) {
        finish();
    }

    public void clickFav(View view) {
        Toast.makeText(NewsDetail.this, "Added to favourites", Toast.LENGTH_SHORT).show();
    }

    public void clickReadMore(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    public void clickExport(View view) {
        NotesModel notesModel = new NotesModel();
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MM yyyy HH:mm a");
        Date date = new Date();
        notesModel.setTitle(title);
        notesModel.setNotes(desc);
        notesModel.setDate(formatter.format(date));
        database.mainDAO().insert(notesModel);
        Toast.makeText(NewsDetail.this, "Note exported", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(NewsDetail.this, Notes.class);
        startActivity(intent);
    }
}