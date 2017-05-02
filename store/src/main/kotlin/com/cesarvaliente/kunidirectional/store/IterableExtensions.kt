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

package com.cesarvaliente.kunidirectional.store

inline fun <T> Iterable<T>.firstOrDefault(predicate: (T) -> Boolean?, default: T): T {
    this.forEach { if (predicate(it) != null && predicate(it)!!) return it }
    return default
}

inline fun <T> Iterable<T>.findAndMap(find: (T) -> Boolean, map: (T) -> T): List<T> {
    return map { if (find(it)) map(it) else it }
}