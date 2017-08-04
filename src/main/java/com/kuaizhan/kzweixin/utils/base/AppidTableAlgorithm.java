package com.kuaizhan.kzweixin.utils.base;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.SingleKeyTableShardingAlgorithm;
import com.kuaizhan.kzweixin.utils.Crc32Util;

import java.util.Collection;

/**
 * 使用appId做分表的算法的基类
 * 支持xxx_0这样的表名
 * Created by zixiong on 2017/08/03.
 */
public abstract class AppidTableAlgorithm implements SingleKeyTableShardingAlgorithm<String> {

    // 分表的table数目
    private int tableNum;
    private static final String SEPARATOR = "_";

    public AppidTableAlgorithm(int tableNum) {
        this.tableNum = tableNum;
    }

    @Override
    public String doEqualSharding(Collection<String> collection, ShardingValue<String> shardingValue) {
        String appId = shardingValue.getValue();
        // 逻辑tableName校验
        String logicTableName = shardingValue.getLogicTableName();
        String splits[] = logicTableName.split(SEPARATOR);
        int length = splits.length;
        // 不是类似xxx_0的表名
        if (length == 1 || ! splits[length - 1].matches("\\d+")) {
            throw new RuntimeException("[AppidTableAlgorithm] logic table name must like 'xxx_0'");
        }

        // 组装分表后的表名
        splits[length - 1] = String.valueOf(Crc32Util.getValue(appId) % tableNum);
        return String.join(SEPARATOR, splits);
    }

    @Override
    public Collection<String> doInSharding(Collection<String> collection, ShardingValue<String> shardingValue) {
        return null;
    }

    @Override
    public Collection<String> doBetweenSharding(Collection<String> collection, ShardingValue<String> shardingValue) {
        return null;
    }
}
