package saschpe.gameon.data.remote

import saschpe.gameon.data.remote.repository.DealsRemoteRepository
import saschpe.gameon.data.remote.repository.GameRemoteRepository
import saschpe.gameon.data.remote.repository.SearchRemoteRepository
import saschpe.gameon.data.remote.repository.WebRemoteRepository

object Module {
    private val api = Api("GameOn")

    val dealsRemoteRepository = DealsRemoteRepository(api)
    val gameRemoteRepository = GameRemoteRepository(api)
    val searchRemoteRepository = SearchRemoteRepository(api)
    val webRemoteRepository = WebRemoteRepository(api)
}