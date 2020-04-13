package saschpe.gameon.mobile.favorites

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import saschpe.gameon.domain.Module.dismissPriceAlertUseCase

class PriceAlertDismissedBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val argPlain = requireNotNull(intent.getStringExtra(ARG_PLAIN))
        GlobalScope.launch {
            dismissPriceAlertUseCase.invoke(argPlain)
        }
    }

    companion object {
        const val ARG_PLAIN = "plain"
    }
}