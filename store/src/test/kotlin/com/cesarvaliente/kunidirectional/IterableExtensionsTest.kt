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

import com.cesarvaliente.kunidirectional.store.findAndMap
import com.cesarvaliente.kunidirectional.store.firstOrDefault
import org.hamcrest.CoreMatchers.hasItem
import org.hamcrest.CoreMatchers.not
import org.junit.Assert.assertThat
import org.junit.Test
import org.hamcrest.CoreMatchers.`is` as iz

class IterableExtensionsTest {

    @Test
    fun should_return_first() {
        val dummyList = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        val element = dummyList.firstOrDefault(predicate = { it % 4 == 0 }, default = 10)

        assertThat(element, iz(4))
    }

    @Test
    fun should_return_default() {
        val dummyList = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        val element = dummyList.firstOrDefault(predicate = { it % 12 == 0 }, default = 10)

        assertThat(element, iz(10))
    }

    @Test
    fun should_find_and_map() {
        val dummyList = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        val dummyListMapped = dummyList.findAndMap(find = { it % 2 == 0 }, map = { it * 10 })

        assertThat(dummyListMapped, hasItem(20))
    }

    @Test
    fun should_not_find_and_not_map() {
        val dummyList = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        val dummyListMapped = dummyList.findAndMap(find = { it % 12 == 0 }, map = { it * 10 })

        assertThat(dummyListMapped, not(hasItem(20)))
    }
}