package saschpe.gameon.mobile.favorites

import android.content.Context
import androidx.work.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import saschpe.gameon.data.core.model.FavoritePriceAlerts
import saschpe.gameon.domain.usecase.GetPriceAlertsUseCase
import saschpe.log4k.Log
import java.util.concurrent.TimeUnit
import saschpe.gameon.data.core.Result as CoreResult

class PriceAlertsWorker(
    appContext: Context, params: WorkerParameters,
    private val getPriceAlertsUseCase: GetPriceAlertsUseCase,
    private val priceAlertsNotification: PriceAlertsNotification
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork() = withContext(Dispatchers.IO) {
        when (val results = getPriceAlertsUseCase()) {
            is CoreResult.Success<FavoritePriceAlerts> -> {
                Log.debug("Successfully loaded new price alerts: ${results.data.alerts}")
                priceAlertsNotification.notify(results.data.alerts)
                Result.success()
            }
            is CoreResult.Error -> {
                Log.error(results.throwable.toString())
                Result.failure()
            }
        }
    }

    companion object {
        private const val WORK_TAG = "favorites"
        internal const val WORK_REPEAT_INTERVAL = 30L
        internal const val WORK_UNIQUE_NAME = "price-alerts"

        private val constraints: Constraints by lazy {
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        }

        fun enqueueOnce(workManager: WorkManager) = workManager.enqueue(
            OneTimeWorkRequestBuilder<PriceAlertsWorker>()
                .setConstraints(constraints).addTag(WORK_TAG).build()
        )

        fun enqueuePeriodic(workManager: WorkManager) = workManager.enqueueUniquePeriodicWork(
            WORK_UNIQUE_NAME, ExistingPeriodicWorkPolicy.KEEP,
            PeriodicWorkRequestBuilder<PriceAlertsWorker>(WORK_REPEAT_INTERVAL, TimeUnit.MINUTES)
                .setConstraints(constraints).addTag(WORK_TAG).build()
        )
    }
}