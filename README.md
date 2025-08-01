# Distributed Servent Network

This project implements a simplified distributed system of interconnected nodes ("servents") that communicate through serialized messages and dynamically manage their presence in the network using a decentralized structure inspired by the **Kademlia** protocol.

---

## 📌 Overview

Each node (servant) runs on a specific port and host, has a unique identifier, and participates in message-based communication with other nodes. The system supports joining, failure detection, and dynamic maintenance of the network topology.

---

## 🧩 Components

### 1. Servents

Each servent is defined by the following:

- `Host`: Address where it runs (e.g. `localhost`)
- `Port`: Port it listens to for incoming connections
- `Id`: Unique ID for internal use
- `Key`: SHA-1-based unique key used in routing

---

### 2. Messages

All communication is handled via **messages**, which are serialized objects sent over sockets between servents. The base class `Message` is extended by various message types, each of which implements a `handle()` method to define its behavior when received.

#### Message Fields:

- `Id`: Unique ID generated via `AtomicInteger`
- `Text`: Optional content
- `Sender`: Originating servent
- `Receiver`: Target servent
- `Route`: List of intermediate servents the message passed through

#### Supported Message Types:

- `AddMessage`
- `CheckedMessage`
- `CheckMessage`
- `FailMessage`
- `JoinAskMessage`
- `JoinTellMessage`
- `PingMessage`
- `PongMessage`
- `PullAskMessage`
- `PullTellMessage`
- `RemoveMessage`
- `RemovedMessage`

#### Message Handling:

- `MessageListener`: Listens for incoming messages on a socket and places them into a `BlockingQueue`
- `MessageHandler`: Processes messages from the queue in a separate thread and delegates them to the appropriate handler

---

### 3. Network Joining & Communication

The system uses a **Kademlia-like** protocol to handle joining and communication:

#### Joining the Network:

1. A new servent contacts a **bootstrap node** at `localhost:10000`.
2. The bootstrap node:
   - Generates a unique `Key` for the new servent using SHA-1
   - Finds closest nodes by XOR distance
   - Returns a list of neighbors
3. The new servent sends `Ping` messages to those neighbors
4. Neighbors respond with `Pong` and add the new node to their set

#### Failure Detection:

- Each servent periodically sends `Ping` messages to known neighbors
- If no `Pong` is received within `FAILURE_SOFT` ms:
  - It checks via another neighbor
- If no response within `FAILURE_HARD` ms:
  - The node is considered dead
  - It is removed from the active set
  - Bootstrap is notified

---

## 🔄 Node Lifecycle Summary

- Nodes listen for messages on dedicated ports
- Messages are processed asynchronously using threads and queues
- The network dynamically expands as nodes join
- Nodes monitor their peers and prune unreachable ones
- Bootstrap is only used for initial discovery

---

## ⚙️ Technologies Used

- Java
- Sockets
- Threading (ExecutorService, custom threads)
- SHA-1 hashing
- BlockingQueue for inter-thread communication
- Custom message serialization

---

## 📝 Notes

- Each message carries a trace of its route (`route` list)
- Node key uniqueness is ensured via SHA-1 hashing
- The system does **not** assume reliable or FIFO message delivery
- Each node operates independently, with decentralized failure recovery
