/*
 * Designed and developed by 2022 skydoves (Jaewoong Eum)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.skydoves.chatgpt.feature.chat.channels

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddComment
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.skydoves.balloon.compose.Balloon
import com.skydoves.chatgpt.core.designsystem.component.ChatGPTLoadingIndicator
import com.skydoves.chatgpt.core.designsystem.composition.LocalOnFinishDispatcher
import com.skydoves.chatgpt.core.designsystem.theme.PURPLE600
import com.skydoves.chatgpt.core.navigation.AppComposeNavigator
import com.skydoves.chatgpt.core.navigation.ChatGPTScreens
import com.skydoves.chatgpt.feature.chat.R
import com.skydoves.chatgpt.feature.chat.theme.ChatGPTStreamTheme
import io.getstream.chat.android.compose.ui.channels.ChannelsScreen

@Composable
fun ChatGPTChannels(
  modifier: Modifier,
  composeNavigator: AppComposeNavigator,
  viewModel: ChatGPTChannelsViewModel = hiltViewModel(),
  onFinishDispatcher: (() -> Unit)? = LocalOnFinishDispatcher.current
) {
  val uiState by viewModel.channelUiState.collectAsState()

  HandleGPTChannelsUiState(uiState = uiState)

  ChatGPTStreamTheme {
    Box(modifier = modifier.fillMaxSize()) {
      ChannelsScreen(
        isShowingHeader = false,
        onItemClick = { channel ->
          composeNavigator.navigate(ChatGPTScreens.Messages.createRoute(channel.cid))
        },
        onBackPressed = { onFinishDispatcher?.invoke() }
      )

      val isBalloonChannelDisplayed by viewModel.isBalloonDisplayedState.collectAsState()
      Balloon(
        modifier = Modifier
          .align(Alignment.BottomEnd)
          .padding(16.dp)
          .size(58.dp),
        builder = rememberFloatingBalloon(),
        balloonContent = {
          Text(
            modifier = Modifier
              .padding(12.dp)
              .fillMaxWidth(),
            text = "You can add your ChatGPT channel!",
            textAlign = TextAlign.Center,
            color = Color.White
          )
        }
      ) { balloonWindow ->
        LaunchedEffect(key1 = Unit) {
          if (!isBalloonChannelDisplayed) {
            balloonWindow.showAlignTop()
          }

          balloonWindow.setOnBalloonClickListener {
            viewModel.balloonChannelDisplayed()
            balloonWindow.dismiss()
          }
        }

        FloatingActionButton(
          modifier = Modifier.matchParentSize(),
          containerColor = PURPLE600,
          shape = CircleShape,
          onClick = { viewModel.handleEvents(GPTChannelEvent.CreateChannel) }
        ) {
          Icon(
            imageVector = Icons.Filled.AddComment,
            contentDescription = null,
            tint = Color.White
          )
        }
      }

      if (uiState == GPTChannelUiState.Loading) {
        ChatGPTLoadingIndicator()
      }
    }
  }
}

@Composable
private fun HandleGPTChannelsUiState(
  uiState: GPTChannelUiState
) {
  val context = LocalContext.current
  LaunchedEffect(key1 = uiState) {
    when (uiState) {
      is GPTChannelUiState.Success -> Toast.makeText(
        context,
        R.string.toast_success_create_channel,
        Toast.LENGTH_SHORT
      ).show()
      is GPTChannelUiState.Error -> Toast.makeText(
        context,
        R.string.toast_error,
        Toast.LENGTH_SHORT
      ).show()
      else -> Unit
    }
  }
}
