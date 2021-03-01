package com.guys_from_301.stock_game

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import java.util.*

val PERCENTAGE_LIST = listOf<Int>(80, 50, 25, 10)
class Dialog_negotiation(context: Context) {
    var mContext: Context? = context
    val dlg = Dialog(context)   //부모 액티비티의 context 가 들어감
    private lateinit var tv_title: TextView
    private lateinit var tv_price: TextView
    private lateinit var tv_percentage: TextView
    private lateinit var tv_negotiation_contents: TextView
    private lateinit var iv_monthly: ImageView
    private lateinit var iv_asset: ImageView
    private lateinit var iv_salary_raise: ImageView
    private lateinit var btn_negotiation: Button

    fun start(theme: Int, step: Int, marketViewModel: MarketViewModel){
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        dlg.setContentView(R.layout.dialog_negotiation)     //다이얼로그에 사용할 xml 파일을 불러옴
        dlg.setCancelable(true)    //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히도록 함
        dlg.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        tv_title = dlg.findViewById(R.id.title)
        tv_price = dlg.findViewById(R.id.tv_price)
        tv_percentage = dlg.findViewById(R.id.percentage)
        tv_negotiation_contents = dlg.findViewById(R.id.tv_negotiation_contents)
        iv_asset = dlg.findViewById(R.id.iv_asset)
        iv_monthly = dlg.findViewById(R.id.iv_monthly)
        iv_salary_raise = dlg.findViewById(R.id.iv_salary_raise)
        btn_negotiation = dlg.findViewById(R.id.btn_negotiation)
        val dialogSuccess = mContext?.let { Dialog_negotiation_success(it) }
        val dialogUnsuccess = mContext?.let { Dialog_negotiation_unsuccess(it) }

        when (theme){
            1-> {
                tv_title.text = "초기 자산 협상하기"
                tv_negotiation_contents.text = "초기 자산을 $"+ dec.format(SET_CASH_STEP[step+1]).toString()+"로 인상하는 협상을 진행합니다"
                iv_asset.visibility = View.VISIBLE
            }
            2-> {tv_title.text = "월급 협상하기"
                tv_negotiation_contents.text = "월급을 $"+ dec.format(SET_MONTHLY_STEP[step+1]).toString()+"로 인상하는 협상을 진행합니다"
                iv_monthly.visibility = View.VISIBLE
            }
            3-> {tv_title.text = "연봉 인상률 협상하기"
                tv_negotiation_contents.text = "연봉 인상률을 "+ dec.format(SET_SALARY_RAISE_STEP[step+1]).toString()+"%로 인상하는 협상을 진행합니다"
                iv_salary_raise.visibility = View.VISIBLE
            }
        }
        when (step){
            0-> tv_percentage.text = "성공확률: 80%"
            1-> tv_percentage.text = "성공확률: 50%"
            2-> tv_percentage.text = "성공확률: 25%"
            3-> tv_percentage.text = "성공확률: 10%"
        }

        btn_negotiation.setOnClickListener{
            if(negotiation(step)){
                when(theme){
                    1->marketViewModel.applyItemRaiseSetCash()
                    2->marketViewModel.applyItemRaiseSetMonthly()
                    3->marketViewModel.applyItemRaiseSetSalaryRaise()
                }
                if (dialogSuccess != null) {
                    dialogSuccess.start(theme,step)
                }
                dlg.dismiss()
            }
            else{
                when(theme){
                    1->marketViewModel.applyItemFailtoSetCash()
                    2->marketViewModel.applyItemFailtoSetMonthly()
                    3->marketViewModel.applyItemFailtoSetSalaryRaise()
                }
                if (dialogUnsuccess != null) {
                    dialogUnsuccess.start(theme, 0)
                }
                dlg.dismiss()
            }
        }
        dlg.show()
    }

    fun negotiation(level: Int): Boolean {
        var percentage = 0
        val random = Random()
        val num = random.nextInt(99)
        when (level) {
            0 -> percentage = 80
            1 -> percentage = 50
            2 -> percentage = 25
            3 -> percentage = 10
        }
        return num < percentage
    }

}