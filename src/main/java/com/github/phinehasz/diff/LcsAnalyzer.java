package com.github.phinehasz.diff;

import net.sourceforge.pmd.lang.ast.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.github.phinehasz.diff.JavaParser.substring;

/**
 * BASED ON LCS
 * @author zhhiyp
 * @version : LcsAnalyzer.java 2020-12-23 21:18
 */
public class LcsAnalyzer {

	// USE LCS TO CALCULATE !
	public static List<AstDiffNode> nodeListLcs(Node[] tpl, Node[] str, int strSt) {
		if (tpl.length == 0 && str.length == 0) {
			return new ArrayList<>();
		} else if (tpl.length == 0) {
			return new ArrayList<>(Collections.singletonList(new AstDiffNode(strSt, str.length, 2)));
		} else if (str.length == 0) {
			return new ArrayList<>(
					Collections.singletonList(new AstDiffNode(strSt, tpl.length, 3, tpl)));
		}

		int[][] dp = new int[tpl.length + 1][str.length + 1];
		int maxi, maxj, maxk;
		maxi = maxj = maxk = 0;
		for (int i = 1; i <= tpl.length; i++) {
			for (int j = 1; j <= str.length; j++) {
				if (JavaParser.equals(tpl[i - 1], str[i - 1])) {
					dp[i][j] = dp[i - 1][j - 1] + 1;
					if (dp[i][j] >= maxk) {
						maxk = dp[i][j];
						maxi = i;
						maxj = j;
					}
				}
			}
		}

		List<AstDiffNode> list = new ArrayList<>();
		if (maxk == 0) {
			list.add(new AstDiffNode(strSt, tpl.length, 3, tpl));
			list.add(new AstDiffNode(strSt, str.length, 2));
		} else {
			list.addAll(nodeListLcs(substring(tpl, 0, maxi - maxk), substring(str, 0, maxj - maxk), strSt));
			list.add(new AstDiffNode(maxj - maxk, maxk, 1));
			list.addAll(nodeListLcs(substring(tpl, maxi), substring(str, maxj), maxj));
		}
		return list;
	}

	public static class AstDiffNode {
		int startPosition;
		// sub tpl
		Node[] subTpl;
		int length;
		// operation. 1 is same, 2 is add, 3 is sub
		int op;

		public AstDiffNode(int st, int length, int op) {
			this.startPosition = st;
			this.length = length;
			this.op = op;
		}

		public AstDiffNode(int st, int length, int op, Node[] subTpl) {
			this.length = length;
			this.op = op;
			this.startPosition = st;
			this.subTpl = subTpl;
		}

		@Override
		public String toString() {
			return "AstDiffNode{" +
					"length=" + length +
					", st=" + startPosition +
					", op=" + op +
					'}';
		}
	}
}
