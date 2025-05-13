package org.connexuss.project.interfaces

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.BadgedBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.realtime
import org.connexuss.project.misc.Supabase
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.filter.FilterOperation
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.github.jan.supabase.realtime.PrimaryKey
import io.github.jan.supabase.realtime.selectAsFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import org.connexuss.project.comunicacion.Post


var currentHiloId = mutableStateOf("")
var initialCount = mutableStateOf(0)
var newPostsCount = mutableStateOf(0)

val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

val filter: FilterOperation = FilterOperation(
    column = "idhilo",
    operator = FilterOperator.EQ,
    value = currentHiloId.value
)

@OptIn(SupabaseExperimental::class)
val flow: Flow<List<Post>> = Supabase.client
    .from("post")
    .selectAsFlow(
        primaryKey = Post::idPost,
        channelName = "public:post",
        filter = filter
    )
    .onEach { posts ->
        initialCount.value = posts.size
        newPostsCount.value = posts.size - initialCount.value
    }

class HiloViewModel : ViewModel() {
    private var _newPostsCount by mutableStateOf(0)
    val newPostsCount: State<Int> get() = mutableStateOf(_newPostsCount)

    fun onNewPost() {
        _newPostsCount++
    }
    fun resetCounter() {
        _newPostsCount = 0
    }
}

val canal = Supabase.client
    .realtime
    .channel("public:post")
    .onPostgresChange(
        event = PostgresAction.Insert,
        schema = "public",
        table = "post",
        filter = "hiloId=eq.$currentHiloId"
    ) { change ->
        newPostsCount.value += 1
    }
    .subscribe()


@Composable
fun HiloTopBar(
    title: String,
    navController: NavHostController?,
    viewModel: HiloViewModel,
    showRefresh: Boolean = true,
    startRoute: String
) {
    TopAppBar(
        title = { Text(text = title, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
        navigationIcon = { /* botón atrás si aplica */ },
        actions = {
            if (viewModel.newPostsCount.value > 0) {
                BadgedBox(
                    badge = { Text("${viewModel.newPostsCount.value}") },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Icon(Icons.Default.Info, contentDescription = "Nuevos posts")
                }
            }
            if (showRefresh) {
                IconButton(onClick = {
                    viewModel.resetCounter()
                    navController?.currentDestination?.route?.let { current ->
                        navController.navigate(current) {
                            popUpTo(startRoute) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }) {
                    Icon(Icons.Default.Refresh, contentDescription = "Refrescar")
                }
            }
        }
    )
}
