package saschpe.gameon.mobile.favorites

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import saschpe.gameon.data.core.model.GameOverview
import saschpe.gameon.mobile.R
import saschpe.gameon.mobile.game.GameFragment

class PriceAlertsNotification(
    private val context: Context
) {
    private val notificationManager by lazy {
        NotificationManagerCompat.from(context).apply { createNotificationChannel() }
    }

    suspend fun notify(alerts: Map<String, GameOverview.Lowest>) =
        withContext(Dispatchers.Default) {
            if (alerts.isNotEmpty()) {
                val notifications = buildNotifications(alerts)
                buildSummaryNotification(notifications.size)?.let {
                    notifications.add(it)
                }

                launch(Dispatchers.Main) {
                    notifications.forEachIndexed { index: Int, notification: Notification ->
                        notificationManager.notify(index, notification)
                    }
                }
            }
        }

    private fun buildNotifications(alerts: Map<String, GameOverview.Lowest>): MutableList<Notification> =
        alerts.mapTo(mutableListOf()) {
            val plain = it.key
            val lowest = it.value

            NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setAutoCancel(true)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setContentIntent(showGamePendingIntent(plain))
                .setContentTitle(context.getString(R.string.price_alert))
                .setContentText(lowest.price_formatted)
                .setGroup(NOTIFICATION_GROUP_KEY)
                .setSmallIcon(R.drawable.ic_notification)
                .build()
        }

    private fun buildSummaryNotification(count: Int): Notification? = when {
        Build.VERSION.SDK_INT < Build.VERSION_CODES.N || count <= 1 -> null
        else -> NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(true)
            .setGroup(NOTIFICATION_GROUP_KEY)
            .setGroupSummary(true)
            .setContentIntent(showFavoritesPendingIntent())
            .setContentTitle(context.getString(R.string.price_alerts_template, count))
            .setSmallIcon(R.drawable.ic_notification)
            .build()
    }

    private fun showGamePendingIntent(plain: String) = NavDeepLinkBuilder(context)
        .setGraph(R.navigation.navigation_main)
        .setDestination(R.id.gameFragment)
        .setArguments(bundleOf(GameFragment.ARG_PLAIN to plain))
        .createPendingIntent()

    private fun showFavoritesPendingIntent() = NavDeepLinkBuilder(context)
        .setGraph(R.navigation.navigation_main)
        .setDestination(R.id.favoritesFragment)
        .createPendingIntent()

    private fun NotificationManagerCompat.createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                context.getString(R.string.price_alerts),
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = context.getString(R.string.price_alerts_description)
            })
        }
    }

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "favorites"
        private const val NOTIFICATION_GROUP_KEY = "favorites_group"
    }
}