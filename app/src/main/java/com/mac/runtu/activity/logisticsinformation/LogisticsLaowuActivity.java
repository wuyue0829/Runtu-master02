package com.mac.runtu.activity.logisticsinformation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.mac.runtu.R;
import com.mac.runtu.activity.laborscrvice.LaborServiceCooperationActivity;

/**
 * 物流信息搜索结果列表页
 */
public class LogisticsLaowuActivity extends AppCompatActivity {

    private Button btn_start;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_laowu);

        btn_start = (Button) findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogisticsLaowuActivity.this,
                        LaborServiceCooperationActivity.class));
                finish();
            }
        });
    }
}
