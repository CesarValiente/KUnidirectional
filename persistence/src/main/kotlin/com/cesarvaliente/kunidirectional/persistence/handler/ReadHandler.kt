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

package com.cesarvaliente.kunidirectional.persistence.handler

import com.cesarvaliente.kunidirectional.persistence.queryAllItemsSortedByPosition
import com.cesarvaliente.kunidirectional.persistence.toStoreItemsList
import com.cesarvaliente.kunidirectional.store.Action
import com.cesarvaliente.kunidirectional.store.ReadAction
import com.cesarvaliente.kunidirectional.store.ReadAction.FetchItemsAction
import com.cesarvaliente.kunidirectional.store.ReadAction.ItemsLoadedAction
import io.realm.Realm

object ReadHandler : ActionHandler<ReadAction> {

    override fun handle(action: ReadAction, actionDispatcher: (Action) -> Unit) {
        when (action) {
            is FetchItemsAction -> fetchAllItems(actionDispatcher)
        }
    }

    private fun fetchAllItems(actionDispatcher: (Action) -> Unit) {
        val db = Realm.getDefaultInstance()
        val persistenceItems = db.queryAllItemsSortedByPosition()
        val storeItems = persistenceItems.toStoreItemsList()
        db.close()

        actionDispatcher.invoke(ItemsLoadedAction(storeItems))
    }
}