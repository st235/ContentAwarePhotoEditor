package st235.com.github.seamcarving.di

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import st235.com.github.seamcarving.data.media.MediaSaver
import st235.com.github.seamcarving.data.media.MediaScanner
import st235.com.github.seamcarving.presentation.gallery.GalleryViewModel

val viewModelsModules = module {

    viewModel { GalleryViewModel() }

}

val dataModules = module {

    val ALBUM_NAME = "editor"

    single {
        val contentResolver = androidContext().contentResolver
        MediaScanner.create(contentResolver, ALBUM_NAME)
    }

    single {
        val contentResolver = androidContext().contentResolver
        MediaSaver.create(contentResolver, ALBUM_NAME)
    }

}

val applicationModules = viewModelsModules + dataModules
