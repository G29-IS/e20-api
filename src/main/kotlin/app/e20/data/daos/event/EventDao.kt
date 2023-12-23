package app.e20.data.daos.event

import app.e20.core.logic.typedId.impl.IxId
import app.e20.data.models.event.EventData
import app.e20.data.models.user.UserData
import kotlinx.datetime.LocalDateTime

interface EventDao {
    /**
     * Creates a new event
     *
     * @param userId id of the user that organized the event
     * @param eventCreateOrUpdateRequestData
     *
     * @return [EventData]
     */
    suspend fun create(userId: IxId<UserData>, eventCreateOrUpdateRequestData: EventData.EventCreateOrUpdateRequestData): EventData

    /**
     * Gets a single event by its [id]
     *
     * @return [EventData] or null if no event with that id exists
     */
    suspend fun get(id: IxId<EventData>): EventData?

    /**
     * Gets all events in the range of [startDate] and [endDate]
     *
     * @return the [List] of [EventData]
     */
    suspend fun getForDates(startDate: LocalDateTime, endDate: LocalDateTime): List<EventData>

    /**
     * Updates an event
     *
     * @param id event id
     * @param organizerId event organizer id
     * @param eventCreateOrUpdateRequestData update data
     *
     * @return true if any event was updated
     */
    suspend fun update(id: IxId<EventData>, organizerId: IxId<UserData>, eventCreateOrUpdateRequestData: EventData.EventCreateOrUpdateRequestData) : EventData?

    /**
     * Deletes a single event
     *
     * @param id event id
     * @param organizerId event organizer id
     *
     * @return true if any event was deleted
     */
    suspend fun delete(id: IxId<EventData>, organizerId: IxId<UserData>) : Boolean
}