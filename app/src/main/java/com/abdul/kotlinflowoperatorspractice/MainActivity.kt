package com.abdul.kotlinflowoperatorspractice

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.abdul.kotlinflowoperatorspractice.ui.theme.KotlinFlowOperatorsPracticeTheme
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.system.measureTimeMillis

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KotlinFlowOperatorsPracticeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                    FlowsPractice()
                }
            }
        }
    }
}

@OptIn(DelicateCoroutinesApi::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun FlowsPractice() {
    //consumer
    GlobalScope.launch(Dispatchers.Main) {
       // val time = measureTimeMillis {
            /* producer()
                 .map {
                     delay(1000) //1 second
                     it * 2
                     Log.d("soha", "Map Thread - ${Thread.currentThread().name}")
                 }
                 .flowOn(Dispatchers.Main)
                 .filter {
                     delay(500) //0.5 seconds
                     Log.d("soha", "Filter Thread - ${Thread.currentThread().name}")
                     it < 8

                 }
                 .flowOn(Dispatchers.IO) //produce value on IO Thread (Used for switch thread)
                 //.buffer(3) //store 3 items in buffer
                 .collect { // consume/collect on Main Thread
                     //consumer is slow
                     //delay(1500) //1.5 seconds
                     //Log.d("soha", it.toString())
                     Log.d("soha", "Collector Thread - ${Thread.currentThread().name}")
                 }*/
         //}
        //Log.d("soha", time.toString())

        //Exception handling
        try {
            producer().collect {
                Log.d("soha", "Collector Thread - $it ${Thread.currentThread().name}")
                //Exception handling
               // throw Exception("Error in Collector")
            }
        } catch (e: Exception) {
            Log.d("soha", e.message.toString())
        }
    }
}

private fun producer(): Flow<Int> {
    return flow<Int> {
        //to produce value on io thread
        val list = listOf<Int>(1, 2, 3, 4, 5)
        list.forEach {
            //producer is fast
            delay(1000) //1 second
            Log.d("soha", "Emitter Thread - ${Thread.currentThread().name}")
            emit(it)
            //Exception handling
            throw Exception("Error in Emitter")
        }
    }.catch { //handle flow producer exception
        Log.d("soha", "Emitter Catch - ${it.message}")
        //we can also return callback while getting exception
        emit(-1)
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KotlinFlowOperatorsPracticeTheme {
        Greeting("Android")
    }
}