package com.example.myapplication

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DATACLASS {

    @SerializedName("USERID")
    @Expose
    var USERID : String

    @SerializedName("PASSWORD")
    @Expose
    var PASSWORD : String

    @SerializedName("DATE")
    @Expose
    var DATE : String

    @SerializedName("MONEY")
    @Expose
    var MONEY : Int

    @SerializedName("NICKNAME")
    @Expose
    var NICKNAME : String

    @SerializedName("PROFIT")
    @Expose
    var PROFIT : Int

    @SerializedName("HISTORY")
    @Expose
    var HISTORY : String

    @SerializedName("LEVEL")
    @Expose
    var LEVEL : Int

    @SerializedName("EXP")
    @Expose
    var EXP : Int

    constructor(USERID: String, PASSWORD: String, DATE: String, MONEY: Int, NICKNAME:String, PROFIT:Int, HISTORY:String, LEVEL : Int, EXP:Int) {
        this.USERID = USERID
        this.PASSWORD = PASSWORD
        this.DATE = DATE
        this.MONEY = MONEY
        this.NICKNAME = NICKNAME
        this.PROFIT = PROFIT
        this.HISTORY = HISTORY
        this.LEVEL = LEVEL
        this.EXP = EXP
    }
}