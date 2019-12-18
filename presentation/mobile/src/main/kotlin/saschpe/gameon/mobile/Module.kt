package saschpe.gameon.mobile

import androidx.work.Configuration
import androidx.work.WorkManager
import saschpe.gameon.mobile.base.AppContentProvider.Companion.applicationContext
import saschpe.gameon.mobile.base.AppWorkerFactory
import saschpe.gameon.mobile.favorites.PriceAlertsNotification

object Module {
    val priceAlertsNotification = PriceAlertsNotification(applicationContext)
    val workManager: WorkManager by lazy {
        WorkManager.initialize(
            applicationContext,
            Configuration.Builder().setWorkerFactory(AppWorkerFactory()).build()
        )
        WorkManager.getInstance(applicationContext)
    }
}