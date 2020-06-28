package com.yyy.huojia.login;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.google.gson.Gson;

import com.yyy.huojia.BaseActivity;
import com.yyy.huojia.BuildConfig;
import com.yyy.huojia.main.MainActivity;
import com.yyy.huojia.R;
import com.yyy.huojia.application.BaseApplication;
import com.yyy.huojia.dialog.JudgeDialog;
import com.yyy.huojia.dialog.LoadingDialog;
import com.yyy.huojia.interfaces.PermissionListener;
import com.yyy.huojia.interfaces.ResponseListener;
import com.yyy.huojia.login.model.LoginBean;
import com.yyy.huojia.util.FileUtil;
import com.yyy.huojia.util.PxUtil;
import com.yyy.huojia.util.SharedPreferencesHelper;
import com.yyy.huojia.util.StringUtil;
import com.yyy.huojia.util.Toasts;
import com.yyy.huojia.util.VersionUtil;
import com.yyy.huojia.util.net.NetConfig;
import com.yyy.huojia.util.net.NetParams;
import com.yyy.huojia.util.net.NetUtil;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;

public class LoginActivity extends BaseActivity {
    private final String TAG = "LoginActivity";

    @BindView(R.id.iv_login_header)
    ImageView ivLoginHeader;
    @BindView(R.id.et_user)
    EditText etUser;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.btn_login)
    TextView btnLogin;
    @BindView(R.id.btn_sweep)
    TextView btnSweep;

    private static final int REQUEST_CODE_SCAN = 11;
    SharedPreferencesHelper preferencesHelper;

    String userid;
    String password;
    String url;
    String address;

    int versionSystem;
    String dowmloadUrl;

    ProgressDialog progressDialog;
    //    Dialog loading;
    File file;
    boolean isTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        preferencesHelper = new SharedPreferencesHelper(this, getString(R.string.preferenceCache));
        inti();
        initView();
    }

    private void inti() {
        isTest = false;
        address = (String) preferencesHelper.getSharedPreference("address", "");
        if (isTest == true) {
            preferencesHelper.put("address", NetConfig.address);
            address = NetConfig.address;
        }
        versionSystem = VersionUtil.getAppVersionCode(this);

    }

    private void initView() {
        String userId = (String) preferencesHelper.getSharedPreference("userid", "");
        String passWord = (String) preferencesHelper.getSharedPreference("password", "");

        etUser.setText(userId);
        etUser.setSelection(etUser.getText().length());
        etPwd.setText(passWord);
        etPwd.setSelection(etPwd.getText().length());

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) ivLoginHeader.getLayoutParams();
        params.height = PxUtil.getHeight(this);
        ivLoginHeader.setLayoutParams(params);

    }


    @OnClick({R.id.btn_login, R.id.btn_sweep})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.btn_login:
                try {
                    isNone();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_sweep:
//                Toasts.showShort(this, "暂未开放");
                permission(intent);
//                String filePath = "/storage/emulated/0/Download/browser/031001700311-20586419.pdf";
//                String fileName = "031001700311-20586419.pdf";
//                DisplayFileActivity.openDispalyFileActivity(LoginActivity.this, filePath, fileName);
                break;
        }
    }


    /**
     * 判断url,用户名和密码是否为空
     */
    private void isNone() throws Exception {
        if (TextUtils.isEmpty(address)) {
            Toasts.showShort(this, getString(R.string.login_url));
            return;
        }

        url = address + NetConfig.server + NetConfig.Login_Method;
        userid = etUser.getText().toString();
        password = etPwd.getText().toString();

        if (TextUtils.isEmpty(url)) {
            Toasts.showShort(LoginActivity.this, "请扫描二维码");
            return;
        }
        if (TextUtils.isEmpty(userid)) {
            Toasts.showShort(LoginActivity.this, "请输入用户名");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toasts.showShort(LoginActivity.this, "请输入密码");
            return;
        }
        getContact();
    }

    /**
     * 获取数据
     */
    private void getContact() {
        LoadingDialog.showDialogForLoading(this);
        new NetUtil(getParams(), url, new OkHttpClient(), new ResponseListener() {
            @Override
            public void onSuccess(String string) {
                try {
                    initData(string);
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toasts.showLong(LoginActivity.this, "登录失败");
                            LoadingDialog.cancelDialogForLoading();
                        }
                    });
                }
            }

            @Override
            public void onFail(IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LoadingDialog.cancelDialogForLoading();
                        Toasts.showLong(LoginActivity.this, e.getMessage());
                    }
                });

            }
        });
    }

    /**
     * 设置参数
     *
     * @return
     */
    private List<NetParams> getParams() {
        List<NetParams> list = new ArrayList<>();
        list.add(new NetParams("userid", userid));
        list.add(new NetParams("password", password));
        return list;
    }

    JudgeDialog updateDialog;

    /**
     * 返回数据处理
     *
     * @param response
     */
    private void initData(String response) throws Exception {
        Log.e("data", response);
        Gson gson = new Gson();
        LoginBean model = gson.fromJson(response, LoginBean.class);
        if (model.isSuccess()) {
            if (model.getTables().getAPPInfo().size() > 0) {
                dowmloadUrl = model.getTables().getAPPInfo().get(0).getSAppApk();
                int readTimeout = model.getTables().getAPPInfo().get(0).getiTimeout();
                if (readTimeout > 0) {
                    BaseApplication.getInstance().setClinet(readTimeout);
                }
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LoadingDialog.cancelDialogForLoading();
//                    login(model);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (StringUtil.isNotEmpty(dowmloadUrl)) {
                                versionControl(model);
                            } else {
                                login(model);
                            }
                        }
                    });


