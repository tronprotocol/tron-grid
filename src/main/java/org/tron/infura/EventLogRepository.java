package org.tron.infura;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventLogRepository extends MongoRepository<EventLogEntity, String> {

  @Override
  List<EventLogEntity> findAll();

  List<EventLogEntity> findByTransactionId(String transactionId);

  List<EventLogEntity> findByContractAddress(String contractAddress);

  List<EventLogEntity> findByContractAddressAndEntryName(String contractAddress, String entryName);

  List<EventLogEntity> findByContractAddressAndEntryNameAndBlockNumber(String contractAddress,
      String entryName, Long blockNumber);

}