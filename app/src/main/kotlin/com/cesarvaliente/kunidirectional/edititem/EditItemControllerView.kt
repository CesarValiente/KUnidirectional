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

package com.cesarvaliente.kunidirectional.edititem

import com.cesarvaliente.kunidirectional.ControllerView
import com.cesarvaliente.kunidirectional.actionDispatcherSingleton
import com.cesarvaliente.kunidirectional.stateDispatcherSingleton
import com.cesarvaliente.kunidirectional.store.ActionDispatcher
import com.cesarvaliente.kunidirectional.store.Color
import com.cesarvaliente.kunidirectional.store.Navigation
import com.cesarvaliente.kunidirectional.store.State
import com.cesarvaliente.kunidirectional.store.StateDispatcher
import com.cesarvaliente.kunidirectional.store.ThreadExecutor
import com.cesarvaliente.kunidirectional.store.action.CreationAction.CreateItemAction
import com.cesarvaliente.kunidirectional.store.action.NavigationAction.ItemsScreenAction
import com.cesarvaliente.kunidirectional.store.action.UpdateAction.UpdateColorAction
import com.cesarvaliente.kunidirectional.store.action.UpdateAction.UpdateItemAction
import java.lang.ref.WeakReference

class EditItemControllerView(
        val editItemViewCallback: WeakReference<EditItemViewCallback>,
        actionDispatcher: ActionDispatcher = actionDispatcherSingleton,
        stateDispatcher: StateDispatcher = stateDispatcherSingleton,
        handleStateDifferentThread: ThreadExecutor? = null)
    : ControllerView(actionDispatcher, stateDispatcher, handleStateDifferentThread) {

    fun createItem(localId: String, text: String, favorite: Boolean, color: Color, position: Long) =
            dispatch(CreateItemAction(localId, text, favorite, color, position))

    fun updateItem(localId: String, text: String, color: Color) =
            dispatch(UpdateItemAction(localId, text, color))

    fun updateColor(localId: String, color: Color) =
            dispatch(UpdateColorAction(localId, color))

    fun backToItems() =
            dispatch(ItemsScreenAction())

    override fun handleState(state: State) {
        println("Thread hadleState: ${Thread.currentThread().name}")
        when (state.navigation) {
            Navigation.EDIT_ITEM -> editItemViewCallback.get()?.updateItem(state.editItemScreen.currentItem)
            Navigation.ITEMS_LIST -> editItemViewCallback.get()?.backToItemsList()
        }
    }
}