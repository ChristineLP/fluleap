package predictor;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Contact
 */
@WebServlet("/contact")
public class Contact extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Contact() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println(ServletUtilities.getHead());
		out.println(ServletUtilities.getBanner());

		out.println("<div id=\"main\">\n" + "<div class=\"container\">\n"
				+ "<div class=\"row main-row\">\n" + "<div class=\"8u\">\n"
				+ "<section>\n" + "<header>\n" + "<h2>Contact form</h2>\n"
				+ "</header>\n");

		// if redirected from results page with null value
		// print warning message
		try {
			if (request.getParameter("form").equals("incomplete")) {
				out.println("<p><warning>! Please complete the form.</warning></p>");
			} 
		} catch (NullPointerException npe) {
			// do nothing if null pointer exception is caught
			// to prevent exception generated if no seqArr parameter
		}

		// contact form
		out.println("<form action=\"mail\" method=\"post\" action=\"#\">\n"
				+ "<div class=\"row half\">\n"
				+ "<div class=\"6u\"><input type=\"text\" class=\"text\" name=\"name\" placeholder=\"Name\" required /></div>\n"
				+ "</div>\n"
				+ "<div class=\"row half\">\n"
				+ "<div class=\"6u\"><input type=\"text\" class=\"text\" name=\"email\" placeholder=\"Email\" required /></div>\n"
				+ "</div>\n"
				+ "<div class=\"row half\">\n"
				+ "<div class=\"6u\"><input type=\"text\" class=\"text\" name=\"subject\" placeholder=\"Subject\" required /></div>\n"
				+ "</div>\n"
				+ "<div class=\"row half\">\n"
				+ "<div class=\"8u\">\n"
				+ "<textarea name=\"message\" placeholder=\"Message\" required></textarea>\n"
				+ "</div>\n"
				+ "</div>\n"
				+ "<div class=\"row\">\n"
				+ "<div class=\"8u\">\n"
				+ "<input type=\"reset\" class=\"button\" value=\"Clear\">\n"
				+ "<input type=\"submit\" class=\"button\" value=\"Send message\">\n"
				+ "</div>\n" + "</div>\n" + "</form>");

		out.println("</section>\n" + "</div>");

		out.println(getMailingAddress());
		out.println("</div>\n" + "</div>\n" + "</div>\n");
		out.println(ServletUtilities.getFooter());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	private String getMailingAddress() {
		return ("<div class=\"4u\">\n" + "<section>\n"
				+ "<h2>Mailing address</h2>\n"
				+ "<p>Bioinformatics Center<br>\n"
				+ "Department of Biochemistry<br>\n"
				+ "National University of Singapore<br>\n" + "MD7<br>\n"
				+ "8 Medical Drive<br>\n" + "Singapore 117597<br></p>\n"
				+ "<p>+65 6516 3357</p>\n"
				+ "<p>christine@bic.nus.edu.sg</p>\n" + "</section>\n"
				+ "</div>");
	}
}
