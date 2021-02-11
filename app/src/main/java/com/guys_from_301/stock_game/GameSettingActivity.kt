package com.guys_from_301.stock_game

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import com.guys_from_301.stock_game.data.GameSet
import com.guys_from_301.stock_game.data.GameSetDB
import java.text.DecimalFormat

var setCash: Float = 0F
var setMonthly: Float = 0F
var setSalaryraise: Float = 0F
var setGamelength: Int = 0
var setGamespeed: Int = 0
val dec = DecimalFormat("###,###") // 금액 표시형식
val per = DecimalFormat("#,###.0") // % 표시형식


class GameSettingActivity : AppCompatActivity() {
    private lateinit var tvSetcash: TextView
    private lateinit var tvSetmonthly: TextView
    private lateinit var tvSetsalaryraise: TextView
    private lateinit var tvSetgamelength: TextView
    private lateinit var tvSetgamespeed: TextView

    private lateinit var seekbarSetcash: SeekBar
    private lateinit var seekbarSetmonthly: SeekBar
    private lateinit var seekbarSetsalaryraise: SeekBar
    private lateinit var seekbarSetgamelength: SeekBar
    private lateinit var seekbarSetgamespeed: SeekBar

    private lateinit var btnGamestart: Button
    private var gameSetDb: GameSetDB? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_setting)

        tvSetcash = findViewById(R.id.tv_setcash)
        tvSetmonthly = findViewById(R.id.tv_setmonthly)
        tvSetsalaryraise = findViewById(R.id.tv_setsalaryraise)
        tvSetgamelength = findViewById(R.id.tv_setgamelength)
        tvSetgamespeed = findViewById(R.id.tv_setgamespeed)

        seekbarSetcash = findViewById(R.id.seekbar_setcash)
        seekbarSetmonthly = findViewById(R.id.seekbar_setmonthly)
        seekbarSetsalaryraise = findViewById(R.id.seekbar_setsalaryraise)
        seekbarSetgamelength = findViewById(R.id.seekbar_setgamelength)
        seekbarSetgamespeed = findViewById(R.id.seekbar_setgamespeed)

        btnGamestart = findViewById(R.id.btn_gamestart)
        gameSetDb = GameSetDB.getInstace(this)


        // Seekbar Range: 0 ~ 1000
        seekbarSetcash.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                setCash = progress * 1000000F // 0 ~ 10 억
                tvSetcash.text = dec.format(setCash / 10000F)+ " 만 원"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                setCash = seekBar!!.progress * 1000000F
                tvSetcash.text = dec.format(setCash / 10000F)+ " 만 원"
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                setCash = seekBar!!.progress * 1000000F
                tvSetcash.text = dec.format(setCash / 10000F)+ " 만 원"
            }
        })

        // Seekbar Range: 0 ~ 200
        seekbarSetmonthly.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                setMonthly = progress * 100000F // 0 ~ 2 천만 원
                tvSetmonthly.text = dec.format(setMonthly / 10000F)+ " 만 원"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                setMonthly = seekBar!!.progress * 100000F
                tvSetmonthly.text = dec.format(setMonthly / 10000F)+ " 만 원"
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                setMonthly = seekBar!!.progress * 100000F
                tvSetmonthly.text = dec.format(setMonthly / 10000F)+ " 만 원"
            }
        })

        // Seekbar Range: 0 ~ 200
        seekbarSetsalaryraise.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                setSalaryraise = progress / 10F
                tvSetsalaryraise.text = "$setSalaryraise %/year"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                setSalaryraise = seekBar!!.progress / 10F
                tvSetsalaryraise.text = "$setSalaryraise %/year"
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                setSalaryraise = seekBar!!.progress / 10F
                tvSetsalaryraise.text = "$setSalaryraise %/year"
            }
        })

        // Seekbar Range: 0 ~ 4
        seekbarSetgamelength.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                setGamelength = (progress + 1) * 5
                tvSetgamelength.text = "$setGamelength years"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                setGamelength = (seekBar!!.progress + 1) * 5
                tvSetgamelength.text = "$setGamelength years"
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                setGamelength = (seekBar!!.progress + 1) * 5
                tvSetgamelength.text = "$setGamelength years"
            }
        })

        // Seekbar Range: 0 ~ 4
        seekbarSetgamespeed.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                setGamespeed = progress + 1
                tvSetgamespeed.text = "$setGamespeed days/sec"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                setGamespeed = seekBar!!.progress + 1
                tvSetgamespeed.text = "$setGamespeed days/sec"
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                setGamespeed = seekBar!!.progress + 1
                tvSetgamespeed.text = "$setGamespeed days/sec"
            }
        })


        btnGamestart.setOnClickListener {
            if (setCash==0F) {
                Toast.makeText(this@GameSettingActivity, "초기 자본금을 설정하세요", Toast.LENGTH_SHORT).show()
            }
            else {
                val addRunnable = Runnable {
                    val newGameSetDB = GameSet()
                    newGameSetDB.setcash = setCash
                    newGameSetDB.setgamelength = setGamelength
                    newGameSetDB.setgamespeed = setGamespeed
                    newGameSetDB.setmonthly = setMonthly
                    newGameSetDB.setsalaryraise = setMonthly
                    gameSetDb?.gameSetDao()?.insert(newGameSetDB)
                }
                val addThread = Thread(addRunnable)
                addThread.start()

                val intent = Intent(this,GameNormalActivity::class.java)
                startActivity(intent)
            }
        }


    }
}