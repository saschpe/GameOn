package saschpe.gameon.mobile.game.reviews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import kotlinx.coroutines.launch
import saschpe.gameon.common.base.errorLogged
import saschpe.gameon.common.base.recyclerview.SpacingItemDecoration
import saschpe.gameon.data.core.Result
import saschpe.gameon.data.core.model.GameInfo
import saschpe.gameon.data.core.model.STEAM_STORE
import saschpe.gameon.mobile.Module.firebaseAnalytics
import saschpe.gameon.mobile.R
import saschpe.gameon.mobile.base.customtabs.openUrl
import saschpe.gameon.mobile.databinding.FragmentGameOtherBinding
import saschpe.gameon.mobile.game.GameFragment
import saschpe.gameon.common.R as CommonR

class GameOtherFragment : Fragment(R.layout.fragment_game_other) {
    private lateinit var reviewsAdapter: GameReviewsAdapter
    private lateinit var argPlain: String
    private val viewModel: GameOtherViewModel by viewModels()
    private var _binding: FragmentGameOtherBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        argPlain = requireNotNull(arguments?.getString(ARG_PLAIN))
        reviewsAdapter = GameReviewsAdapter(requireContext())
        viewModel.getGameInfo(argPlain)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentGameOtherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.apply {
            adapter = reviewsAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(SpacingItemDecoration(context, CommonR.dimen.recycler_spacing))
        }

        viewModel.gameInfoLiveData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success<GameInfo> -> {
                    val gameInfo = result.data
                    val viewModels = gameInfo.reviews?.map { review ->
                        val onClick: (() -> Unit)? = when (review.key) {
                            STEAM_STORE -> {
                                {
                                    lifecycleScope.launch {
                                        viewModel.getStreamReviewsUrl(argPlain)
                                            ?.let { openUrl(it) }
                                    }
                                }
                            }

                            else -> null
                        }

                        GameReviewsAdapter.ViewModel.ReviewViewModel(
                            store = review.key,
                            review = review.value,
                            onClick = onClick
                        )
                    } ?: listOf(GameReviewsAdapter.ViewModel.NoResultsViewModel())

                    reviewsAdapter.submitList(viewModels)

                    binding.isDlcChip.visibility = if (gameInfo.is_dlc) View.VISIBLE else View.GONE
                    binding.achievementsChip.visibility =
                        if (gameInfo.achievements) View.VISIBLE else View.GONE
                    binding.tradingCardsChip.visibility =
                        if (gameInfo.trading_cards) View.VISIBLE else View.GONE
                    binding.earlyAccessChip.visibility =
                        if (gameInfo.early_access) View.VISIBLE else View.GONE
                    binding.isPackageChip.visibility = if (gameInfo.is_package) View.VISIBLE else View.GONE
                    if (!gameInfo.is_dlc &&
                        !gameInfo.achievements &&
                        !gameInfo.trading_cards &&
                        !gameInfo.early_access &&
                        !gameInfo.is_package
                    ) {
                        binding.perksDivider.visibility = View.GONE
                        binding.perksText.visibility = View.GONE
                    }

                    binding.checkOnProtonDB.setOnClickListener {
                        lifecycleScope.launch { openUrl("$PROTON_DB_SEARCH_URL${gameInfo.title}") }
                    }
                }

                is Result.Error -> {
                    result.errorLogged()
                    parentFragment?.let {
                        if (it is GameFragment) {
                            it.showSnackBarWithRetryAction(CommonR.string.unable_to_load_game) {
                                viewModel.getGameInfo(argPlain)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "Game Other")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "GameOtherFragment")
        }
    }

    companion object {
        const val ARG_PLAIN = "plain"
        private const val PROTON_DB_SEARCH_URL = "https://www.protondb.com/search?q="
    }
}
