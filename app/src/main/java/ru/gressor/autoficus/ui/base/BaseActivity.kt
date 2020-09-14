package ru.gressor.autoficus.ui.base

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity<T, S : BaseViewState<T>> : AppCompatActivity() {
    abstract val viewModel: BaseViewModel<T, S>
    abstract val layoutRes: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes)

        viewModel.getViewState().observe(this, {
            it?.let { viewState ->
                viewState.error?.let { error ->
                    renderError(error)
                    return@observe
                }
                viewState.data?.let { data -> renderData(data) }
            }
        })
    }

    abstract fun renderData(data: T)

    protected fun renderError(error: Throwable) {
        error.message?.let {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

//    fun renderError(error: Throwable, view: View) {
//        error.message?.let {
//            val snackBar = Snackbar.make(this., it, Snackbar.LENGTH_INDEFINITE)
//            snackBar.setAction(R.string.ok_bth_title, View.OnClickListener { snackBar.dismiss() })
//            snackBar.show()
//        }
//    }

}