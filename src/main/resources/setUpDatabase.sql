
--creating books table in bookstore database
CREATE TABLE Books(
                      BookID SERIAL PRIMARY KEY,
                      Title TEXT NOT NULL,
                      Author VARCHAR(40) NOT NULL,
                      Genre VARCHAR(30) NOT NULL,
                      Price REAL NOT NULL CHECK(Price > 0),
                      QuantityInStock INTEGER NOT NULL CHECK(QuantityInStock >= 0)
);

--creating customers table in bookstore database
CREATE TABLE Customers(
                          CustomerID SERIAL PRIMARY KEY,
                          Name VARCHAR(20) NOT NULL,
                          Email VARCHAR(60) UNIQUE,
                          Phone VARCHAR(20) NOT NULL
);

--creating sales table that will hold foreign keys referencing primary keys of books and customers tables
CREATE TABLE Sales (
                       SaleID SERIAL PRIMARY KEY,
                       BookID INTEGER,
                       CustomerID INTEGER,
                       DateOfSale DATE,
                       QuantitySold INTEGER NOT NULL CHECK(QuantitySold >= 0),
                       TotalPrice REAL NOT NULL CHECK(TotalPrice >= 0),
                       CONSTRAINT fk_book FOREIGN KEY (BookID) REFERENCES Books(BookID) ON DELETE SET NULL,
                       CONSTRAINT fk_customer FOREIGN KEY (CustomerID) REFERENCES Customers(CustomerID) ON DELETE SET NULL
);

--creating a trigger to update quantity of books in store
CREATE OR REPLACE FUNCTION update_books_quantity_in_stock()
    RETURNS TRIGGER AS $$
BEGIN
    UPDATE Books
    SET QuantityInStock = QuantityInStock - NEW.QuantitySold
    WHERE BookID = NEW.BookID;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_books_quantity
    AFTER INSERT ON Sales
    FOR EACH ROW
EXECUTE FUNCTION update_books_quantity_in_stock();

--create update book author function
CREATE OR REPLACE FUNCTION update_book_author(
    id INTEGER,
    newAuthor VARCHAR(255)
)
    RETURNS VOID AS
$$
BEGIN
    UPDATE books
    SET author = newAuthor
    WHERE bookid = id;
END;
$$
    LANGUAGE plpgsql;

--create update book genre function
CREATE OR REPLACE FUNCTION update_book_genre(
    id INTEGER,
    newGenre VARCHAR(255)
)
    RETURNS VOID AS
$$
BEGIN
    UPDATE books
    SET genre = newGenre
    WHERE bookid = id;
END;
$$
    LANGUAGE plpgsql;

--create update book price function
CREATE OR REPLACE FUNCTION update_book_price(
    id INTEGER,
    newPrice REAL
)
    RETURNS VOID AS
$$
BEGIN
    IF newPrice > 0 THEN
        UPDATE books
        SET price = newPrice
        WHERE bookid = id;
    END IF;
    END;
                $$
    LANGUAGE plpgsql;

--create update book quantity function
CREATE OR REPLACE FUNCTION update_book_quantity(
    id INTEGER,
    newQuantity INTEGER
)
    RETURNS VOID AS
$$
BEGIN
    IF newQuantity >= 0 THEN
        UPDATE books
        SET quantityinstock = newQuantity
        WHERE bookid = id;
    END IF;
END;
$$
    LANGUAGE plpgsql;

--create update book title function
CREATE OR REPLACE FUNCTION update_book_title(
    id INTEGER,
    newTitle VARCHAR(255)
)
    RETURNS VOID AS
$$
BEGIN
    UPDATE books
    SET title = newTitle
    WHERE bookid = id;
END;
$$
    LANGUAGE plpgsql;

--create update customer name function
CREATE OR REPLACE FUNCTION update_customer_name(
    id INTEGER,
    newName VARCHAR(255)
)
    RETURNS VOID AS
$$
BEGIN
    UPDATE customers
    SET name = newName
    WHERE customerid = id;
END;
$$
    LANGUAGE plpgsql;

--create update customer email function
CREATE OR REPLACE FUNCTION update_customer_email(
    id INTEGER,
    newEmail VARCHAR(255)
)
    RETURNS VOID AS
$$
BEGIN
    UPDATE customers
    SET email = newEmail
    WHERE customerid = id;
END;
$$
    LANGUAGE plpgsql;

--create update customer phone function
CREATE OR REPLACE FUNCTION update_customer_phone(
    id INTEGER,
    newPhone VARCHAR(255)
)
    RETURNS VOID AS
$$
BEGIN
    UPDATE customers
    SET phone = newPhone
    WHERE customerid = id;
END;
$$
    LANGUAGE plpgsql;

