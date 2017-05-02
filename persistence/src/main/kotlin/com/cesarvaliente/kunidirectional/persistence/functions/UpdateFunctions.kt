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

package com.cesarvaliente.kunidirectional.persistence.functions

import com.cesarvaliente.kunidirectional.persistence.queryByLocalId
import com.cesarvaliente.kunidirectional.persistence.toPersistenceColor
import com.cesarvaliente.kunidirectional.persistence.update
import com.cesarvaliente.kunidirectional.store.ActionDispatcher
import com.cesarvaliente.kunidirectional.store.action.UpdateAction
import com.cesarvaliente.kunidirectional.store.action.UpdateAction.ReorderItemsAction
import com.cesarvaliente.kunidirectional.store.action.UpdateAction.UpdateColorAction
import com.cesarvaliente.kunidirectional.store.action.UpdateAction.UpdateFavoriteAction
import com.cesarvaliente.kunidirectional.store.action.UpdateAction.UpdateItemAction
import io.realm.Realm

object UpdateFunctions : ActionFunction<UpdateAction> {

    override fun apply(action: UpdateAction, actionDispatcher: ActionDispatcher?) {
        when (action) {
            is ReorderItemsAction -> reorderItems(action)
            is UpdateItemAction -> updateItem(action)
            is UpdateFavoriteAction -> updateFavorite(action)
            is UpdateColorAction -> updateColor(action)
        }
    }

    private fun reorderItems(action: ReorderItemsAction) {
        if (action.items.isEmpty()) return

        val db = Realm.getDefaultInstance()
        action.items.forEach { item ->
            val managedItem = db.queryByLocalId(item.localId)
            managedItem?.update(db) { position = item.position }
        }
        db.close()
    }

    private fun updateItem(action: UpdateItemAction) {
        val db = Realm.getDefaultInstance()
        val managedItem = db.queryByLocalId(action.localId)
        managedItem?.update(db) {
            text = action.text
            setColorAsEnum(action.color.toPersistenceColor())
        }
        db.close()
    }

    private fun updateFavorite(action: UpdateFavoriteAction) {
        val db = Realm.getDefaultInstance()
        val managedItem = db.queryByLocalId(action.localId)
        managedItem?.update(db) {
            favorite = action.favorite
        }
        db.close()
    }

    private fun updateColor(action: UpdateColorAction) {
        val db = Realm.getDefaultInstance()
        val managedItem = db.queryByLocalId(action.localId)
        managedItem?.update(db) {
            setColorAsEnum(action.color.toPersistenceColor())
        }
        db.close()
    }
}