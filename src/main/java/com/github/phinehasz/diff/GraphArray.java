package com.github.phinehasz.diff;

/**
 * Set array in range of (-length, length).
 * @author zhhiyp
 */
class GraphArray {
  private int length;
  private int[] list;

  public GraphArray(int length) {
    this.length = length;
    this.list = new int[this.length * 2];
  }

  public int get(int pos) {
    return this.list[pos + this.length];
  }

  public void set(int pos, int value) {
    this.list[pos + this.length] = value;
  }
}
