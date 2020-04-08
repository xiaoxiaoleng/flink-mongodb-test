package org.okkam.flink.mongodb.test;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import com.mongodb.hadoop.io.BSONWritable;
import com.mongodb.hadoop.mapred.MongoInputFormat;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.hadoopcompatibility.mapred.HadoopInputFormat;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.bson.BSONObject;

public class MongodbExample {
    public static void main(String[] args) throws Exception {

//        final ParameterTool params = ParameterTool.fromArgs(args);
//
//        String webSource = params.get("webSource", "baidu");
//        int year = params.getInt("year", 2016);
//        String condition = String.format("{'source':'%s','year':{'$regex':'^%d'}}", webSource, year);

        // set up the execution environment
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

/*
        BatchTableEnvironment tEnv = BatchTableEnvironment.create(env);
        tEnv.registerTableSource("transactions", new BoundedTransactionTableSource());
        tEnv.registerTableSink("spend_report", new SpendReportTableSink());
        tEnv.registerFunction("truncateDateToHour", new TruncateDateToHour());
        tEnv.scan("transactions")
                .insertInto("spend_report");
        env.execute("Spend Report");
*/


        // create a MongodbInputFormat, using a Hadoop input format wrapper
        HadoopInputFormat<BSONWritable, BSONWritable> hdIf = new HadoopInputFormat<>(
                new MongoInputFormat(), BSONWritable.class, BSONWritable.class, new JobConf());


       // hdIf.getJobConf().set("mongo.input.split.create_input_splits", "false");
       // hdIf.getJobConf().set("mongo.input.query", condition);

        hdIf.getJobConf().set("mongo.input.uri", "mongodb://mongo:MongoDB_863*^#@10.1.50.15:27017/pacific.resObject?authMechanism=SCRAM-SHA-1&authSource=admin");
        // hdIf.getJobConf().set("mongo.input.uri", "mongodb://mongo:MongoDB_863*^#@10.1.50.15:27017/pacific.resHistory?authMechanism=SCRAM-SHA-1&authSource=admin");


        long start = System.currentTimeMillis();
        System.out.println("========== begin ==========");
        DataSet<Tuple2<BSONWritable, BSONWritable>> input = env.createInput(hdIf);

        // a little example how to use the data in a mapper.

        DataSet<Tuple2<Text, BSONWritable>> fin = input.map(
                new MapFunction<Tuple2<BSONWritable, BSONWritable>, Tuple2<Text, BSONWritable>>() {

                    private static final long serialVersionUID = 1L;


                    @Override
                    public Tuple2<Text, BSONWritable> map(Tuple2<BSONWritable, BSONWritable> record) throws Exception {

                        BSONWritable value = record.getField(1);
                        BSONObject doc = value.getDoc();
                        Object _id = doc.get("_id");
                        Object _incre_id = doc.get("_incre_id");


                        String id = String.valueOf(_id);
                        DBObject builder = BasicDBObjectBuilder.start()
                                .add("_incre_id", _incre_id)
                                .add("id", id)
                                // .add("type", jsonld.getString("@type"))
                                .get();

                        if ("30009.0".equals(String.valueOf(_incre_id))) {
                            System.out.println("===== =====");
                        }

                        BSONWritable w = new BSONWritable(builder);
                        return new Tuple2<Text, BSONWritable>(new Text(id), w);
                    }
                });

        // emit result (this works only locally)
        fin.print();

        //  MongoConfigUtil.setOutputURI(hdIf.getJobConf(), "mongodb://mongo:MongoDB_863*^#@10.1.50.15:27017/pacific.resObject?authMechanism=SCRAM-SHA-1&authSource=admin");
        // emit result (this works only locally)
        // fin.output(new HadoopOutputFormat<Text, BSONWritable>(new MongoOutputFormat<Text, BSONWritable>(), hdIf.getJobConf()));

        // execute program
        env.execute("Mongodb Example");
        long end = System.currentTimeMillis();
        long r = end - start;
        System.out.println("========== end cost time: " + r + " ms;  ==========");

    }
}