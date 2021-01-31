//package com.example.myapplication
//
//import android.R.attr.name
//import com.google.gson.annotations.Expose
//import com.google.gson.annotations.SerializedName
//
//// 서버에서 데이터 받아올 때 필요한
//class RetrofitData {
//    @Expose
//    @SerializedName("u_id")
//    var u_id: String = ""
//
//    @Expose
//    @SerializedName("u_pw")
//    var u_pw: String = ""
//
//    @Expose
//    @SerializedName("u_date")
//    var u_date: String = ""
//
//    fun get_id(): String {
//        return u_id
//    }
//    fun get_pw(): String {
//        return u_pw
//    }
//    fun get_date(): String{
//        return u_date
//    }
//
//}