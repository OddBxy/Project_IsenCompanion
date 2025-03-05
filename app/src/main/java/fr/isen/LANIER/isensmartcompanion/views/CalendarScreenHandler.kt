@file:OptIn(ExperimentalMaterial3Api::class)

package fr.isen.LANIER.isensmartcompanion.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import fr.isen.LANIER.isensmartcompanion.models.IsenCour
import fr.isen.LANIER.isensmartcompanion.models.IsenEvent
import fr.isen.LANIER.isensmartcompanion.services.EventFetcher
import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarView(mod : Modifier, ){
    val datePickerState = rememberDatePickerState(System.currentTimeMillis())

    Scaffold(
        modifier = mod,
        topBar = {
            dateInfo(datePickerState)
        }

    ){ innerPadding ->
        Column(Modifier.verticalScroll(rememberScrollState()).padding(innerPadding)) {
            displaySchedule(datePickerState)
            Spacer(Modifier.height(20.dp))
            displayTodayEvents(datePickerState)
        }
    }


}

@Composable
fun dateInfo(datePickerState : DatePickerState){
    var datePickerVisibility by remember { mutableStateOf(false) }
    val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    val date = Date(datePickerState.selectedDateMillis!!)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(10.dp)
            .border(
                BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(20)
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    )
    {
        FloatingActionButton(
            onClick = {
                datePickerState.selectedDateMillis = datePickerState.selectedDateMillis!! - 86400000
            },
            contentColor = MaterialTheme.colorScheme.primary,
            containerColor = Color.Transparent,
            elevation = FloatingActionButtonDefaults.elevation(0.dp)
        ) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, "")
        }

        Text(
            dateFormat.format(date),
            Modifier.clickable(enabled = true) { datePickerVisibility = true }
        )

        FloatingActionButton(
            onClick = {
                datePickerState.selectedDateMillis = datePickerState.selectedDateMillis!! + 86400000
            },
            contentColor = MaterialTheme.colorScheme.primary,
            containerColor = Color.Transparent,
            elevation = FloatingActionButtonDefaults.elevation(0.dp)
        ) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, "")
        }
    }

    if(datePickerVisibility){
        showDatePicker(datePickerState, {datePickerVisibility = false})
    }
}


@Composable
fun showDatePicker(date : DatePickerState, onDismiss: () -> Unit){
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    ) {
        DatePicker(
            state = date
        )
    }
}


@Composable
fun displaySchedule(datePickerState: DatePickerState){
    var isenCours by remember { mutableStateOf(listOf<IsenCour>()) }
    val context = LocalContext.current
    val file = BufferedReader(InputStreamReader(context.assets.open("coursisen.json")))
    isenCours = Gson().fromJson(file.readText(), Array<IsenCour>::class.java).asList()

    val dateFormat = SimpleDateFormat( "dd MMMM yyyy", Locale.getDefault() )
    val selectedDate = dateFormat.format(datePickerState.selectedDateMillis)

    val classes = mutableListOf<IsenCour>()
    for(cours in isenCours){
        if(cours.date == selectedDate){
            classes.add(cours)
        }
    }

    classes.sortBy { it.hourStart }

    //cant use lazyColumn in column with modifier.verticalScroll
    Column (Modifier.fillMaxHeight() ){
        for(currentClass in classes){
            Card( Modifier.height(115.dp).padding(10.dp).fillMaxWidth() ) {

                Row(Modifier.background(MaterialTheme.colorScheme.onSecondary).fillMaxWidth().padding(10.dp)) {
                    Card(Modifier.fillMaxWidth(1/5f).fillMaxHeight()) {
                        Column(
                            Modifier.fillMaxSize().padding(1.dp),
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text( currentClass.hourStart.toString() + " h" )
                            Text( "to" )
                            Text( currentClass.hourEnd.toString() + " h")
                        }
                    }
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text(currentClass.title, fontWeight = FontWeight.Bold)
                        Text("Teacher : ${currentClass.teacher}", fontSize = 13.sp)
                        Text("Room ${currentClass.room}", fontSize = 13.sp)
                    }
                }

            }
        }
    }
}

@Composable
fun displayTodayEvents(datePickerState: DatePickerState){
    var isExpanded by remember { mutableStateOf(false) }
    var isenEvents by remember { mutableStateOf(listOf<IsenEvent>()) }
    //use singleton to get all events
    EventFetcher.getAllEvents { isenEvents = it }

    var todayEvents = mutableListOf<IsenEvent>()
    val dateFormat = SimpleDateFormat( "dd MMMM yyyy", Locale.getDefault() )

    for (event in isenEvents){
        val selectedDate = dateFormat.format(datePickerState.selectedDateMillis)
        if(event.date == selectedDate){
            todayEvents.add(event)
        }

    }

    Column (
        modifier = Modifier
            .background(MaterialTheme.colorScheme.onSecondary)
            .fillMaxWidth()
            .clickable {
                isExpanded = !isExpanded
            }
    ){
        Row(Modifier.fillMaxWidth().padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 25.sp)){
                        append("Events")
                    }
                }
            )
            if(!isExpanded){
                Icon(Icons.Filled.KeyboardArrowDown, "dropdown logo")
            }
            else{
                Icon(Icons.Filled.KeyboardArrowUp, "dropdown logo")
            }
        }

        if(isExpanded){
            //cant use lazyColumn in column with modifier.verticalScroll
            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                if(todayEvents.isEmpty()){
                    Text(
                        "No scheduled events for this day",
                        Modifier.fillMaxWidth().padding(10.dp),
                        textAlign = TextAlign.Center
                    )
                }
                else{
                    for(event in todayEvents){
                        Column(
                            Modifier.padding(10.dp).fillMaxWidth(9/10f)
                        ) {
                            Text("${event.category} : \n${event.title}", modifier = Modifier.padding(horizontal = 0.dp, vertical = 10.dp))
                            HorizontalDivider(
                                thickness = 2.dp,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }
            }
        }
    }


}