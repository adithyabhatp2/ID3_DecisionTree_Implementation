import java.io.FileWriter;
import java.io.IOException;
import weka.core.*;

public class ID3Predictor
	{

	int debugLevel = 0;

	public String predictClassForInstance(Instance testInst, TreeNode<Id3Node> root)
		{
		String classPrediction = "";

		if(root.children.isEmpty())
			{
			if(!root.data.isLeaf)
				root.children.get(0); // TODO to throw exception if we mess up
			else
				{
				classPrediction = root.data.leafClass;
				return classPrediction;
				}
			}
		else
			{
			for (TreeNode<Id3Node> child : root.children)
				{
				Attribute testAttr = child.data.parentAttr;
				if(testAttr.isNominal())
					{
					String childVal = (String) child.data.parentAttrVal;
					String instVal = testInst.stringValue(testAttr);
					if(instVal.equalsIgnoreCase(childVal))
						{
						if(debugLevel >= 2)
							{
							System.out.println("ParentAttr: " + testAttr.name() + " ParentVal: " + childVal + " InstVal: " + instVal);
							}
						classPrediction = predictClassForInstance(testInst, child);
						break;
						}
					}
				else if(testAttr.isNumeric())
					{
					double childVal = (double) child.data.parentAttrVal;
					double instVal = testInst.value(testAttr);
					if(child.data.parentAttrCompOp.contains(">"))
						{
						if(instVal > childVal)
							{
							if(debugLevel >= 2)
								{
								System.out.println("ParentAttr: " + testAttr.name() + " compOp " + child.data.parentAttrCompOp + " compVal: " + childVal + " instVal: " + instVal);
								}
							classPrediction = predictClassForInstance(testInst, child);
							break;
							}
						}
					else if(child.data.parentAttrCompOp.contains("<="))
						{
						if(instVal <= childVal)
							{

							if(debugLevel >= 2)
								{
								System.out.println("ParentAttr: " + testAttr.name() + " compOp " + child.data.parentAttrCompOp + " compVal: " + childVal + " instVal: " + instVal);
								}
							classPrediction = predictClassForInstance(testInst, child);
							break;
							}
						}
					}
				}
			if(classPrediction.length() < 1)
				{
				System.err.println("ERROR : ID3PRediction ... no value / comp Op found!!");
				}
			}

		if(debugLevel >= 1)
		{
		System.out.println("PREDICTION : " + classPrediction+"\r\n");
		}
		return classPrediction;
		}

	public void printPredictions(Instances testData, FileWriter outFW, TreeNode<Id3Node> root) throws IOException
		{
		System.out.print("\r\n<Predictions for the Test Set Instances>\r\n");
		outFW.write("\r\n<Predictions for the Test Set Instances>\r\n");
		outFW.flush();
		int numCorrect = 0;
		for (int i = 0;i < testData.numInstances();i++)
			{
			Instance testInst = testData.instance(i);
			String predictedClass = predictClassForInstance(testInst, root);
			String actualClass = testInst.stringValue(testInst.classAttribute());
			if(predictedClass.trim().equalsIgnoreCase(actualClass.trim()))
				{
				numCorrect++;
				}
			outFW.write(i+1 + ": Actual: " + actualClass + " Predicted: " + predictedClass + "\r\n");
			System.out.print(i+1 + ": Actual: " + actualClass + " Predicted: " + predictedClass + "\r\n");
			}
		outFW.write("Number of correctly classified: " + numCorrect + " Total number of test instances: " + testData.numInstances());
		System.out.println("Number of correctly classified: " + numCorrect + " Total number of test instances: " + testData.numInstances());
		}

	}
