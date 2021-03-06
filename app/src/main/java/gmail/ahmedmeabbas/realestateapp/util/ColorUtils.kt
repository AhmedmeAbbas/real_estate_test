package gmail.ahmedmeabbas.realestateapp.util

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes

object ColorUtils {

    fun Context.getColorFromAttr(
        @AttrRes attrColor: Int,
        typedValue: TypedValue = TypedValue(),
        resolveRefs: Boolean = true
    ): Int {
        theme.resolveAttribute(attrColor, typedValue, resolveRefs)
        return typedValue.data
    }
}