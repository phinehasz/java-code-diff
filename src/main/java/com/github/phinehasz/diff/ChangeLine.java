package com.github.phinehasz.diff;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhhiyp
 * @version : ChangeLine.java 2020-12-24 22:29
 */
public class ChangeLine {

	private List<Integer> deletedLines = new ArrayList<>();
	private List<Integer> addedLines = new ArrayList<>();

	public List<Integer> getDeletedLines() {
		return deletedLines;
	}

	public List<Integer> getAddedLines() {
		return addedLines;
	}

	@Override
	public String toString() {
		return "ChangeLine{" +
				"deletedLines=" + deletedLines +
				", addedLines=" + addedLines +
				'}';
	}
}
