# Inventory System - Quick Setup  

A simple inventory management system with Docker and MySQL.  
You need Docker 27.5.1 or Java 21.0.7
## Installation  

### Option 1: Using Docker (Recommended)  
1. Pull and run the pre-configured image (includes MySQL):  
   ```bash
   docker pull mateusdev26/inventory-system:latest  
   docker run -i mateusdev26/inventory-system:latest
   ```  

### Option 2: Manual Setup (Without Docker)  
1. Clone and run:  
   ```bash
   git clone https://github.com/mateusdev26/inventory-system.git  
   cd inventory-system  
   java -jar target/*.jar  
   ```  
   *Note: Requires MySQL server running locally with default credentials.*  

---

No database setup needed - the system includes pre-loaded sample data.  

> **Important:** The Docker version auto-configures MySQL. For manual setup, ensure MySQL is installed and running before starting the app.
