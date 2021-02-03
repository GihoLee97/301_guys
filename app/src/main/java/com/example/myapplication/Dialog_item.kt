package com.example.myapplication

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.Button
import android.widget.TextView

class Dialog_item(context : Context) {
    var mContext: Context? = context
    private val dlg = Dialog(context)   //부모 액티비티의 context 가 들어감
    private lateinit var btnok : Button
    private lateinit var btncancel : Button
    private lateinit var btn_item1_plus : Button
    private lateinit var btn_item1_minus : Button
    private lateinit var btn_item2_plus : Button
    private lateinit var btn_item2_minus : Button
    private lateinit var btn_item3_plus : Button
    private lateinit var btn_item3_minus : Button
    private lateinit var textitem1 : TextView
    private lateinit var textitem2 : TextView
    private lateinit var textitem3 : TextView
    private lateinit var listenter: Dialog_item.ItemDialogClickedListener


    fun start(item1 : Int, item2 : Int, item3 : Int){

        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        dlg.setContentView(R.layout.dialog_item_pick)     //다이얼로그에 사용할 xml 파일을 불러옴
        dlg.setCancelable(false)    //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함
        var item1_number : Int = item1 ; var item2_number : Int = item2 ;var item3_number : Int = item3
        textitem1 = dlg.findViewById(R.id.item1_number)
        textitem2 = dlg.findViewById(R.id.item2_number)
        textitem3 = dlg.findViewById(R.id.item3_number)
        btnok = dlg.findViewById(R.id.btn_ok)
        btncancel = dlg.findViewById(R.id.btn_cancel)
        btn_item1_plus = dlg.findViewById(R.id.btn_item1_plus)
        btn_item1_minus = dlg.findViewById(R.id.btn_item1_minus)
        btn_item2_plus = dlg.findViewById(R.id.btn_item2_plus)
        btn_item2_minus = dlg.findViewById(R.id.btn_item2_minus)
        btn_item3_plus = dlg.findViewById(R.id.btn_item3_plus)
        btn_item3_minus = dlg.findViewById(R.id.btn_item3_minus)
        textitem1.text="개수 : "+item1_number.toString()+"개"
        textitem2.text="개수 : "+item2_number.toString()+"개"
        textitem3.text="개수 : "+item3_number.toString()+"개"
        btnok.setOnClickListener{

            //인터페이스의 함수를 호출하여 변수에 저장된 값들을 Activity로 전달
            var result: List<Int> = listOf(item1_number,item2_number,item3_number)
            listenter.onItemClicked(result)

            dlg.dismiss()
            click = !click //////////////////////////////////////////////////////////////////////////
        }
        btncancel.setOnClickListener {
            dlg.dismiss()
            click = !click /////////////////////////////////////////////////////////////////////////
        }
        btn_item1_plus.setOnClickListener{
            item1_number = item1_number.toInt() + 1
            textitem1.text="개수 : "+item1_number.toString()+"개"

        }
        btn_item1_minus.setOnClickListener{
            if(item1_number == 0){
            }
            else item1_number = item1_number.toInt() - 1

            textitem1.text="개수 : "+item1_number.toString()+"개"
        }
        btn_item2_plus.setOnClickListener{

            item2_number = item2_number.toInt() + 1
            textitem2.text="개수 : "+item2_number.toString()+"개"
        }
        btn_item2_minus.setOnClickListener{
            if(item2_number == 0){
            }
            else item2_number = item2_number.toInt() - 1

            textitem2.text="개수 : "+item2_number.toString()+"개"
        }
        btn_item3_plus.setOnClickListener{
            item3_number = item3_number.toInt() + 1
            textitem3.text="개수 : "+item3_number.toString()+"개"
        }
        btn_item3_minus.setOnClickListener{
            if(item3_number == 0){
            }
            else item3_number = item3_number.toInt() - 1

            textitem3.text="개수 : "+item3_number.toString()+"개"
        }

        dlg.show()
    }

    fun setOnItemClickedListener(listener: (List<Int>)->Unit){
        this.listenter = object : Dialog_item.ItemDialogClickedListener {
            override fun onItemClicked(content: List<Int>) {
                listener(content)
            }
        }
    }
    interface ItemDialogClickedListener{
        fun onItemClicked(content: List<Int>)
    }

}
