package org.ar.ar_android_voicelive_base

import android.app.Application
import kotlin.properties.Delegates

class VoiceLive:Application() {

    val userId :String? =((Math.random()*9+1)*100000L).toInt().toString()

    companion object{
        var voiceLive: VoiceLive by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        voiceLive =this
    }
}