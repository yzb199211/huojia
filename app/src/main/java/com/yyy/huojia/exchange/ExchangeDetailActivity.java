package com.yyy.huojia.exchange;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yyy.huojia.R;
import com.yyy.huojia.dialog.DeleteDialog;
import com.yyy.huojia.dialog.JudgeDialog;
import com.yyy.huojia.dialog.LoadingDialog;
import com.yyy.huojia.interfaces.OnItemClickListener;
import com.yyy.huojia.interfaces.ResponseListener;
import com.yyy.huojia.model.Stock;
import com.yyy.huojia.model.TaskOrder;

import com.yyy.huojia.util.ResultCode;
import com.yyy.huojia.util.SharedPreferencesHelper;
import com.yyy.huojia.util.StringUtil;
import com.yyy.huojia.util.Toasts;
import com.yyy.huojia.util.TransInformation;
import com.yyy.huojia.util.net.NetConfig;
import com.yyy.huojia.util.net.NetParams;
import com.yyy.huojia.util.net.NetUtil;
import com.yyy.huojia.util.net.Otypes;
import com.yyy.huojia.view.recycle.RecyclerViewDivider;
import com.yyy.yyylibrary.pick.builder.OptionsPickerBuilder;
import com.yyy.yyylibrary.pick.listener.OnOptionsSelectListener;
import com.yyy.yyylibrary.pick.view.OptionsPickerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExchangeDetailActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.tv_stock_in)
    TextView tvStockIn;
    @BindView(R.id.tv_stock_out)
    TextView tvStockOut;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_total_weight)
    TextView tvTotalWeight;
    @BindView(R.id.tv_total_money)
    TextView tvTotalMoney;
    @BindView(R.id.rv_item)
    RecyclerView rvItem;
    @BindView(R.id.tv_delete)
    TextView tvDelete;

    SharedPreferencesHelper preferencesHelper;

    int RecNo = 0;
    int stockidIn;
    int stockidOut;

    ExchangeList item;

    String url;
    String userid;

    List<Stock> stocks;
    List<TaskOrder> tasks;
    List<BarCode> barCodes;

    Set<String> codes;

    private OptionsPickerView pvStockIn;
    private OptionsPickerView pvStockOut;
    BarCodeAdapter barCodeAdapter;

    JudgeDialog clearDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_detail);
        ButterKnife.bind(this);
        preferencesHelper = new SharedPreferencesHelper(this, getString(R.string.preferenceCache));
        init();
    }

    private void init() {
        getDefaultData();
        initList();
        getIntentData();
        initView();
        judgeRecNo();
    }

    private void getDefaultData() {
        url = (String) preferencesHelper.getSharedPreference("address", "") + NetConfig.server + NetConfig.PDAHandler_Method;
        userid = (String) preferencesHelper.getSharedPreference("userid", "");
    }

    private void initList() {
        stocks = new ArrayList<>();
        tasks = new ArrayList<>();
        barCodes = new ArrayList<>();
        codes = new HashSet<>();
    }

    private void getIntentData() {
        String data = getIntent().getStringExtra("data");
        if (StringUtil.isNotEmpty(data)) {
            item = new Gson().fromJson(data, ExchangeList.class);
            RecNo = item.getIRecNo();
            stockidIn = item.getIInBscDataStockMRecNo();
            stockidOut = item.getIOutBscDataStockMRecNo();
            tvStockIn.setText(item.getSInStockName());
            tvStockOut.setText(item.getSOutStockName());
        }
    }

    private void initView() {
        tvTitle.setText(getString(R.string.title_output));
        ivRight.setVisibility(View.GONE);
        tvTotalWeight.setText("总重量：0kg");
        tvTotalMoney.setText("总卷数：0");
        initRecycle();
        initScanCode();
    }

    private void initRecycle() {
        rvItem.setLayoutManager(new LinearLayoutManager(this));
        rvItem.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.VERTICAL));
    }

    private void initScanCode() {
        etCode.setTransformationMethod(new TransInformation());
        etCode.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.e("code", event.getScanCode() + "");
                if ((keyCode == KeyEvent.KEYCODE_ENTER || event.getScanCode() == 148) && event.getAction() == KeyEvent.ACTION_UP) {
                    getCodeData(etCode.getText().toString());
                    etCode.setText("");
                }
                return false;
            }
        });

    }

    private List<NetParams> getCodeParams(String code) {
        List<NetParams> params = new ArrayList<>();
        params.add(new NetParams("otype", Otypes.OutBarCode));
        params.add(new NetParams("sBarcode", code));
        params.add(new NetParams("iOutBscDataStockMRecNo", stockidOut + ""));
        return params;
    }

    private void getCodeData(String code) {
        if (stockidOut == 0) {
            Toasts.showShort(this, "请选择出库仓");
            return;
        }
        LoadingDialog.showDialogForLoading(this);
        new NetUtil(getCodeParams(code), url, new ResponseListener() {
            @Override
            public void onSuccess(String string) {
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    if (jsonObject.optBoolean("success")) {
                        initCodeList(jsonObject.optJSONArray("tables").optString(0));
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

    private void initCodeList(String tables) throws Exception {
        List<BarCode> list = new Gson().fromJson(tables, new TypeToken<List<BarCode>>() {
        }.getType());
        removeRepeat(list);
    }

    private void removeRepeat(List<BarCode> list) throws Exception {
        for (int i = 0; i < list.size(); i++) {
            int size = codes.size();
            codes.add(list.get(i).getSBatchNo());
            if (size < codes.size()) {
                barCodes.add(0, list.get(i));
            }
        }
        setRecycle();
    }


    private void setRecycle() {
        if (barCodes.size() > 0) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    refreshList();
                    FinishLoading(null);
                }
            });
        }
    }

    private void refreshList() {
        if (barCodeAdapter == null) {
            barCodeAdapter = new BarCodeAdapter(this, barCodes);
            barCodeAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    showDeleteDialog(position);
                }
            });
            rvItem.setAdapter(barCodeAdapter);
        } else {
            barCodeAdapter.notifyDataSetChanged();
        }
        refreshView();
    }

    DeleteDialog deleteDialog;

    private void showDeleteDialog(int position) {
        if (deleteDialog == null) {
            deleteDialog = new DeleteDialog(this).content("确认是否删除" + barCodes.get(position).getSBatchNo() + "?");
        } else {
            deleteDialog.setContent(("确认是否删除" + barCodes.get(position).getSBatchNo() + "?"));
        }
        deleteDialog.setOnCloseListener(new DeleteDialog.OnCloseListener() {
            @Override
            public void onClick(boolean confirm) {
                if (confirm) {
                    codes.remove(barCodes.get(position).getSBatchNo());
                    barCodes.remove(position);
                    barCodeAdapter.notifyItemRemoved(position);
                    refreshView();
                }
            }
        });
        deleteDialog.show();
    }

    private void refreshView() {
        tvTotalWeight.setText("总重量：" + getTotalWeight() + "kg");
        tvTotalMoney.setText("总卷数：" + barCodes.size());
    }

    private int getTotalMoney() {
        int money = 0;
        for (BarCode barCode : barCodes) {
            money = money + barCode.getFTotal();
        }
        return money;
    }

    private int getTotalWeight() {
        int weight = 0;
        for (BarCode barCode : barCodes) {
            weight = weight + barCode.getFQty();
        }
        return weight;
    }

    private void judgeRecNo() {
        if (RecNo == 0) {
            tvDelete.setVisibility(View.INVISIBLE);
        } else {
            initData();
        }
    }

    private void initData() {
        getData();
    }

    private List<NetParams> getParams() {
        List<NetParams> params = new ArrayList<>();
        params.add(new NetParams("iMMStockDbMRecNo", RecNo + ""));
        params.add(new NetParams("otype", Otypes.ExchangeDetail));
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
                        initCodeList(jsonObject.optJSONArray("tables").optString(0));
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


    private void showClearDialog() {
        if (clearDialog == null)
            clearDialog = new JudgeDialog(this, R.style.JudgeDialog, "是否清空所有条码数据？", new JudgeDialog.OnCloseListener() {
                @Override
                public void onClick(boolean confirm) {
                    if (confirm) {
                        clear();
                    }
                }
            });
        clearDialog.show();
    }

    @OnClick({R.id.iv_back, R.id.tv_stock_in, R.id.tv_stock_out, R.id.tv_clear, R.id.tv_delete, R.id.tv_save, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_stock_in:
                select_stock(2);
                break;
            case R.id.tv_stock_out:
                select_stock(1);
                break;
            case R.id.tv_clear:
                showClearDialog();
                break;
            case R.id.tv_delete:
                showDeleteMain();
                break;
            case R.id.tv_save:
                if (canSave()) {
                    confirmSave();
                }
                break;
            case R.id.tv_submit:
                confirmSubmit();
                break;
        }
    }

    JudgeDialog deleteMain;

    private void showDeleteMain() {
        if (deleteMain == null)
            deleteMain = new JudgeDialog(this, R.style.JudgeDialog, "确认是否删除该领用单？", new JudgeDialog.OnCloseListener() {
                @Override
                public void onClick(boolean confirm) {
                    if (confirm) {
                        delete();
                    }
                }
            });
        deleteMain.show();
    }

    private List<NetParams> getDeleteParams() {
        List<NetParams> params = new ArrayList<>();
        params.add(new NetParams("otype", Otypes.ExchangeDelete));
        params.add(new NetParams("iRecNo", RecNo + ""));
        return params;
    }

    private void delete() {
        LoadingDialog.showDialogForLoading(this);
        new NetUtil(getDeleteParams(), url, new ResponseListener() {
            @Override
            public void onSuccess(String string) {
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    if (jsonObject.optBoolean("success")) {
                        FinishLoading("删除成功");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setResult(ResultCode.DeleteCode);
                                finish();
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

    JudgeDialog submitDialog;

    private void confirmSubmit() {
        if (submitDialog == null)
            submitDialog = new JudgeDialog(this, R.style.JudgeDialog, "是否提交数据？", new JudgeDialog.OnCloseListener() {
                @Override
                public void onClick(boolean confirm) {
                    if (confirm) {
                        save(true);
                    }
                }
            });
        submitDialog.show();
    }

    private List<NetParams> getSubmitParams() {
        List<NetParams> params = new ArrayList<>();
        params.add(new NetParams("otype", Otypes.ExchangeSubmit));
        params.add(new NetParams("iRecNo", RecNo + ""));
        params.add(new NetParams("sUserID", userid));
        return params;
    }

    private void submit() {
        LoadingDialog.showDialogForLoading(this);
        new NetUtil(getSubmitParams(), url, new ResponseListener() {
            @Override
            public void onSuccess(String string) {
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    if (jsonObject.optBoolean("success")) {
                        FinishLoading("领用成功");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setResult(ResultCode.RefreshCode);
                                finish();
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
        if (stockidIn == 0 || stockidOut == 0) {
            Toasts.showShort(this, "出库仓和入库存不能为空");
            return false;
        }
        return true;
    }

    JudgeDialog saveDialog;

    private void confirmSave() {
        if (saveDialog == null)
            saveDialog = new JudgeDialog(this, R.style.JudgeDialog, "是否保存数据？", new JudgeDialog.OnCloseListener() {
                @Override
                public void onClick(boolean confirm) {
                    if (confirm) {
                        save(false);
                    }
                }
            });
        saveDialog.show();
    }

    private List<NetParams> getSaveParams() {
        List<NetParams> params = new ArrayList<>();
        params.add(new NetParams("otype", Otypes.ExchangeSave));
        params.add(new NetParams("iOutBscDataStockMRecNo", stockidOut + ""));
        params.add(new NetParams("iInBscDataStockMRecNo", stockidIn + ""));
        params.add(new NetParams("sUserID", userid));
        params.add(new NetParams("iMMStockDbMRecNo", RecNo + ""));
        params.add(new NetParams("sBarCodes", getBarcodes()));
        return params;
    }

    private void save(boolean submit) {
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
                                if (submit) {
                                    try {
                                        RecNo = jsonObject.optJSONArray("tables").optJSONArray(0).getJSONObject(0).optInt("iRecNo");
                                        Log.e("no", jsonObject.optJSONArray("tables").optJSONArray(0).getJSONObject(0).optInt("iRecNo") + "");
                                        submit();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        FinishLoading(getString(R.string.error_json));
                                    }
                                } else {
                                    setResult(ResultCode.RefreshCode);
                                    finish();
                                }
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

    private String getBarcodes() {
        String barcode = "";
        for (int i = 0; i < barCodes.size(); i++) {
            if (i == 0) {
                barcode = barcode + barCodes.get(i).getSBatchNo();
            } else {
                barcode = barcode + "," + barCodes.get(i).getSBatchNo();
            }
        }
        return barcode;
    }


    private void select_stock(int type) {
        if (stocks.size() == 0) {
            getStocksData(type);
        } else {
            showStockPick(type);
        }
    }

    private List<NetParams> getStockParams() {
        List<NetParams> params = new ArrayList<>();
        params.add(new NetParams("otype", Otypes.ExchangeStock));
        return params;
    }

    private void getStocksData(int type) {
        LoadingDialog.showDialogForLoading(this);
        new NetUtil(getStockParams(), url, new ResponseListener() {
            @Override
            public void onSuccess(String string) {
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    if (jsonObject.optBoolean("success")) {
                        initStockList(jsonObject.optJSONArray("tables").optString(0), type);
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

    private void initStockList(String tables, int type) throws Exception {
        stocks.addAll(new Gson().fromJson(tables, new TypeToken<List<Stock>>() {
        }.getType()));
        if (stocks.size() > 0) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showStockPick(type);
                }
            });

        }
    }

    private void showStockPick(int type) {
        if (type == 1) {
            showStockOutPick();
        } else if (type == 2) {
            showStockInPick();
        }
    }

    private void showStockInPick() {
        if (pvStockIn == null) {
            initPvStockIn();
        }
        pvStockIn.show();
    }

    private void showStockOutPick() {
        if (pvStockOut == null) {
            initPvStockOut();
        }
        pvStockOut.show();
    }

    private void initPvStockOut() {
        pvStockOut = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                stockidOut = stocks.get(options1).getIRecNo();
                tvStockOut.setText(stocks.get(options1).getSStockName());
            }
        })
                .setTitleText("出库仓选择")
                .setContentTextSize(18)//设置滚轮文字大小
                .setDividerColor(Color.LTGRAY)//设置分割线的颜色
                .setSelectOptions(0)//默认选中项
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setLabels("", "", "")
                .isDialog(true)
                .setBgColor(0xFFFFFFFF) //设置外部遮罩颜色
                .build();
        pvStockOut.setPicker(stocks);//一级选择器
        setDialog(pvStockOut);
    }

    private void initPvStockIn() {
        pvStockIn = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                stockidIn = stocks.get(options1).getIRecNo();
                tvStockIn.setText(stocks.get(options1).getSStockName());
            }
        })
                .setTitleText("出库仓选择")
                .setContentTextSize(18)//设置滚轮文字大小
                .setDividerColor(Color.LTGRAY)//设置分割线的颜色
                .setSelectOptions(0)//默认选中项
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setLabels("", "", "")
                .isDialog(true)
                .setBgColor(0xFFFFFFFF) //设置外部遮罩颜色
                .build();
        pvStockIn.setPicker(stocks);//一级选择器
        setDialog(pvStockIn);
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

    private void clear() {
        codes.clear();
        barCodes.clear();
        refreshList();
    }

    private void FinishLoading(@NonNull String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LoadingDialog.cancelDialogForLoading();
                if (StringUtil.isNotEmpty(msg)) {
                    Toasts.showShort(ExchangeDetailActivity.this, msg);
                }
            }
        });
    }


}
