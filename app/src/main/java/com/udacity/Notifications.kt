package com.udacity

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

private val Notification_ID = 0

@SuppressLint("WrongConstant")
fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context, status:String, dowenloadId:Long) {

    val content = Intent(applicationContext, DetailActivity::class.java)
    content.putExtra("status", status)
    content.putExtra("DowenloadId", dowenloadId)

    //create Pending
    val pendingIntent = PendingIntent.getActivity(
        applicationContext,
        Notification_ID,
        content,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val action = NotificationCompat.Action.Builder(0,"Show",pendingIntent).build()


    //Builder
    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.chanel_id)
    )

        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentTitle("Dowenload Complete")
        .setContentText(messageBody)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .addAction(action)

    notify(Notification_ID, builder.build())

}