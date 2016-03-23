package predictor;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Strains
 */
@WebServlet("/strain")
public class Strains extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Strains() {
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

		out.println(getBodyContent());

		// if redirected from results page with null value
		// print warning message
		try {
			if (request.getParameter("seqArr").equals("null")) {
				out.println("<p><warning>! Please input sequence for at least ONE protein.</warning></p>");
			} else if (request.getParameter("seqArr").equals("invalid")) {
				out.println("<p><warning>! Sequence contains invalid characters or is of insufficient length.\n"
						+ "Please input only valid characters from 20 standard amino acids and ensure protein length is at least 5.</warning></p>");
			}
		} catch (NullPointerException npe) {
			// do nothing if null pointer exception is caught
			// to prevent exception generated if no seqArr parameter
		}

		out.println("<br><p>\n"
				+ "<form action=\"strains-results\" method=\"post\">\n"
				+ "<table>\n"
				+ "<tr><td class=\"leftcolumn\"><h4>Strain name</h4></td><td><div class=\"6u\"><input type=\"text\" class=\"text\" name=\"strainName\" placeholder=\"Optional\"><br></div></td></tr>\n"
				+ "<tr><td class=\"leftcolumn\"><h3>HA</h3></td><td><div class=\"8u\"><textarea name=\"HAseq\" placeholder=\"HA sequence\" cols=\"70\" rows=\"5\"></textarea></div></td></tr>\n"
				+ "<tr><td class=\"leftcolumn\"><h3>M1</h3></td><td><div class=\"8u\"><textarea name=\"M1seq\" placeholder=\"M1 sequence\" cols=\"70\" rows=\"5\"></textarea></div></td></tr>\n"
				+ "<tr><td class=\"leftcolumn\"><h3>M2</h3></td><td><div class=\"8u\"><textarea name=\"M2seq\" placeholder=\"M2 sequence\" cols=\"70\" rows=\"5\"></textarea></div></td></tr>\n"
				+ "<tr><td class=\"leftcolumn\"><h3>NA</h3></td><td><div class=\"8u\"><textarea name=\"NAseq\" placeholder=\"NA sequence\" cols=\"70\" rows=\"5\"></textarea></div></td></tr>\n"
				+ "<tr><td class=\"leftcolumn\"><h3>NP</h3></td><td><div class=\"8u\"><textarea name=\"NPseq\" placeholder=\"NP sequence\" cols=\"70\" rows=\"5\"></textarea></div></td></tr>\n"
				+ "<tr><td class=\"leftcolumn\"><h3>NS1</h3></td><td><div class=\"8u\"><textarea name=\"NS1seq\" placeholder=\"NS1 sequence\" cols=\"70\" rows=\"5\"></textarea></div></td></tr>\n"
				+ "<tr><td class=\"leftcolumn\"><h3>NS2</h3></td><td><div class=\"8u\"><textarea name=\"NS2seq\" placeholder=\"NS2 sequence\" cols=\"70\" rows=\"5\"></textarea></div></td></tr>\n"
				+ "<tr><td class=\"leftcolumn\"><h3>PA</h3></td><td><div class=\"8u\"><textarea name=\"PAseq\" placeholder=\"PA sequence\" cols=\"70\" rows=\"5\"></textarea></div></td></tr>\n"
				+ "<tr><td class=\"leftcolumn\"><h3>PB1</h3></td><td><div class=\"8u\"><textarea name=\"PB1seq\" placeholder=\"PB1 sequence\" cols=\"70\" rows=\"5\"></textarea></div></td></tr>\n"
				+ "<tr><td class=\"leftcolumn\"><h3>PB1-F2</h3></td><td><div class=\"8u\"><textarea name=\"PB1F2seq\" placeholder=\"PB1-F2 sequence\" cols=\"70\" rows=\"5\"></textarea></div></td></tr>\n"
				+ "<tr><td class=\"leftcolumn\"><h3>PB2</h3></td><td><div class=\"8u\"><textarea name=\"PB2seq\" placeholder=\"PB2 sequence\" cols=\"70\" rows=\"5\"></textarea></div></td></tr>\n"
				+ "<tr><td class=\"leftcolumn\"></td><td><br><input type=\"checkbox\" name=\"includeProteins\" value=\"yes\" checked><strong>Include predictions for individual protein tropism</strong></td></tr>\n"
				+ "<tr><td class=\"leftcolumn\"></td><td><br><input type=\"reset\" class=\"button\" value=\"Clear\">\n"
				+ "<input type=\"submit\" class=\"button\"></td></tr>\n"
				+ "</table>\n" + "</form>\n" + "</p>");

		out.println("</section>\n" + "</div>\n" + "</div>\n" + "</div>\n"
				+ "</div>\n");
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
				+ "<h2>Strain predictor</h2>\n"
				+ "<p>Host tropism predictor of influenza virus strains. The predictor utilizes combined protein sequence information from influenza proteins to predict host tropism of an influenza virus strain.</p>\n"
				+ "<a href=\"tutorial.html\" class=\"button\">Tutorial</a>\n"
				+ "<a href=\"strain-example\" class=\"button\">Example</a></p>\n"
                + "<p><strong>Please input protein sequence for prediction in the text box below. While sequences for all proteins are not compulsory, fewer input would result in a less accurate prediction.</p>\n"
                + "<p><strong>Protein sequence must have a minimum length of 5 amino acids.</strong></p>\n"
                + "<p><strong>Only sequences containing 20 standard amino acids are allowed, in one-letter amino acid code as follows:<br></strong>\n"
                + "<strong>A C D E F G H I K L M N P Q R S T V W Y</strong></p>\n");
		}

}
