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

import com.cesarvaliente.kunidirectional.store.Item
import com.cesarvaliente.kunidirectional.store.State
import com.cesarvaliente.kunidirectional.store.StateHandler
import com.cesarvaliente.kunidirectional.store.Store
import com.cesarvaliente.kunidirectional.store.ThreadExecutor

abstract class ControllerView(
        val store: Store,
        mainThread: ThreadExecutor? = null)
    : LifecycleCallbacks, StateHandler(mainThread) {

    override var isActivityRunning: Boolean = false

    val state: State
        get() = store.state

    val currentItem: Item
        get() = state.editItemScreen.currentItem

    override fun onStart() {
        isActivityRunning = true
        store.stateHandlers.add(this)
        handleState(store.state)
    }

    override fun onResume() {}

    override fun onPause() {}

    override fun onStop() {
        isActivityRunning = false
    }

    override fun onDestroy() {
        store.stateHandlers.remove(this)
    }

    override fun handle(data: State) {
        if (isActivityRunning) handleState(state)
    }

    abstract fun handleState(state: State)
}
