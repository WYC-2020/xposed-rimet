/*
 * Copyright (c) 2019 The sky Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tk.anysoft.xposed.lark.ui.dialog;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import tk.anysoft.xposed.lark.ui.activity.ManagerActivity;
import com.sky.xposed.common.ui.util.ViewUtil;
import com.sky.xposed.common.ui.view.CommonFrameLayout;
import com.sky.xposed.common.ui.view.EditTextItemView;
import com.sky.xposed.common.ui.view.SimpleItemView;
import com.sky.xposed.common.ui.view.SwitchItemView;
import com.sky.xposed.common.util.DisplayUtil;
import tk.anysoft.xposed.lark.BuildConfig;
import tk.anysoft.xposed.lark.Constant;
import tk.anysoft.xposed.lark.contract.RimetContract;
import tk.anysoft.xposed.lark.data.model.UpdateModel;
import tk.anysoft.xposed.lark.plugin.interfaces.XConfig;
import tk.anysoft.xposed.lark.plugin.interfaces.XPlugin;
import tk.anysoft.xposed.lark.ui.util.ActivityUtil;
import tk.anysoft.xposed.lark.ui.util.DialogUtil;

/**
 * 设置界面 弹出菜单
 *  针对飞书因为未找到fragment 注入菜单方式，暂时使用进入设置界面直接弹出
 * Created by sky on 2019/3/13.
 */
public class SettingsDialog extends CommonDialog implements RimetContract.View {

    private TextView tvPrompt;
    private SwitchItemView sivLuckyEnable;
    private EditTextItemView sivLuckyDelayed;
    private SwitchItemView sivFastLuckyEnable;
    private SwitchItemView sivRecallEnable;
    private SwitchItemView sivLocationEnable;
    private SwitchItemView sivWifiEnable;
    private SwitchItemView sivWifiCurrentEnable;
    private SwitchItemView sivBaseStationEnable;
    private SimpleItemView sivSettingsLocation;//位置信息
    private SimpleItemView sivSettingsWIFI;//WIFI信息
    private SimpleItemView sivSettingsBaseStation;//基站信息
    private SimpleItemView sivLove;
    private SimpleItemView sivAbout;

    @Override
    public void createView(CommonFrameLayout frameView) {

        int left = DisplayUtil.dip2px(getContext(), 15);
        int top = DisplayUtil.dip2px(getContext(), 12);

        tvPrompt = new TextView(getContext());
        tvPrompt.setTextSize(14);
        tvPrompt.setBackgroundColor(Color.GRAY);
        tvPrompt.setTextColor(Color.WHITE);
        tvPrompt.setPadding(left, top, left, top);

        sivLuckyEnable = ViewUtil.newSwitchItemView(getContext(), "自动接收红包");
        sivLuckyEnable.setDesc("开启时自动接收红包");

        sivLuckyDelayed = new EditTextItemView(getContext());
        sivLuckyDelayed.setInputType(com.sky.xposed.common.Constant.InputType.NUMBER_SIGNED);
        sivLuckyDelayed.setMaxLength(2);
        sivLuckyDelayed.setUnit("秒");
        sivLuckyDelayed.setName("红包延迟时间");
        sivLuckyDelayed.setExtendHint("单位(秒)");

        sivFastLuckyEnable = ViewUtil.newSwitchItemView(getContext(), "快速打开红包");
        sivFastLuckyEnable.setDesc("开启时快速打开红包");

        sivRecallEnable = ViewUtil.newSwitchItemView(getContext(), "消息防撤回");
        sivRecallEnable.setDesc("开启时消息不会被撤回");

        sivLocationEnable = ViewUtil.newSwitchItemView(getContext(), "虚拟定位");
        sivLocationEnable.setDesc("开启时会修改当前位置信息");

        sivSettingsLocation = ViewUtil.newSimpleItemView(getContext(), "位置信息");
        sivSettingsLocation.setExtendHint("设置位置信息");

        sivWifiEnable = ViewUtil.newSwitchItemView(getContext(), "虚拟附近WIFI");
        sivWifiEnable.setDesc("开启时会修改附近WIFI列表");

        sivWifiCurrentEnable = ViewUtil.newSwitchItemView(getContext(), "虚拟已连接WIFI");
        sivWifiCurrentEnable.setDesc("开启时会修改当前已连接WIFI信息");

        sivSettingsWIFI = ViewUtil.newSimpleItemView(getContext(), "WIFI信息");
        sivSettingsWIFI.setExtendHint("设置WIFI信息");

        sivBaseStationEnable = ViewUtil.newSwitchItemView(getContext(), "虚拟基站");
        sivBaseStationEnable.setDesc("开启时会修改当前基站信息");

        sivSettingsBaseStation = ViewUtil.newSimpleItemView(getContext(), "基站信息");
        sivSettingsBaseStation.setExtendHint("设置基站信息");



        sivLove = ViewUtil.newSimpleItemView(getContext(), "爱心公益");
        sivAbout = ViewUtil.newSimpleItemView(getContext(), "关于");

        //添加到页面
        frameView.addContent(tvPrompt);
//        frameView.addContent(sivLuckyEnable);//红包开关
//        frameView.addContent(sivLuckyDelayed);//红包延迟
//        frameView.addContent(sivFastLuckyEnable);//快速抢红包
//        frameView.addContent(sivRecallEnable);//防止撤回

        frameView.addContent(sivLocationEnable);//开启虚拟定位
        frameView.addContent(sivSettingsLocation);//虚拟定位设置

        frameView.addContent(sivWifiEnable);//开启虚拟WIFI
        frameView.addContent(sivWifiCurrentEnable);//开启虚拟WIFI
        frameView.addContent(sivSettingsWIFI);//虚拟WIFI设置

        frameView.addContent(sivBaseStationEnable);//开启虚拟基站
        frameView.addContent(sivSettingsBaseStation);//虚拟基站设置

//        frameView.addContent(sivLove);//爱心公益
        frameView.addContent(sivAbout);//关于
    }

