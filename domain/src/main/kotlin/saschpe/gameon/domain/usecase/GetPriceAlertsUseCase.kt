package saschpe.gameon.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import saschpe.gameon.data.core.Result
import saschpe.gameon.data.core.model.FavoritePriceAlerts
import saschpe.gameon.data.core.model.GameOverview
import saschpe.gameon.data.local.model.FavoriteEntity
import saschpe.gameon.data.local.repository.FavoritesLocalRepository
import saschpe.gameon.data.remote.itad.repository.GameRemoteRepository
import saschpe.gameon.domain.UseCase
import saschpe.gameon.domain.mapper.PRICE_CONVERSION_FACTOR

class GetPriceAlertsUseCase(
    private val favoritesLocalRepository: FavoritesLocalRepository,
    private val gameRemoteRepository: GameRemoteRepository
) : UseCase<Void, FavoritePriceAlerts> {
    override suspend fun invoke(vararg arguments: Void): Result<FavoritePriceAlerts> {
        require(arguments.isEmpty())

        // Load stored favorites...
        when (val getFavoritesResult = withContext(Dispatchers.IO) {
            favoritesLocalRepository.getAll()
        }) {
            is Result.Success<List<FavoriteEntity>> -> {
                val favorites = getFavoritesResult.data
                val priceAlerts = mutableMapOf<String, GameOverview.Price>()

                val plains = favorites.map { it.plain }

                // Check remote 'overview' API for lowest prices on favorites retrieved...
                when (val getOverviewsResult = withContext(Dispatchers.IO) {
                    gameRemoteRepository.overview(plains)
                }) {
                    is Result.Success<GameRemoteRepository.GameOverviewResponse> -> {
                        val overviews = getOverviewsResult.data.data

                        // Add to result if the fresh lowest price is below the user's price threshold..
                        favorites.forEach { favoriteEntity ->
                            val overview = overviews[favoriteEntity.plain]
                            overview?.price?.let { price ->
                                favoriteEntity.priceThreshold?.let { priceThreshold ->
                                    if (price.price < priceThreshold / PRICE_CONVERSION_FACTOR) {
                                        priceAlerts[favoriteEntity.plain] = price
                                    }
                                }
                            }
                        }

                        val favoritePriceAlerts = FavoritePriceAlerts(priceAlerts.toMap())
                        return Result.Success(favoritePriceAlerts)
                    }
                    is Result.Error -> return getOverviewsResult
                }
            }
            is Result.Error -> return getFavoritesResult
        }
    }
}
