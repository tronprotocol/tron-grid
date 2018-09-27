package org.tron.trongrid;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.PageRequest;

@RestController
public class EventLogController {

  @Autowired
  EventLogRepository eventLogRepository;

  @RequestMapping(method = RequestMethod.GET, value = "/events")
  public Iterable<EventLogEntity> events() {
    return eventLogRepository.findByBlockTimestampGreaterThan((long)0, this.make_pagination(0,100,"block_timestamp"));
  }

  @RequestMapping(method = RequestMethod.GET, value = "/event/transaction/{transactionId}")
  public Iterable<EventLogEntity> findOneByTransaction(@PathVariable String transactionId) {
    return eventLogRepository.findByTransactionId(transactionId);
  }

  @RequestMapping(method = RequestMethod.GET, value = "/event/contract/{contractAddress}")
  public Iterable<EventLogEntity> findByContractAddress(@PathVariable String contractAddress) {
    return eventLogRepository.findByContractAddress(contractAddress);
  }

  @RequestMapping(method = RequestMethod.GET, value = "/event/contract/{contractAddress}/{eventName}")
  public Iterable<EventLogEntity> findByContractAddressAndEntryName(
      @PathVariable String contractAddress,
      @PathVariable String eventName) {
    return eventLogRepository.findByContractAddressAndEntryName(contractAddress, eventName);
  }

  @RequestMapping(method = RequestMethod.GET, value = "/event/contract/{contractAddress}/{eventName}/{blockNumber}")
  public List<EventLogEntity> findByContractAddressAndEntryNameAndBlockNumber(
      @PathVariable String contractAddress,
      @PathVariable String eventName,
      @PathVariable Long blockNumber,
      @RequestParam(value="since", required=false, defaultValue = "0" ) Long timestamp,
      @RequestParam(value="size", required=false, defaultValue="100") int page_size) {


      return eventLogRepository.findByContractAndEventSinceTimestamp(contractAddress,
                                                                      eventName,
                                                                      timestamp,
              this.make_pagination(0,page_size,"block_timestamp"));


  }

  @RequestMapping(method = RequestMethod.GET, value = "/event/timestamp")
  public List<EventLogEntity> findByBlockTimestampGreaterThan(
          @RequestParam(value="since", required=false, defaultValue = "0" ) Long since_timestamp,
          @RequestParam(value="contract", required=false) String contract_address,
          @RequestParam(value="page", required=false, defaultValue="0") int page,
          @RequestParam(value="size", required=false, defaultValue="50") int page_size) {

    if (contract_address == null || contract_address.length() == 0)
      return eventLogRepository.findByBlockTimestampGreaterThan(since_timestamp, this.make_pagination(page,page_size,"block_timestamp"));

    return eventLogRepository.findByBlockTimestampAndContractAddressGreaterThan(since_timestamp, contract_address, this.make_pagination(page,page_size,"block_timestamp"));


  }

  private Pageable make_pagination(int page_num, int page_size, String sort_property){
    return PageRequest.of(page_num, page_size, Sort.Direction.DESC, sort_property);
  }



}
