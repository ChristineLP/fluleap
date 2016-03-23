package predictor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;

import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

/**
 * ZoonoticPredictor
 * 
 * <P>
 * Predicts the classification of a given host tropism protein signature
 * Estimates avian, human and zoonotic probability distribution Takes
 * accuracy-weighted mean of 5 models: ANN, IBk, NaiveBayes, SVM and Random
 * Forest
 * 
 * @author Christine Eng
 * @version 1.0
 * 
 */

class ZoonoticPredictor {

	String ip;
	long timeInM;
	String strainName;
	long id;
	String unclassifiedFile;
	String[] prClassArr;
	double[][] prDistributionArr;

	ZoonoticPredictor(String ipAddr, long timeInMillis, String name) {
		ip = ipAddr;
		timeInM = timeInMillis;
		id = ServletUtilities.generateID(ip, timeInM);
		strainName = name;

		// only for local server test
		// file name of prefix Z and id, unique for each prediction
		unclassifiedFile = "/temp/Z" + id + ".arff";

		// for server
		// set path to temp folder
		// unclassifiedFile = "/var/lib/tomcat/temp/Z" + id + ".arff";

		// initialize results array
		prClassArr = new String[5]; // 5 classifications predicted by 5 models
		prDistributionArr = new double[5][3]; // matrix of 5 by 3 for A, H and Z
												// distribution by 5 models
	}

