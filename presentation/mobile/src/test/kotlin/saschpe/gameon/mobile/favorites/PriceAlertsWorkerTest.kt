package saschpe.gameon.mobile.favorites

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.*
import androidx.work.testing.TestListenableWorkerBuilder
import androidx.work.testing.WorkManagerTestInitHelper
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import saschpe.gameon.data.core.model.FavoritePriceAlerts
import saschpe.gameon.domain.usecase.GetPriceAlertsUseCase
import java.util.concurrent.TimeUnit
import saschpe.gameon.data.core.Result as CoreResult

@RunWith(AndroidJUnit4::class)
class PriceAlertsWorkerTest {
    private lateinit var context: Context
    private lateinit var worker: PriceAlertsWorker

    private val mockGetPriceAlertsUseCase = mockk<GetPriceAlertsUseCase>()
    private val mockPriceAlertsNotification = mockk<PriceAlertsNotification>()
    private val testWorkerFactory = object : WorkerFactory() {
        override fun createWorker(context: Context, name: String, parameters: WorkerParameters) = PriceAlertsWorker(
            context,
            parameters,
            mockGetPriceAlertsUseCase,
            mockPriceAlertsNotification
        )
    }
    private val testWorkManager by lazy {
        WorkManager.initialize(
            context,
            Configuration.Builder().setWorkerFactory(testWorkerFactory).build()
        )
        WorkManager.getInstance(context)
    }

    @Before
    fun beforeWorker() {
        context = ApplicationProvider.getApplicationContext()
        worker = TestListenableWorkerBuilder<PriceAlertsWorker>(context)
            .setWorkerFactory(testWorkerFactory)
            .build()
    }

    @Test
    fun `doWork notifies when GetPriceAlertsUseCase worked`() = runBlocking {
        // Arrange
        val alerts = FavoritePriceAlerts()
        coEvery { mockGetPriceAlertsUseCase.invoke() } returns CoreResult.Success(alerts)
        coEvery { mockPriceAlertsNotification.notify(any()) } just Runs

        // Act
        val result = worker.doWork()

        // Assert
        coVerify(exactly = 1) { mockPriceAlertsNotification.notify(any()) }
        assertEquals(ListenableWorker.Result.success(), result)
    }

    @Test
    fun `doWork fails when the GetPriceAlertsUseCase fails`() = runBlocking {
        // Arrange
        coEvery { mockGetPriceAlertsUseCase.invoke() } returns CoreResult.Error.withMessage("Testing")

        // Act
        val result = worker.doWork()

        // Assert
        assertEquals(ListenableWorker.Result.failure(), result)
    }

    @Test
    fun `PriceAlertsWorker_enqueue periodically enqueues work`() {
        // Arrange
        val mockWorkManager = mockk<WorkManager>()
        every { mockWorkManager.enqueueUniquePeriodicWork(any(), any(), any()) } returns mockk()

        // Act
        PriceAlertsWorker.enqueuePeriodic(mockWorkManager)

        // Assert
        verify(exactly = 1) {
            mockWorkManager.enqueueUniquePeriodicWork(
                PriceAlertsWorker.WORK_UNIQUE_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                any()
            )
        }
    }

    @Ignore("TestWorkManager casting")
    fun `enqueue once with constraints`() = runBlocking {
        // Arrange
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED).build()
        val request = OneTimeWorkRequestBuilder<PriceAlertsWorker>()
            .setConstraints(constraints).build()
        val testDriver = WorkManagerTestInitHelper.getTestDriver(context)

        // Act
        testWorkManager.enqueue(request).result.get()
        testDriver?.setAllConstraintsMet(request.id)

        // Assert
        val workInfo = testWorkManager.getWorkInfoById(request.id).get()
        assertEquals(WorkInfo.State.SUCCEEDED, workInfo.state)
    }

    @Ignore("Unexecuted runnables")
    fun `enqueue periodically with constraints`() = runBlocking {
        // Arrange
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED).build()
        val request = PeriodicWorkRequestBuilder<PriceAlertsWorker>(
            PriceAlertsWorker.WORK_REPEAT_INTERVAL,
            TimeUnit.MINUTES
        ).setConstraints(constraints).build()
        val testDriver = WorkManagerTestInitHelper.getTestDriver(context)

        // Act
        PriceAlertsWorker.enqueuePeriodic(testWorkManager).result.await()
        testDriver?.setPeriodDelayMet(request.id)

        // Assert
        val workInfo = testWorkManager.getWorkInfoById(request.id).get()
        assertEquals(WorkInfo.State.ENQUEUED, workInfo.state)
    }
}
