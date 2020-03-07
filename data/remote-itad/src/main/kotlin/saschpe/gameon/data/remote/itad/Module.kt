package saschpe.gameon.data.remote.itad

import saschpe.gameon.data.remote.itad.repository.*

object Module {
    private val api = Api(
        "GameOn",
        "d1ac7e96fdeb83528a7489be031597272430d922"
    )

    val dealsRemoteRepository = DealsRemoteRepository(api)
    val gameRemoteRepository = GameRemoteRepository(api)
    val searchRemoteRepository = SearchRemoteRepository(api)
    val webRemoteRepository = WebRemoteRepository(api)
}