package com.example.cathaybk_android_exercise.Controller.Activity

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.cathaybk_android_exercise.R
import com.example.githubusers.Model.Data.User
import com.example.githubusers.Model.Uill.CircleTransform

import com.google.gson.Gson
import com.squareup.picasso.Picasso
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

//主頁
class OneUserActivity : Activity() {

    lateinit var mXX: ImageView
    lateinit var mimgAvatar: ImageView
    lateinit var mnameEEEE: TextView
    lateinit var mimgBadge: ImageView
    lateinit var mTextViewId: TextView
    lateinit var mTextViewlocation: TextView
    lateinit var mTextViewlink: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oneuser)

        mXX = findViewById(R.id.XX)
        mimgAvatar = findViewById(R.id.imgAvatar)
        mnameEEEE = findViewById(R.id.nameEEEE)
        mimgBadge = findViewById(R.id.imgBadge)
        mTextViewId = findViewById(R.id.TextViewId)
        mTextViewlocation = findViewById(R.id.TextViewlocation)
        mTextViewlink = findViewById(R.id.TextViewlink)

        mXX.setOnClickListener(View.OnClickListener() {
            onBackPressed()
        })

        val bundle = intent.extras
        val mID = bundle!!.getString("ID")
        getOneUserForAPI(mID!!)
    }


    fun getOneUserForAPI(name: String) {
        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
            // 實例化一個 Builder
            //加上要發送請求的 API 網址
            //name 為傳入的參數
            .url("https://api.github.com/users" + "/" + name)
            //建立 Request
            .build()
        val call = okHttpClient.newCall(request)

        call.enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                println("fail : $e")
            }
            override fun onResponse(call: Call?, response: Response?) {
                //處理回來的 Response
                val responseStr = response!!.body()!!.string()
                val item = Gson().fromJson(responseStr, User.Response::class.java)

                runOnUiThread {
                    // Stuff that updates the UI
                    mnameEEEE.text = item.name
                    mTextViewId.text = item.login
                    mTextViewlocation.text = item.location
                    mTextViewlink.text =item.blog

                    Picasso.get()
                        .load(item.avatar_url)
                        .transform(CircleTransform())
                        .placeholder(R.drawable.refresh)
                        .error(R.drawable.xx)
                        .into(mimgAvatar)
                }
            }
        })
    }

}