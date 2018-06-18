package com.dmk.busara_androidtask;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.ISyncModel;
import net.SyncModel;

import java.util.ArrayList;

import adapter.CategoriesAdapter;
import adapter.VideosAdapter;
import model.Video;

public class VideoListActivity extends AppCompatActivity {


    private  ArrayList<Video> videoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videolist);

        final GridView gridView = findViewById(R.id.gridCategories);

        final ProgressBar progressBar = findViewById(R.id.progressBar);

        TextView title = findViewById(R.id.txtTitle);
        TextView txtDesc = findViewById(R.id.txtDesc);

        title.setText(getIntent().getStringExtra("title"));
        txtDesc.setText(getIntent().getStringExtra("desc"));


        SyncModel syncModel = new SyncModel(this);

        syncModel.setVideosDelegate(new ISyncModel.VideosDelegate() {
            @Override
            public void onResponse(ArrayList<Video> videos) {

                progressBar.setVisibility(View.GONE);
                videoList = videos;
                gridView.setAdapter(new VideosAdapter(VideoListActivity.this,videoList ));

            }
        });
        syncModel.getVideos(getIntent().getIntExtra("category_id", 0));


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(VideoListActivity.this, VideoActivity.class)
                        .putExtra("link", ""+videoList.get(i).file_path).putExtra("title", ""+videoList.get(i).name));

            }
        });


    }

}
