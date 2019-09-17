package com.hxgd.collection.view;

import android.content.Context;

import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

public class LoadingDialog {

    QMUITipDialog qmuiTipDialog;

    public LoadingDialog(Context context) {
        this(context,"加载中...");
    }

    public LoadingDialog(Context context,String message) {
        qmuiTipDialog = new QMUITipDialog.Builder(context)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(message)
                .create(false);
    }

    public void show(){
        if (qmuiTipDialog != null && !qmuiTipDialog.isShowing()){
            qmuiTipDialog.show();
        }
    }

    public void dismiss(){
        if (qmuiTipDialog != null & qmuiTipDialog.isShowing()){
            qmuiTipDialog.dismiss();
        }
    }

}