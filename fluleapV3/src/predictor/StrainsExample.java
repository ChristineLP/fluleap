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
@WebServlet("/strain-example")
public class StrainsExample extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public StrainsExample() {
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
				out.println("<p><warning>! Sequence contains invalid characters.\n"
						+ "Please input only valid characters from 20 standard amino acids.</warning></p>");
			}
		} catch (NullPointerException npe) {
			// do nothing if null pointer exception is caught
			// to prevent exception generated if no seqArr parameter
		}

		int type;
		try {
			type = Integer.parseInt(request.getParameter("seqType"));
		} catch (NullPointerException npe) {
			// if no seqType parameter given, set default to 2
			type = 2;
		} catch (NumberFormatException nfe) {
			// if seqType parameter is not an integer or out of range of 1 - 4, set default to 2
			type = 2;
		}
		
		// get example sequence array
		String[] sampleArr = PredictorUtilities.getExampleSequences(type);
		
		out.println("<p>\n"
				+ "<form action=\"strains-results\" method=\"post\">\n"
				+ "<table>\n"
				+ "<tr><td class=\"leftcolumn\"><h4>Strain name</h4></td><td><div class=\"6u\"><input type=\"text\" class=\"text\" name=\"strainName\" value=\"" + sampleArr[0] + "\"><br></div></td></tr>\n"
				+ "<tr><td class=\"leftcolumn\"><h3>HA</h3></td><td><div class=\"8u\"><textarea name=\"HAseq\" cols=\"70\" rows=\"5\">" + sampleArr[1] + "</textarea></div></td></tr>\n"
				+ "<tr><td class=\"leftcolumn\"><h3>M1</h3></td><td><div class=\"8u\"><textarea name=\"M1seq\" cols=\"70\" rows=\"5\">" + sampleArr[2] + "</textarea></div></td></tr>\n"
				+ "<tr><td class=\"leftcolumn\"><h3>M2</h3></td><td><div class=\"8u\"><textarea name=\"M2seq\" cols=\"70\" rows=\"5\">" + sampleArr[3] + "</textarea></div></td></tr>\n"
				+ "<tr><td class=\"leftcolumn\"><h3>NA</h3></td><td><div class=\"8u\"><textarea name=\"NAseq\" cols=\"70\" rows=\"5\">" + sampleArr[4] + "</textarea></div></td></tr>\n"
				+ "<tr><td class=\"leftcolumn\"><h3>NP</h3></td><td><div class=\"8u\"><textarea name=\"NPseq\" cols=\"70\" rows=\"5\">" + sampleArr[5] + "</textarea></div></td></tr>\n"
				+ "<tr><td class=\"leftcolumn\"><h3>NS1</h3></td><td><div class=\"8u\"><textarea name=\"NS1seq\" cols=\"70\" rows=\"5\">" + sampleArr[6] + "</textarea></div></td></tr>\n"
				+ "<tr><td class=\"leftcolumn\"><h3>NS2</h3></td><td><div class=\"8u\"><textarea name=\"NS2seq\" cols=\"70\" rows=\"5\">" + sampleArr[7] + "</textarea></div></td></tr>\n"
				+ "<tr><td class=\"leftcolumn\"><h3>PA</h3></td><td><div class=\"8u\"><textarea name=\"PAseq\" cols=\"70\" rows=\"5\">" + sampleArr[8] + "</textarea></div></td></tr>\n"
				+ "<tr><td class=\"leftcolumn\"><h3>PB1</h3></td><td><div class=\"8u\"><textarea name=\"PB1seq\" cols=\"70\" rows=\"5\">" + sampleArr[9] + "</textarea></div></td></tr>\n"
				+ "<tr><td class=\"leftcolumn\"><h3>PB1-F2</h3></td><td><div class=\"8u\"><textarea name=\"PB1F2seq\" cols=\"70\" rows=\"5\">" + sampleArr[10] + "</textarea></div></td></tr>\n"
				+ "<tr><td class=\"leftcolumn\"><h3>PB2</h3></td><td><div class=\"8u\"><textarea name=\"PB2seq\" cols=\"70\" rows=\"5\">" + sampleArr[11] + "</textarea></div></td></tr>\n"
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
				+ "<a href=\"tutorial.html\" class=\"button\">Tutorial</a></p>\n"
				+ "<p>Prediction for the virus strain as well as predictions for individual proteins would be made.</p>\n"
				+ "<p><strong>Click on submit button at the end of the form to run the prediction.</strong></p>");
	}

}
