package com.zplayer.demo;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuyu.gsyvideoplayer.model.GSYVideoModel;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.ListGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import moe.codeest.enviews.ENDownloadView;


/**
 * Created by LiuKai on 2018/2/2.
 */

public class ShinyvVideoPlayer extends StandardGSYVideoPlayer {

    protected List<GSYVideoModel> mUriList = new ArrayList<>();
    protected int mPlayPosition;



    public ShinyvVideoPlayer(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public ShinyvVideoPlayer(Context context) {
        super(context);
    }

    public ShinyvVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    public int getLayoutId() {
        return R.layout.view_player;
    }

    /**
     * 设置播放URL
     *
     * @param url           播放url
     * @param cacheWithPlay 是否边播边缓存
     * @return
     */
    public boolean setUp(List<GSYVideoModel> url, boolean cacheWithPlay, int position) {
        mUriList = url;
        mPlayPosition = position;
        GSYVideoModel gsyVideoModel = url.get(position);
        boolean set = setUp(gsyVideoModel.getUrl(), cacheWithPlay, gsyVideoModel.getTitle());
        if (!TextUtils.isEmpty(gsyVideoModel.getTitle())) {
            mTitleTextView.setText(gsyVideoModel.getTitle());
        }
        return set;
    }

    /**
     * 设置播放URL
     *
     * @param url           播放url
     * @param cacheWithPlay 是否边播边缓存
     * @param cachePath     缓存路径，如果是M3U8或者HLS，请设置为false
     * @return
     */
    public boolean setUp(List<GSYVideoModel> url, boolean cacheWithPlay, int position, File cachePath) {
        mUriList = url;
        mPlayPosition = position;
        GSYVideoModel gsyVideoModel = url.get(position);
        boolean set = setUp(gsyVideoModel.getUrl(), cacheWithPlay, cachePath, gsyVideoModel.getTitle());
        if (!TextUtils.isEmpty(gsyVideoModel.getTitle())) {
            mTitleTextView.setText(gsyVideoModel.getTitle());
        }
        return set;
    }

    @Override
    public GSYBaseVideoPlayer startWindowFullscreen(Context context, boolean actionBar, boolean statusBar) {
        GSYBaseVideoPlayer gsyBaseVideoPlayer = super.startWindowFullscreen(context, actionBar, statusBar);
        if (gsyBaseVideoPlayer != null) {
            ShinyvVideoPlayer listGSYVideoPlayer = (ShinyvVideoPlayer) gsyBaseVideoPlayer;
            listGSYVideoPlayer.mPlayPosition = mPlayPosition;
            listGSYVideoPlayer.mUriList = mUriList;
            GSYVideoModel gsyVideoModel = mUriList.get(mPlayPosition);
            if (!TextUtils.isEmpty(gsyVideoModel.getTitle())) {
                listGSYVideoPlayer.mTitleTextView.setText(gsyVideoModel.getTitle());
            }
        }
        return gsyBaseVideoPlayer;
    }

    @Override
    protected void resolveNormalVideoShow(View oldF, ViewGroup vp, GSYVideoPlayer gsyVideoPlayer) {
        if (gsyVideoPlayer != null) {
            ShinyvVideoPlayer listGSYVideoPlayer = (ShinyvVideoPlayer) gsyVideoPlayer;
            mPlayPosition = listGSYVideoPlayer.mPlayPosition;
            mUriList = listGSYVideoPlayer.mUriList;
            GSYVideoModel gsyVideoModel = mUriList.get(mPlayPosition);
            if (!TextUtils.isEmpty(gsyVideoModel.getTitle())) {
                mTitleTextView.setText(gsyVideoModel.getTitle());
            }
        }
        super.resolveNormalVideoShow(oldF, vp, gsyVideoPlayer);
    }

    @Override
    public void onCompletion() {
        if (mPlayPosition < (mUriList.size() - 1)) {
            return;
        }
        super.onCompletion();
    }

    @Override
    public void onAutoCompletion() {
        if (mPlayPosition < (mUriList.size() - 1)) {
            mPlayPosition++;
            GSYVideoModel gsyVideoModel = mUriList.get(mPlayPosition);
            setUp(gsyVideoModel.getUrl(), mCache, mCachePath, gsyVideoModel.getTitle());
            if (!TextUtils.isEmpty(gsyVideoModel.getTitle())) {
                mTitleTextView.setText(gsyVideoModel.getTitle());
            }
            startPlayLogic();
            return;
        }
        super.onAutoCompletion();
    }

    /**
     * 开始状态视频播放，prepare时不执行  addTextureView();
     */
    @Override
    protected void prepareVideo() {
        startPrepare();
    }

    @Override
    public void onPrepared() {
        super.onPrepared();
        addTextureView();
    }

/*    @Override
    protected void changeUiToNormal() {
        super.changeUiToNormal();
        if (mHadPlay && mPlayPosition < (mUriList.size() - 1)) {
            setViewShowState(mThumbImageViewLayout, GONE);
            setViewShowState(mTopContainer, INVISIBLE);
            setViewShowState(mBottomContainer, INVISIBLE);
            setViewShowState(mStartButton, GONE);
            setViewShowState(mLoadingProgressBar, VISIBLE);
            setViewShowState(mBottomProgressBar, INVISIBLE);
            setViewShowState(mLockScreen, GONE);
            if (mLoadingProgressBar instanceof ENDownloadView) {
                ((ENDownloadView) mLoadingProgressBar).start();
            }
        }
    }*/

    @Override
    protected void updateStartImage() {
        if (mStartButton instanceof ImageView) {
            ImageView imageView = (ImageView) mStartButton;
            if (mCurrentState == CURRENT_STATE_PLAYING) {
                imageView.setImageResource(R.drawable.video_click_pause_selector);
            } else if (mCurrentState == CURRENT_STATE_ERROR) {
                imageView.setImageResource(R.drawable.video_click_play_selector);
            } else {
                imageView.setImageResource(R.drawable.video_click_play_selector);
            }
        }
    }
}
