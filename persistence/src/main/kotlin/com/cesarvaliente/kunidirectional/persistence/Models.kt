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

import io.realm.RealmModel
import io.realm.annotations.Ignore
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass


internal const val LOCAL_ID = "localId"
internal const val POSITION = "position"

enum class Color {
    RED, YELLOW, GREEN, BLUE, WHITE
}

@RealmClass
open class Item() : RealmModel {
    constructor(localId: String, text: String?, favorite: Boolean = false,
                colorEnum: Color = Color.WHITE, position: Long) : this() {
        this.localId = localId
        this.text = text
        this.favorite = favorite
        this.color = colorEnum.name
        this.position = position
    }

    @PrimaryKey open var localId: String = ""
    open var text: String? = null
    open var favorite: Boolean = false
    @Ignore private var colorEnum: Color = Color.WHITE
    open var color: String = colorEnum.name
    open var position: Long = 0

    fun getColorAsEnum(): Color = Color.valueOf(color)

    fun setColorAsEnum(color: Color) {
        this.color = color.name
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Item

        if (localId != other.localId) return false
        if (text != other.text) return false
        if (favorite != other.favorite) return false
        if (colorEnum != other.colorEnum) return false
        if (color != other.color) return false
        if (position != other.position) return false

        return true
    }

    override fun hashCode(): Int {
        var result = localId.hashCode()
        result = 31 * result + (text?.hashCode() ?: 0)
        result = 31 * result + favorite.hashCode()
        result = 31 * result + colorEnum.hashCode()
        result = 31 * result + color.hashCode()
        result = 31 * result + position.hashCode()
        return result
    }
}