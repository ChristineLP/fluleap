package predictor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import ctd.CTDCalculator;

/**
 * TransformSequence
 * 
 * <P>
 * Transform a given sequence into feature vectors and writes it into a new ARFF
 * file for classification prediction
 * 
 * @author Christine Eng
 * @version 1.0
 * 
 */

class TransformSequence {

	long id;
	String ip;
	long timeInM;
	String strainName;
	String model;
	double[] ctdProtArr;
	double[] ctdStrainArr;
	String sequence;

	int[][] featuresArr = {
			{ 18, 60, 67, 68, 72, 89, 93, 103, 130, 133, 135, 137, 138, 142,
					146 },
			{ 5, 56, 68, 77, 106, 109, 110, 115, 124, 125, 127, 136, 142, 143,
					146 },
			{ 5, 14, 30, 34, 47, 48, 56, 60, 72, 89, 124, 129, 133, 140, 145 },
			{ 5, 25, 27, 46, 47, 109, 127, 128, 132, 133, 135, 138, 141, 142,
					145 },
			{ 14, 27, 50, 67, 69, 71, 89, 102, 106, 111, 128, 136, 138, 141,
					146 },
			{ 4, 5, 30, 42, 47, 56, 57, 60, 67, 69, 87, 90, 130, 145, 146 },
			{ 15, 24, 27, 31, 57, 66, 67, 72, 73, 125, 131, 134, 135, 142, 143 },
			{ 2, 13, 22, 29, 36, 44, 55, 64, 71, 89, 108, 111, 124, 129, 146 },
			{ 26, 30, 56, 68, 73, 76, 79, 109, 110, 113, 115, 125, 132, 136,
					138 },
			{ 11, 63, 67, 71, 86, 89, 91, 105, 121, 127, 129, 131, 134, 135,
					145 },
			{ 88, 90, 104, 108, 110, 111, 114, 115, 124, 127, 129, 130, 141,
					142, 143 } };

	TransformSequence(String sequence, long uniqueID, String ipAddr, long time, String name, String mod) {
		getSingleFeatures(sequence);
		id = uniqueID;
		ip = ipAddr;
		timeInM = time;
		strainName = name;
		model = mod;
	}
	
	TransformSequence(String[] seqArr, long uniqueID, String ipAddr, long time, String name, String mod) {
		getStrainFeatures(seqArr);
		id = uniqueID;
		ip = ipAddr;
		timeInM = time;
		strainName = name;
		model = mod;
	}

	private void getSingleFeatures(String sequence) {
		CTDCalculator ctdc = new CTDCalculator();
		ctdProtArr = ctdc.getCTD(sequence);
	}

	private void getStrainFeatures(String[] seqArr) {
		CTDCalculator ctdc = new CTDCalculator();
		ctdStrainArr = new double[165];
		int strainArrIndex = 0;
		double[] ctdArr; // temp array to store ctd values for proteins
		String currPeptide = "";

		// loops through the peptide array to extract features for each protein
		// loop 1: extract protein from sequence array
		for (int i = 0; i < seqArr.length; i++) {
			currPeptide = seqArr[i];
			if (currPeptide != null)
				ctdArr = ctdc.getCTD(currPeptide); // CTD for current peptide
			else
				ctdArr = getEmptyCTD();

			// writes ctd
			// loop 2: loop through the features array
			// to find the 15 features in the ctd array
			for (int j = 0; j < featuresArr[i].length; j++) {
				int k = featuresArr[i][j] - 1; // index of feature in ctd array
				// weka index of extracted features starts with 1
				// ctdArray index of the same feature starts with 0

				ctdStrainArr[strainArrIndex] = ctdArr[k];
				strainArrIndex++;

			}
		}
	}

	private double[] getEmptyCTD() {
		double[] emptyCTD = new double[146];

		for (int i = 0; i < emptyCTD.length; i++)
			emptyCTD[i] = -1;

		return emptyCTD;
	}

