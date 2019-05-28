package demo.com.lgx.wechatfloatdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.IFloatWindow;
import com.yhao.floatwindow.MoveType;
import com.yhao.floatwindow.PermissionUtil;
import com.yhao.floatwindow.Screen;
import com.yhao.floatwindow.Util;
import com.yhao.floatwindow.ViewStateListener;

import demo.com.lgx.wechatfloatdemo.weghit.ScaleCircleImageView;

/**
 * Created by heng on 2019-05-27
 */
public class UtilManager implements ViewStateListener {

    private static String TAG = "aaaaaaaaaaaaaaa";

    private Context mApplication;
    private ScaleCircleImageView myImageView;
    private ViewGroup parent;
    private static UtilManager instance;


    public static UtilManager getInstance(Context application, int BG_BACKGROUND, int imageIcon, ViewGroup viewGroup) {
        if (instance == null) {
            synchronized (UtilManager.class) {
                if (instance == null) {
                    instance = new UtilManager(application, BG_BACKGROUND, imageIcon, viewGroup);
                }
            }
        }
        return instance;
    }

    public UtilManager(Context application, int BG_BACKGROUND, int imageIcon, ViewGroup viewGroup) {
        this.mApplication = application;
        this.parent = viewGroup;
        windowshow(BG_BACKGROUND, imageIcon);
    }


    public void update(int bg, int icon) {
        IFloatWindow old = FloatWindow.get("old");
        IFloatWindow cancel2 = FloatWindow.get("cancel2");
        IFloatWindow cancel = FloatWindow.get("cancel");
        ImageView imageView = new ImageView(mApplication);
        imageView.setImageResource(R.drawable.regulator_me_silence_icon);
        old.getmB().setView(imageView);


    }


    public void windowshow(int BG_BACKGROUND, int imageIcon) {
        myImageView = new ScaleCircleImageView(mApplication);

        if (PermissionUtil.hasPermission(mApplication)) {
            IFloatWindow old = FloatWindow.get("old");
            if (old == null) {
                IFloatWindow cancel2 = FloatWindow.get("cancel2");
                if (cancel2 == null) {
                    FloatWindow
                            .with(mApplication)
                            .setTag("cancel2")
                            .setView(BG_BACKGROUND)
                            .setCancelParam2(320)
                            .setMoveType(MoveType.inactive, 0, 0)
                            .setDesktopShow(false)
                            .build();
                }
                IFloatWindow cancel = FloatWindow.get("cancel");
                if (cancel == null) {
                    FloatWindow
                            .with(mApplication)
                            .setTag("cancel")
                            .setView(BG_BACKGROUND)
                            .setCancelParam2(300)
                            .setMoveType(MoveType.inactive, 0, 0)
                            .setDesktopShow(false)
                            .build();
                }


                ImageView imageView = new ImageView(mApplication);
                imageView.setBackgroundResource(imageIcon);
                FloatWindow
                        .with(mApplication)
                        .setTag("old")
                        .setView(imageView)
                        .setMoveType(MoveType.slide, 0, 0)
                        .setWidth(75)

//                        .setFilter(false, WebViewActivity.class)
                        .setHeight(75)
                        .setX(Screen.width, 0.8f)  //设置控件初始位置
                        .setY(parent.getHeight() / 3)
                        .setParentHeight(parent.getHeight())
                        .setMoveStyle(300, new AccelerateInterpolator())
                        .setViewStateListener(this)
                        .setDesktopShow(false)
                        .build();
                old = FloatWindow.get("old");
                startAnimation(old);
            } else {
                startAnimation(old);
            }
        } else {
            //没有浮窗权限
            startEmptyAnimation();

        }
    }

    public void diss() {
        if (instance != null) {

            FloatWindow.destroy("old");
            FloatWindow.destroy("cancel2");
            FloatWindow.destroy("cancel");

        }
    }

    private void startEmptyAnimation() {
//创建当前视图的bitmap
        View view = parent;
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        int mWidth = Util.dp2px(mApplication, 75);
        int xOffset = Util.getScreenWidth(mApplication) - mWidth;
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
                Log.i(TAG, "onAnimationEnd: ==========");
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
                Log.i(TAG, "onAnimationEnd: bbbbbbbbbbbb");
                onShow();

            }

        });

    }

    @Override
    public void onPositionUpdate(int x, int y) {
        Log.i(TAG, "onPositionUpdate: ");

    }

    @Override
    public void onShow() {
        Log.i(TAG, "onShow: ");
    }

    @Override
    public void onHide() {
        Log.i(TAG, "onHide: ");
    }

    @Override
    public void onDismiss() {
        if (instance != null)
            instance = null;
        Log.i(TAG, "onDismiss: ");
    }

    @Override
    public void onMoveAnimStart() {
        Log.i(TAG, "onMoveAnimStart: ");
    }

    @Override
    public void onMoveAnimEnd() {
        Log.i(TAG, "onMoveAnimEnd: ");
    }

    @Override
    public void onBackToDesktop() {
        Log.i(TAG, "onBackToDesktop: ");
    }

    @Override
    public void onCancelHide() {
        Log.i(TAG, "onCancelHide: ");
    }
}
