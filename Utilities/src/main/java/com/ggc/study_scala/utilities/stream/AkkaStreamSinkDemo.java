package com.ggc.study_scala.utilities.stream;

import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.IOResult;
import akka.stream.Materializer;
import akka.stream.javadsl.FileIO;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import akka.util.ByteString;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.concurrent.CompletionStage;

public class AkkaStreamSinkDemo {

    private ActorSystem system;
    private Materializer materializer;

    @Before
    public void init() {
        system = ActorSystem.create("sys");
        materializer = ActorMaterializer.create(system);
    }

    /**
     * 使用Sink做fold运算，该运算会将fold的第一个参数作为初始值传入后面函数中的x，
     * 每次计算后将结果继续作为下一次计算的参数输入。runWith会将Source和Sink连接起来并运行。
     */
    @Test
    public void testSinkUsage4Fold() {
        Sink<Integer, CompletionStage<Integer>> sink =
                Sink.fold(2, (x, y) -> x * y);

        Source<Integer, NotUsed> source = Source.range(1, 3);

        source.runWith(sink, materializer)
                .thenAccept(System.out::println);
    }

    /**
     * 使用Sink做reduce运算，该运算和fold类似，不过没有初始参数。
     */
    @Test
    public void testSinkUsage4Reduce() {
        Sink<Integer, CompletionStage<Integer>> sink =
                Sink.reduce((x, y) -> x * y);

        Source<Integer, NotUsed> source = Source.range(1, 3);

        source.runWith(sink, materializer)
                .thenAccept(System.out::println);
    }

    /**
     * 使用FileIO API构建输出流的Sink，结合Source可以实现简单的文件复制功能。
     * TODO 未完成文件复制功能
     */
    @Test
    public void testSinkUsage4FileIOApi() {
        Sink<ByteString, CompletionStage<IOResult>> sink =
                FileIO.toPath(Paths.get("src/test/test_datasource/dstarget1.txt"));

        Source<ByteString, CompletionStage<IOResult>> source =
                FileIO.fromPath(Paths.get("src/test/test_datasource/datasource1.txt"));
        source.runWith(sink,materializer);

    }

    @After
    public void shutdown() {
        system.shutdown();
    }

}
