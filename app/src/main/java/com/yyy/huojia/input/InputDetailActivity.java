package com.yyy.huojia.input;

import android.content.Intent;
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
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yyy.huojia.R;
import com.yyy.huojia.dialog.DeleteDialog;
import com.yyy.huojia.dialog.EditDialog;
import com.yyy.huojia.dialog.JudgeDialog;
import com.yyy.huojia.dialog.LoadingDialog;
import com.yyy.huojia.interfaces.OnItemClickListener;
import com.yyy.huojia.interfaces.OnItemLongClickListener;
import com.yyy.huojia.interfaces.ResponseListener;
import com.yyy.huojia.input.model.BarCode;
import com.yyy.huojia.model.Stock;
import com.yyy.huojia.model.Supplier;
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

public class InputDetailActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_stock)
    TextView tvStock;
    @BindView(R.id.tv_supplier)
    TextView tvSupplier;
    @BindView(R.id.switch_view)
    Switch switchView;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.rv_item)
    RecyclerView rvItem;
    @BindView(R.id.tv_total_num)
    TextView tvTotalNum;
    @BindView(R.id.tv_total_weight)
    TextView tvTotalWeight;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    @BindView(R.id.tv_save)
    TextView tvSave;

    SharedPreferencesHelper preferencesHelper;

    int RecNo;
    int stockid;
    int supplierid;

    String url;
    String userid;

    List<Stock> stocks;
    List<Supplier> suppliers;
    List<BarCode> barCodes;

    Set<String> codes;


    private OptionsPickerView pvStock;
    private OptionsPickerView pvSupplier;

    BarCodeAdapter barCodeAdapter;

    EditDialog editDialog;
    JudgeDialog clearDialog;

    boolean isRed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_detail);
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
        setRedListener();
    }


    private void getDefaultData() {
        url = (String) preferencesHelper.getSharedPreference("address", "") + NetConfig.server + NetConfig.PDAHandler_Method;
        userid = (String) preferencesHelper.getSharedPreference("userid", "");
    }

    private void initList() {
        stocks = new ArrayList<>();
        suppliers = new ArrayList<>();
        barCodes = new ArrayList<>();
        codes = new HashSet<>();
    }

    private void getIntentData() {
        RecNo = getIntent().getIntExtra("RecNo", 0);
    }

    private void initView() {
        tvTitle.setText(getString(R.string.title_input));
        tvRight.setText(getString(R.string.top_no_arrvied));
        tvRight.setVisibility(View.VISIBLE);
        ivRight.setVisibility(View.GONE);
        tvTotalNum.setText("总卷数：0");
        tvTotalWeight.setText("总重量：0kg");
        initRecycle();
        initScanCode();
    }

    private void setRedListener() {
        switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (codes.size() > 0 && isRed != isChecked) {
                    showClearDialog(isChecked);
                }
            }
        });
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
        params.add(new NetParams("otype", Otypes.InBarCode));
        params.add(new NetParams("sBarCode", code));
        params.add(new NetParams("iRed", switchView.isChecked() ? "1" : "0"));
        params.add(new NetParams("iBscDataCustomerRecNo", supplierid + ""));
        params.add(new NetParams("iBscDataStockMRecNo", stockid + ""));
        return params;
    }

    private void getCodeData(String code) {
        if (stockid == 0) {
            Toasts.showShort(this, "请选择仓库");
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

    private void judgeRecNo() {
        if (RecNo == 0) {
            tvDelete.setVisibility(View.INVISIBLE);
            tvSave.setVisibility(View.INVISIBLE);
        } else {
            initData();
        }
    }

    private void initData() {
        supplierid = getIntent().getIntExtra("supplierid", 0);
        stockid = getIntent().getIntExtra("stockid", 0);
        tvStock.setText(getIntent().getStringExtra("stockname"));
        tvSupplier.setText(getIntent().getStringExtra("suppliername"));
        isRed = getIntent().getStringExtra("red").equals("是");
        switchView.setChecked(isRed);
        getData();
    }


    private List<NetParams> getParams() {
        List<NetParams> params = new ArrayList<>();
        params.add(new NetParams("iMMStockInMRecNo", RecNo + ""));
        params.add(new NetParams("otype", Otypes.InputDetail));
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

    private void initCodeList(String tables) throws Exception {
        List<BarCode> list = new Gson().fromJson(tables, new TypeToken<List<BarCode>>() {
        }.getType());
        removeRepeat(list);
    }

    private void removeRepeat(List<BarCode> list) {
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
                    if (supplierid == 0) {
                        supplierid = barCodes.get(0).getiBscDataCustomerRecNo();
                        tvSupplier.setText(barCodes.get(0).getsCustShortName());
                    }
                    refreshList();
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
                    showEditDialog(position);
                }
            });
            barCodeAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
                @Override
                public void onLongClick(View view, int position) {
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
        tvTotalNum.setText("总卷数：" + barCodes.size());
        tvTotalWeight.setText("总重量：" + getTotalWeight() + "kg");
    }

    private int getTotalWeight() {
        int weight = 0;
        for (BarCode barCode : barCodes) {
            weight = weight + barCode.getFQty();
        }
        return weight;
    }

    private void showEditDialog(int position) {
        if (editDialog == null) {
            editDialog = new EditDialog(this).title("修改" + barCodes.get(position).getSBatchNo() + "重量").max(barCodes.get(position).getFQty());
        } else {
            editDialog.setTitle(("修改" + barCodes.get(position).getSCode() + "重量"));
            editDialog.setMax(barCodes.get(position).getFQty());
        }
        editDialog.setOnCloseListener(new EditDialog.OnCloseListener() {
            @Override
            public void onClick(boolean confirm, @NonNull String data) {
                if (confirm) {
                    barCodes.get(position).setFQty(Double.parseDouble(data));
                    barCodeAdapter.notifyItemChanged(position);
                    refreshView();
                }
            }
        });
        editDialog.show();
    }

    @OnClick({R.id.iv_back, R.id.tv_right, R.id.tv_stock, R.id.tv_supplier, R.id.tv_clear, R.id.tv_delete, R.id.tv_save, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right:
                startActivity(new Intent().setClass(InputDetailActivity.this, InputNotArrivedActivity.class));
                break;
            case R.id.tv_stock:
                select_stock();
                break;
            case R.id.tv_supplier:
                select_supplier();
                break;
            case R.id.tv_clear:
                showClearDialog(isRed);
                break;
            case R.id.tv_delete:
                break;
            case R.id.tv_save:
                if (canSave()) {
                    confirmSave();
                }
                break;
            case R.id.tv_submit:
                confirmSubmit();
                break;
            default:
                break;
        }
    }

    private void showClearDialog(boolean isRed) {
        if (clearDialog == null)
            clearDialog = new JudgeDialog(this, R.style.JudgeDialog, "是否清空所有条码数据？", new JudgeDialog.OnCloseListener() {
                @Override
                public void onClick(boolean confirm) {
                    if (confirm) {
                        clear(isRed);
                    } else {
                        switchView.setChecked(isRed);
                    }
                }
            });
        clearDialog.show();
    }

    private void clear(boolean isRed) {
        codes.clear();
        barCodes.clear();
        refreshList();
        this.isRed = isRed;
    }

    private void clearAll() {
        codes.clear();
        barCodes.clear();
        refreshList();
        switchView.setChecked(false);
        isRed = false;
        RecNo = 0;
    }

    private void select_supplier() {
        if (suppliers.size() == 0) {
            getSupplierData();
        } else {
            showSupplierPick();
        }
    }


    private void select_stock() {
        if (stocks.size() == 0) {
            getStocksData();
        } else {
            showStockPick();
        }
    }

    private List<NetParams> getSupplierParams() {
        List<NetParams> params = new ArrayList<>();
        params.add(new NetParams("otype", Otypes.Supplier));
        return params;
    }

    private void getSupplierData() {
        LoadingDialog.showDialogForLoading(this);
        new NetUtil(getSupplierParams(), url, new ResponseListener() {
            @Override
            public void onSuccess(String string) {
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    if (jsonObject.optBoolean("success")) {
                        initSupplierList(jsonObject.optJSONArray("tables").optString(0));
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

    private void initSupplierList(String tables) throws Exception {
        suppliers.addAll(new Gson().fromJson(tables, new TypeToken<List<Supplier>>() {
        }.getType()));
        if (suppliers.size() > 0) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showSupplierPick();
                }
            });

        }
    }

    private List<NetParams> getStockParams() {
        List<NetParams> params = new ArrayList<>();
        params.add(new NetParams("otype", Otypes.Stock));
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

    private void showStockPick() {
        if (pvStock == null) {
            initPvStock();
        }
        pvStock.show();
    }

    private void showSupplierPick() {
        if (pvSupplier == null) {
            initPvSupplier();
        }
        pvSupplier.show();
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

    private void initPvSupplier() {
        pvSupplier = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                supplierid = suppliers.get(options1).getIRecNo();
                tvSupplier.setText(suppliers.get(options1).getSCustName());
            }
        })
                .setTitleText("供应商选择")
                .setContentTextSize(18)//设置滚轮文字大小
                .setDividerColor(Color.LTGRAY)//设置分割线的颜色
                .setSelectOptions(0)//默认选中项
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setLabels("", "", "")
                .isDialog(true)
                .setBgColor(0xFFFFFFFF) //设置外部遮罩颜色
                .build();
        pvSupplier.setPicker(suppliers);//一级选择器
        setDialog(pvSupplier);
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

    private boolean canSave() {
        if (stockid == 0 || supplierid == 0) {
            Toasts.showShort(this, "仓库和供应商不能为空");
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
        params.add(new NetParams("otype", Otypes.InputSave));
        params.add(new NetParams("iBscDataStockMRecNo", stockid + ""));
        params.add(new NetParams("iBscDataCustomerRecNo", supplierid + ""));
        params.add(new NetParams("sUserID", userid));
        params.add(new NetParams("iMMStockInMRecNo", RecNo + ""));
        params.add(new NetParams("iRed", switchView.isChecked() ? "1" : "0"));
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
                barcode = barcode + barCodes.get(i).getSBatchNo() + "," + barCodes.get(i).getFQty();
            } else {
                barcode = barcode + ";" + barCodes.get(i).getSBatchNo() + "," + barCodes.get(i).getFQty();
            }
        }
        return barcode;
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
        params.add(new NetParams("otype", Otypes.InputSubmit));
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
                        FinishLoading("入库成功");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                clearAll();
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

    private void FinishLoading(@NonNull String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LoadingDialog.cancelDialogForLoading();
                if (StringUtil.isNotEmpty(msg)) {
                    Toasts.showShort(InputDetailActivity.this, msg);
                }
            }
        });
    }


}
