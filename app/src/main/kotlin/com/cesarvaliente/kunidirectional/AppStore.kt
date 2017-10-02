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

import android.util.Log
import com.cesarvaliente.kunidirectional.persistence.PersistenceSideEffect
import com.cesarvaliente.kunidirectional.persistence.PersistenceThreadService
import com.cesarvaliente.kunidirectional.store.Store
import com.cesarvaliente.kunidirectional.store.StoreThreadService

object AppStore : Store(
        storeThread = StoreThreadService(),
        logger = { tag, message -> Log.d(tag, message) }) {

    fun enablePersistence() {
        PersistenceSideEffect(store = this, persistenceThread = PersistenceThreadService())
    }
}

