package saschpe.gameon.domain.mapper

import saschpe.gameon.data.core.model.Favorite
import saschpe.gameon.data.local.model.FavoriteEntity

fun FavoriteEntity.toFavorite() = Favorite(
    id = id,
    createdAt = createdAt,
    plain = plain,
    priceThreshold = priceThreshold?.let { it.toDouble() / PRICE_CONVERSION_FACTOR },
    dismissed = dismissed
)

fun Favorite.toFavoriteEntity() = FavoriteEntity(
    id = id ?: 0,
    createdAt = createdAt,
    plain = plain,
    priceThreshold = priceThreshold?.let { (it * PRICE_CONVERSION_FACTOR).toLong() },
    dismissed = dismissed
)

const val PRICE_CONVERSION_FACTOR = 100
