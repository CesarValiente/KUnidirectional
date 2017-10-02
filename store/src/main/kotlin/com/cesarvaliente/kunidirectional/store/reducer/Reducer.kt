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

package com.cesarvaliente.kunidirectional.store.reducer

import com.cesarvaliente.kunidirectional.store.EditItemScreen
import com.cesarvaliente.kunidirectional.store.Item
import com.cesarvaliente.kunidirectional.store.ItemsListScreen
import com.cesarvaliente.kunidirectional.store.Navigation
import com.cesarvaliente.kunidirectional.store.State
import com.cesarvaliente.kunidirectional.store.Action
import com.cesarvaliente.kunidirectional.store.findAndMap

abstract class Reducer<in T : Action> {

    open fun reduce(action: T, currentState: State) =
            with(currentState) {
                currentState.copy(
                        itemsListScreen = reduceItemsListScreen(action, itemsListScreen),
                        editItemScreen = reduceEditItemScreen(action, editItemScreen),
                        navigation = reduceNavigation(action, navigation)
                )
            }

    open fun reduceItemsListScreen(action: T, itemsListScreen: ItemsListScreen) =
            itemsListScreen.copy(items = reduceItemsCollection(action, itemsListScreen.items))

    open fun reduceItemsCollection(action: T, currentItems: List<Item>) =
            currentItems.findAndMap(
                    find = { shouldReduceItem(action, it) },
                    map = { changeItemFields(action, it) })

    open fun reduceEditItemScreen(action: T, editItemScreen: EditItemScreen) =
            editItemScreen.copy(
                    currentItem = reduceCurrentItem(action, editItemScreen.currentItem))

    open fun reduceCurrentItem(action: T, currentItem: Item) =
            if (shouldReduceItem(action, currentItem)) changeItemFields(action, currentItem)
            else currentItem

    open fun shouldReduceItem(action: T, currentItem: Item) = false

    open fun changeItemFields(action: T, currentItem: Item) = currentItem

    open fun reduceNavigation(action: T, currentNavigation: Navigation) = currentNavigation
}