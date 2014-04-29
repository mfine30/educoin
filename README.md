educoin
=======

A Learning Cryptocurrency

Our application is designed as a learning tool that demonstrates some of the basics of the bitcoin protocol. 
The code is already documented, but we aim to provide a high level overview here. 

The lists on the left side of the screen enable you to send a transaction from user A to user B, 
and enter the amount in the field below it. 
At the very bottom of the screen we provide a visualization of the block chain. 
   Magenta represents a transaction from the world to a user - genesis blocks and mining rewards.
   Yellow represents a transaction that has not yet been approved by having other blocks added onto it. 
   Green represents an approved transaction.
   Red represents a rejected transaction - mainly, the user does not have enough coins to send.
The transaction history, validation, and other things are listed in the history section in the center of the screen. 
The right side of the screen enables the user to update certain variables about the world.

As a more thorough explanation, we will provide a basic transaction and how the application responds.
Alice originally has 100 coins.
Assume Alice sends 50 coins to Bob. 
Alice will create a Transaction, t, and add it to her blockChain.
Then, she will update the world on this new transaction, and 
someone who is in the world and not Alice nor Bob will validate it. 
The transaction should now appear at the bottom of the application as a yellow block. 
Similarly, David, the other person in the world validates this transaction and 
gets rewarded with 25 (Magenta block) coins for mining it. 
Mining is the process of finding a valid nonce to reach the appropriate number of leading zeros on a hashed string. 
Once two more valid transaction complete, then Alice’s transaction will be green and confirmed.

To better understand, please refer to the code and comments.
