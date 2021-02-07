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

    @SerializedName("ITEM1")
    @Expose
    var ITEM1 : Int

    @SerializedName("ITEM2")
    @Expose
    var ITEM2 : Int

    @SerializedName("ITEM3")
    @Expose
    var ITEM3 : Int

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

    constructor(USERID: String, PASSWORD: String, DATE: String, MONEY: Int, ITEM1: Int, ITEM2: Int, ITEM3: Int, NICKNAME:String, PROFIT:Int, HISTORY:String, LEVEL : Int) {
        this.USERID = USERID
        this.PASSWORD = PASSWORD
        this.DATE = DATE
        this.MONEY = MONEY
        this.ITEM1 = ITEM1
        this.ITEM2 = ITEM2
        this.ITEM3 = ITEM3
        this.NICKNAME = NICKNAME
        this.PROFIT = PROFIT
        this.HISTORY = HISTORY
        this.LEVEL = LEVEL
    }
}