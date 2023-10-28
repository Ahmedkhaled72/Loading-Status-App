package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    private lateinit var loadingButon: LoadingButton
    private lateinit var radioButto: RadioGroup
    private lateinit var checked:RadioButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))



        radioButto = findViewById(R.id.groub)
        loadingButon = findViewById(R.id.custom_button)

        loadingButon.setOnClickListener {

            if ( radioButto.checkedRadioButtonId == -1)  {
                Toast.makeText(this, "Please Select One To Dowenload", Toast.LENGTH_LONG).show()
                afterClicked()

            }
            else{

                val oneBtn = radioButto.checkedRadioButtonId
                checked = findViewById(oneBtn)
                when(checked){
                  glideButton -> download("https://github.com/bumptech/glide", glideButton.text.toString())
                  repositryButton -> download("https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter",repositryButton.text.toString())
                  retrofitButton -> download("https://github.com/square/retrofit",retrofitButton.text.toString())
                   }
                 }

        }

        notificationManager = ContextCompat.getSystemService(applicationContext, NotificationManager::class.java) as NotificationManager
        createChannel(getString(R.string.chanel_id),
                      getString(R.string.dowenloadChannelName))

    }

    private fun afterClicked()
    {
        Handler().postDelayed({loadingButon.changeStateFun(ButtonState.Completed)}, 3000)
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            val action = intent?.action
            if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
            {
                if(dowenloadStatus() == DownloadManager.STATUS_SUCCESSFUL)
                {
                    loadingButon.changeStateFun(ButtonState.Completed)
                    notificationManager.sendNotification((applicationContext.getString(R.string.notification_description)),
                    applicationContext,"Success", downloadID)
                }else{
                    notificationManager.sendNotification((applicationContext.getString(R.string.notification_description)),
                        applicationContext,"Fail",downloadID)
                }

            }
        }
    }

    private fun download(URL:String, Title:String) {
        val request =
            DownloadManager.Request(Uri.parse(URL))
                .setTitle(Title)
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID = downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    private fun dowenloadStatus(): Int {
        val query = DownloadManager.Query()
        query.setFilterById(downloadID)
        val dowenloadManager  = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        val cursor = dowenloadManager.query(query)
        if(cursor.moveToFirst())
        {
            val columIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
            val status = cursor.getInt(columIndex)
            return status
        }
        return DownloadManager.ERROR_UNKNOWN
    }


    private fun createChannel(channelId: String, channelName:String)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            //create notification chaneel and pass it to NotificationManager
            val notificationChanel =  NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )

            //set
            notificationChanel.enableLights(true)
            notificationChanel.lightColor = Color.RED
            notificationChanel.enableVibration(true)
            notificationChanel.description = "Dowenload file"

            //get instance
            val notificationManager = this.getSystemService(NotificationManager::class.java)

            //pass the channel you created
            notificationManager.createNotificationChannel(notificationChanel)

        }
    }

}
