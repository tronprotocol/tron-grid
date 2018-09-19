package org.tron.trongrid;

import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "eventLog")
public class EventLogEntity implements Serializable {

  private static final long serialVersionUID = -70777625567836430L;

  @Id
  private String id;

  @Field(value = "block_number")
  @JsonProperty(value = "block_number")
  private long blockNumber;

  @Field(value = "block_timestamp")
  @JsonProperty(value = "block_timestamp")
  private long blockTimestamp;

  @Field(value = "contract_address")
  @JsonProperty(value = "contract_address")
  private String contractAddress;

  @Field(value = "event_name")
  @JsonProperty(value = "event_name")
  private String entryName;

  @Field(value = "result")
  @JsonProperty(value = "result")
  private JSONArray resultJsonArray;

  @Field(value = "transaction_id")
  @JsonProperty(value = "transaction_id")
  private String transactionId;

  public EventLogEntity(long blockNumber, long blockTimestamp, String contractAddress,
      String entryName, JSONArray resultJsonArray, String transactionId) {
    this.blockNumber = blockNumber;
    this.blockTimestamp = blockTimestamp;
    this.contractAddress = contractAddress;
    this.entryName = entryName;
    this.resultJsonArray = resultJsonArray;
    this.transactionId = transactionId;
  }

  public static long getSerialVersionUID() {
    return serialVersionUID;
  }

  public long getBlockNumber() {
    return blockNumber;
  }

  public void setBlockNumber(long blockNumber) {
    this.blockNumber = blockNumber;
  }

  public long getBlockTimestamp() {
    return blockTimestamp;
  }

  public void setBlockTimestamp(long blockTimestamp) {
    this.blockTimestamp = blockTimestamp;
  }

  public String getContractAddress() {
    return contractAddress;
  }

  public void setContractAddress(String contractAddress) {
    this.contractAddress = contractAddress;
  }

  public String getEntryName() {
    return entryName;
  }

  public void setEntryName(String entryName) {
    this.entryName = entryName;
  }

  public JSONArray getResultJsonArray() {
    return resultJsonArray;
  }

  public void setResultJsonArray(JSONArray resultJsonArray) {
    this.resultJsonArray = resultJsonArray;
  }

  public String getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }
}
