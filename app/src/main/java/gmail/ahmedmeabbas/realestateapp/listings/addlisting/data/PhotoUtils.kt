package gmail.ahmedmeabbas.realestateapp.listings.addlisting.data

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import java.io.ByteArrayOutputStream

object PhotoUtils {

    fun getImageByteArray(contentResolver: ContentResolver, photoUri: Uri, height: Int): ByteArray {
        val originalBitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(contentResolver, photoUri)
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(contentResolver, photoUri)
        }

        val scaledBitmap = scaleToFitHeight(originalBitmap, height)

        val byteOutputStream = ByteArrayOutputStream()
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteOutputStream)

        return byteOutputStream.toByteArray()
    }

    private fun scaleToFitHeight(b: Bitmap, height: Int): Bitmap {
        val factor = height / b.height.toFloat()
        return Bitmap.createScaledBitmap(b, (b.width * factor).toInt(), height, true)
    }
}