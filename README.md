# Tron-Grid

Tron-Grid uses SpringBoot to provide a query interface. It takes Java-Tron and writes event into Mongo DB. The user can poll the Smart Contract's details. Refer to the following link for Java Tron details:   

```
https://github.com/tronprotocol/java-tron
event_parser branch
based on：org.tron.core.db.Manager#sendEventLog
```


## The Following are Four Methods for Polling:
### 1.  By Contract Address:<br>
    curl https://api.shasta.trongrid.io/event/contract/TMJnJcHfdP5rhmXVkwRYb1a9A6gS46PUm6
    
    By default, its gonna return the latest 50 events. If you want to return the latest event:
    curl https://api.shasta.trongrid.io/event/contract/TMJnJcHfdP5rhmXVkwRYb1a9A6gS46PUm6?size=1
    
    if you want to return the second latest event,
    curl https://api.shasta.trongrid.io/event/contract/TMJnJcHfdP5rhmXVkwRYb1a9A6gS46PUm6?size=1&page=2
### 2.  By Contract Address and Event Name:<br>
    curl https://api.trongrid.io/event/contract/TMJnJcHfdP5rhmXVkwRYb1a9A6gS46PUm6/Notify
    
    to retrieve the latest event of the contract:
    curl https://api.trongrid.io/event/contract/TMJnJcHfdP5rhmXVkwRYb1a9A6gS46PUm6/Notify?size=1
    
    to retrieve the second latest event of the contract:
    curl https://api.trongrid.io/event/contract/TMJnJcHfdP5rhmXVkwRYb1a9A6gS46PUm6/Notify?size=1&page=2
### 3.  By Contract Address, Event Name, and Block Height:
    curl https://api.trongrid.io/event/contract/TMJnJcHfdP5rhmXVkwRYb1a9A6gS46PUm6/Notify/88
### 4.  By Transaction ID:<br>
curl https://api.trongrid.io/event/transaction/5c3747ffa94fc87a2188708a9e0758cbd01f000d3d01f6589651921930183f6a
<br>

## Request Parameters:<br>
    since: set a time stamp, default 0, returning all events after that timestamp
    EX: curl https://api.trongrid.io/event/contract/TMJnJcHfdP5rhmXVkwRYb1a9A6gS46PUm6/Notify?since=1541547888000
    
    size: number of results return. Default is 20, maximum is 200
    EX: curl https://api.trongrid.io/event/contract/TMJnJcHfdP5rhmXVkwRYb1a9A6gS46PUm6/Notify?size=10
    
    page: page number
    EX: curl https://api.trongrid.io/event/contract/TMJnJcHfdP5rhmXVkwRYb1a9A6gS46PUm6/Notify?size=10&page=2
    gonna return latest 11 - 20th results
    
    sort: block_timestamp(Ascending order) or -block_timestamp(Descending order), sort result by block_timestamp
An Example Contract:
```
pragma solidity ^0.4.23;

contract Fibonacci {

    event Notify(uint input, uint result);

    function fibonacci(uint number) public constant returns(uint result) {
        if (number == 0) return 0;
        else if (number == 1) return 1;
        else return Fibonacci.fibonacci(number - 1) + Fibonacci.fibonacci(number - 2);
    }

    function fibonacciNotify(uint number) public returns(uint result) {
        result = fibonacci(number);
        emit Notify(number, result);
    }
}
```
Deploy Contract Using Tronweb, Tronbox, or directly using rpc or http call:
```
deploycontract DataStore [{"constant":false,"inputs":[{"name":"number","type":"uint256"}],"name":"fibonacciNotify","outputs":[{"name":"result","type":"uint256"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[{"name":"number","type":"uint256"}],"name":"fibonacci","outputs":[{"name":"result","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"anonymous":false,"inputs":[{"indexed":false,"name":"input","type":"uint256"},{"indexed":false,"name":"result","type":"uint256"}],"name":"Notify","type":"event"}] 608060405234801561001057600080fd5b50610196806100206000396000f30060806040526004361061004c576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680633c7fdc701461005157806361047ff414610092575b600080fd5b34801561005d57600080fd5b5061007c600480360381019080803590602001909291905050506100d3565b6040518082815260200191505060405180910390f35b34801561009e57600080fd5b506100bd60048036038101908080359060200190929190505050610124565b6040518082815260200191505060405180910390f35b60006100de82610124565b90507f71e71a8458267085d5ab16980fd5f114d2d37f232479c245d523ce8d23ca40ed8282604051808381526020018281526020019250505060405180910390a1919050565b6000808214156101375760009050610165565b60018214156101495760019050610165565b61015560028303610124565b61016160018403610124565b0190505b9190505600a165627a7a723058201540ed8f82b334522f0e3a11793ba18c1d184536d7b797b30adbba3ca9b7f52c0029 1000000 30 0
```

After triggering `fibonacciNotify` and event notify:
```
triggercontract 《address: TMJnJcHfdP5rhmXVkwRYb1a9A6gS46PUm6》 fibonacciNotify(uint256) 7 false 1000000 0000000000000000000000000000000000000000000000000000000000000000
```

Return Value
```
[
  {
    "block_number": 88,
    "block_timestamp": 1534767012000,
    "contract_address": "TMJnJcHfdP5rhmXVkwRYb1a9A6gS46PUm6",
    "event_name": "Notify",
    "result": [
      "7",
      "13"
    ],
    "transaction_id": "5c3747ffa94fc87a2188708a9e0758cbd01f000d3d01f6589651921930183f6a"
  }
]
```
Return Format:
```
block_number: block height
block_timestamp: event time stamp
contract_address: contract address
event_name: event name
result: event parameter
transaction_id: transcation id
```


## Build
```
mvn package
```

## Run
```
cd target
nohup java -jar trongrid-0.0.1-SNAPSHOT.jar &
```
