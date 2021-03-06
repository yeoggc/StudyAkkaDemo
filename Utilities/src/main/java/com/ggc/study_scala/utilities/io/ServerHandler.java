package com.ggc.study_scala.utilities.io;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.io.Tcp;
import akka.io.TcpMessage;
import akka.util.ByteString;

public class ServerHandler extends UntypedActor {

    @Override
    public void onReceive(Object msg) throws Exception {
        if (msg instanceof Tcp.Received) {
            Tcp.Received re = (Tcp.Received) msg;
            ByteString b = re.data();
            String content = b.utf8String();
            System.out.println("ServerHandler:" + content);
            ActorRef conn = getSender();
            conn.tell(TcpMessage.write(ByteString.fromString("Thanks")), getSelf());
        } else if (msg instanceof Tcp.ConnectionClosed) {
            System.out.println("Connection is closed " + msg);
            getContext().stop(getSelf());
        } else {
            System.out.println("Other Tcp Server: " + msg);
        }
    }
}
