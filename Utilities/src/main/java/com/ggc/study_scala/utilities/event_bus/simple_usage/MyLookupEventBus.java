package com.ggc.study_scala.utilities.event_bus.simple_usage;

import akka.actor.ActorRef;
import akka.event.japi.LookupEventBus;

public class MyLookupEventBus extends LookupEventBus<Event, ActorRef, String> {
    @Override
    public String classify(Event event) {
        return event.getType();
    }

    @Override
    public int compareSubscribers(ActorRef ref1, ActorRef ref2) {
        return ref1.compareTo(ref2);
    }

    @Override
    public void publish(Event event, ActorRef ref) {
        ref.tell(event.getMessage(), ActorRef.noSender());
    }

    /**
     * 期望的classify数,一般设置为2的n次幂
     */
    @Override
    public int mapSize() {
        return 8;
    }
}
