package ru.gressor.autoficus.ui.main

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import ru.gressor.autoficus.R

class LogoutDialog: DialogFragment() {

    companion object {
        val TAG = LogoutDialog::class.java.name + " TAG"

        fun createInstance(onLogout: (() -> Unit)) = LogoutDialog().apply {
            this.onLogout = onLogout
        }
    }

    var onLogout: (() -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(context)
            .setTitle(getString(R.string.logout))
            .setMessage(getString(R.string.are_you_sure))
            .setPositiveButton(android.R.string.ok) { _, _ -> onLogout?.invoke() }
            .setNegativeButton(android.R.string.cancel) { _, _ -> dismiss() }
            .create()
}