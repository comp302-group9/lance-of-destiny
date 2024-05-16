package PhysicsEngines;

public class CollisionInfo {
	
	

	
    private CollisionType collisionType;
    private Vector collisionNormal;

    public CollisionInfo(CollisionType collisionType, Vector collisionNormal) {
        this.collisionType = collisionType;
        this.collisionNormal = collisionNormal;
    }

    public CollisionType getCollisionType() {
        return collisionType;
    }

    public void setCollisionType(CollisionType collisionType) {
        this.collisionType = collisionType;
    }

    public Vector getCollisionNormal() {
        return collisionNormal;
    }

    public void setCollisionNormal(Vector collisionNormal) {
        this.collisionNormal = collisionNormal;
    }
}