package fr.isen.LANIER.isensmartcompanion

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import fr.isen.LANIER.isensmartcompanion.models.AppDataBase
import fr.isen.LANIER.isensmartcompanion.models.Routes
import fr.isen.LANIER.isensmartcompanion.ui.theme.ISENSmartCompanionTheme
import fr.isen.LANIER.isensmartcompanion.views.EventScreen
import fr.isen.LANIER.isensmartcompanion.views.HistoryScreen
import fr.isen.LANIER.isensmartcompanion.views.chatDisplay
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        createChannel()
        setContent {
            val navController = rememberNavController()
            val db = Room.databaseBuilder(
                LocalContext.current,
                AppDataBase::class.java, "historyDataBase"
            ).fallbackToDestructiveMigration().build()

            ISENSmartCompanionTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing),
                    topBar = {HeaderBar()},
                    bottomBar = { bottomNavBar(navController) }
                ) { innerPadding ->
                    NavHost(navController = navController, startDestination = "HomeScreen", builder = {
                        composable(Routes.HOMERoute) { chatDisplay(mod = Modifier.padding(innerPadding), db) }
                        composable(Routes.EVENTSRoute) { EventScreen(mod = Modifier.padding(innerPadding)) }
                        composable(Routes.HISTORYRoute) { HistoryScreen(mod = Modifier.padding(innerPadding),db) }
                    })
                }
            }
        }
    }


    private fun createChannel(){
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
            var channel = NotificationChannel("EVENTS","eventsChannel", NotificationManager.IMPORTANCE_DEFAULT)
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeaderBar() {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically )
            {
                Image(
                    painter= painterResource(id = R.drawable.test),
                    contentDescription = "",
                    modifier = Modifier.size(40.dp).clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(40.dp))
                Text(text = "IsenSmartCompanion", modifier = Modifier.wrapContentHeight())
            }
        }
    )
}


@Composable
fun bottomNavBar(navController: NavController){

    NavigationBar (
        containerColor = MaterialTheme.colorScheme.secondaryContainer
    ){
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, "HomeLogo") },
            selected = false,
            onClick = {navController.navigate(Routes.HOMERoute)},
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Refresh, "SearchLogo") },
            selected = false,
            onClick = {navController.navigate(Routes.HISTORYRoute)},
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Info, "EventLogo") },
            selected = false,
            onClick = {navController.navigate(Routes.EVENTSRoute)},
        )
    }
}
