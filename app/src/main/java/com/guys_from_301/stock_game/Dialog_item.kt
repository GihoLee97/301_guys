package com.guys_from_301.stock_game

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import com.guys_from_301.stock_game.data.GameNormal
import com.guys_from_301.stock_game.data.GameNormalDB
import com.guys_from_301.stock_game.data.Profile
import com.guys_from_301.stock_game.data.ProfileDB
import kotlin.math.abs
import kotlin.math.roundToInt

class Dialog_item(context: Context, _nowmoney :Int, _nowvalue1: Int) {
    var mContext: Context? = context
    private val dlg = Dialog(context)   //부모 액티비티의 context 가 들어감\

    private var nowmoney: Int = _nowmoney // 현재 현금 받아오기. 다이얼로그 닫힐때 합산해서 DB에 반영
    private var nowvalue1: Int = _nowvalue1 // 현재 현금 받아오기. 다이얼로그 닫힐때 합산해서 DB에 반영
    private var item1ConsumeTemp: Int = 0 // 아이템1 소모 생명
    private var item2ConsumeTemp: Int = 0 // 아이템2 소모 생명
    private var itemConsumeStacks: Int = 0 // 총 소모스택

    private var item2speed: Int = 0 // 아이템2 게임 스피드

    private lateinit var tvItemstack: TextView
    private lateinit var tvItem1: TextView
    private lateinit var tvItem2: TextView
    private lateinit var tvItem3: TextView
    private lateinit var tvItem4: TextView
    private lateinit var tvItem1quant: TextView
    private lateinit var tvItem2quant: TextView
    private lateinit var tvItem3quant: TextView
    private lateinit var tvItem4quant: TextView
    private lateinit var seekbarItem1: SeekBar
    private lateinit var seekbarItem2: SeekBar
    private lateinit var btnItem1ok: Button
    private lateinit var btnItem2ok: Button
    private lateinit var btnItem3ok: Button
    private lateinit var btnItem4ok: Button
    private lateinit var btnItemok: Button
    private lateinit var listenter: ItemDialogClickedListener

    private var gameNormalDb: GameNormalDB? = null


