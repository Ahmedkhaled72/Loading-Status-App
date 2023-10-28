package com.udacity

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    private lateinit var okButton: Button
    private lateinit var fileText: TextView
    private lateinit var statusText: TextView


    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        okButton = findViewById(R.id.mianButton)
        fileText = findViewById(R.id.file_Disc)
        statusText = findViewById(R.id.file_StatusA)

        val status = intent.getStringExtra("status")
        val  dowenloadId:Long = intent.getLongExtra("DowenloadId",0L)

        fun DowenloadDescription():String
        {
            val query = DownloadManager.Query()
            query.setFilterById(dowenloadId)
            val dowenloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            val cursor = dowenloadManager.query(query)

            if(cursor.moveToFirst())
            {
                val description = cursor.getString(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TITLE))
                return  description
            }
            cursor.close()
            return DownloadManager.ERROR_UNKNOWN.toString()
        }

        fileText.text = DowenloadDescription()
        statusText.text = status

        okButton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

}
