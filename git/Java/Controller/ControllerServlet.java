package Controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Model.Book;
import Model.BookDAO;


public class ControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BookDAO bookDAO;
	
	public void init() {
		String hostName = getServletContext().getInitParameter("hostName");
		String sqlInstanceName = getServletContext().getInitParameter("sqlInstanceName");
		String database = getServletContext().getInitParameter("database");
		String port = getServletContext().getInitParameter("port");
		String userName = getServletContext().getInitParameter("userName");
		String password = getServletContext().getInitParameter("password");
		
		bookDAO = new BookDAO(hostName, sqlInstanceName, port, database, userName, password);
	}
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		String action = request.getServletPath();
    	
        try {
        	switch (action) {
        		case "/new":
        			showNewForm(request, response);
        			break;
        		case "/insert":
        			insertBook(request, response);
        			break;
        		case "/delete":
        			deleteBook(request, response);
        			break;
        		case "/edit":
        			showEditForm(request, response);
        			break;
        		case "/update":
        			updateBook(request, response);
        			break;
        		default:
        			listBook(request, response);
        	}
        }catch (Exception e) {
        	e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		doGet(request, response);
	}
	
	private void listBook(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        List<Book> listBook = bookDAO.listAllBooks();
        request.setAttribute("listBook", listBook);
        RequestDispatcher dispatcher = request.getRequestDispatcher("BookList.jsp");
        dispatcher.forward(request, response);
    }
 
    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("BookForm.jsp");
        dispatcher.forward(request, response);
    }
    
    private void insertBook(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        float price = Float.parseFloat(request.getParameter("price"));
        Book newBook = new Book(title, author, price);
        bookDAO.insertBook(newBook);
        response.sendRedirect("list");
    }
    
    private void deleteBook(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
 
        Book book = new Book(id);
        bookDAO.deleteBook(book);
        response.sendRedirect("list");
    }
 
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Book existingBook = bookDAO.getBook(id);
        RequestDispatcher dispatcher = request.getRequestDispatcher("BookForm.jsp");
        request.setAttribute("book", existingBook);
        dispatcher.forward(request, response);
 
    }   
 
    private void updateBook(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        float price = Float.parseFloat(request.getParameter("price"));
 
        Book book = new Book(id, title, author, price);
        bookDAO.updateBook(book);
        response.sendRedirect("list");
    }
}
