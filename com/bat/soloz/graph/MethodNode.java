package com.bat.soloz.graph;

/**
 *
 * @author Zebulun Barnett
 */
import java.util.LinkedList;

public class MethodNode {

	private String fullyQualifiedMethodName;
	private int linesOfCode;
	private int numberOfCalls;
	private LinkedList<MethodNode> children;
	private String bodyText;
	private RecursivityType recursivity;

	public MethodNode(final String fullyQualifiedMethodName, final int linesOfCode, final String bodyText) {
		this(fullyQualifiedMethodName, linesOfCode, new LinkedList<MethodNode>(), bodyText,	RecursivityType.NotRecursive);
	}
	
	public MethodNode(final String fullyQualifiedMethodName,
			final int linesOfCode,
			final LinkedList<MethodNode> children,
			final String bodyText,
			final RecursivityType recursivity) {
		this.fullyQualifiedMethodName = fullyQualifiedMethodName;
		this.linesOfCode = linesOfCode;
		this.children = children;
		this.bodyText = bodyText;
		this.recursivity = recursivity;
		
		numberOfCalls = 0;
	}

	public void setChildren(final LinkedList<MethodNode> children) {
		this.children = children; // TODO: should they overwrite the old children or be added?
	}

	public void setRecursivity(final RecursivityType recursivity) {
		this.recursivity = recursivity;
	}
	
	public void incrementCallCount() {
		numberOfCalls++;
	}

	public RecursivityType getRecursivity() {
		return recursivity;
	}

	public LinkedList<MethodNode> getChildren() {
		return children;
	}
	
	public int getNumberOfCalls() {
		return numberOfCalls;
	}

	public void addChild(final MethodNode child) {
		if (children == null) {
			children = new LinkedList<>();
		}

		children.add(child);
	}

	public String getLongName() {
		return fullyQualifiedMethodName;
	}

	public String getShortName() {
		if (fullyQualifiedMethodName.contains(".")) {
			return fullyQualifiedMethodName.substring(fullyQualifiedMethodName.lastIndexOf(".") + 1);
		} else {
			return fullyQualifiedMethodName;
		}
	}

	@Override
	public String toString() {
		String summary = getLongName();

		if (children != null && children.size() > 0) {
			for (MethodNode child : children) {
				summary += "\n\t" + child.getLongName();
			}
		}

		return summary;
	}

	public int getLinesOfCode() {
		return linesOfCode;
	}

	public String getBodyText() {
		return bodyText;
	}
}