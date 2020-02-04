package com.bsit.obu_issue;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    private LocationManager lm;
    @Override
    public void initView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    public void fillView() {
        super.fillView();
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

    }
    @OnClick({R.id.test,R.id.write_data})
    public void onViewClick(View view){
        switch (view.getId()){
            case R.id.test:
                if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),101);
                }else{
                    if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                        ActivityCompat. requestPermissions(this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                                100);
                    }else{
                        startActivity(new Intent(this,CdbListActivity.class));            }
                }
                break;
            case R.id.write_data:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    ZSCBleBusiness.getInstance().writeByteDate(ByteUtil.hexStr("00A40000023F0000"));
                }
                break;
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivity(new Intent(this,CdbListActivity.class));
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
