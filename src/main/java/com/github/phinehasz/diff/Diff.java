package com.github.phinehasz.diff;

import net.sourceforge.pmd.lang.java.ast.Token;

import java.util.List;

/**
 * Diff result model
 * @author zhhiyp
 */
public class Diff {
	private Token[] oldLines;
	private Token[] newLines;
	private ChangeLine changeLine = new ChangeLine();

	public Diff(Token[] oldTokens, Token[] newTokens) {
		this.oldLines = oldTokens;
		this.newLines = newTokens;
	}

	public ChangeLine getChangeLine() {
		makeDiff();
		return changeLine;
	}
	/**
	 * Show diff in the console between oldString and new String.
	 */
	private void makeDiff() {
		Myers myers = new Myers(oldLines, newLines);
		myers.diff();

		List<Node> path = myers.getPath();

		this.collectDiffLine(oldLines, newLines, path);
	}

	private void collectDiffLine(Token[] oldLines, Token[] newLines, List<Node> path) {
		Node prevNode = path.get(0);
		for (int i = 1; i < path.size(); i++) {
			Node node = path.get(i);
			int gapDis = (node.getCoordinateX() - node.getCoordinateY())
					- (prevNode.getCoordinateX() - prevNode.getCoordinateY());

			int posX = prevNode.getCoordinateX();
			int posY = prevNode.getCoordinateY();

			if (gapDis == 1) {
				// right
				Token deleteLine = oldLines[prevNode.getCoordinateX()];
				posX++;
				changeLine.getDeletedLines().add(deleteLine.beginLine);
			} else if (gapDis == -1) {
				// down
				Token newLine = newLines[prevNode.getCoordinateY()];
				posY++;
				changeLine.getAddedLines().add(newLine.beginLine);
			}

			for (; posX < node.getCoordinateX() && posY < node.getCoordinateY()
					&& posX < oldLines.length && posY < newLines.length; posX++, posY++) {
				//same
			}

			prevNode = node;
		}
	}

}