    fun start() {
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        dlg.setContentView(R.layout.dialog_item_pick)     //다이얼로그에 사용할 xml 파일을 불러옴
        dlg.setCancelable(false)    //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함

        tvItemstack = dlg.findViewById(R.id.tv_itemstack)
        tvItem1 = dlg.findViewById(R.id.tv_item1)
        tvItem2 = dlg.findViewById(R.id.tv_item2)
        tvItem3 = dlg.findViewById(R.id.tv_item3)
        tvItem4 = dlg.findViewById(R.id.tv_item4)
        tvItem1quant = dlg.findViewById(R.id.tv_item1quant)
        tvItem2quant = dlg.findViewById(R.id.tv_item2quant)
        tvItem3quant = dlg.findViewById(R.id.tv_item3quant)
        tvItem4quant = dlg.findViewById(R.id.tv_item4quant)
        seekbarItem1 = dlg.findViewById(R.id.seekbar_item1)
        seekbarItem2 = dlg.findViewById(R.id.seekbar_item2)
        btnItem1ok = dlg.findViewById(R.id.btn_item1ok)
        btnItem2ok = dlg.findViewById(R.id.btn_item2ok)
        btnItem3ok = dlg.findViewById(R.id.btn_item3ok)
        btnItem4ok = dlg.findViewById(R.id.btn_item4ok)
        btnItemok = dlg.findViewById(R.id.btn_itemok)
        gameNormalDb = GameNormalDB.getInstace(dlg.context)

        tvItemstack.text = nowmoney.toString() + "스택"


        // 각 버튼 및 탐색바 초기화
        btnItem1ok.text = "사용"
        btnItem1ok.isEnabled = true
        btnItem1ok.text = "사용"
        btnItem1ok.isEnabled = true

        if (item3Active) {
            btnItem3ok.text = "사용중"
            btnItem3ok.isEnabled = false
        }
        if (item4Active) {
            btnItem4ok.text = "사용중"
            btnItem4ok.isEnabled = false
        }

        seekbarItem2.progress = setGamespeed
        item2speed = setGamespeed


        seekbarItem1.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (((progress * item1Able / 1000F) - (progress * item1Able / 1000F).roundToInt()) < 0F) {
                    item1Length = (progress * item1Able / 1000F).roundToInt() - 1
                } else {
                    item1Length = (progress * item1Able / 1000F).roundToInt()
                }

                item1ConsumeTemp = item1Length // 스택소모
                tvItem1.text =
                    item1ConsumeTemp.toString() + "스택 소모, " + item1Length.toString() + " 거래일 되돌리기"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                if (((seekBar!!.progress * item1Able / 1000F) - (seekBar!!.progress * item1Able / 1000F).roundToInt()) < 0F) {
                    item1Length = (seekBar!!.progress * item1Able / 1000F).roundToInt() - 1
                } else {
                    item1Length = (seekBar!!.progress * item1Able / 1000F).roundToInt()
                }

                item1ConsumeTemp = item1Length // 스택소모
                tvItem1.text =
                    item1ConsumeTemp.toString() + "스택 소모, " + item1Length.toString() + " 거래일 되돌리기"
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (((seekBar!!.progress * item1Able / 1000F) - (seekBar!!.progress * item1Able / 1000F).roundToInt()) < 0F) {
                    item1Length = (seekBar!!.progress * item1Able / 1000F).roundToInt() - 1
                } else {
                    item1Length = (seekBar!!.progress * item1Able / 1000F).roundToInt()
                }

                item1ConsumeTemp = item1Length // 스택소모
                tvItem1.text =
                    item1ConsumeTemp.toString() + "스택 소모, " + item1Length.toString() + " 거래일 되돌리기"
            }
        })


        seekbarItem2.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                item2speed = progress
                item2ConsumeTemp = abs(setGamespeed - item2speed) * 30

                if (item2speed==6) {
                    tvItem2.text =
                            item2ConsumeTemp.toString() + "스택 소모, 초당 " + "8 거래일"
                } else if (item2speed==7) {
                    tvItem2.text =
                            item2ConsumeTemp.toString() + "스택 소모, 초당 " + "10 거래일"
                } else {
                    tvItem2.text =
                            item2ConsumeTemp.toString() + "스택 소모, 초당 " + item2speed.toString() + " 거래일"
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                item2speed = seekBar!!.progress
                item2ConsumeTemp = abs(setGamespeed - item2speed) * 30

                if (item2speed==6) {
                    tvItem2.text =
                            item2ConsumeTemp.toString() + "스택 소모, 초당 " + "8 거래일"
                } else if (item2speed==7) {
                    tvItem2.text =
                            item2ConsumeTemp.toString() + "스택 소모, 초당 " + "10 거래일"
                } else {
                    tvItem2.text =
                            item2ConsumeTemp.toString() + "스택 소모, 초당 " + item2speed.toString() + " 거래일"
                }
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                item2speed = seekBar!!.progress
                item2ConsumeTemp = abs(setGamespeed - item2speed) * 30

                if (item2speed==6) {
                    tvItem2.text =
                            item2ConsumeTemp.toString() + "스택 소모, 초당 " + "8 거래일"
                } else if (item2speed==7) {
                    tvItem2.text =
                            item2ConsumeTemp.toString() + "스택 소모, 초당 " + "10 거래일"
                } else {
                    tvItem2.text =
                            item2ConsumeTemp.toString() + "스택 소모, 초당 " + item2speed.toString() + " 거래일"
                }
            }
        })


        btnItem1ok.setOnClickListener {
            if (item1Length == 0) {
                Toast.makeText(dlg.context, "되돌아갈 거래일 수를 지정하세요", Toast.LENGTH_SHORT).show()
            } else {
                nowmoney = nowmoney?.minus(item1ConsumeTemp)
                tvItemstack.text = nowmoney.toString() + "스택"
                btnItemok.text = "확인"

                item1Active = true

                btnItem1ok.text = "사용됨"
                btnItem1ok.isEnabled = false
                val addRunnable = kotlinx.coroutines.Runnable {
                    val newGameNormalDB = GameNormal()
                    newGameNormalDB.id = localdatatime
                    newGameNormalDB.buyorsell = "아이템 사용"
                    newGameNormalDB.select = 1
                    newGameNormalDB.setId = setId
                    newGameNormalDB.item1active = item1Active
                    newGameNormalDB.item1active = item2Active
                    newGameNormalDB.item1active = item3Active
                    newGameNormalDB.item1active = item4Active
                    newGameNormalDB.item1able = item1Able
                    newGameNormalDB.item1length = item1Length
                    gameNormalDb?.gameNormalDao()?.insert(newGameNormalDB)
                }
                val addThread = Thread(addRunnable)
                addThread.start()

                Toast.makeText(
                    dlg.context,
                    item1ConsumeTemp.toString() + "스택을 소모해 " + item1Length.toString() + " 거래일 만큼 시간을 되돌립니다",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        btnItem2ok.setOnClickListener {
            if (item2speed == setGamespeed) {
                Toast.makeText(dlg.context, "변경할 시간 진행 빠르기를 선택하세요", Toast.LENGTH_SHORT).show()
            } else {
                nowmoney = nowmoney?.minus(item2ConsumeTemp)
                tvItemstack.text = nowmoney.toString() + "스택"
                btnItemok.text = "확인"

                setGamespeed = item2speed

                btnItem2ok.text = "사용됨"
                btnItem2ok.isEnabled = false
                val addRunnable = kotlinx.coroutines.Runnable {
                    val newGameNormalDB = GameNormal()
                    newGameNormalDB.id = localdatatime
                    newGameNormalDB.buyorsell = "아이템 사용"
                    newGameNormalDB.select = 2
                    newGameNormalDB.setId = setId
                    newGameNormalDB.item1active = item1Active
                    newGameNormalDB.item1active = item2Active
                    newGameNormalDB.item1active = item3Active
                    newGameNormalDB.item1active = item4Active
                    newGameNormalDB.item1able = item1Able
                    newGameNormalDB.item1length = item1Length
                    gameNormalDb?.gameNormalDao()?.insert(newGameNormalDB)
                }
                val addThread = Thread(addRunnable)
                addThread.start()

                Toast.makeText(
                    dlg.context,
                    item2ConsumeTemp.toString() + "스택을 소모해 1초에 " + item2speed.toString() + " 거래일 만큼의 시간이 흐릅니다",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }


        btnItem3ok.setOnClickListener {
            nowmoney = nowmoney?.minus(50)
            tvItemstack.text = nowmoney.toString() + "스택"
            btnItemok.text = "확인"
            item3Active = true

            btnItem3ok.text = "사용중"
            btnItem3ok.isEnabled = false
            val addRunnable = kotlinx.coroutines.Runnable {
                val newGameNormalDB = GameNormal()
                newGameNormalDB.id = localdatatime
                newGameNormalDB.buyorsell = "아이템 사용"
                newGameNormalDB.select = 3
                newGameNormalDB.setId = setId
                newGameNormalDB.item1active = item1Active
                newGameNormalDB.item1active = item2Active
                newGameNormalDB.item1active = item3Active
                newGameNormalDB.item1active = item4Active
                newGameNormalDB.item1able = item1Able
                newGameNormalDB.item1length = item1Length
                gameNormalDb?.gameNormalDao()?.insert(newGameNormalDB)
            }
            val addThread = Thread(addRunnable)
            addThread.start()

            Toast.makeText(dlg.context, "50스택을 소모해 제공되는 뉴스가 월 10개로 늘었습니다", Toast.LENGTH_SHORT)
                .show()
        }

        btnItem4ok.setOnClickListener {
            nowmoney = nowmoney?.minus(50)
            tvItemstack.text = nowmoney.toString() + "스택"
            btnItemok.text = "확인"
            item4Active = true

            btnItem4ok.text = "사용중"
            btnItem4ok.isEnabled = false
            val addRunnable = kotlinx.coroutines.Runnable {
                val newGameNormalDB = GameNormal()
                newGameNormalDB.id = localdatatime
                newGameNormalDB.buyorsell = "아이템 사용"
                newGameNormalDB.select = 4
                newGameNormalDB.setId = setId
                newGameNormalDB.item1active = item1Active
                newGameNormalDB.item1active = item2Active
                newGameNormalDB.item1active = item3Active
                newGameNormalDB.item1active = item4Active
                newGameNormalDB.item1able = item1Able
                newGameNormalDB.item1length = item1Length
                gameNormalDb?.gameNormalDao()?.insert(newGameNormalDB)
            }
            val addThread = Thread(addRunnable)
            addThread.start()

            Toast.makeText(dlg.context, "50스택을 소모해 레버리지 ETF 거래가 가능해졌습니다", Toast.LENGTH_SHORT).show()
        }

        btnItemok.setOnClickListener {
            // DB에 스택 변동사항 저장
            var profileDb : ProfileDB? = null
            profileDb = ProfileDB?.getInstace(mContext!!)
            // DB에 아이템 사용여부 저장
            // 서버에 올리는 코드
            update(getHash(profileDb?.profileDao()?.getLoginid()!!).trim(),
                    getHash(profileDb?.profileDao()?.getLoginpw()!!).trim(),
                    nowmoney, nowvalue1, profileDb?.profileDao()?.getNickname()!!,
                    profileDb?.profileDao()?.getProfit()!!,
                    profileDb?.profileDao()?.getRoundCount()!!,
                    profileDb?.profileDao()?.getHistory()!!,
                    profileDb?.profileDao()?.getLevel()!!)
                    // update money to DB
            val newProfile = Profile()
            newProfile.id = profileDb?.profileDao()?.getId()?.toLong()
            newProfile.nickname = profileDb?.profileDao()?.getNickname()!!
            newProfile.history = profileDb?.profileDao()?.getHistory()!!
            newProfile.level = profileDb?.profileDao()?.getLevel()!!
            newProfile.exp = profileDb?.profileDao()?.getExp()!!
            newProfile.rank = profileDb?.profileDao()?.getRank()!!
            newProfile.login = profileDb?.profileDao()?.getLogin()!!
            newProfile.money = nowmoney
            newProfile.value1 = nowvalue1
            newProfile.profit = profileDb?.profileDao()?.getProfit()!!
            newProfile.login_id = profileDb?.profileDao()?.getLoginid()!!
            newProfile.login_pw = profileDb?.profileDao()?.getLoginpw()!!
            profileDb?.profileDao()?.update(newProfile)
            //
            dlg.dismiss()
            click =
                !click //////////////////////////////////////////////////////////////////////////
        }

        dlg.show()
    }

    fun setOnItemClickedListener(listener: (List<Int>) -> Unit) {
        this.listenter = object : ItemDialogClickedListener {
            override fun onItemClicked(content: List<Int>) {
                listener(content)
            }
        }
    }

    interface ItemDialogClickedListener {
        fun onItemClicked(content: List<Int>)
    }

}
