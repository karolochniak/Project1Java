# MatrixProject: Java Concurrency Benchmark ☕

A Java-based benchmarking project designed to compare different multithreading approaches in Java. The program demonstrates the performance differences between classical thread creation and high-level thread pooling during a computationally heavy task: matrix multiplication.

---

## 🎯 About the Project

The core of this project is the multiplication of two large matrices (1500x1500). To speed up the calculations, the workload is distributed across multiple threads, calculating one row at a time. 

The project measures and compares the execution time of two distinct concurrency paradigms:

1.  **Classical Threads (`java.lang.Thread`):** 
    Creates a new, independent thread for every single row of the matrix (resulting in 1500 concurrent threads). This highlights the overhead and scheduling costs associated with massive thread creation.
2.  **Thread Pool (`ExecutorService`):** 
    Utilizes a fixed thread pool sized optimally to the number of available CPU cores. It submits 1500 tasks to the pool, allowing the system to efficiently manage resources without context-switching overload.

---

## 🧠 Key Concepts Explored

*   **Java Concurrency API:** Practical usage of `Runnable`, `Thread`, and the `java.util.concurrent` package.
*   **ExecutorService:** Managing task execution through thread pools (`Executors.newFixedThreadPool`).
*   **Hardware Awareness:** Dynamically adapting the thread pool size based on hardware capabilities (`Runtime.getRuntime().availableProcessors()`).
*   **Performance Profiling:** Measuring execution time using `System.currentTimeMillis()` to analyze the overhead of OS-level thread management versus JVM-managed worker threads.

---

## 🚀 Compilation & Execution

Ensure you have the Java Development Kit (JDK) installed on your system. 

### 1. Compilation
Navigate to the directory containing the source file and compile it:
```bash
javac MatrixProject.java
