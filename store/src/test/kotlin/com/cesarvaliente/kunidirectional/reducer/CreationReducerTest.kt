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

package com.cesarvaliente.kunidirectional.reducer

import com.cesarvaliente.kunidirectional.store.Item
import com.cesarvaliente.kunidirectional.store.LOCAL_ID
import com.cesarvaliente.kunidirectional.store.CreationAction.CreateItemAction
import com.cesarvaliente.kunidirectional.store.reducer.CreationReducer
import org.hamcrest.CoreMatchers.not
import org.junit.Assert.assertThat
import org.junit.Test
import org.hamcrest.CoreMatchers.`is` as iz


class CreationReducerTest {
    @Test
    fun should_reduceItemsCollection_when_CreateItemAction_and_empty_collection() {
        val createItemAction = CreateItemAction(
                localId = LOCAL_ID,
                text = TEXT,
                favorite = FAVORITE,
                color = COLOR,
                position = POSITION)

        val itemsReduced = CreationReducer.reduceItemsCollection(
                action = createItemAction,
                currentItems = emptyList())

        assertThat(itemsReduced, iz(not(emptyList())))
        assertThat(itemsReduced.count(), iz(1))
        assertThat(itemsReduced, iz(listOf(
                Item(localId = LOCAL_ID,
                        text = TEXT, favorite = FAVORITE, color = COLOR,
                        position = POSITION))))
    }

    @Test
    fun should_reduceItemsCollection_when_CreateItemAction_and_non_empty_collection() {
        val item1 = createItem(1)
        val item2 = createItem(2)

        val listOfItems = listOf(item1, item2)

        val action = CreateItemAction(
                localId = LOCAL_ID + 3,
                text = TEXT + 3,
                favorite = FAVORITE,
                color = COLOR,
                position = POSITION + 3)

        val itemsReduced = CreationReducer.reduceItemsCollection(
                action = action,
                currentItems = listOfItems)

        assertThat(itemsReduced, iz(not(emptyList())))
        assertThat(itemsReduced.count(), iz(3))
        assertThat(itemsReduced, iz(listOf(
                item1, item2, Item(localId = LOCAL_ID + 3,
                text = TEXT + 3, favorite = FAVORITE, color = COLOR, position = POSITION + 3))))
    }
}
