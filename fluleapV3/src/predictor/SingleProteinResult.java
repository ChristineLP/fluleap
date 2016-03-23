package predictor;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SingleProteinResult
 */
@WebServlet("/single-protein-result")
public class SingleProteinResult extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SingleProteinResult() {
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
		// get ip address and time
		String ip = request.getRemoteAddr();
		long time = Calendar.getInstance().getTimeInMillis();
		// returns the number of milliseconds since January 1, 1970 represented
		// by the Date object
		System.out.println(ip + " @ " + Calendar.getInstance().getTime());

		String strainName = request.getParameter("strainName");
		System.out.println(strainName);

		boolean isComplete = true;
		String errorData = "";

		// first check parameters
		String model = request.getParameter("selectedProtein");
		System.out.println(model);
		String sequence = request.getParameter("seq");

		// check for missing form data
		if (sequence == null || sequence.trim().equals("")) {
			isComplete = false;
			errorData = "seq=null";
		}
		// remove new line characters and white spaces from sequence
		sequence = PredictorUtilities.trimSequence(sequence);
		System.out.println(sequence);
		// check if all peptide characters are valid
		if (sequence.equals("invalid character")) {
			isComplete = false;
			errorData = "seq=invalid";
		}

		// if parameter values are complete, continue sending html response
		if (isComplete) {
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println(ServletUtilities.getHead());
			out.println(ServletUtilities.getBanner());

			out.println(getBodyContent());

			//HostTropismPredictor sp = new HostTropismPredictor(getServletContext().getRealPath("/"));
			HostTropismPredictor htp = new HostTropismPredictor(ip, time, strainName);

			// retrieve model as input stream for predictor
			InputStream modelStream = getModelStream(model);
			int prediction = htp.predict(sequence, model, modelStream);
			System.out.println(prediction);

			out.println("<h3>Host tropism prediction for " + model + " </h3>\n"
					+ "<p>Sequence: <br>\n" + "<strong>" + sequence
					+ "</strong></p>\n" + "<p>Prediction: <h4>");

			if (prediction == 0)
				out.println("AVIAN</h4></p>\n");
			else
				out.println("HUMAN</h4></p>\n");

			out.println("</section>\n" + "</div>\n" + "</div>\n" + "</div>\n"
					+ "</div>\n");
			out.println(ServletUtilities.getFooter());
		} else {
			response.sendRedirect("single-protein?" + errorData);
		}

	}

	/**
	 * Returns prediction model as input stream
	 * 
	 * @param model
	 * @return input stream for model
	 */
	private InputStream getModelStream(String model) {
		// remove - from PB1F2
		if (model.equals("PB1-F2"))
			model = "PB1F2";

		// concatenate path for model
		String path = "/WEB-INF/models/IRD20140212" + model + "final.model";
		System.out.println(path);

		return (getServletContext().getResourceAsStream(path));
	}

	private String getBodyContent() {
		return ("<div id=\"main\">\n" + "<div class=\"container\">\n"
				+ "<div class=\"row main-row\">\n" + "<div class=\"12u\">\n"
				+ "<section>\n"
				+ "<h2>Single protein prediction results</h2>\n");
	}
}
