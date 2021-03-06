package com.github.visola.githubnotifier.model;

import java.io.IOException;
import java.util.Calendar;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class EventDeserializer extends JsonDeserializer<Event> {

  @Override
  public Event deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    ObjectCodec codec = p.getCodec();
    JsonNode node = codec.readTree(p);

    Event e = new Event();
    e.setId(node.get("id").asLong());

    Calendar createdAt = Calendar.getInstance();
    createdAt.setTimeInMillis(node.get("created_at").asLong());
    e.setCreatedAt(createdAt);

    EventType type = EventType.valueOf(node.get("type").asText());
    e.setType(type);

    switch (e.getType()) {
      case PullRequestEvent:
        PullRequestEventPayload payload = codec.treeToValue(node.get("payload"), PullRequestEventPayload.class);
        payload.setEvent(e);
        e.setPayload(payload);
        break;
      default:
        e.setPayload(new StringEventPayload(e, node.get("payload").toString()));
        break;
    }

    return e;
  }

}
