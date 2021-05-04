package models

import java.io.Serializable
import java.time.LocalTime

class AvailableTimingSlot(val fromTime: LocalTime, val toTime: LocalTime, val slotDuration: Int) :
    Serializable