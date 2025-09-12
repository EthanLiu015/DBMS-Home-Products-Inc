-- Drop database if it exists to start fresh
DROP DATABASE IF EXISTS HomeProductsIncSmall;

-- Create the database
CREATE DATABASE HomeProductsIncSmall;

-- Use the newly created database
USE HomeProductsIncSmall;

--
-- Table structure for table `tblWarehouse`
--
CREATE TABLE `tblWarehouse` (
  `WarehouseID` int NOT NULL AUTO_INCREMENT,
  `Address1` varchar(100) DEFAULT NULL,
  `Address2` varchar(100) DEFAULT NULL,
  `City` varchar(50) DEFAULT NULL,
  `State` varchar(2) DEFAULT NULL,
  `ZipCode` varchar(10) DEFAULT NULL,
  `GeneralPhoneNumber` varchar(15) DEFAULT NULL,
  `Supervisor` varchar(100) DEFAULT NULL,
  `SupervisorPhone` varchar(15) DEFAULT NULL,
  `Capacity` int DEFAULT NULL,
  `NumberOfEmployees` smallint DEFAULT NULL,
  PRIMARY KEY (`WarehouseID`)
);

--
-- Dumping data for table `tblWarehouse`
--
INSERT INTO `tblWarehouse` VALUES (1,'123 Industrial Way','Suite 100','Springfield','IL','62704','(555) 111-2222','John Smith','(555) 111-2223',10000,15),(2,'456 Distribution Dr','','Shelbyville','IL','62565','(555) 333-4444','Jane Doe','(555) 333-4445',25000,30);

--
-- Table structure for table `tblProduct`
--
CREATE TABLE `tblProduct` (
  `ProductID` varchar(4) NOT NULL,
  `ProductDescription` varchar(200) DEFAULT NULL,
  `UnitPrice` decimal(10,2) DEFAULT NULL,
  `UnitsOnHand` smallint DEFAULT NULL,
  `Class` varchar(2) DEFAULT NULL,
  `WarehouseID` int DEFAULT NULL,
  PRIMARY KEY (`ProductID`),
  KEY `WarehouseID` (`WarehouseID`),
  CONSTRAINT `tblproduct_ibfk_1` FOREIGN KEY (`WarehouseID`) REFERENCES `tblWarehouse` (`WarehouseID`)
);

--
-- Dumping data for table `tblProduct`
--
INSERT INTO `tblProduct` VALUES ('AA01','20V Cordless Drill',79.99,150,'TO',1),('AP01','Smart Refrigerator',2499.99,20,'AP',2),('GS01','Garden Hose 50ft',25.50,300,'GS',1),('HW01','Hammer',12.95,500,'HW',1),('SG01','Lawn Mower',399.00,50,'SG',2);

--
-- Table structure for table `tblSalesRep`
--
CREATE TABLE `tblSalesRep` (
  `SalesRepID` int NOT NULL AUTO_INCREMENT,
  `LastName` varchar(30) DEFAULT NULL,
  `FirstName` varchar(30) DEFAULT NULL,
  `BusinessNumber` varchar(15) DEFAULT NULL,
  `CellNumber` varchar(15) DEFAULT NULL,
  `HomeNumber` varchar(15) DEFAULT NULL,
  `FaxNumber` varchar(15) DEFAULT NULL,
  `Title` varchar(50) DEFAULT NULL,
  `Address1` varchar(50) DEFAULT NULL,
  `City` varchar(50) DEFAULT NULL,
  `State` varchar(2) DEFAULT NULL,
  `ZipCode` varchar(10) DEFAULT NULL,
  `CommissionRate` decimal(10,2) DEFAULT NULL,
  `SalesRepManagerID` int DEFAULT NULL,
  PRIMARY KEY (`SalesRepID`),
  KEY `SalesRepManagerID` (`SalesRepManagerID`),
  CONSTRAINT `tblsalesrep_ibfk_1` FOREIGN KEY (`SalesRepManagerID`) REFERENCES `tblSalesRep` (`SalesRepID`)
);

--
-- Dumping data for table `tblSalesRep`
--
INSERT INTO `tblSalesRep` VALUES (1,'Jones','Robert','(555) 987-6543','(555) 111-2222','(555) 333-4444','(555) 555-6666','Senior Sales Manager','100 Main St','Capital City','IL','62701',0.10,NULL),(5,'Smith','Alice','(555) 234-5678','(555) 876-5432','','','Sales Representative','200 Oak Ave','Springfield','IL','62704',0.07,1);

