package com.guys_from_301.stock_game

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Dataclass_kakao {

    @SerializedName("NAME")
    @Expose
    var NAME : String

    @SerializedName("NICKNAME")
    @Expose
    var NICKNAME : String

    @SerializedName("ID")
    @Expose
    var ID : String

    @SerializedName("MONEY")
    @Expose
    var MONEY : Int

    @SerializedName("LEVEL")
    @Expose
    var LEVEL : Int

    @SerializedName("IMAGE")
    @Expose
    var IMAGE : String

    constructor(NAME :String, NICKNAME:String, ID:String, MONEY:Int, LEVEL:Int, IMAGE :String) {
        this.NAME = NAME
        this.NICKNAME = NICKNAME
        this.ID = ID
        this.MONEY = MONEY
        this.LEVEL = LEVEL
        this.IMAGE = IMAGE
    }
}