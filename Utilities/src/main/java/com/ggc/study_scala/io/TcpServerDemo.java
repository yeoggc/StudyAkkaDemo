package com.ggc.study_scala.io;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.io.Tcp;
import akka.io.TcpMessage;
import akka.util.ByteString;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TcpServerDemo {
    public static void main(String[] args) {

        ActorSystem actorSystem = ActorSystem.create("GGC_ActorSystem");

        ActorRef tcpClientActor = actorSystem.actorOf(
                Props.create(TcpClientActor.class),
                "tcpClientActor"
        );
        ActorRef tcpServerActor = actorSystem.actorOf(
                Props.create(TcpServerActor.class),
                "tcpServerActor"
        );

        ActorRef tcpManager = Tcp.get(actorSystem).manager();
        tcpManager.tell(
                TcpMessage.bind(
                        tcpServerActor,
                        new InetSocketAddress("127.0.0.1", 1234),
                        100),
                tcpClientActor);

        tcpManager.tell(
                TcpMessage.connect(
                        new InetSocketAddress("127.0.0.1", 1234))
                , tcpClientActor);

//        Executors
//                .newSingleThreadScheduledExecutor()
//                .schedule(
//                        () -> {
//                            tcpClientActor.tell(TcpMessage.close(),tcpClientActor);
//                        },
//                        3, TimeUnit.SECONDS
//                );


    }
}
