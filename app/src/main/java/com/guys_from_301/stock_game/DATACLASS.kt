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

    @SerializedName("VALUE1")
    @Expose
    var VALUE1 : Int

    @SerializedName("NICKNAME")
    @Expose
    var NICKNAME : String

    @SerializedName("PROFITRATE")
    @Expose
    var PROFITRATE : Float

    @SerializedName("RELATIVEPROFITRATE")
    @Expose
    var RELATIVEPROFITRATE : Float

    @SerializedName("ROUNDCOUNT")
    @Expose
    var ROUNDCOUNT : Int

    @SerializedName("HISTORY")
    @Expose
    var HISTORY : Int

    @SerializedName("LEVEL")
    @Expose
    var LEVEL : Int

    @SerializedName("EXP")
    @Expose
    var EXP : Int

    @SerializedName("QUEST")
    @Expose
    var QUEST : Int

    @SerializedName("QUEST2")
    @Expose
    var QUEST2 : Int

    @SerializedName("POTION")
    @Expose
    var POTION : Int

    @SerializedName("SETCASH")
    @Expose
    var SETCASH : Float

    @SerializedName("SETMONTHLY")
    @Expose
    var SETMONTHLY : Float

    @SerializedName("SETSALARYRAISE")
    @Expose
    var SETSALARYRAISE : Float

    @SerializedName("IMAGENUMBER")
    @Expose
    var IMAGENUMBER : Int

    @SerializedName("OVERWRITE")
    @Expose
    var OVERWRITE : Int



    constructor(USERID: String, PASSWORD: String, DATE: String, MONEY: Int, VALUE1: Int, NICKNAME:String,
                PROFITRATE:Float, RELATIVEPROFITRATE:Float,ROUNDCOUNT:Int, HISTORY:Int, LEVEL : Int, EXP:Int,
                QUEST:Int, QUEST2:Int, POTION:Int, SETCASH : Float, SETMONTHLY:Float, SETSALARYRAISE:Float,
                IMAGENUMBER : Int, OVERWRITE : Int) {
        this.USERID = USERID
        this.PASSWORD = PASSWORD
        this.DATE = DATE
        this.MONEY = MONEY
        this.VALUE1 = VALUE1
        this.NICKNAME = NICKNAME
        this.PROFITRATE = PROFITRATE
        this.RELATIVEPROFITRATE = RELATIVEPROFITRATE
        this.ROUNDCOUNT = ROUNDCOUNT
        this.HISTORY = HISTORY
        this.LEVEL = LEVEL
        this.EXP = EXP
        this.QUEST = QUEST
        this.QUEST2 = QUEST2
        this.POTION = POTION
        this.SETCASH = SETCASH
        this.SETMONTHLY = SETMONTHLY
        this.SETSALARYRAISE = SETSALARYRAISE
        this.IMAGENUMBER = IMAGENUMBER
        this.OVERWRITE = OVERWRITE
    }
}