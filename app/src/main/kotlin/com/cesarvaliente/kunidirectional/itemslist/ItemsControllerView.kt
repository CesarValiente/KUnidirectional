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
import com.cesarvaliente.kunidirectional.store.DeleteAction.DeleteItemAction
import com.cesarvaliente.kunidirectional.store.Item
import com.cesarvaliente.kunidirectional.store.Navigation
import com.cesarvaliente.kunidirectional.store.NavigationAction.EditItemScreenAction
import com.cesarvaliente.kunidirectional.store.ReadAction.FetchItemsAction
import com.cesarvaliente.kunidirectional.store.State
import com.cesarvaliente.kunidirectional.store.Store
import com.cesarvaliente.kunidirectional.store.ThreadExecutor
import com.cesarvaliente.kunidirectional.store.UpdateAction.ReorderItemsAction
import com.cesarvaliente.kunidirectional.store.UpdateAction.UpdateFavoriteAction
import java.lang.ref.WeakReference

class ItemsControllerView(
        val itemsViewCallback: WeakReference<ItemsViewCallback>,
        store: Store,
        mainThread: ThreadExecutor? = null)
    : ControllerView(store, mainThread) {

    fun fetchItems() =
            store.dispatch(FetchItemsAction())

    fun toEditItemScreen(item: Item) =
            store.dispatch(EditItemScreenAction(item))

    fun reorderItems(items: List<Item>) =
            store.dispatch(ReorderItemsAction(items))

    fun changeFavoriteStatus(item: Item) =
            store.dispatch(UpdateFavoriteAction(localId = item.localId, favorite = !item.favorite))

    fun deleteItem(item: Item) =
            store.dispatch(DeleteItemAction(item.localId))

    override fun handleState(state: State) {
        when (state.navigation) {
            Navigation.ITEMS_LIST -> itemsViewCallback.get()?.updateItems(state.itemsListScreen.items)
            Navigation.EDIT_ITEM -> itemsViewCallback.get()?.goToEditItem()
        }
    }
}