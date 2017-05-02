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

import android.content.Context
import android.os.Build
import android.widget.EditText
import com.cesarvaliente.kunidirectional.store.Color
import org.junit.Assert.assertThat
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.hamcrest.CoreMatchers.`is` as iz

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class,
        sdk = intArrayOf(Build.VERSION_CODES.LOLLIPOP),
        application = RoboTestApplication::class)
class ExtensionsTest {
    lateinit var context: Context

    @Before
    fun setup() {
        context = RuntimeEnvironment.application
    }

    @Test
    fun should_parse_Yellow_Color_to_correct_Int_resource() {
        assertThat(Color.YELLOW.toColorResource(), iz(R.color.yellow))
    }

    @Test
    fun should_parse_Blue_Color_to_correct_Int_resource() {
        assertThat(Color.BLUE.toColorResource(), iz(R.color.blue))
    }

    @Test
    fun should_parse_Green_Color_to_correct_Int_resource() {
        assertThat(Color.GREEN.toColorResource(), iz(R.color.green))
    }

    @Test
    fun should_parse_Pink_Color_to_correct_Int_resource() {
        assertThat(Color.RED.toColorResource(), iz(R.color.red))
    }

    @Test
    fun should_parse_White_Color_to_correct_Int_resource() {
        assertThat(Color.WHITE.toColorResource(), iz(R.color.white))
    }

    @Test
    fun should_execute_block_not_blank_text_if_EditText_text_is_not_blank() {
        val editText = EditText(context)
        editText.setText("Hello")
        editText.isNotBlankThen(blockTextNotBlank = { assert(true) },
                blockTextBlank = { fail() })
    }

    @Test
    fun should_execute_block_text_blank_if_EditText_text_is_blank() {
        val editText = EditText(context)
        editText.isNotBlankThen(blockTextNotBlank = { fail() },
                blockTextBlank = { assert(true) })
    }

    @Test
    fun should_update_text_if_is_different() {
        val editText = EditText(context)
        editText.setText("Hello")
        editText.updateText("World")
        assertThat(editText.text.toString(), iz("World"))
    }

    @Test
    fun should_say_that_is_different_text() {
        val editText = EditText(context)
        editText.setText("Hello")
        assertThat(editText.isDifferentThan("World"), iz(true))
    }

    @Test
    fun should_say_that_is_not_different_text() {
        val editText = EditText(context)
        editText.setText("Hello")
        assertThat(editText.isDifferentThan("Hello"), iz(false))
    }

    @Test
    fun should_say_text_is_blank_when_not_initialised() {
        val editText = EditText(context)
        assertThat(editText.isNotBlank(), iz(false))
    }

    @Test
    fun should_say_text_is_not_blank_when_has_text() {
        val editText = EditText(context)
        editText.setText("Hello")
        assertThat(editText.isNotBlank(), iz(true))
    }

    @Test
    fun should_say_text_is_blank_when_has_text_but_are_blank_spaces() {
        val editText = EditText(context)
        editText.setText("      ")
        assertThat(editText.isNotBlank(), iz(false))
    }
}
