package com.example.bccc.test;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bccc.test.utils.ProcessInfo;
import com.example.bccc.test.utils.Programe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class testFragment extends Fragment {
    private ListView lv_main;
    private List<AppInfo> data;
    private AppAdapter adapter;
    Context mBase;
    private Context context;
    private Intent monitorService;
    private int pid, uid;
    private static final int TIMEOUT = 20000;
    private ProcessInfo processInfo;
    private boolean isServiceStop = false;



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        lv_main = (ListView)view.findViewById(R.id.lv_main);
        mBase = getContext();
        super.onCreate(savedInstanceState);
        data = getAllAppInfos();
        adapter = new AppAdapter();
        lv_main.setAdapter(adapter);

        return view;
        //给ListView设置item的点击监听


        lv_main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //提示当前行的应用名称
                /*String appName = data.get(position).getAppName();
                //提示
                Toast.makeText(testFragment.this, appName, 0).show();*/
                monitorService = new Intent();
                monitorService.setClass(getActivity(), EmmageeService.class);
                //if (getString(R.string.start_test).equals(lv_main.getText().toString())) {
                AppAdapter adapter = (AppAdapter) lv_main.getAdapter();
                    if (adapter.checkedProg != null) {
                        String packageName = adapter.checkedProg.getPackageName();
                        String processName = adapter.checkedProg.getProcessName();
                        Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
                        String startActivity = "";
                        //Log.d(LOG_TAG, packageName);
                        // clear logcat
                        try {
                            Runtime.getRuntime().exec("logcat -c");
                        } catch (IOException e) {
                            //Log.d(LOG_TAG, e.getMessage());
                        }
                        try {
                            startActivity = intent.resolveActivity(getPackageManager()).getShortClassName();
                            startActivity(intent);
                        } catch (Exception e) {
                            //Toast.makeText(MainPageActivity.this, getString(R.string.can_not_start_app_toast), Toast.LENGTH_LONG).show();
                            return;
                        }
                        waitForAppStart(packageName);
                        monitorService.putExtra("processName", processName);
                        monitorService.putExtra("pid", pid);
                        monitorService.putExtra("uid", uid);
                        monitorService.putExtra("packageName", packageName);
                        monitorService.putExtra("startActivity", startActivity);
                        startService(monitorService);
                        isServiceStop = false;
                        //btnTest.setText(getString(R.string.stop_test));
                    } else {
                        //Toast.makeText(MainPageActivity.this, getString(R.string.choose_app_toast), Toast.LENGTH_LONG).show();
                    }
               // } /*else {
                    //btnTest.setText(getString(R.string.start_test));
                    //Toast.makeText(MainPageActivity.this, getString(R.string.test_result_file_toast) + EmmageeService.resultFilePath,
                            //Toast.LENGTH_LONG).show();
                    stopService(monitorService);
                }

        });

        //给LitView设置Item的长按监听
        //lv_main.setOnItemLongClickListener(this);

    }


    public PackageManager getPackageManager() {
        return mBase.getPackageManager();
    }

    private void waitForAppStart(String packageName) {
       //Log.d(LOG_TAG, "wait for app start");
        boolean isProcessStarted = false;
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() < startTime + TIMEOUT) {
            pid = processInfo.getPidByPackageName(mBase, packageName);
            if (pid != 0) {
                isProcessStarted = true;
                break;
            }
            if (isProcessStarted) {
                break;
            }
        }
    }

    public boolean stopService(Intent name) {
        return mBase.stopService(name);
    }



    //@Override
    /*public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.fragment_test);

        //初始化成员变量
        //lv_main = (ListView) findViewById(R.id.lv_main);
        data = getAllAppInfos();
        adapter = new AppAdapter();
        //显示列表
        lv_main.setAdapter(adapter);

        //给ListView设置item的点击监听
        lv_main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //提示当前行的应用名称
                String appName = data.get(position).getAppName();
                //提示
                Toast.makeText(testFragment.this, appName, 0).show();
            }
        });

        //给LitView设置Item的长按监听
        lv_main.setOnItemLongClickListener(this);
    }*/


    class AppAdapter extends BaseAdapter {
        List<Programe> programes;
        Programe checkedProg;
        @Override
        public int getCount() {
            return data.size();
        }
        @Override
        public Object getItem(int position) {
            return data.get(position);
        }
        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        //返回带数据当前行的Item视图对象
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //1. 如果convertView是null, 加载item的布局文件
            if(convertView==null) {
                convertView = View.inflate(getActivity(), R.layout.item_main, null);
            }
            //2. 得到当前行数据对象
            AppInfo appInfo = data.get(position);
            //3. 得到当前行需要更新的子View对象
            ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_item_icon);
            TextView textView = (TextView) convertView.findViewById(R.id.tv_item_name);
            //4. 给视图设置数据
            imageView.setImageDrawable(appInfo.getIcon());
            textView.setText(appInfo.getAppName());
            return convertView;
        }
    }


    /*
     * 得到手机中所有应用信息的列表
     * AppInfo
     *  Drawable icon  图片对象
     *  String appName
     *  String packageName
     */
    protected List<AppInfo> getAllAppInfos() {
        List<AppInfo> list = new ArrayList<AppInfo>();
        // 得到应用的packgeManager
        PackageManager packageManager = mBase.getPackageManager();
        // 创建一个主界面的intent
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        // 得到包含应用信息的列表
        List<ResolveInfo> ResolveInfos = packageManager.queryIntentActivities(
                intent, 0);
        // 遍历
        for (ResolveInfo ri : ResolveInfos) {
            // 得到包名
            String packageName = ri.activityInfo.packageName;
            // 得到图标
            Drawable icon = ri.loadIcon(packageManager);
            // 得到应用名称
            String appName = ri.loadLabel(packageManager).toString();
            // 封装应用信息对象
            AppInfo appInfo = new AppInfo(icon, appName, packageName);
            // 添加到list
            list.add(appInfo);
        }
        return list;
    }


    //@Override
    /*public boolean onItemLongClick(AdapterView<?> parent, View view,
                                   int position, long id) {
        //删除当前行
        //删除当前行的数据
        data.remove(position);
        //更新列表
        //lv_main.setAdapter(adapter);//显示列表, 不会使用缓存的item的视图对象
        adapter.notifyDataSetChanged();//通知更新列表, 使用所有缓存的item的视图对象

        return true;
    }*/





    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    /*private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public testFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment testFragment.
     */
    // TODO: Rename and change types and number of parameters
    /*public static testFragment newInstance(String param1, String param2) {
        testFragment fragment = new testFragment();
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
        return inflater.inflate(R.layout.fragment_test, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    /*public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
