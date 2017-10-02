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

package com.cesarvaliente.kunidirectional.edititem

import com.cesarvaliente.kunidirectional.COLOR
import com.cesarvaliente.kunidirectional.FAVORITE
import com.cesarvaliente.kunidirectional.LOCAL_ID
import com.cesarvaliente.kunidirectional.POSITION
import com.cesarvaliente.kunidirectional.TEXT
import com.cesarvaliente.kunidirectional.TestStore
import com.cesarvaliente.kunidirectional.createItem
import com.cesarvaliente.kunidirectional.store.Color
import com.cesarvaliente.kunidirectional.store.EditItemScreen
import com.cesarvaliente.kunidirectional.store.Item
import com.cesarvaliente.kunidirectional.store.ItemsListScreen
import com.cesarvaliente.kunidirectional.store.Navigation
import com.cesarvaliente.kunidirectional.store.State
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.spy
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.lang.ref.WeakReference
import org.hamcrest.CoreMatchers.`is` as iz

class EditItemControllerViewTest {
    private @Mock lateinit var editItemViewCallback: EditItemViewCallback
    private lateinit var editItemControllerView: EditItemControllerView
    private lateinit var editItemControllerViewSpy: EditItemControllerView
    private lateinit var store: TestStore

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        store = TestStore
        editItemControllerView = EditItemControllerView(
                editItemViewCallback = WeakReference(editItemViewCallback),
                store = store)

        editItemControllerView.isActivityRunning = true
        editItemControllerViewSpy = spy(editItemControllerView)
        store.stateHandlers.add(editItemControllerViewSpy)

    }

    @After
    fun clean() {
        store.clear()
    }

    @Test
    fun should_create_an_item_and_handle_State() {
        editItemControllerViewSpy.createItem(localId = LOCAL_ID, text = TEXT,
                favorite = FAVORITE, color = COLOR, position = POSITION)
        argumentCaptor<State>().apply {
            verify(editItemControllerViewSpy).handleState(capture())

            with(lastValue.itemsListScreen.items) {
                assertThat(this, iz(not(emptyList())))
                assertThat(this.size, iz(1))

                with(component1()) {
                    assertThat(this.localId, iz(LOCAL_ID))
                    assertThat(this.text, iz(TEXT))
                    assertThat(this.favorite, iz(FAVORITE))
                    assertThat(this.color, iz(COLOR))
                    assertThat(this.position, iz(POSITION))
                }
            }

            assertThat(lastValue.editItemScreen.currentItem.isEmpty(), iz(true))
            assertThat(lastValue.navigation, iz(Navigation.ITEMS_LIST))
        }
    }

    @Test
    fun should_update_an_item_and_handle_State() {
        val item1 = createItem(1)
        val state = State(itemsListScreen = ItemsListScreen(listOf(item1)),
                editItemScreen = EditItemScreen(item1),
                navigation = Navigation.EDIT_ITEM)
        store.dispatch(state)

        val NEW_TEXT = "new text"
        editItemControllerViewSpy.updateItem(localId = item1.localId,
                text = NEW_TEXT, color = Color.GREEN)

        argumentCaptor<State>().apply {
            verify(editItemControllerViewSpy, times(2)).handleState(capture())

            assertThat(lastValue.itemsListScreen.items, iz(not(emptyList())))
            assertThat(lastValue.itemsListScreen.items.size, iz(1))

            fun verifyItem(item: Item) =
                    with(item) {
                        assertThat(localId, iz(item1.localId))
                        assertThat(text, iz(NEW_TEXT))
                        assertThat(favorite, iz(item1.favorite))
                        assertThat(color, iz(Color.GREEN))
                        assertThat(position, iz(item1.position))
                    }
            verifyItem(lastValue.itemsListScreen.items.component1())
            verifyItem(lastValue.editItemScreen.currentItem)
            assertThat(lastValue.navigation, iz(Navigation.EDIT_ITEM))
        }
    }

    @Test
    fun should_update_Item_color_and_handle_State() {
        val item1 = createItem(1)
        val state = State(itemsListScreen = ItemsListScreen(listOf(item1)),
                editItemScreen = EditItemScreen(item1),
                navigation = Navigation.EDIT_ITEM)
        store.dispatch(state)

        editItemControllerViewSpy.updateColor(localId = item1.localId,
                color = Color.BLUE)

        argumentCaptor<State>().apply {
            verify(editItemControllerViewSpy, times(2)).handleState(capture())

            assertThat(lastValue.itemsListScreen.items, iz(not(emptyList())))
            assertThat(lastValue.itemsListScreen.items.size, iz(1))

            fun verifyItem(item: Item) =
                    with(item) {
                        assertThat(localId, iz(item1.localId))
                        assertThat(text, iz(item1.text))
                        assertThat(favorite, iz(item1.favorite))
                        assertThat(color, iz(Color.BLUE))
                        assertThat(position, iz(item1.position))
                    }
            verifyItem(lastValue.itemsListScreen.items.component1())
            verifyItem(lastValue.editItemScreen.currentItem)
            assertThat(lastValue.navigation, iz(Navigation.EDIT_ITEM))
        }
    }

    @Test
    fun should_back_to_Items_list_and_handle_State() {
        val state = State(navigation = Navigation.EDIT_ITEM)
        store.dispatch(state)

        editItemControllerViewSpy.backToItems()

        argumentCaptor<State>().apply {
            verify(editItemControllerViewSpy, times(2)).handleState(capture())

            assertThat(lastValue.itemsListScreen.items, iz(emptyList()))
            assertThat(lastValue.editItemScreen.currentItem.isEmpty(), iz(true))
            assertThat(lastValue.navigation, iz(Navigation.ITEMS_LIST))
        }
    }

    @Test
    fun should_handle_State_and_call_updateItem_function() {
        val state = State(navigation = Navigation.EDIT_ITEM)
        editItemControllerView.handleState(state)
        verify(editItemViewCallback).updateItem(any())
    }

    @Test
    fun should_handle_State_and_call_backToItemsList_function() {

        val state = State(navigation = Navigation.ITEMS_LIST)
        reset(editItemViewCallback)
        editItemControllerView.handleState(state)
        verify(editItemViewCallback).backToItemsList()
    }
}