    @Override
    protected void initView(View view, Bundle args) {
        super.initView(view, args);

        // 创建对象
//        mRimetPresenter = new RimetPresenter(getPluginManager(), this);

        setTitle(Constant.Name.TITLE);

        SimpleItemView simpleItemViews[] = {sivSettingsLocation,sivSettingsWIFI,sivSettingsBaseStation};

        for (int i = 0; i < simpleItemViews.length; i++) {
            TextView tvExt = simpleItemViews[i].getExtendView();
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) tvExt.getLayoutParams();
            params.leftMargin = DisplayUtil.dip2px(getContext(), 100);
            tvExt.setMaxLines(2);
            tvExt.setEllipsize(TextUtils.TruncateAt.END);
            tvExt.setTextSize(12);
        }


        SharedPreferences preferences = getDefaultSharedPreferences();
        XPlugin xPlugin = getPluginManager().getXPluginById(Constant.Plugin.DING_DING);

        sivLuckyEnable.bind(getDefaultSharedPreferences(),
                Integer.toString(Constant.XFlag.ENABLE_LUCKY), false,
                (view1, key, value) -> {
                    xPlugin.setEnable(Constant.XFlag.ENABLE_LUCKY, value);
                    return true;
                });

        sivLuckyDelayed.bind(getDefaultSharedPreferences(),
                Integer.toString(Constant.XFlag.LUCKY_DELAYED), "",
                (view12, key, value) -> true);

        sivFastLuckyEnable.bind(getDefaultSharedPreferences(),
                Integer.toString(Constant.XFlag.ENABLE_FAST_LUCKY), false,
                (view1, key, value) -> {
                    xPlugin.setEnable(Constant.XFlag.ENABLE_FAST_LUCKY, value);
                    return true;
                });

        sivRecallEnable.bind(getDefaultSharedPreferences(),
                Integer.toString(Constant.XFlag.ENABLE_RECALL), true,
                (view1, key, value) -> {
                    xPlugin.setEnable(Constant.XFlag.ENABLE_RECALL, value);
                    return true;
                });

        sivLocationEnable.bind(getDefaultSharedPreferences(),
                Integer.toString(Constant.XFlag.ENABLE_LOCATION), false,
                (view1, key, value) -> {
                    xPlugin.setEnable(Constant.XFlag.ENABLE_LOCATION, value);
                    return true;
                });

        sivWifiEnable.bind(getDefaultSharedPreferences(),
                Integer.toString(Constant.XFlag.ENABLE_WIFI), false,
                (view1, key, value) -> {
                    xPlugin.setEnable(Constant.XFlag.ENABLE_WIFI, value);
                    return true;
                });

        sivWifiCurrentEnable.bind(getDefaultSharedPreferences(),
                Integer.toString(Constant.XFlag.ENABLE_WIFI_CURRENT), false,
                (view1, key, value) -> {
                    xPlugin.setEnable(Constant.XFlag.ENABLE_WIFI_CURRENT, value);
                    return true;
                });

        sivBaseStationEnable.bind(getDefaultSharedPreferences(),
                Integer.toString(Constant.XFlag.ENABLE_BASESTATION), false,
                (view1, key, value) -> {
                    xPlugin.setEnable(Constant.XFlag.ENABLE_BASESTATION, value);
                    return true;
                });

