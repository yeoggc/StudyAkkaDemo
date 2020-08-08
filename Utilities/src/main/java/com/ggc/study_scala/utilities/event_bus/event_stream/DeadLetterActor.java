package com.ggc.study_scala.utilities.event_bus.event_stream;

import akka.actor.DeadLetter;
import akka.actor.UntypedActor;

public class DeadLetterActor extends UntypedActor {

    /**
     * 给actorRef订阅了DeadLetter事件，当给某个已经终止的Actor发送消息后，
     * 该消息会进入死信列表，然后actorRef会收到DeadLetter消息。
     * 通过DeadLetter消息，我们可以得到死信内容、发送者、接收者
     *
     * @param msg msg
     */
    public void onReceive(Object msg) {

        if (msg instanceof DeadLetter) {
            DeadLetter deadLetter = (DeadLetter) msg;
            System.out.println("deadLetter:" + deadLetter.message() + " , " + deadLetter.sender() + " , " + deadLetter.recipient());
        } else {
            System.out.println("otherMessage" + msg);
        }
    }
}
