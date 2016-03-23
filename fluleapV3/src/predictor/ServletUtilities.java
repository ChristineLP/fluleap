package predictor;

import javax.servlet.http.*;

/** Some simple time savers. Static methods. */

public class ServletUtilities {
	public static String headWithTitle(String title) {
		return ("<!DOCTYPE html>\n" + "<html>\n" + "<head><title>" + title + "</title></head>\n");
	}

	public static String getHead() {
		return ("<!DOCTYPE HTML>\n"
				+ "<!--\n"
				+ "Minimaxing 3.1 by HTML5 UP\n"
				+ "html5up.net | @n33co\n"
				+ "Free for personal and commercial use under the CCA 3.0 license (html5up.net/license)\n"
				+ "-->\n"
				+ "<html>\n"
				+ "<head>\n"
				+ "<title>FluLeap: Prediction System for Host Tropism of Influenza Virus</title>\n"
				+ "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />\n"
				+ "<meta name=\"description\" content=\"\" />\n"
				+ "<meta name=\"keywords\" content=\"\" />\n"
				+ "<link href=\"http://fonts.googleapis.com/css?family=Ubuntu+Condensed\" rel=\"stylesheet\">\n"
				+ "<link rel=\"stylesheet\" href=\"css/skel-noscript.css\" />\n"
				+ "<link rel=\"stylesheet\" href=\"css/style.css\" />\n"
				+ "<link rel=\"stylesheet\" href=\"css/style-desktop.css\" />\n"
				+ "<script src=\"js/jquery.min.js\"></script>\n"
				+ "<script src=\"js/config.js\"></script>\n"
				+ "<script src=\"js/skel.min.js\"></script>\n"
				+ "<script src=\"js/skel-panels.min.js\"></script>\n"
				+ "<!--[if lte IE 9]><link rel=\"stylesheet\" href=\"css/ie9.css\" /><![endif]-->\n"
				+ "<!--[if lte IE 8]><script src=\"js/html5shiv.js\"></script><![endif]-->\n"
				+ "</head>\n");
	}

	public static String getBanner() {
		return ("<body>\n"
				+ "<!-- ********************************************************* -->\n"
				+ "<div id=\"header-wrapper\">\n"
				+ "<div class=\"container\">\n" + "<div class=\"row\">\n"
				+ "<div class=\"12u\">\n" + "<header id=\"header\">\n"
				+ "<h1><a href=\"index.html\" id=\"logo\">FluLeap</a></h1>\n"
				+ "<nav id=\"nav\">\n"
				+ "<a href=\"multiple-proteins\">Protein predictor</a>\n"
				+ "<a href=\"strain\">Strain predictor</a>\n"
				+ "<a href=\"about.html\">About</a>\n"
				+ "<a href=\"data.html\">Data</a>\n"
				+ "<a href=\"contact\">Contact</a>\n" + "</nav>\n"
				+ "</header>\n" + "</div>\n" + "</div>\n" + "</div>\n"
				+ "</div>\n");

	}

	public static String getFooter() {
		return ("<div id=\"footer-wrapper\">\n"
				+ "<div class=\"container\">\n"
				+ "<div class=\"row\">\n"
				+ "<div class=\"8u\">\n"
				+ "<section>\n"
				+ "<h2>Useful influenza resources</h2>\n"
				+ "<div>\n"
				+ "<div class=\"row\">\n"
				+ "<div class=\"3u\">\n"
				+ "<ul class=\"link-list\">\n"
				+ "<li><a href=\"http://www.ncbi.nlm.nih.gov/genomes/FLU/FLU.html\">NCBI Influenza Virus Resource</a></li>\n"
				+ "</ul>\n"
				+ "</div>\n"
				+ "<div class=\"3u\">\n"
				+ "<ul class=\"link-list\">\n"
				+ "<li><a href=\"http://www.fludb.org/brc/home.spg?decorator=influenza\">Influenza Research Database</a></li>\n"
				+ "</ul>\n"
				+ "</div>\n"
				+ "<div class=\"3u\">\n"
				+ "<ul class=\"link-list\">\n"
				+ "<li><a href=\"http://www.who.int/influenza/en/\">WHO Influenza</a></li>\n"
				+ "</ul>\n"
				+ "</div>\n"
				+ "<div class=\"3u\">\n"
				+ "<ul class=\"link-list\">\n"
				+ "<li><a href=\"http://fluresearch.org/\">NIAID Influenza Research Collaboration</a></li>\n"
				+ "</ul>\n"
				+ "</div>\n"
				+ "</div>\n"
				+ "</div>\n"
				+ "</section>\n"
				+ "</div>\n"
				+ "<div class=\"4u\">\n"
				+ "<section>\n"
				+ "<h2>About us</h2>\n"
				+ "<p>This website is developed by the research team at Bioinformatics Center, Department of Biochemistry, National University of Singapore.</p>\n"
				+ "<footer class=\"controls\">\n"
				+ "<a href=\"http://www.bic.nus.edu.sg/\" class=\"button\">More about us...</a>\n"
				+ "</footer>\n"
				+ "</section>\n"
				+ "</div>\n"
				+ "</div>\n"
				+ "<div class=\"row\">\n"
				+ "<div class=\"12u\">\n"
				+ "<div id=\"copyright\">\n"
				+ "&copy; NUS Bioinformatics Center. All rights reserved.\n"
				+ "</div>\n"
				+ "</div>\n"
				+ "</div>\n"
				+ "</div>\n"
				+ "</div>\n"
				+ "<!-- ********************************************************* -->\n"
				+ "</body>\n" + "</html>\n");
	}

