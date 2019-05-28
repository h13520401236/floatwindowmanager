package demo.com.lgx.wechatfloatdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends BaseActivity {

    private LinearLayout view;
    private UtilManager instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.button);

        view = findViewById(R.id.content);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SecondActivity.class));
//                instance = UtilManager.getInstance(MyApplication.getMyApplication(), R.layout.layout_window, R.drawable.image_cancel, view);

            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        instance.diss();

    }


}
