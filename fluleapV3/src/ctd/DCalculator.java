package ctd;
import java.math.BigDecimal;
import java.math.MathContext;

public class DCalculator {

	char[] peptideArr;
	int[] groupsArr;
	int length;
	int property;
	PropertyGroup pg;
	int n1, n2, n3;
	int[] positions;
	double[] vectors;

	DCalculator(char[] arr) {
		peptideArr = arr;
		groupsArr = new int[peptideArr.length];
		length = groupsArr.length;
		pg = new PropertyGroup();
		positions = new int[15];
		vectors = new double[15];
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
			else if (groupsArr[i] == 3)
				n3++;
		}

	}

	void countDist() {

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 5; j++) {

				int num;
				if (i == 0)
					num = j;
				else if (i == 1)
					num = j + 5;
				else
					num = j + 10;

				positions[num] = getPos(i + 1, j + 1);
			}
		}
	}

	int getPos(int group, int dist) {
		int total, residue, count;

		// get total residues in each group
		if (group == 1)
			total = n1;
		else if (group == 2)
			total = n2;
		else
			total = n3;

		if (total == 0)
			return 0;
		
		// if there is only 1 residue of the group in the sequence
		if (total == 1) {
			for (int i = 0; i < groupsArr.length; i++) {
				if (groupsArr[i] == group)
					return i + 1;
			}
		}

		// find position according to distribution
		if (dist == 1) {
			for (int i = 0; i < groupsArr.length; i++) {
				if (groupsArr[i] == group)
					return i + 1;
			}
		} else {
			double num;

			if (dist == 2)
				num = 0.25 * total;
			else if (dist == 3)
				num = 0.5 * total;
			else if (dist == 4)
				num = 0.75 * total;
			else
				num = total;

			residue = (int) Math.round(num);

			count = 0;
			for (int i = 0; i < groupsArr.length; i++) {
				if (groupsArr[i] == group)
					count++;
				if (residue == count)
					return i + 1;
			}
		}

		return 0;

	}

	void calculate() {
		for (int i = 0; i < vectors.length; i++) {
			double num = (double) positions[i] / length * 100;
			BigDecimal bd = new BigDecimal(num, new MathContext(4));
			vectors[i] = bd.doubleValue();
		}
	}

	double[] getVectors(int mode) {
		property = mode;

		getGroups(property);
		countGroups();
		countDist();
		calculate();

		return vectors;
	}
}
