package saschpe.gamesale.data.remote

import saschpe.gamesale.data.remote.repository.DealsRemoteRepository
import saschpe.gamesale.data.remote.repository.GameRemoteRepository
import saschpe.gamesale.data.remote.repository.SearchRemoteRepository
import saschpe.gamesale.data.remote.repository.WebRemoteRepository

object Module {
    private val api = Api("GameSales")

    val dealsRemoteRepository = DealsRemoteRepository(api)
    val gameRemoteRepository = GameRemoteRepository(api)
    val searchRemoteRepository = SearchRemoteRepository(api)
    val webRemoteRepository = WebRemoteRepository(api)
}