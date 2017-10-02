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

import com.cesarvaliente.kunidirectional.store.DeleteAction
import com.cesarvaliente.kunidirectional.store.reducer.DeleteReducer
import org.hamcrest.CoreMatchers.not
import org.junit.Assert.assertThat
import org.junit.Test
import org.hamcrest.CoreMatchers.`is` as iz


class DeleteReducerTest {

    @Test
    fun should_reduceItemsCollection_when_DeleteItemAction_item_is_not_in_list() {
        val item1 = createItem(1)
        val listOfItems = listOf(item1)

        val deleteItemAction = DeleteAction.DeleteItemAction(LOCAL_ID + 2)
        val collectionReduced = DeleteReducer.reduceItemsCollection(deleteItemAction, listOfItems)

        assertThat(collectionReduced, iz(not(emptyList())))
        assertThat(collectionReduced.size, iz(1))
        assertThat(collectionReduced[0], iz(item1))
    }

    @Test
    fun should_reduceItemsCollection_when_DeleteItemAction_and_returns_empty_list() {
        val item1 = createItem(1)
        val listOfItems = listOf(item1)

        val deleteItemAction = DeleteAction.DeleteItemAction(item1.localId)
        val collectionReduced = DeleteReducer.reduceItemsCollection(deleteItemAction, listOfItems)

        assertThat(collectionReduced, iz(emptyList()))
    }

    @Test
    fun should_reduceItemsCollection_when_DeleteItemAction_and_returns_non_empty_list() {
        val item1 = createItem(1)
        val item2 = createItem(2)
        val listOfItems = listOf(item1, item2)

        val deleteItemAction = DeleteAction.DeleteItemAction(item1.localId)
        val collectionReduced = DeleteReducer.reduceItemsCollection(deleteItemAction, listOfItems)

        assertThat(collectionReduced, iz(not(emptyList())))
        assertThat(collectionReduced.size, iz(1))
        assertThat(collectionReduced[0], iz(item2))
    }

    @Test
    fun should_reduceCurrentItem_when_DeleteItemAction_and_items_are_same() {
        val item1 = createItem(1)
        val deleteItemAction = DeleteAction.DeleteItemAction(item1.localId)

        val itemReduced = DeleteReducer.reduceCurrentItem(deleteItemAction, item1)
        assertThat(itemReduced, iz(not(item1)))
        assertThat(itemReduced.isEmpty(), iz(true))
    }

    @Test
    fun should_reduceCurrentItem_when_DeleteItemAction_and_items_are_not_same() {
        val item1 = createItem(1)
        val deleteItemAction = DeleteAction.DeleteItemAction(LOCAL_ID + 2)

        val itemReduced = DeleteReducer.reduceCurrentItem(deleteItemAction, item1)
        assertThat(itemReduced, iz(item1))
    }
}