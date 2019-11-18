package saschpe.gameon.data.local

import android.annotation.SuppressLint
import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import java.io.File

/**
 * Hidden initialization content provider.
 *
 * Does not provide real content but hides initialization boilerplate from the library user.
 *
 * @link https://firebase.googleblog.com/2016/12/how-does-firebase-initialize-on-android.html
 */
class DataContentProvider : ContentProvider() {
    /**
     * Called exactly once before Application.onCreate()
     */
    override fun onCreate(): Boolean {
        applicationContext = context ?: throw Exception("Need the context")

        // Fetch those important directories as they cause disk access, which is bad...
        filesDir = applicationContext.filesDir
        cacheDir = applicationContext.cacheDir
        return true
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? = null

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        args: Array<out String>?,
        sortOrder: String?
    ): Cursor? = null

    override fun update(uri: Uri, values: ContentValues?, selection: String?, args: Array<out String>?): Int = 0

    override fun delete(uri: Uri, selection: String?, args: Array<out String>?): Int = 0

    override fun getType(uri: Uri): String? = null

    companion object {
        @SuppressLint("StaticFieldLeak")
        internal lateinit var applicationContext: Context

        @SuppressLint("StaticFieldLeak")
        internal lateinit var filesDir: File

        @SuppressLint("StaticFieldLeak")
        internal lateinit var cacheDir: File
    }
}
