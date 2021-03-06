package com.developerideas.myapplication.model.database

import com.developerideas.data.source.LocalDataSource
import com.developerideas.domain.Movie
import com.developerideas.myapplication.model.toDomainMovie
import com.developerideas.myapplication.model.toRoomMovie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RoomDataSource(db: MovieDatabase):LocalDataSource {

    private val movieDao = db.movieDao()

    override suspend fun isEmpty(): Boolean =
        withContext(Dispatchers.IO) { movieDao.movieCount() <= 0 }

    override suspend fun saveMovies(movies: List<Movie>) {
        return withContext(Dispatchers.IO) { movieDao.insertMovies(movies.map { it.toRoomMovie() }) }
    }

    override suspend fun getPopularMovies(): List<Movie> = withContext(Dispatchers.IO) {
        movieDao.getAll().map { it.toDomainMovie() }
    }

    override suspend fun findById(id: Int): Movie = withContext(Dispatchers.IO) {
        movieDao.findById(id).toDomainMovie()
    }

    override suspend fun update(movie: Movie) {
        withContext(Dispatchers.IO) { movieDao.updateMovie(movie.toRoomMovie()) }
    }

    override suspend fun getFavoritesMovies(): List<Movie> = withContext(Dispatchers.IO) {
        movieDao.getFavorites().map { it.toDomainMovie() }
    }

    override suspend fun deleteFavoriteMovies(id: Int) {
        return withContext(Dispatchers.IO) {movieDao.deleteMovie(id)}
    }
}