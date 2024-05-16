package com.newolf.notificationdemo.adapter

import com.newolf.library.adapt.base.BaseQuickAdapter
import com.newolf.library.adapt.base.viewholder.BaseViewHolder
import com.newolf.notificationdemo.R
import com.newolf.notificationdemo.data.NotificationBean

/**
 * ======================================================================
 *
 *
 * @author : NeWolf
 * @version : 1.0
 * @since :  2024-03-01
 *
 * =======================================================================
 */
class NotificationTypeAdapter :
    BaseQuickAdapter<NotificationBean, BaseViewHolder>(R.layout.adapter_notification_type) {
    override fun convert(holder: BaseViewHolder, item: NotificationBean) {
        holder.setText(R.id.tv_show, item.showItem)
    }
}