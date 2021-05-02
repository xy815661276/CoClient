package com.example.docker_android.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.docker_android.Base.BaseActivity;
import com.example.docker_android.Dialog.LoadingDialog;
import com.example.docker_android.DockerAPI.DockerService;
import com.example.docker_android.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class CreateImageActivity extends BaseActivity {
    private EditText Image_name;
    private EditText Image_tag;
    private TextView submit;
    /**
     * 活动跳转接口
     * @param context
     * @param data1
     * @param data2
     */
    public static void actionStart(Context context, String data1, String data2) {
        Intent intent = new Intent(context, CreateImageActivity.class);
        intent.putExtra("param1", data1);
        intent.putExtra("param2", data2);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_create_image;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        Image_name = findViewById(R.id.image_name);
        Image_tag = findViewById(R.id.image_tag);
        submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadingDialog.showDialogForLoading(CreateImageActivity.this);
                String image_name = Image_name.getText().toString();
                String image_tag = Image_tag.getText().toString();
                if(image_name.equals("")||image_tag.equals("")){
                    Toast.makeText(CreateImageActivity.this,"Input available",Toast.LENGTH_SHORT).show();
                    LoadingDialog.hideDialogForLoading();
                }
                else {
                    new Handler().postDelayed(new Runnable() {
                        //加载动画太快，延时几秒
                        @Override
                        public void run() {
                            DockerService.CreateImage(image_name,image_tag,new okhttp3.Callback(){
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    e.printStackTrace();
                                }
                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(response.code()==200)
                                                Toast.makeText(CreateImageActivity.this,"Pull Successfully",Toast.LENGTH_LONG).show();
                                            else
                                                Toast.makeText(CreateImageActivity.this,"Pull Failed,try again",Toast.LENGTH_LONG).show();
                                            LoadingDialog.hideDialogForLoading();
                                            finish();
                                        }
                                    });
                                }
                            });
                        }
                    },1000);
                }
            }
        });
    }

    @Override
    public void initToolbar() {

    }
}
