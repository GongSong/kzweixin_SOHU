package com.kuaizhan.kzweixin.utils.base;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.SingleKeyTableShardingAlgorithm;
import com.kuaizhan.kzweixin.utils.DBTableUtil;

import java.util.Collection;

/**
 * Created by zixiong on 2017/08/03.
 */
public class AppidTableAlgorithm implements SingleKeyTableShardingAlgorithm<String> {

    @Override
    public String doEqualSharding(Collection<String> collection, ShardingValue<String> shardingValue) {
        String appId = shardingValue.getValue();
        String tableName = DBTableUtil.getFanTableName(appId);
        return tableName;
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
