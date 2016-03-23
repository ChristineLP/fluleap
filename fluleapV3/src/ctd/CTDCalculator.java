package ctd;
import java.math.BigDecimal;
import java.math.MathContext;

public class CTDCalculator {

	CCalculator cc;
	TCalculator tc;
	DCalculator dc;
	String sequence;
	char[] peptideArr;
	int length;
	double[] hydro;
	double[] polariz;
	double[] polar;
	double[] vdw;
	double[] charge;
	double[] solvent;
	double[] structure;
	double[] comp;
	double[] ctd;

	public CTDCalculator() {

	}

	public double[] getCTD(String peptide) {
		sequence = peptide;
		initializeArray();

		cc = new CCalculator(peptideArr);
		tc = new TCalculator(peptideArr);
		dc = new DCalculator(peptideArr);

		hydro = new double[21];
		polariz = new double[21];
		polar = new double[21];
		vdw = new double[21];
		charge = new double[21];
		solvent = new double[21];
		//structure = new double[21];
		comp = new double[20];
		ctd = new double[146];

		/*
		 * int property: 1 = hydrophobicity 2 = polarizibility 3 = polarity 4 =
		 * van der waal's volume 5 = charge 6 = solvent accessibility 7 =
		 * secondary structure
		 */
		findHydro();
		findPolariz();
		findPolar();
		findVDW();
		findCharge();
		findSolvent();
		//findStructure();
		findComp();

		combineCTD();

		return ctd;
	}

	void initializeArray() {
		peptideArr = sequence.toCharArray();

		length = peptideArr.length;
	}

	void findHydro() {
		double[] c = cc.getVectors(1);
		double[] t = tc.getVectors(1);
		double[] d = dc.getVectors(1);

		for (int i = 0; i < hydro.length; i++) {
			if (i < 3)
				hydro[i] = c[i];
			else if (i > 2 && i < 6)
				hydro[i] = t[i - 3];
			else
				hydro[i] = d[i - 6];
		}
	}

	void findPolariz() {
		double[] c = cc.getVectors(2);
		double[] t = tc.getVectors(2);
		double[] d = dc.getVectors(2);

		for (int i = 0; i < polariz.length; i++) {
			if (i < 3)
				polariz[i] = c[i];
			else if (i > 2 && i < 6)
				polariz[i] = t[i - 3];
			else
				polariz[i] = d[i - 6];
		}
	}

	void findPolar() {
		double[] c = cc.getVectors(3);
		double[] t = tc.getVectors(3);
		double[] d = dc.getVectors(3);

		for (int i = 0; i < polar.length; i++) {
			if (i < 3)
				polar[i] = c[i];
			else if (i > 2 && i < 6)
				polar[i] = t[i - 3];
			else
				polar[i] = d[i - 6];
		}
	}

	void findVDW() {
		double[] c = cc.getVectors(4);
		double[] t = tc.getVectors(4);
		double[] d = dc.getVectors(4);

		for (int i = 0; i < vdw.length; i++) {
			if (i < 3)
				vdw[i] = c[i];
			else if (i > 2 && i < 6)
				vdw[i] = t[i - 3];
			else
				vdw[i] = d[i - 6];
		}
	}

	void findCharge() {
		double[] c = cc.getVectors(5);
		double[] t = tc.getVectors(5);
		double[] d = dc.getVectors(5);

		for (int i = 0; i < charge.length; i++) {
			if (i < 3)
				charge[i] = c[i];
			else if (i > 2 && i < 6)
				charge[i] = t[i - 3];
			else
				charge[i] = d[i - 6];
		}
	}

	void findSolvent() {
		double[] c = cc.getVectors(6);
		double[] t = tc.getVectors(6);
		double[] d = dc.getVectors(6);

		for (int i = 0; i < solvent.length; i++) {
			if (i < 3)
				solvent[i] = c[i];
			else if (i > 2 && i < 6)
				solvent[i] = t[i - 3];
			else
				solvent[i] = d[i - 6];
		}
	}
	
	void findStructure() {
		double[] c = cc.getVectors(7);
		double[] t = tc.getVectors(7);
		double[] d = dc.getVectors(7);

		for (int i = 0; i < structure.length; i++) {
			if (i < 3)
				structure[i] = c[i];
			else if (i > 2 && i < 6)
				structure[i] = t[i - 3];
			else
				structure[i] = d[i - 6];
		}
	}

	void findComp() {
		int[] count = new int[20];

		// initialize count array
		for (int i = 0; i < count.length; i++) {
			count[i] = 0;
		}

		// count the number of amino acids in the sequence
		for (int i = 0; i < length; i++) {
			char currentAA = peptideArr[i];
			if (currentAA != 'X')
				count[getAAPos(currentAA)]++;
		}

		// get amino acid composition
		for (int i = 0; i < comp.length; i++) {
			if (count[i] == 0)
				comp[i] = 0;
			else {
				double num = (double) count[i] / length;
				BigDecimal bd1 = new BigDecimal(num, new MathContext(4));
				comp[i] = bd1.doubleValue();
			}
		}
	}

	int getAAPos(char aa) {
		char[] aaArr = { 'A', 'R', 'N', 'D', 'C', 'E', 'Q', 'G', 'H', 'I', 'L',
				'K', 'M', 'F', 'P', 'S', 'T', 'W', 'Y', 'V' };

		for (int i = 0; i < aaArr.length; i++) {
			if (aa == aaArr[i])
				return i;
		}

		System.out.println(aa);
		return -1;
	}

	void combineCTD() {
		for (int i = 0; i < ctd.length; i++) {
			if (i < 21)
				ctd[i] = hydro[i];
			else if (i > 20 && i < 42)
				ctd[i] = polariz[i - 21];
			else if (i > 41 && i < 63)
				ctd[i] = polar[i - 42];
			else if (i > 62 && i < 84)
				ctd[i] = vdw[i - 63];
			else if (i > 83 && i < 105)
				ctd[i] = charge[i - 84];
			else if (i > 104 && i < 126)
				ctd[i] = solvent[i - 105];
			//else if (i > 125 && i < 147)
				//ctd[i] = structure[i - 126];
			else
				ctd[i] = comp[i - 126];
		}
	}

}
