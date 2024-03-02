package com.novelitech.combinezipmergeflowkotlin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val isAuthenticated = MutableStateFlow(true)

    private val user = MutableStateFlow<User?>(null)
    private val posts = MutableStateFlow(emptyList<Post>())

    // Using _ to demonstrate that this is a state that a I want to expose to the UI
    private val _profileState = MutableStateFlow<ProfileState?>(null)
    val profileState = _profileState.asStateFlow()

    private val flow1 = (1..10).asFlow().onEach { delay(1000L) }
    private val flow2 = (10..20).asFlow().onEach { delay(300L) }
    var numberString by mutableStateOf("")
        private set

    init {

        // Combine is a operator that will trigger in every change/emit in "user" or "posts"
        // This ".combine" returns another Flow, so I can use this return to combine another Flow
        isAuthenticated
            .combine(user) { isAuthenticated, user ->
                if(isAuthenticated) user else null
            }
            .combine(posts) { user, posts -> // It gets the result of the first combine (the return value "user")

                user?.let {
                    _profileState.value = profileState.value?.copy(
                        profilePicUrl = user.profilePicUrl,
                        username = user.username,
                        description = user.description,
                        posts = posts,
                    )
                }
            }
            .launchIn(viewModelScope)

        // It's the same as above. The advantage of the above solution is that it's shorter
//        viewModelScope.launch {
//            user.combine(posts) { user, posts ->
//                _profileState.value = _profileState.value?.copy(
//                    profilePicUrl = user?.profilePicUrl,
//                    username = user?.username,
//                    description = user?.description,
//                    posts = posts,
//                )
//            }.collect()
//        }

        // Zip will fire when both flows emit new values. So just when flow1 and flow2 have emittions this function will trigger
        // In this case, the number 20 in flow2 will not appears on screen because there will be no more number to be emitted in flow1
        flow1.zip(flow2) { number1, number2 ->
            numberString += "($number1, $number2)\n"
        }.launchIn(viewModelScope)

        // Merge will listens to all flows that we pass to it and collect every value from any of them
        merge(flow1, flow2)
            .onEach {
                numberString += "$it\n"
            }
            .launchIn(viewModelScope)
    }

}