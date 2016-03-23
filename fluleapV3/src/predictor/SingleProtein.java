package predictor;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SingleProteinPredictor
 */
@WebServlet("/single-protein")
public class SingleProtein extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SingleProtein() {
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
		// print header and banner
		out.println(ServletUtilities.getHead());
		out.println(ServletUtilities.getBanner());

		// print body content
		out.println(getBodyContent());

		try {
			if (request.getParameter("seq").equals("null")) {
				out.println("<p><warning>! Please input sequence.</warning></p>");
			} else if (request.getParameter("seq").equals("invalid")) {
				out.println("<p><warning>! Sequence contains invalid characters.\n"
						+ "Please input only valid characters from 20 standard amino acids.</warning></p>");
			}
		} catch (NullPointerException npe) {
			// do nothing if null pointer exception is caught
			// to prevent exception generated if no seq parameter
		}

		// print form for input
		out.println("<p>\n"
				+ "<form action=\"single-protein-result\" method=\"post\">\n"
				+ "Select protein: <br>\n"
				+ "<br>\n"
				+ "<div class=\"2u\"><select name=\"selectedProtein\">\n"
				+ "<option value=\"HA\">HA</option>\n"
				+ "<option value=\"M1\">M1</option>\n"
				+ "<option value=\"M2\">M2</option>\n"
				+ "<option value=\"NA\">NA</option>\n"
				+ "<option value=\"NP\">NP</option>\n"
				+ "<option value=\"NS1\">NS1</option>\n"
				+ "<option value=\"NS2\">NS2</option>\n"
				+ "<option value=\"PA\">PA</option>\n"
				+ "<option value=\"PB1\">PB1</option>\n"
				+ "<option value=\"PB1F2\">PB1-F2</option>\n"
				+ "<option value=\"PB2\">PB2</option>\n"
				+ "</select></div>\n"
				+ "<br></p>\n"
				+ "<p>\n"
				+ "Protein sequence: <br>\n"
				+ "<div class=\"8u\"><textarea name=\"seq\" placeholder=\"Sequence\" cols=\"70\" rows=\"5\"></textarea></div><br>\n"
				+ "<input type=\"reset\" class=\"button\" value=\"Clear\">\n"
				+ "<input type=\"submit\" class=\"button\">\n" + "</form>\n"
				+ "</p>");

		out.println("</section>\n" + "</div>\n" + "</div>\n" + "</div>\n"
				+ "</div>)\n");
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

	private String getBodyContent() {
		return ("<div id=\"main\">\n"
				+ "<div class=\"container\">\n"
				+ "<div class=\"row main-row\">\n"
				+ "<div class=\"12u\">\n"
				+ "<section>\n"
				+ "<h2>Single protein predictor</h2>\n"
				+ "<p>Predictor for individual influenza proteins including glycoproteins HA and NA, nucleoprotein NP, both matrix proteins M1 and M2, both non-structural proteins NS1 and NS2, as well as the rest of the viral polymerase proteins PA, PB1, PB1-F2, and PB2.</p>\n"
				+ "<p><strong>Please select protein and input protein sequence in the text box below. Only sequences containing 20 standard amino acids are allowed.</strong></p>\n");
	}

}