	private void writeHeader(BufferedWriter bw, int mode) throws Exception {
		bw.write("%Host tropism prediction - " + model + "\n");
		bw.write("%http://fluleap.bic.nus.edu.sg\n");
		bw.write("%Unclassified Peptide\n%\n");
		bw.write("%Request " + id + "\n");
		bw.write("%generated from " + ip + "\n");
		bw.write("%on " + new Date(timeInM) + "\n");
		if (strainName != null && !strainName.isEmpty())
			bw.write("%for " + strainName + "\n%\n");
		else
			bw.write("%\n");
		
		bw.write("@relation Unclassified_peptide\n\n");

		if (mode == 0) {
			bw.write("@attribute 'hydroc1' real\n");
			bw.write("@attribute 'hydroc2' real\n");
			bw.write("@attribute 'hydroc3' real\n");
			bw.write("@attribute 'hydrot1' real\n");
			bw.write("@attribute 'hydrot2' real\n");
			bw.write("@attribute 'hydrot3' real\n");
			bw.write("@attribute 'hydrod1' real\n");
			bw.write("@attribute 'hydrod2' real\n");
			bw.write("@attribute 'hydrod3' real\n");
			bw.write("@attribute 'hydrod4' real\n");
			bw.write("@attribute 'hydrod5' real\n");
			bw.write("@attribute 'hydrod6' real\n");
			bw.write("@attribute 'hydrod7' real\n");
			bw.write("@attribute 'hydrod8' real\n");
			bw.write("@attribute 'hydrod9' real\n");
			bw.write("@attribute 'hydrod10' real\n");
			bw.write("@attribute 'hydrod11' real\n");
			bw.write("@attribute 'hydrod12' real\n");
			bw.write("@attribute 'hydrod13' real\n");
			bw.write("@attribute 'hydrod14' real\n");
			bw.write("@attribute 'hydrod15' real\n");
			bw.write("@attribute 'polarizc1' real\n");
			bw.write("@attribute 'polarizc2' real\n");
			bw.write("@attribute 'polarizc3' real\n");
			bw.write("@attribute 'polarizt1' real\n");
			bw.write("@attribute 'polarizt2' real\n");
			bw.write("@attribute 'polarizt3' real\n");
			bw.write("@attribute 'polarizd1' real\n");
			bw.write("@attribute 'polarizd2' real\n");
			bw.write("@attribute 'polarizd3' real\n");
			bw.write("@attribute 'polarizd4' real\n");
			bw.write("@attribute 'polarizd5' real\n");
			bw.write("@attribute 'polarizd6' real\n");
			bw.write("@attribute 'polarizd7' real\n");
			bw.write("@attribute 'polarizd8' real\n");
			bw.write("@attribute 'polarizd9' real\n");
			bw.write("@attribute 'polarizd10' real\n");
			bw.write("@attribute 'polarizd11' real\n");
			bw.write("@attribute 'polarizd12' real\n");
			bw.write("@attribute 'polarizd13' real\n");
			bw.write("@attribute 'polarizd14' real\n");
			bw.write("@attribute 'polarizd15' real\n");
			bw.write("@attribute 'polarc1' real\n");
			bw.write("@attribute 'polarc2' real\n");
			bw.write("@attribute 'polarc3' real\n");
			bw.write("@attribute 'polart1' real\n");
			bw.write("@attribute 'polart2' real\n");
			bw.write("@attribute 'polart3' real\n");
			bw.write("@attribute 'polard1' real\n");
			bw.write("@attribute 'polard2' real\n");
			bw.write("@attribute 'polard3' real\n");
			bw.write("@attribute 'polard4' real\n");
			bw.write("@attribute 'polard5' real\n");
			bw.write("@attribute 'polard6' real\n");
			bw.write("@attribute 'polard7' real\n");
			bw.write("@attribute 'polard8' real\n");
			bw.write("@attribute 'polard9' real\n");
			bw.write("@attribute 'polard10' real\n");
			bw.write("@attribute 'polard11' real\n");
			bw.write("@attribute 'polard12' real\n");
			bw.write("@attribute 'polard13' real\n");
			bw.write("@attribute 'polard14' real\n");
			bw.write("@attribute 'polard15' real\n");
			bw.write("@attribute 'vdwc1' real\n");
			bw.write("@attribute 'vdwc2' real\n");
			bw.write("@attribute 'vdwc3' real\n");
			bw.write("@attribute 'vdwt1' real\n");
			bw.write("@attribute 'vdwt2' real\n");
			bw.write("@attribute 'vdwt3' real\n");
			bw.write("@attribute 'vdwd1' real\n");
			bw.write("@attribute 'vdwd2' real\n");
			bw.write("@attribute 'vdwd3' real\n");
			bw.write("@attribute 'vdwd4' real\n");
			bw.write("@attribute 'vdwd5' real\n");
			bw.write("@attribute 'vdwd6' real\n");
			bw.write("@attribute 'vdwd7' real\n");
			bw.write("@attribute 'vdwd8' real\n");
			bw.write("@attribute 'vdwd9' real\n");
			bw.write("@attribute 'vdwd10' real\n");
			bw.write("@attribute 'vdwd11' real\n");
			bw.write("@attribute 'vdwd12' real\n");
			bw.write("@attribute 'vdwd13' real\n");
			bw.write("@attribute 'vdwd14' real\n");
			bw.write("@attribute 'vdwd15' real\n");
			bw.write("@attribute 'chargec1' real\n");
			bw.write("@attribute 'chargec2' real\n");
			bw.write("@attribute 'chargec3' real\n");
			bw.write("@attribute 'charget1' real\n");
			bw.write("@attribute 'charget2' real\n");
			bw.write("@attribute 'charget3' real\n");
			bw.write("@attribute 'charged1' real\n");
			bw.write("@attribute 'charged2' real\n");
			bw.write("@attribute 'charged3' real\n");
			bw.write("@attribute 'charged4' real\n");
			bw.write("@attribute 'charged5' real\n");
			bw.write("@attribute 'charged6' real\n");
			bw.write("@attribute 'charged7' real\n");
			bw.write("@attribute 'charged8' real\n");
			bw.write("@attribute 'charged9' real\n");
			bw.write("@attribute 'charged10' real\n");
			bw.write("@attribute 'charged11' real\n");
			bw.write("@attribute 'charged12' real\n");
			bw.write("@attribute 'charged13' real\n");
			bw.write("@attribute 'charged14' real\n");
			bw.write("@attribute 'charged15' real\n");
			bw.write("@attribute 'solventc1' real\n");
			bw.write("@attribute 'solventc2' real\n");
			bw.write("@attribute 'solventc3' real\n");
			bw.write("@attribute 'solventt1' real\n");
			bw.write("@attribute 'solventt2' real\n");
			bw.write("@attribute 'solventt3' real\n");
			bw.write("@attribute 'solventd1' real\n");
			bw.write("@attribute 'solventd2' real\n");
			bw.write("@attribute 'solventd3' real\n");
			bw.write("@attribute 'solventd4' real\n");
			bw.write("@attribute 'solventd5' real\n");
			bw.write("@attribute 'solventd6' real\n");
			bw.write("@attribute 'solventd7' real\n");
			bw.write("@attribute 'solventd8' real\n");
			bw.write("@attribute 'solventd9' real\n");
			bw.write("@attribute 'solventd10' real\n");
			bw.write("@attribute 'solventd11' real\n");
			bw.write("@attribute 'solventd12' real\n");
			bw.write("@attribute 'solventd13' real\n");
			bw.write("@attribute 'solventd14' real\n");
			bw.write("@attribute 'solventd15' real\n");
			bw.write("@attribute 'comp1' real\n");
			bw.write("@attribute 'comp2' real\n");
			bw.write("@attribute 'comp3' real\n");
			bw.write("@attribute 'comp4' real\n");
			bw.write("@attribute 'comp5' real\n");
			bw.write("@attribute 'comp6' real\n");
			bw.write("@attribute 'comp7' real\n");
			bw.write("@attribute 'comp8' real\n");
			bw.write("@attribute 'comp9' real\n");
			bw.write("@attribute 'comp10' real\n");
			bw.write("@attribute 'comp11' real\n");
			bw.write("@attribute 'comp12' real\n");
			bw.write("@attribute 'comp13' real\n");
			bw.write("@attribute 'comp14' real\n");
			bw.write("@attribute 'comp15' real\n");
			bw.write("@attribute 'comp16' real\n");
			bw.write("@attribute 'comp17' real\n");
			bw.write("@attribute 'comp18' real\n");
			bw.write("@attribute 'comp19' real\n");
			bw.write("@attribute 'comp20' real\n");
			bw.write("@attribute 'class' {0, 1}\n\n");
			bw.write("@data\n");
			bw.flush();
		}
		else {
			bw.write("@attribute 'HAhydrod12' real\n");
			bw.write("@attribute 'HApolard12' real\n");
			bw.write("@attribute 'HAvdwt1' real\n");
			bw.write("@attribute 'HAvdwt2' real\n");
			bw.write("@attribute 'HAvdwd3' real\n");
			bw.write("@attribute 'HAcharget2' real\n");
			bw.write("@attribute 'HAcharged3' real\n");
			bw.write("@attribute 'HAcharged13' real\n");
			bw.write("@attribute 'HAcomp4D' real\n");
			bw.write("@attribute 'HAcomp7Q' real\n");
			bw.write("@attribute 'HAcomp9H' real\n");
			bw.write("@attribute 'HAcomp11L' real\n");
			bw.write("@attribute 'HAcomp12K' real\n");
			bw.write("@attribute 'HAcomp16S' real\n");
			bw.write("@attribute 'HAcomp20V' real\n");
			bw.write("@attribute 'M1hydrot2' real\n");
			bw.write("@attribute 'M1polard8' real\n");
			bw.write("@attribute 'M1vdwt2' real\n");
			bw.write("@attribute 'M1vdwd8' real\n");
			bw.write("@attribute 'M1solventc1' real\n");
			bw.write("@attribute 'M1solventt1' real\n");
			bw.write("@attribute 'M1solventt2' real\n");
			bw.write("@attribute 'M1solventd4' real\n");
			bw.write("@attribute 'M1solventd13' real\n");
			bw.write("@attribute 'M1solventd14' real\n");
			bw.write("@attribute 'M1comp1A' real\n");
			bw.write("@attribute 'M1comp10I' real\n");
			bw.write("@attribute 'M1comp16S' real\n");
			bw.write("@attribute 'M1comp17T' real\n");
			bw.write("@attribute 'M1comp20V' real\n");
			bw.write("@attribute 'M2hydrot2' real\n");
			bw.write("@attribute 'M2hydrod8' real\n");
			bw.write("@attribute 'M2polarizd3' real\n");
			bw.write("@attribute 'M2polarizd7' real\n");
			bw.write("@attribute 'M2polart2' real\n");
			bw.write("@attribute 'M2polart3' real\n");
			bw.write("@attribute 'M2polard8' real\n");
			bw.write("@attribute 'M2polard12' real\n");
			bw.write("@attribute 'M2vdwd3' real\n");
			bw.write("@attribute 'M2charget2' real\n");
			bw.write("@attribute 'M2solventd13' real\n");
			bw.write("@attribute 'M2comp3N' real\n");
			bw.write("@attribute 'M2comp7Q' real\n");
			bw.write("@attribute 'M2comp14F' real\n");
			bw.write("@attribute 'M2comp19Y' real\n");
			bw.write("@attribute 'NAhydrot2' real\n");
			bw.write("@attribute 'NApolarizt1' real\n");
			bw.write("@attribute 'NApolarizt3' real\n");
			bw.write("@attribute 'NApolart1' real\n");
			bw.write("@attribute 'NApolart2' real\n");
			bw.write("@attribute 'NAsolventt1' real\n");
			bw.write("@attribute 'NAcomp1A' real\n");
			bw.write("@attribute 'NAcomp2R' real\n");
			bw.write("@attribute 'NAcomp6E' real\n");
			bw.write("@attribute 'NAcomp7Q' real\n");
			bw.write("@attribute 'NAcomp9H' real\n");
			bw.write("@attribute 'NAcomp12K' real\n");
			bw.write("@attribute 'NAcomp15P' real\n");
			bw.write("@attribute 'NAcomp16S' real\n");
			bw.write("@attribute 'NAcomp19Y' real\n");
			bw.write("@attribute 'NPhydrod8' real\n");
			bw.write("@attribute 'NPpolarizt3' real\n");
			bw.write("@attribute 'NPpolard2' real\n");
			bw.write("@attribute 'NPvdwt1' real\n");
			bw.write("@attribute 'NPvdwt3' real\n");
			bw.write("@attribute 'NPvdwd2' real\n");
			bw.write("@attribute 'NPcharget2' real\n");
			bw.write("@attribute 'NPcharged12' real\n");
			bw.write("@attribute 'NPsolventc1' real\n");
			bw.write("@attribute 'NPsolventt3' real\n");
			bw.write("@attribute 'NPcomp2R' real\n");
			bw.write("@attribute 'NPcomp10I' real\n");
			bw.write("@attribute 'NPcomp12K' real\n");
			bw.write("@attribute 'NPcomp15P' real\n");
			bw.write("@attribute 'NPcomp20V' real\n");
			bw.write("@attribute 'NS1hydrot1' real\n");
			bw.write("@attribute 'NS1hydrot2' real\n");
			bw.write("@attribute 'NS1polarizd3' real\n");
			bw.write("@attribute 'NS1polarizd15' real\n");
			bw.write("@attribute 'NS1polart2' real\n");
			bw.write("@attribute 'NS1polard8' real\n");
			bw.write("@attribute 'NS1polard9' real\n");
			bw.write("@attribute 'NS1polard12' real\n");
			bw.write("@attribute 'NS1vdwt1' real\n");
			bw.write("@attribute 'NS1vdwt3' real\n");
			bw.write("@attribute 'NS1chargec3' real\n");
			bw.write("@attribute 'NS1charget3' real\n");
			bw.write("@attribute 'NS1comp4D' real\n");
			bw.write("@attribute 'NS1comp19Y' real\n");
			bw.write("@attribute 'NS1comp20V' real\n");
			bw.write("@attribute 'NS2hydrod9' real\n");
			bw.write("@attribute 'NS2polarizc3' real\n");
			bw.write("@attribute 'NS2polarizt3' real\n");
			bw.write("@attribute 'NS2polarizd4' real\n");
			bw.write("@attribute 'NS2polard9' real\n");
			bw.write("@attribute 'NS2vdwc3' real\n");
			bw.write("@attribute 'NS2vdwt1' real\n");
			bw.write("@attribute 'NS2vdwd3' real\n");
			bw.write("@attribute 'NS2vdwd4' real\n");
			bw.write("@attribute 'NS2solventd14' real\n");
			bw.write("@attribute 'NS2comp5C' real\n");
			bw.write("@attribute 'NS2comp8G' real\n");
			bw.write("@attribute 'NS2comp9H' real\n");
			bw.write("@attribute 'NS2comp16S' real\n");
			bw.write("@attribute 'NS2comp17T' real\n");
			bw.write("@attribute 'PAhydroc2' real\n");
			bw.write("@attribute 'PAhydrod7' real\n");
			bw.write("@attribute 'PApolarizc1' real\n");
			bw.write("@attribute 'PApolarizd2' real\n");
			bw.write("@attribute 'PApolarizd9' real\n");
			bw.write("@attribute 'PApolarc2' real\n");
			bw.write("@attribute 'PApolard7' real\n");
			bw.write("@attribute 'PAvdwc1' real\n");
			bw.write("@attribute 'PAvdwd2' real\n");
			bw.write("@attribute 'PAcharget2' real\n");
			bw.write("@attribute 'PAsolventc3' real\n");
			bw.write("@attribute 'PAsolventt3' real\n");
			bw.write("@attribute 'PAsolventd13' real\n");
			bw.write("@attribute 'PAcomp3N' real\n");
			bw.write("@attribute 'PAcomp20V' real\n");
			bw.write("@attribute 'PB1polarizt2' real\n");
			bw.write("@attribute 'PB1polarizd3' real\n");
			bw.write("@attribute 'PB1polard8' real\n");
			bw.write("@attribute 'PB1vdwt2' real\n");
			bw.write("@attribute 'PB1vdwd4' real\n");
			bw.write("@attribute 'PB1vdwd7' real\n");
			bw.write("@attribute 'PB1vdwd10' real\n");
			bw.write("@attribute 'PB1solventt1' real\n");
			bw.write("@attribute 'PB1solventt2' real\n");
			bw.write("@attribute 'PB1solventd2' real\n");
			bw.write("@attribute 'PB1solventd4' real\n");
			bw.write("@attribute 'PB1solventd14' real\n");
			bw.write("@attribute 'PB1comp6E' real\n");
			bw.write("@attribute 'PB1comp10I' real\n");
			bw.write("@attribute 'PB1comp12K' real\n");
			bw.write("@attribute 'PB1F2hydrod5' real\n");
			bw.write("@attribute 'PB1F2polard15' real\n");
			bw.write("@attribute 'PB1F2vdwt1' real\n");
			bw.write("@attribute 'PB1F2vdwd2' real\n");
			bw.write("@attribute 'PB1F2chargec2' real\n");
			bw.write("@attribute 'PB1F2charget2' real\n");
			bw.write("@attribute 'PB1F2charged1' real\n");
			bw.write("@attribute 'PB1F2charged15' real\n");
			bw.write("@attribute 'PB1F2solventd10' real\n");
			bw.write("@attribute 'PB1F2comp1A' real\n");
			bw.write("@attribute 'PB1F2comp3N' real\n");
			bw.write("@attribute 'PB1F2comp5C' real\n");
			bw.write("@attribute 'PB1F2comp8G' real\n");
			bw.write("@attribute 'PB1F2comp9H' real\n");
			bw.write("@attribute 'PB1F2comp19Y' real\n");
			bw.write("@attribute 'PB2charget1' real\n");
			bw.write("@attribute 'PB2charget3' real\n");
			bw.write("@attribute 'PB2charged14' real\n");
			bw.write("@attribute 'PB2solventc3' real\n");
			bw.write("@attribute 'PB2solventt2' real\n");
			bw.write("@attribute 'PB2solventt3' real\n");
			bw.write("@attribute 'PB2solventd3' real\n");
			bw.write("@attribute 'PB2solventd4' real\n");
			bw.write("@attribute 'PB2solventd13' real\n");
			bw.write("@attribute 'PB2comp1A' real\n");
			bw.write("@attribute 'PB2comp3N' real\n");
			bw.write("@attribute 'PB2comp4D' real\n");
			bw.write("@attribute 'PB2comp15P' real\n");
			bw.write("@attribute 'PB2comp16S' real\n");
			bw.write("@attribute 'PB2comp17T' real\n");
			bw.write("@attribute 'class' {0, 1}\n\n");
			bw.write("@data\n");
			bw.flush();
		}
	}

	private void writeCTD(BufferedWriter bw, int mode) throws Exception {
		double[] ctdArr;

		if (mode == 0)
			ctdArr = ctdProtArr;
		else
			ctdArr = ctdStrainArr;

		for (int i = 0; i < ctdArr.length; i++) {
			if (ctdArr[i] != -1)
				bw.write(ctdArr[i] + ",");
			else
				bw.write("?,"); // missing value
		}

		bw.write("?\n"); // unclassified
		bw.flush();
	}

	/**
	 * Takes a sequence and transform it into feature vector and finally write
	 * to an ARFF file
	 * 
	 * mode 0 - protein classification mode 1 - whole strain classification
	 * 
	 * @param filename
	 * @param mode
	 * @throws Exception
	 */
	void writeFile(String filename, int mode) {
		// create output file
		File f = new File(filename); // output ARFF file for transformed
										// sequence
		
		FileWriter fw;
		try {
			if (!f.exists()) {
				f.createNewFile();
			} 
			
			fw = new FileWriter(f, false);
			BufferedWriter bw = new BufferedWriter(fw);

			writeHeader(bw, mode);
			writeCTD(bw, mode);

			bw.write("%\n%\n%\n");
			bw.flush();
			bw.close();

		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
