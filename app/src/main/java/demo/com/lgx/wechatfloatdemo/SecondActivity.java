package demo.com.lgx.wechatfloatdemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.IFloatWindow;
import com.yhao.floatwindow.MoveType;
import com.yhao.floatwindow.PermissionUtil;
import com.yhao.floatwindow.Screen;
import com.yhao.floatwindow.Util;

import demo.com.lgx.wechatfloatdemo.weghit.IPermissionListener;
import demo.com.lgx.wechatfloatdemo.weghit.IViewStateListener;
import demo.com.lgx.wechatfloatdemo.weghit.ScaleCircleImageView;
import demo.com.lgx.wechatfloatdemo.weghit.WindowManager;

/**
 * Created by Harry on 2018/8/8.
 * desc:
 */

public class SecondActivity extends BaseActivity {
String TAG = "SecondActivity";
    private ViewGroup parent;
    private ScaleCircleImageView myImageView;
    private WindowManager instance;
    //    private UtilManager instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Button button = findViewById(R.id.button);
        parent = findViewById(R.id.view);

//        myImageView = findViewById(R.id.image);
        myImageView = new ScaleCircleImageView(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(SecondActivity.this,WebViewActivity.class));
////                imageView.setTestNum();
//                if (instance == null)
//                    instance = UtilManager.getInstance(MyApplication.getMyApplication(), R.layout.layout_window, R.drawable.regulator_me_silence_icon, parent);


            }
        });

        instance = WindowManager.getInstance(parent, MyApplication.getMyApplication());
        instance.setiViewStateListener(new IViewStateListener() {
            @Override
            public void onShow() {

            }

            @Override
            public void onDismiss() {
                Log.i(TAG, "onDismiss: ");
            }

            @Override
            public void onCancelHide() {

            }

            @Override
            public void onBackToDesktop() {

            }

            @Override
            public void onHide() {

            }
        });

        instance.setiPermissionListener(new IPermissionListener() {
            @Override
            public void onSuccess() {
                Log.i(TAG, "onSuccess: ");

            }

            @Override
            public void onFail() {
                Log.i(TAG, "onFail: ");

            }

            @Override
            public void request() {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }
        });
    }

    private void a() {
        if (PermissionUtil.hasPermission(SecondActivity.this)) {
            IFloatWindow old = FloatWindow.get("old");
            if (old == null) {
                IFloatWindow cancel2 = FloatWindow.get("cancel2");
                if (cancel2 == null) {
                    FloatWindow
                            .with(getApplicationContext())
                            .setTag("cancel2")
                            .setView(R.layout.layout_window)
                            .setCancelParam2(320)
                            .setMoveType(MoveType.inactive, 0, 0)
                            .setDesktopShow(false)
                            .build();
                }
                IFloatWindow cancel = FloatWindow.get("cancel");
                if (cancel == null) {
                    FloatWindow
                            .with(getApplicationContext())
                            .setTag("cancel")
                            .setView(R.layout.layout_window)
                            .setCancelParam2(300)
                            .setMoveType(MoveType.inactive, 0, 0)
                            .setDesktopShow(false)
                            .build();
                }


                ImageView imageView = new ImageView(SecondActivity.this);
                imageView.setBackgroundResource(R.mipmap.ic_launcher_round);
                FloatWindow
                        .with(getApplicationContext())
                        .setTag("old")
                        .setView(imageView)
                        .setMoveType(MoveType.slide, 0, 0)
                        .setWidth(75)
//                                .setFilter(false, WebViewActivity.class)
                        .setHeight(75)
                        .setX(Screen.width, 0.8f)  //设置控件初始位置
                        .setY(parent.getHeight() / 3)
                        .setParentHeight(parent.getHeight())
                        .setMoveStyle(300, new AccelerateInterpolator())
                        .setDesktopShow(false)
                        .build();
                old = FloatWindow.get("old");
//                        imageView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                startActivity(new Intent(getApplicationContext(), WebViewActivity.class));
//                            }
//                        });
                startAnimation(old);
            } else {
                startAnimation(old);
            }
        } else {
            //没有浮窗权限
            startEmptyAnimation();

        }
    }

    private void startEmptyAnimation() {
        //创建当前视图的bitmap
        View view = parent;
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        int mWidth = Util.dp2px(this, 75);
        int xOffset = Util.getScreenWidth(this) - mWidth;
        int yOffset = parent.getHeight() / 3;
        myImageView
                .createAnmiationParam()
                .setFromLeftX(0)
                .setToLeftX(xOffset)
                .setFromRightX(parent.getWidth())
                .setToRightX(xOffset + mWidth)
                .setFromTopY(0)
                .setToTopY(yOffset)
                .setFromBottomY(parent.getHeight())
                .setFromRadius(0)
                .setToRadius(mWidth / 2)
                .setToBottomY(yOffset + mWidth);
        myImageView.startAnimation(bitmap, mWidth);
        myImageView.setScaleCircleListener(new ScaleCircleImageView.ScaleCircleListener() {
            @Override
            public void onAnimationEnd() {
                MyApplication.getMyApplication().setBackNoPermission(true);
//                finish();
                Log.i("ta", "onAnimationEnd: startEmptyAnimation");
            }

        });
    }

    private void startAnimation(IFloatWindow old) {


        //创建当前视图的bitmap
        View view = parent;
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        myImageView
                .createAnmiationParam()
                .setFromLeftX(0)
                .setToLeftX(old.getmB().xOffset)
                .setFromRightX(parent.getWidth())
                .setToRightX(old.getmB().xOffset + old.getmB().mWidth)
                .setFromTopY(0)
                .setToTopY(old.getmB().yOffset)
                .setFromBottomY(parent.getHeight())
                .setFromRadius(0)
                .setToRadius(old.getmB().mWidth / 2)
                .setToBottomY(old.getmB().yOffset + old.getmB().mWidth);
        myImageView.startAnimation(bitmap, old.getmB().mWidth);
        myImageView.setScaleCircleListener(new ScaleCircleImageView.ScaleCircleListener() {
            @Override
            public void onAnimationEnd() {
                Log.i("ta", "onAnimationEnd: startAnimation");
//                finish();
            }

        });

    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    public void click(View view) {

        Log.i("2323232", "click: 2222222");
        if (instance != null) {
            instance.updateUiManager(R.layout.layout_window, R.drawable.image_cancel);


        }
//        if (instance != null)
//            instance.update(1, 1);


    }

    public void click1(View view) {
        if (instance != null) {
            instance.updateUiManager(R.layout.layout_window1, R.drawable.regulator_me_silence_icon);


        }
    }
}
