package ctd;
import java.math.BigDecimal;
import java.math.MathContext;

public class CCalculator {

	char[] peptideArr;
	int[] groupsArr;
	int length;
	int property;
	PropertyGroup pg;
	int n1, n2, n3;
	double c1, c2, c3;
	double[] vectors;

	CCalculator(char[] arr) {
		peptideArr = arr;
		groupsArr = new int[peptideArr.length];
		length = peptideArr.length;
		pg = new PropertyGroup();
		vectors = new double[3];
	}

	void getGroups(int mode) {

		for (int i = 0; i < peptideArr.length; i++) {
			groupsArr[i] = pg.getGroup(mode, peptideArr[i]);
		}
	}

	void countGroups() {
		n1 = 0;
		n2 = 0;
		n3 = 0;

		for (int i = 0; i < groupsArr.length; i++) {
			if (groupsArr[i] == 1)
				n1++;
			else if (groupsArr[i] == 2)
				n2++;
			else
				n3++;
		}

	}

	void calculate() {

		c1 = (double) n1 / length * 100;
		c2 = (double) n2 / length * 100;
		c3 = (double) n3 / length * 100;

		BigDecimal bd1 = new BigDecimal(c1, new MathContext(4));
		BigDecimal bd2 = new BigDecimal(c2, new MathContext(4));
		BigDecimal bd3 = new BigDecimal(c3, new MathContext(4));

		c1 = bd1.doubleValue();
		c2 = bd2.doubleValue();
		c3 = bd3.doubleValue();

	}

	double[] getVectors(int mode) {
		property = mode;

		getGroups(property);
		countGroups();
		calculate();

		vectors[0] = c1;
		vectors[1] = c2;
		vectors[2] = c3;

		return vectors;
	}
}
