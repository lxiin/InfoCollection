package com.hxgd.collection.user;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hxgd.collection.R;

import java.util.concurrent.Executor;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xzh.com.addresswheel_master.model.AddressDtailsEntity;
import xzh.com.addresswheel_master.model.AddressModel;
import xzh.com.addresswheel_master.utils.JsonUtil;
import xzh.com.addresswheel_master.utils.Utils;
import xzh.com.addresswheel_master.view.ChooseAddressWheel;
import xzh.com.addresswheel_master.view.listener.OnAddressChangeListener;

public class TestCityAct extends AppCompatActivity {

    @BindView(R.id.btn_login)
    Button btnLogin;

    @BindView(R.id.et_phone)
    EditText etPhone;


    private ChooseAddressWheel chooseAddressWheel = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        init();

    }

    private void init() {

        initWheel();
        initData();


    }

    private void initWheel() {
        chooseAddressWheel = new ChooseAddressWheel(this);
        chooseAddressWheel.setOnAddressChangeListener(new OnAddressChangeListener() {
            @Override
            public void onAddressChange(String province, String city, String district) {
                etPhone.setText(province + " " + city + " " + district);

            }
        });
    }

    private void initData() {
        String address = Utils.readAssert(this, "address.txt");
        AddressModel model = JsonUtil.parseJson(address, AddressModel.class);
        if (model != null) {
            AddressDtailsEntity data = model.Result;
            if (data == null) return;
            etPhone.setText(data.Province + " " + data.City + " " + data.Area);
            if (data.ProvinceItems != null && data.ProvinceItems.Province != null) {
                chooseAddressWheel.setProvince(data.ProvinceItems.Province);
                chooseAddressWheel.defaultValue(data.Province, data.City, data.Area);
            }
        }
    }


    @OnClick(R.id.btn_login)
    public void onBtnLoginClick(View view){
        Utils.hideKeyBoard(this);
        chooseAddressWheel.show(view);
//        Toast.makeText(this, "sadasdasd", Toast.LENGTH_SHORT).show();
    }
}
