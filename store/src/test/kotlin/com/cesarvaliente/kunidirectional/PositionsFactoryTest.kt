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

import com.cesarvaliente.kunidirectional.store.PositionsFactory
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matchers.greaterThan
import org.junit.Assert.assertThat
import org.junit.Test
import org.hamcrest.CoreMatchers.`is` as iz

class PositionsFactoryTest {

    @Test
    fun should_return_new_position() {
        val positionsFactory = object : PositionsFactory {}
        val position1 = positionsFactory.newPosition()
        val position2 = positionsFactory.newPosition()

        assertThat(position1, iz(not(position2)))
        assertThat(position2, greaterThan(position1))
    }
}