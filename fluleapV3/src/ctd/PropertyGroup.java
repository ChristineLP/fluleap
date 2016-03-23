package ctd;
public class PropertyGroup {

	char[][] hydro;
	char[][] polariz;
	char[][] polar;
	char[][] vdw;
	char[][] charge;
	char[][] solvent;
	char[][] structure;

	PropertyGroup() {
		initializeHydro();
		initializePolariz();
		initializePolar();
		initializeVDW();
		initializeCharge();
		initializeSolvent();
		initializeStructure();
	}

	void initializeHydro() {
		char[][] arr = { { 'R', 'K', 'E', 'D', 'Q', 'N' },
				{ 'G', 'A', 'S', 'T', 'P', 'H', 'Y' },
				{ 'C', 'V', 'L', 'I', 'M', 'F', 'W' } };

		hydro = arr;
	}

	void initializePolariz() {
		/*
		 * char[][] arr = { { 'G', 'A', 'S', 'C', 'T', 'P', 'D' },
				{ 'N', 'V', 'E', 'Q', 'I', 'L' },
				{ 'M', 'H', 'K', 'F', 'R', 'Y', 'W' } };
		 */
		
		char[][] arr = { { 'G', 'A', 'S', 'T',  'D' },
				{ 'N', 'V', 'E', 'Q', 'I', 'L', 'C', 'P' },
				{ 'M', 'H', 'K', 'F', 'R', 'Y', 'W' } };

		polariz = arr;
	}

	void initializePolar() {
		char[][] arr = { { 'L', 'I', 'F', 'W', 'C', 'M', 'V', 'Y' },
				{ 'P', 'A', 'T', 'G', 'S' },
				{ 'H', 'Q', 'R', 'K', 'N', 'E', 'D' } };

		polar = arr;
	}

	void initializeVDW() {
		/*
		 * char[][] arr = { { 'G', 'A', 'S', 'D', 'T' },
				{ 'C', 'P', 'N', 'V', 'E', 'Q', 'I', 'L' },
				{ 'K', 'M', 'H', 'F', 'R', 'Y', 'W' } };
		 */
		
		char[][] arr = { { 'G', 'A', 'S', 'D', 'T', 'P', 'C' },
				{ 'N', 'V', 'E', 'Q', 'I', 'L' },
				{ 'K', 'M', 'H', 'F', 'R', 'Y', 'W' } };

		vdw = arr;
	}

	void initializeCharge() {
		char[][] arr = {
				{ 'K', 'R' },
				{ 'A', 'N', 'C', 'Q', 'G', 'H', 'I', 'L', 'M', 'F', 'P', 'S',
						'T', 'W', 'Y', 'V' }, { 'D', 'E' } };

		charge = arr;
	}

	void initializeSolvent() {
		char[][] arr = { { 'A', 'L', 'F', 'C', 'G', 'I', 'V', 'W' },
				{ 'R', 'K', 'Q', 'E', 'N', 'D' },
				{ 'M', 'P', 'S', 'T', 'H', 'Y' } };

		solvent = arr;
	}

	void initializeStructure() {
		char[][] arr = { { 'E', 'A', 'L', 'M', 'Q', 'K', 'R', 'H' },
				{ 'V', 'I', 'Y', 'C', 'W', 'F', 'T' },
				{ 'G', 'N', 'P', 'S', 'D' } };

		structure = arr;
	}

	int getHydro(char residue) {

		for (int i = 0; i < hydro.length; i++) {

			for (int j = 0; j < hydro[i].length; j++) {

				if (residue == hydro[i][j])
					return i + 1;
			}
		}

		return 0;
	}

	int getPolariz(char residue) {

		for (int i = 0; i < polariz.length; i++) {

			for (int j = 0; j < polariz[i].length; j++) {

				if (residue == polariz[i][j])
					return i + 1;
			}
		}

		return 0;
	}

	int getPolar(char residue) {

		for (int i = 0; i < polar.length; i++) {

			for (int j = 0; j < polar[i].length; j++) {

				if (residue == polar[i][j])
					return i + 1;
			}
		}

		return 0;
	}

	int getVDW(char residue) {

		for (int i = 0; i < vdw.length; i++) {

			for (int j = 0; j < vdw[i].length; j++) {

				if (residue == vdw[i][j])
					return i + 1;
			}
		}

		return 0;
	}

	int getCharge(char residue) {

		for (int i = 0; i < charge.length; i++) {

			for (int j = 0; j < charge[i].length; j++) {

				if (residue == charge[i][j])
					return i + 1;
			}
		}

		return 0;
	}

	int getSolvent(char residue) {

		for (int i = 0; i < solvent.length; i++) {

			for (int j = 0; j < solvent[i].length; j++) {

				if (residue == solvent[i][j])
					return i + 1;
			}
		}

		return 0;
	}

	int getStructure(char residue) {

		for (int i = 0; i < structure.length; i++) {

			for (int j = 0; j < structure[i].length; j++) {

				if (residue == structure[i][j])
					return i + 1;
			}
		}

		return 0;
	}

	int getGroup(int property, char residue) {
		/*
		 * int property: 1 = hydrophobicity 2 = polarizibility 3 = polarity 4 =
		 * van der waal's volume 5 = charge 6 = solvent accessibility 7 =
		 * secondary structure
		 */

		int group;

		if (property == 1)
			group = getHydro(residue);
		else if (property == 2)
			group = getPolariz(residue);
		else if (property == 3)
			group = getPolar(residue);
		else if (property == 4)
			group = getVDW(residue);
		else if (property == 5)
			group = getCharge(residue);
		else if (property == 6)
			group = getSolvent(residue);
		else if (property == 7)
			group = getStructure(residue);
		else
			group = 0;

		return group;
	}

}
