package org.tron.trongrid;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class QueryFactory {

    private Query query;

    public static final String findByContractAndEventSinceTimestamp = "{ 'contract_address' : ?0, " +
            "'event_name': ?1,  " +
            "'$or' : [ {'block_timestamp' : ?2}, {'block_timestamp' : {$gt : ?2}} ], " +
            "'resource_Node' : {$exists : true} }";

    public static final String findByContractSinceTimeStamp = "{ 'contract_address' : ?0, " +
            "'$or' : [ {'block_timestamp' : ?1}, {'block_timestamp' : {$gt : ?1}} ], " +
            "'resource_Node' : {$exists : true}}";

    public static Pageable make_pagination(int page_num, int page_size, String sort_property){

        if (sort_property.charAt(0) == '-')
            return PageRequest.of(page_num, page_size, Sort.Direction.DESC, sort_property.substring(1));

        return PageRequest.of(page_num, page_size, Sort.Direction.ASC, sort_property);
    }

    public static boolean isBool(String s) {
        return s.equalsIgnoreCase("true") || s.equalsIgnoreCase("false");
    }

    public QueryFactory(){
        this.query = new Query();
        this.query.addCriteria(Criteria.where("resource_Node").exists(true));
    }

    public QueryFactory(long timestamp, long blocknum){
        this.query = new Query();
        this.query.addCriteria(Criteria.where("resource_Node").exists(true));
        this.setTimestampGreaterEqual(timestamp);
        if (blocknum > 0)
            this.setBocknumberGreaterEqual(blocknum);
    }

    public void setTimestampGreaterEqual (long timestamp) {
        this.query.addCriteria(Criteria.where("block_timestamp").gte(timestamp));
    }

    public void setBocknumberGreaterEqual (long blockNum) {
        this.query.addCriteria(Criteria.where("block_number").gte(blockNum));
    }

    public void setContractAddress (String addr) {
        this.query.addCriteria(Criteria.where("contract_address").is(addr));
    }

    public void setPageniate(Pageable page){
        this.query.with(page);
    }

    public void setEventName (String event) {
        this.query.addCriteria(Criteria.where("event_name").is(event));
    }

    public void setTxid (String txid) {
        this.query.addCriteria(Criteria.where("transaction_id").is(txid));
    }

    public void setBockNum(long block){ this.query.addCriteria(Criteria.where("block_number").is(block)); }

    public String toString (){
        return this.query.toString();
    }

    public Query getQuery() { return this.query; }

}
