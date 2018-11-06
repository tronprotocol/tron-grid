package org.tron.trongrid;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.servlet.support.RequestContext;

import javax.servlet.http.HttpServletRequest;

@RestController
public class EventLogController {

  @Autowired
  EventLogRepository eventLogRepository;
  @Autowired
  MongoTemplate mongoTemplate;

  // variables for pagniate
  private long timestamp = 0;
  private int page = 0;
  private int page_size = 20;
  private String sort = "-block_timestamp";

  @RequestMapping(method = RequestMethod.GET, value = "/healthcheck")
  public String  healthCheck(){
    return "OK";
  }

  @RequestMapping(method = RequestMethod.GET, value = "/events")
  public List<EventLogEntity> events(
          HttpServletRequest request) {

    this.setPagniateVariable(request);
    return eventLogRepository.findByBlockTimestampGreaterThan(this.timestamp, QUERY.make_pagination(Math.max(0,this.page-1),Math.min(200,this.page_size),"block_timestamp"));
  }

  @RequestMapping(method = RequestMethod.GET, value = "/event/transaction/{transactionId}")
  public List<EventLogEntity> findOneByTransaction(@PathVariable String transactionId) {
    return eventLogRepository.findByTransactionId(transactionId);
  }

  @RequestMapping(method = RequestMethod.GET, value = "/event/contract/{contractAddress}")
  public List<EventLogEntity> findByContractAddress(@PathVariable String contractAddress,
                                                    HttpServletRequest request) {

    this.setPagniateVariable(request);
    return eventLogRepository.findByBlockTimestampAndContractAddressGreaterThan(this.timestamp, contractAddress,
            QUERY.make_pagination(Math.max(0,this.page-1),Math.min(200,this.page_size),"block_timestamp"));
  }

  @RequestMapping(method = RequestMethod.GET, value = "/event/contract/{contractAddress}/{eventName}")
  public List<EventLogEntity> findByContractAddressAndEntryName(
          @PathVariable String contractAddress,
          @PathVariable String eventName,
          HttpServletRequest request) {

    this.setPagniateVariable(request);
    return eventLogRepository.findByContractAndEventSinceTimestamp(contractAddress,
            eventName,
            this.timestamp,
            QUERY.make_pagination(Math.max(0,this.page-1),Math.min(200,this.page_size),"block_timestamp"));
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
          @RequestParam(value="contract", required=false) String contract_address,
          HttpServletRequest request) {

    this.setPagniateVariable(request);

    if (contract_address == null || contract_address.length() == 0)
      return eventLogRepository.findByBlockTimestampGreaterThan(this.timestamp, QUERY.make_pagination(Math.max(0,this.page-1),Math.min(200,this.page_size),"block_timestamp"));

    return eventLogRepository.findByBlockTimestampAndContractAddressGreaterThan(this.timestamp, contract_address, QUERY.make_pagination(Math.max(0,this.page-1),Math.min(200,this.page_size),"block_timestamp"));

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

  private void setPagniateVariable(HttpServletRequest request){
    if (request.getParameter("page") != null && request.getParameter("page").length() > 0)
      this.page = Integer.parseInt(request.getParameter("page"));
    if (request.getParameter("size") != null && request.getParameter("size").length() > 0)
      this.page_size = Integer.parseInt(request.getParameter("size"));
    if (request.getParameter("since") != null && request.getParameter("since").length() > 0)
      this.timestamp = Long.parseLong(request.getParameter("since"));
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
