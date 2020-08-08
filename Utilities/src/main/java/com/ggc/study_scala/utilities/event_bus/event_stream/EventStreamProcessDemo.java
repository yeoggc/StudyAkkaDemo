package com.ggc.study_scala.utilities.event_bus.event_stream;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.DeadLetter;
import akka.actor.Props;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class EventStreamProcessDemo {

    public static void main(String[] args) {

        ActorSystem actorSystem = ActorSystem.create("GGC_ActorSystem");

        ActorRef deadLetterActor = actorSystem.actorOf(Props.create(DeadLetterActor.class), "deadLetterActor");
        //通过事件流让deadLetterActor订阅了DeadLetter事件
        actorSystem.eventStream().subscribe(deadLetterActor, DeadLetter.class);

        //手动停止这个ActorRef
        final ActorRef stoppedActor = actorSystem.actorOf(Props.create(TestStoppedActor.class), "testStoppedActor");
        actorSystem.stop(stoppedActor);

        //由于停止ActorRef是异步操作，延迟1秒在给某个已经终止的Actor发送消息
        Executors
                .newSingleThreadScheduledExecutor()
                .schedule(() -> stoppedActor.tell("msg=> 给已终止的Actor发送消息", ActorRef.noSender()),
                        1, TimeUnit.SECONDS);


    }
}
