package com.newolf.notificationdemo.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.IInterface
import android.os.Parcel
import java.io.FileDescriptor

class NotificationListenerService : Service() {

    override fun onBind(intent: Intent): IBinder {
        return object : IBinder {
            override fun getInterfaceDescriptor(): String {
                return "NotificationListenerService"
            }

            override fun pingBinder(): Boolean {
                TODO("Not yet implemented")
            }

            override fun isBinderAlive(): Boolean {
                TODO("Not yet implemented")
            }

            override fun queryLocalInterface(descriptor: String): IInterface? {
                TODO("Not yet implemented")
            }

            override fun dump(fd: FileDescriptor, args: Array<out String>?) {
                TODO("Not yet implemented")
            }

            override fun dumpAsync(fd: FileDescriptor, args: Array<out String>?) {
                TODO("Not yet implemented")
            }

            override fun transact(code: Int, data: Parcel, reply: Parcel?, flags: Int): Boolean {
                TODO("Not yet implemented")
            }

            override fun linkToDeath(recipient: IBinder.DeathRecipient, flags: Int) {
                TODO("Not yet implemented")
            }

            override fun unlinkToDeath(recipient: IBinder.DeathRecipient, flags: Int): Boolean {
                TODO("Not yet implemented")
            }
        }
    }
}