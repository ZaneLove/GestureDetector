package com.zanelove.gesturedetectordemo;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.*;
import android.view.GestureDetector.OnGestureListener;
import android.widget.*;
import com.zanelove.gesturedetectordemo.utils.DateTools;
import com.zanelove.gesturedetectordemo.utils.DensityUtil;
import com.zanelove.gesturedetectordemo.views.MyVideoView;

public class MainActivity extends FragmentActivity implements OnGestureListener,View.OnTouchListener {

    /** 视频窗口的宽和高 */
    private int playerWidth, playerHeight;
    /** 视频播放时间,视频播放的总时长 */
    private int playingTime, videoTotalTime;

    /** 视频播放控件 */
    private MyVideoView tv_pro_play;
    /** 控制视频播放的全部控件的父控件 */
    private LinearLayout ll_player_controller;
    /** 播放按钮,全屏按钮 */
    private ImageView iv_play_pause, iv_full_screen;
    /** 播放的时间,全部时间 */
    private TextView tv_playing_time, tv_total_time;

    /** 播放进度 */
    private SeekBar sb_video_progress;

    /** 手势改变视频进度,音量,亮度 */
    private RelativeLayout root_layout;// 根布局
    private RelativeLayout gesture_volume_layout, gesture_bright_layout;// 音量控制布局,亮度控制布局
    private TextView geture_tv_volume_percentage, geture_tv_bright_percentage;// 音量百分比,亮度百分比
    private ImageView gesture_iv_player_volume, gesture_iv_player_bright;// 音量图标,亮度图标
    private RelativeLayout gesture_progress_layout;// 进度图标
    private TextView geture_tv_progress_time;// 播放时间进度
    private ImageView gesture_iv_progress;// 快进或快退标志
    private GestureDetector gestureDetector;
    private AudioManager audiomanager;
    private int maxVolume, currentVolume;
    private float mBrightness = -1f; // 亮度
    private static final float STEP_PROGRESS = 2f;// 设定进度滑动时的步长，避免每次滑动都改变，导致改变过快
    private static final float STEP_VOLUME = 2f;// 协调音量滑动时的步长，避免每次滑动都改变，导致改变过快
    private boolean firstScroll = false;// 每次触摸屏幕后，第一次scroll的标志
    private int GESTURE_FLAG = 0;// 1,调节进度，2，调节音量,3.调节亮度
    private static final int GESTURE_MODIFY_PROGRESS = 1;
    private static final int GESTURE_MODIFY_VOLUME = 2;
    private static final int GESTURE_MODIFY_BRIGHT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acti_video_play);

        initView();
    }

    private void initView() {
        // 视频播放控件
        tv_pro_play = (MyVideoView) findViewById(R.id.tv_pro_play);

        /*
        iv_full_screen = (ImageView) findViewById(R.id.iv_full_screen);
        iv_play_pause = (ImageView) findViewById(R.id.iv_play_pause);

        ll_player_controller = (LinearLayout) findViewById(R.id.ll_player_controller);

        iv_play_pause = (ImageView) findViewById(R.id.iv_play_pause);
        sb_video_progress = (SeekBar) findViewById(R.id.sb_video_progress);
        iv_full_screen = (ImageView) findViewById(R.id.iv_full_screen);
        tv_playing_time = (TextView) findViewById(R.id.tv_playing_time);
        tv_total_time = (TextView) findViewById(R.id.tv_total_time);*/

    // ****************音量/进度/亮度*********************
        root_layout = (RelativeLayout) findViewById(R.id.root_layout);
        gesture_volume_layout = (RelativeLayout) findViewById(R.id.gesture_volume_layout);
        gesture_bright_layout = (RelativeLayout) findViewById(R.id.gesture_bright_layout);
        gesture_progress_layout = (RelativeLayout) findViewById(R.id.gesture_progress_layout);
        geture_tv_progress_time = (TextView) findViewById(R.id.geture_tv_progress_time);
        geture_tv_volume_percentage = (TextView) findViewById(R.id.geture_tv_volume_percentage);
        geture_tv_bright_percentage = (TextView) findViewById(R.id.geture_tv_bright_percentage);
        gesture_iv_progress = (ImageView) findViewById(R.id.gesture_iv_progress);
        gesture_iv_player_volume = (ImageView) findViewById(R.id.gesture_iv_player_volume);
        gesture_iv_player_bright = (ImageView) findViewById(R.id.gesture_iv_player_bright);
        gestureDetector = new GestureDetector(this, this);
        root_layout.setLongClickable(true);
        gestureDetector.setIsLongpressEnabled(true);
        root_layout.setOnTouchListener(this);
        audiomanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        maxVolume = audiomanager.getStreamMaxVolume(AudioManager.STREAM_MUSIC); // 获取系统最大音量
        currentVolume = audiomanager.getStreamVolume(AudioManager.STREAM_MUSIC); // 获取当前值

        /** 获取视频播放窗口的尺寸 */
        ViewTreeObserver viewObserver = root_layout.getViewTreeObserver();
        viewObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                root_layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                playerWidth = root_layout.getWidth();
                playerHeight = root_layout.getHeight();
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // 手势里除了singleTapUp，没有其他检测up的方法
        if (event.getAction() == MotionEvent.ACTION_UP) {
            GESTURE_FLAG = 0;// 手指离开屏幕后，重置调节音量或进度的标志
            gesture_volume_layout.setVisibility(View.GONE);
            gesture_bright_layout.setVisibility(View.GONE);
            gesture_progress_layout.setVisibility(View.GONE);
        }
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        firstScroll = true;// 设定是触摸屏幕后第一次scroll的标志
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        float mOldX = e1.getX(), mOldY = e1.getY();
        int y = (int) e2.getRawY();
        if (firstScroll) {// 以触摸屏幕后第一次滑动为标准，避免在屏幕上操作切换混乱
            // 横向的距离变化大则调整进度，纵向的变化大则调整音量
            if (Math.abs(distanceX) >= Math.abs(distanceY)) {
                gesture_progress_layout.setVisibility(View.VISIBLE);
                gesture_volume_layout.setVisibility(View.GONE);
                gesture_bright_layout.setVisibility(View.GONE);
                GESTURE_FLAG = GESTURE_MODIFY_PROGRESS;
            } else {
                if (mOldX > playerWidth * 3.0 / 5) {// 音量
                    gesture_volume_layout.setVisibility(View.VISIBLE);
                    gesture_bright_layout.setVisibility(View.GONE);
                    gesture_progress_layout.setVisibility(View.GONE);
                    GESTURE_FLAG = GESTURE_MODIFY_VOLUME;
                } else if (mOldX < playerWidth * 2.0 / 5) {// 亮度
                    gesture_bright_layout.setVisibility(View.VISIBLE);
                    gesture_volume_layout.setVisibility(View.GONE);
                    gesture_progress_layout.setVisibility(View.GONE);
                    GESTURE_FLAG = GESTURE_MODIFY_BRIGHT;
                }
            }
        }
        // 如果每次触摸屏幕后第一次scroll是调节进度，那之后的scroll事件都处理音量进度，直到离开屏幕执行下一次操作
        if (GESTURE_FLAG == GESTURE_MODIFY_PROGRESS) {
            // distanceX=lastScrollPositionX-currentScrollPositionX，因此为正时是快进
            if (Math.abs(distanceX) > Math.abs(distanceY)) {// 横向移动大于纵向移动
                if (distanceX >= DensityUtil.dip2px(this, STEP_PROGRESS)) {// 快退，用步长控制改变速度，可微调
                    gesture_iv_progress.setImageResource(R.drawable.souhu_player_backward);
                    if (playingTime > 3) {// 避免为负
                        playingTime -= 3;// scroll方法执行一次快退3秒
                    } else {
                        playingTime = 0;
                    }
                } else if (distanceX <= -DensityUtil.dip2px(this, STEP_PROGRESS)) {// 快进
                    gesture_iv_progress.setImageResource(R.drawable.souhu_player_forward);
                    if (playingTime < videoTotalTime - 16) {// 避免超过总时长
                        playingTime += 3;// scroll执行一次快进3秒
                    } else {
                        playingTime = videoTotalTime - 10;
                    }
                }
                if (playingTime < 0) {
                    playingTime = 0;
                }
                tv_pro_play.seekTo(playingTime);
                geture_tv_progress_time.setText(DateTools.getTimeStr(playingTime) + "/" + DateTools.getTimeStr(videoTotalTime));
            }
        }
        // 如果每次触摸屏幕后第一次scroll是调节音量，那之后的scroll事件都处理音量调节，直到离开屏幕执行下一次操作
        else if (GESTURE_FLAG == GESTURE_MODIFY_VOLUME) {
            currentVolume = audiomanager.getStreamVolume(AudioManager.STREAM_MUSIC); // 获取当前值
            if (Math.abs(distanceY) > Math.abs(distanceX)) {// 纵向移动大于横向移动
                if (distanceY >= DensityUtil.dip2px(this, STEP_VOLUME)) {// 音量调大,注意横屏时的坐标体系,尽管左上角是原点，但横向向上滑动时distanceY为正
                    if (currentVolume < maxVolume) {// 为避免调节过快，distanceY应大于一个设定值
                        currentVolume++;
                    }
                    gesture_iv_player_volume.setImageResource(R.drawable.souhu_player_volume);
                } else if (distanceY <= -DensityUtil.dip2px(this, STEP_VOLUME)) {// 音量调小
                    if (currentVolume > 0) {
                        currentVolume--;
                        if (currentVolume == 0) {// 静音，设定静音独有的图片
                            gesture_iv_player_volume.setImageResource(R.drawable.souhu_player_silence);
                        }
                    }
                }
                int percentage = (currentVolume * 100) / maxVolume;
                geture_tv_volume_percentage.setText(percentage + "%");
                audiomanager.setStreamVolume(AudioManager.STREAM_MUSIC,currentVolume, 0);
            }
        }
        // 如果每次触摸屏幕后第一次scroll是调节亮度，那之后的scroll事件都处理亮度调节，直到离开屏幕执行下一次操作
        else if (GESTURE_FLAG == GESTURE_MODIFY_BRIGHT) {
            gesture_iv_player_bright.setImageResource(R.drawable.souhu_player_bright);
            if (mBrightness < 0) {
                mBrightness = getWindow().getAttributes().screenBrightness;
                if (mBrightness <= 0.00f)
                    mBrightness = 0.50f;
                if (mBrightness < 0.01f)
                    mBrightness = 0.01f;
            }
            WindowManager.LayoutParams lpa = getWindow().getAttributes();
            lpa.screenBrightness = mBrightness + (mOldY - y) / playerHeight;
            if (lpa.screenBrightness > 1.0f)
                lpa.screenBrightness = 1.0f;
            else if (lpa.screenBrightness < 0.01f)
                lpa.screenBrightness = 0.01f;
            getWindow().setAttributes(lpa);
            geture_tv_bright_percentage.setText((int) (lpa.screenBrightness * 100) + "%");
        }

        firstScroll = false;// 第一次scroll执行完成，修改标志
        return false;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {}

    @Override
    public void onShowPress(MotionEvent e) {}
}