package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.fragment.Container_NewsFragment;

public class PostContentActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_post_content);
        ImageView image =(ImageView)findViewById(R.id.image);
        TextView title =(TextView)findViewById(R.id.title);
        TextView content =(TextView)findViewById(R.id.content);
        TextView writer =(TextView)findViewById(R.id.writer);
        TextView like =(TextView)findViewById(R.id.like);

        News_Content_JSONTaskAndParsor newsJson= new News_Content_JSONTaskAndParsor(image,title,content,writer,like);
        newsJson.execute(News_JSONTaskAndParsor.INFOBOARD_NUMBER_URL+ getIntent().getStringExtra(Container_NewsFragment.INTENT_QUERY));

    }



    @Override
    public void onClick(View view) {

    }
}
