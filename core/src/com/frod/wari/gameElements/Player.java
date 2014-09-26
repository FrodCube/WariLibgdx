package com.frod.wari.gameElements;


public enum Player {
	PLAYER1, PLAYER2;

	private Hole myHole;

	public int firstHole() {
		if (this == PLAYER1) {
			return 0;
		}

		return Table.HOLES_PER_PLAYER;
	}

	public void setHole(Hole h) {
		myHole = h;
	}

	public int lastHole() {
		return firstHole() + Table.HOLES_PER_PLAYER;
	}

	public boolean isMyHole(int n) {
		return firstHole() <= n && n < lastHole();
	}

	public void addScore(int n) {
		myHole.addSeeds(n, true);
	}

	public int getScore() {
		return myHole.getSeeds();
	}

	public Player getOpponent() {
		if (this == PLAYER1) {
			return PLAYER2;
		}

		return PLAYER1;
	}
}