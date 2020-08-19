package com.ggc.study_scala.utilities.stream;

import akka.Done;
import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;

public class AkkaStreamFlowDemo {

    private ActorSystem system;
    private Materializer materializer;

    @Before
    public void init() {
        system = ActorSystem.create("sys");
        materializer = ActorMaterializer.create(system);
    }

    @Test
    public void testFlowUsage1() {
        Flow<String, Integer, NotUsed> flow =
                Flow.of(String.class).map(x -> Integer.parseInt(x) * 3);

        Sink<Integer, CompletionStage<Done>> sink =
                Sink.foreach(System.out::println);

        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        // 方式一：Flow组件通过调用Source.via方法附加在Source上，此时会构建一个新的Source对象
        Source.from(list).via(flow).runWith(sink, materializer);

        // 方式二：通过调用Flow.to方法附加在Sink上，此时会构建一个新的Sink对象。
        Source.from(list).runWith(flow.to(sink), materializer);
    }

    @Test
    public void testFlowUsage2() {


        Flow<String, Integer, NotUsed> flow =
                Flow.fromSinkAndSource(Sink.foreach(System.out::println), Source.range(1, 3));

        Sink<Integer, CompletionStage<Done>> sink =
                Sink.foreach(System.out::println);

        Sink<String, NotUsed> newSink = flow.to(sink);


        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        Source.from(list).runWith(newSink,materializer);


    }

    @After
    public void shutdown() {
        system.shutdown();
    }

}
