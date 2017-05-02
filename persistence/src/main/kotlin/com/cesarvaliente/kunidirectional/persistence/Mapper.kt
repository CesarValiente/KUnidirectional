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

package com.cesarvaliente.kunidirectional.persistence

import com.cesarvaliente.kunidirectional.persistence.Color as PersistenceColor
import com.cesarvaliente.kunidirectional.persistence.Item as PersistenceItem
import com.cesarvaliente.kunidirectional.store.Color as StoreColor
import com.cesarvaliente.kunidirectional.store.Item as StoreItem

fun StoreItem.toPersistenceItem(): PersistenceItem =
        with(this) {
            PersistenceItem(localId, text, favorite, color.toPersistenceColor(), position)
        }

fun StoreColor.toPersistenceColor(): PersistenceColor =
        when (this) {
            StoreColor.BLUE -> PersistenceColor.BLUE
            StoreColor.GREEN -> PersistenceColor.GREEN
            StoreColor.RED -> PersistenceColor.RED
            StoreColor.WHITE -> PersistenceColor.WHITE
            StoreColor.YELLOW -> PersistenceColor.YELLOW
        }

fun PersistenceColor.toStoreColor(): StoreColor =
        when (this) {
            PersistenceColor.BLUE -> StoreColor.BLUE
            PersistenceColor.GREEN -> StoreColor.GREEN
            PersistenceColor.RED -> StoreColor.RED
            PersistenceColor.WHITE -> StoreColor.WHITE
            PersistenceColor.YELLOW -> StoreColor.YELLOW
        }

fun PersistenceItem.toStoreItem(): StoreItem =
        with(this) {
            StoreItem(localId, text, favorite, getColorAsEnum().toStoreColor(), position)
        }

fun List<PersistenceItem>.toStoreItemsList(): List<StoreItem> =
        this.map(PersistenceItem::toStoreItem)
