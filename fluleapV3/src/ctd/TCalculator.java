package ctd;
import java.math.BigDecimal;
import java.math.MathContext;

public class TCalculator {

	char[] peptideArr;
	int[] groupsArr;
	int[] transArr;
	int length;
	int property;
	PropertyGroup pg;
	int n1, n2, n3;
	double t1, t2, t3;
	double[] vectors;

	TCalculator(char[] arr) {
		peptideArr = arr;
		groupsArr = new int[peptideArr.length];
		transArr = new int[peptideArr.length - 1];
		length = transArr.length;
		pg = new PropertyGroup();
		vectors = new double[3];
	}

	void getGroups(int mode) {

		for (int i = 0; i < peptideArr.length; i++) {
			groupsArr[i] = pg.getGroup(mode, peptideArr[i]);
		}
	}

	void getTransition() {
		int r1 = 0;
		int r2 = 0;
		int sum = 0;

		for (int i = 0; i < transArr.length; i++) {
			r1 = groupsArr[i];
			r2 = groupsArr[i + 1];
			sum = r1 + r2;

			if (r1 == r2)
				transArr[i] = 0;
			else {
				if (sum == 3)
					transArr[i] = 1;
				else if (sum == 4)
					transArr[i] = 2;
				else if (sum == 5)
					transArr[i] = 3;
			}

		}
	}

	void countTrans() {
		n1 = 0;
		n2 = 0;
		n3 = 0;

		for (int i = 0; i < transArr.length; i++) {
			if (transArr[i] == 1)
				n1++;
			else if (transArr[i] == 2)
				n2++;
			else if (transArr[i] == 3)
				n3++;
		}

	}

	void calculate() {

		t1 = (double) n1 / length * 100;
		t2 = (double) n2 / length * 100;
		t3 = (double) n3 / length * 100;

		BigDecimal bd1 = new BigDecimal(t1, new MathContext(4));
		BigDecimal bd2 = new BigDecimal(t2, new MathContext(4));
		BigDecimal bd3 = new BigDecimal(t3, new MathContext(4));

		t1 = bd1.doubleValue();
		t2 = bd2.doubleValue();
		t3 = bd3.doubleValue();

	}

	double[] getVectors(int mode) {
		property = mode;

		getGroups(property);
		getTransition();
		countTrans();
		calculate();

		vectors[0] = t1;
		vectors[1] = t2;
		vectors[2] = t3;

		return vectors;
	}

}
