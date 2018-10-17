package org.tron.trongrid;

public class QUERY {

    public static final String findByContractAndEventSinceTimestamp = "{ 'contract_address' : ?0, " +
            "'event_name': ?1,  " +
            "'$or' : [ {'block_timestamp' : ?2}, {'block_timestamp' : {$gt : ?2}} ], " +
            "'resource_Node' : 'FullNode' }";

    public static final String findByContractSinceTimeStamp = "{ 'contract_address' : ?0, " +
            "'$or' : [ {'block_timestamp' : ?1}, {'block_timestamp' : {$gt : ?1}} ], " +
            "'resource_Node' : 'FullNode'}";


}
