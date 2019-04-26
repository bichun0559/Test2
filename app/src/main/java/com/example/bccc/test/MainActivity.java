package com.example.bccc.test;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity{
    private ViewPager viewPager;
    ArrayList<Fragment>fragmentList;
    private BottomNavigationView navigation;
    MainActivityViewPagerAdapter myFragmentAdapter;

    //设置导航栏选择变化监听器
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                //根据导航栏的选择给viewPager设置相应的Fragment
                case R.id.navigation_home:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_dashboard:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_notifications:
                    viewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //状态栏沉浸
        if(Build.VERSION.SDK_INT >= 21 ) {
            View decorView = getWindow().getDecorView();
            int option =  View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //初始化底部导航栏
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //初始化viewPager
        viewPager=(ViewPager)findViewById(R.id.viewPager);
        //初始化fragment列表
        fragmentList=new ArrayList<>();
        fragmentList.add(new testFragment());
        fragmentList.add(new historyFragment());
        fragmentList.add(new informationFragment());
        //初始化viewPager适配器
        myFragmentAdapter=new MainActivityViewPagerAdapter(getSupportFragmentManager(),fragmentList);
        //注入适配器
        viewPager.setAdapter(myFragmentAdapter);







        //设置viewPager点击事件
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                navigation.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


}


/*public class MainActivity extends AppCompatActivity  {

    private ViewPager viewPager;
    private BottomNavigationView navigationView;
    MainActivityViewPagerAdapter adapter;

    //private testFragment fragment1 = new testFragment();
    //private historyFragment fragment2 = new historyFragment();
    //private informationFragment fragment3 = new informationFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取到两个控件的对象
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        //viewPager.addOnPageChangeListener(this);
        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        //为ViewPager设置Adapter
        adapter = new MainActivityViewPagerAdapter(getSupportFragmentManager());
        //为Adapter添加Fragment
        adapter.addFragment(new testFragment());
        adapter.addFragment(new historyFragment());
        adapter.addFragment(new informationFragment());
        //为BottomNavigationView的菜单项设置监听事件
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //获取到菜单项的id
                int itemId = item.getItemId();

                switch (itemId) {
                    case R.id.navigation_home:
                        viewPager.setCurrentItem(0);
                        return true;
                    case R.id.navigation_dashboard:
                        viewPager.setCurrentItem(1);
                        return true;
                    case R.id.navigation_notifications:
                        viewPager.setCurrentItem(2);
                        return true;
                }
                return false;
            }
        });
        //为ViewPager设置监听
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                navigationView.getMenu().getItem(position).setChecked(true);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
       /* navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
       viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public  Fragment getItem(int position) {
                switch (position){
                    case 0:
                        return fragment1;
                        //break;
                    case 1:
                        return fragment2;
                        //break;
                    case 3:
                        return fragment3;
                        //break;
                }
                return null;
            }

            @Override
            public int getCount() {
                return 3;
            }
        });
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            viewPager.setCurrentItem(item.getOrder());
            return true;
        }
    };

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //页面滑动的时候，改变BottomNavigationView的Item高亮
        navigationView.getMenu().getItem(position).setChecked(true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}*/
