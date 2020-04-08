/*
package org.okkam.flink.mongodb.test;

import com.mongodb.hadoop.MongoInputFormat;
import com.mongodb.hadoop.io.BSONWritable;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.hadoopcompatibility.mapred.HadoopInputFormat;
import org.apache.hadoop.mapred.JobConf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

*/
/**
 * Created by xiaozhong on 2020/4/7.
 *//*

public class MongoSourceJob {

    private static final Logger LOG = LoggerFactory.getLogger(MongoSourceJob.class);

    public static final String MONGO_URI = "mongodb://ip:port/db.collection";

    public static void main(String[] args) throws Exception {
        //获取条件参数
        final ParameterTool parameterTool = ParameterTool.fromArgs(args);
        String webSource = parameterTool.get("webSource", "baidu");
        int year = parameterTool.getInt("year", 2016);
        String condition = String.format("{'source':'%s','year':{'$regex':'^%d'}}", webSource, year);
        //创建运行环境
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        //将mongo数据转化为Hadoop数据格式
        HadoopInputFormat<BSONWritable, BSONWritable> hdIf =
                new HadoopInputFormat<>(new MongoInputFormat(), BSONWritable.class, BSONWritable.class, new JobConf());
        hdIf.getJobConf().set("mongo.input.split.create_input_splits", "false");
        hdIf.getJobConf().set("mongo.input.uri", MONGO_URI);
        hdIf.getJobConf().set("mongo.input.query", condition);

        long count = env.createInput(hdIf)
                .map((MapFunction<Tuple2<BSONWritable, BSONWritable>, String>) value -> {
                    BSONWritable v = value.getField(1);
                    return JSON.parseObject(v.getDoc().toString()).toJSONString();
                })
                .count();
        LOG.info("总共读取到{}条MongoDB数据",count);
    }

}
*/
