package com.guys_from_301.stock_game

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.guys_from_301.stock_game.data.ProfileDB
import com.kakao.sdk.user.UserApiClient
import org.w3c.dom.Text

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BlankFragment2.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentProfile : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val profileActivityViewModel = ProfileActivityViewModel(_MainActivity!!)
    private lateinit var tv_my_nick : TextView
    var profileDb: ProfileDB? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        profileDb = ProfileDB.getInstace(_MainActivity!!)
        var v : View = inflater.inflate(R.layout.fragment_profile, container, false)
        tv_my_nick = v.findViewById(R.id.tv_my_nick)
        tv_my_nick.text = profileDb?.profileDao()?.getNickname()!!
        v.findViewById<TextView>(R.id.tv_my_level).text = "   레벨 " + profileDb?.profileDao()?.getLevel()!!.toString()

        if(profileDb?.profileDao()?.getLogin()!! != 4){
            v.findViewById<ImageView>(R.id.iv_my_image).visibility=View.INVISIBLE
        }
        else{
            UserApiClient.instance.me { user, error ->
                if (error!=null)
                    Toast.makeText(_MainActivity,"사용자 정보 요청 실패(카카오)", Toast.LENGTH_SHORT)
                else if (user!=null) {
                    Glide.with(_MainActivity!!).load(user?.kakaoAccount?.profile?.thumbnailImageUrl).circleCrop().into(v.findViewById<ImageView>(R.id.iv_my_image))
                }
            }
        }
        v.findViewById<LinearLayout>(R.id.ll_withdraw).setOnClickListener{
            if(profileDb?.profileDao()?.getLogin()!! == 1){
                val dlg_delete = Dialog_DeleteAlert(_MainActivity!!)
                dlg_delete.start()
            }
            else{
                val dlg_delete = Dialog_DeleteKakaoGoogle(_MainActivity!!)
                dlg_delete.start()
            }
        }
        v.findViewById<TextView>(R.id.tv_nickname_change).setOnClickListener{
            val dlg = Dialog_nick(_MainActivity!!, false, profileActivityViewModel)
            dlg.start(profileDb)
        }

        return v
    }
//    override fun onResume() {
//        tv_my_nick.text = profileDb?.profileDao()?.getNickname()!!
//        super.onResume()
//    }
//
//    override fun onPause() {
//        profileActivityViewModel.write2database()
//        super.onPause()
//    }
//
//    override fun onStart() {
//        profileActivityViewModel.refresh()
//        super.onStart()
//    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BlankFragment2.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentProfile().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}