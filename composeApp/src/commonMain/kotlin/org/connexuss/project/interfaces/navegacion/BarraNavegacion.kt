package org.connexuss.project.interfaces.navegacion

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import connexus_serverless.composeapp.generated.resources.Res
import connexus_serverless.composeapp.generated.resources.ic_chats
import connexus_serverless.composeapp.generated.resources.ic_foros
import org.connexuss.project.interfaces.comun.traducir
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun MiBottomBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        // Ítem de Chats
        NavigationBarItem(
            selected = currentRoute == "contactos",
            onClick = {
                navController.navigate("contactos") {
                    navController.graph.startDestinationRoute?.let {
                        popUpTo(it) { saveState = true }
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = {
                Icon(
                    painter = painterResource(Res.drawable.ic_chats),
                    contentDescription = traducir("chats"),
                    modifier = Modifier.size(24.dp)
                )
            },
            label = {
                Text(
                    text = traducir("chats"),
                    style = MaterialTheme.typography.labelMedium
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

        // Ítem de Foros
        NavigationBarItem(
            selected = currentRoute == "foroLocal"/*"foro"*/,
            onClick = {
                navController.navigate("foroLocal"/*"foro"*/) {
                    navController.graph.startDestinationRoute?.let {
                        popUpTo(it) { saveState = true }
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = {
                Icon(
                    painter = painterResource(Res.drawable.ic_foros),
                    contentDescription = traducir("foro"),
                    modifier = Modifier.size(24.dp)
                )
            },
            label = {
                Text(
                    text = traducir("foro"),
                    style = MaterialTheme.typography.labelMedium
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}
