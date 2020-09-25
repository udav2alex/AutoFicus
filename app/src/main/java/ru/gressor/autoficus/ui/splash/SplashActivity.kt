package ru.gressor.autoficus.ui.splash

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.viewmodel.ext.android.viewModel
import ru.gressor.autoficus.ui.base.BaseActivity
import ru.gressor.autoficus.ui.main.MainActivity

@ExperimentalCoroutinesApi
class SplashActivity: BaseActivity<Boolean?>() {

    override val viewModel: SplashViewModel by viewModel()
    override val layoutRes: Int? = null

    override fun onResume() {
        super.onResume()
        viewModel.requestUser()
    }

    override fun renderData(data: Boolean?) {
        data?.takeIf { it }?.let {
            startMainActivity()
        }
    }

    private fun startMainActivity() {
        MainActivity.start(this)
        finish()
    }

}