package predictor;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class StrainsResults
 */
@WebServlet("/strains-results")
public class StrainsResults extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public StrainsResults() {
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
		// print client address and time
		System.out.println("--StrainsResults--");
		// get ip address and time
		String ip = request.getRemoteAddr();
		long time = Calendar.getInstance().getTimeInMillis(); 
		// returns the number of milliseconds since January 1, 1970 represented by the Date object
		System.out.println(ip + " @ " + Calendar.getInstance().getTime());
		
		String strainName = request.getParameter("strainName");
		System.out.println(strainName);
		
		// first check parameters
		// there must be at least 1 value entered
		boolean isComplete = true;
		String errorData = "";

		// retrieve sequences from http request
		String[] seqArr = retrieveSequences(request);
		// retrieveSequences returns null which means sequence contains invalid
		// character
		if (seqArr == null) {
			isComplete = false;
			errorData = "seqArr=invalid";
		} else if (PredictorUtilities.isEmpty(seqArr)) {
			// checks if array is empty
			isComplete = false;
			errorData = "seqArr=null";
		}

		// only proceed to send response if parameter values are not empty
		if (isComplete) {
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println(ServletUtilities.getHead());
			out.println(ServletUtilities.getBanner());

			out.println(getBodyContent());
			
			// NEED THIS TO WORK ON TOMCAT SERVER
			// SinglePredictor sp = new SinglePredictor(getServletContext()
			// .getRealPath("/"));

			// Test revert back to original sp for local server
			HostTropismPredictor htp = new HostTropismPredictor(ip, time, strainName);

			// get model stream for strain
			InputStream strainStream = getServletContext().getResourceAsStream(
					"/WEB-INF/models/IRD20140211ALLfinal.model");

			int strainPrediction = htp.predict(seqArr, strainStream);

			out.println("<h3>Host tropism prediction for strain</h3>\n");
			out.println("<h3>" + strainName + "</h3>");

			out.println("<p><strong>Prediction: </strong>");

			if (strainPrediction == 0)
				out.println("<predictiona>AVIAN</predictiona><br>\n");
			else
				out.println("<predictionh>HUMAN</predictionh><br>\n");

			out.println("<strong>Accuracy: " + PredictorUtilities.getACC(0)
					+ "%<sup>1</sup></strong></p>");

			// retrieve models as input streams for individual protein
			// predictors
			ArrayList<InputStream> streamArr = getModelStreams(seqArr);
			// prediction results in an array
			int[] predictionsArr = htp.predict(seqArr, streamArr);

			out.println("<br>\n<h3>Host tropism protein signature</h3>\n");

			// print signature
			out.println("<p>\n<table>\n" + "<tr>" + "<td class =\""
					+ PredictorUtilities.getSigStyle(predictionsArr[0]) + "\">1</td>"
					+ "<td class =\"" + PredictorUtilities.getSigStyle(predictionsArr[1])
					+ "\">2</td>" + "<td class =\""
					+ PredictorUtilities.getSigStyle(predictionsArr[2]) + "\">3</td>"
					+ "<td class =\"" + PredictorUtilities.getSigStyle(predictionsArr[3])
					+ "\">4</td>" + "<td class =\""
					+ PredictorUtilities.getSigStyle(predictionsArr[4]) + "\">5</td>"
					+ "<td class =\"" + PredictorUtilities.getSigStyle(predictionsArr[5])
					+ "\">6</td>" + "<td class =\""
					+ PredictorUtilities.getSigStyle(predictionsArr[6]) + "\">7</td>"
					+ "<td class =\"" + PredictorUtilities.getSigStyle(predictionsArr[7])
					+ "\">8</td>" + "<td class =\""
					+ PredictorUtilities.getSigStyle(predictionsArr[8]) + "\">9</td>"
					+ "<td class =\"" + PredictorUtilities.getSigStyle(predictionsArr[9])
					+ "\">10</td>" + "<td class =\""
					+ PredictorUtilities.getSigStyle(predictionsArr[10]) + "\">11</td>"
					+ "</tr>\n");

			// print labels
			out.println("<tr>" + "<td class =\"sigcolumn\">HA</td>"
					+ "<td class =\"sigcolumn\">M1</td>"
					+ "<td class =\"sigcolumn\">M2</td>"
					+ "<td class =\"sigcolumn\">NA</td>"
					+ "<td class =\"sigcolumn\">NP</td>"
					+ "<td class =\"sigcolumn\">NS1</td>"
					+ "<td class =\"sigcolumn\">NS2</td>"
					+ "<td class =\"sigcolumn\">PA</td>"
					+ "<td class =\"sigcolumn\">PB1</td>"
					+ "<td class =\"sigcolumn\">PB1-F2</td>"
					+ "<td class =\"sigcolumn\">PB2</td>"
					+ "</tr></table>\n</p>");
			
			// only display button for zoonotic prediction if signature is complete
			if (PredictorUtilities.hasCompleteSig(predictionsArr)) {
			// button to submit for zoonotic prediction
			out.println("<br><p>\n"
					+ "<form action=\"zoonotic-results\" method=\"post\">\n"
					+ "<input type=\"hidden\" name=\"HAsig\" value=\"" + predictionsArr[0] + "\">\n"
					+ "<input type=\"hidden\" name=\"M1sig\" value=\"" + predictionsArr[1] + "\">\n"
					+ "<input type=\"hidden\" name=\"M2sig\" value=\"" + predictionsArr[2] + "\">\n"
					+ "<input type=\"hidden\" name=\"NAsig\" value=\"" + predictionsArr[3] + "\">\n"
					+ "<input type=\"hidden\" name=\"NPsig\" value=\"" + predictionsArr[4] + "\">\n"
					+ "<input type=\"hidden\" name=\"NS1sig\" value=\"" + predictionsArr[5] + "\">\n"
					+ "<input type=\"hidden\" name=\"NS2sig\" value=\"" + predictionsArr[6] + "\">\n"
					+ "<input type=\"hidden\" name=\"PAsig\" value=\"" + predictionsArr[7] + "\">\n"
					+ "<input type=\"hidden\" name=\"PB1sig\" value=\"" + predictionsArr[8] + "\">\n"
					+ "<input type=\"hidden\" name=\"PB1F2sig\" value=\"" + predictionsArr[9] + "\">\n"
					+ "<input type=\"hidden\" name=\"PB2sig\" value=\"" + predictionsArr[10] + "\">\n"
					+ "<input type =\"hidden\" name=\"strainName\" value=\"" + strainName + "\">\n"
					+ "<input type=\"submit\" class=\"button\" value=\"zoonotic prediction\"></td></tr>\n"
					+ "</form>\n" + "</p>");
			}
			
			out.println("<br>\n<p>Based on the following protein sequences:</p>");

			try {
				// if includeProteins box is checked
				// predict host tropism for individual proteins and print
				// results
				if (request.getParameter("includeProteins").equals("yes")) {

					out.println("<p>\n<table>\n"
							+ "<tr><th class=\"leftcolumn\"><h3>Protein</h3></th>"
							+ "<th class=\"centercolumn\"><h3>Sequence</h3></th>"
							+ "<th class=\"rightcolumn1\"><h3>Prediction</h3></th>"
							+ "<th class=\"rightcolumn2\"><h3>Accuracy</h3></th></tr>");
					for (int i = 0; i < predictionsArr.length; i++) {
						out.print("<tr><td class=\"leftcolumn\"><h3>"
								+ PredictorUtilities.getModelName(i + 1)
								+ "</td>");
						out.print("<td class=\"centercolumn\">"
								+ ServletUtilities.wrapText(seqArr[i])
								+ "</td>");
						if (predictionsArr[i] == 0) {
							out.println("<td class=\"rightcolumn1\"><predictiona>Avian</predictiona></td>"
									+ "<td class=\"rightcolumn2\">"
									+ PredictorUtilities.getACC(i + 1)
									+ "%<sup>1</sup></td></tr>");
						} else if (predictionsArr[i] == 1) {
							out.println("<td class=\"rightcolumn1\"><predictionh>Human*</predictionh></td>"
									+ "<td class=\"rightcolumn2\">"
									+ PredictorUtilities.getACC(i + 1)
									+ "%<sup>1</sup></td></tr>");
						} else {
							out.println("<td class=\"rightcolumn1\"></td>"
									+ "<td class=\"rightcolumn2\"></td></tr>");
						}
					}
					out.println("</table>\n</p>");
				}
			} catch (NullPointerException npe) {
				// if includeProteins checkbox not checked, null pointer
				// exception
				// will be thrown
				// print sequences input by user
				out.println("<p>\n<table>\n"
						+ "<tr><th class=\"leftcolumn\"><h3>Protein</h3></th>"
						+ "<th class=\"centercolumn\"><h3>Sequence</h3></th>");
				for (int i = 0; i < seqArr.length; i++) {
					out.print("<tr><td class=\"leftcolumn\"><h3>"
							+ PredictorUtilities.getModelName(i + 1) + "</td>");
					out.print("<td class=\"centercolumn\">"
							+ ServletUtilities.wrapText(seqArr[i])
							+ "</td></tr>");
				}
				out.println("</table>\n</p>");
			}

			out.println("<br>\n"
					+ "<p><sup>1</sup><strong>Based on 10-fold cross-validation training performance of the prediction model.<br>\n"
					+ "For more information on the training of prediction models, please click <a href=\"supporting-information.html\" target=\"_blank\">here</a>.<br>\n"
					+ "*If origin of the sequence was avian, and the host tropism prediction is </strong><predictionh>human</predictionh><strong>, the protein might have adapted to human host."
					+ "<br>Please read further interpretation of results <a href=\"tutorial.html\" target=\"_blank\">here</a>.</strong></p>");
			out.println("<input type=\"button\" onclick=\"history.back();\" class=\"button\" value=\"Back\">");
			out.println("<a href=\"strain\" class=\"button\">New prediction</a></p>\n");
			out.println("</section>\n" + "</div>\n" + "</div>\n" + "</div>\n"
					+ "</div>\n");
			out.println(ServletUtilities.getFooter());
		} else {
			response.sendRedirect("strain?" + errorData);
		}
	}

	/**
	 * Retrieves sequences input by user and returns in array
	 * 
	 * @param request
	 * @return sequence array
	 */
	private String[] retrieveSequences(HttpServletRequest request) {
		String[] seqArr = new String[11];

		for (int i = 0; i < 11; i++) {
			// get parameter name
			String paramName = PredictorUtilities.getModelName(i + 1) + "seq";

			// retrieve parameter value from Http request
			String seq = request.getParameter(paramName);
			// if no parameter value, assign null to seqArr
			if (seq == null || seq.length() == 0)
				seqArr[i] = null;
			else if (seq.length() < 5)
				return null; // return null if sequence length is less than 5
			else {
				seq = PredictorUtilities.trimSequence(seq);

				// check if sequence contains all valid chars
				if (seq.equals("invalid character"))
					return null; // return null if sequence is invalid
				else
					seqArr[i] = new String(seq);
			}
		}

		return seqArr;
	}

	/**
	 * Returns an array of prediction models as input stream If no sequence
	 * input for a model, corresponding array index is null
	 * 
	 * @param sequence
	 *            array
	 * @return input stream array
	 */
	private ArrayList<InputStream> getModelStreams(String[] seqArr) {
		ArrayList<InputStream> streamArr = new ArrayList<InputStream>(11);

		for (int i = 0; i < seqArr.length; i++) {
			if (seqArr[i] != null) {
				// concatenate path for model
				String path = "/WEB-INF/models/IRD20140212"
						+ PredictorUtilities.getModelName(i + 1)
						+ "final.model";
				// System.out.println(path);

				streamArr.add(i, getServletContext().getResourceAsStream(path));
			} else
				streamArr.add(i, null);
		}

		return streamArr;
	}

	private String getBodyContent() {
		return ("<div id=\"main\">\n" + "<div class=\"container\">\n"
				+ "<div class=\"row main-row\">\n" + "<div class=\"12u\">\n"
				+ "<section>\n" + "<h2>Strain prediction results</h2>\n");
	}

}
