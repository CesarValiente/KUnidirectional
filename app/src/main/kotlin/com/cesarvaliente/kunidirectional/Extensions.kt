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

import android.support.v4.content.ContextCompat.getColor
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import com.cesarvaliente.kunidirectional.store.Color
import com.cesarvaliente.kunidirectional.store.Color.BLUE
import com.cesarvaliente.kunidirectional.store.Color.GREEN
import com.cesarvaliente.kunidirectional.store.Color.RED
import com.cesarvaliente.kunidirectional.store.Color.WHITE
import com.cesarvaliente.kunidirectional.store.Color.YELLOW
import com.cesarvaliente.kunidirectional.store.Item

fun ImageView.load(drawableId: Int) =
        //Here we could use an Image library to load our resource on a different way having it isolated
        setImageResource(drawableId)


fun View.changeBackgroundColor(colorId: Int) =
        setBackgroundColor(getColor(context, colorId))

fun View.changeBackgroundColor(color: Color) =
        setBackgroundColor(getColor(context, color.toColorResource()))

fun Color.toColorResource(): Int =
        //We don't use Presentation models, we enrich the Store models using EF
        when (this) {
            RED -> R.color.red
            YELLOW -> R.color.yellow
            GREEN -> R.color.green
            BLUE -> R.color.blue
            WHITE -> R.color.white
        }

fun Item.getStableId(): Long = localId.hashCode().toLong()

fun EditText.isNotBlankThen(blockTextNotBlank: () -> Unit,
                            blockTextBlank: (() -> Unit)? = null) {
    if (isNotBlank()) {
        blockTextNotBlank()
    } else {
        blockTextBlank?.invoke()
    }
}

fun EditText.updateText(newText: String?) =
        newText?.let {
            if (isNotBlank() && isDifferentThan(it)) {
                setText(it)
            }
        }

fun EditText.isDifferentThan(newText: String): Boolean =
        text.toString() != newText

fun EditText.isNotBlank(): Boolean =
        text.isNotBlank()