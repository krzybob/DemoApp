package pl.bobinski.demo.ui.details

import android.content.res.Configuration
import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Scale
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import pl.bobinski.demo.R
import pl.bobinski.demo.domain.ListedUser
import pl.bobinski.demo.domain.UserDetails
import pl.bobinski.demo.ui.common.Loader
import pl.bobinski.demo.ui.common.Toolbar
import pl.bobinski.demo.ui.theme.DemoAppTheme
import pl.bobinski.demo.utils.toShortFormat
import java.util.Date

@Destination
@Composable
fun UserDetailsScreen(
    navigator: DestinationsNavigator,
    viewModel: UserDetailsViewModel = hiltViewModel(),
    listedUser: ListedUser
) {
    val (processEvent, state, effects) = viewModel.use()

    LaunchedEffect(effects) {
        effects.collect { effect ->
            when (effect) {
                Effect.NavigateBack -> {
                    navigator.popBackStack()
                }
            }
        }
    }

    UserDetailsScreen(
        state = state,
        title = listedUser.login,
        processEvent = processEvent
    )
}

@Composable
private fun UserDetailsScreen(
    state: State,
    title: String,
    processEvent: (Event) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Toolbar(
                title = title,
                onBack = { processEvent(Event.OnBackClicked) }
            )
        }
    ) { innerPadding ->
        AnimatedContent(
            modifier = Modifier.padding(innerPadding),
            targetState = state,
            label = "userDetails"
        ) { targetState ->
            when (targetState) {
                State.Loading -> Loader(modifier = Modifier.fillMaxSize())

                is State.Success -> Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                ) {
                    Image(targetState.user)
                    Info(targetState.user)
                }

                State.Error -> Text(text = stringResource(id = R.string.connection_error))
            }
        }
    }
}

@Composable
private fun Image(user: UserDetails) {
    AsyncImage(
        modifier = Modifier.fillMaxWidth(),
        model = ImageRequest.Builder(LocalContext.current)
            .data(user.avatar)
            .diskCachePolicy(CachePolicy.ENABLED)
            .scale(Scale.FILL)
            .build(),
        contentScale = ContentScale.FillWidth,
        placeholder = painterResource(id = R.drawable.avatar_placeholder),
        fallback = painterResource(id = R.drawable.avatar_placeholder),
        error = painterResource(id = R.drawable.avatar_placeholder),
        contentDescription = "Avatar"
    )
}

@Composable
private fun Info(user: UserDetails) {
    Surface(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.secondary) {
        Column(modifier = Modifier.padding(12.dp)) {
            InfoText(stringResource(id = R.string.login, user.login))
            InfoText(stringResource(id = R.string.since, user.since.toShortFormat()))
            user.email?.let {
                InfoText(stringResource(id = R.string.email, it))
            }
            user.company?.let {
                InfoText(stringResource(id = R.string.company, it))
            }
            InfoText(stringResource(id = R.string.public_repos, user.publicRepos))
            InfoText(stringResource(id = R.string.followers, user.followers))
        }
    }
}

@Composable
private fun InfoText(text: String) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.onSecondary,
        style = MaterialTheme.typography.titleMedium
    )
}

@Preview(apiLevel = 33)
@Preview(apiLevel = 33, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Preview() {
    DemoAppTheme {
        UserDetailsScreen(
            state = State.Success(
                UserDetails(
                    login = "Preview user",
                    avatar = Uri.EMPTY,
                    since = Date(),
                    email = "preview@preview.com",
                    company = "Previewsoft",
                    publicRepos = 20,
                    followers = 100
                )
            ),
            title = "Preview user",
            processEvent = {}
        )
    }
}