package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val profile1_btn = findViewById<Button>(R.id.profile1_btn)
        profile1_btn.setOnClickListener{
            val intent = Intent(this,ProfileActivity::class.java)
            startActivity(intent)
        }

        val setting_btn = findViewById<Button>(R.id.setting_btn)
        setting_btn.setOnClickListener {
            val intent = Intent(this,SettingActivity::class.java)
            startActivity(intent)
        }


        var game_btn =  findViewById<Button>(R.id.game_btn)
        game_btn.isEnabled = false // 로딩 미완료 상태일 때 게임 버튼 비활성화

        game_btn.setOnClickListener{
            val intent = Intent(this,GameNormalActivity::class.java)
            startActivity(intent)
        }

        while (true) {
            if (loadcomp) {
                game_btn.isEnabled = true
                game_btn.text = "게임 시작"
                break
            }
            Thread.sleep(50)
        }

    }

    // 두번 누르면 종료되는 코드
    var time3: Long = 0
    override fun onBackPressed() {
        val time1 = System.currentTimeMillis()
        val time2 = time1 - time3
        if (time2 in 0..2000) {
            // 이거 3줄 다 써야 안전하게 종료
            moveTaskToBack(true)
            finish()
            android.os.Process.killProcess(android.os.Process.myPid())
        }
        else {
            time3 = time1
            Toast.makeText(applicationContext, "한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show()
        }
    }
}
