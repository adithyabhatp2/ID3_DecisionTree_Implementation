import java.text.DecimalFormat;
import java.util.*;
import weka.core.*;

public class Id3Node
	{

	int debugOn = 0;

	public Instances instances;
	public ArrayList<Attribute> atttributes;

	public Attribute parentAttr;
	public Object parentAttrVal;
	public String parentAttrCompOp;

	public boolean isLeaf;
	public String leafClass;

	public int numPos;
	public int numNeg;

	public Id3Node(Instances insts, ArrayList<Attribute> attributes)
		{
		this.instances = insts;
		this.computeNumPosNeg();
		this.atttributes = attributes;
		}

	public void computeNumPosNeg()
		{
		if(debugOn > 1)
			{
			System.out.println("Computing Pos Neg");
			}
		numPos = 0;
		numNeg = 0;
		for (int i = 0;i < instances.numInstances();i++)
			{
			Instance inst = instances.instance(i);
			if(inst.value(inst.classIndex()) == 0)
				{
				this.numNeg++;
				}
			else if(inst.value(inst.classIndex()) == 1)
				{
				this.numPos++;
				}
			else
				{
				if(debugOn > 1)
					{
					System.out.print("\n\nError in reading class value : ");
					System.out.println(inst.stringValue(inst.classIndex()));
					}
				}
			}
		if(debugOn >= 1)
			{
			System.out.println("Neg: " + numNeg + " Pos: " + numPos);
			}
		}

	public void makeLeaf(String classVal)
		{
		this.leafClass = classVal;
		this.isLeaf = true;
		}

	public String toString()
		{
		String displayString = new String();

		String displayedCompVal = (parentAttr.isNominal() == true)? (String) this.parentAttrVal : 
		 new DecimalFormat("###0.000000")
		 .format((double) this.parentAttrVal);

		displayString = parentAttr.name() + " " + this.parentAttrCompOp + " " + displayedCompVal + " ";
		displayString += "[" + numNeg + " " + numPos + "]";

		if(isLeaf)
			displayString += ": " + leafClass;

		return displayString;
		}

	}
