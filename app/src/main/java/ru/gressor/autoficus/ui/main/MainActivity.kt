package ru.gressor.autoficus.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.viewmodel.ext.android.viewModel
import ru.gressor.autoficus.R
import ru.gressor.autoficus.data.entity.Note
import ru.gressor.autoficus.ui.base.BaseActivity
import ru.gressor.autoficus.ui.note.NoteActivity
import ru.gressor.autoficus.ui.splash.SplashActivity

@ExperimentalCoroutinesApi
class MainActivity : BaseActivity<List<Note>?>() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }

    override val viewModel: MainViewModel by viewModel()
    override val layoutRes = R.layout.activity_main

    private lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)

        adapter = MainAdapter { note -> NoteActivity.start(this@MainActivity, note.id) }
        notes_recycler.layoutManager = LinearLayoutManager(this)
        notes_recycler.adapter = adapter

        fab.setOnClickListener {
            NoteActivity.start(this@MainActivity)
        }
    }

    override fun renderData(data: List<Note>?) {
        data?.let { adapter.notes = it }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean =
        MenuInflater(this).inflate(R.menu.main, menu).let { true }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.logout -> showLogoutDialog().let { true }
            else -> false
        }

    private fun showLogoutDialog() {
        supportFragmentManager.findFragmentByTag(LogoutDialog.TAG) ?: LogoutDialog.createInstance {
            onLogout()
        }.show(supportFragmentManager, LogoutDialog.TAG)
    }

    private fun onLogout() {
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                startActivity(Intent(this, SplashActivity::class.java))
                finish()
            }
    }
}