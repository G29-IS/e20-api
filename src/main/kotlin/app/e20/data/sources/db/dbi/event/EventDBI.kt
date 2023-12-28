package app.e20.data.sources.db.dbi.event

import app.e20.core.logic.typedId.impl.IxId
import app.e20.data.models.event.EventData
import app.e20.data.models.user.UserData
import app.e20.data.sources.db.dbi.DBI
import kotlinx.datetime.LocalDateTime

/**
 * [EventData] database interactor
 *
 * @see create
 * @see get
 * @see getForDates
 * @see update
 * @see delete
 */
interface EventDBI : DBI {
    /**
     * Creates a new event
     */
    suspend fun create(eventData: EventData)

    /**
     * Gets a single event by its [id]
     *
     * @return [EventData] or null if no event with that id exists
     */
    suspend fun get(id: IxId<EventData>): EventData?

    /**
     * Gets all the events organized by a user
     *
     * @param id organizer id
     *
     * @return a [List] of all the [EventData] organized by the user
     */
    suspend fun getOfOrganizer(id: IxId<UserData>): List<EventData>

    /**
     * Gets all events in the range of [startDate] and [endDate]
     *
     * @return the [List] of [EventData]
     */
    suspend fun getForDates(startDate: LocalDateTime, endDate: LocalDateTime): List<EventData>

    /**
     * Updates an event
     *
     * @param id id of the event to update
     * @param organizerId id of the event organizer
     * @param eventCreateOrUpdateRequestData update data
     *
     * @return true if any event was updated
     */
    suspend fun update(id: IxId<EventData>, organizerId: IxId<UserData>, eventCreateOrUpdateRequestData: EventData.EventCreateOrUpdateRequestData) : Boolean

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