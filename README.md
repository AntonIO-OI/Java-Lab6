# Lab 6
### Author: Anton Bur, Group IP-22, Variant #3

---

### Variant Calculations
| Parameter | Formula | Result |  
|-----------|---------|--------|  
| **C2**    | `3 % 2` | 1      |  
| **C3**    | `3 % 3` | 0      |  

---

### Task Variants
| Step | Description                               | Details                                                                                             |  
|------|-------------------------------------------|-----------------------------------------------------------------------------------------------------|  
| **2** | Determine the task to do based on **C2**: | Set                                                                                                 |  
| **3** | Determine the task to do based on **C3**: | An array with an initial number of elements of 15 and an increase in the number of elements by 30%  |  

---

### How to Run the Project

#### Prerequisites
Ensure [Maven](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html) is [installed](https://maven.apache.org/download.cgi) on your computer.

#### Steps to Execute
1. Clone the repository:
   ```bash  
   git clone https://github.com/AntonIO-OI/Java-Lab6
   cd Java-Lab6 
   ```  

2. Build the project (this will also run unit tests):
   ```bash  
   mvn package  
   ```  

3. Run the project:
   ```bash  
   mvn clean compile exec:java  
   ```  

4. Run unit tests:
   ```bash  
   mvn test  
   ```  

### How to Generate Javadoc

#### 1. Using `javadoc` Command:
1. Open a terminal in the project root directory.
2. Run the following command to generate the Javadoc:
   ```bash
   javadoc -d docs -sourcepath src/main/java -subpackages vegetable
   ```
3. After the process completes, navigate to the `/docs` folder to view the generated documentation.

#### 2. Using Maven:
1. Open a terminal in the project root directory.
2. Run the following Maven command to generate the Javadoc:
   ```bash
   mvn javadoc:javadoc
   ```
3. Once the process is finished, navigate to the `/target/reports/apidocs` folder to access the generated documentation.
