package com.yyy.huojia.kaiping;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yyy.huojia.R;
import com.yyy.huojia.dialog.JudgeDialog;
import com.yyy.huojia.dialog.LoadingDialog;
import com.yyy.huojia.interfaces.ResponseListener;
import com.yyy.huojia.model.Stock;
import com.yyy.huojia.model.TaskOrder;
import com.yyy.huojia.output.OutputDetailActivity;
import com.yyy.huojia.util.ResultCode;
import com.yyy.huojia.util.SharedPreferencesHelper;
import com.yyy.huojia.util.StringUtil;
import com.yyy.huojia.util.Toasts;
import com.yyy.huojia.util.TransInformation;
import com.yyy.huojia.util.net.NetConfig;
import com.yyy.huojia.util.net.NetParams;
import com.yyy.huojia.util.net.NetUtil;
import com.yyy.huojia.util.net.Otypes;
import com.yyy.yyylibrary.pick.builder.OptionsPickerBuilder;
import com.yyy.yyylibrary.pick.listener.OnOptionsSelectListener;
import com.yyy.yyylibrary.pick.view.OptionsPickerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class KaiPingDetailActivity extends AppCompatActivity {

    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_stock)
    TextView tvStock;
    @BindView(R.id.tv_task)
    TextView tvTask;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_weight)
    TextView tvWeight;
    @BindView(R.id.rv_item)
    RecyclerView rvItem;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;

    List<Stock> stocks = new ArrayList<>();
    List<TaskOrder> tasks = new ArrayList<>();
    List<KaiPingDetailB> detailBS = new ArrayList<>();
    KaiPingB kaiPingB;
    private OptionsPickerView pvStock;
    private OptionsPickerView pvTask;
    private KaiPingAdapter adapter;

    int RecNo;
    int stockid;
    int taskid;

    String url;
    String userid;
    String code;
    SharedPreferencesHelper preferencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kai_ping_detail);
        ButterKnife.bind(this);
        preferencesHelper = new SharedPreferencesHelper(this, getString(R.string.preferenceCache));
        init();
    }

    private void init() {
        initTop();
        intiRecycle();
        url = (String) preferencesHelper.getSharedPreference("address", "") + NetConfig.server + NetConfig.PDAHandler_Method;
        initScanCode();
    }

    private void intiRecycle() {
        rvItem.setLayoutManager(new LinearLayoutManager(this));
        rvItem.setAdapter(getAdapter());
    }

    private void initTop() {
        ivRight.setVisibility(View.GONE);
        tvTitle.setText("开平单");
    }

    private KaiPingAdapter getAdapter() {
        adapter = new KaiPingAdapter(this, detailBS);
        return adapter;
    }

    private void initScanCode() {
        etCode.setTransformationMethod(new TransInformation());
        etCode.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.e("code", event.getScanCode() + "");
                if ((keyCode == KeyEvent.KEYCODE_ENTER || event.getScanCode() == 148) && event.getAction() == KeyEvent.ACTION_UP) {
                    clear();
                    getCodeData(etCode.getText().toString());
                    etCode.setText("");
                }
                return false;
            }
        });

    }

    private void clear() {
        detailBS.clear();
        kaiPingB = null;
        tvName.setText("");
        tvWeight.setText("");
        adapter.notifyDataSetChanged();
    }

    private List<NetParams> getCodeParams(String code) {
        List<NetParams> params = new ArrayList<>();
        params.add(new NetParams("otype", Otypes.OutBarCode));
        params.add(new NetParams("sBarCode", code));
        params.add(new NetParams("iProTaskOrderMRecNo", taskid + ""));
        params.add(new NetParams("iBscDataStockMRecNo", stockid + ""));
        return params;
    }

    private void getCodeData(String code) {
        if (stockid == 0) {
            Toasts.showShort(this, "请选择仓库");
            return;
        }
        if (taskid == 0) {
            Toasts.showShort(this, "请选择任务单");
            return;
        }
        LoadingDialog.showDialogForLoading(this);
        new NetUtil(getCodeParams(code), url, new ResponseListener() {
            @Override
            public void onSuccess(String string) {
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    if (jsonObject.optBoolean("success")) {
                        initMain((jsonObject.optJSONObject("tables").optJSONArray("Main").optString(0)));
                        initCodeList(jsonObject.optJSONObject("tables").optString("Detail"));
                        KaiPingDetailActivity.this.code = code;
                        FinishLoading(null);
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

    private void initMain(String optString) throws Exception {
        kaiPingB = new Gson().fromJson(optString, KaiPingB.class);
        setMain();
    }

    private void setMain() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvName.setText(kaiPingB.getSName());
                tvWeight.setText("总长度：" + kaiPingB.getFLength());
            }
        });

    }

    private void initCodeList(String optString) throws Exception {
        detailBS.addAll(new Gson().fromJson(optString, new TypeToken<List<KaiPingDetailB>>() {
        }.getType()));
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });

    }

    @OnClick({R.id.iv_back, R.id.tv_stock, R.id.tv_task, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_stock:
                select_stock();
                break;
            case R.id.tv_task:
                select_task();
                break;
            case R.id.tv_submit:
                if (canSave())
                    confirmSave();
                break;
            default:
                break;
        }
    }

    JudgeDialog saveDialog;

    private void confirmSave() {
        if (saveDialog == null)
            saveDialog = new JudgeDialog(this, R.style.JudgeDialog, "是否保存数据？");
        saveDialog.setOnCloseListener(confirm -> {
            if (confirm) {
                save();
            }
        });
        saveDialog.show();
    }

    private List<NetParams> getSaveParams() {
        List<NetParams> params = new ArrayList<>();
        params.add(new NetParams("otype", Otypes.SaveMMStockOutMEven));
        params.add(new NetParams("iBscDataStockMRecNo", stockid + ""));
        params.add(new NetParams("iProTaskOrderMRecNo", taskid + ""));
        params.add(new NetParams("sUserID", userid));
        params.add(new NetParams("sBarCodes", code));
        return params;
    }

    private void save() {
        LoadingDialog.showDialogForLoading(this);
        new NetUtil(getSaveParams(), url, new ResponseListener() {
            @Override
            public void onSuccess(String string) {
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    if (jsonObject.optBoolean("success")) {
                        FinishLoading(null);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                Toasts.showShort(KaiPingDetailActivity.this, "保存成功");
                                FinishLoading("保存成功");
                                clear();
                            }
                        });
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

    private boolean canSave() {
        if (stockid == 0 || taskid == 0) {
            Toasts.showShort(this, "仓库和开平任务单不能为空");
            return false;
        }
        return true;
    }

    private void select_stock() {
        if (stocks.size() == 0) {
            getStocksData();
        } else {
            showStockPick();
        }
    }

    private void showStockPick() {
        if (pvStock == null) {
            initPvStock();
        }
        pvStock.show();
    }

    private void initPvStock() {
        pvStock = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                stockid = stocks.get(options1).getIRecNo();
                tvStock.setText(stocks.get(options1).getSStockName());
            }
        })
                .setTitleText("仓库选择")
                .setContentTextSize(18)//设置滚轮文字大小
                .setDividerColor(Color.LTGRAY)//设置分割线的颜色
                .setSelectOptions(0)//默认选中项
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setLabels("", "", "")
                .isDialog(true)
                .setBgColor(0xFFFFFFFF) //设置外部遮罩颜色
                .build();
        pvStock.setPicker(stocks);//一级选择器
        setDialog(pvStock);
    }

    private List<NetParams> getStockParams() {
        List<NetParams> params = new ArrayList<>();
        params.add(new NetParams("otype", Otypes.GetEvenStockM));
        return params;
    }

    private void getStocksData() {
        LoadingDialog.showDialogForLoading(this);
        new NetUtil(getStockParams(), url, new ResponseListener() {
            @Override
            public void onSuccess(String string) {
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    if (jsonObject.optBoolean("success")) {
                        initStockList(jsonObject.optJSONArray("tables").optString(0));
                        FinishLoading(null);
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

    private void initStockList(String tables) throws Exception {
        stocks.addAll(new Gson().fromJson(tables, new TypeToken<List<Stock>>() {
        }.getType()));
        if (stocks.size() > 0) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showStockPick();
                }
            });

        }
    }

    private void select_task() {
        if (tasks.size() == 0) {
            getTaskData();
        } else {
            showTaskPick();
        }
    }

    private void showTaskPick() {
        if (pvTask == null) {
            initPvTask();
        }
        pvTask.show();
    }

    private void initPvTask() {
        pvTask = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                taskid = tasks.get(options1).getIRecNo();
                tvTask.setText(tasks.get(options1).getPickerViewText());
            }
        })
                .setTitleText("开平任务单")
                .setContentTextSize(18)//设置滚轮文字大小
                .setDividerColor(Color.LTGRAY)//设置分割线的颜色
                .setSelectOptions(0)//默认选中项
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setLabels("", "", "")
                .isDialog(true)
                .setBgColor(0xFFFFFFFF) //设置外部遮罩颜色
                .build();
        pvTask.setPicker(tasks);//一级选择器
        setDialog(pvTask);
    }

    private List<NetParams> getTaskParams() {
        List<NetParams> params = new ArrayList<>();
        params.add(new NetParams("otype", Otypes.Task));
        params.add(new NetParams("iType", "1"));
        return params;
    }

    private void getTaskData() {
        LoadingDialog.showDialogForLoading(this);
        new NetUtil(getTaskParams(), url, new ResponseListener() {
            @Override
            public void onSuccess(String string) {
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    if (jsonObject.optBoolean("success")) {
                        initTaskList(jsonObject.optJSONArray("tables").optString(0));
                        FinishLoading(null);
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

    private void initTaskList(String tables) throws Exception {
        tasks.addAll(new Gson().fromJson(tables, new TypeToken<List<TaskOrder>>() {
        }.getType()));
        if (tasks.size() > 0) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showTaskPick();
                }
            });

        }
    }

    private void setDialog(OptionsPickerView pickview) {
        getDialogLayoutParams();
        pickview.getDialogContainerLayout().setLayoutParams(getDialogLayoutParams());
        initDialogWindow(pickview.getDialog().getWindow());
    }

    private void initDialogWindow(Window window) {
        window.setWindowAnimations(R.style.picker_view_slide_anim);//修改动画样式
        window.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
        window.setDimAmount(0.1f);
        window.setAttributes(getDialogWindowLayoutParams(window));
    }

    private WindowManager.LayoutParams getDialogWindowLayoutParams(Window window) {
        WindowManager.LayoutParams winParams;
        winParams = window.getAttributes();
        winParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        return winParams;
    }

    private FrameLayout.LayoutParams getDialogLayoutParams() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM);
        params.leftMargin = 0;
        params.rightMargin = 0;
        return params;
    }

    private void FinishLoading(@NonNull String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LoadingDialog.cancelDialogForLoading();
                if (StringUtil.isNotEmpty(msg)) {
                    Toasts.showShort(KaiPingDetailActivity.this, msg);
                }
            }
        });
    }
}