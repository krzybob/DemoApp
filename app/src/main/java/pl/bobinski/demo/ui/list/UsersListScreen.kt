@file:OptIn(ExperimentalMaterial3Api::class)

package pl.bobinski.demo.ui.list

import android.content.res.Configuration
import android.net.Uri
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.flowOf
import pl.bobinski.demo.R
import pl.bobinski.demo.domain.ListedUser
import pl.bobinski.demo.ui.common.Loader
import pl.bobinski.demo.ui.common.Toolbar
import pl.bobinski.demo.ui.destinations.UserDetailsScreenDestination
import pl.bobinski.demo.ui.theme.DemoAppTheme

@Destination
@RootNavGraph(start = true)
@Composable
fun UsersListScreen(
    navigator: DestinationsNavigator,
    viewModel: UsersListViewModel = hiltViewModel()
) {
    val (processEvent, state, effects) = viewModel.use()

    LaunchedEffect(effects) {
        effects.collect { effect ->
            when (effect) {
                is Effect.ShowDetails -> {
                    navigator.navigate(UserDetailsScreenDestination(listedUser = effect.user))
                }
            }
        }
    }

    UsersListScreen(
        state = state,
        processEvent = processEvent
    )
}

@Composable
private fun UsersListScreen(
    state: State,
    processEvent: (Event) -> Unit,
) {
    val users = state.data.collectAsLazyPagingItems()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { Toolbar(stringResource(id = R.string.users_list_title)) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = { Text(stringResource(id = R.string.search_field_label)) },
                value = state.query,
                onValueChange = { processEvent(Event.OnQueryChanged(it)) }
            )

            Crossfade(
                targetState = users.loadState.refresh is LoadState.Loading,
                label = "loadingUsersList"
            ) { loading ->
                if (loading) {
                    Loader(modifier = Modifier.fillMaxSize())
                } else {
                    UsersList(
                        users = users,
                        onClick = { processEvent(Event.OnUserClicked(it)) }
                    )
                }
            }
        }
    }
}

@Composable
private fun UsersList(users: LazyPagingItems<ListedUser>, onClick: (ListedUser) -> Unit) {
    LazyVerticalStaggeredGrid(
        modifier = Modifier.fillMaxSize(),
        columns = StaggeredGridCells.Adaptive(120.dp),
        contentPadding = PaddingValues(16.dp),
        verticalItemSpacing = 8.dp,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for (i in 0 until users.itemCount) {
            item { users[i]?.let { user -> UserCard(user = user, onClick = { onClick(user) }) } }
        }
        if (users.itemCount == 0 && users.loadState.refresh is LoadState.NotLoading) {
            item { Text(text = stringResource(id = R.string.no_results)) }
        } else if (users.loadState.refresh is LoadState.Error) {
            item { Text(text = stringResource(id = R.string.connection_error)) }
        }
    }
}

@Composable
private fun UserCard(user: ListedUser, onClick: () -> Unit) {
    Card(modifier = Modifier
        .wrapContentSize()
        .clip(MaterialTheme.shapes.medium)
        .clickable { onClick() }) {

        Box {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(user.avatar)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .build(),
                contentScale = ContentScale.FillWidth,
                placeholder = painterResource(id = R.drawable.avatar_placeholder),
                fallback = painterResource(id = R.drawable.avatar_placeholder),
                error = painterResource(id = R.drawable.avatar_placeholder),
                contentDescription = "Avatar"
            )

            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f)
            ) {
                Column(Modifier.padding(horizontal = 6.dp, vertical = 4.dp)) {
                    Text(
                        text = user.login,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }
        }
    }
}

@Preview(apiLevel = 33)
@Preview(apiLevel = 33, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Preview() {
    DemoAppTheme {
        UsersListScreen(
            state = State(
                query = "krzybob",
                data = flowOf(
                    PagingData.from(
                        List(12) { ListedUser("krzybob", Uri.EMPTY) },
                        sourceLoadStates = LoadStates(
                            refresh = LoadState.NotLoading(true),
                            append = LoadState.NotLoading(true),
                            prepend = LoadState.NotLoading(true),
                        )
                    )
                )
            ),
            processEvent = {}
        )
    }
}