package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.opencsv.CSVReader
import com.opencsv.CSVReaderBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


val snp_date: ArrayList<String> = ArrayList() // S&P500 날짜
val snp_val: ArrayList<String> = ArrayList() // S&P500 지수 값
val snp_vol: ArrayList<String> = ArrayList() // S&P500 거래량
val fund_date: ArrayList<String> = ArrayList() // Fed Fund Rate 날짜
val fund_val: ArrayList<String> = ArrayList() // Fed Fund Rate 값
val bond_date: ArrayList<String> = ArrayList() // 10 Year Treasury Bond Rate 날짜
val bond_val: ArrayList<String> = ArrayList() // 10 Year Treasury Bond Rate 값
val indpro_date: ArrayList<String> = ArrayList() // 미국 산업 생산량 날짜
val indpro_val: ArrayList<String> = ArrayList() // 미국 산성 생산량 값
val unem_date: ArrayList<String> = ArrayList() // 미국 실업률 날짜
val unem_val: ArrayList<String> = ArrayList() // 미국 실업률 값
val inf_date: ArrayList<String> = ArrayList() // Inflation rate 날짜
val inf_val: ArrayList<String> = ArrayList() // Inflation rate 값
var loadcomp: Int = 0 // 데이터 로드 완료 여부(미완료:0, 완료:1)


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val profile1_btn = findViewById<Button>(R.id.profile1_btn)
        profile1_btn.setOnClickListener{
            val intent = Intent(this,ProfileActivity::class.java)
            startActivity(intent)
        }

        val setting_btn = findViewById<Button>(R.id.setting_btn)
        setting_btn.setOnClickListener {
            val intent = Intent(this,SettingActivity::class.java)
            startActivity(intent)
        }

        val game_btn = findViewById<Button>(R.id.game_btn)
        game_btn.setOnClickListener{
            val intent = Intent(this,GameNormalActivity::class.java)
            startActivity(intent)
        }

        // 게임 데이터 로드
        GlobalScope.launch(Dispatchers.Default) {

            var fileReader: BufferedReader? = null
            var csvReader: CSVReader? = null
            var count = 0

            // 모든 금융 데이터 csv 파일들은 \app\src\main\assets 에 저장
            // 금융 데이터 업데이트 시 해당 파일들 만을 최신화, 나머지는 모두 자동화

            ///////////////////////////////////////////////////////////////////////////////////////
            // "^GSPC.csv" 파일로 부터 S&P500 historical data 추출
            try {
                //println("\n--- S&P 500 ---")

                fileReader = BufferedReader(InputStreamReader(getAssets().open("^GSPC.csv")))
                // 1950-01-03 (헤더 포함 5498번쨰 행)부터 거래량 정보 유효(이전은 0, null 아님).
                // 헤더 스킵 및 1962-01-02 (헤더 포함 8511번쨰 행)부터 다른 모든 데이터 유효(bondrate).
                csvReader = CSVReaderBuilder(fileReader).withSkipLines(8510).build()

                val snp_rs = csvReader.readAll()
                count = 0
                for (snp_r in snp_rs) {
                    // snp500에 데이터 추가
                    snp_date.add(count, snp_r[0])
                    snp_val.add(count, snp_r[4])
                    snp_vol.add(count, snp_r[6])
                    count += 1

                    // 입력 확인
                    println("[SNP] 날짜 : " + snp_date[count - 1] + " | " + "값 : " + snp_val[count - 1] + " | " + "거래량 : " + snp_vol[count - 1] + " | " + "COUNT = $count")
                    // [0]: Date, [1]: Open, [2]: High, [3]: Low, [4]: Close, [5]: Adj Close, [6]: Volume
                    // 1950-01-03 (헤더 포함 5498번쨰 행)부터 거래량 정보 유효(이전은 0, null 아님).
                    // Open, High, Low, Close 값은 1967-06-30 까지 동일, 1967-07-03 (헤더 포함 9897번째 행)부터 세분화 되어 각각의 값이 달라짐.
                    // 참고) 2021-01-27 까지의 데이터 수(거래일)는 총 23380-1개
                }
            } catch (e: Exception) {
                println("Reading CSV Error : SNP !") // 에러 메시지 출력
                println("[SNP] size : " + snp_date.size.toString())
                e.printStackTrace()
            } finally {
                try {
                    fileReader!!.close()
                    csvReader!!.close()
                    println("[SNP] size : " + snp_date.size.toString())
                    count = 0
                } catch (e: IOException) {
                    println("Closing fileReader/csvParser Error : SNP !") // 에러 메시지 출력
                    e.printStackTrace()
                    count = 0
                }
            }

            ///////////////////////////////////////////////////////////////////////////////////////
            // "fed-funds-rate-historical-chart.csv" 파일로 부터 FED Fund Rate historical data 추출
            try {
                println("\n--- FED Fund Rate ---")

                fileReader = BufferedReader(InputStreamReader(getAssets().open("fed-funds-rate-historical-chart.csv")))
                // 헤더 스킵 및 1962-01-02 (헤더 포함 2759번쨰 행)부터 다른 모든 데이터 유효(bondrate).
                csvReader = CSVReaderBuilder(fileReader).withSkipLines(2758).build()

                val fundrate_rs = csvReader.readAll()
                for (fundrate_r in fundrate_rs) {
                    fund_date.add(count, fundrate_r[0])
                    fund_val.add(count, fundrate_r[1])
                    count += 1

                    //입력 확인
                    println("[FundRate] 날짜 : " + fund_date[count - 1] + " | " + "값 : " + fund_val[count - 1])
                    // [0]: Date, [1]: value
                    // 1954-07-01 (헤더 포함 17번쨰 행)부터 정보 유효.
                    // 이후 일별 데이터(매일 O, 거래일 X)
                    // 참고) 2021-02-17 까지의 데이터 수는 24048-16개
                    // CSV 파일 끝부분에 날짜는 존재하나 이율 값이 null 인 경우가 있음(# of null 행 < 25)
                }
            } catch (e: Exception) {
                println("Reading CSV Error : Fund Rate !") // 에러 메시지 출력
                println("[FundRate] size : " + fund_date.size.toString())
                e.printStackTrace()
            } finally {
                try {
                    fileReader!!.close()
                    csvReader!!.close()
                    println("[FundRate] size : " + fund_date.size.toString())
                    count = 0
                } catch (e: IOException) {
                    println("Closing fileReader/csvParser Error : !") // 에러 메시지 출력
                    e.printStackTrace()
                    count = 0
                }
            }

            ///////////////////////////////////////////////////////////////////////////////////////
            // "10-year-treasury-bond-rate-yield-chart.csv" 파일로 부터 미 국채 10년물 이율 historical data 추출
            try {
                println("\n--- 미 국채 10년물 이율 ---")

                fileReader = BufferedReader(InputStreamReader(getAssets().open("10-year-treasury-bond-rate-yield-chart.csv")))
                // 헤더 스킵(16행)
                csvReader = CSVReaderBuilder(fileReader).withSkipLines(16).build()

                val bondrate_rs = csvReader.readAll()
                for (bondrate_r in bondrate_rs) {
                    bond_date.add(count, bondrate_r[0])
                    bond_val.add(count, bondrate_r[1])
                    count += 1

                    //입력 확인
                    println("[BondRate] 날짜 : " + bond_date[count - 1] + " | " + "값 : " + bond_val[count - 1])
                    // [0]: Date, [1]: value
                    // 1962-01-02 (헤더 포함 17번쨰 행)부터 정보 유효.
                    // 이후 일별 데이터(거래일)
                    // 참고) 2021-01-27 까지의 데이터 수는 14786-16개
                    // CSV 파일 끝부분에 날짜는 존재하나 이율 값이 null 인 경우가 있음(# of null 행 < 25)
                }
            } catch (e: Exception) {
                println("Reading CSV Error : BondRate !") // 에러 메시지 출력
                println("[Bondrate] size : " + bond_date.size.toString())
                e.printStackTrace()
            } finally {
                try {
                    fileReader!!.close()
                    csvReader!!.close()
                    println("[Bondrate] size : " + bond_date.size.toString())
                    count = 0
                } catch (e: IOException) {
                    println("Closing fileReader/csvParser Error : BondRate !") // 에러 메시지 출력
                    e.printStackTrace()
                    count = 0
                }
            }

            ///////////////////////////////////////////////////////////////////////////////////////
            // "industrial-production-historical-chart.csv" 파일로 부터 Industrial production historical data 추출
            try {
                println("\n--- Industrial production ---")

                fileReader = BufferedReader(InputStreamReader(getAssets().open("industrial-production-historical-chart.csv")))
                // 헤더 스킵 및 1962-01-02 (헤더 포함 521번쨰 행)부터 다른 모든 데이터 유효(bondrate).
                // 521행은 1962-01-01
                csvReader = CSVReaderBuilder(fileReader).withSkipLines(520).build()

                val indpro_rs = csvReader.readAll()
                for (indpro_r in indpro_rs) {
                    indpro_date.add(count, indpro_r[0])
                    indpro_val.add(count, indpro_r[1])
                    count += 1

                    //입력 확인
                    println("[IndPro] 날짜 : " + indpro_date[count - 1] + " | " + "값 : " + indpro_val[count - 1])
                    // [0]: Date, [1]: value
                    // 1919-01-01 (헤더 포함 17번쨰 행)부터 정보 유효.
                    // 이후 월별 데이터(매월 1일)
                    // 참고) 2020-12-01 까지의 데이터 수는 1240-16개
                    // null 없음
                }
            } catch (e: Exception) {
                println("Reading CSV Error : IndPro !") // 에러 메시지 출력
                println("[IndPro] size : " + indpro_date.size.toString())
                e.printStackTrace()
            } finally {
                try {
                    fileReader!!.close()
                    csvReader!!.close()
                    println("[IndPro] size : " + indpro_date.size.toString())
                    count = 0
                } catch (e: IOException) {
                    println("Closing fileReader/csvParser Error : IndPro !") // 에러 메시지 출력
                    e.printStackTrace()
                    count = 0
                }
            }

            ///////////////////////////////////////////////////////////////////////////////////////
            // "us-national-unemployment-rate.csv" 파일로 부터 S&P500 historical data 추출
            try {
                println("\n--- Us National Unemployment Rate ---")

                fileReader = BufferedReader(InputStreamReader(getAssets().open("us-national-unemployment-rate.csv")))
                // 헤더 스킵 및 1962-01-02 (헤더 포함 185번쨰 행)부터 다른 모든 데이터 유효(bondrate).
                // 185행은 1962-01-01
                csvReader = CSVReaderBuilder(fileReader).withSkipLines(184).build()

                val unem_rs = csvReader.readAll()
                for (unem_r in unem_rs) {
                    unem_date.add(count, unem_r[0])
                    unem_val.add(count, unem_r[1])
                    count += 1

                    //입력 확인
                    println("[UnEm] 날짜 : " + unem_date[count - 1] + " | " + "값 : " + unem_val[count - 1])
                    // [0]: Date, [1]: value
                    // 1948-01-01 (헤더 포함 17번쨰 행)부터 정보 유효.
                    // 이후 월별 데이터(표기상 매월 1일)
                    // 참고) 2020-12-01 까지의 데이터 수는 892-16개
                }
            } catch (e: Exception) {
                println("Reading CSV Error : UnEm !") // 에러 메시지 출력
                println("[UnEm] size : " + unem_date.size.toString())
                e.printStackTrace()
            } finally {
                try {
                    fileReader!!.close()
                    csvReader!!.close()
                    println("[UnEm] size : " + unem_date.size.toString())
                    count = 0
                } catch (e: IOException) {
                    println("Closing fileReader/csvParser Error : UnEm !") // 에러 메시지 출력
                    e.printStackTrace()
                    count = 0
                }
            }

            ///////////////////////////////////////////////////////////////////////////////////////
            // "historical-inflation-rate-by-year.csv" 파일로 부터 Inflation rate historical data 추출
            try {
                println("\n--- Inflation rate ---")

                fileReader = BufferedReader(InputStreamReader(getAssets().open("historical-inflation-rate-by-year.csv")))
                // 헤더 스킵 및 1962-01-02 (헤더 포함 63번쨰 행)부터 다른 모든 데이터 유효(bondrate).
                // 521행은 1961-12-01
                csvReader = CSVReaderBuilder(fileReader).withSkipLines(62).build()

                val infrate_rs = csvReader.readAll()
                for (infrate_r in infrate_rs) {
                    inf_date.add(count, infrate_r[0])
                    inf_val.add(count, infrate_r[1])
                    count += 1

                    //입력 확인
                    println("[InfRate] 날짜 : " + inf_date[count - 1] + " | " + "값 : " + inf_val[count - 1])
                    // [0]: Date, [1]: value
                    // 1914-12-01 (헤더 포함 17번쨰 행)부터 정보 유효.
                    // 이후 년별 데이터(표기상 매년 12월 1일)
                    // 참고) 2020-12-01 까지의 데이터 수는 123-16개
                    // CSV 파일 마지막 행은 유효하지 않은 데이터(null 은 아님)
                }
            } catch (e: Exception) {
                println("Reading CSV Error : InfRate !") // 에러 메시지 출력
                println("[InfRate] size : " + inf_date.size.toString())
                e.printStackTrace()
            } finally {
                try {
                    fileReader!!.close()
                    csvReader!!.close()
                    println("[InfRate] size : " + inf_date.size.toString())
                    count = 0
                } catch (e: IOException) {
                    println("Closing fileReader/csvParser Error : InfRate !") // 에러 메시지 출력
                    e.printStackTrace()
                    count = 0
                }
            }
            println("Data Load Complete!") // 데이터 입력 종료

            loadcomp = 1
        }



    }

    // 두번 누르면 종료되는 코드
    var time3: Long = 0
    override fun onBackPressed() {
        val time1 = System.currentTimeMillis()
        val time2 = time1 - time3
        if (time2 in 0..2000) {
            // 이거 3줄 다 써야 안전하게 종료
            moveTaskToBack(true)
            finish()
            android.os.Process.killProcess(android.os.Process.myPid())
        }
        else {
            time3 = time1
            Toast.makeText(applicationContext, "한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show()
        }
    }
}
