package org.ar.ar_android_videolive_base

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission
import org.ar.ar_android_videolive_base.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(),View.OnClickListener{
    private var viewBinding: ActivityMainBinding? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding=ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(viewBinding?.root)
        viewBinding?.anchor?.isEnabled =false
        viewBinding?.guest?.isEnabled =false
        viewBinding?.input?.addTextChangedListener(textWatcher)
        viewBinding?.anchor?.setOnClickListener(this)
        viewBinding?.guest?.setOnClickListener(this)
        if (!AndPermission.hasPermissions(this, Permission.Group.STORAGE,
                        Permission.Group.MICROPHONE)){
            AndPermission.with(this)
                    .runtime()
                    .permission(
                            Permission.Group.STORAGE,
                            Permission.Group.MICROPHONE,
                            Permission.Group.CAMERA
                    ).onGranted({}).start();
        }
    }

    private var textWatcher: TextWatcher =object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            viewBinding?.anchor?.isEnabled = p0?.toString()?.length!! > 0
            viewBinding?.guest?.isEnabled = p0.toString().isNotEmpty()
        }
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.anchor ->{
                Intent(this,VideoLiveActivity::class.java).let {
                    it.putExtra("channelId",viewBinding?.input?.text.toString())
                    it.putExtra("type",1)  //主播
                    startActivity(it)
                }
            }
            R.id.guest ->{
                Intent(this,VideoLiveActivity::class.java).let {
                    it.putExtra("channelId",viewBinding?.input?.text.toString())
                    it.putExtra("type",2) //游客
                    startActivity(it)
                }
            }
        }
    }
}