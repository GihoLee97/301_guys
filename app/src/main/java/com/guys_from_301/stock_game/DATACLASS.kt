package com.guys_from_301.stock_game

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

    @SerializedName("VLAUE1")
    @Expose
    var VALUE1 : Int

    @SerializedName("NICKNAME")
    @Expose
    var NICKNAME : String

    @SerializedName("PROFIT")
    @Expose
    var PROFIT : Int

    @SerializedName("HISTORY")
    @Expose
    var HISTORY : Int

    @SerializedName("LEVEL")
    @Expose
    var LEVEL : Int

    @SerializedName("EXP")
    @Expose
    var EXP : Int

    constructor(USERID: String, PASSWORD: String, DATE: String, MONEY: Int, VALUE1: Int, NICKNAME:String, PROFIT:Int, HISTORY:Int, LEVEL : Int, EXP:Int) {
        this.USERID = USERID
        this.PASSWORD = PASSWORD
        this.DATE = DATE
        this.MONEY = MONEY
        this.VALUE1 = VALUE1
        this.NICKNAME = NICKNAME
        this.PROFIT = PROFIT
        this.HISTORY = HISTORY
        this.LEVEL = LEVEL
        this.EXP = EXP
    }
}