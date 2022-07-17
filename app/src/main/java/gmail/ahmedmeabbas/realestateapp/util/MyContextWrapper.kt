package gmail.ahmedmeabbas.realestateapp.util

import android.content.Context
import android.content.ContextWrapper
import android.os.LocaleList
import java.util.*

class MyContextWrapper(base: Context) : ContextWrapper(base) {

    fun wrap(context: Context, language: String): ContextWrapper {
        val newLocale = Locale(language)
        val config = resources.configuration
        config.setLocale(newLocale)
        val localeList = LocaleList(newLocale)
        LocaleList.setDefault(localeList)
        config.setLocales(localeList)

        return ContextWrapper(context.createConfigurationContext(config))
    }
}