        // 设置初始信息
        sivSettingsLocation.setExtend(preferences.getString(
                Integer.toString(Constant.XFlag.LOCATIONNAME), ""));
        sivSettingsLocation.setOnClickListener(v -> {
            // 跳转到地图界面
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.putExtra("flag", Constant.XFlag.ENABLE_LOCATION);
            intent.setClassName(BuildConfig.APPLICATION_ID, ManagerActivity.class.getName());
            startActivityForResult(intent, 99);
        });

        sivSettingsWIFI.setExtend(preferences.getString(
                Integer.toString(Constant.XFlag.WIFINAME), ""));
        sivSettingsWIFI.setOnClickListener(v -> {
            // 跳转到地图界面
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.putExtra("flag", Constant.XFlag.ENABLE_WIFI);
            intent.setClassName(BuildConfig.APPLICATION_ID, ManagerActivity.class.getName());
            startActivityForResult(intent, 99);
        });

        sivSettingsBaseStation.setExtend(preferences.getString(
                Integer.toString(Constant.XFlag.BASESTATIONNAME), ""));
        sivSettingsBaseStation.setOnClickListener(v -> {
            // 跳转到地图界面
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.putExtra("flag", Constant.XFlag.ENABLE_BASESTATION);
            intent.setClassName(BuildConfig.APPLICATION_ID, ManagerActivity.class.getName());
            startActivityForResult(intent, 99);
        });


        sivLove.setOnClickListener(v -> {
            // 打开捐赠界面
            LoveDialog loveDialog = new LoveDialog();
            loveDialog.show(getFragmentManager(), "love");
        });

        sivAbout.setOnClickListener(v -> {
            // 打开关于界面
            DialogUtil.showAboutDialog(getContext());
        });

        // 是否支持版本
        XConfig xConfig = getPluginManager().getVersionManager().getSupportConfig();
        setPromptText(xConfig != null ? "" : "");//不支持当前版本!
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 99 && resultCode == Activity.RESULT_OK) {
            int flag = data.getIntExtra("flag",0);
            if (Constant.XFlag.ENABLE_LOCATION == flag){
                // 保存当前模拟信息
                saveLocationInfo(
                        data.getStringExtra("title"),
                        data.getStringExtra("data"),
                        sivSettingsLocation,
                        Constant.XFlag.LOCATIONNAME,
                        Constant.XFlag.LOCATIONDATA);
            }else if (Constant.XFlag.ENABLE_WIFI == flag){
                saveLocationInfo(
                        data.getStringExtra("title"),
                        data.getStringExtra("data"),
                        sivSettingsWIFI,
                        Constant.XFlag.WIFINAME,
                        Constant.XFlag.WIFIDATA);
            }else if (Constant.XFlag.ENABLE_BASESTATION == flag){
                saveLocationInfo(
                        data.getStringExtra("title"),
                        data.getStringExtra("data"),
                        sivSettingsBaseStation,
                        Constant.XFlag.BASESTATIONNAME,
                        Constant.XFlag.BASESTATIONDATA);
            }
        }
    }

    @Override
    public void onUpdate(UpdateModel model) {
        // 提示用户更新
        DialogUtil.showDialog(getContext(),
                "提示", "发现新的版本,是否更新? \n" + model.getDesc(),
                "更新", (dialog, which) -> {
                    // 跳转到浏览器安装
                    ActivityUtil.openUrl(getContext(), model.getUrl());
                },
                !model.isForce() ? "取消" : "", null, !model.isForce());
    }

    @Override
    public void onUpdateFailed(String msg) {
        showMessage(msg);
    }

    @Override
    public void onUpdateConfigSucceed() {
        setPromptText("配置更新成功,需要重启钉钉即可生效!");
    }

    @Override
    public void onUpdateConfigFailed(String msg) {
        showMessage(msg);
    }

    @Override
    public void onClearConfigSucceed() {
        showMessage("清除配置成功!");
    }

    @Override
    public void onClearConfigFailed(String msg) {
        showMessage(msg);
    }

    /**
     * 设置提示消息
     * @param text
     */
    private void setPromptText(String text) {
        tvPrompt.setText(text);
        ViewUtil.setVisibility(tvPrompt, TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
    }

    /**
     * 保存当前模拟信息
     * @param title
     * @param data
     */
    private void saveLocationInfo(String title, String data,SimpleItemView simpleItemView,int titleId,int dataId) {
        getDefaultSharedPreferences()
                .edit()
                .putString(Integer.toString(titleId), title)
                .putString(Integer.toString(dataId), data)
                .apply();

        // 设置UI信息
        simpleItemView.setExtend(title);
    }
}
