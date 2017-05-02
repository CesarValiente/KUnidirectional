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

package com.cesarvaliente.kunidirectional.persistence

import com.cesarvaliente.kunidirectional.persistence.functions.CreationFunctions
import com.cesarvaliente.kunidirectional.persistence.functions.DeleteFunctions
import com.cesarvaliente.kunidirectional.persistence.functions.ReadFunctions
import com.cesarvaliente.kunidirectional.persistence.functions.UpdateFunctions
import com.cesarvaliente.kunidirectional.store.ActionDispatcher
import com.cesarvaliente.kunidirectional.store.ExecutorServices
import com.cesarvaliente.kunidirectional.store.ThreadExecutor
import com.cesarvaliente.kunidirectional.store.ThreadExecutorService
import com.cesarvaliente.kunidirectional.store.action.Action
import com.cesarvaliente.kunidirectional.store.action.ActionSubscriber
import com.cesarvaliente.kunidirectional.store.action.CreationAction
import com.cesarvaliente.kunidirectional.store.action.DeleteAction
import com.cesarvaliente.kunidirectional.store.action.ReadAction
import com.cesarvaliente.kunidirectional.store.action.UpdateAction

class PersistenceThreadService : ThreadExecutorService(ExecutorServices.middleware1)

class PersistenceActionSubscriber(actionDispatcher: ActionDispatcher,
                                  persistenceDifferentThread: ThreadExecutor? = null)
    : ActionSubscriber(actionDispatcher, persistenceDifferentThread) {

    override fun reduce(action: Action) {
        println("Persistence thread: ${Thread.currentThread().name}")
        when (action) {
            is CreationAction -> CreationFunctions.apply(action)
            is UpdateAction -> UpdateFunctions.apply(action)
            is ReadAction -> ReadFunctions.apply(action, actionDispatcher)
            is DeleteAction -> DeleteFunctions.apply(action)
        }
    }
}