package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.example.myapplication.data.Profile
import com.example.myapplication.data.ProflieDB
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.kakao.sdk.user.UserApiClient

class AccountManagementActivity : AppCompatActivity() {
    //receive profile room data
    private var profileDb: ProflieDB? = null
    private lateinit var googleAuth : FirebaseAuth
    private lateinit var layout_generalAccountManagement : LinearLayout
    private lateinit var layout_googleAccountManagement : LinearLayout
    private lateinit var layout_kakaoAccountManagement : LinearLayout
    private lateinit var imageView_generalAccountProfile : ImageView
    private lateinit var imageView_googleAccountProfile : ImageView
    private lateinit var imageView_kakaoAccountProfile : ImageView
    private lateinit var textView_generalAcountID : TextView
    private lateinit var textView_googleAcountID : TextView
    private lateinit var textView_kakaoAcountID : TextView
    private lateinit var btn_generalAcountID : Button
    private lateinit var textView_generalAcountEmail : TextView
    private lateinit var textView_googleAcountEmail : TextView
    private lateinit var textView_kakaoAcountEmail : TextView
    private lateinit var btn_generalAcountEmail : Button
    private lateinit var btn_generalAccountPW : Button
    private lateinit var btn_googleAccountSignOut : Button
    private lateinit var btn_kakaoAccountDelete : Button
    private lateinit var btn_kakaoAccountRevokeAccess : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_account_management)

        layout_generalAccountManagement = findViewById(R.id.layout_generalAccountManagement)
        imageView_generalAccountProfile = findViewById(R.id.imageView_generalAccountProfile)
        textView_generalAcountID = findViewById(R.id.editText_generalAcountID)
        btn_generalAcountID = findViewById(R.id.btn_generalAccountID)
        textView_generalAcountEmail = findViewById(R.id.editText_generalAcountEmail)
        btn_generalAcountEmail = findViewById(R.id.btn_generalAcountEmail)
        btn_generalAccountPW = findViewById(R.id.btn_generalAccountPW)

        layout_googleAccountManagement = findViewById(R.id.layout_googleAccountManagement)
        imageView_googleAccountProfile = findViewById(R.id.imageView_googleAccountProfile)
        textView_googleAcountID = findViewById(R.id.editText_googleAcountID)
        textView_googleAcountEmail = findViewById(R.id.editText_googleAcountEmail)
        btn_googleAccountSignOut = findViewById(R.id.btn_googleAccountSignOut)

        layout_kakaoAccountManagement = findViewById(R.id.layout_kakaoAccountManagement)
        imageView_kakaoAccountProfile = findViewById(R.id.imageView_kakaoAccountProfile)
        textView_kakaoAcountID = findViewById(R.id.editText_kakaoAcountID)
        textView_kakaoAcountEmail = findViewById(R.id.editText_kakaoAcountEmail)
        btn_kakaoAccountDelete = findViewById(R.id.btn_kakaoAccountDelete)
        btn_kakaoAccountRevokeAccess = findViewById(R.id.btn_kakaoAccountRevokeAccess)

        // 현재 로그인된 계정만 띄우기
        profileDb = ProflieDB.getInstace(this)
        if(profileDb?.profileDao()?.getAll().isNullOrEmpty()) {
            Toast.makeText(this, "there is no profile in DB", Toast.LENGTH_SHORT).show()
            layout_generalAccountManagement.visibility = View.GONE
            layout_googleAccountManagement.visibility = View.GONE
            layout_kakaoAccountManagement.visibility = View.GONE
        }
        else{
            var loginMethod = profileDb?.profileDao()?.getLogin()
            if(loginMethod?.and(1)==1){
                layout_generalAccountManagement.visibility = View.VISIBLE
                textView_generalAcountID.text = profileDb?.profileDao()?.getLoginid()
            }

            else layout_generalAccountManagement.visibility = View.GONE
            if(loginMethod?.and(2)==2) {
                layout_googleAccountManagement.visibility = View.VISIBLE
                googleAuth = FirebaseAuth.getInstance()
                val currUser = googleAuth.currentUser
                textView_googleAcountID.text = currUser?.uid
                textView_googleAcountEmail.text = currUser?.email
                Glide.with(this).load(currUser?.photoUrl).into(imageView_googleAccountProfile)
            }
            else layout_googleAccountManagement.visibility = View.GONE
            if(loginMethod?.and(4)==4) {
                layout_kakaoAccountManagement.visibility = View.VISIBLE
                UserApiClient.instance.me { user, error ->
                    if (error!=null)
                        Toast.makeText(this,"사용자 정보 요청 실패(카카오)",Toast.LENGTH_SHORT)
                    else if (user!=null) {
                        textView_kakaoAcountID.text = user?.id.toString() + user?.kakaoAccount?.profile?.nickname
                        textView_kakaoAcountEmail.text = user?.kakaoAccount?.email
                        Glide.with(this).load(user?.kakaoAccount?.profile?.thumbnailImageUrl).into(imageView_kakaoAccountProfile)
                    }
                }
            }
            else layout_kakaoAccountManagement.visibility = View.GONE
        }

        btn_googleAccountSignOut.setOnClickListener {
            googleAuth = FirebaseAuth.getInstance()
            googleAuth.signOut()
            updatelogOutInFo2DB("GOOGLE")
            val intent = Intent(this,InitialActivity::class.java)
            startActivity(intent)
        }

        btn_kakaoAccountDelete.setOnClickListener {
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    Log.e("KAKAO LOGOUT", "로그아웃 실패. SDK에서 토큰 삭제됨", error)
                }
                else {
                    Log.i("KAKAO LOGOUT", "로그아웃 성공. SDK에서 토큰 삭제됨")
                }
            }
            updatelogOutInFo2DB("KAKAO")
            val intent = Intent(this,InitialActivity::class.java)
            startActivity(intent)
        }

        btn_kakaoAccountRevokeAccess.setOnClickListener {
            UserApiClient.instance.unlink { error ->
                if (error != null) {
                    Log.e("KAKAO REVOKE ACCESS", "연결 끊기 실패", error)
                }
                else {
                    Log.i("KAKAO REVOKE ACCESS", "연결 끊기 성공. SDK에서 토큰 삭제 됨")
                }
            }
            updatelogOutInFo2DB("KAKAO")
            val intent = Intent(this,InitialActivity::class.java)
            startActivity(intent)
        }
        // 일반 로그인 시 비밀번호 변경
        btn_generalAccountPW.setOnClickListener{
            val dlg_change_pw = Dialog_change_pw(this)
            dlg_change_pw.start()
        }
    }

    private fun updatelogOutInFo2DB(method : String){
        profileDb = ProflieDB?.getInstace(this)
        if(!profileDb?.profileDao()?.getAll().isNullOrEmpty()) {
            val setRunnable = Runnable {
                val newProfile = Profile()
                newProfile.id = profileDb?.profileDao()?.getId()?.toLong()
                newProfile.nickname = profileDb?.profileDao()?.getNickname()!!
                newProfile.history = profileDb?.profileDao()?.getHistory()!!
                newProfile.level = profileDb?.profileDao()?.getLevel()!!
                newProfile.login = profileDb?.profileDao()?.getLogin()!!
                newProfile.profit = profileDb?.profileDao()?.getProfit()!!
                newProfile.login_id = profileDb?.profileDao()?.getLoginid()!!
                newProfile.login_pw = profileDb?.profileDao()?.getLoginpw()!!
                profileDb?.profileDao()?.update(newProfile)
            }
            var setThread = Thread(setRunnable)
            setThread.start()
        }
    }
}