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

import com.cesarvaliente.kunidirectional.persistence.handler.CreationHandler
import com.cesarvaliente.kunidirectional.persistence.handler.DeleteHandler
import com.cesarvaliente.kunidirectional.persistence.handler.ReadHandler
import com.cesarvaliente.kunidirectional.persistence.handler.UpdateHandler
import com.cesarvaliente.kunidirectional.store.Action
import com.cesarvaliente.kunidirectional.store.CreationAction
import com.cesarvaliente.kunidirectional.store.DeleteAction
import com.cesarvaliente.kunidirectional.store.ExecutorServices
import com.cesarvaliente.kunidirectional.store.ReadAction
import com.cesarvaliente.kunidirectional.store.SideEffect
import com.cesarvaliente.kunidirectional.store.Store
import com.cesarvaliente.kunidirectional.store.ThreadExecutor
import com.cesarvaliente.kunidirectional.store.ThreadExecutorService
import com.cesarvaliente.kunidirectional.store.UpdateAction

class PersistenceThreadService : ThreadExecutorService(ExecutorServices.persistence)

class PersistenceSideEffect(val store: Store, persistenceThread: ThreadExecutor? = null)
    : SideEffect(persistenceThread) {

    init {
        store.sideEffects.add(this)
    }

    override fun handle(action: Action) {
        println("Persistence thread: ${Thread.currentThread().name}")
        when (action) {
            is CreationAction -> CreationHandler.handle(action) { store.dispatch(it) }
            is UpdateAction -> UpdateHandler.handle(action) { store.dispatch(it) }
            is ReadAction -> ReadHandler.handle(action) { store.dispatch(it) }
            is DeleteAction -> DeleteHandler.handle(action) { store.dispatch(it) }
        }
    }
}