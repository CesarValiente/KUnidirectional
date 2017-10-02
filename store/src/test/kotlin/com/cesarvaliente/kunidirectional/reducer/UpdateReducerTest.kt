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

import com.cesarvaliente.kunidirectional.store.Color
import com.cesarvaliente.kunidirectional.store.UpdateAction
import com.cesarvaliente.kunidirectional.store.UpdateAction.UpdateColorAction
import com.cesarvaliente.kunidirectional.store.UpdateAction.UpdateItemAction
import com.cesarvaliente.kunidirectional.store.reducer.UpdateReducer
import org.hamcrest.CoreMatchers.not
import org.junit.Assert.assertThat
import org.junit.Test
import org.hamcrest.CoreMatchers.`is` as iz


class UpdateReducerTest {

    @Test
    fun should_reduceItemsCollection_when_ReorderItemsAction_return_reorder_items_list() {
        val item1 = createItem(1)
        val item2 = createItem(2)
        val item3 = createItem(3)

        val defaultList = listOf(item2, item3, item1)
        val reorderedList = listOf(item1, item2, item3)

        val reorderItemsAction = UpdateAction.ReorderItemsAction(reorderedList)
        val reducedItemsCollection = UpdateReducer.reduceItemsCollection(reorderItemsAction, defaultList)

        assertThat(reducedItemsCollection, iz(not(defaultList)))
        assertThat(reducedItemsCollection, iz(reorderedList))
    }

    @Test
    fun should_shouldReduceItem_when_UpdateItemAction() {
        val item = createItem(1)
        val updateItemAction = UpdateItemAction(localId = item.localId, text = "new text", color = Color.GREEN)

        val shouldReduceItem = UpdateReducer.shouldReduceItem(updateItemAction, item)

        assertThat(shouldReduceItem, iz(true))
    }

    @Test
    fun should_shouldReduceItem_when_UpdateFavoriteAction() {
        val item = createItem(1)
        val updateFavoriteAction = UpdateAction.UpdateFavoriteAction(localId = item.localId, favorite = true)

        val shouldReduceItem = UpdateReducer.shouldReduceItem(updateFavoriteAction, item)

        assertThat(shouldReduceItem, iz(true))
    }

    @Test
    fun should_shouldReduceItem_when_UpdateColorAction() {
        val item = createItem(1)
        val updateColorAction = UpdateColorAction(localId = item.localId, color = Color.GREEN)

        val shouldReduceItem = UpdateReducer.shouldReduceItem(updateColorAction, item)

        assertThat(shouldReduceItem, iz(true))
    }

    @Test
    fun should_not_shouldReduceItem_when_ReorderItemsAction() {
        val item1 = createItem(1)
        val item2 = createItem(2)
        val item3 = createItem(3)
        val itemsList = listOf(item1, item2, item3)

        val reorderItemsAction = UpdateAction.ReorderItemsAction(itemsList)
        val shouldReduceItem = UpdateReducer.shouldReduceItem(reorderItemsAction, item1)

        assertThat(shouldReduceItem, iz(false))
    }

    @Test
    fun should_changeItemFields_when_UpdateItemAction() {
        val item = createItem(1)
        val NEW_TEXT = "new text"
        val updateItemAction = UpdateItemAction(localId = item.localId, text = NEW_TEXT, color = Color.GREEN)

        val reducedItem = UpdateReducer.changeItemFields(updateItemAction, item)

        assertThat(reducedItem, iz(not(item)))
        assertThat(reducedItem.text, iz(NEW_TEXT))
        assertThat(reducedItem.color, iz(Color.GREEN))
    }

    @Test
    fun should_changeItemFields_when_UpdateFavoriteAction() {
        val item = createItem(1)
        val updateFavoriteAction = UpdateAction.UpdateFavoriteAction(localId = item.localId, favorite = true)

        val reducedItem = UpdateReducer.changeItemFields(updateFavoriteAction, item)

        assertThat(reducedItem, iz(not(item)))
        assertThat(reducedItem.text, iz(item.text))
        assertThat(reducedItem.color, iz(item.color))
        assertThat(reducedItem.favorite, iz(true))
    }

    @Test
    fun should_changeItemFields_when_UpdateColorAction() {
        val item = createItem(1)
        val updateColorAction = UpdateColorAction(localId = item.localId, color = Color.GREEN)

        val reducedItem = UpdateReducer.changeItemFields(updateColorAction, item)

        assertThat(reducedItem, iz(not(item)))
        assertThat(reducedItem.text, iz(item.text))
        assertThat(reducedItem.color, iz(Color.GREEN))
    }
}