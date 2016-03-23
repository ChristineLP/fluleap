package predictor;

import java.io.InputStream;
import java.util.ArrayList;

import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

/**
 * SinglePredictor
 * 
 * <P>
 * Predicts the classification of a given sequence and model specified
 * 
 * @author Christine Eng
 * @version 1.0
 * 
 */

class HostTropismPredictor {

	String ip; // ip address of request
	long timeInM; // time in milliseconds of the request
	String strainName; // strain name of prediction
	long id; // unique id for each prediction

	String sequence; // sequence to be predicted
	String[] seqArr; // protein sequence array for whole strain
	String model; // prediction model to be used for classification
	String[] modelArr; // array containing models to be used for classification
	// int prediction; // result of prediction
	// int[] predictionsArr; // array of prediction results
	TransformSequence ts;
	Instances data;
	Instances unlabelled;
	Instances labelled;
	Classifier cls; // classifier model loaded for prediction
	String filename;
	String prefixProtein = "WEB-INF\\models\\IRD20140212";
	String prefixCombined = "WEB-INF\\models\\IRD20140211";
	String suffix = "final.model";
	// String absolutePath; // absolute path for servlet for reference to all
	// files
	String unclassifiedFile;

	/**
	 * Constructor for class
	 * 
	 * Sets the path to read and write all files
	 * 
	 * @param path
	 * @param input
	 *            stream for attributes file
	 */

	HostTropismPredictor(String ipAddr, long timeInMillis, String name) {
		// absolutePath = path;
		ip = ipAddr;
		timeInM = timeInMillis;
		id = ServletUtilities.generateID(ip, timeInM);
		strainName = name;

		// local server test
		// edit 14/05/2015 for local server test
		// file name of prefix Z and id, unique for each prediction
		unclassifiedFile = "/temp/HT" + id + ".arff";

		// server path
		// set path to temp folder
		// unclassifiedPath = "/var/lib/tomcat/temp/Unclassified.arff";
		// classifiedPath = "/var/lib/tomcat/temp/Classified.arff";
	}

	/**
	 * Predicts the classification of a given sequence and the specified protein
	 * model to be used for prediction
	 * 
	 * @param seq
	 * @param mod
	 * @param input
	 *            stream
	 * @return classification predicted
	 * @throws Exception
	 */

	int predict(String s, String mod, InputStream modelStream) {
		int prediction = -1;
		unclassifiedFile = "/temp/HT" + id + mod + ".arff";
		
		try {
			initialize(modelStream);
			extractFeatures(s, mod);
			readARFF();
			prediction = classify();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return prediction;
	}

	/**
	 * Predicts the classification of whole strain with all its protein
	 * sequences combined
	 * 
	 * @param array
	 *            of protein sequences
	 * @param input
	 *            stream of strain model
	 * @return classification predicted
	 * @throws Exception
	 */
	int predict(String[] sArr, InputStream strainStream) {
		int prediction = -1;
		unclassifiedFile = "/temp/HT" + id + "strain.arff";

		try {
			initialize(strainStream);
			extractFeatures(sArr, "strain");
			readARFF();
			prediction = classify();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return prediction;
	}

	/**
	 * Predicts the classification of all the proteins in the array
	 * 
	 * @param array
	 *            of protein sequences
	 * @param mod
	 * @return classification predicted
	 * @throws Exception
	 */
	int[] predict(String[] seqArr, ArrayList<InputStream> modelStreams) {
		int[] predictionsArr = new int[seqArr.length];

		try {
			for (int i = 0; i < seqArr.length; i++) {
				if (modelStreams.get(i) != null & seqArr[i] != null) {
					String mod = PredictorUtilities.getModelName(i + 1);
					unclassifiedFile = "/temp/HT" + id + mod + ".arff";
					initialize(modelStreams.get(i));
					extractFeatures(seqArr[i], mod);
					readARFF();
					predictionsArr[i] = classify();
				} else {
					predictionsArr[i] = -1;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return predictionsArr;
	}

	private void initialize(InputStream modelStream) throws Exception {
		// deserialize prediction model
		cls = (Classifier) weka.core.SerializationHelper.read(modelStream);
	}

	private void extractFeatures(String s, String model) throws Exception {
		sequence = s;

		// tranform sequence to feature vectors
		// and write to .arff file
		ts = new TransformSequence(s, id, ip, timeInM, strainName, model);
		ts.writeFile(unclassifiedFile, 0);
	}

	private void extractFeatures(String sArr[], String model) throws Exception {
		seqArr = sArr;

		ts = new TransformSequence(seqArr, id, ip, timeInM, strainName, model);
		ts.writeFile(unclassifiedFile, 1);
	}

	// reads from .arff file
	private void readARFF() throws Exception {
		DataSource source = new DataSource(unclassifiedFile);
		data = source.getDataSet();
		// set last attribute as the class
		if (data.classIndex() == -1)
			data.setClassIndex(data.numAttributes() - 1);
	}

	// classifies the unlabelled peptide and returns prediction
	private int classify() throws Exception {
		// load unlabelled data
		unlabelled = new Instances(data);
		// setting class attribute
		unlabelled.setClassIndex(data.numAttributes() - 1);

		// make prediction here
		double predictionIndex = cls.classifyInstance(unlabelled.instance(0));

		// get the predicted class label from the predictionIndex
		String predictedClassLabel = unlabelled.classAttribute().value(
				(int) predictionIndex);

		return Integer.parseInt(predictedClassLabel);

	}

}
