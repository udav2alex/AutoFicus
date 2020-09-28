package ru.gressor.autoficus.ui.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach
import ru.gressor.autoficus.R
import ru.gressor.autoficus.data.errors.NoAuthException
import kotlin.coroutines.CoroutineContext

@ExperimentalCoroutinesApi
abstract class BaseActivity<T> : AppCompatActivity(), CoroutineScope {

    override val coroutineContext: CoroutineContext by lazy { Dispatchers.Main + Job() }

    private lateinit var dataJob: Job
    private lateinit var errorJob: Job
    abstract val viewModel: BaseViewModel<T>
    abstract val layoutRes: Int?

    companion object {
        const val REQUEST_CODE_SIGN_IN = 31415
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutRes?. let{ setContentView(it) }
    }
    override fun onStart() {
        super.onStart()
        dataJob = launch {
            viewModel.getViewStateChannel().consumeEach {
                renderData(it)
            }
        }
        errorJob = launch {
            viewModel.getErrorChannel().consumeEach {
                renderError(it)
            }
        }
    }
    override fun onStop() {
        super.onStop()
        dataJob.cancel()
        errorJob.cancel()
    }
    override fun onDestroy() {
        super.onDestroy()
        coroutineContext.cancel()
    }

    abstract fun renderData(data: T)

    private fun renderError(error: Throwable) {
        when(error) {
            is NoAuthException -> startLoginActivity()
            else -> error.message?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startLoginActivity() {
        val providers = listOf(
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.EmailBuilder().build()
        )

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setLogo(R.drawable.android_robot)
                .setTheme(R.style.LoginStyle)
                .setAvailableProviders(providers)
                .build(),
            REQUEST_CODE_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_SIGN_IN && resultCode != Activity.RESULT_OK) {
            finish()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}