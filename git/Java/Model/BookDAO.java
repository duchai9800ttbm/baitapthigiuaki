package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class BookDAO {

	private String hostName;
	private String sqlInstanceName;
    private String userName;
    private String password;
    private Connection connection;
    private String database;
    private String port;
    
     
    public BookDAO(String hostName, String sqlInstanceName, String port, String database, String userName, String password) {
		this.hostName = hostName;
		this.sqlInstanceName = sqlInstanceName;
		this.port = port;
		this.database = database;
		this.userName = userName;
		this.password = password;
	}

	protected void connect() throws SQLException {
        if (connection == null || connection.isClosed()) {
        
        	try {
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
        	
        	String connectionURL = "jdbc:sqlserver://" + hostName + ":" + port + ";instance=" + sqlInstanceName + ";databaseName=" + database;
        
            connection = DriverManager.getConnection(connectionURL, userName, password);
        }
    }
     
    protected void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
     
    public boolean insertBook(Book book) throws SQLException {
        String sql = "INSERT INTO book (title, author, price) VALUES (?, ?, ?)";
        connect();
         
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, book.getTitle());
        statement.setString(2, book.getAuthor());
        statement.setFloat(3, book.getPrice());
         
        boolean rowInserted = statement.executeUpdate() > 0;
        statement.close();
        disconnect();
        return rowInserted;
    }
     
    public ArrayList<Book> listAllBooks() throws SQLException {
        ArrayList<Book> listBook = new ArrayList<>();
         
        String sql = "SELECT * FROM book";
         
        connect();
         
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
         
        while (resultSet.next()) {
            int id = resultSet.getInt("book_id");
            String title = resultSet.getString("title");
            String author = resultSet.getString("author");
            float price = resultSet.getFloat("price");
             
            Book book = new Book(id, title, author, price);
            listBook.add(book);
        }
         
        resultSet.close();
        statement.close();
         
        disconnect();
         
        return listBook;
    }
     
    public boolean deleteBook(Book book) throws SQLException {
        String sql = "DELETE FROM book where book_id = ?";
         
        connect();
         
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, book.getId());
         
        boolean rowDeleted = statement.executeUpdate() > 0;
        statement.close();
        disconnect();
        return rowDeleted;     
    }
     
    public boolean updateBook(Book book) throws SQLException {
        String sql = "UPDATE book SET title = ?, author = ?, price = ?";
        sql += " WHERE book_id = ?";
        connect();
         
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, book.getTitle());
        statement.setString(2, book.getAuthor());
        statement.setFloat(3, book.getPrice());
        statement.setInt(4, book.getId());
         
        boolean rowUpdated = statement.executeUpdate() > 0;
        statement.close();
        disconnect();
        return rowUpdated;     
    }
     
    public Book getBook(int id) throws SQLException {
        Book book = null;
        String sql = "SELECT * FROM book WHERE book_id = ?";
         
        connect();
         
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
         
        ResultSet resultSet = statement.executeQuery();
         
        if (resultSet.next()) {
            String title = resultSet.getString("title");
            String author = resultSet.getString("author");
            float price = resultSet.getFloat("price");
             
            book = new Book(id, title, author, price);
        }
         
        resultSet.close();
        statement.close();
         
        return book;
    }
}
