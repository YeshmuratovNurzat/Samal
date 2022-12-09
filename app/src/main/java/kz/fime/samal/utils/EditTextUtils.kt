package kz.fime.samal.utils

import android.util.Log
import android.widget.EditText
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.slots.PredefinedSlots
import ru.tinkoff.decoro.slots.Slot
import ru.tinkoff.decoro.watchers.MaskFormatWatcher

class EditTextUtils {

    companion object {
        private val PHONE_MASK = arrayOf(
                PredefinedSlots.hardcodedSlot('+').withTags(Slot.TAG_DECORATION),
                PredefinedSlots.hardcodedSlot('7').withTags(Slot.TAG_DECORATION),
                PredefinedSlots.hardcodedSlot(' ').withTags(Slot.TAG_DECORATION),
                PredefinedSlots.hardcodedSlot('(').withTags(Slot.TAG_DECORATION),
                PredefinedSlots.digit(),
                PredefinedSlots.digit(),
                PredefinedSlots.digit(),
                PredefinedSlots.hardcodedSlot(')').withTags(Slot.TAG_DECORATION),
                PredefinedSlots.hardcodedSlot(' ').withTags(Slot.TAG_DECORATION),
                PredefinedSlots.digit(),
                PredefinedSlots.digit(),
                PredefinedSlots.digit(),
                PredefinedSlots.hardcodedSlot('-').withTags(Slot.TAG_DECORATION),
                PredefinedSlots.digit(),
                PredefinedSlots.digit(),
                PredefinedSlots.hardcodedSlot('-').withTags(Slot.TAG_DECORATION),
                PredefinedSlots.digit(),
                PredefinedSlots.digit()
        )

        fun setPhoneMask(editText: EditText) {
            val mask: MaskImpl = MaskImpl.createTerminated(PHONE_MASK)
            val maskTxt = "7"

            val watcher = MaskFormatWatcher(mask)
            watcher.installOn(editText)
            editText.setText(maskTxt)
        }

        fun getPhoneUnmasked(phone: String): String {
            if (phone.isEmpty()) {
                return phone
            }
            return phone.replace("[\\D]".toRegex(), "").substring(1)
        }

        fun getPhoneMasked(phone: String): String {
            Log.d("PHONE", phone)
            return if (isValid(phone)) {
                "+7 " + phone.substring(0, 3) + " " + phone.substring(3, 6) + " " + phone.substring(6, 8) + " " + phone.substring(8, 10)
            } else {
                if (isValidWithBeginning(phone)) {
                    "+" + phone[0] + " " + phone.substring(1, 4) + " " + phone.substring(4, 7) + " " + phone.substring(7, 9) + " " + phone.substring(9, 11)
                } else phone
            }
        }

        fun getPhoneSecretMasked(phone: String) : String {
            return if (isValid(phone)) {
                "+7 " + phone.substring(0, 3) + " *** " + phone.substring(7, 10)
            } else phone
        }

        private fun isValid(input: String): Boolean {
            return (input.length == 10)
        }

        private fun isValidWithBeginning(input: String) : Boolean {
            return input.length == 11
        }
    }
}