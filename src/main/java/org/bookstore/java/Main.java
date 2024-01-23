package org.bookstore.java;

import java.io.*;
import java.sql.*;
import java.util.Scanner;

public class Main {
    static String jdbcURL;
    static String username;
    static String password;
    static Connection connection;

    public static void main(String[] args) {
        try {
            connectToDB();

            boolean fin = false;
            Scanner commandInput = new Scanner(System.in);

            while (!fin) {
                System.out.println("""
                    Commands
                    1 - set up database
                    2 - update book info
                    3 - update customer info
                    4 - list books by genre
                    5 - list books by author
                    6 - list customer purchase history
                    7 - list sales history
                    8 - list total revenue by genre
                    9 - finish
                    input : """);
                String command = commandInput.nextLine();
                switch (command) {
                    case "1" -> setUpDatabase();
                    case "2" -> updateBookDetails();
                    case "3" -> updateCustomerDetails();
                    case "4" -> listBooksByGenre();
                    case "5" -> listBooksByAuthor();
                    case "6" -> listCustomerPurchaseHistory();
                    case "7" -> listSaleHistory();
                    case "8" -> generateTotalRevenueByGenre();
                    case "9" -> fin = true;
                }
            }
            disconnectFromDB();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    private static void connectToDB() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Insert the database URL");
        jdbcURL = scanner.nextLine();
        System.out.println("insert username");
        username = scanner.nextLine();
        System.out.println("Insert password");
        password = scanner.nextLine();
        connection = DriverManager.getConnection(jdbcURL, username, password);
        System.out.println("connected");
    }
    public static void setUpDatabase() throws SQLException {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("src/main/resources/setUpDatabase.sql"))) {
            String string;
            while ((string = bufferedReader.readLine()) != null) {
                stringBuilder.append(string).append('\n');
            }
        }catch (IOException e) {
        }
        Statement statement = connection.createStatement();
        statement.execute(stringBuilder.toString());
        statement.close();
    }
    private static void updateBookDetails() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("book id: ");
        int id = scanner.nextInt();
        System.out.println("Enter the column name to update (title,author,genre,price,quantity)");
        String columnName = scanner.nextLine();
        switch (columnName) {
            case "title" -> updateBookTitle(id);
            case "author" -> updateBookAuthor(id);
            case "genre" -> updateBookGenre(id);
            case "price" -> updateBookPrice(id);
            case "quantity" -> updateBookQuantity(id);
        }
    }
    private static void updateBookQuantity(int id) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter new genre");
        int newQuantity = scanner.nextInt();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT update_book_quantity(?,?)");
        preparedStatement.setInt(1,id);
        preparedStatement.setInt(2,newQuantity);
        preparedStatement.execute();
        preparedStatement.close();
    }
    private static void updateBookPrice(int id) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter new genre");
        double newPrice = scanner.nextDouble();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT update_book_price(?,?)");
        preparedStatement.setInt(1,id);
        preparedStatement.setDouble(2,newPrice);
        preparedStatement.execute();
        preparedStatement.close();
    }
    private static void updateBookGenre(int id) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter new genre");
        String newGenre = scanner.nextLine();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT update_book_genre(?,?)");
        preparedStatement.setInt(1,id);
        preparedStatement.setString(2,newGenre);
        preparedStatement.execute();
        preparedStatement.close();
    }
    private static void updateBookAuthor(int id) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter new author");
        String newAuthor = scanner.nextLine();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT update_book_author(?,?)");
        preparedStatement.setInt(1,id);
        preparedStatement.setString(2,newAuthor);
        preparedStatement.execute();
        preparedStatement.close();
    }
    private static void updateBookTitle(int id) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter new title");
        String newTitle = scanner.nextLine();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT update_book_title(?,?)");
        preparedStatement.setInt(1,id);
        preparedStatement.setString(2,newTitle);
        preparedStatement.execute();
        preparedStatement.close();
    }
    //Vulnerability
    private static void listBooksByGenre() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        Statement statement = connection.createStatement();
        System.out.println("Input genre");
        String genre = scanner.nextLine();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM books WHERE genre='" + genre + "';");
        System.out.println("Book ID, Title, Author, Genre, Price, Quantity");
        while (resultSet.next()) {
            int id = resultSet.getInt("bookId");
            String title = resultSet.getString("title");
            String author = resultSet.getString("author");
            double price = resultSet.getDouble("price");
            int quantity = resultSet.getInt("quantity");
            System.out.println(id + ", " + title + ", " + author + ", " + genre + ", " + price + ", " + quantity);
        }
    }
    //Vulnerability
    private static void listBooksByAuthor() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        Statement statement = connection.createStatement();
        System.out.println("Input genre");
        String author = scanner.nextLine();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM books WHERE author='" + author + "';");
        System.out.println("Book ID, Title, Author, Genre, Price, Quantity");
        while (resultSet.next()) {
            int id = resultSet.getInt("bookId");
            String title = resultSet.getString("title");
            String genre = resultSet.getString("genre");
            double price = resultSet.getDouble("price");
            int quantity = resultSet.getInt("quantity");
            System.out.println(id + ", " + title + ", " + author + ", " + genre + ", " + price + ", " + quantity);
        }
    }
    //Vulnerability
    private static void listCustomerPurchaseHistory() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        Statement statement = connection.createStatement();
        System.out.println("Input customer id");
        int customerId = scanner.nextInt();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM sales WHERE customerid='" + customerId + "';");
        System.out.println("Sale ID, Book ID, Customer ID, Date Of Sale, Quantity Sold, Total Price");
        while (resultSet.next()) {
            int saleId = resultSet.getInt("saleId");
            int bookId = resultSet.getInt("bookId");
            Date dateOfSale = resultSet.getDate("dateOfSale");
            int quantitySold = resultSet.getInt("quantitySold");
            double totalPrice = resultSet.getDouble("totalPrice");
            System.out.println(saleId + ", " + bookId + ", " + customerId + ", " + dateOfSale + ", " + quantitySold + ", " + totalPrice);
        }
    }
    private static void listSaleHistory() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("""
                SELECT Sales.DateOfSale AS DateOfSale, Books.Title AS BookTitle, Customers.Name as CustomerName\s
                FROM Sales
                JOIN Books ON Sales.BookID = Books.BookID
                JOIN Customers ON Sales.CustomerID = Customers.CustomerID;""");
        System.out.println("Book Title, Customer Name, Date Of Sale");
        while (resultSet.next()) {
            String bookTitle = resultSet.getString("bookTitle");
            Date dateOfSale = resultSet.getDate("dateOfSale");
            String customerName = resultSet.getString("customerName");
            System.out.println(bookTitle + ", " + customerName + ", " + dateOfSale);
        }
    }
    private static void generateTotalRevenueByGenre() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("""
                SELECT Books.Genre, SUM(Sales.TotalPrice) as Revenue
                FROM Sales
                JOIN Books ON Sales.BookID = Books.BookID
                GROUP BY Books.Genre;""");
        System.out.println("Genre, Revenue");
        while (resultSet.next()) {
            String genre = resultSet.getString("genre");
            double revenue = resultSet.getDouble("revenue");
            System.out.println(genre + ", " + revenue);
        }
    }
    private static void updateCustomerDetails() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("book id: ");
        int id = scanner.nextInt();
        System.out.println("Enter the column name to update (name,email,phone)");
        String columnName = scanner.nextLine();
        switch (columnName) {
            case "name" -> updateCustomerName(id);
            case "email" -> updateCustomerEmail(id);
            case "phone" -> updateCustomerPhone(id);
        }
    }
    private static void updateCustomerPhone(int id) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter new name");
        String newPhone = scanner.nextLine();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT update_customer_phone(?,?)");
        preparedStatement.setInt(1,id);
        preparedStatement.setString(2,newPhone);
        preparedStatement.execute();
        preparedStatement.close();
    }
    private static void updateCustomerEmail(int id) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter new name");
        String newEmail = scanner.nextLine();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT update_customer_email(?,?)");
        preparedStatement.setInt(1,id);
        preparedStatement.setString(2,newEmail);
        preparedStatement.execute();
        preparedStatement.close();
    }
    private static void updateCustomerName(int id) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter new name");
        String newName = scanner.nextLine();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT update_customer_name(?,?)");
        preparedStatement.setInt(1,id);
        preparedStatement.setString(2,newName);
        preparedStatement.execute();
        preparedStatement.close();
    }
    private static void disconnectFromDB() throws SQLException {
        connection.close();
        System.out.println("disconnected");
    }
}