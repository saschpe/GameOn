package saschpe.gameon.mobile.base

import android.content.Context
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import saschpe.gameon.domain.Module.getPriceAlertsUseCase
import saschpe.gameon.mobile.Module.priceAlertsNotification
import saschpe.gameon.mobile.favorites.PriceAlertsWorker

internal class AppWorkerFactory : WorkerFactory() {
    override fun createWorker(
        appContext: Context, workerClassName: String, workerParameters: WorkerParameters
    ) = when (workerClassName) {
        PriceAlertsWorker::class.java.name -> PriceAlertsWorker(
            appContext, workerParameters, getPriceAlertsUseCase, priceAlertsNotification
        )
        else -> throw IllegalArgumentException("$workerClassName is not supported.")
    }
}