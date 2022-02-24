package st235.com.github.seamcarving.di

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import st235.com.github.seamcarving.data.AlbumRepository
import st235.com.github.seamcarving.data.StatefulMediaRepository
import st235.com.github.seamcarving.utils.media.MediaSaver
import st235.com.github.seamcarving.utils.media.MediaScanner
import st235.com.github.seamcarving.interactors.AlbumsInteractor
import st235.com.github.seamcarving.interactors.StatefulMediaInteractor
import st235.com.github.seamcarving.presentation.editor.EditorViewModel
import st235.com.github.seamcarving.presentation.gallery.GalleryViewModel

val viewModelsModule = module {

    viewModel { GalleryViewModel(get(), get()) }

    viewModel { EditorViewModel(get()) }

}

val interactorsModule = module {

    factory { AlbumsInteractor(get()) }

    factory { StatefulMediaInteractor(get()) }

}

val dataModule = module {

    single { AlbumRepository(get()) }

    single { StatefulMediaRepository(get()) }

    single {
        val contentResolver = androidContext().contentResolver
        MediaScanner.create(contentResolver)
    }

    single {
        val contentResolver = androidContext().contentResolver
        MediaSaver.create(contentResolver)
    }

}

val applicationModules = viewModelsModule +
        interactorsModule +
        dataModule
