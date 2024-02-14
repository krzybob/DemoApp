package pl.bobinski.demo.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import pl.bobinski.demo.core.Initializer
import pl.bobinski.demo.image.CoilInitializer
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @IntoSet
    @Singleton
    @Provides
    fun provideCoilInitializer(): Initializer {
        return CoilInitializer()
    }
}