//                    finish();
                }
            });
        } else
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, model.getMessage());
                    LoadingDialog.cancelDialogForLoading();
                    Toasts.showLong(LoginActivity.this, model.getMessage());
                }
            });

    }

    /*检测版本号*/
    private void versionControl(LoginBean model) {
        int versionCurrrent = model.getTables().getAPPInfo().get(0).getSAppVersion();
        try {
            if (versionCurrrent > versionSystem) {
                updateDialog = new JudgeDialog(LoginActivity.this, R.style.JudgeDialog, getString(R.string.update_latest), new JudgeDialog.OnCloseListener() {
                    @Override
                    public void onClick(boolean confirm) {
                        if (confirm) {
                            updatePermission();
                        } else {
                            login(model);
                        }
                    }
                });
                updateDialog.show();

            } else if (versionCurrrent < versionSystem) {
                updateDialog = new JudgeDialog(LoginActivity.this, R.style.JudgeDialog, getString(R.string.update_older), new JudgeDialog.OnCloseListener() {
                    @Override
                    public void onClick(boolean confirm) {
                        if (confirm) {
                            updatePermission();
                        } else {
                            login(model);
                        }
                    }
                });
                updateDialog.show();
            } else {
                login(model);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*登录*/
    private void login(LoginBean model) {
        String data = new Gson().toJson(model.getTables());
        String num = new Gson().toJson(model.getData());
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, MainActivity.class);
        intent.putExtra("data", data);
        Log.e("data", data);
        intent.putExtra("num", num);
        Log.e("num", num);
        preferencesHelper.put("userid", model.getTables().getPerson().get(0).getSCode());
        preferencesHelper.put("userName", model.getTables().getPerson().get(0).getSName());
        preferencesHelper.put("userDepartment", model.getTables().getPerson().get(0).getSClassName());
        preferencesHelper.put("password", etPwd.getText().toString());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        });

    }

    /**
     * 扫描权限申请和扫描逻辑处理
     *
     * @param intent
     */
    private void permission(Intent intent) {
        requestRunPermisssion(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, new PermissionListener() {
            @Override
            public void onGranted() {
                intent.setClass(LoginActivity.this, CaptureActivity.class);
                /* ZxingConfig是配置类
                 * 可以设置是否显示底部布局，闪光灯，相册，
                 * 是否播放提示音  震动
                 * 设置扫描框颜色等
                 * 也可以不传这个参数
                 * */
                ZxingConfig config = new ZxingConfig();
                // config.setPlayBeep(false);//是否播放扫描声音 默认为true
                // config.setShake(false);//是否震动  默认为true
                config.setDecodeBarCode(false);//是否扫描条形码 默认为true
                // config.setReactColor(R.color.colorAccent);//设置扫描框四个角的颜色 默认为白色
                // config.setFrameLineColor(R.color.colorAccent);//设置扫描框边框颜色 默认无色
                // config.setScanLineColor(R.color.colorAccent);//设置扫描线的颜色 默认白色
                config.setFullScreenScan(false);//是否全屏扫描  默认为true  设为false则只会在扫描框中扫描
                intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
                startActivityForResult(intent, REQUEST_CODE_SCAN);
            }

            @Override
            public void onDenied(List<String> deniedPermission) {
//                Toast.makeText(LoginActivity.this, "授权失败", Toast.LENGTH_LONG).show();
                Toasts.showShort(LoginActivity.this, "授权失败");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码返回值
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(Constant.CODED_CONTENT);
                try {

                    storageAddress(content);


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toasts.showShort(this, getString(R.string.error_json));
                }
                Log.e(TAG, content);
            } else {
                Toasts.showShort(this, getString(R.string.empty_data));
            }
        }
    }

    /*扫描数据处理*/
    private void storageAddress(String content) throws JSONException {
        JSONObject jsonObject = new JSONObject(content);
        String address = jsonObject.optString("ServerAddr");
        String addressImg = jsonObject.optString("ServerImageAddr");
        if (StringUtil.isNotEmpty(address)) {
            preferencesHelper.put("address", address);
            this.address = address;
        } else {
            Toasts.showShort(this, getString(R.string.login_address_empty));
        }

//        if (StringUtil.isNotEmpty(addressImg)) {
//            preferencesHelper.put("addressImg", address);
//        } else {
//            Toasts.showShort(this, getString(R.string.login_img_address_empty));
//        }
    }

    /*下载进度更新*/
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

//            System.out.println("-arg1--->" + msg.arg1);
//            System.out.println("-arg2--->" + msg.arg2);
//            System.out.println("-what--->" + msg.what);
//            System.out.println("-obj--->" + msg.obj);

            if (msg.what == 11) {
                progressDialog.setProgress((Integer) msg.obj);
            }
            if (msg.what == 0) {
                installApk(file);
            }
        }

        ;
    };

    /*显示下载进度*/
    private void showPrograss() {
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("正在下载");
        progressDialog.setMessage("请稍后...");
        progressDialog.setMax(100);
        progressDialog.show();
    }

    /*安装apk*/
    public void installApk(File apkPath) {
        //安装跳转
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            /* Android N 写法*/
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(LoginActivity.this, BuildConfig.APPLICATION_ID + ".fileProvider", apkPath);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkPath),
                    "application/vnd.android.package-archive");
        }
        try {
            startActivity(intent);

        } catch (ActivityNotFoundException exception) {
            Toast.makeText(LoginActivity.this, "no activity", Toast.LENGTH_SHORT).show();
            exception.printStackTrace();
        }

    }

    /*获取文件下载权限*/
    private void updatePermission() {
        requestRunPermisssion(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, new PermissionListener() {
            @Override
            public void onGranted() {
//                downFile("http://wap.apk.anzhi.com/data5/apk/201812/12/com.evernote_04286100.apk");
                downFile(address + dowmloadUrl);
            }

            @Override
            public void onDenied(List<String> deniedPermission) {
                Toasts.showShort(LoginActivity.this, "授权失败");
            }
        });
    }

    /*apk下载*/
    private void downFile(String url) {
        showPrograss();
        File dirfile = FileUtil.creatDir(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "zhongda");
        file = FileUtil.creatFile(dirfile, "huojia.apk");


        new NetUtil(url, Environment.getExternalStorageDirectory().getAbsolutePath(), "/zhongda/huojia.apk", new NetUtil.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(File file) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                //下载完成进行相关逻辑操作
                Message msg = mHandler.obtainMessage();
                msg.what = 0;
                mHandler.sendMessage(msg);

            }

            @Override
            public void onDownloading(int progress) {
//                        progressDialog.setProgress(progress);

                Message msg = mHandler.obtainMessage();
                msg.what = 11;
                msg.obj = progress;
                mHandler.sendMessage(msg);
            }

            @Override
            public void onDownloadFailed(Exception e) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                //下载异常进行相关提示操作
                Message msg = mHandler.obtainMessage();
                msg.what = 1;
                msg.obj = e;
                mHandler.sendMessage(msg);
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (updateDialog != null && updateDialog.isShowing()) {
            updateDialog.dismiss();
        }
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
