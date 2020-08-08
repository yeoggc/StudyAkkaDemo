package com.ggc.study_scala.utilities.event_bus.simple_usage;

import akka.actor.UntypedActor;

public class EventActor extends UntypedActor {

    @Override
    public void onReceive(Object message) throws Exception, Exception {
        System.out.println("self = " + getSelf() + " , sender = " + getSender() + " , message = " + message);
    }
}
