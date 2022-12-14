package com.example.movieappcompose.presentation.pages

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movieappcompose.R
import com.example.movieappcompose.domain.entities.Movie
import com.example.movieappcompose.presentation.ViewModelFactory
import com.example.movieappcompose.presentation.components.ErrorScreen
import com.example.movieappcompose.presentation.components.LoadingScreen
import com.example.movieappcompose.presentation.components.MovieTile
import com.example.movieappcompose.presentation.theme.MovieAppComposeTheme
import com.example.movieappcompose.presentation.viewmodels.WatchlistViewModel
import com.example.movieappcompose.utilities.ResultState

@Composable
fun WatchlistPage(
    navigateToDetail: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WatchlistViewModel = viewModel(
        factory = ViewModelFactory.getInstance(LocalContext.current)
    )
) {
    val watchlistMoviesResult by viewModel.fetchWatchlistMovies()
        .observeAsState(initial = ResultState.Loading)

    when (watchlistMoviesResult) {
        is ResultState.Loading -> LoadingScreen()
        is ResultState.Success<List<Movie>> -> {
            val data = (watchlistMoviesResult as ResultState.Success<List<Movie>>).data

            if (data.isNotEmpty()) {
                WatchlistContent(
                    watchlistMovies = data,
                    navigateToDetail = navigateToDetail,
                    modifier = modifier,
                )
            } else {
                ErrorScreen(
                    text = stringResource(R.string.watchlist_empty)
                )
            }
        }
        is ResultState.Error -> ErrorScreen(
            text = stringResource(R.string.watchlist_empty)
        )
    }
}

@Composable
fun WatchlistContent(
    watchlistMovies: List<Movie>,
    navigateToDetail: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        modifier = modifier.testTag(stringResource(id = R.string.watchlist_tag_test)),
    ) {
        items(watchlistMovies, key = { it.id }) { item ->
            MovieTile(
                imageUrl = item.posterPath ?: "",
                title = item.title,
                subtitle = item.overview ?: "",
                onClick = { navigateToDetail(item.id) },
                modifier = modifier.padding(horizontal = 16.dp),
            )
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showSystemUi = true
)
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showSystemUi = true
)
@Composable
fun WatchlistPagePreview() {
    MovieAppComposeTheme {
        WatchlistPage(
            navigateToDetail = {}
        )
    }
}