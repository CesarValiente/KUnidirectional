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

import com.cesarvaliente.kunidirectional.store.CreationAction.CreateItemAction
import com.cesarvaliente.kunidirectional.store.Item
import com.cesarvaliente.kunidirectional.store.LOCAL_ID
import com.cesarvaliente.kunidirectional.store.Navigation
import com.cesarvaliente.kunidirectional.store.State
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.spy
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.hamcrest.CoreMatchers.`is` as iz

class ControllerViewTest {
    private lateinit var store: TestStore
    private lateinit var controllerViewSpy: ControllerView

    @Before
    fun setup() {
        store = TestStore
        val controllerView = object : ControllerView(store = store) {
            override var isActivityRunning: Boolean = true
            override fun handleState(appState: State) {}
        }
        controllerViewSpy = spy(controllerView)
        store.stateHandlers.add(controllerViewSpy)
    }

    @After
    fun clean() {
        store.clear()
    }

    @Test
    fun controllerView_should_subscribe_successfully() {
        with(store.stateHandlers) {
            assertThat(isEmpty(), iz(false))
            assertThat(count(), iz(1))
            assertThat(contains(controllerViewSpy), iz(true))
        }
    }

    @Test
    fun controllerView_should_unsubscribe() {
        with(store.stateHandlers) {
            remove(controllerViewSpy)

            assertThat(isEmpty(), iz(true))
            assertThat(contains(controllerViewSpy), iz(false))
        }
    }

    @Test
    fun should_handle_State() {
        val newItem = Item(localId = LOCAL_ID,
                text = TEXT, favorite = FAVORITE, color = COLOR, position = POSITION)

        val createItemAction = CreateItemAction(newItem.localId,
                newItem.text!!, newItem.favorite, newItem.color,
                newItem.position)

        store.dispatch(createItemAction)

        argumentCaptor<State>().apply {
            verify(controllerViewSpy).handleState(capture())

            with(lastValue) {
                with(itemsListScreen.items) {
                    assertThat(this, iz(not(emptyList())))
                    assertThat(this.size, iz(1))
                    with(this[0]) {
                        assertThat(this.localId, iz(newItem.localId))
                        assertThat(this.text, iz(newItem.text))
                        assertThat(this.color, iz(newItem.color))
                        assertThat(this.favorite, iz(newItem.favorite))
                        assertThat(this.position, iz(newItem.position))
                    }
                }
                assertThat(editItemScreen.currentItem, iz(not(newItem)))
                assertThat(navigation, iz(Navigation.ITEMS_LIST))
            }
        }
    }

    @Test
    fun should_not_handle_State_when_activity_is_not_running() {
        val newItem = Item(localId = LOCAL_ID,
                text = TEXT, favorite = FAVORITE, color = COLOR, position = POSITION)

        val createItemAction = CreateItemAction(newItem.localId,
                newItem.text!!, newItem.favorite, newItem.color,
                newItem.position)

        controllerViewSpy.isActivityRunning = false
        store.dispatch(createItemAction)

        argumentCaptor<State>().apply {
            verify(controllerViewSpy, times(0)).handleState(capture())
        }
    }
}