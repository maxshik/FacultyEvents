import com.example.fitday.room.Event
import com.example.fitday.room.EventsDao
import kotlinx.coroutines.flow.Flow

class EventRepository(private val eventDao: EventsDao) {
    fun getAllEvents(): Flow<List<Event>> {
        return eventDao.getAllEvent()
    }

    suspend fun insertNote(event: Event) {
        eventDao.insert(event)
    }

    suspend fun updateNote(event: Event) {
        eventDao.update(event)
    }

    suspend fun deleteNote(event: Event) {
        eventDao.delete(event)
    }
}