/**
 * Copyright (C) 2017 Cesar Valiente & Corey Shaw
 *
 * https://github.com/CesarValiente
 * https://github.com/coshaw
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cesarvaliente.kunidirectional

import com.cesarvaliente.kunidirectional.store.ActionDispatcher
import com.cesarvaliente.kunidirectional.store.Item
import com.cesarvaliente.kunidirectional.store.State
import com.cesarvaliente.kunidirectional.store.StateDispatcher
import com.cesarvaliente.kunidirectional.store.Subscriber
import com.cesarvaliente.kunidirectional.store.ThreadExecutor
import com.cesarvaliente.kunidirectional.store.action.Action

abstract class ControllerView(
        protected val actionDispatcher: ActionDispatcher,
        protected val stateDispatcher: StateDispatcher,
        private val handleStateDifferentThread: ThreadExecutor? = null)
    : LifecycleCallbacks, Subscriber<State> {

    override var isActivityRunning: Boolean = false

    val state: State
        get() = stateDispatcher.state

    val currentItem: Item
        get() = state.editItemScreen.currentItem

    override fun onStart() {
        isActivityRunning = true
        if (!stateDispatcher.isSubscribed(this)) {
            stateDispatcher.subscribe(this)
        }
        handleState(stateDispatcher.state)
    }

    override fun onResume() {}

    override fun onPause() {}

    override fun onStop() {
        isActivityRunning = false
    }

    override fun onDestroy() {
        stateDispatcher.unsubscribe(this)
    }

    protected fun <T : Action> dispatch(action: T) {
        actionDispatcher.dispatch(action)
    }

    override fun onNext(data: State) {
        if (isActivityRunning) {
            handleStateDifferentThread?.let { it.execute { handleState(state) } }
                    ?: handleState(state)
        }
    }

    abstract fun handleState(state: State)
}
