package org.ar.ar_android_voice_base

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import org.ar.ar_android_voicelive_base.R

class InfoAdapter: BaseQuickAdapter<InfoBean,BaseViewHolder>(R.layout.layout_info){

    override fun convert(holder: BaseViewHolder, item: InfoBean) {
        holder.setText(R.id.userId,item.userId)
        if (item.isSelf){
            holder.setVisible(R.id.self, true)
            holder.setText(R.id.txaudio, "${item.txAudio}Kb/s")
        }else{
            holder.setVisible(R.id.self, false)
            holder.setVisible(R.id.txaudio,false)
        }
        holder.setText(R.id.rxaudio,"${item.rxAudio}Kb/s")
    }
}