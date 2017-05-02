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
import com.cesarvaliente.kunidirectional.persistence.Color as PersistenceColor
import com.cesarvaliente.kunidirectional.persistence.Item as PersistenceItem
import com.cesarvaliente.kunidirectional.store.Color as StoreColor
import com.cesarvaliente.kunidirectional.store.Item as StoreItem
import org.hamcrest.core.Is.`is` as iz

const val LOCAL_ID_VALUE = "localId"
const val TEXT_VALUE = "text"
const val POSITION_VALUE = 1L
val COLOR_VALUE = com.cesarvaliente.kunidirectional.store.Color.RED
const val FAVORITE_VALUE = false


fun createPersistenceItem(index: Int): PersistenceItem =
        createStoreItem(index).toPersistenceItem()

fun createStoreItem(index: Int): StoreItem =
        StoreItem(
                localId = LOCAL_ID_VALUE + index,
                text = TEXT_VALUE + index,
                favorite = FAVORITE_VALUE,
                color = COLOR_VALUE,
                position = POSITION_VALUE + index)

/**
This function asserts that the current item is the same the given item.
We can not use equals() from Item, since a result from Realm is not a real Item, but
a proxy that matches our Item, so equals() always fails since we are comparing Item with a ProxyItem */
fun PersistenceItem.assertIsEqualsTo(otherItem: PersistenceItem) {
    assertThat(localId, iz(otherItem.localId))
    assertThat(text, iz(otherItem.text))
    assertThat(color, iz(otherItem.color))
    assertThat(favorite, iz(otherItem.favorite))
    assertThat(position, iz(otherItem.position))
}


