package com.nl.principal.LN_Document_Revision_Service.service;

import com.nl.principal.LN_Document_Revision_Service.domain.Event;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Peter Maunatlala on 2026/05/25.
 * Project: LN-Document-Revision-Service
 * Email: pmguzzini@gmail.com
 */
@Service
public class BufferService {

    //Thread-safe map to store buffers, keyed by Id
    private final Map<String, TreeMap<Long, Event>> buffers = new ConcurrentHashMap<>();

        public void buffer(Event event) {

        //Retrieve the buffer or create a new map if the key absent
        buffers.computeIfAbsent(event.documentId(), id -> new TreeMap<>())
               .put(event.sequence(), event);
    }

    //Handle the buffer and clear the key in the map
    public Event getNext(String documentId, long expectedSequence) {
        TreeMap<Long, Event> map = buffers.get(documentId);

        if (map == null) {
            return null;
        }

        return map.remove(expectedSequence);
    }

}
