
package com.example.myapplication


import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.provider.ContactsContract
import android.view.Window
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import com.example.myapplication.R
import com.example.myapplication.data.Profile
import com.example.myapplication.data.ProflieDB

class Dialog_nick(context : Context) {

    private val dlg = Dialog(context) //부모 액티비티의 context 가 들어감
    private lateinit var btn_ok : Button
    private lateinit var nickname_editText : EditText
    private lateinit var listenter: Dialog_nick.NicknameDialogClickedListener


    fun start(profileDb :ProflieDB?) {
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE) //타이틀바 제거
        dlg.setContentView(R.layout.dialog_nickname) //다이얼로그에 사용할 xml 파일을 불러옴
        dlg.setCancelable(false) //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함
        //
        btn_ok = dlg.findViewById(R.id.nicknameokbtn)
        nickname_editText = dlg.findViewById(R.id.editNickName)


        btn_ok.setOnClickListener {
            val setRunnable = Runnable {
                val newProfile = Profile()
                newProfile.id = profileDb?.profileDao()?.getId()?.toLong()
                newProfile.nickname = nickname_editText.text.toString()
                newProfile.history = profileDb?.profileDao()?.getHistory()!!
                newProfile.level = profileDb?.profileDao()?.getLevel()!!
                newProfile.login = profileDb?.profileDao()?.getLogin()!!
                newProfile.profit = profileDb?.profileDao()?.getProfit()!!
                profileDb?.profileDao()?.update(newProfile)
            }

            var setThread = Thread(setRunnable)
            setThread.start()

            var result:String = nickname_editText.text.toString()
            listenter.onNicknameClicked(result)
            dlg.dismiss()
        }
        dlg.show()
    }

    fun setOnNicknameClickedListener(listener: (String)->Unit){
        this.listenter = object : NicknameDialogClickedListener{
            override fun onNicknameClicked(content: String) {
                listener(content)
            }
        }
    }

    interface NicknameDialogClickedListener{
        fun onNicknameClicked(content: String)
    }

}