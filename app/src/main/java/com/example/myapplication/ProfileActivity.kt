package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.data.Profile
import com.example.myapplication.data.ProflieDB

class ProfileActivity : AppCompatActivity() {
    //receive profile room data
    private var profileDb : ProflieDB? = null
    private var proflie = mutableListOf<Profile>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        profileDb = ProflieDB.getInstace(this)


        val nickname_btn = findViewById<Button>(R.id.nickname_btn)
        nickname_btn.setOnClickListener {
            val layoutInflater: LayoutInflater = getLayoutInflater()
            val builder = AlertDialog.Builder(this)
            val dialogview = layoutInflater.inflate(R.layout.nickname_dialog,null)
            builder.setView(dialogview).show()
        }

    }
}