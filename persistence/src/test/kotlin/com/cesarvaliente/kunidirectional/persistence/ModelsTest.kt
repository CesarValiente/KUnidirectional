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

import org.hamcrest.CoreMatchers.not
import org.junit.Assert.assertThat
import org.junit.Test
import org.hamcrest.CoreMatchers.`is` as iz

class ModelsTest {
    private val LOCAL_ID = "localId"
    private val TEXT = "text"
    private val POSITION = 1L
    private val COLOR = Color.RED
    private val FAVORITE = false


    @Test
    fun should_parse_Color_correctly_with_default_value() {
        val item = Item()
        assertThat(item.getColorAsEnum(), iz(Color.WHITE))
    }

    @Test
    fun should_parse_Color_correctly_from_setter() {
        val item = Item()
        item.setColorAsEnum(Color.BLUE)
        assertThat(item.color.toUpperCase(), iz(Color.BLUE.name))
    }

    @Test
    fun should_Item_be_equal_to_other() {
        val item1 = Item(localId = LOCAL_ID, text = TEXT,
                favorite = FAVORITE, colorEnum = COLOR, position = POSITION)
        val item2 = Item(localId = LOCAL_ID, text = TEXT,
                favorite = FAVORITE, colorEnum = COLOR, position = POSITION)

        assertThat(item1, iz(item2))
    }

    @Test
    fun should_Item_not_be_equal_to_other() {
        val item1 = Item(localId = LOCAL_ID, text = TEXT,
                favorite = FAVORITE, colorEnum = COLOR, position = POSITION)
        val item2 = Item(localId = LOCAL_ID + 1, text = TEXT + 1,
                favorite = FAVORITE, colorEnum = COLOR, position = POSITION)

        assertThat(item1, iz(not(item2)))
    }
}