package com.ggc.study_scala.utilities.stream;


import akka.Done;
import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.dispatch.Futures;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.javadsl.RunnableGraph;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;

public class AkkaStreamSourceDemo {

    private ActorSystem system;
    private Materializer materializer;

    @Before
    public void init() {
        system = ActorSystem.create("sys");
        materializer = ActorMaterializer.create(system);
    }

    /**
     * 先从Source看起，这里使用Source.range方法创建了一个包含1到5的整数的Source对象，
     * 作为数据的来源和流的起点。注意，现在仅仅使用它是无法将这些数据取出来的，
     * 也就是说，它目前只是描述了该怎样产生这些数据。
     *
     * Source包括两个泛型：
     *  第一个泛型表示它产生的数据类型，
     *  第二个泛型表示运行时产生的其他辅助数据，假如没有则设置为NotUsed。
     *
     * 为了能操作Source产生的数据，我们定义了一个Sink对象，
     * 它的foreach方法可以循环出Source中的每个元素，
     * 但是要达成这一步，需要调用Source.to方法将Sink连接起来，
     * 此时会创建RunnableGraph对象。到这里，流操作的准备工作才算正式完成。
     *
     * 当执行RunnableGraph.run方法时，需要传入一个Materializer对象，
     * 它主要用来给流分配Actor并驱动其执行。
     *
     */
    @Test
    public void testSourceUsage1() {
        Source<Integer, NotUsed> source = Source.range(1, 5);
        Sink<Integer, CompletionStage<Done>> sink = Sink.foreach(System.out::println);

        RunnableGraph<NotUsed> graph = source.to(sink);
        graph.run(materializer);
    }

    /**
     * 从集合中构建Source，并使用runForeach循环打印出来，
     * 注意：这里的runForeach实际上是对runWith（Sink.foreach（f），materializer）的包装。
     */
    @Test
    public void testSourceUsage2() {
        List<String> list = new ArrayList<>();
        list.add("sh");
        list.add("bj");
        list.add("nj");

        Source<String, NotUsed> source = Source.from(list);
        source.runForeach(System.out::println, materializer);
    }

    /**
     * 从Future中构建Source，比如通过Patterns.ask向Actor请求一个Future结果。
     */
    @Test
    public void testSourceUsage3() {
        Source<String, NotUsed> source = Source.fromFuture(Futures.successful("Hello Akka!"));
        source.runForeach(System.out::println, materializer);

    }

    /**
     * 构建可连续生产某类型数据的Source，可以使用limit取出前n个数据。
     */
    @Test
    public void testSourceUsage4() {
        // 重复的生成元素
        Source<String, NotUsed> s3 = Source.repeat("Hello");

        // 取出前5个
        s3.limit(5).runForeach(System.out::println, materializer);
    }

    @After
    public void shutdown() {
        system.shutdown();
    }


}
