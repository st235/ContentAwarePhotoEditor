package st235.com.github.seamcarving.di

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import st235.com.github.seamcarving.utils.media.MediaSaver
import st235.com.github.seamcarving.utils.media.MediaScanner
import st235.com.github.seamcarving.interactors.GalleryInteractor
import st235.com.github.seamcarving.presentation.editor.EditorViewModel
import st235.com.github.seamcarving.presentation.gallery.GalleryViewModel

val viewModelsModules = module {

    viewModel { GalleryViewModel(get()) }

    viewModel { EditorViewModel(get()) }

}

val interactorsModules = module {

    single { GalleryInteractor(get(), get()) }

}

val dataModules = module {

    single {
        val contentResolver = androidContext().contentResolver
        MediaScanner.create(contentResolver)
    }

    single {
        val contentResolver = androidContext().contentResolver
        MediaSaver.create(contentResolver)
    }

}

val applicationModules = viewModelsModules +
        interactorsModules +
        dataModules
