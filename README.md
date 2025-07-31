# File Sharing Cloud – Distributed File System

## Description

This project implements a **distributed file-sharing system** for working with **ASCII-encoded text files**. It simulates a virtual file system across multiple nodes in a network.

Each node can store part of the file system, and users interact with the system using a command-line interface (CLI). Nodes can join or leave the network at any time, and the system is resilient to node failures.

## Features

- **Add, pull, and remove files** from the virtual file system
- **Recursive folder support** when adding or removing directories
- **CLI-based interaction** with simple commands
- **Resilient network structure**, including fault detection and recovery
- **File backup** and replication support
- **Dynamic node participation** – nodes can join/leave the network during runtime
- **Supports non-centralized topologies** for higher fault tolerance and performance
- **Two failure detection thresholds:**
  - *Soft limit* (e.g., 1000ms): marks a node as suspicious
  - *Hard limit* (e.g., 10000ms): triggers removal and redistribution of files

## Commands

All commands are entered via CLI, and file paths are relative to the node's working directory (defined in the config file):

- `add name` – Adds a file or folder into the distributed system  
- `pull name` – Downloads a file from the system to the local working directory  
- `remove name` – Deletes a file or folder from the system  
- `stop` – Gracefully shuts down the node

> Note: File names cannot contain spaces.

## Configuration

Each node loads its config file on startup. Example config options:

```properties
root=./working_dir
port=5050
bootstrap_ip=192.168.1.10
bootstrap_port=5000
failure_soft_limit=1000
failure_hard_limit=10000
```

# System Overview

## Configuration Parameters

- **root** – Local folder used as working directory  
- **port** – Port where the node listens for incoming messages  
- **bootstrap_ip / bootstrap_port** – Bootstrap server used only for initial node discovery  
- **failure_soft_limit** – Time in milliseconds before a node is marked as suspicious  
- **failure_hard_limit** – Time in milliseconds before a node is considered failed  

---

## Architecture

Two supported topologies:

### 1. Complete Graph
- Direct connections between all nodes  
- Simpler, but not scalable  

### 2. Sparse (Non-complete) Graph _(Recommended)_
- No single point of failure  
- Communication is routed through intermediate nodes  
- System restructures automatically when nodes join or leave  

---

## Bootstrap Server

- Used only once during node startup  
- Forwards the new node to a known active node, then becomes inactive  
- Does **not** manage or store system topology  
- Requires **minimal** bandwidth  

---

## Fault Tolerance

- Nodes **regularly ping** their neighbors  
- If a node is unresponsive:
  - Marked as **suspicious** after `soft limit`  
  - Confirmed and removed after `hard limit`  
- Failed node's data is recovered by a **backup (buddy) node**  
- System must tolerate **worst-case scenarios** like coordinated multi-node failures  

---

## File Storage

- Files are stored on the node where they were added  
- Every file must have **at least one backup**  
- If a node fails:
  - Its files are reassigned to other active nodes  

---

## Network Simulation

- Supports both **real** and **simulated** networks  
- Messages can arrive **out of order** (non-FIFO)  
- On local runs:
  - **Random artificial delays** are introduced  
  - Communication must **not assume localhost**; use **actual IPs**  
