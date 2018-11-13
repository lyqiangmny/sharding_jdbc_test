package com.lyqiang.sharding;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.SingleKeyTableShardingAlgorithm;
import com.google.common.collect.Range;

import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * @author guolq on 2018/11/13.
 */
public class MyShardingAlgorithm implements SingleKeyTableShardingAlgorithm<Integer> {

    private Integer tableCount = 3;

    @Override
    public String doEqualSharding(Collection<String> collection, ShardingValue<Integer> shardingValue) {
        Integer modValue = shardingValue.getValue() % tableCount;
        String modStr = modValue < tableCount ? "" + modValue : modValue.toString();
        for (String each : collection) {
            if (each.endsWith(modStr)) {
                return each;
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Collection<String> doInSharding(Collection<String> collection, ShardingValue<Integer> shardingValue) {
        Collection<String> result = new LinkedHashSet<>(collection.size());
        for (Integer value : shardingValue.getValues()) {
            Integer modValue = value % tableCount;
            String modStr = modValue < tableCount ? "" + modValue : modValue.toString();
            for (String tableName : collection) {
                if (tableName.endsWith(modStr)) {
                    result.add(tableName);
                }
            }
        }
        return result;
    }

    @Override
    public Collection<String> doBetweenSharding(Collection<String> collection, ShardingValue<Integer> shardingValue) {
        Collection<String> result = new LinkedHashSet<>(collection.size());
        Range<Integer> range = shardingValue.getValueRange();
        for (Integer i = range.lowerEndpoint(); i <= range.upperEndpoint(); i++) {
            Integer modValue = i % tableCount;
            String modStr = modValue < tableCount ? "" + modValue : modValue.toString();
            for (String each : collection) {
                if (each.endsWith(modStr)) {
                    result.add(each);
                }
            }
        }
        return result;
    }
}
