package org.ar.ar_android_voicelive_base

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission
import org.ar.ar_android_voicelive_base.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(),View.OnClickListener{

    private var viewBinding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(viewBinding?.root)
        viewBinding?.anchor?.setOnClickListener(this)
        viewBinding?.guest?.setOnClickListener(this)
        if (!AndPermission.hasPermissions(this, Permission.Group.STORAGE,
                        Permission.Group.MICROPHONE)){
            AndPermission.with(this).runtime().permission(
                    Permission.Group.STORAGE,
                    Permission.Group.MICROPHONE
            ).onGranted({}).start()
        }
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.anchor ->{
                if (!TextUtils.isEmpty(viewBinding?.input?.text.toString())){
                    Intent(this,VoiceLiveActivity::class.java).let {
                        it.putExtra("channelId",viewBinding?.input?.text.toString())
                        it.putExtra("type",1)  //主播
                        startActivity(it)
                    }
                }else{
                    Toast.makeText(this, "请输入频道ID", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.guest ->{
                if (!TextUtils.isEmpty(viewBinding?.input?.text.toString())){
                    Intent(this,VoiceLiveActivity::class.java).let {
                        it.putExtra("channelId",viewBinding?.input?.text.toString())
                        it.putExtra("type",2) //游客
                        startActivity(it)
                    }
                }else{
                    Toast.makeText(this, "请输入频道ID", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}