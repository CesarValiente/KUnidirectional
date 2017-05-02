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

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.cesarvaliente.kunidirectional.R
import com.cesarvaliente.kunidirectional.changeBackgroundColor
import com.cesarvaliente.kunidirectional.load
import com.cesarvaliente.kunidirectional.store.Item
import com.cesarvaliente.kunidirectional.toColorResource
import org.jetbrains.anko.find

internal interface ItemTouchHelperViewHolder {
    fun onItemSelected()
    fun onItemClear()
}

class ItemViewHolder(view: View,
                     private val itemClick: (Item) -> Unit,
                     private val setFavorite: (Item) -> Unit,
                     private val reorderItems: () -> Unit)
    : RecyclerView.ViewHolder(view), ItemTouchHelperViewHolder {

    private lateinit var currentItem: Item
    val itemContentLayout: RelativeLayout = itemView.find(R.id.contentLayout)
    val itemText: TextView = itemView.find(R.id.itemText)
    val itemStar: ImageView = itemView.find(R.id.itemStar)

    fun bindItem(item: Item) =
            with(item) {
                currentItem = this
                bindViewContent(this)
                bindClickHandlers(this)
            }

    private fun bindViewContent(item: Item) =
            with(item) {
                itemText.text = text
                itemStar.load(
                        if (favorite) R.drawable.ic_star_black
                        else R.drawable.ic_star_border_black)
                itemContentLayout.changeBackgroundColor(color.toColorResource())
            }

    private fun bindClickHandlers(item: Item) {
        itemContentLayout.setOnClickListener { itemClick(item) }
        itemStar.setOnClickListener { setFavorite(item) }
    }

    override fun onItemSelected() {
        itemContentLayout.changeBackgroundColor(R.color.item_selected)
    }

    override fun onItemClear() {
        itemContentLayout.changeBackgroundColor(currentItem.color.toColorResource())
        reorderItems()
    }
}