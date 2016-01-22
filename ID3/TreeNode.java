import java.util.*;

public class TreeNode<T>
	{
	public T data;
	public TreeNode<T> parent;
	public List<TreeNode<T>> children;

	public TreeNode()
		{
		if(this.children==null)
			{
			this.children = new LinkedList<TreeNode<T>>();
			}
		}

	public TreeNode(T val)
		{
		this.data = val;
		}

	public void addChild(TreeNode<T> child)
		{
		this.children.add(child);
		}

	public T getData()
		{
		return this.data;
		}

	public void setData(T val)
		{
		this.data = val;
		}


	}