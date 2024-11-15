package saschpe.gameon.mobile

import androidx.work.Configuration
import androidx.work.WorkManager
import com.google.firebase.analytics.FirebaseAnalytics
import saschpe.gameon.common.base.content.AppContentProvider.Companion.applicationContext
import saschpe.gameon.mobile.base.AppWorkerFactory
import saschpe.gameon.mobile.favorites.PriceAlertsNotification

object Module {
    val firebaseAnalytics = FirebaseAnalytics.getInstance(applicationContext)
    val priceAlertsNotification = PriceAlertsNotification(applicationContext)
    val workManager: WorkManager by lazy {
        val configuration = Configuration.Builder().setWorkerFactory(AppWorkerFactory()).build()
        WorkManager.initialize(applicationContext, configuration)
        WorkManager.getInstance(applicationContext)
    }
}
