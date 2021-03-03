package com.guys_from_301.stock_game

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast

class NewInitialActivity : AppCompatActivity() {
    private lateinit var tv_goToLoginActivity : TextView
    private lateinit var tv_goToSignUpActivity : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_initial)

        tv_goToLoginActivity = findViewById(R.id.tv_goToLoginActivity)
        tv_goToSignUpActivity = findViewById(R.id.tv_goToSignUpActivity)

        tv_goToLoginActivity.setOnClickListener {
            val intent = Intent(this,InitialLoginActivity::class.java)
            startActivity(intent)
        }

        tv_goToSignUpActivity.setOnClickListener {
            val intent = Intent(this,InitialSignUpEntranceActivity::class.java)
            startActivity(intent)
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
            finishAffinity()
            android.os.Process.killProcess(android.os.Process.myPid())
        }
        else {
            time3 = time1
            Toast.makeText(applicationContext, "한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show()
        }
        finish()
    }
}