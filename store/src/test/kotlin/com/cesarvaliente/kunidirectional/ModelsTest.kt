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
import com.cesarvaliente.kunidirectional.store.generateLocalId
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.not
import org.junit.Assert.assertThat
import org.junit.Test
import org.hamcrest.CoreMatchers.`is` as iz

class ModelsTest {

    @Test
    fun should_generateLocalId() {
        val localId1 = generateLocalId()

        assertThat(localId1, not(containsString("-")))
    }

    @Test
    fun should_generate_different_localId() {
        val localId1 = generateLocalId()
        val localId2 = generateLocalId()

        assertThat(localId1, iz(not(localId2)))
    }

    @Test
    fun should_Item_be_empty() {
        val item = Item()

        assertThat(item.isEmpty(), iz(true))
    }

    @Test
    fun should_item_not_be_empty() {
        val item = Item(text = "test")

        assertThat(item.isEmpty(), iz(false))
    }
}