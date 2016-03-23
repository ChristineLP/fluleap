package predictor;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ZoonoticResults
 */
@WebServlet("/zoonotic-results")
public class ZoonoticResults extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ZoonoticResults() {
		super();
		// TODO Auto-generated constructor stub
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
		// TODO Auto-generated method stub
		System.out.println("--ZoonoticResults--");
		// get ip address and time
		String ip = request.getRemoteAddr();
		long time = Calendar.getInstance().getTimeInMillis(); 
		// returns the number of milliseconds since January 1, 1970 represented by the Date object
		System.out.println(ip + " @ " + Calendar.getInstance().getTime());

		String strainName = request.getParameter("strainName");
		System.out.println(strainName);
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println(ServletUtilities.getHead());
		out.println(ServletUtilities.getBanner());

		out.println(getBodyContent());

		/**
		 * PREDICTION HERE
		 */
		ZoonoticPredictor zp = new ZoonoticPredictor(ip, time, strainName);

		// get feature vectors
		int[] featuresArr = retrieveFeatures(request);
		// get model streams
		ArrayList<InputStream> streamArr = getModelStreams();
		zp.predict(featuresArr, streamArr);
		
		int prClass = zp.getPredictionClass();
		double[] prDistArr = zp.getPredictionDistribution(true);

		System.out.println("===Final consolidated results===");
		BigDecimal aDist = new BigDecimal(prDistArr[0]);
		aDist = aDist.setScale(3, RoundingMode.HALF_UP);
		BigDecimal hDist = new BigDecimal(prDistArr[1]);
		hDist = hDist.setScale(3, RoundingMode.HALF_UP);
		BigDecimal zDist = new BigDecimal(prDistArr[2]);
		zDist = zDist.setScale(3, RoundingMode.HALF_UP);
		System.out.println(prClass + ": " + aDist + " " + hDist + " " + zDist);
		
		/**
		 * PREDICTION END
		 */
		
		out.println("<h3>Zoonotic prediction for strain</h3>\n");

		if (strainName != null)
			out.println("<h3>" + strainName + "</h3>");
		else
			out.println("<br>\n");
		
		out.println("<p><strong>Prediction: </strong>");
		if (prClass == 0)
			out.println("<predictiona>AVIAN</predictiona><br>\n");
		else if (prClass == 1)
			out.println("<predictionh>HUMAN</predictionh><br>\n");
		else
			out.println("<predictionz>ZOONOTIC</predictionz><br>\n");
		
		out.println("<strong>Estimated zoonotic risk: </strong>");
		if (prDistArr[2] < 0.5)
			out.println("<strong>" + zDist + "</strong><br></p>\n");
		else
			out.println("<warning>" + zDist + "</warning><br></p>\n");
		
		// print signature
		out.println("<br>\n<h3>Host tropism protein signature</h3>\n");
		out.println("<p>\n<table>\n" + "<tr>" + "<td class =\""
				+ PredictorUtilities.getSigStyle(featuresArr[0]) + "\">1</td>"
				+ "<td class =\""
				+ PredictorUtilities.getSigStyle(featuresArr[1]) + "\">2</td>"
				+ "<td class =\""
				+ PredictorUtilities.getSigStyle(featuresArr[2]) + "\">3</td>"
				+ "<td class =\""
				+ PredictorUtilities.getSigStyle(featuresArr[3]) + "\">4</td>"
				+ "<td class =\""
				+ PredictorUtilities.getSigStyle(featuresArr[4]) + "\">5</td>"
				+ "<td class =\""
				+ PredictorUtilities.getSigStyle(featuresArr[5]) + "\">6</td>"
				+ "<td class =\""
				+ PredictorUtilities.getSigStyle(featuresArr[6]) + "\">7</td>"
				+ "<td class =\""
				+ PredictorUtilities.getSigStyle(featuresArr[7]) + "\">8</td>"
				+ "<td class =\""
				+ PredictorUtilities.getSigStyle(featuresArr[8]) + "\">9</td>"
				+ "<td class =\""
				+ PredictorUtilities.getSigStyle(featuresArr[9]) + "\">10</td>"
				+ "<td class =\""
				+ PredictorUtilities.getSigStyle(featuresArr[10])
				+ "\">11</td>" + "</tr>\n");

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
				+ "<td class =\"sigcolumn\">PB2</td>" + "</tr></table>\n</p>");
		
		out.println("<br>\n<h3>Estimated probability distribution</h3>\n");
		out.println("<p><strong>Avian: " + aDist + "</strong><br>\n");
		out.println("<strong>Human: " + hDist + "</strong><br>\n");
		out.println("<strong>Zoonotic: " + zDist + "</strong><br>\n</p>");
		
		out.println("</section>\n" + "</div>\n" + "</div>\n" + "</div>\n"
				+ "</div>\n");
		out.println(ServletUtilities.getFooter());
	}

	private String getBodyContent() {
		return ("<div id=\"main\">\n" + "<div class=\"container\">\n"
				+ "<div class=\"row main-row\">\n" + "<div class=\"12u\">\n"
				+ "<section>\n" + "<h2>Strain prediction results</h2>\n");
	}

	private int[] retrieveFeatures(HttpServletRequest request) {
		int[] featuresArr = new int[12];

		// get host tropism predictions of 11 proteins as signature
		// final feature is total number of human proteins in signature
		int totalHuman = 0;
		for (int i = 0; i < 11; i++) {
			// get parameter name
			String paramName = PredictorUtilities.getModelName(i + 1) + "sig";

			try {
				// retrieve parameter value from Http request
				int sig = Integer.parseInt(request.getParameter(paramName));
				if (sig == 0 || sig == 1) {
					featuresArr[i] = sig;
					totalHuman += sig;
				} else
					throw new NumberFormatException();
			} catch (NumberFormatException nfe) {
				// if no integer, assign invalid value to signature
				featuresArr[i] = -1;
			}
		}
		featuresArr[11] = totalHuman;

		for (int i = 0; i < featuresArr.length; i++) {
			System.out.print(featuresArr[i] + " ");
		}
		System.out.println();

		return featuresArr;
	}

	/**
	 * Returns all 5 prediction models as input stream in an array
	 * 
	 * @return
	 */
	private ArrayList<InputStream> getModelStreams() {
		ArrayList<InputStream> streamArr = new ArrayList<InputStream>(5);

		// add model streams into array
		streamArr.add(
				0,
				getServletContext().getResourceAsStream(
						"/WEB-INF/models/ANNfinal.model"));
		streamArr.add(
				1,
				getServletContext().getResourceAsStream(
						"/WEB-INF/models/IBkfinal.model"));
		streamArr.add(
				2,
				getServletContext().getResourceAsStream(
						"/WEB-INF/models/NaiveBayesfinal.model"));
		streamArr.add(
				3,
				getServletContext().getResourceAsStream(
						"/WEB-INF/models/RFfinal.model"));
		streamArr.add(
				4,
				getServletContext().getResourceAsStream(
						"/WEB-INF/models/SMOfinal.model"));

		return streamArr;
	}

}