	/**
	 * Predicts the classification of a strain given a host tropism protein
	 * signature array and an array of 5 models
	 * 
	 * @param sigArr
	 * @param modelStreams
	 */
	void predict(int[] sigArr, ArrayList<InputStream> modelStreams) {

		try {
			if (sigArr != null)
				writeFile(sigArr);

			for (int i = 0; i < modelStreams.size(); i++) {
				if (modelStreams.get(i) != null) {
					classify(i, modelStreams.get(i));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the majority classification
	 * 
	 * @return classification with the most votes
	 */
	int getPredictionClass() {
		// initialize counts of each classification
		int a = 0;
		int h = 0;
		int z = 0;

		// counts the number of classification
		for (int i = 0; i < prClassArr.length; i++) {
			System.out.println(i + ": " + prClassArr[i]);

			if (prClassArr[i].equals("avian"))
				a++;
			else if (prClassArr[i].equals("human"))
				h++;
			else
				z++;
		}

		// if any classification has more than 3 votes, then it's the majority,
		// return the class
		if (a >= 3)
			return 0;
		else if (h >= 3)
			return 1;
		else if (z >= 3)
			return 2;
		else {
			// the last option is
			// if there is a tie between 2 classification groups, ie 2 2 1
			// get the probability distribution
			double[] distArr = getPredictionDistribution(true);
			double maxDist = 0; // to record the highest distribution
			int maxClass = 0; // class with the highest distribution

			for (int i = 0; i < distArr.length; i++) {
				if (distArr[i] >= maxDist) {
					maxDist = distArr[i];
					maxClass = i;
				}
			}
			return maxClass;
		}
	}

	/**
	 * Returns the mean avian, human and zoonotic distribution of 5 models
	 * 
	 * @param option to apply accuracy weights of models to mean distribution
	 * @return array of mean avian, human and zoonotic distribution
	 */
	double[] getPredictionDistribution(boolean applyACCweights) {
		double[] meanDistArr = new double[3];

		// PRINTING TEST
		System.out.println("PRINTING DISTRIBUTION ARRAY");
		for (int i = 0; i < prDistributionArr.length; i++) {
			for (int j = 0; j < prDistributionArr[i].length; j++) {
				BigDecimal d = new BigDecimal(prDistributionArr[i][j]);
				d = d.setScale(3, RoundingMode.HALF_UP);
				System.out.print(d + " ");
			}
			System.out.println();
		}

		// traverse column wise to add the avian, human and zoonotic
		// distributions
		// apply accuracy weights to the distributions and then obtain the mean
		// and record into mean distribution array
		for (int i = 0; i < prDistributionArr[i].length; i++) {
			double distTotal = 0; // initialize the total distribution

			for (int j = 0; j < prDistributionArr.length; j++) {
				if (applyACCweights)
					distTotal += (prDistributionArr[j][i] * PredictorUtilities
							.getZoonoticACC(j));
				else
					distTotal += prDistributionArr[j][i];
			}

			meanDistArr[i] = distTotal / 5;
		}

		return meanDistArr;
	}

	/**
	 * Writes host tropism protein signature features into ARFF file for
	 * classification
	 * 
	 * @param sigArr
	 */
	private void writeFile(int[] sigArr) {
		// create output file
		File f = new File(unclassifiedFile); // output ARFF file for transformed
		// sequence

		FileWriter fw;
		try {
			if (!f.exists()) {
				f.createNewFile();
			}

			fw = new FileWriter(f, false);
			BufferedWriter bw = new BufferedWriter(fw);

			writeHeader(bw);
			writeData(bw, sigArr);

			bw.write("%\n%\n%\n");
			bw.flush();
			bw.close();

		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Writes header for ARFF file Contains header with title, ip, date and
	 * time, and attributes
	 * 
	 * @param bw
	 * @throws Exception
	 */
	private void writeHeader(BufferedWriter bw) throws Exception {
		bw.write("%Zoonotic prediction\n");
		bw.write("%http://fluleap.bic.nus.edu.sg\n");
		bw.write("%Unclassified strain\n%\n");
		bw.write("%Request " + id + "\n");
		bw.write("%generated from " + ip + "\n");
		bw.write("%on " + new Date(timeInM) + "\n");
		System.out.println("Strain name: " + strainName);
		if (strainName != null && !strainName.isEmpty())
			bw.write("%for " + strainName + "\n%\n");
		else
			bw.write("%\n");
		bw.write("@relation Unclassified_strain\n\n");

		bw.write("@attribute HA {0,1}\n");
		bw.write("@attribute M1 {0,1}\n");
		bw.write("@attribute M2 {0,1}\n");
		bw.write("@attribute NA {0,1}\n");
		bw.write("@attribute NP {0,1}\n");
		bw.write("@attribute NS1 {0,1}\n");
		bw.write("@attribute NS2 {0,1}\n");
		bw.write("@attribute PA {0,1}\n");
		bw.write("@attribute PB1 {0,1}\n");
		bw.write("@attribute PB1F2 {0,1}\n");
		bw.write("@attribute PB2 {0,1}\n");
		bw.write("@attribute Total real\n");
		bw.write("@attribute class {avian,human,zoonotic}\n\n");
		bw.write("@data\n");
		bw.flush();
	}

	/**
	 * Writes data for ARFF file Encodes each signature with 0 or 1 prediction
	 * and class with ?
	 * 
	 * @param bw
	 * @param dataArr
	 * @throws Exception
	 */
	private void writeData(BufferedWriter bw, int[] dataArr) throws Exception {
		for (int i = 0; i < dataArr.length; i++) {
			bw.write(dataArr[i] + ",");
		}
		bw.write("?\n");
		bw.flush();
	}

	/**
	 * Classifies instance given a model Loads instance from ARFF file and
	 * classifies with the model Records predicted class label and distribution
	 * in results array
	 * 
	 * @param modelIndex
	 * @param modelStream
	 * @throws Exception
	 */
	private void classify(int modelIndex, InputStream modelStream)
			throws Exception {
		// deserialize the classifier

		Classifier classifier = (Classifier) weka.core.SerializationHelper
				.read(modelStream);

		// read the unclassified ARFF file
		// load the instance to be tested
		Instances data = DataSource.read(unclassifiedFile);

		// set the last attribute as the class
		if (data.classIndex() == -1)
			data.setClassIndex(data.numAttributes() - 1);

		// load unlabelled data
		Instances unlabelled = new Instances(data);
		// setting class attribute
		unlabelled.setClassIndex(data.numAttributes() - 1);

		// label instance
		// only 1 instance in each ARFF file so it's always instance(0)

		// make prediction here
		double predictionIndex = classifier.classifyInstance(unlabelled
				.instance(0));

		// get the predicted class label from the predictionIndex
		String predictedClassLabel = unlabelled.classAttribute().value(
				(int) predictionIndex);

		// get the predicted probability distribution
		double[] predictionDistribution = classifier
				.distributionForInstance(unlabelled.instance(0));

		// record results in array
		prClassArr[modelIndex] = predictedClassLabel;
		for (int i = 0; i < 3; i++) {
			prDistributionArr[modelIndex][i] = predictionDistribution[i];
		}
	}
}
