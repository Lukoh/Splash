/*
 * Copyright (C) 2021 Lukoh Nam, goForer
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License,
 * or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 */

package com.goforer.lukohsplash.domain.processor.photo

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.goforer.lukohsplash.domain.UseCase
import com.goforer.lukohsplash.presentation.vm.Params
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CropImageUseCase
@Inject
constructor() : UseCase<Bitmap>() {
    @Suppress("UNCHECKED_CAST")
    override fun run(lifecycleScope: CoroutineScope, params: Params) = flow {
        emit(cropImage(params.query.firstParam as Bitmap))
    }.stateIn(
        scope = lifecycleScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = params.query.firstParam as Bitmap
    )

    private fun cropImage(bitmap: Bitmap): Bitmap {
        val corpBitmap = Bitmap.createBitmap(
            bitmap, 0, (bitmap.height / 2) - CORRECTION_Y_VALUE, bitmap.width, bitmap.height / 2
        )

        val stream = ByteArrayOutputStream()

        corpBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)

        val bitmapData = stream.toByteArray()

        return BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.size)
    }
}
