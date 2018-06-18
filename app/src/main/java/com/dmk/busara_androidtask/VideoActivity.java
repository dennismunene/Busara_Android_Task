package com.dmk.busara_androidtask;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

public class VideoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        if(getIntent().hasExtra("title"))
        getSupportActionBar().setTitle(getIntent().getStringExtra("title"));



        final ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);
        String url = getIntent().getStringExtra("link");
        final VideoView web = (VideoView) findViewById(R.id.videoView);
        web.setVideoURI(Uri.parse(url));
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(web);
        web.setMediaController(mediaController);
        web.requestFocus();
        web.start();
        web.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                progressBar.setVisibility(View.GONE);
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