--
-- Table structure for table `tblCustomer`
--
CREATE TABLE `tblCustomer` (
  `CustomerID` int NOT NULL AUTO_INCREMENT,
  `FirstName` varchar(30) DEFAULT NULL,
  `LastName` varchar(30) DEFAULT NULL,
  `Address1` varchar(50) DEFAULT NULL,
  `City` varchar(50) DEFAULT NULL,
  `State` varchar(2) DEFAULT NULL,
  `ZipCode` varchar(5) DEFAULT NULL,
  `Credit` decimal(10,2) DEFAULT NULL,
  `SalesRepID` int DEFAULT NULL,
  `Company` varchar(50) DEFAULT NULL,
  `Website` varchar(200) DEFAULT NULL,
  `Email` varchar(100) DEFAULT NULL,
  `BusinessNumber` varchar(15) DEFAULT NULL,
  `CellNumber` varchar(15) DEFAULT NULL,
  `Title` varchar(50) DEFAULT NULL,
  `CustomerStatus` varchar(10) DEFAULT NULL,
  `Notes` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`CustomerID`),
  KEY `SalesRepID` (`SalesRepID`),
  CONSTRAINT `tblcustomer_ibfk_1` FOREIGN KEY (`SalesRepID`) REFERENCES `tblSalesRep` (`SalesRepID`)
);

--
-- Dumping data for table `tblCustomer`
--
INSERT INTO `tblCustomer` VALUES (1,'Michael','Scott','1725 Slough Avenue','Scranton','PA','18503',5000.00,5,'Dunder Mifflin','www.dundermifflin.com','mscott@dundermifflin.com','(570) 123-4567','(570) 987-6543','Regional Manager','Active','Best Boss'),(2,'Dwight','Schrute','1 Schrute Farms','Honesdale','PA','18431',10000.00,5,'Schrute Farms','www.schrute-farms.com','dks@schrute-farms.com','(570) 234-5678','(570) 876-5432','Beet Farmer','Active','Assistant to the Regional Manager');

--
-- Table structure for table `tblOrder`
--
CREATE TABLE `tblOrder` (
  `OrderID` int NOT NULL AUTO_INCREMENT,
  `CustomerID` int DEFAULT NULL,
  `OrderDate` date DEFAULT NULL,
  `ShippingDate` date DEFAULT NULL,
  `OrderStatus` varchar(50) DEFAULT NULL,
  `ShippingMethod` varchar(50) DEFAULT NULL,
  `SalesTax` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`OrderID`),
  KEY `CustomerID` (`CustomerID`),
  CONSTRAINT `tblorder_ibfk_1` FOREIGN KEY (`CustomerID`) REFERENCES `tblCustomer` (`CustomerID`)
);

--
-- Dumping data for table `tblOrder`
--
INSERT INTO `tblOrder` VALUES (122,1,'2024-01-15','2024-01-18','Paid','UPS Ground',0.07),(131,2,'2024-02-20','2024-02-22','Unpaid','US Standard Ground',0.06);

--
-- Table structure for table `tblOrdersProducts`
--
CREATE TABLE `tblOrdersProducts` (
  `OrderID` int NOT NULL,
  `ProductID` varchar(4) NOT NULL,
  `QuantityOrdered` int DEFAULT NULL,
  `QuotedPrice` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`OrderID`,`ProductID`),
  KEY `ProductID` (`ProductID`),
  CONSTRAINT `tblordersproducts_ibfk_1` FOREIGN KEY (`OrderID`) REFERENCES `tblOrder` (`OrderID`),
  CONSTRAINT `tblordersproducts_ibfk_2` FOREIGN KEY (`ProductID`) REFERENCES `tblProduct` (`ProductID`)
);

--
-- Dumping data for table `tblOrdersProducts`
--
INSERT INTO `tblOrdersProducts` VALUES (122,'AA01',2,150.00),(122,'HW01',5,60.00),(131,'GS01',1,25.50);

--
-- Table structure for table `tblPayment`
--
CREATE TABLE `tblPayment` (
  `PaymentID` int NOT NULL AUTO_INCREMENT,
  `CustomerID` int DEFAULT NULL,
  `OrderID` int DEFAULT NULL,
  `PaymentDate` date DEFAULT NULL,
  `Amount` decimal(10,2) DEFAULT NULL,
  `Method` varchar(50) DEFAULT NULL,
  `CardHolder` varchar(100) DEFAULT NULL,
  `CardNumber` varchar(16) DEFAULT NULL,
  `ExpirationDate` date DEFAULT NULL,
  `BooleanCreditCard` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`PaymentID`),
  KEY `CustomerID` (`CustomerID`),
  KEY `OrderID` (`OrderID`),
  CONSTRAINT `tblpayment_ibfk_1` FOREIGN KEY (`CustomerID`) REFERENCES `tblCustomer` (`CustomerID`),
  CONSTRAINT `tblpayment_ibfk_2` FOREIGN KEY (`OrderID`) REFERENCES `tblOrder` (`OrderID`)
);

--
-- Dumping data for table `tblPayment`
--
INSERT INTO `tblPayment` VALUES (1,1,122,'2024-01-15',224.70,'Credit Card','Michael Scott','1234567812345678','2026-12-31',1);