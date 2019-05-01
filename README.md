# TronGrid

## THIS DOC REFER TO THE LEGACY FORMAT. IT STILL WORK FOR RETRO-COMPATIBILITY, BUT WILL BE DEPRECATED SOONER OR LATER. PLEASE REFER TO THE OFFICIAL [README](https://github.com/tronprotocol/tron-grid/blob/master/README.md)

TronGrid v2 uses a set of NodeJS apps to talk with Redis and PostgreSQL to provide a simple, fast and reliable query interface for the Tron API.


## The Following are Four Methods for Polling:
### 1.  By Contract Address:<br>

https://api.trongrid.io/event/contract/TEEXEWrkMFKapSMJ6mErg39ELFKDqEs6w3
    
### 2.  By Contract Address and Event Name:<br>

curl https://api.trongrid.io/event/contract/TEEXEWrkMFKapSMJ6mErg39ELFKDqEs6w3/DiceResult

### 3.  By Contract Address, Event Name, and Block Height:

https://api.trongrid.io/event/contract/TEEXEWrkMFKapSMJ6mErg39ELFKDqEs6w3/DiceResult/7273383
    
### 4.  By Transaction ID:<br>

https://api.trongrid.io/event/transaction/d74ba9c3947b509db385fe2df5fb1dc49f10fb33da93e1e5903d897714ef0f5c


## Request Parameters:<br>
`fromTimestamp` sets a time stamp, default 0, returning all events after or before that timestamp. For example:

https://api.trongrid.io/event/contract/TEEXEWrkMFKapSMJ6mErg39ELFKDqEs6w3?fromTimestamp=1541547888000
 
For retro-compatibility you can pass `since` instead of `fromTimestamp`.
    
`size` indicates the number of results returned. Default is 20, maximum is 200. Example:

https://api.trongrid.io/event/contract/TMJnJcHfdP5rhmXVkwRYb1a9A6gS46PUm6/Notify?size=10
    
    
`page` is no more supported.
    
`sort` indicates the order. By default the order is descending. To explicitly indicate it, use
```
sort=block_timestamp
```
for ascending order and 
```
sort=-block_timestamp
```
for descending order. For example:

https://api.trongrid.io/event/contract/TEEXEWrkMFKapSMJ6mErg39ELFKDqEs6w3?fromTimestamp=1541547888000&sort=block_timestamp


`fingerprint` is necessary for pagination. Any time you require an API that could return more data that the indicate size, you will see that the latest element has the property `_fingerprint`. To get the next page, you can just call again the same API adding the parameter `fingerprint=[previous _fingerint parameter]`. For example:

https://api.trongrid.io/event/contract/TEEXEWrkMFKapSMJ6mErg39ELFKDqEs6w3?fingerprint=e1E1OqO3vrhmwE23


`onlyConfirmed` returns only the confirmed events.

`onlyUnconfirmed` returns only unconfirmed events.

If you pass both it returns an error.

