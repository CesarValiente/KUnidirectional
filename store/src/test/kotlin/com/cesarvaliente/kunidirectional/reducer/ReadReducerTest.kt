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

import com.cesarvaliente.kunidirectional.store.action.ReadAction
import com.cesarvaliente.kunidirectional.store.reducer.ReadReducer
import org.hamcrest.CoreMatchers.not
import org.junit.Assert.assertThat
import org.junit.Test
import org.hamcrest.CoreMatchers.`is` as iz


class ReadReducerTest {

    @Test
    fun should_reduceItemsCollection_when_ItemsLoadedCollection_return_new_items_list() {
        val item1 = createItem(1)
        val item2 = createItem(2)
        val item3 = createItem(3)
        val listOfItems = listOf(item1, item2, item3)
        val itemsLoadedAction = ReadAction.ItemsLoadedAction(listOfItems)

        val reducedItemsCollection = ReadReducer.reduceItemsCollection(itemsLoadedAction, emptyList())
        assertThat(reducedItemsCollection, iz(not(emptyList())))
        assertThat(reducedItemsCollection.size, iz(3))
        assertThat(reducedItemsCollection, iz(listOfItems))
    }

    @Test
    fun should_reduceItemsCollection_when_FetchItemsAction_return_unmodified_items_list() {
        val item1 = createItem(1)
        val item2 = createItem(2)
        val item3 = createItem(3)
        val listOfItems = listOf(item1, item2, item3)
        val itemsLoadedAction = ReadAction.FetchItemsAction()

        val reducedItemsCollection = ReadReducer.reduceItemsCollection(itemsLoadedAction, listOfItems)
        assertThat(reducedItemsCollection, iz(listOfItems))
    }
}