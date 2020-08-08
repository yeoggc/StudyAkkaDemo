package com.ggc.study_scala.utilities.event_bus.event_stream;

import akka.actor.UntypedActor;

public class TestStoppedActor extends UntypedActor {

    public void onReceive(Object message) throws Exception, Exception {
        System.out.println("self = " + getSelf() + " , sender = " + getSender() + " , message = " + message);
    }
}
