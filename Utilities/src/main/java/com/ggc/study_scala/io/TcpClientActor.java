package com.ggc.study_scala.io;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.io.Tcp;
import akka.io.TcpMessage;
import akka.util.ByteString;

public class TcpClientActor extends UntypedActor {
    @Override
    public void onReceive(Object msg) throws Exception, Exception {
        if (msg instanceof Tcp.Bound) {
            Tcp.Bound bound = (Tcp.Bound) msg;
            System.out.println("self : " + getSelf() + " , sender : "+getSender()  +" , bound : " + bound);
        } else if (msg instanceof Tcp.Connected) {
            Tcp.Connected conn = (Tcp.Connected) msg;

            //Connection Actor
            ActorRef connActor = getSender();
            ActorRef clientHandler = getContext().actorOf(Props.create(ClientHandler.class), "clientHandler");
            connActor.tell(TcpMessage.register(clientHandler), getSelf());
            connActor.tell(TcpMessage.write(ByteString.fromString("HelloAkka")), getSelf());

            System.out.println("self : " + getSelf() + " , sender : "+getSender()  +" , conn : " + conn);

        }
        /*
            编译错误：
                java: 找不到符号
                符号:   类 Close
                位置: 类 akka.io.Tcp
        */
//        else if (msg instanceof Tcp.Close) {
//            Tcp.Close close = (Tcp.Close) msg;
//            System.out.println("self : " + getSelf() + " , close : " + close);
//        }
        else {
            unhandled(msg);
        }
    }
}
