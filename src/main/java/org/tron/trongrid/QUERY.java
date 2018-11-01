package org.tron.trongrid;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class QUERY {

    public static final String findByContractAndEventSinceTimestamp = "{ 'contract_address' : ?0, " +
            "'event_name': ?1,  " +
            "'$or' : [ {'block_timestamp' : ?2}, {'block_timestamp' : {$gt : ?2}} ], " +
            "'resource_Node' : {$exists : true} }";

    public static final String findByContractSinceTimeStamp = "{ 'contract_address' : ?0, " +
            "'$or' : [ {'block_timestamp' : ?1}, {'block_timestamp' : {$gt : ?1}} ], " +
            "'resource_Node' : {$exists : true}}";

    public static Pageable make_pagination(int page_num, int page_size, String sort_property){

        return PageRequest.of(page_num, page_size, Sort.Direction.DESC, sort_property);
    }


}
