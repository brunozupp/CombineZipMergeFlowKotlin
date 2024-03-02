package com.novelitech.combinezipmergeflowkotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.novelitech.combinezipmergeflowkotlin.ui.theme.CombineZipMergeFlowKotlinTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CombineZipMergeFlowKotlinTheme {
                val viewModel = viewModel<MainViewModel>()
                Text(text = viewModel.numberString)
            }
        }
    }
}
