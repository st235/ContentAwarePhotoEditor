package st235.com.github.seamcarving.di

import com.google.gson.GsonBuilder
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import st235.com.github.seamcarving.data.AspectRatiosRepository
import st235.com.github.seamcarving.data.GalleryRepository
import st235.com.github.seamcarving.data.StatefulMediaRepository
import st235.com.github.seamcarving.utils.media.MediaSaver
import st235.com.github.seamcarving.utils.media.MediaScanner
import st235.com.github.seamcarving.interactors.GalleryInteractor
import st235.com.github.seamcarving.interactors.AspectRatiosInteractor
import st235.com.github.seamcarving.interactors.CarvingInteractor
import st235.com.github.seamcarving.interactors.ImagesInteractor
import st235.com.github.seamcarving.interactors.StatefulMediaInteractor
import st235.com.github.seamcarving.presentation.editor.EditorViewModel
import st235.com.github.seamcarving.presentation.gallery.GalleryViewModel
import st235.com.github.seamcarving.utils.BitmapHelper
import st235.com.github.seamcarving.utils.carving.SeamCarvingProcessor

val viewModelsModule = module {

    viewModel { GalleryViewModel(get(), get()) }

    viewModel { EditorViewModel(get(), get(), get(), get()) }

}

val interactorsModule = module {

    factory { CarvingInteractor(get()) }

    factory { GalleryInteractor(get()) }

    factory { StatefulMediaInteractor(get()) }

    factory { AspectRatiosInteractor(get()) }

    factory { ImagesInteractor(get()) }

}

val dataModule = module {

    single { AspectRatiosRepository(get(), get()) }

    single { GalleryRepository(get(), get()) }

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

val utilsModule = module {

    single { SeamCarvingProcessor(androidContext()) }

    single { GsonBuilder().excludeFieldsWithoutExposeAnnotation().create() }

    factory { androidContext().contentResolver }

    factory { androidContext().resources }

    factory { BitmapHelper(get()) }

}

val applicationModules = viewModelsModule +
        interactorsModule +
        dataModule +
        utilsModule
