package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.example.myapplication.data.Profile
import com.example.myapplication.data.ProflieDB
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
    private lateinit var btn_googleAcountID : Button
    private lateinit var btn_kakaoAcountID : Button
    private lateinit var textView_generalAcountEmail : TextView
    private lateinit var textView_googleAcountEmail : TextView
    private lateinit var textView_kakaoAcountEmail : TextView
    private lateinit var btn_generalAcountEmail : Button
    private lateinit var btn_googleAcountEmail : Button
    private lateinit var btn_kakaoAcountEmail : Button
    private lateinit var btn_generalAcountPW : Button
    private lateinit var btn_googleAcountPW : Button
    private lateinit var btn_kakaoAcountPW : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_management)

        layout_generalAccountManagement = findViewById(R.id.layout_generalAccountManagement)
        imageView_generalAccountProfile = findViewById(R.id.imageView_generalAccountProfile)
        textView_generalAcountID = findViewById(R.id.editText_generalAcountID)
        btn_generalAcountID = findViewById(R.id.btn_generalAccountID)
        textView_generalAcountEmail = findViewById(R.id.editText_generalAcountEmail)
        btn_generalAcountEmail = findViewById(R.id.btn_generalAcountEmail)
        btn_generalAcountPW = findViewById(R.id.btn_generalAccountPW)

        layout_googleAccountManagement = findViewById(R.id.layout_googleAccountManagement)
        imageView_googleAccountProfile = findViewById(R.id.imageView_googleAccountProfile)
        textView_googleAcountID = findViewById(R.id.editText_googleAcountID)
        btn_googleAcountID = findViewById(R.id.btn_googleAccountID)
        textView_googleAcountEmail = findViewById(R.id.editText_googleAcountEmail)
        btn_googleAcountEmail = findViewById(R.id.btn_googleAcountEmail)
        btn_googleAcountPW = findViewById(R.id.btn_googleAcountPW)

        layout_kakaoAccountManagement = findViewById(R.id.layout_kakaoAccountManagement)
        imageView_kakaoAccountProfile = findViewById(R.id.imageView_kakaoAccountProfile)
        textView_kakaoAcountID = findViewById(R.id.editText_kakaoAcountID)
        btn_kakaoAcountID = findViewById(R.id.btn_kakaoAcountID)
        textView_kakaoAcountEmail = findViewById(R.id.editText_kakaoAcountEmail)
        btn_kakaoAcountEmail = findViewById(R.id.btn_kakaoAcountEmail)
        btn_kakaoAcountPW = findViewById(R.id.btn_kakaoAcountPW)

        // 현재 로그인된 계정만 띄우기
        profileDb = ProflieDB?.getInstace(this)
        if(profileDb?.profileDao()?.getAll().isNullOrEmpty()) {
            Toast.makeText(this, "there is no profile in DB", Toast.LENGTH_SHORT).show()
            layout_generalAccountManagement.visibility = View.GONE
            layout_googleAccountManagement.visibility = View.GONE
            layout_kakaoAccountManagement.visibility = View.GONE
        }
        else{
            var loginMethod = profileDb?.profileDao()?.getLogin()
            if(loginMethod?.and(1)==1) layout_generalAccountManagement.visibility = View.VISIBLE
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
    }
}