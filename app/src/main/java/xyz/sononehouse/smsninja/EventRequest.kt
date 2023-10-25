package xyz.sononehouse.smsninja

class ProcessMsgData(val status: String, val date: Long, val sender: String, val body: String)

class EventRequest (val device: String, val app: String, val event: String, val data: Any)

class EventResponse (val success: Boolean, val message: String)
