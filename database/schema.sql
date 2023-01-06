-- schema.sql
-- Creating database 
CREATE SCHEMA IF NOT EXISTS `eshop` ;
USE `eshop` ;

-- Creating table called customers in eshop
CREATE TABLE IF NOT EXISTS customers(
  name varchar(32) not null,
  address varchar(128) not null,
  email varchar(128) not null,
  primary key(name)
  );

-- Loading data into the table
insert into customers (name, address, email ) values ( "fred","201 Cobblestone Lane","fredflintstone@bedrock.com");
insert into customers (name, address, email ) values ( "sherlock","221B Baker Street, London","sherlock@consultingdetective.org");
insert into customers (name, address, email ) values ( "spongebob","124 Conch Street, Bikini Bottom","spongebob@yahoo.com");
insert into customers (name, address, email ) values ( "jessica","698 Candlewood Land, Cabot Cove","fletcher@gmail.com");
insert into customers (name, address, email ) values ( "dursley","4 Privet Drive, Little Whinging, Surrey","dursley@gmail.com");

use eshop;
CREATE TABLE orders(
	order_id varchar(8) not null,
    delivery_id varchar(8) ,
    status varchar(32) default "pending",
    order_date date not null,
    name varchar(32) not null,
    
	primary key(order_id),
    constraint fk_name
		foreign key(name)
        references customers(name)
  );

Create table if not exists item(
	id int auto_increment not null,
	item varchar(32) not null,
    quantity int not null,
    order_id varchar(8) not null,
    
    primary key(id),
     constraint fk_order_id 
		foreign key(order_id)
        references orders(order_id)
    
    );
    
Create table if not exists order_status(
	order_id varchar(8) not null,
    delivery_id varchar(128) ,
    status char(20),
    status_update timestamp not null

        );