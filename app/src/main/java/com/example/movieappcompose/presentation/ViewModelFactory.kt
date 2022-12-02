package com.example.movieappcompose.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movieappcompose.Injection.provideGetMovieDetail
import com.example.movieappcompose.Injection.provideGetNowPlayingMovies
import com.example.movieappcompose.Injection.provideGetRecommendedMovies
import com.example.movieappcompose.Injection.provideGetTopRatedMovies
import com.example.movieappcompose.Injection.provideSearchMovie
import com.example.movieappcompose.domain.usecase.*
import com.example.movieappcompose.presentation.viewmodels.DetailViewModel
import com.example.movieappcompose.presentation.viewmodels.MovieViewModel
import com.example.movieappcompose.presentation.viewmodels.SearchViewModel

class ViewModelFactory private constructor(
    private val getTopRatedMovies: GetTopRatedMovies,
    private val getNowPlayingMovies: GetNowPlayingMovies,
    private val getMovieDetail: GetMovieDetail,
    private val getRecommendedMovies: GetRecommendedMovies,
    private val searchMovie: SearchMovie,
) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
            return MovieViewModel(getTopRatedMovies, getNowPlayingMovies) as T
        } else if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(getMovieDetail, getRecommendedMovies) as T
        } else if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(searchMovie) as T
        }
        throw IllegalArgumentException("Unknown view model class: ${modelClass}")
    }

    companion object {
        private var instance: ViewModelFactory? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: ViewModelFactory(
                provideGetTopRatedMovies(),
                provideGetNowPlayingMovies(),
                provideGetMovieDetail(),
                provideGetRecommendedMovies(),
                provideSearchMovie(),
            )
        }.also { instance = it }
    }
}