package com.example.sharedflows_kotlin

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.sharedflows_kotlin.ui.theme.SharedFLows_kotlinTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SharedFLows_kotlinTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    collector()
                }
            }
        }
    }
}

/*
SharedFlow is a Kotlin coroutine flow designed for sharing emissions among
multiple collectors. It is a hot stream, meaning it actively emits
values regardless of the number of subscribers. You can configure
it with a replay parameter to replay the most recent values to new collectors.
It allows for fine-tuned control over buffering and backpressure,
making it suitable for event broadcasting or state sharing. Unlike StateFlow,
it doesn't hold a single value and can emit multiple values over time.
 */


fun sharedFlowProducer():Flow<Int>{
    val mutableSharedFlow = MutableSharedFlow<Int>(replay = 3)

/*    the concept of "replay" refers to the ability to replay a certain number
    of the most recent emitted values to new collectors.
    This can be particularly useful
    when you want new subscribers to receive the last few emitted items
    immediately upon collecting, rather than waiting for new emissions.
 */


    //val mutableSharedFlow = MutableSharedFlow<Int>()
    CoroutineScope(Dispatchers.Main).launch {
        for (i in 1..10){
            delay(1000)
            mutableSharedFlow.emit(i)
        }
    }
    return mutableSharedFlow
}

fun collector(){

    val sharedFlow = sharedFlowProducer()

    //collector 1
    CoroutineScope(Dispatchers.Main).launch {
        sharedFlow.collect{
            Log.d("sharedflowkotlin","collector 1:$it")
        }
    }

    //collector 2
    CoroutineScope(Dispatchers.Main).launch {
        delay(2500)
        sharedFlow.collect{
            Log.d("sharedflowkotlin","collector 2:$it")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SharedFLows_kotlinTheme {

    }
}