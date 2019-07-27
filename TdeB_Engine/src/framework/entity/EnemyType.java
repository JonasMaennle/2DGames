package framework.entity;

public enum EnemyType {
	
	SPIDER(1.0f),
	GHOST(1.5f), 
	DIGGER(2f), 
	FLY(1.1f);

	private float possibilityFactor;
	
	EnemyType(float possibilityFactor) {
		this.possibilityFactor = possibilityFactor;
	}

	public float getPossibilityFactor() {
		return possibilityFactor;
	}
}
