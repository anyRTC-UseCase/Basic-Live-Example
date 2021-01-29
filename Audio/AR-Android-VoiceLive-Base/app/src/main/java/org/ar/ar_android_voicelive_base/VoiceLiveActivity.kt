package org.ar.ar_android_voicelive_base

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import org.ar.ar_android_voice_base.InfoAdapter
import org.ar.ar_android_voice_base.InfoBean
import org.ar.ar_android_voicelive_base.databinding.ActivityVoiceBinding
import org.ar.rtc.Constants
import org.ar.rtc.IRtcEngineEventHandler
import org.ar.rtc.RtcEngine

class VoiceLiveActivity:AppCompatActivity(),View.OnClickListener{

    private val TAG = VoiceLiveActivity::class.java.simpleName
    private var viewBinding: ActivityVoiceBinding? = null
    private var channelId:String =""
    private var type:Int =1
    private var mRtcEngine : RtcEngine? =null
    private var linearLayoutManager: LinearLayoutManager= LinearLayoutManager(this)
    private val infoAdapter = InfoAdapter()
    private var isMic:Boolean=false
    private var isVoice:Boolean=false

    private inner class mRtcEvent : IRtcEngineEventHandler() {
        override fun onJoinChannelSuccess(channel: String?, uid: String?, elapsed: Int) {
            super.onJoinChannelSuccess(channel, uid, elapsed)
            runOnUiThread{
                Log.i(TAG, "onJoinChannelSuccess:zhao type ="+type)
                if (type ==1){
                    infoAdapter.addData(InfoBean(uid!!,true))
                }
            }
        }

        override fun onUserJoined(uid: String?, elapsed: Int) {
            super.onUserJoined(uid, elapsed)
            runOnUiThread {
                Log.i(TAG, "onUserJoined:zhao uid ="+uid)
                infoAdapter.addData(InfoBean(uid!!,false))
            }
        }

        @SuppressLint("SetTextI18n")
        override fun onRtcStats(stats: RtcStats?) {
            super.onRtcStats(stats)
            runOnUiThread{
                val txaudio = infoAdapter.getViewByPosition(0,R.id.txaudio)as? TextView
                val rxaudio = infoAdapter.getViewByPosition(0,R.id.rxaudio)as? TextView
                txaudio?.setText("${stats?.txAudioKBitRate}Kb/s")
                rxaudio?.setText("${stats?.rxAudioKBitRate}Kb/s")
            }
        }

        @SuppressLint("SetTextI18n")
        override fun onRemoteAudioStats(stats: RemoteAudioStats?) {
            super.onRemoteAudioStats(stats)
            runOnUiThread{
                infoAdapter.data.forEachIndexed { index, infoBean ->
                    if (infoBean.userId.equals(stats?.uid)){
                        val rxaudio = infoAdapter.getViewByPosition(index,R.id.rxaudio) as? TextView
                        rxaudio?.setText("${stats?.receivedBitrate}Kb/s")
                    }
                }
            }
        }

        override fun onUserOffline(uid: String?, reason: Int) {
            super.onUserOffline(uid, reason)
            runOnUiThread {
                var leftIndex = -1
                infoAdapter.data.forEachIndexed { index, infoBean ->
                    if (infoBean.userId==uid) {
                        leftIndex = index
                    }
                }
                if (leftIndex != -1){
                    infoAdapter.remove(leftIndex)
                }
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityVoiceBinding.inflate(LayoutInflater.from(this))
        setContentView(viewBinding?.root)
        channelId = intent.getStringExtra("channelId").toString()
        type =intent.getIntExtra("type",1)
        linearLayoutManager.orientation =LinearLayoutManager.VERTICAL
        viewBinding?.recycle?.layoutManager =linearLayoutManager
        viewBinding?.recycle?.adapter = infoAdapter
        if (type==1){
            viewBinding?.mic?.visibility =View.VISIBLE
            viewBinding?.voice?.visibility =View.VISIBLE
        }else{
            viewBinding?.mic?.visibility =View.GONE
            viewBinding?.voice?.visibility =View.GONE
        }
        viewBinding?.mic?.setOnClickListener(this)
        viewBinding?.leave?.setOnClickListener(this)
        viewBinding?.voice?.setOnClickListener(this)
        joinChannel()
    }

    private fun joinChannel() {
        mRtcEngine = RtcEngine.create(this,getString(R.string.ar_appid),mRtcEvent())
        mRtcEngine?.enableAudio()
        mRtcEngine?.setChannelProfile(Constants.CHANNEL_PROFILE_COMMUNICATION)
        if (type ==1){
            mRtcEngine?.setClientRole(Constants.CLIENT_ROLE_BROADCASTER)
        }else{
            mRtcEngine?.setClientRole(Constants.CLIENT_ROLE_AUDIENCE)
        }
        mRtcEngine?.setEnableSpeakerphone(true)
        mRtcEngine?.joinChannel(getString(R.string.ar_token),channelId,"",VoiceLive.voiceLive.userId)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            release()
            finish()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }


    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.mic->{
                isMic =!isMic
                viewBinding?.mic?.isSelected= isMic
                mRtcEngine?.muteLocalAudioStream(isMic)
            }
            R.id.leave->{
                release()
                finish()
            }
            R.id.voice->{
                isVoice =!isVoice
                viewBinding?.voice?.isSelected =isVoice
                mRtcEngine?.setEnableSpeakerphone(!isVoice)
            }
        }
    }

    private fun release(){
        mRtcEngine?.leaveChannel()
        RtcEngine.destroy()
        mRtcEngine=null
    }

    override fun onDestroy() {
        super.onDestroy()
        release()
    }
}