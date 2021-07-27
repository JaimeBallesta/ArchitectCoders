package com.developerideas.myapplication.ui.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.developerideas.domain.Movie
import com.developerideas.usecases.GetPopularMovies

import com.developerideas.myapplication.ui.common.Event
import com.developerideas.myapplication.ui.common.ScopedViewModel
import kotlinx.coroutines.launch

class MainViewModel  @ViewModelInject constructor(private val getPopularMovies: GetPopularMovies) : ScopedViewModel() {

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> get() = _movies

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _navigateToMovie = MutableLiveData<Event<Int>>()
    val navigateToMovie: LiveData<Event<Int>> get() = _navigateToMovie

    private val _requestLocationPermission = MutableLiveData<Event<Unit>>()
    val requestLocationPermission: LiveData<Event<Unit>> get() = _requestLocationPermission

    init {
        initScope()
        refresh()
    }

    private fun refresh() {
        _requestLocationPermission.value = Event(Unit)
    }

    fun onCoarsePermissionRequested() {
        launch {
            _loading.value = true
            _movies.value = getPopularMovies.invoke()
            _loading.value = false
        }
    }

    fun onMovieClicked(movie: Movie) {
        _navigateToMovie.value = Event(movie.id)
    }

    override fun onCleared() {
        destroyScope()
        super.onCleared()
    }
}