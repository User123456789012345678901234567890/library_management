# CSA Group Project 1: Resource Management System

## Overview
This project implements an object-oriented **Management System** designed to handle resource allocation and request assignment. The system models physical or digital resources using an inheritance hierarchy and processes incoming requests based on capacity, availability, and priority queue logic.

---

## Architecture Diagram

```text
                  +------------------+
                  |  ABSTRACT CLASS  |
                  +--------+---------+
                           |
       +-------------------+-------------------+
       |                   |                   |
+------+-------+    +------+-------+    +------+-------+
|  Subclass A  |    |  Subclass B  |    |  Subclass C  |
+--------------+    +------+-------+    +--------------+
                           |
                           v
              +--------------------------+
              |    MANAGEMENT SYSTEM     |
              +------------+-------------+
                           |
            +--------------+--------------+
            |                             |
            v                             v
     +--------------+              +--------------+
     |  Resources   |              |   Requests   |
     +--------------+              +--------------+
     | • Capacity   |              | • Waiting    |
     | • Avail.     |              |   List       |
     +------+-------+              | • Priority   |
            |                      +------+-------+
            |                             |
            +--------------+--------------+
                           |
                           v
                    +--------------+
                    |  Assignment  |
                    +--------------+
