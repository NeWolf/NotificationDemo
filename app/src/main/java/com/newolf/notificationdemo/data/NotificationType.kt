package com.newolf.notificationdemo.data

/**
 * ======================================================================
 *
 *
 * @author : NeWolf
 * @version : 1.0
 * @since :  2024-02-29
 *
 * =======================================================================
 */
@MustBeDocumented
@kotlin.annotation.Retention(AnnotationRetention.BINARY)
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.FIELD,
    AnnotationTarget.LOCAL_VARIABLE
)
annotation class NotificationType {
    companion object {
        /**
         * 基本样式的通知
         */
        const val BASIC_NOTIFICATION: Int = 0

        /**
         * 可展开的通知
         */
        const val EXPANDABLE_NOTIFICATION: Int = 1

        /**
         * 通话样式通知
         */
        const val CALL_STYLE_NOTIFICATION: Int = 2

        /**
         * 具有失效性的通知
         */
        const val TIME_SENSITIVE_NOTIFICATION: Int = 3

        /**
         * 自定义通知
         */
        const val CUSTOM_NOTIFICATION: Int = 4

        /**
         * Add action buttons
         */
        const val BASIC_ADD_ACTION_BUTTONS = 5

        /**
         * Add a direct reply action
         */
        const val BASIC_DIRECT_REPLY = 6

        /**
         * Add a progress bar,style A
         */
        const val BASIC_PROGRESS_BAR_A = 7

        /**
         * Add a progress bar,style B
         */
        const val BASIC_PROGRESS_BAR_B = 8

        /**
         * Set a system-wide category
         */
        const val BASIC_SYSTEM_WIDE_CATEGORY = 9

        /**
         * Show an urgent message
         */
        const val BASIC_URGENT_MESSAGE = 10

        /**
         * Set lock screen visibility
         */
        const val BASIC_LOCK_SCREEN_VISIBILITY = 11

        /**
         * Update a notification
         */
        const val BASIC_UPDATE = 12

        /**
         * Remove a notification
         */
        const val BASIC_REMOVE = 13
    }
}
