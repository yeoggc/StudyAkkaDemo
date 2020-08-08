package com.ggc.study_scala.utilities.event_bus.simple_usage;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class EventBusDemo {
    public static void main(String[] args) {

        ActorSystem actorSystem = ActorSystem.create("GGC_ActorSystem");
        ActorRef eventActor = actorSystem.actorOf(Props.create(EventActor.class), "eventActor");

        //新建EventBus
        MyLookupEventBus lookupEventBus = new MyLookupEventBus();

        //订阅info与warn事件
        lookupEventBus.subscribe(eventActor, "info");
        lookupEventBus.subscribe(eventActor, "warn");

        //发布info事件
        lookupEventBus.publish(new Event("info", "Hello EventBus"));
        //取消订阅
        lookupEventBus.unsubscribe(eventActor, "warn");
        //发布warn事件
        lookupEventBus.publish(new Event("warn", "Oh No"));

    }
}
