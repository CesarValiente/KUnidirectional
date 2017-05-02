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

import com.cesarvaliente.kunidirectional.store.Item
import com.cesarvaliente.kunidirectional.store.action.UpdateAction
import com.cesarvaliente.kunidirectional.store.action.UpdateAction.ReorderItemsAction
import com.cesarvaliente.kunidirectional.store.action.UpdateAction.UpdateColorAction
import com.cesarvaliente.kunidirectional.store.action.UpdateAction.UpdateFavoriteAction
import com.cesarvaliente.kunidirectional.store.action.UpdateAction.UpdateItemAction

object UpdateReducer : Reducer<UpdateAction>() {

    override fun reduceItemsCollection(action: UpdateAction, currentItems: List<Item>): List<Item> =
            when (action) {
                is ReorderItemsAction -> action.items
                else -> super.reduceItemsCollection(action, currentItems)
            }

    override fun shouldReduceItem(action: UpdateAction, currentItem: Item): Boolean =
            when (action) {
                is UpdateItemAction -> action.localId == currentItem.localId
                is UpdateFavoriteAction -> action.localId == currentItem.localId
                is UpdateColorAction -> action.localId == currentItem.localId
                else -> false
            }

    override fun changeItemFields(action: UpdateAction, currentItem: Item): Item =
            when (action) {
                is UpdateItemAction -> currentItem.copy(
                        text = action.text,
                        color = action.color)
                is UpdateFavoriteAction -> currentItem.copy(
                        favorite = action.favorite
                )
                is UpdateColorAction -> currentItem.copy(
                        color = action.color
                )
                else -> currentItem
            }
}
