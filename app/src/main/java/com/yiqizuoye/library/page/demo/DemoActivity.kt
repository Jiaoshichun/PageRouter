package com.yiqizuoye.library.page.demo

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.yiqizuoye.library.page.R
import com.yiqizuoye.library.page.api.*

var type = 1

class DemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        PageConfig.init(this, PageCreatorImpl())
        PageConfig.setTypeFactory( object : TypeFactory {
            override fun getType(): Int {
                return type
            }
        })
        PageConfig.addGlobalInterceptor(PageInterceptor { pageData, data, otherData ->
            Log.d(
                TAG,
                "addGlobalInterceptor-->pageData:$pageData   data:$data  otherData:$otherData"
            )
            false
        })
        findViewById<Button>(R.id.start).setOnClickListener {
//            for (i in 0..10) {
            val open = PageRouter.create(this, "key", "开始咯").open()
//                Log.d(TAG, "open Result:$open")
//            }
//            val open = PageRouter.create("key2", "222222").open(this)


        }

    }
}