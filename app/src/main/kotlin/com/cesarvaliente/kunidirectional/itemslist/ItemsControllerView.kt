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

package com.cesarvaliente.kunidirectional.itemslist

import com.cesarvaliente.kunidirectional.ControllerView
import com.cesarvaliente.kunidirectional.actionDispatcherSingleton
import com.cesarvaliente.kunidirectional.stateDispatcherSingleton
import com.cesarvaliente.kunidirectional.store.ActionDispatcher
import com.cesarvaliente.kunidirectional.store.Item
import com.cesarvaliente.kunidirectional.store.Navigation
import com.cesarvaliente.kunidirectional.store.State
import com.cesarvaliente.kunidirectional.store.StateDispatcher
import com.cesarvaliente.kunidirectional.store.ThreadExecutor
import com.cesarvaliente.kunidirectional.store.action.DeleteAction.DeleteItemAction
import com.cesarvaliente.kunidirectional.store.action.NavigationAction.EditItemScreenAction
import com.cesarvaliente.kunidirectional.store.action.ReadAction.FetchItemsAction
import com.cesarvaliente.kunidirectional.store.action.UpdateAction.ReorderItemsAction
import com.cesarvaliente.kunidirectional.store.action.UpdateAction.UpdateFavoriteAction
import java.lang.ref.WeakReference

class ItemsControllerView(
        val itemsViewCallback: WeakReference<ItemsViewCallback>,
        actionDispatcher: ActionDispatcher = actionDispatcherSingleton,
        stateDispatcher: StateDispatcher = stateDispatcherSingleton,
        handleStateDifferentThread: ThreadExecutor? = null)
    : ControllerView(actionDispatcher, stateDispatcher, handleStateDifferentThread) {

    fun fetchItems() =
            dispatch(FetchItemsAction())

    fun toEditItemScreen(item: Item) =
            dispatch(EditItemScreenAction(item))

    fun reorderItems(items: List<Item>) =
            dispatch(ReorderItemsAction(items))

    fun changeFavoriteStatus(item: Item) =
            dispatch(UpdateFavoriteAction(
                    localId = item.localId,
                    favorite = !item.favorite
            ))

    fun deleteItem(item: Item) =
            dispatch(DeleteItemAction(item.localId))

    override fun handleState(state: State) {
        when (state.navigation) {
            Navigation.ITEMS_LIST -> itemsViewCallback.get()?.updateItems(state.itemsListScreen.items)
            Navigation.EDIT_ITEM -> itemsViewCallback.get()?.goToEditItem()
        }
    }
}