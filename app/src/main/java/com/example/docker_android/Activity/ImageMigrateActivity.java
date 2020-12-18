package com.example.docker_android.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.docker_android.Base.BaseActivity;
import com.example.docker_android.Dialog.LoadingDialog;
import com.example.docker_android.DockerAPI.MigrateService;
import com.example.docker_android.R;
import com.example.docker_android.Utils.DownloadUtil;
import com.example.docker_android.Utils.RootCmd;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ImageMigrateActivity extends BaseActivity {

    private EditText ip;
    private EditText container;
    private EditText image;
    private EditText tag;
    private TextView submit;
    private ProgressDialog dialog;
    /**
     * 活动跳转接口
     * @param context 上下文
     * @param data1 数据1
     * @param data2 数据2
     */
    public static void actionStart(Context context, String data1, String data2) {
        Intent intent = new Intent(context, ImageMigrateActivity.class);
        intent.putExtra("param1", data1);
        intent.putExtra("param2", data2);
        context.startActivity(intent);
    }
    @Override
    public int getLayoutId() {
        return R.layout.activity_image_migrate;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        ip = findViewById(R.id.image_ip_addr);
        container = findViewById(R.id.container_name);
        image = findViewById(R.id.image_name);
        tag = findViewById(R.id.tag_name);
        submit = findViewById(R.id.submit);
        dialog = new ProgressDialog(ImageMigrateActivity.this);
        requestPower();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ip_address = ip.getText().toString().trim();
                String image_name = image.getText().toString().trim();
                String container_name = container.getText().toString().trim();
                String tag_name = tag.getText().toString().trim();
                if(ip_address.equals("") || image_name.equals("") || container_name.equals("") || tag_name.equals("")){
                    Toast.makeText(ImageMigrateActivity.this,"There are empty items",Toast.LENGTH_SHORT).show();
                }
                else {
                    showProgress();
                    String migrate_url = "http://" + ip_address + ":8081";
                    MigrateService.image_migrate(migrate_url,container_name,image_name,tag_name, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.d("migrate",e.getMessage());
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String responseData = response.body().string();
                            JSONObject tmp = JSON.parseObject(responseData);
                            int code = tmp.getIntValue("code");
                            if(code == 1){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String download_url = "http://"+ ip_address +":8080/images/arm64-target.tar.xz";
                                        DownloadUtil.get().download(download_url, "Download/", new DownloadUtil.OnDownloadListener() {
                                            @Override
                                            public void onDownloadSuccess() {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        importImage();
                                                        dialog.dismiss();
                                                        Toast.makeText(ImageMigrateActivity.this,"Image Migrate Successfully",Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    }
                                                });
                                            }
                                            @Override
                                            public void onDownloading(int progress) {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        dialog.setProgress(progress);
                                                    }
                                                });
                                            }
                                            @Override
                                            public void onDownloadFailed() {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        dialog.dismiss();
                                                        Toast.makeText(ImageMigrateActivity.this,"Download failed",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void initToolbar() {

    }
    public void requestPower() {
        //判断是否已经赋予权限
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //这里可以写个对话框之类的项向用户解释为什么要申请权限，并在对话框的确认键后续再次申请权限.它在用户选择"不再询问"的情况下返回false
            } else {
                //申请权限，字符串数组内是一个或多个要申请的权限，1是申请权限结果的返回参数，在onRequestPermissionsResult可以得知申请结果
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,}, 1);
            }
        }
    }
    public void showProgress(){
        dialog.setTitle("Image Migrate");
        dialog.setMessage("In migration...");
        dialog.setMax(100);
        dialog.setProgress(0);
        dialog.show();
    }
    public void importImage(){
        RootCmd.execRootCmdSilent("cd /sdcard/Download && docker load -i arm64-target.tar.xz");
        RootCmd.execRootCmdSilent("cd /sdcard/Download && rm arm64-target.tar.xz");
    }
}