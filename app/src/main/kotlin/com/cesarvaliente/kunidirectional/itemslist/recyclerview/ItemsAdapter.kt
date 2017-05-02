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

package com.cesarvaliente.kunidirectional.itemslist.recyclerview

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.cesarvaliente.kunidirectional.R
import com.cesarvaliente.kunidirectional.getStableId
import com.cesarvaliente.kunidirectional.store.Item
import java.util.Collections

internal interface ItemTouchHelperAdapter {
    fun onItemMove(fromPosition: Int, toPosition: Int)
    fun onItemDeleted(position: Int)
}

class ItemsAdapter(
        private var items: List<Item>,
        private val itemClick: (Item) -> Unit,
        private val setFavorite: (Item) -> Unit,
        private val updateItemsPositions: (List<Item>) -> Unit,
        private val deleteItem: (Item) -> Unit)
    : RecyclerView.Adapter<ItemViewHolder>(), ItemTouchHelperAdapter {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_layout, parent, false)
        return ItemViewHolder(view, itemClick, setFavorite, this::updateItemsPositions)
    }

    override fun onBindViewHolder(itemViewHolder: ItemViewHolder, position: Int) {
        itemViewHolder.bindItem(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun getItem(position: Int): Item = items[position]

    override fun getItemId(position: Int): Long =
            getItem(position).getStableId()

    fun removeAt(position: Int) {
        items = items.minus(items[position])
        notifyItemRemoved(position)
    }

    fun updateItems(newItems: List<Item>) {
        val oldItems = items
        items = newItems
        applyDiff(oldItems, items)
    }

    private fun applyDiff(oldItems: List<Item>, newItems: List<Item>) {
        val diffResult = DiffUtil.calculateDiff(ItemsDiffCallback(oldItems, newItems))
        diffResult.dispatchUpdatesTo(this)
    }

    private fun updateItemsPositions() {
        updateItemsPositions(items)
    }

    override fun onItemDeleted(position: Int) {
        deleteItem(items[position])
        removeAt(position)
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        swapItems(fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    fun swapItems(fromPosition: Int, toPosition: Int) = if (fromPosition < toPosition) {
        (fromPosition .. toPosition - 1).forEach { i ->
            swapPositions(i, i + 1)
            Collections.swap(items, i, i + 1)
        }
    } else {
        (fromPosition downTo toPosition + 1).forEach { i ->
            swapPositions(i, i - 1)
            Collections.swap(items, i, i - 1)
        }
    }

    fun swapPositions(position1: Int, position2: Int) {
        val item1 = items[position1]
        val item2 = items[position2]
        items = items.map {
            if (it.localId == item1.localId) it.copy(position = item2.position)
            else if (it.localId == item2.localId) it.copy(position = item1.position)
            else it
        }
    }
}