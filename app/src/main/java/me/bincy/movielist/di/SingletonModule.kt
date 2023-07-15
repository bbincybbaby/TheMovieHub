package me.bincy.movielist.di

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.bincy.movielist.data.MovieRepository
import me.bincy.movielist.data.MovieRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SingletonModule {

    @Singleton
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory()).build()
    }

    @Provides
    @Singleton
    fun providesMovieRepository(
        moshi: Moshi,
        @ApplicationContext context: Context
    ): MovieRepository {
        return MovieRepositoryImpl(moshi, context)
    }
}