package patient.adapters

import models.AppointmentDetails

class AppointmentDetailItem(val appointmentDetails: AppointmentDetails) : AppointmentListItem() {
    override fun getType(): Int = TYPE_APPOINTMENT_DETAILS
}