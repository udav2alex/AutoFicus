package ru.gressor.autoficus.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import ru.gressor.autoficus.data.NotesRepository
import ru.gressor.autoficus.data.provider.FireDbDataProvider
import ru.gressor.autoficus.data.provider.RemoteDataProvider
import ru.gressor.autoficus.ui.main.MainViewModel
import ru.gressor.autoficus.ui.note.NoteViewModel
import ru.gressor.autoficus.ui.splash.SplashViewModel

val appModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { FireDbDataProvider(get(), get()) } bind RemoteDataProvider::class
    single { NotesRepository(get()) }
}

@ExperimentalCoroutinesApi
val splashModule = module {
    viewModel { SplashViewModel(get()) }
}

@ExperimentalCoroutinesApi
val mainModule = module {
    viewModel { MainViewModel(get()) }
}

@ExperimentalCoroutinesApi
val noteModule = module {
    viewModel { NoteViewModel(get()) }
}