	/**
	 * Read a parameter with the specified name, convert it to an int, and
	 * return it. Return the designated default value if the parameter doesn't
	 * exist or if it is an illegal integer format.
	 */

	public static int getIntParameter(HttpServletRequest request,
			String paramName, int defaultValue) {
		String paramString = request.getParameter(paramName);
		int paramValue;
		try {
			paramValue = Integer.parseInt(paramString);
		} catch (Exception nfe) { // null or bad format
			paramValue = defaultValue;
		}
		return (paramValue);
	}

	/** Reads param and converts to double. Default if problem. */

	public static double getDoubleParameter(HttpServletRequest request,
			String paramName, double defaultValue) {
		String paramString = request.getParameter(paramName);
		double paramValue;
		try {
			paramValue = Double.parseDouble(paramString);
		} catch (Exception nfe) { // null or bad format
			paramValue = defaultValue;
		}
		return (paramValue);
	}

/** Replaces characters that have special HTML meanings
   *  with their corresponding HTML character entities.
   *  Specifically, given a string, this method replaces all 
   *  occurrences of  
   *  {@literal
   *  '<' with '&lt;', all occurrences of '>' with
   *  '&gt;', and (to handle cases that occur inside attribute
   *  values), all occurrences of double quotes with
   *  '&quot;' and all occurrences of '&' with '&amp;'.
   *  Without such filtering, an arbitrary string
   *  could not safely be inserted in a Web page.
   *  }
   */

	public static String filter(String input) {
		if (!hasSpecialChars(input)) {
			return (input);
		}
		StringBuilder filtered = new StringBuilder(input.length());
		char c;
		for (int i = 0; i < input.length(); i++) {
			c = input.charAt(i);
			switch (c) {
			case '<':
				filtered.append("&lt;");
				break;
			case '>':
				filtered.append("&gt;");
				break;
			case '"':
				filtered.append("&quot;");
				break;
			case '&':
				filtered.append("&amp;");
				break;
			default:
				filtered.append(c);
			}
		}
		return (filtered.toString());
	}
	
	/**
	 * Trims sequence to fit table in web browser
	 * 
	 * @param sequence
	 * @return
	 */
	public static String wrapText(String sequence) {
		String trimmedSeq = "";
		if (sequence == null)
			return "No input";
		else if (sequence.length() <= 70)
			return sequence;
		else {
			// sequence longer than 70 characters
			// trim sequence by wrapping text
			
			// first add first line into new string
			trimmedSeq += sequence.substring(0, 70) + "\r\n";
			
			// next wrap text every 70 characters
			int counter = 0;
			for (int i = 70; i < sequence.length(); i++) {
				counter++;
				trimmedSeq += sequence.charAt(i);
				if (counter == 70) {
					// add new line char and reset counter
					trimmedSeq += "\r\n";
					counter = 0;
				}
			}

			return trimmedSeq;
		}
	}
	
	/**
	 * Generate unique id for each session from ip address and time
	 * 
	 * @param ipAddr
	 * @param timeInMillis
	 * @return id
	 */
	static long generateID(String ipAddr, long timeInMillis) {
		long id = 1;
		
		// convert ip address to long integer
		long ipNum = Long.parseLong(ipAddr.replaceAll(":", ""));
		id = id * 3 + ipNum;
		id = id * 7 + timeInMillis;
		
		return id;
	}

	private static boolean hasSpecialChars(String input) {
		boolean flag = false;
		if ((input != null) && (input.length() > 0)) {
			char c;
			for (int i = 0; i < input.length(); i++) {
				c = input.charAt(i);
				switch (c) {
				case '<':
					flag = true;
					break;
				case '>':
					flag = true;
					break;
				case '"':
					flag = true;
					break;
				case '&':
					flag = true;
					break;
				}
			}
		}
		return (flag);
	}

	private ServletUtilities() {
	} // Uninstantiatable class
}
