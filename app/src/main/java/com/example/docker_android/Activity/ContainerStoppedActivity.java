package com.example.docker_android.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docker_android.Adapter.ContainerAdapter;
import com.example.docker_android.Adapter.ContainerStoppedAdapter;
import com.example.docker_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContainerStoppedActivity extends AppCompatActivity {
    /**
     * 活动跳转接口
     * @param context
     * @param data1
     * @param data2
     */
    public static void actionStart(Context context, String data1, String data2) {
        Intent intent = new Intent(context, ContainerStoppedActivity.class);
        intent.putExtra("param1", data1);
        intent.putExtra("param2", data2);
        context.startActivity(intent);
    }
    @BindView(R.id.stopped_RecycleView)
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container_stopped);
        ButterKnife.bind(this);  //使用BindView必须，不然会崩溃
        ContainerStoppedAdapter adapter = new ContainerStoppedAdapter(MainActivity.list_stop, ContainerStoppedActivity.this);
        recyclerView.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(ContainerStoppedActivity.this,1);
        recyclerView.setLayoutManager(layoutManager);
    }
}
