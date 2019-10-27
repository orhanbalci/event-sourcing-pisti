package net.orhanbalci.pisti;

public enum PointType {
  DOUBLE_PISTI(20),
  PISTI(10),
  JACK(1),
  ACE(1),
  TWO_OF_CLUBS(2),
  TEN_OF_DIAMONDS(3),
  MAJORITY(3);
  private int points;

  private PointType(int points) {
    this.points = points;
  }

  public int getPoints() {
    return points;
  }
}
