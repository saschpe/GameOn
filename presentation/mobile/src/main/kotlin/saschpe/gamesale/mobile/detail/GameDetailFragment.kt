package saschpe.gamesale.mobile.detail

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import kotlinx.android.synthetic.main.fragment_home.*
import saschpe.gamesale.mobile.R

class GameDetailFragment : Fragment(R.layout.fragment_game_detail) {
    private var paramSlug: String? = null
    private var paramTitle: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.run {
            paramSlug = getString(ARG_SLUG)
            paramTitle = getString(ARG_TITLE)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        setupWithNavController(toolbar, findNavController())

        // TODO:
    }

    override fun onResume() {
        super.onResume()
        paramTitle?.let { (activity as AppCompatActivity).title = it }
    }

    companion object {
        const val ARG_SLUG = "plain"
        const val ARG_TITLE = "title"
    }
}
