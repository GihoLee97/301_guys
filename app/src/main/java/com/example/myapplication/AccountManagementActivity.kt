package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.example.myapplication.data.Profile
import com.example.myapplication.data.ProflieDB

class AccountManagementActivity : AppCompatActivity() {
    //receive profile room data
    private var profileDb: ProflieDB? = null
    private var profileList = mutableListOf<Profile>()
    private lateinit var layout_generalAccountManagement : LinearLayout
    private lateinit var layout_googleAccountManagement : LinearLayout
    private lateinit var layout_kakaoAccountManagement : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_management)

        layout_generalAccountManagement = findViewById(R.id.layout_generalAccountManagement)
        layout_googleAccountManagement = findViewById(R.id.layout_googleAccountManagement)
        layout_kakaoAccountManagement = findViewById(R.id.layout_kakaoAccountManagement)

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
            if(loginMethod?.and(2)==2) layout_googleAccountManagement.visibility = View.VISIBLE
            else layout_googleAccountManagement.visibility = View.GONE
            if(loginMethod?.and(4)==4) layout_kakaoAccountManagement.visibility = View.VISIBLE
            else layout_kakaoAccountManagement.visibility = View.GONE

        }
    }
}