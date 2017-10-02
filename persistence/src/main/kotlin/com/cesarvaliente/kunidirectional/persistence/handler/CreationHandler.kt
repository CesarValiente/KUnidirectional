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

import com.cesarvaliente.kunidirectional.persistence.Item
import com.cesarvaliente.kunidirectional.persistence.insertOrUpdate
import com.cesarvaliente.kunidirectional.persistence.toPersistenceColor
import com.cesarvaliente.kunidirectional.store.Action
import com.cesarvaliente.kunidirectional.store.CreationAction
import com.cesarvaliente.kunidirectional.store.CreationAction.CreateItemAction
import io.realm.Realm

object CreationHandler : ActionHandler<CreationAction> {

    override fun handle(action: CreationAction, actionDispatcher: (Action) -> Unit) {
        when (action) {
            is CreateItemAction -> createItem(action)
        }
    }

    private fun createItem(action: CreateItemAction) =
            with(action) {
                val item = Item(
                        localId = localId,
                        text = text,
                        favorite = favorite,
                        colorEnum = color.toPersistenceColor(),
                        position = position)

                val db = Realm.getDefaultInstance()
                item.insertOrUpdate(db)
                db.close()
            }

}