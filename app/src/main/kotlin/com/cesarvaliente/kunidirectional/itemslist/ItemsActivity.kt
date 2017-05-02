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

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import android.view.MenuItem
import com.cesarvaliente.kunidirectional.MainThread
import com.cesarvaliente.kunidirectional.R
import com.cesarvaliente.kunidirectional.ViewActivity
import com.cesarvaliente.kunidirectional.edititem.EditItemActivity
import com.cesarvaliente.kunidirectional.itemslist.recyclerview.ItemTouchHelperCallback
import com.cesarvaliente.kunidirectional.itemslist.recyclerview.ItemsAdapter
import com.cesarvaliente.kunidirectional.store.Item
import kotlinx.android.synthetic.main.items_layout.*
import org.jetbrains.anko.doFromSdk
import org.jetbrains.anko.toast
import java.lang.ref.WeakReference

class ItemsActivity : ViewActivity<ItemsControllerView>(), ItemsViewCallback {
    private val itemsAdapter: ItemsAdapter

    private val isPersistenceEnabled: Boolean
        get () {
            println("called")
            return PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
                    getString(R.string.pref_persistence_key), true)
        }

    init {
        itemsAdapter = ItemsAdapter(
                items = emptyList<Item>(),
                itemClick = { item -> openEditItemScreen(item) },
                setFavorite = { item -> changeFavoriteStatus(item) },
                updateItemsPositions = { items -> reorderItems(items) },
                deleteItem = { item -> deleteItem(item) }
        )
    }

    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.items_layout)
        setupControllerView()
        bindViews()
    }

    override fun setupControllerView() {
        val controllerView = ItemsControllerView(
                itemsViewCallback = WeakReference(this),
                handleStateDifferentThread = MainThread(WeakReference(this)))
        registerControllerViewForLifecycle(controllerView)
    }

    override fun onResume() {
        super.onResume()
        controllerView.fetchItems()
    }

    @SuppressLint("NewApi")
    private fun bindViews() {
        setContentView(R.layout.items_layout)
        setStatusBarColor()
        setupRecyclerView()
        newItem.setOnClickListener { openEditItemScreen(controllerView.currentItem) }
    }

    private fun setupRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(this)
        itemsRecyclerView.layoutManager = linearLayoutManager
        itemsRecyclerView.adapter = itemsAdapter

        val touchHelperCallback = ItemTouchHelperCallback(itemsAdapter)
        val touchHelper = ItemTouchHelper(touchHelperCallback)
        touchHelper.attachToRecyclerView(itemsRecyclerView)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setStatusBarColor() =
            doFromSdk(Build.VERSION_CODES.LOLLIPOP) {
                window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)
            }

    private fun openEditItemScreen(item: Item) =
            controllerView.toEditItemScreen(item)

    private fun reorderItems(items: List<Item>) =
            controllerView.reorderItems(items)

    private fun changeFavoriteStatus(item: Item) =
            controllerView.changeFavoriteStatus(item)

    private fun deleteItem(item: Item) {
        val TIME_TO_WAIT = 2000
        val deleteItemRunnable = Runnable { controllerView.deleteItem(item) }
        Snackbar.make(itemsCoordinatorLayout, R.string.item_deleted, TIME_TO_WAIT)
                .setAction(R.string.item_deleted_undo,
                        {
                            handler.removeCallbacksAndMessages(null)
                            with(controllerView.state.itemsListScreen) {
                                updateItems(items)
                            }
                        })
                .addCallback(object : Snackbar.Callback() {
                    override fun onShown(snackbar: Snackbar?) {
                        super.onShown(snackbar)
                        handler.postDelayed(deleteItemRunnable, TIME_TO_WAIT.toLong())
                    }
                })
                .show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.items_menu, menu)
        menu.findItem(R.id.item_toggle_persistence)?.isChecked = isPersistenceEnabled
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_toggle_persistence -> togglePersistenceOptionMenu()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val menuItem = menu?.findItem(R.id.item_toggle_persistence)
        menuItem?.isChecked = isPersistenceEnabled
        return super.onPrepareOptionsMenu(menu)
    }

    private fun togglePersistenceOptionMenu() {
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this)
        val prefPersistenceKey = getString(R.string.pref_persistence_key)
        val isEnabled = sharedPrefs.getBoolean(prefPersistenceKey, true)
        sharedPrefs.edit().putBoolean(prefPersistenceKey, !isEnabled).apply()

        toast(R.string.persistence_changed)
    }

    override fun updateItems(items: List<Item>) =
            itemsAdapter.updateItems(items)

    override fun goToEditItem() {
        val intent = EditItemActivity.createEditItemActivityIntent(this)
        startActivity(intent)
    }
}

interface ItemsViewCallback {
    fun updateItems(items: List<Item>)
    fun goToEditItem()
}
