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

import java.util.concurrent.CopyOnWriteArrayList

abstract class Subscriber<in T>(private val executeOnThisThread: ThreadExecutor? = null) {

    fun onNext(data: T) {
        executeOnThisThread?.execute { handle(data) } ?: handle(data)
    }

    abstract fun handle(data: T)
}

abstract class SideEffect(executeOnThisThread: ThreadExecutor? = null) : Subscriber<Action>(executeOnThisThread)

abstract class StateHandler(executeOnThisThread: ThreadExecutor? = null) : Subscriber<State>(executeOnThisThread)

fun CopyOnWriteArrayList<SideEffect>.dispatch(action: Action) {
    forEach { it.onNext(action) }
}

fun CopyOnWriteArrayList<StateHandler>.dispatch(state: State) {
    forEach { it.onNext(state) }
}