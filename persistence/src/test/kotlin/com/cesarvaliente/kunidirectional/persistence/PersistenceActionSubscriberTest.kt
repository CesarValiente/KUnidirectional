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

import com.cesarvaliente.kunidirectional.store.ActionDispatcher
import org.junit.Assert.assertThat
import org.junit.Test
import org.hamcrest.CoreMatchers.`is` as iz

class PersistenceActionSubscriberTest {
    val actionDispatcher = ActionDispatcher()

    @Test
    fun should_subscribe_to_dispatcher() {
        val persistenceActionSubscriber = PersistenceActionSubscriber(
                actionDispatcher = actionDispatcher)

        with(actionDispatcher) {
            assertThat(isEmpty(), iz(false))
            assertThat(isSubscribed(persistenceActionSubscriber), iz(true))
        }
    }
}