package com.example.docker_android.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.docker_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.hust.wjz.crun.CrunJni;

public class CrunActivity extends AppCompatActivity {
    @BindView(R.id.crun_text)
    TextView test;
    /**
     * 活动跳转接口
     * @param context
     * @param data1
     * @param data2
     */
    public static void actionStart(Context context, String data1, String data2) {
        Intent intent = new Intent(context, CrunActivity.class);
        intent.putExtra("param1", data1);
        intent.putExtra("param2", data2);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crun);
        ButterKnife.bind(this);
        CrunJni crunJni = new CrunJni();
        try {
            test.setText(String.valueOf(crunJni.run("test","/mycontainer",false,"",0L,"",false,false,false)));
//            test.setText(String.valueOf(crunJni.list(false)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
