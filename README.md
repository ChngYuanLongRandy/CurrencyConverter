# Currency Converter

## Project Brief
You will be developing an automated currency conversion system. 
In this system, you will be given a set of user profiles as well as instructions 
pertaining to a set of transactions. Each transaction will be a conversion of 
some sort for one user. Your task will be to spin up your conversion system and 
process all of the transactions, updating the user profiles as required.

## Requirements
Update the wallets based on the transactions 

## Assumptions / Behavior
- File formats/field order will not change. Assume the values will.
- Transaction currencies are ordered (FROM, TO). If user’s wallet does not 
contain the TO currency, add that currency to the wallet.
- Transaction amount is indicating the FROM amount
If the transaction FROM and TO currency are the same, skip the transaction.
- If user’s wallet has insufficient amount of the FROM currency, skip the transaction.
- If the user specified in the transaction doesn’t exist in the wallets file (users.json), skip the transaction.
- If the currency (FROM or TO) is invalid (e.g. xxx), skip the transaction.
For every valid or skipped transaction, logging (with appropriate exceptions/log level) must be 
done on the console and in a log file.
