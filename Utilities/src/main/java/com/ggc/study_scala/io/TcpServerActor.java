package com.ggc.study_scala.io;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.io.Tcp;
import akka.io.TcpMessage;


public class TcpServerActor extends UntypedActor {

    @Override
    public void preStart() throws Exception {
        super.preStart();

    }

    @Override
    public void onReceive(Object msg) throws Exception {
        if (msg instanceof Tcp.Connected) {
            Tcp.Connected conn = (Tcp.Connected) msg;
            System.out.println("self : " + getSelf() + " , sender : " + getSender() + " , conn : " + conn);
            ActorRef handler = getContext().actorOf(Props.create(ServerHandler.class));
            getSender().tell(TcpMessage.register(handler), getSelf());
        }

        /*
            编译错误：
                java: 找不到符号
                符号:   类 Close
                位置: 类 akka.io.Tcp
        */
//        else if (msg instanceof Tcp.PeerClosed) {
//            Tcp.PeerClosed peerClosed = (Tcp.PeerClosed) msg;
//            System.out.println("self : " + getSelf() + " , peerClosed : " + peerClosed);
//        }
        else {
            unhandled(msg);
        }
    }
}
