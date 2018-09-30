package com.example.myapplication.fragment;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.myapplication.R;

public class SponEnrollment extends AppCompatActivity {
    private Button cancle;
    private ImageView eImage;
    private static final int PICK_FROM_GALLERY = 100;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spon_enrollment);

        cancle = (Button)findViewById(R.id.eCancle);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        eImage = (ImageView)findViewById(R.id.enroll_image);
        eImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                //갤러리 호출
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_GALLERY);

                /*//사진 잘라내기 셋팅
                intent.putExtra("crop","true");
                intent.putExtra("aspectX", 0);
                intent.putExtra("aspectY", 0);
                intent.putExtra("outputX", 200);
                intent.putExtra("outputY", 150);*/

                intent.putExtra("return-data", true);

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == PICK_FROM_GALLERY)
        {
            if (resultCode == RESULT_OK)
            {

                //기존 이미지 지우기
                eImage.setImageResource(0);

                //ClipData 또는 Uri를 가져온다
                Uri uri = data.getData();
                ClipData clipData = data.getClipData();

                //이미지 URI 를 이용하여 이미지뷰에 순서대로 세팅한다.
                if(clipData!=null)
                {

                    for(int i = 0; i < 3; i++)
                    {
                        if(i<clipData.getItemCount()){
                            Uri urione =  clipData.getItemAt(i).getUri();
                            switch (i){
                                case 0:
                                    eImage.setImageURI(urione);
                                    break;

                            }
                        }
                    }
                }
                else if(uri != null)
                {
                    eImage.setImageURI(uri);
                }
            }
        }
    }





}
