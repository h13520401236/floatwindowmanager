package demo.com.lgx.wechatfloatdemo.weghit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.IFloatWindow;
import com.yhao.floatwindow.MoveType;
import com.yhao.floatwindow.PermissionListener;
import com.yhao.floatwindow.PermissionUtil;
import com.yhao.floatwindow.Screen;
import com.yhao.floatwindow.Util;
import com.yhao.floatwindow.ViewStateListener;

import demo.com.lgx.wechatfloatdemo.MyApplication;

/**
 * Created by heng on 2019-05-28
 */
public class WindowManager implements ViewStateListener, PermissionListener {
    private String TAG = "WindowManager";
    private static WindowManager mWindowManager;

    private Context appContext;

    private ViewGroup mParent;

    private ScaleCircleImageView myImageView;


    public static WindowManager getInstance(ViewGroup parent, Context application) {
        if (mWindowManager == null) {
            synchronized (WindowManager.class) {
                if (mWindowManager == null) {
                    mWindowManager = new WindowManager(parent, application);
                }
            }
        }
        return mWindowManager;
    }


    private WindowManager(ViewGroup parent, Context application) {
        this.appContext = application;
        this.mParent = parent;

    }

    private void remove() {
        FloatWindow.destroy("old");
        FloatWindow.destroy("cancel2");
        FloatWindow.destroy("cancel");
    }


    public void updateUiManager(int cancleLayoutResId, int btnResId) {
        remove();
        updateShowWindow(cancleLayoutResId, btnResId);
    }


    public IFloatWindow getIFloatWindow(String tag) {
        return FloatWindow.get(tag);
    }


    private void updateShowWindow(int cancleLayoutResId, int btnResId) {

        myImageView = new ScaleCircleImageView(appContext);
        if (PermissionUtil.hasPermission(appContext)) {
            IFloatWindow old = FloatWindow.get("old");
            if (old == null) {
                IFloatWindow cancel2 = FloatWindow.get("cancel2");
                if (cancel2 == null) {
                    FloatWindow
                            .with(appContext)
                            .setTag("cancel2")
                            .setView(cancleLayoutResId)
                            .setCancelParam2(320)
                            .setMoveType(MoveType.inactive, 0, 0)
                            .setDesktopShow(false)
                            .build();
                }
                IFloatWindow cancel = FloatWindow.get("cancel");
                if (cancel == null) {
                    FloatWindow
                            .with(appContext)
                            .setTag("cancel")
                            .setView(cancleLayoutResId)
                            .setCancelParam2(300)
                            .setMoveType(MoveType.inactive, 0, 0)
                            .setDesktopShow(false)
                            .build();
                }


                ImageView imageView = new ImageView(appContext);
                imageView.setBackgroundResource(btnResId);
                FloatWindow
                        .with(appContext)
                        .setTag("old")
                        .setView(imageView)
                        .setMoveType(MoveType.slide, 0, 0)
                        .setWidth(75)

//                        .setFilter(false, WebViewActivity.class)
                        .setHeight(75)
                        .setX(Screen.width, 0.8f)  //设置控件初始位置
                        .setY(mParent.getHeight() / 3)
                        .setParentHeight(mParent.getHeight())
                        .setMoveStyle(300, new AccelerateInterpolator())
                        .setViewStateListener(this)
                        .setPermissionListener(this)
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

    private void startEmptyAnimation() {
//创建当前视图的bitmap
        View view = mParent;
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        int mWidth = Util.dp2px(appContext, 75);
        int xOffset = Util.getScreenWidth(appContext) - mWidth;
        int yOffset = mParent.getHeight() / 3;
        myImageView
                .createAnmiationParam()
                .setFromLeftX(0)
                .setToLeftX(xOffset)
                .setFromRightX(mParent.getWidth())
                .setToRightX(xOffset + mWidth)
                .setFromTopY(0)
                .setToTopY(yOffset)
                .setFromBottomY(mParent.getHeight())
                .setFromRadius(0)
                .setToRadius(mWidth / 2)
                .setToBottomY(yOffset + mWidth);
        myImageView.startAnimation(bitmap, mWidth);
        myImageView.setScaleCircleListener(new ScaleCircleImageView.ScaleCircleListener() {
            @Override
            public void onAnimationEnd() {
                MyApplication.getMyApplication().setBackNoPermission(true);
            }

        });


        if (iPermissionListener != null) {
            iPermissionListener.request();
        }



//        if (MyApplication.getMyApplication().isBackNoPermission()) {
//            MyApplication.getMyApplication().setBackNoPermission(false);
//            new AlertDialog.Builder(appContext).setTitle("提示,你没有开启浮窗权限")
//                    .setPositiveButton("开启", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
//                            intent.setData(Uri.parse("package:" + appContext.getPackageName()));
//                            appContext.startActivity(intent);
//                        }
//                    }).setNegativeButton("取消", null).show();
//
//        }
    }

    private void startAnimation(IFloatWindow old) {
//创建当前视图的bitmap
        View view = mParent;
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        myImageView
                .createAnmiationParam()
                .setFromLeftX(0)
                .setToLeftX(old.getmB().xOffset)
                .setFromRightX(mParent.getWidth())
                .setToRightX(old.getmB().xOffset + old.getmB().mWidth)
                .setFromTopY(0)
                .setToTopY(old.getmB().yOffset)
                .setFromBottomY(mParent.getHeight())
                .setFromRadius(0)
                .setToRadius(old.getmB().mWidth / 2)
                .setToBottomY(old.getmB().yOffset + old.getmB().mWidth);
        myImageView.startAnimation(bitmap, old.getmB().mWidth);
        myImageView.setScaleCircleListener(new ScaleCircleImageView.ScaleCircleListener() {
            @Override
            public void onAnimationEnd() {
                onShow();

            }

        });
    }

    @Override
    public void onPositionUpdate(int x, int y) {

    }

    @Override
    public void onShow() {
        if (iViewStateListener != null) {
            iViewStateListener.onShow();
        }

        Log.i(TAG, "onShow: ");


    }

    @Override
    public void onHide() {
        Log.i(TAG, "onHide: ");
        if (iViewStateListener != null) {
            iViewStateListener.onHide();
        }


    }

    @Override
    public void onDismiss() {

        Log.i(TAG, "onDismiss: ");
        if (iViewStateListener != null) {
            iViewStateListener.onDismiss();
        }

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
        if (iViewStateListener != null) {
            iViewStateListener.onBackToDesktop();
        }

        Log.i(TAG, "onBackToDesktop: ");

    }

    @Override
    public void onCancelHide() {
        Log.i(TAG, "onCancelHide: ");
        if (iViewStateListener != null) {
            iViewStateListener.onCancelHide();
        }

    }


    @Override
    public void onSuccess() {
        Log.i(TAG, "onSuccess: ");
        if (iPermissionListener != null) {
            iPermissionListener.onSuccess();
        }
    }

    @Override
    public void onFail() {
        Log.i(TAG, "onFail: ");
        if (iPermissionListener != null) {
            iPermissionListener.onFail();
        }
    }

    private IViewStateListener iViewStateListener;

    //监听btn状态
    public void setiViewStateListener(IViewStateListener iViewStateListener) {
        this.iViewStateListener = iViewStateListener;
    }

    //监听权限
    private IPermissionListener iPermissionListener;

    public void setiPermissionListener(IPermissionListener iPermissionListener) {
        this.iPermissionListener = iPermissionListener;
    }

    public void removeWindowManager() {
        if (mWindowManager != null) {
            remove();
            mWindowManager = null;
        }
    }
}
