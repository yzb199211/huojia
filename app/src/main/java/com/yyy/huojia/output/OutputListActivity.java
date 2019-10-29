package com.yyy.huojia.output;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yyy.huojia.R;
import com.yyy.huojia.dialog.LoadingDialog;
import com.yyy.huojia.interfaces.OnItemClickListener;
import com.yyy.huojia.interfaces.ResponseListener;
import com.yyy.huojia.output.model.OutputList;
import com.yyy.huojia.util.ResultCode;
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

public class OutputListActivity extends AppCompatActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rv_item)
    RecyclerView rvItem;

    SharedPreferencesHelper preferencesHelper;

    String url;
    String userid;

    List<OutputList> list;
    OutputListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_output_list);
        ButterKnife.bind(this);
        preferencesHelper = new SharedPreferencesHelper(this, getString(R.string.preferenceCache));
        init();
    }

    private void init() {
        getDefaultData();
        initList();
        initView();
        getData();
    }


    private void getDefaultData() {
        url = (String) preferencesHelper.getSharedPreference("address", "") + NetConfig.server + NetConfig.PDAHandler_Method;
        userid = (String) preferencesHelper.getSharedPreference("userid", "");
    }

    private void initList() {
        list = new ArrayList<>();
    }

    private void initView() {
        tvTitle.setText(getString(R.string.title_output));
        initRecycle();
    }

    private void initRecycle() {
        rvItem.setLayoutManager(new LinearLayoutManager(this));
        rvItem.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.VERTICAL));
    }

    private List<NetParams> getParams() {
        List<NetParams> params = new ArrayList<>();
        params.add(new NetParams("otype", Otypes.OutputList));
        params.add(new NetParams("sUserID", userid));
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
                    } else {
                        FinishLoading(jsonObject.optString("message"));
                    }
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

    private void initData(String tables) throws Exception {
        List<OutputList> list = getList(tables);
        if (list != null && list.size() > 0) {
            this.list.clear();
            this.list.addAll(list);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    refreshList();
                }
            });

        } else {
            FinishLoading(getString(R.string.empty_data));
        }
    }

    private List<OutputList> getList(String data) throws Exception {
        return new Gson().fromJson(data, new TypeToken<List<OutputList>>() {
        }.getType());
    }

    private void refreshList() {
        if (adapter == null) {
            initAdapter();
            rvItem.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
        FinishLoading(null);
    }

    private void initAdapter() {
        adapter = new OutputListAdapter(this, list);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivityForResult(new Intent()
                                .setClass(OutputListActivity.this, OutputDetailActivity.class)
                                .putExtra("RecNo", list.get(position).getIRecNo())
                                .putExtra("taskid", list.get(position).getIProTaskOrderMRecNo())
                                .putExtra("taskname", list.get(position).getSProTaskOrderMBillNo())
                                .putExtra("stockname", list.get(position).getSStockName())
                                .putExtra("stockid", list.get(position).getiBscDataStockMRecNo())
                                .putExtra("red", list.get(position).getSRed())
                        , 0
                );

            }
        });
    }

    private void FinishLoading(@NonNull String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LoadingDialog.cancelDialogForLoading();
                if (StringUtil.isNotEmpty(msg)) {
                    Toasts.showShort(OutputListActivity.this, msg);
                }
            }
        });

    }

    @OnClick({R.id.iv_back, R.id.iv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_right:
                startActivityForResult(new Intent().setClass(OutputListActivity.this, OutputDetailActivity.class), 0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case ResultCode.DeleteCode:
                getData();
                break;
            case ResultCode.RefreshCode:
                getData();
                break;
            default:
                break;
        }
    }
}
