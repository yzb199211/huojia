package com.yyy.huojia.input;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yyy.huojia.R;
import com.yyy.huojia.dialog.LoadingDialog;
import com.yyy.huojia.input.model.NotArrived;
import com.yyy.huojia.interfaces.ResponseListener;
import com.yyy.huojia.util.SharedPreferencesHelper;
import com.yyy.huojia.util.StringUtil;
import com.yyy.huojia.util.Toasts;
import com.yyy.huojia.util.net.NetConfig;
import com.yyy.huojia.util.net.NetParams;
import com.yyy.huojia.util.net.NetUtil;
import com.yyy.huojia.util.net.Otypes;
import com.yyy.huojia.view.recycle.RecyclerViewDivider;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InputNotArrivedActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.rv_item)
    RecyclerView rvItem;

    SharedPreferencesHelper preferencesHelper;

    String url;

    InputNoArrivedAdapter adapter;
    List<NotArrived> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_no_arrived);
        ButterKnife.bind(this);
        preferencesHelper = new SharedPreferencesHelper(this, getString(R.string.preferenceCache));
        init();
    }

    private void init() {
        initView();
        initList();
        getDefaultData();
        getData();
    }

    private void initList() {
        list = new ArrayList<>();
    }


    private void initView() {
        tvTitle.setText(getString(R.string.title_not_arrived));
        ivRight.setVisibility(View.GONE);
        initRecycle();
    }

    private void initRecycle() {
        rvItem.setLayoutManager(new LinearLayoutManager(this));
        rvItem.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.VERTICAL));
    }

    private void getDefaultData() {
        url = preferencesHelper.getSharedPreference("address", "") + NetConfig.server + NetConfig.PDAHandler_Method;
    }

    private List<NetParams> getParams() {
        List<NetParams> params = new ArrayList<>();
        params.add(new NetParams("otype", Otypes.InputNotArrived));
        return params;
    }

    private void getData() {
        LoadingDialog.showDialogForLoading(this);
        new NetUtil(getParams(), url, new ResponseListener() {
            @Override
            public void onSuccess(String string) {
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    if (jsonObject.optBoolean("success")) {
                        initData(jsonObject.optJSONArray("tables").getString(0));
                    } else FinishLoading(jsonObject.optString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    FinishLoading(getString(R.string.error_json));
                } catch (Exception e) {
                    e.printStackTrace();
                    FinishLoading(getString(R.string.error_data));
                }
            }

            @Override
            public void onFail(IOException e) {
                e.printStackTrace();
                FinishLoading(e.getMessage());
            }
        });
    }

    private void initData(String tables) {
        list.clear();
        list.addAll(new Gson().fromJson(tables, new TypeToken<List<NotArrived>>() {
        }.getType()));
        if (list != null && list.size() > 0) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    refreshList();
                    FinishLoading(null);
                }
            });

        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    FinishLoading(null);
                }
            });
        }
    }

    private void refreshList() {
        if (adapter == null) {
            adapter = new InputNoArrivedAdapter(this, list);
            rvItem.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }

    private void FinishLoading(@NonNull String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LoadingDialog.cancelDialogForLoading();
                if (StringUtil.isNotEmpty(msg)) {
                    Toasts.showShort(InputNotArrivedActivity.this, msg);
                }
            }
        });
    }
}
