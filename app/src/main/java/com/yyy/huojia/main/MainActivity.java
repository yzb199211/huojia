package com.yyy.huojia.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;


import com.yyy.huojia.R;
import com.yyy.huojia.check.CheckListActivity;
import com.yyy.huojia.exchange.ExchangeList;
import com.yyy.huojia.exchange.ExchangeListActivity;
import com.yyy.huojia.input.InputDetailActivity;
import com.yyy.huojia.input.InputListActivity;
import com.yyy.huojia.interfaces.OnItemClickListener;
import com.yyy.huojia.kaiping.KaiPingDetailActivity;
import com.yyy.huojia.output.OutputListActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.rv_menu)
    RecyclerView rvMenu;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    MenuUsualAdapter menuUsualAdapter;
//    SharedPreferencesHelper preferencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
//        preferencesHelper = new SharedPreferencesHelper(this, getString(R.string.preferenceCache));
//        Log.e(TAG, (String) preferencesHelper.getSharedPreference("userid", ""));
        ivBack.setVisibility(View.GONE);
        ivRight.setVisibility(View.GONE);
        tvTitle.setText(getString(R.string.app_name1));
        setMenus();
    }

    private void setMenus() {
        List<MainMenu> list = new ArrayList<>();
        list.add(new MainMenu(1, R.mipmap.icon_storage, "入库单"));
        list.add(new MainMenu(2, R.mipmap.icon_fentiao, "分条单"));
        list.add(new MainMenu(3, R.mipmap.icon_output, "开平单"));
        list.add(new MainMenu(4, R.mipmap.icon_statistics, "盘点单"));
        list.add(new MainMenu(5, R.mipmap.icon_exchange, "调拨单"));

        menuUsualAdapter = new MenuUsualAdapter(list, this);
        rvMenu.setAdapter(menuUsualAdapter);
        NoScrollGvManager manager = new NoScrollGvManager(this, 4);
        manager.setAutoMeasureEnabled(true);
        rvMenu.setHasFixedSize(true);
        manager.setScrollEnabled(false);
        rvMenu.setLayoutManager(manager);
        menuUsualAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                goNext(list.get(position).getId());
            }
        });
    }

    private void goNext(int id) {
        switch (id) {
            case 1:
                startActivity(new Intent().setClass(MainActivity.this, InputDetailActivity.class));
                break;
            case 2:
                startActivity(new Intent().setClass(MainActivity.this, OutputListActivity.class));
                break;
            case 3:
                startActivity(new Intent().setClass(MainActivity.this, KaiPingDetailActivity.class));
                break;
            case 4:
                startActivity(new Intent().setClass(MainActivity.this, CheckListActivity.class));
                break;
            case 5:
                startActivity(new Intent().setClass(MainActivity.this, ExchangeListActivity.class));
                break;
            default:
                break;
        }
    }

}
