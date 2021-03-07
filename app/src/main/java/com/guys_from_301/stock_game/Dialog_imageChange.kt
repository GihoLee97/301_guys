package com.guys_from_301.stock_game


import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.InputFilter
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.guys_from_301.stock_game.retrofit.RetrofitImage
import com.guys_from_301.stock_game.retrofit.setImage
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Dialog_imageChange(context: Context) {
    var mContext: Context? = context
    private val dlg = Dialog(context) //부모 액티비티의 context 가 들어감
    private lateinit var rg_profileImageChange : RadioGroup
    private lateinit var btn_image_change : Button
    private lateinit var ib_cancel : ImageButton
    private lateinit var tv_profileImageChangeNotice : TextView
    private lateinit var rb_image1 : RadioButton
    private lateinit var rb_image2 : RadioButton
    private lateinit var rb_image3 : RadioButton
    private lateinit var rb_image4 : RadioButton
    private lateinit var rb_image5 : RadioButton

    private var isRadioGroupChecked = false


    fun start() {
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE) //타이틀바 제거
        dlg.setContentView(R.layout.dialog_image_change) //다이얼로그에 사용할 xml 파일을 불러옴
        dlg.setCancelable(false) //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함
        dlg.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        //
        rg_profileImageChange = dlg.findViewById(R.id.rg_profileImageChange)
        tv_profileImageChangeNotice = dlg.findViewById(R.id.tv_profileImageChangeNotice)
        btn_image_change = dlg.findViewById(R.id.btn_image_change)
        ib_cancel = dlg.findViewById(R.id.ib_cancel)
        rb_image1 = dlg.findViewById(R.id.rb_image1)
        rb_image2 = dlg.findViewById(R.id.rb_image2)
        rb_image3 = dlg.findViewById(R.id.rb_image3)
        rb_image4 = dlg.findViewById(R.id.rb_image4)
        rb_image5 = dlg.findViewById(R.id.rb_image5)

        isRadioGroupChecked = false

        var _u_imageNumber = 0

        rb_image1.setOnCheckedChangeListener{ group, checked ->
            isRadioGroupChecked = true
            if(checked) {
                _u_imageNumber = 0
                rb_image2.isChecked = false
                rb_image3.isChecked = false
                rb_image4.isChecked = false
                rb_image5.isChecked = false
            }
            btn_image_change.setBackgroundResource(R.drawable.nickname_change_ok_box)
        }
        rb_image2.setOnCheckedChangeListener{ group, checked ->
            isRadioGroupChecked = true
            if(checked) {
                _u_imageNumber = 1
                rb_image1.isChecked = false
                rb_image3.isChecked = false
                rb_image4.isChecked = false
                rb_image5.isChecked = false
            }
            btn_image_change.setBackgroundResource(R.drawable.nickname_change_ok_box)
        }
        rb_image3.setOnCheckedChangeListener{ group, checked ->
            isRadioGroupChecked = true
            if(checked) {
                _u_imageNumber = 2
                rb_image1.isChecked = false
                rb_image2.isChecked = false
                rb_image4.isChecked = false
                rb_image5.isChecked = false
            }
            btn_image_change.setBackgroundResource(R.drawable.nickname_change_ok_box)
        }
        rb_image4.setOnCheckedChangeListener{ group, checked ->
            isRadioGroupChecked = true
            if(checked) {
                _u_imageNumber = 3
                rb_image1.isChecked = false
                rb_image2.isChecked = false
                rb_image3.isChecked = false
                rb_image5.isChecked = false
            }
            btn_image_change.setBackgroundResource(R.drawable.nickname_change_ok_box)
        }
        rb_image5.setOnCheckedChangeListener{ group, checked ->
            isRadioGroupChecked = true
            if(checked) {
                _u_imageNumber = 4
                rb_image1.isChecked = false
                rb_image2.isChecked = false
                rb_image3.isChecked = false
                rb_image4.isChecked = false
            }
            btn_image_change.setBackgroundResource(R.drawable.nickname_change_ok_box)
        }

        rg_profileImageChange.setOnCheckedChangeListener{ group, checkedId ->
            isRadioGroupChecked = true
            Log.d("Giho","checkedId : "+checkedId.toString())
            btn_image_change.setBackgroundResource(R.drawable.nickname_change_ok_box)
        }

        ib_cancel.setOnClickListener {
            dlg.dismiss()
        }

        btn_image_change.setOnClickListener {
            if (!isRadioGroupChecked) {
                tv_profileImageChangeNotice.visibility = View.VISIBLE
            } else {
                Log.d("Giho","imageNum : "+_u_imageNumber.toString())
                profileDbManager!!.setImageNum(_u_imageNumber)
                setImage(getHash(profileDbManager!!.getLoginId()!!),_u_imageNumber)
                dlg.dismiss()
            }
        }
        dlg.show()
    }

}