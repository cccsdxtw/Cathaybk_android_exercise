package com.example.cathaybk_android_exercise.Controller.Activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.cathaybk_android_exercise.Controller.Adapter.UsersListAdapter
import com.example.cathaybk_android_exercise.R
import com.example.githubusers.Model.Data.Users
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException


//主頁
class MainActivity : AppCompatActivity() {

    lateinit var itemViewer: ListView
    var TAG: String ="MainActivity"
    var scrollFlag = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        itemViewer = findViewById(R.id.itemViewer)
        getAllUserForAPI()

    }

    protected fun getAllUserForAPI() {
        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
            // 實例化一個 Builder
            //加上要發送請求的 API 網址
            //name 為傳入的參數
            .url("https://api.github.com/users")
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
//                Log.d(TAG,"API取得數據="+responseStr);
                val users: Array<Users.Response> = Gson().fromJson(
                    responseStr,
                    Array<Users.Response>::class.java
                )
                Log.d("GetAllUserForAPI::", "users:" + users)
                Log.d("GetAllUserForAPI::", "users:" + users[1].login)
                changeList(itemViewer, users)
            }
        })


    }
    private fun changeList(name: ListView, itemList: Array<Users.Response>) {
        runOnUiThread {
            // Stuff that updates the UI
            val mListAdapter: UsersListAdapter = UsersListAdapter()
            mListAdapter.UsersListAdapter(this, this, itemList)
            name.setAdapter(mListAdapter)
            mListAdapter.notifyDataSetChanged()
        }
    }



}