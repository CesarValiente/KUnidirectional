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

package com.cesarvaliente.kunidirectional.itemslist

import com.cesarvaliente.kunidirectional.createItem
import com.cesarvaliente.kunidirectional.store.ActionDispatcher
import com.cesarvaliente.kunidirectional.store.ItemsListScreen
import com.cesarvaliente.kunidirectional.store.Navigation
import com.cesarvaliente.kunidirectional.store.State
import com.cesarvaliente.kunidirectional.store.StateDispatcher
import com.cesarvaliente.kunidirectional.store.StoreActionSubscriber
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.clearInvocations
import com.nhaarman.mockito_kotlin.spy
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import org.hamcrest.CoreMatchers.*
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.lang.ref.WeakReference
import org.hamcrest.CoreMatchers.`is` as iz

class ItemsControllerViewTest {
    lateinit var actionDispatcher: ActionDispatcher
    lateinit var stateDispatcher: StateDispatcher
    @Mock lateinit var itemsViewCallback: ItemsViewCallback
    lateinit var itemsControllerView: ItemsControllerView
    lateinit var itemsControllerViewSpy: ItemsControllerView

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        actionDispatcher = ActionDispatcher()
        stateDispatcher = StateDispatcher()

        StoreActionSubscriber(
                actionDispatcher = actionDispatcher,
                stateDispatcher = stateDispatcher)

        itemsControllerView = ItemsControllerView(
                itemsViewCallback = WeakReference(itemsViewCallback),
                actionDispatcher = actionDispatcher,
                stateDispatcher = stateDispatcher)

        itemsControllerViewSpy = spy(itemsControllerView)
        itemsControllerViewSpy.onStart()

        clearInvocations(itemsControllerViewSpy)
        clearInvocations(itemsViewCallback)
    }

    @Test
    fun should_fetch_Items_and_handle_State() {
        val item1 = createItem(1)
        val item2 = createItem(2)
        val item3 = createItem(3)
        val listOfItems = listOf(item1, item2, item3)

        val state = State(ItemsListScreen(items = listOfItems))
        stateDispatcher.dispatch(state)

        itemsControllerViewSpy.fetchItems()

        argumentCaptor<State>().apply {
            verify(itemsControllerViewSpy, times(2)).handleState(capture())

            with(lastValue.itemsListScreen.items) {
                assertThat(this, iz(not(emptyList())))
                assertThat(this.size, iz(listOfItems.size))
                assertThat(this, iz(listOfItems))
            }

            assertThat(lastValue.editItemScreen.currentItem.isEmpty(), iz(true))
            assertThat(lastValue.navigation, iz(Navigation.ITEMS_LIST))
        }
    }

    @Test
    fun should_edit_Item_and_handle_State() {
        val item1 = createItem(1)
        val state = State(itemsListScreen = ItemsListScreen(listOf(item1)),
                navigation = Navigation.ITEMS_LIST)
        stateDispatcher.dispatch(state)

        itemsControllerViewSpy.toEditItemScreen(item1)
        argumentCaptor<State>().apply {
            verify(itemsControllerViewSpy, times(2)).handleState(capture())

            assertThat(lastValue.editItemScreen.currentItem, iz(item1))
            assertThat(lastValue.navigation, iz(Navigation.EDIT_ITEM))
        }
    }

    @Test
    fun should_reorder_Items_and_handle_State() {
        val item1 = createItem(1)
        val item2 = createItem(2)
        val item3 = createItem(3)
        val defaultList = listOf(item3, item2, item1)

        val state = State(itemsListScreen = ItemsListScreen(defaultList),
                navigation = Navigation.ITEMS_LIST)
        stateDispatcher.dispatch(state)

        val reorderedList = listOf(item1, item2, item3)
        itemsControllerViewSpy.reorderItems(reorderedList)

        argumentCaptor<State>().apply {
            verify(itemsControllerViewSpy, times(2)).handleState(capture())

            with(lastValue.itemsListScreen) {
                assertThat(items.size, iz(defaultList.size))
                assertThat(items, iz(not(defaultList)))
                assertThat(items, iz(reorderedList))
            }
            assertThat(lastValue.navigation, iz(Navigation.ITEMS_LIST))
        }
    }

    @Test
    fun should_change_Item_favorite_status_and_handle_state() {
        val item1 = createItem(1)
        val state = State(itemsListScreen = ItemsListScreen(listOf(item1)),
                navigation = Navigation.ITEMS_LIST)
        stateDispatcher.dispatch(state)

        itemsControllerViewSpy.changeFavoriteStatus(item1)
        argumentCaptor<State>().apply {
            verify(itemsControllerViewSpy, times(2)).handleState(capture())

            with(lastValue.itemsListScreen.items) {
                assertThat(size, iz(1))
                assertThat(component1().favorite, iz(not(item1.favorite)))
            }
            assertThat(lastValue.editItemScreen.currentItem.isEmpty(), iz(true))
            assertThat(lastValue.navigation, iz(Navigation.ITEMS_LIST))
        }
    }

    @Test
    fun should_delete_Item_and_handle_State() {
        val item1 = createItem(1)
        val item2 = createItem(2)
        val item3 = createItem(3)
        val defaultList = listOf(item1, item2, item3)

        val state = State(itemsListScreen = ItemsListScreen(defaultList),
                navigation = Navigation.ITEMS_LIST)
        stateDispatcher.dispatch(state)

        itemsControllerViewSpy.deleteItem(item1)

        argumentCaptor<State>().apply {
            verify(itemsControllerViewSpy, times(2)).handleState(capture())

            with(lastValue.itemsListScreen) {
                assertThat(items.size, iz(not(defaultList.size)))
                assertThat(items.size, iz(2))
                assertThat(items, hasItem(not(item1)))
                assertThat(items.component1(), iz(item2))
                assertThat(items.component2(), iz(item3))
            }
            assertThat(lastValue.navigation, iz(Navigation.ITEMS_LIST))
        }
    }
}