package com.example.cathaybk_android_exercise.Controller.Activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
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
    lateinit var LinearLayout: LinearLayout

    var TAG: String = "MainActivity"
    var scrollFlag = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        itemViewer = findViewById(R.id.itemViewer)
        LinearLayout = findViewById(R.id.LinearLayout)
        getAllUserForAPI()

        var newVisibleItem: Int = 0
        var firsX: Float = 0.0F
        var oldX: Float = 0.0F

//        itemViewer.isEnabled = false


        itemViewer.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent): Boolean {
                Log.d("event.action::", "event.action" + event.action)
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        Log.d(TAG, "ACTION_DOWN")
                        oldX = event.y
                    }
                    MotionEvent.ACTION_MOVE -> {
                        if (oldX == 0.0F) {
                            oldX = event.y
                        }
                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        firsX = event.y
                        Log.d(TAG, "oldX" + oldX + "|||firsX" + firsX+"|||newVisibleItem"+newVisibleItem)
                        if (oldX > firsX && oldX - firsX > 5.0F && newVisibleItem + 5 < itemViewer.getCount()) {
                            // 向上滑动
                            newVisibleItem = newVisibleItem + 5
                            itemViewer.setSelection(newVisibleItem)

                        }
                        if (oldX < firsX && firsX - oldX > 5.0F &&newVisibleItem - 5 >= 0) {
                            // 向下滑动
                            newVisibleItem = newVisibleItem - 5
                            itemViewer.setSelection(newVisibleItem)


                        }
                        oldX = 0.0F
                    }

                    else -> {
                        return false
                    }

                }
                return false
            }
        })

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