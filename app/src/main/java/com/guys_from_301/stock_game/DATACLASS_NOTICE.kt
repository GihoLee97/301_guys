package com.guys_from_301.stock_game

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DATACLASS_NOTICE {

    @SerializedName("ID")
    @Expose
    var ID : Int

    @SerializedName("TITLE")
    @Expose
    var TITLE : String

    @SerializedName("CONTENT")
    @Expose
    var CONTENT : String

    @SerializedName("DATE")
    @Expose
    var DATE : String

    constructor(ID: Int, TITLE: String, CONTENT: String, DATE : String) {
        this.ID = ID
        this.TITLE = TITLE
        this.CONTENT = CONTENT
        this.DATE = DATE
    }
}