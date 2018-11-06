package org.tron.trongrid;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.mongodb.core.MongoTemplate;

@RestController
public class EventLogController {

  @Autowired
  EventLogRepository eventLogRepository;
  @Autowired
  MongoTemplate mongoTemplate;

  @RequestMapping(method = RequestMethod.GET, value = "/healthcheck")
  public String  healthCheck(){
    return "OK";
  }

  @RequestMapping(method = RequestMethod.GET, value = "/events")
  public List<EventLogEntity> events(
          @RequestParam(value="since", required=false, defaultValue = "0" ) Long timestamp,
          @RequestParam(value="page", required=false, defaultValue="1") int page,
          @RequestParam(value="size", required=false, defaultValue="20") int page_size) {

    page_size = Math.min(200,page_size);
    return eventLogRepository.findByBlockTimestampGreaterThan(timestamp, QUERY.make_pagination(Math.max(0,page-1),page_size,"block_timestamp"));
  }

  @RequestMapping(method = RequestMethod.GET, value = "/event/transaction/{transactionId}")
  public Iterable<EventLogEntity> findOneByTransaction(@PathVariable String transactionId) {
    return eventLogRepository.findByTransactionId(transactionId);
  }

  @RequestMapping(method = RequestMethod.GET, value = "/event/contract/{contractAddress}")
  public List<EventLogEntity> findByContractAddress(@PathVariable String contractAddress,
                                                    @RequestParam(value="since", required=false, defaultValue = "0" ) Long timestamp,
                                                    @RequestParam(value="page", required=false, defaultValue="1") int page,
                                                    @RequestParam(value="size", required=false, defaultValue="20") int page_size) {

    page_size = Math.min(200,page_size);
    return eventLogRepository.findByBlockTimestampAndContractAddressGreaterThan(timestamp, contractAddress,
            QUERY.make_pagination(Math.max(0,page-1),page_size,"block_timestamp"));
  }

  @RequestMapping(method = RequestMethod.GET, value = "/event/contract/{contractAddress}/{eventName}")
  public List<EventLogEntity> findByContractAddressAndEntryName(
          @PathVariable String contractAddress,
          @PathVariable String eventName,
          @RequestParam(value="since", required=false, defaultValue = "0" ) Long timestamp,
          @RequestParam(value="page", required=false, defaultValue="1") int page,
          @RequestParam(value="size", required=false, defaultValue="20") int page_size) {

    page_size = Math.min(200,page_size);
    return eventLogRepository.findByContractAndEventSinceTimestamp(contractAddress,
            eventName,
            timestamp,
            QUERY.make_pagination(Math.max(0,page-1), page_size, "block_timestamp"));
  }

  @RequestMapping(method = RequestMethod.GET, value = "/event/contract/{contractAddress}/{eventName}/{blockNumber}")
  public List<EventLogEntity> findByContractAddressAndEntryNameAndBlockNumber(
      @PathVariable String contractAddress,
      @PathVariable String eventName,
      @PathVariable Long blockNumber) {

    return eventLogRepository
            .findByContractAddressAndEntryNameAndBlockNumber(contractAddress, eventName, blockNumber);

  }

  @RequestMapping(method = RequestMethod.GET, value = "/event/timestamp")
  public List<EventLogEntity> findByBlockTimestampGreaterThan(
          @RequestParam(value="since", required=false, defaultValue = "0" ) Long since_timestamp,
          @RequestParam(value="contract", required=false) String contract_address,
          @RequestParam(value="page", required=false, defaultValue="1") int page,
          @RequestParam(value="size", required=false, defaultValue="20") int page_size) {

    page_size = Math.min(200,page_size);

    if (contract_address == null || contract_address.length() == 0)
      return eventLogRepository.findByBlockTimestampGreaterThan(since_timestamp, QUERY.make_pagination(Math.max(0,page-1),page_size,"block_timestamp"));

    return eventLogRepository.findByBlockTimestampAndContractAddressGreaterThan(since_timestamp, contract_address, QUERY.make_pagination(Math.max(0,page-1),page_size,"block_timestamp"));

  }

  @RequestMapping(method = RequestMethod.GET, value = "/event/filter/contract/{contractAddress}/{eventName}")
  public List<EventLogEntity> filterevent(
          @RequestParam Map<String,String> allRequestParams,
          @PathVariable String contractAddress,
          @PathVariable String eventName,

          @RequestParam(value="since", required=false, defaultValue = "0" ) Long since_timestamp,
          @RequestParam(value="page", required=false, defaultValue="1") int page,
          @RequestParam(value="size", required=false, defaultValue="20") int page_size){

    Query query = new Query();
    query.addCriteria(Criteria.where("contract_address").is(contractAddress));
    query.addCriteria(Criteria.where("event_name").is(eventName));
    query.addCriteria((Criteria.where("block_timestamp").gte(since_timestamp)));
    JSONObject res = JSONObject.parseObject(allRequestParams.get("result"));

    for(String k : res.keySet()){
      if(QUERY.isBool(res.getString(k))){
        query.addCriteria(Criteria.where(String.format("%s.%s","result",k)).is(Boolean.parseBoolean(res.getString(k))));
        continue;
      }
        query.addCriteria(Criteria.where(String.format("%s.%s","result",k)).is((res.getString(k))));
    }

    query.with(QUERY.make_pagination(Math.max(0,page-1),page_size,"block_timestamp"));
    System.out.println(query.toString());
    List<EventLogEntity> result = mongoTemplate.find(query,EventLogEntity.class);
    return result;
  }


//  @RequestMapping(method = RequestMethod.GET, value = "/offset")
//  public List<EventLogEntity> offset_test(
//          @RequestParam(value="since", required=false, defaultValue = "0" ) Long timestamp,
//          @RequestParam(value="page", required=false, defaultValue="1") int page,
//          @RequestParam(value="size", required=false, defaultValue="20") int page_size) {
//
//    Query query = new Query();
//    query.addCriteria(Criteria.where("resource_Node").exists(true));
//
//    return null;
//  }





}
