package patient.adapters

abstract class AppointmentListItem {
    companion object {
        const val TYPE_APPOINTMENT_DATE = 0
        const val TYPE_APPOINTMENT_DETAILS = 1
    }

    abstract fun getType(): Int
}