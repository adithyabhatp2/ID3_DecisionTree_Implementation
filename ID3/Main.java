import weka.core.*;
import weka.core.converters.ArffLoader.ArffReader;
import java.io.*;
import java.util.*;

public class Main
	{

	public static void main(String[] args)
		{
		try
			{

			ID3Generator id3Gen = new ID3Generator();

			int m = 0;
			double samplePercent = 0;
			int sampleNum;
			String dataSet;

			FileWriter outFW = null;
			String arffTrainFilePath = null;
			String arffTestFilePath = null;
			String sampleFilePath = null;

			int mode = args.length;

			// mode = -1;
			//
			// if(mode==-1)
			// {
			// m = 2;
			// dataSet = "heart";
			// outFW = new FileWriter("output/" + dataSet + "_" + m + ".txt");
			// arffTrainFilePath = "input/" + dataSet + "_train.arff";
			// arffTestFilePath = "input/" + dataSet + "_test.arff";
			// }
			//
			// else if(mode == 0)
			// {
			// m = 2;
			// samplePercent = 0.05;
			// sampleNum = 10;
			// dataSet = "heart";
			// // String dataSet = "diabetes";
			//
			// outFW = new FileWriter("output/" + dataSet + "_" + m + "_" +
			// samplePercent + "_" + sampleNum + ".txt");
			// arffTrainFilePath = "input/" + dataSet + "_train.arff";
			// arffTestFilePath = "input/" + dataSet + "_test.arff";
			// sampleFilePath = "./samples/" + dataSet + "_" + m +
			// "_train_sample_" + samplePercent + "_" + sampleNum + ".txt";
			// }
			// else
			if(mode == 3)
				{
				outFW = new FileWriter(args[0] + ".out.txt");
				arffTrainFilePath = args[0];
				arffTestFilePath = args[1];
				m = Integer.parseInt(args[2]);
				}
			else // Sample + test
				{
				// if(mode !=4)
				// {
				System.out.println("Correct Usage : ");
				System.out.println("arffTrainFilePath arffTrainFilePath m");
//				System.out.println("m samplePercent sampleNum dataSet");
				System.exit(-1);
				// }
				// m = Integer.parseInt(args[0]);
				// samplePercent = Double.parseDouble(args[1]);
				// sampleNum = Integer.parseInt(args[2]);
				// dataSet = args[3];
				//
				// outFW = new FileWriter("output/" + dataSet + "_" + m + "_" +
				// samplePercent + "_" + sampleNum + ".txt");
				// arffTrainFilePath = "input/" + dataSet + "_train.arff";
				// arffTestFilePath = "input/" + dataSet + "_test.arff";
				// sampleFilePath = "./samples/" + dataSet + "_" + m +
				// "_train_sample_" + samplePercent + "_" + sampleNum + ".txt";

				}

			Instances trainData = readInputFromArff(arffTrainFilePath);
			Instances testData = readInputFromArff(arffTestFilePath);

			ArrayList<Attribute> attributesList = (ArrayList<Attribute>) Collections.list(trainData.enumerateAttributes());
			attributesList.remove(trainData.classAttribute());

			if(mode == 4)
				{
				trainData = createRandomSample(trainData, (int) (samplePercent * trainData.numInstances()));
				BufferedWriter writer = new BufferedWriter(new FileWriter(sampleFilePath));
				writer.write(trainData.toString());
				writer.close();
				}

			TreeNode<Id3Node> root = id3Gen.generateID3Tree(trainData, attributesList, m, null);
			id3Gen.recPrintID3Tree(root, outFW, 0);

			ID3Predictor id3Pred = new ID3Predictor();
			id3Pred.printPredictions(testData, outFW, root);

			outFW.flush();
			outFW.close();
			}
		catch(IOException e)
			{
			// TODO Auto-generated catch block
			e.printStackTrace();
			}

		}

	// Read input from ARFF file
	public static Instances readInputFromArff(String arffInFilePath)
		{
		Instances data = null;

		try
			{
			BufferedReader reader = new BufferedReader(new FileReader(arffInFilePath));
			ArffReader arff = new ArffReader(reader);

			data = arff.getData();
			data.setClassIndex(data.numAttributes() - 1);

			reader.close();
			}
		catch(IOException e)
			{
			e.printStackTrace();
			}
		finally
			{

			}
		return data;
		}

	public static Instances createRandomSample(Instances sourceInsts, int size)
		{
		System.out.println("Beginning Sampling : " + size);
		Instances sampled = new Instances(sourceInsts, size);
		Random r = new Random();
		for (int i = 0;i < size;i++)
			{
			int ind = r.nextInt(sourceInsts.numInstances());
			sampled.add(sourceInsts.instance(ind));
			sourceInsts.delete(ind);
			}

		return sampled;
		}

	}
