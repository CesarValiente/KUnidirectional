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
import com.cesarvaliente.kunidirectional.store.Navigation
import com.cesarvaliente.kunidirectional.store.NavigationAction
import com.cesarvaliente.kunidirectional.store.NavigationAction.EditItemScreenAction
import com.cesarvaliente.kunidirectional.store.NavigationAction.ItemsScreenAction

object NavigationReducer : Reducer<NavigationAction>() {

    override fun reduceEditItemScreen(action: NavigationAction, editItemScreen: EditItemScreen): EditItemScreen =
            when (action) {
                is EditItemScreenAction -> editItemScreen.copy(
                        currentItem = action.item)
                else -> super.reduceEditItemScreen(action, editItemScreen)
            }

    override fun reduceNavigation(action: NavigationAction, currentNavigation: Navigation): Navigation =
            when (action) {
                is EditItemScreenAction -> Navigation.EDIT_ITEM
                is ItemsScreenAction -> Navigation.ITEMS_LIST
            }
}