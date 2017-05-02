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

interface Subscriber<in T> {
    fun onNext(data: T)
}

abstract class Dispatcher<T> {
    private val subscriptions = ArrayList<Subscriber<T>>()

    fun subscribe(subscriber: Subscriber<T>): Boolean =
            subscriptions.add(subscriber)

    fun unsubscribe(subscriber: Subscriber<T>): Boolean =
            subscriptions.remove(subscriber)

    fun unsubcribeAll() =
            subscriptions.clear()

    fun isEmpty(): Boolean =
            subscriptions.isEmpty()

    fun count(): Int =
            subscriptions.size

    open fun dispatch(data: T) =
            subscriptions.forEach { it.onNext(data) }

    fun isSubscribed(subscriber: Subscriber<T>): Boolean {
        val isSubscribed = subscriptions.find { it == subscriber }
        return isSubscribed != null
    }
}

@AllOpen
class ActionDispatcher : Dispatcher<Action>()

class StateDispatcher : Dispatcher<State>() {
    var state: State = State()
        private set

    override fun dispatch(data: State) {
        state = data
        super.dispatch(data)
    }
}
