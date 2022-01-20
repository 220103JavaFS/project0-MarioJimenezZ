-- CREATE USERS TABLE -- 
CREATE TABLE users (
	id SERIAL PRIMARY KEY,
	email VARCHAR(60) NOT NULL,
	password VARCHAR(60) NOT NULL,
	balance DECIMAL DEFAULT 2000.0,
	type VARCHAR(60),
	first_name VARCHAR(60),
	last_name VARCHAR(60),
	orders INTEGER
);

-- CREATE CATEGORIES TABLE --
CREATE TABLE categories (
	id SERIAL PRIMARY KEY,
	name VARCHAR(60) NOT NULL,
	description TEXT,
	products INTEGER
);

-- CREATE PRODUCTS TABLE -- 
CREATE TABLE products (
	id SERIAL PRIMARY KEY,
	seller_id INTEGER REFERENCES users(id),
	name VARCHAR(60) NOT NULL,
	category_id INTEGER REFERENCES categories(id),
	description TEXT,
	price DECIMAL
);

-- CREATE SELLER APPLICATIONS TABLE --
CREATE TABLE seller_apps (
	id SERIAL PRIMARY KEY,
	user_id INTEGER REFERENCES users(id) UNIQUE NOT NULL,
	status VARCHAR(10)	
);

-- CREATE CARTS TABLE --
CREATE TABLE carts (
	id SERIAL PRIMARY KEY,
	user_id INTEGER REFERENCES users(id) UNIQUE NOT NULL,
	product_list INTEGER ARRAY
);

-- CREATES CUSTOMER_ORDERS TABLES --
CREATE TABLE customer_orders (
	order_id SERIAL PRIMARY KEY,
	user_id INTEGER REFERENCES users(id) NOT NULL,
	product_list INTEGER ARRAY,
	order_date TIMESTAMP,
	order_total DECIMAL,
	order_status VARCHAR(30)
) 

-- CREATES SELLER_ORDERS TABLES --
CREATE TABLE seller_orders (
	order_id SERIAL PRIMARY KEY,
	buyer_id INTEGER REFERENCES users(id) NOT NULL,
	seller_id INTEGER REFERENCES users(id) NOT NULL,
	customer_order_id INTEGER REFERENCES customer_orders(order_id) NOT NULL,
	product_list INTEGER ARRAY,
	order_date TIMESTAMP,
	order_total DECIMAL,
	order_status VARCHAR(30)
) 

-- TRIGGERS & FUNCTIONS --
CREATE OR REPLACE FUNCTION increase_products() RETURNS TRIGGER AS 
$$
BEGIN 
	UPDATE categories SET products = (SELECT products FROM categories WHERE NEW.category_id = categories.id)+1
	WHERE categories.id = NEW.category_id; 
	RETURN NEW; 
END
$$
LANGUAGE plpgsql; 

CREATE TRIGGER increment_products AFTER INSERT ON products FOR EACH ROW EXECUTE PROCEDURE increase_products();

-- EXAMPLE DATA --
-- USERS
INSERT INTO users (email, "password",balance,"type",first_name,last_name,orders) VALUES
	 ('example.admin@revature.net','5f4dcc3b5aa765d61d8327deb882cf99',2000.0,'ADMINISTRATOR', 'Mario','Jimenez',0),
	 ('example.seller@revature.net','5f4dcc3b5aa765d61d8327deb882cf99',500.0,'SELLER', 'Luigi', 'Sellsalot',0);
-- CATEGORIES
INSERT INTO categories ("name",description,products) VALUES
	 ('Home','Everything you would ever need to decorate your home', 0),
	 ('Electronics','All the great electronic items for cheap', 0),
	 ('Office Supplies','Things for the office', 0);
-- PRODUCTS
INSERT INTO products (seller_id,"name",category_id,description,price) VALUES
	 (2,'Cute Photo Frame', 1,'a photo frame that will make your picture even better!', 50.0),
	 (2,'Computer Desktop', 2,'Best computer you''ll ever have', 900.0),
	 (1,'Stapler', 3,'Best stapler in the business!', 20.5);