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

package com.cesarvaliente.kunidirectional.store

import com.cesarvaliente.kunidirectional.store.reducer.CreationReducer
import com.cesarvaliente.kunidirectional.store.reducer.DeleteReducer
import com.cesarvaliente.kunidirectional.store.reducer.NavigationReducer
import com.cesarvaliente.kunidirectional.store.reducer.ReadReducer
import com.cesarvaliente.kunidirectional.store.reducer.UpdateReducer
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.LinkedBlockingQueue

interface Subscribers {
    val sideEffects: CopyOnWriteArrayList<SideEffect>
    val stateHandlers: CopyOnWriteArrayList<StateHandler>

    fun dispatch(action: Action)
    fun dispatch(state: State)
}

abstract class Store(override val sideEffects: CopyOnWriteArrayList<SideEffect> = CopyOnWriteArrayList(),
                     override val stateHandlers: CopyOnWriteArrayList<StateHandler> = CopyOnWriteArrayList(),
                     private val storeThread: ThreadExecutor? = null,
                     private val logger: (String, String) -> Unit = { _, _ -> Unit }) : Subscribers {

    private var actions = LinkedBlockingQueue<Action>()

    var state = State()
        protected set

    @Synchronized
    override fun dispatch(action: Action) {
        actions.offer(action)
        when {
            storeThread != null -> storeThread.execute { handle(actions.poll()) }
            else -> handle(actions.poll())
        }
    }

    private fun handle(action: Action) {
        val newState = reduce(action, state)
        dispatch(newState)
        sideEffects.dispatch(action)
    }

    override fun dispatch(state: State) {
        this.state = state
        stateHandlers.dispatch(state)
    }

    private fun reduce(action: Action, currentState: State): State {
        logger("action", action.toString())
        val newState = reduceAction(action, currentState)
        logger("state", newState.toString())
        return newState
    }

    private fun reduceAction(action: Action, currentState: State): State =
            when (action) {
                is CreationAction -> CreationReducer.reduce(action, currentState)
                is UpdateAction -> UpdateReducer.reduce(action, currentState)
                is ReadAction -> ReadReducer.reduce(action, currentState)
                is DeleteAction -> DeleteReducer.reduce(action, currentState)
                is NavigationAction -> NavigationReducer.reduce(action, currentState)
            }
}