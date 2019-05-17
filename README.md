TBh8rNhGN1eh22Mae7H7ma6Nh3ouyd3AF3

# TronGrid

TronGrid v3 (TG3) uses a set of NodeJS apps to talk with Redis and PostgreSQL to provide a simple, fast and reliable query interface for the Tron API.

### For a reference to the legacy(v2) version please refer to this version of the [README](https://github.com/tronprotocol/tron-grid/blob/legacy/README.md)

## Notes:

### Versioning

TronGrid v3 (TG3) will use api versioning moving forward. As this is the first iteration of the improved TronGrid, we will start with **v1**. ex: `https://api.trongrid.io/v1`

### Endpoints

1. Accounts
2. Assets
3. Blocks
4. Contracts
5. Network
6. Proposals
7. Transactions
8. Witnesses

### Parameters, Queries, & Return Values

- Addresses in TG3 can be passed in base58 or hex formats.
- Query parameters can be passed in camelCase or snake_case.
- All returned JSON properties will be in snake_case (at the first level at least)
- **NB:** In this document, we will primarily use base58 and snake_case formats

## APIs

### Accounts API

#### 1. Get Account Info By Address

- _GET_ https://api.trongrid.io/v1/accounts/:address
- JavaTron (JT) API:
  - `/wallet/getaccount`
- Usage:
  - Returns information about a specific account
- Params:
  - address The account’s address in base58 or hex format (0x... and 41...)
- Options:
  - `only_confirmed`: Shows only the situation at latest confirmed block.
    `true` | `false` default `false`
  - `assets`: Shows the assets in the account
    `true` | `false` default `false`
- ex: https://api.trongrid.io/v1/accounts/TLCuBEirVzB6V4menLZKw1jfBTFMZbuKq7only_confirmed=false&assets=true
- Return example:

```JSON
    {
      "success": true,
      "meta": {
        "at": 1558109062846,
        "page_size": 1
      },
      "data": [
        {
          "account_resource": {
            "energy_usage": 6027620,
            "frozen_balance_for_energy": {
              "expire_time": 1558164300000,
              "frozen_balance": 2116000000000
            },
            "latest_consume_time_for_energy": 1558108998000
          },
          "address": "41704833c02883b3261f7baf62f8cb19b4b0c2e64e",
          "allowance": 704953,
          "asset": [...],
          "assetV2": [...],
          "asset_issued_ID": "31303031343736",
          "asset_issued_name": "47616d65546f6b656e",
          "balance": 4196409173,
          "create_time": 1529897991000,
          "free_asset_net_usageV2": [...],
          "is_witness": true,
          "latest_consume_free_time": 1557905064000,
          "latest_opration_time": 1558108998000,
          "latest_withdraw_time": 1557905064000
        }
      ]
    }
```

#### 2. Get Transactions By Account Address

- _GET_ https://api.trongrid.io/v1/accounts/:address/transactions
- JavaTron (JT) API:
  - `/walletextension/gettransactionfromthis`
  - `/walletextension/gettransactiontothis`
- Usage:
  - Returns all the transactions related to a specified account.
- Params:
  `address`: The account’s address
- Options:
  - `only_confirmed`: Shows only confirmed.
    `true` | `false` default `false`
  - `only_unconfirmed`: Shows only unconfirmed.
    `true` | `false` default `false`
  - `only_to`: Only transaction to address.
    `true` | `false` default `false`
  - `only_from`: Only transaction from address.
    `true` | `false` default `false`
  - `limit`: The requested number of transaction per page. Default `20`. Max `200`.
  - `fingerprint`: The fingerprint of the last transaction returned by the previous page
  - `order_by`: Pre sorts the results during the query.
    Example:
    `order_by=block_number,asc`
    `order_by=block_timestamp,desc`
  - `min_block_timestamp`: The minimum transaction timestamp default `0`
    Alias: `min_timestamp`
  - `max_block_timestamp`: The maximum transaction timestamp default `now`
    Alias: `max_timestamp`
- ex: (N.B. Filter are non exclusives.)
  - GET https://api.trongrid.io/v1/accounts/TLCuBEirVzB6V4menLZKw1jfBTFMZbuKq/transactions?only_to=true&only_from=true
    is equivalent to
  - GET https://api.trongrid.io/v1/accounts/TLCuBEirVzB6V4menLZKw1jfBTFMZbuKq/transactions

#### 3. Get Account Resources By Address

- _GET_ https://api.trongrid.io/v1/accounts/:address/resources
- JavaTron (JT) API:
  - `/wallet/getaccountresource`
- Usage:
  - Returns the resources associated to a specific account.
- Params:
  - `address`: The account’s address
- ex: https://api.trongrid.io/v1/accounts/TLCuBEirVzB6V4menLZKw1jfBTFMZbuKq/resources

```JSON
    {
      "free_net_used": 4740,
      "free_net_limit": 5000,
      "asset_net_used": [...],
      "asset_net_limit": [...],
      "total_net_limit": 43200000000,
      "total_net_weight": 7001650727,
      "energy_used": 366327641,
      "energy_limit": 402999576,
      "total_energy_limit": 100000000000,
      "total_energy_weight": 496278437
    }

```

#### 4. Create An Account

- _POST_ https://api.trongrid.io/v1/accounts
- JavaTron (JT) API: `/wallet/createaccount`
- Usage:
  - Creates the transaction for the creation of a new account starting from an existing account.
- Payload:
  - `creator`: Address of the account creating the new account
  - `address`: Address of the new account

#### 5. Update Account Name

- _PUT_ https://api.trongrid.io/v1/accounts/:address
- JavaTron (JT) API: `/wallet/updateaccount`
- Usage:
  - Returns the transaction to change the name of an account. It requires that the account exists.
- Params:
  - `address`: The account’s address
- Options:

  - `only_confirmed`: Shows only confirmed.
    `true` | `false` default `false`
  - `only_unconfirmed`: Shows only unconfirmed.
    `true` | `false` default `false`

- Payload:
  - `name`: New name of the new account

#### 6. Update Account Resources

- _PUT_ https://api.trongrid.io/v1/accounts/:address/balances?action=
- JavaTron (JT) API:
  - `/wallet/freezebalance`
  - `/wallet/unfreezebalance`
- Usage:
  - Returns the transaction to freeze a certain amount of TRX in the balance.
- Params:
  - `address`: The account’s address
- Options: (**required**)
  - `action` The action to be performed.
  - Example:
    `action=freeze`
    `action=unfreeze`
- Payload:
  - `amount`: Amount to be frozen/unfrozen in SUN (**only freeze**)
  - `duration`: Duration of the freeze (**only freeze**)
  - `resource`: Requested resource
    Accepted values:
    `BANDWIDTH`
    `ENERGY`
  - `receiver_address`: Address of the account receiving the bandwidth/energy, if any (**optional**)
- ex: https://api.trongrid.io/v1/accounts/:address/balances?action=

## The Following are Four Methods for Polling:

### 1. By Contract Address:<br>

https://api.trongrid.io/event/contract/TEEXEWrkMFKapSMJ6mErg39ELFKDqEs6w3

### 2. By Contract Address and Event Name:<br>

curl https://api.trongrid.io/event/contract/TEEXEWrkMFKapSMJ6mErg39ELFKDqEs6w3/DiceResult

### 3. By Contract Address, Event Name, and Block Height:

https://api.trongrid.io/event/contract/TEEXEWrkMFKapSMJ6mErg39ELFKDqEs6w3/DiceResult/7273383

### 4. By Transaction ID:<br>

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
