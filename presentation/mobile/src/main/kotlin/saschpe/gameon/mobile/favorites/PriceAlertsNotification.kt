package saschpe.gameon.mobile.favorites

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.os.bundleOf
import androidx.core.text.HtmlCompat
import androidx.navigation.NavDeepLinkBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import saschpe.gameon.common.Module.colors
import saschpe.gameon.data.core.Result
import saschpe.gameon.data.core.model.GameInfo
import saschpe.gameon.data.core.model.GameOverview
import saschpe.gameon.domain.Module.getGameInfoUseCase
import saschpe.gameon.mobile.R
import saschpe.gameon.mobile.game.GameFragment

class PriceAlertsNotification(
    private val context: Context
) {
    private val notificationManager by lazy {
        NotificationManagerCompat.from(context).apply { createNotificationChannel() }
    }

    suspend fun notify(alerts: Map<String, GameOverview.Price>) =
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

    private suspend fun buildNotifications(alerts: Map<String, GameOverview.Price>) =
        alerts.mapTo(mutableListOf()) {
            val plain = it.key
            val lowest = it.value
            val gameInfo: GameInfo? = withContext(Dispatchers.IO) {
                when (val result = getGameInfoUseCase(plain)) {
                    is Result.Success<HashMap<String, GameInfo>> -> result.data[plain]
                    is Result.Error -> null
                }
            }

            NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setAutoCancel(true)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setColor(colors.primary)
                .setColorized(true)
                .setContentIntent(showGamePendingIntent(plain))
                .setContentText(
                    HtmlCompat.fromHtml(
                        context.getString(
                            R.string.get_it_for_template,
                            lowest.price_formatted,
                            lowest.store
                        ), HtmlCompat.FROM_HTML_MODE_LEGACY
                    )
                )
                .setContentTitle(gameInfo?.title ?: plain)
                .setDeleteIntent(priceAlertDismissedPendingIntent(plain))
                .setGroup(NOTIFICATION_GROUP_KEY)
                .setSmallIcon(R.drawable.ic_notification)
                .build()
        }

    private fun buildSummaryNotification(count: Int): Notification? = when {
        Build.VERSION.SDK_INT < Build.VERSION_CODES.N || count <= 1 -> null
        else -> NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(true)
            .setColor(colors.primary)
            .setColorized(true)
            .setContentIntent(showFavoritesPendingIntent())
            .setGroup(NOTIFICATION_GROUP_KEY)
            .setGroupSummary(true)
            .setSmallIcon(R.drawable.ic_notification)
            .build()
    }

    private fun showGamePendingIntent(plain: String) = NavDeepLinkBuilder(context)
        .setGraph(R.navigation.navigation_main)
        .setDestination(R.id.gameFragment)
        .setArguments(bundleOf(GameFragment.ARG_PLAIN to plain))
        .createPendingIntent()

    private fun priceAlertDismissedPendingIntent(plain: String) = PendingIntent.getBroadcast(
        context, 0,
        Intent(context, PriceAlertDismissedBroadcastReceiver::class.java)
            .putExtra(PriceAlertDismissedBroadcastReceiver.ARG_PLAIN, plain),
        PendingIntent.FLAG_CANCEL_CURRENT
    )

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
                description = context.getString(R.string.price_alert_description)
            })
        }
    }

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "favorites"
        private const val NOTIFICATION_GROUP_KEY = "favorites_group"
    }
}