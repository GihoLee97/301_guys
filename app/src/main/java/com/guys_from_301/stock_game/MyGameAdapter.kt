package com.guys_from_301.stock_game

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.guys_from_301.stock_game.data.GameNormalDB
import com.guys_from_301.stock_game.data.GameSet

class MyGameAdapter (private val context: Context, private val game: ArrayList<GameSet>, val itemClick: (GameSet) -> Unit): PagerAdapter() {

    private var gamenormalDb: GameNormalDB? = null
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return game.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = LayoutInflater.from(context).inflate(R.layout.recyclerview_games_item, container, false)

        val gameUnit = game[position]
        val endtime = itemView.findViewById<TextView>(R.id.endtime)
        val gameName = itemView.findViewById<TextView>(R.id.gameName)
        val profitRate = itemView.findViewById<TextView>(R.id.profitrate)
        val lc_chart = itemView.findViewById<LineChart>(R.id.lc_chart)
        val tv_addGame = itemView.findViewById<TextView>(R.id.tv_addGame)
        val tv_currentProfitTitle = itemView.findViewById<TextView>(R.id.tv_currentProfitTitle)
        itemView.layoutParams.width = (dpWidth - (60/540.0*dpWidth).toInt())*2
        if(gameUnit.endtime == "") {
            lc_chart.visibility = View.GONE
            tv_addGame.visibility = View.VISIBLE
            endtime.text = ""
            gameName.text = "새로운 게임 만들기"
            profitRate.text = ""
            tv_currentProfitTitle.text = "최대 3개까지 생성 가능"
        }
        else {
            lc_chart.visibility = View.VISIBLE
            tv_addGame.visibility = View.GONE

            fun assetchart(gameUnit : GameSet) {
                gamenormalDb = GameNormalDB.getInstace(context)

                // 자산 차트 데이터
                val assetEn: ArrayList<Entry> = ArrayList()
                val inputEn: ArrayList<Entry> = ArrayList()

                val idtemp = gameUnit.id

                val assetMonthly = gamenormalDb?.gameNormalDao()?.getAsset(accountID!!, idtemp)
                val inputMonthly = gamenormalDb?.gameNormalDao()?.getInput(accountID!!, idtemp)

                if (assetMonthly != null && inputMonthly != null) {
                    for (i in assetMonthly.indices) {
                        assetEn.add(Entry((i + 1).toFloat(), assetMonthly[i]))
                        inputEn.add(Entry((i + 1).toFloat(), inputMonthly[i]))
                        println("월: " + i.toString() + " | 자산: " + assetMonthly[i] + " | 인풋: " + inputMonthly[i])
                    }
                }

                val assetDs: LineDataSet = LineDataSet(assetEn, "asset")
                val inputDs: LineDataSet = LineDataSet(inputEn, "input")

                val resultDs: ArrayList<ILineDataSet> = ArrayList()

                resultDs.add(inputDs)
                resultDs.add(assetDs)

                val resultD: LineData = LineData(resultDs)

                lc_chart.data = resultD

                lc_chart.isClickable = false

                // 차트 레이아웃 설정
                val legend_result : Legend = lc_chart.legend
                val x_result : XAxis = lc_chart.xAxis
                val yl_result : YAxis = lc_chart.getAxis(YAxis.AxisDependency.LEFT)
                val yr_result : YAxis = lc_chart.getAxis(YAxis.AxisDependency.RIGHT)

                lc_chart.isClickable = false
                lc_chart.description.isEnabled = false

                legend_result.isEnabled = true
                x_result.isEnabled = false
                yl_result.isEnabled = false
                yr_result.isEnabled = false

                assetDs.color = Color.parseColor("#F4730B") // 차트 선
                assetDs.setDrawCircles(false)
                assetDs.setDrawValues(false) // 차트 지점마다 값 표시
                assetDs.lineWidth = 1.5F
                assetDs.highLightColor = Color.parseColor("#B71C1C") // 터치 시 하이라이트
                assetDs.highlightLineWidth = 1F

                inputDs.color = Color.parseColor("#B7B1B3") // 차트 선
                inputDs.setDrawCircles(false)
                inputDs.setDrawValues(false) // 차트 지점마다 값 표시
                inputDs.lineWidth = 1.5F
                inputDs.highLightColor = Color.parseColor("#B71C1C") // 터치 시 하이라이트
                inputDs.highlightLineWidth = 1F

                // 차트 생성
                lc_chart.animateXY(1, 1)
                lc_chart.notifyDataSetChanged()
                resultD.notifyDataChanged()
            }

            // 차트
            assetchart(gameUnit)

            //endtime gamenormal 기준
            if(gameUnit.endtime.length>10)endtime.text = gameUnit.endtime.slice(IntRange(0,3))+"."+gameUnit.endtime.slice(IntRange(5,6))+"."+gameUnit.endtime.slice(IntRange(8,9))+" "+gameUnit.endtime.slice(IntRange(11,15))
            else endtime.text = gameUnit.endtime
            gameName.text = "투자공간" + gameUnit.id.last()
            profitRate.text = per.format(gameUnit.profitrate).toString()+"%"
        }
        itemView.setOnClickListener{ itemClick(gameUnit) }


        container.addView(itemView)

        return itemView
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}