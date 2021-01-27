package org.ar.ar_android_videolive_base

import android.app.Application
import kotlin.properties.Delegates

class VideoLive:Application() {

    val userId :String? =((Math.random()*9+1)*100000L).toInt().toString()

    companion object{
        var videoLive: VideoLive by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        videoLive =this
    }
}