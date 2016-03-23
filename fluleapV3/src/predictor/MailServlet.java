package predictor;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class MailApp
 */
@WebServlet("/mail")
public class MailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	static final String TO = "christine@bic.nus.edu.sg";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MailServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// first check parameters
		// all parameters must have value
		boolean isComplete = true;
		String errorData = "";

		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String subject = request.getParameter("subject");
		String message = request.getParameter("message");
		if (name == null || name.length() == 0 || email == null
				|| email.length() == 0 || subject == null
				|| subject.length() == 0 || message == null
				|| message.length() == 0) {
			isComplete = false;
			errorData = "form=incomplete";
		}

		// only proceed to send response if parameter values are not empty
		if (isComplete) {
			try {
				SendMail.send(TO, name, email, subject, message);

				response.setContentType("text/html");
				PrintWriter out = response.getWriter();

				out.println(ServletUtilities.getHead());
				out.println(ServletUtilities.getBanner());

				out.println("<div id=\"main\">\n"
						+ "<div class=\"container\">\n"
						+ "<div class=\"row main-row\">\n"
						+ "<div class=\"8u\">\n" + "<section>\n" + "<header>\n"
						+ "<h2>Contact form</h2>\n" + "</header>\n");

				out.println("<p><strong>Thank you for your message.</strong></p>");
				out.println("<input type=\"button\" onclick=\"history.back();\" class=\"button\" value=\"Back\">");

				out.println("</section>\n" + "</div>");

				out.println(getMailingAddress());
				out.println("</div>\n" + "</div>\n" + "</div>\n");
				out.println(ServletUtilities.getFooter());
			} catch (Exception e) {
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();

				out.println(ServletUtilities.getHead());
				out.println(ServletUtilities.getBanner());

				out.println("<div id=\"main\">\n"
						+ "<div class=\"container\">\n"
						+ "<div class=\"row main-row\">\n"
						+ "<div class=\"8u\">\n" + "<section>\n" + "<header>\n"
						+ "<h2>Contact form</h2>\n" + "</header>\n");

				out.println("<p><strong>An unexpected error occured. Please try again.</strong></p>");
				out.println("<input type=\"button\" onclick=\"history.back();\" class=\"button\" value=\"Back\">");

				out.println("</section>\n" + "</div>");

				out.println(getMailingAddress());
				out.println("</div>\n" + "</div>\n" + "</div>\n");
				out.println(ServletUtilities.getFooter());
			}

		} else {
			response.sendRedirect("contact?" + errorData);
		}

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
