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

import com.cesarvaliente.kunidirectional.persistence.PersistenceActionSubscriber
import com.cesarvaliente.kunidirectional.persistence.PersistenceThreadService
import com.cesarvaliente.kunidirectional.store.ActionDispatcher
import com.cesarvaliente.kunidirectional.store.StateDispatcher
import com.cesarvaliente.kunidirectional.store.StoreActionSubscriber
import com.cesarvaliente.kunidirectional.store.StoreThreadService


val stateDispatcherSingleton: StateDispatcher by lazy {
    StateDispatcher()
}

val actionDispatcherSingleton: ActionDispatcher by lazy {
    ActionDispatcher()
}

val storeActionSubscriberSingleton: StoreActionSubscriber by lazy {
    StoreActionSubscriber(
            actionDispatcher = actionDispatcherSingleton,
            actionThread = StoreThreadService(),
            stateDispatcher = stateDispatcherSingleton)
}

val persistenceActionSubscriberSingleton: PersistenceActionSubscriber by lazy {
    PersistenceActionSubscriber(
            actionDispatcher = actionDispatcherSingleton,
            persistenceDifferentThread = PersistenceThreadService())
}

