package com.yyy.huojia.input;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.yyy.huojia.input.model.InputList;
import com.yyy.huojia.interfaces.OnItemClickListener;
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

public class InputListActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rv_item)
    RecyclerView rvItem;

    SharedPreferencesHelper preferencesHelper;

    String url;
    String userid;

    List<InputList> inputLists;

    InputListAdapter inputListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
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

    private void initView() {
        tvTitle.setText(getString(R.string.title_input));
        initRecycle();
    }

    private void initRecycle() {
        rvItem.setLayoutManager(new LinearLayoutManager(this));
        rvItem.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.VERTICAL));
    }

    private void initList() {
        inputLists = new ArrayList<>();
    }


    private void getDefaultData() {
        url = (String) preferencesHelper.getSharedPreference("address", "") + NetConfig.server + NetConfig.PDAHandler_Method;
        userid = (String) preferencesHelper.getSharedPreference("userid", "");
    }

    private List<NetParams> getParams() {
        List<NetParams> params = new ArrayList<>();
        params.add(new NetParams("otype", Otypes.InputList));
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
        List<InputList> list = getList(tables);
        if (list != null && list.size() > 0) {
            inputLists.addAll(list);
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

    private List<InputList> getList(String data) throws Exception {
        return new Gson().fromJson(data, new TypeToken<List<InputList>>() {
        }.getType());
    }

    private void refreshList() {
        if (inputListAdapter == null) {
            initAdapter();
            rvItem.setAdapter(inputListAdapter);
        } else {
            inputListAdapter.notifyDataSetChanged();
        }
        FinishLoading(null);
    }

    private void initAdapter() {
        inputListAdapter = new InputListAdapter(this, inputLists);
        inputListAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivityForResult(new Intent()
                                .setClass(InputListActivity.this, InputDetailActivity.class)
                                .putExtra("RecNo", inputLists.get(position).getIRecNo())
                                .putExtra("supplierid", inputLists.get(position).getiBscDataCustomerRecNo())
                                .putExtra("suppliername", inputLists.get(position).getSCustShortName())
                                .putExtra("stockname", inputLists.get(position).getSStockName())
                                .putExtra("stockid", inputLists.get(position).getiBscDataStockMRecNo())
                                .putExtra("red", inputLists.get(position).getSRed())
                        , 0
                );

            }
        });
    }

    private void goDetial(int RecNo) {
        startActivityForResult(new Intent().setClass(this, InputDetailActivity.class).putExtra("RecNo", RecNo), 0);
    }

    @OnClick({R.id.iv_back, R.id.iv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_right:
                goDetial(0);
                break;
        }
    }

    private void FinishLoading(@NonNull String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LoadingDialog.cancelDialogForLoading();
                if (StringUtil.isNotEmpty(msg)) {
                    Toasts.showShort(InputListActivity.this, msg);
                }
            }
        });

    }
}
