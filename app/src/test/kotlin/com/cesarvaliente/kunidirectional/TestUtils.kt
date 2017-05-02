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

package com.cesarvaliente.kunidirectional

import com.cesarvaliente.kunidirectional.store.Color
import com.cesarvaliente.kunidirectional.store.Item

internal val LOCAL_ID = "localId"
internal val TEXT = "new item"
internal val COLOR = Color.RED
internal val FAVORITE = false
internal val POSITION = 1L

internal fun createItem(index: Int): Item =
        Item(localId = LOCAL_ID + index,
                text = TEXT + index,
                favorite = FAVORITE,
                color = COLOR,
                position = POSITION + index)