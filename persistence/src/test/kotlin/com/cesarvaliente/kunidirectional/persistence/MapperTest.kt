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

import org.junit.Assert.assertThat
import org.junit.Test
import com.cesarvaliente.kunidirectional.persistence.Color as PersistenceColor
import com.cesarvaliente.kunidirectional.persistence.Item as PersistenceItem
import com.cesarvaliente.kunidirectional.store.Color as StoreColor
import com.cesarvaliente.kunidirectional.store.Item as StoreItem
import org.hamcrest.CoreMatchers.`is` as iz

class MapperTest {
    private val LOCAL_ID = "localId"
    private val TEXT = "text"
    private val POSITION = 1L
    private val COLOR = StoreColor.RED
    private val FAVORITE = false

    @Test
    fun should_parse_StoreItem_to_PersistenceItem_correctly() {
        val storeItem = StoreItem(localId = LOCAL_ID, text = TEXT,
                favorite = FAVORITE, color = COLOR, position = POSITION)
        val persistenceItem = storeItem.toPersistenceItem()

        with(persistenceItem) {
            assertThat(localId, iz(storeItem.localId))
            assertThat(text, iz(storeItem.text))
            assertThat(favorite, iz(storeItem.favorite))
            assertThat(getColorAsEnum().name, iz(storeItem.color.name))
            assertThat(position, iz(storeItem.position))
        }
    }

    @Test
    fun should_parse_PersistenceItem_to_StoreItem_correctly() {
        val persistenceItem = PersistenceItem(localId = LOCAL_ID, text = TEXT,
                favorite = FAVORITE, colorEnum = PersistenceColor.BLUE, position = POSITION)
        val storeItem = persistenceItem.toStoreItem()

        with(storeItem) {
            assertThat(localId, iz(persistenceItem.localId))
            assertThat(text, iz(persistenceItem.text))
            assertThat(favorite, iz(persistenceItem.favorite))
            assertThat(color.name, iz(persistenceItem.getColorAsEnum().name))
            assertThat(position, iz(persistenceItem.position))
        }
    }

    @Test
    fun should_parse_StoreColor_BLUE_to_PersistenceColor_correctly() {
        val storeColor = StoreColor.BLUE
        assertThat(storeColor.name, iz(storeColor.toPersistenceColor().name))
    }

    @Test
    fun should_parse_StoreColor_WHITE_to_PersistenceColor_correctly() {
        val storeColor = StoreColor.WHITE
        assertThat(storeColor.name, iz(storeColor.toPersistenceColor().name))
    }

    @Test
    fun should_parse_StoreColor_GREEN_to_PersistenceColor_correctly() {
        val storeColor = StoreColor.GREEN
        assertThat(storeColor.name, iz(storeColor.toPersistenceColor().name))
    }

    @Test
    fun should_parse_StoreColor_RED_to_PersistenceColor_correctly() {
        val storeColor = StoreColor.RED
        assertThat(storeColor.name, iz(storeColor.toPersistenceColor().name))
    }

    @Test
    fun should_parse_StoreColor_YELLOW_to_PersistenceColor_correctly() {
        val storeColor = StoreColor.YELLOW
        assertThat(storeColor.name, iz(storeColor.toPersistenceColor().name))
    }

    @Test
    fun should_parse_PersistenceColor_BLUE_to_StoreColor_correctly() {
        val persistenceColor = PersistenceColor.BLUE
        assertThat(persistenceColor.name, iz(persistenceColor.toStoreColor().name))
    }

    @Test
    fun should_parse_PersistenceColor_WHITE_to_StoreColor_correctly() {
        val persistenceColor = PersistenceColor.WHITE
        assertThat(persistenceColor.name, iz(persistenceColor.toStoreColor().name))
    }

    @Test
    fun should_parse_PersistenceColor_GREEN_to_StoreColor_correctly() {
        val persistenceColor = PersistenceColor.GREEN
        assertThat(persistenceColor.name, iz(persistenceColor.toStoreColor().name))
    }

    @Test
    fun should_parse_PersistenceColor_RED_to_StoreColor_correctly() {
        val persistenceColor = PersistenceColor.RED
        assertThat(persistenceColor.name, iz(persistenceColor.toStoreColor().name))
    }

    @Test
    fun should_parse_PersistenceColor_YELLOW_to_StoreColor_correctly() {
        val persistenceColor = PersistenceColor.YELLOW
        assertThat(persistenceColor.name, iz(persistenceColor.toStoreColor().name))
    }
}