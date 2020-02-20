package com.example.docker_android.Fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.example.docker_android.Adapter.ContainerAdapter;
import com.example.docker_android.DockerAPI.DockerService;
import com.example.docker_android.Entity.Container.Container;
import com.example.docker_android.R;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContainerStopFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContainerStopFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    @BindView(R.id.container_stop_recycler_View)
    RecyclerView recyclerView;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;
    private List<Container> list = new ArrayList<>();
    public ContainerStopFragment() {
        // Required empty public constructor
    }
    public ContainerStopFragment(List<Container> stopped) {
        // Required empty public constructor
        this.list = stopped;
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContainerStopFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContainerStopFragment newInstance(String param1, String param2) {
        ContainerStopFragment fragment = new ContainerStopFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_container_stop, container, false);
        ButterKnife.bind(this,view);  //使用BindView必须，不然会崩溃
        startAnim();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //加载动画太快，延时几秒
                LoadData();
            }
        },1000);
        return view;
    }

    private void LoadData() {
        DockerService.getContainers(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseData = response.body().string(); //toString方法未重写，这里使用string()方法 //string不能调用两次 被调用一次就关闭了，这里调用两次会报异常
                Log.d("Component", "onResponse: " + responseData);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONArray jsonArray = JSONArray.parseArray(responseData);
                            for (int i= 0;i < jsonArray.size();i++){
                                Log.d("container", "onResponse: " + jsonArray.getString(i));
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Log.d("container",jsonObject.getString("Names"));
                                Container container = JSON.parseObject(jsonArray.getString(i), Container.class);
                                if(!container.getState().equals("running"))
                                    list.add(container);
                            }
                            ContainerAdapter adapter = new ContainerAdapter(list,getActivity());
                            recyclerView.setAdapter(adapter);
                            GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),1);
                            recyclerView.setLayoutManager(layoutManager);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "刷新失败，请稍后再试或联系管理员", Toast.LENGTH_SHORT).show();
                        }
                        stopAnim();
                    }
                });
            }
        });
    }
    /**
     * 打开加载动画
     */
    private void startAnim(){
        avi.show();
        //avi.smoothToShow();
    }

    /**
     * 关闭加载动画
     */
    private void stopAnim(){
        avi.hide();
        //avi.smoothToHide();
    }
}
