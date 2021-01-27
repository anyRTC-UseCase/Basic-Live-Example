package org.ar.ar_android_videolive_base

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_video.*
import org.ar.ar_android_videolive_base.databinding.ActivityVideoBinding
import org.ar.rtc.Constants
import org.ar.rtc.IRtcEngineEventHandler
import org.ar.rtc.RtcEngine
import org.ar.rtc.VideoEncoderConfiguration
import org.ar.rtc.video.VideoCanvas

class VideoLiveActivity : AppCompatActivity(),View.OnClickListener {

    private val TAG = VideoLiveActivity::class.java.simpleName
    private var viewBinding: ActivityVideoBinding? =null
    private var channelId:String =""
    private var userId:String =""
    private var type:Int =1
    private var mRtcEngine: RtcEngine? =null
    private var isMic:Boolean=false
    private var isCamera:Boolean=false
    private var arVideoGroup:ARVideoGroup? =null

    private inner class mRtcEvent : IRtcEngineEventHandler(){
        override fun onJoinChannelSuccess(channel: String?, uid: String?, elapsed: Int) {
            super.onJoinChannelSuccess(channel, uid, elapsed)
            runOnUiThread {
                Log.i(TAG, "onJoinChannelSuccess: --->")
            }
        }

        override fun onUserJoined(uid: String?, elapsed: Int) {
            super.onUserJoined(uid, elapsed)
            runOnUiThread {
                Log.i(TAG, "onUserJoined: --->")
            }
        }

        override fun onFirstLocalVideoFrame(width: Int, height: Int, elapsed: Int) {
            super.onFirstLocalVideoFrame(width, height, elapsed)
            runOnUiThread {
                Log.i(TAG, "onFirstLocalVideoFrame: -->type ="+type)
                if (type==1){
                    setupLocalVideo()
                }
            }
        }

        override fun onFirstRemoteVideoDecoded(uid: String?, width: Int, height: Int, elapsed: Int) {
            super.onFirstRemoteVideoDecoded(uid, width, height, elapsed)
            runOnUiThread {
                Log.i(TAG, "onFirstRemoteVideoDecoded: --->type ="+type)
                setupRemoteVideo(uid!!)
            }
        }

        override fun onUserOffline(uid: String?, reason: Int) {
            super.onUserOffline(uid, reason)
            runOnUiThread {
                arVideoGroup?.removeView(uid!!);
                Log.i(TAG, "onUserOffline: --->")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityVideoBinding.inflate(LayoutInflater.from(this))
        setContentView(viewBinding?.root)
        channelId =intent.getStringExtra("channelId").toString()
        type = intent.getIntExtra("type",1)
        userId = VideoLive.videoLive.userId.toString()
        arVideoGroup = ARVideoGroup(this,viewBinding?.rlVideo!!)
        if (type==1){
            viewBinding?.mic?.visibility =View.VISIBLE
            viewBinding?.camera?.visibility =View.VISIBLE
        }else{
            viewBinding?.mic?.visibility =View.GONE
            viewBinding?.camera?.visibility =View.GONE
        }
        viewBinding?.mic?.setOnClickListener(this)
        viewBinding?.camera?.setOnClickListener(this)
        viewBinding?.leave?.setOnClickListener(this)
        initEngine()
        joinChannel()
    }

    private fun initEngine(){
        mRtcEngine = RtcEngine.create(this,getString(R.string.ar_appid),mRtcEvent())
        mRtcEngine?.enableVideo()
        mRtcEngine?.setVideoEncoderConfiguration(
            VideoEncoderConfiguration(
                VideoEncoderConfiguration.VD_640x480,
                VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT))
        mRtcEngine?.setChannelProfile(Constants.CHANNEL_PROFILE_COMMUNICATION)
        if (type==1){
            mRtcEngine?.setClientRole(Constants.CLIENT_ROLE_BROADCASTER)
        }else{
            mRtcEngine?.setClientRole(Constants.CLIENT_ROLE_AUDIENCE)
        }
        mRtcEngine?.setEnableSpeakerphone(true)
    }

    private fun joinChannel() {
        mRtcEngine?.joinChannel(getString(R.string.ar_token),channelId,"",userId)
    }

    private fun setupLocalVideo(){
        val view = RtcEngine.CreateRendererView(baseContext)
        if (viewBinding?.rlVideo !=null){
            rl_video.removeAllViews()
        }
        arVideoGroup?.addView(userId,view)
        mRtcEngine?.setupLocalVideo(VideoCanvas(view, VideoCanvas.RENDER_MODE_HIDDEN,userId))
        mRtcEngine?.startPreview()
    }

    private fun removeLocal(){
        arVideoGroup?.removeView(userId);
    }

    private fun setupRemoteVideo(uid:String){
        val view = RtcEngine.CreateRendererView(baseContext)
        arVideoGroup?.addView(uid,view)
        mRtcEngine?.setupRemoteVideo(VideoCanvas(view, Constants.RENDER_MODE_HIDDEN,uid))
        mRtcEngine?.startPreview()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode ==KeyEvent.KEYCODE_BACK){
            release()
            finish()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.mic ->{
                if (isMic){
                    viewBinding?.mic?.setImageResource(R.drawable.img_audio_open)
                }else{
                    viewBinding?.mic?.setImageResource(R.drawable.img_audio_close)
                }
                isMic =!isMic
                mRtcEngine?.muteLocalAudioStream(isMic)
            }
            R.id.camera->{
                if (isCamera){
                    viewBinding?.camera?.setImageResource(R.drawable.img_switch)
                }else{
                    viewBinding?.camera?.setImageResource(R.drawable.img_switch_click)
                }
                isCamera =!isCamera
                mRtcEngine?.switchCamera()
            }
            R.id.leave->{
                release()
                finish()
            }
        }
    }

    private fun release(){
        removeLocal()
        mRtcEngine?.leaveChannel()
        RtcEngine.destroy()
        mRtcEngine=null
    }

    override fun onDestroy() {
        super.onDestroy()
        release()
    }

}