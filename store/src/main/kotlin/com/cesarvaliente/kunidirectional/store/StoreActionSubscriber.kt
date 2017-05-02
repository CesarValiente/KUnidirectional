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

import com.cesarvaliente.kunidirectional.store.action.Action
import com.cesarvaliente.kunidirectional.store.action.ActionSubscriber
import com.cesarvaliente.kunidirectional.store.action.CreationAction
import com.cesarvaliente.kunidirectional.store.action.DeleteAction
import com.cesarvaliente.kunidirectional.store.action.NavigationAction
import com.cesarvaliente.kunidirectional.store.action.ReadAction
import com.cesarvaliente.kunidirectional.store.action.UpdateAction
import com.cesarvaliente.kunidirectional.store.reducer.CreationReducer
import com.cesarvaliente.kunidirectional.store.reducer.DeleteReducer
import com.cesarvaliente.kunidirectional.store.reducer.NavigationReducer
import com.cesarvaliente.kunidirectional.store.reducer.ReadReducer
import com.cesarvaliente.kunidirectional.store.reducer.UpdateReducer

class StoreActionSubscriber(actionDispatcher: ActionDispatcher,
                            actionThread: ThreadExecutor? = null,
                            val stateDispatcher: StateDispatcher)
    : ActionSubscriber(actionDispatcher, actionThread) {

    override fun reduce(action: Action) {
        println("Thread reducer: ${Thread.currentThread().name}")
        println("--- Action : $action")
        val currentState = stateDispatcher.state
        val newState = when (action) {
            is CreationAction -> CreationReducer.reduce(action, currentState)
            is UpdateAction -> UpdateReducer.reduce(action, currentState)
            is ReadAction -> ReadReducer.reduce(action, currentState)
            is DeleteAction -> DeleteReducer.reduce(action, currentState)
            is NavigationAction -> NavigationReducer.reduce(action, currentState)
            else -> currentState
        }
        println(">>>> New state: $newState")
        stateDispatcher.dispatch(newState)
    }
}