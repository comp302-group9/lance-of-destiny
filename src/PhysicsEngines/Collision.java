package PhysicsEngines;

import java.util.ArrayList;
import java.util.List;
import domain.objects.Barrier.*;
import domain.objects.*;

public class Collision {

    // Method to perform broad-phase collision detection
    public static List<PhysicsObject[]> broadPhaseCollision(List<PhysicsObject> objects) {
        List<PhysicsObject[]> potentialCollisions = new ArrayList<>();

        // Perform broad-phase collision detection (e.g., using spatial partitioning)
        // For example, you can use a quadtree or spatial hashing to partition the space and quickly discard distant objects

        // For simplicity, let's assume all objects can potentially collide with each other
        for (int i = 0; i < objects.size(); i++) {
            for (int j = i + 1; j < objects.size(); j++) {
                potentialCollisions.add(new PhysicsObject[]{objects.get(i), objects.get(j)});
            }
        }

        return potentialCollisions;
    }

    // Method to perform narrow-phase collision detection
    public static void narrowPhaseCollision(List<PhysicsObject[]> potentialCollisions) {
        // Iterate through potential collisions and perform narrow-phase collision detection
        for (PhysicsObject[] pair : potentialCollisions) {
            PhysicsObject objA = pair[0];
            PhysicsObject objB = pair[1];
           // Class a = objA.getClass();
           // Class b= objB.getClass();
            
            // Determine the types of objects and use the appropriate collision detection method
            if ((objA instanceof Fireball && objB instanceof Paddle)||(objB instanceof Fireball && objA instanceof Paddle)) {
                if (!detectFireballPaddleCollision((Fireball) objA, (Paddle) objB).getCollisionType().equals(CollisionType.NONE)) {
                    System.out.println("I am here (paddle-fireball\n");
                	handleFireballPaddleCollision((Fireball) objA, (Paddle) objB);
                }
            } else if ((objA instanceof Fireball && objB instanceof Barrier)||(objB instanceof Fireball && objA instanceof Barrier)) {
                if (!detectFireballBarrierCollision((Fireball) objB, (Barrier) objA).getCollisionType().equals(CollisionType.NONE)) {
                	System.out.println("I am here (barrier-fireball\n");
                	handleFireballBarrierCollision((Fireball) objB, (Barrier) objA);
                }
            }
           /* else if (objA instanceof Spell && objB instanceof Paddle) {
                if (detectSpellPaddleCollision((Spell) objA, (Paddle) objB)) {
                    handleSpellPaddleCollision((Spell) objA, (Paddle) objB);
                }
            } else if (objA instanceof Barrier && objB instanceof Barrier) {
                if (detectBarrierBarrierCollision((Barrier) objA, (Barrier) objB)) {
                    handleBarrierBarrierCollision((Barrier) objA, (Barrier) objB);
                }*/
            }// Add more cases for other types of objects as needed
        }
    

    // Method to detect collision between Fireball and Paddle
   /* private static CollisionInfo detectFireballPaddleCollision(Fireball fireball, Paddle paddle) {
        // Get the position and dimensions of the fireball
        Vector fireballPosition = fireball.getPosition();
        int fireballRadius = fireball.getRadius();

        // Get the position and dimensions of the paddle
        Vector paddlePosition = paddle.getPosition();
        int paddleWidth = paddle.getWidth();
        int paddleHeight = paddle.getHeight();

        // Calculate the minimum and maximum coordinates of the paddle
        int paddleMinX = (int) paddlePosition.getX();
        int paddleMaxX = (int) (paddlePosition.getX() + paddleWidth);
        int paddleMinY = (int) paddlePosition.getY();
        int paddleMaxY = (int) (paddlePosition.getY() + paddleHeight);

        // Calculate the closest point on the paddle to the fireball
        int closestX = clamp((int) fireballPosition.getX(), paddleMinX, paddleMaxX);
        int closestY = clamp((int)fireballPosition.getY(), paddleMinY, paddleMaxY);

        // Calculate the distance between the fireball and the closest point on the paddle
        double distance = Math.sqrt(Math.pow(fireballPosition.getX() - closestX, 2) + Math.pow(fireballPosition.getY() - closestY, 2));

        // Check if the fireball is colliding with the paddle
        boolean isColliding = isCollidingWithBox(fireballPosition, closestX, closestY, fireballRadius);

        if (isColliding) {
            // Determine if it's a corner or side collision
            boolean isCornerCollision = isCornerCollision(fireballPosition, closestX, closestY,paddle);
            Vector collisionNormal = calculateCollisionNormal(fireballPosition, closestX, closestY);

            // Store the collision type for later use
            return new CollisionInfo(isCornerCollision ? CollisionType.CORNER : CollisionType.SIDE, collisionNormal);
        }

        return new CollisionInfo(CollisionType.NONE, new Vector(0,0));
    }*/

    // Helper method to clamp a value within a range
    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    // Helper method to calculate the collision normal vector
    private static Vector calculateCollisionNormal(Vector fireballPosition, int closestX, int closestY) {
        // Calculate the collision normal vector based on the position of the fireball and the closest point on the paddle
        int normalX = (int)fireballPosition.getX() - closestX;
        int normalY = (int)fireballPosition.getY() - closestY;
        return new Vector(normalX, normalY).normalize();
    }
    private static Vector calculateCornerCollisionNormal(Vector fireballPosition, int closestX, int closestY, PhysicsObject box) {
        // Calculate the difference between the fireball's position and the closest point on the box
        double deltaX = Math.abs(fireballPosition.getX() - closestX);
        double deltaY = Math.abs(fireballPosition.getY() - closestY);

        // Determine the sign of the normal components based on the box's position relative to the fireball
        int normalXSign = (int) Math.signum(fireballPosition.getX() - closestX);
        int normalYSign = (int) Math.signum(fireballPosition.getY() - closestY);

        // Calculate the collision normal vector for a corner collision
        int normalX = (int) (normalXSign * deltaX);
        int normalY = (int) (normalYSign * deltaY);

        return new Vector(normalX, normalY).normalize();
    }

    // Method to calculate collision normal for side collision
    private static Vector calculateSideCollisionNormal(Vector fireballPosition, int closestX, int closestY, PhysicsObject box) {
        // Calculate the collision normal vector for a side collision
        int normalX = closestX - (int) fireballPosition.getX();
        int normalY = closestY - (int) fireballPosition.getY();

        return new Vector(normalX, normalY).normalize();
    }
    // Method to handle collision between Fireball and Paddle
    private static void handleFireballPaddleCollision(Fireball fireball, Paddle paddle) {
        // Get the collision info
        CollisionInfo collisionInfo = detectFireballPaddleCollision(fireball, paddle);

        // Get the collision type
        CollisionType collisionType = collisionInfo.getCollisionType();
        Vector objectMovementDirection = paddle.getVelocity();
        // Handle different collision types
        switch (collisionType) {
        
            case CORNER:
                // Implement corner collision resolution logic
                // For example, reflect the fireball velocity based on the collision normal
                Vector collisionNormal = collisionInfo.getCollisionNormal();
                System.out.printf("Paddle Corner Normal Vector: %f %f \n", collisionNormal.getX(), collisionNormal.getY());
                fireball.reflect(collisionNormal,objectMovementDirection);
                break;
            case SIDE:
                // Implement side collision resolution logic
                // For example, reflect the fireball velocity based on the collision normal
                Vector sideCollisionNormal = collisionInfo.getCollisionNormal();
                System.out.printf("Paddle Side Normal Vector: %f %f \n", sideCollisionNormal.getX(), sideCollisionNormal.getY());
                fireball.reflect(sideCollisionNormal,objectMovementDirection);
                break;
            case NONE:
                // No collision detected, do nothing
                break;
        }
    }
    private static CollisionInfo detectFireballPaddleCollision(Fireball fireball, Paddle paddle) {
        // Get the position and dimensions of the fireball
        Vector fireballPosition = fireball.getPosition();
        int fireballRadius = fireball.getRadius();

        // Get the position and dimensions of the paddle
        Vector paddlePosition = paddle.getPosition();
        int paddleWidth = paddle.getWidth();
        int paddleHeight = paddle.getHeight();

        // Calculate the minimum and maximum coordinates of the paddle
        int paddleMinX = (int) paddlePosition.getX();
        int paddleMaxX = (int) (paddlePosition.getX() + paddleWidth);
        int paddleMinY = (int) paddlePosition.getY();
        int paddleMaxY = (int) (paddlePosition.getY() + paddleHeight);

        // Calculate the closest point on the paddle to the fireball
        int closestX = clamp((int) fireballPosition.getX(), paddleMinX, paddleMaxX);
        int closestY = clamp((int) fireballPosition.getY(), paddleMinY, paddleMaxY);

        // Check if the fireball is colliding with the paddle
        boolean isColliding = isCollidingWithBox(fireballPosition, closestX, closestY, fireballRadius);

        if (isColliding) {
            // Determine if it's a corner or side collision
            boolean isCornerCollision = isCornerCollision(fireballPosition, closestX, closestY, paddle);
            Vector collisionNormal;
            if (isCornerCollision) {
                // Calculate collision normal for corner collision
                collisionNormal = calculateCornerCollisionNormal(fireballPosition, closestX, closestY, paddle);
            } else {
                // Calculate collision normal for side collision
                collisionNormal = calculateSideCollisionNormal(fireballPosition, closestX, closestY, paddle);
            }

            // Store the collision type and normal for later use
            return new CollisionInfo(isCornerCollision ? CollisionType.CORNER : CollisionType.SIDE, collisionNormal);
        }

        return new CollisionInfo(CollisionType.NONE, new Vector(0, 0));
    }
    

    /*private static CollisionInfo detectFireballBarrierCollision(Fireball fireball, Barrier barrier) {
        // Get the position and dimensions of the fireball
        Vector fireballPosition = fireball.getPosition();
        int fireballRadius = fireball.getRadius();

        // Get the position and dimensions of the barrier
        Vector barrierPosition = barrier.getPosition();
        int barrierWidth = barrier.getWidth();
        int barrierHeight = barrier.getHeight();

        // Calculate the minimum and maximum coordinates of the barrier
        int barrierMinX = (int) barrierPosition.getX();
        int barrierMaxX = (int) barrierPosition.getX() + barrierWidth;
        int barrierMinY = (int) barrierPosition.getY();
        int barrierMaxY = (int) barrierPosition.getY() + barrierHeight;

        // Calculate the closest point on the barrier to the fireball
        int closestX = clamp( (int) fireballPosition.getX(), barrierMinX, barrierMaxX);
        int closestY = clamp( (int)fireballPosition.getY(), barrierMinY, barrierMaxY);

        // Check if the fireball is colliding with the barrier
        boolean isColliding = isCollidingWithBox(fireballPosition, closestX, closestY, fireballRadius);

        if (isColliding) {
            // Determine if it's a corner or side collision
            boolean isCornerCollision = isCornerCollision(fireballPosition, closestX, closestY, barrier);
            Vector collisionNormal = calculateCollisionNormal(fireballPosition, closestX, closestY);

            // Store the collision type for later use
            return new CollisionInfo(isCornerCollision ? CollisionType.CORNER : CollisionType.SIDE, collisionNormal);
        }

        return new CollisionInfo(CollisionType.NONE, new Vector(0,0));
    }*/
    private static CollisionInfo detectFireballBarrierCollision(Fireball fireball, Barrier barrier) {
        // Get the position and dimensions of the fireball
        Vector fireballPosition = fireball.getPosition();
        int fireballRadius = fireball.getRadius();

        // Get the position and dimensions of the barrier
        Vector barrierPosition = barrier.getPosition();
        int barrierWidth = barrier.getWidth();
        int barrierHeight = barrier.getHeight();

        // Calculate the minimum and maximum coordinates of the barrier
        int barrierMinX = (int) barrierPosition.getX();
        int barrierMaxX = (int) (barrierPosition.getX() + barrierWidth);
        int barrierMinY = (int) barrierPosition.getY();
        int barrierMaxY = (int) (barrierPosition.getY() + barrierHeight);

        // Calculate the closest point on the barrier to the fireball
        int closestX = clamp((int) fireballPosition.getX(), barrierMinX, barrierMaxX);
        int closestY = clamp((int) fireballPosition.getY(), barrierMinY, barrierMaxY);

        // Check if the fireball is colliding with the barrier
        boolean isColliding = isCollidingWithBox(fireballPosition, closestX, closestY, fireballRadius);

        if (isColliding) {
            // Determine if it's a corner or side collision
            boolean isCornerCollision = isCornerCollision(fireballPosition, closestX, closestY, barrier);
            Vector collisionNormal;
            if (isCornerCollision) {
                // Calculate collision normal for corner collision
                collisionNormal = calculateCornerCollisionNormal(fireballPosition, closestX, closestY, barrier);
            } else {
                // Calculate collision normal for side collision
                collisionNormal = calculateSideCollisionNormal(fireballPosition, closestX, closestY, barrier);
            }

            // Store the collision type and normal for later use
            return new CollisionInfo(isCornerCollision ? CollisionType.CORNER : CollisionType.SIDE, collisionNormal);
        }

        return new CollisionInfo(CollisionType.NONE, new Vector(0, 0));
    }

    // Helper method to check if the fireball is colliding with the barrier/paddle
    private static boolean isCollidingWithBox(Vector fireballPosition, int closestX, int closestY, int fireballRadius) {
        // Calculate the distance between the fireball and the closest point on the barrier
        double distance = Math.sqrt(Math.pow(fireballPosition.getX() - closestX, 2) + Math.pow(fireballPosition.getY() - closestY, 2));

        // Check if the distance is less than or equal to the fireball's radius
        return distance <= fireballRadius;
    }
    // Helper method to check if the fireball is colliding with the barrier
   /* private static boolean isCollidingWithPaddle(Vector fireballPosition, int closestX, int closestY, int fireballRadius) {
        // Calculate the distance between the fireball and the closest point on the barrier
        double distance = Math.sqrt(Math.pow(fireballPosition.getX() - closestX, 2) + Math.pow(fireballPosition.getY() - closestY, 2));

        // Check if the distance is less than or equal to the fireball's radius
        return distance <= fireballRadius;
    }*/

    // Helper method to determine if it's a corner collision or a side collision
    private static boolean isCornerCollision(Vector fireballPosition, int closestX, int closestY, PhysicsObject box) {
        // Calculate the difference between the fireball's position and the closest point on the barrier
        double deltaX = Math.abs(fireballPosition.getX() - closestX);
        double deltaY = Math.abs(fireballPosition.getY() - closestY);

        // Check if it's a corner collision based on the difference
        return deltaX == box.getWidth() / 2 && deltaY == box.getHeight() / 2;
    }

    // Method to handle collision between Fireball and Barrier
    private static void handleFireballBarrierCollision(Fireball fireball, Barrier barrier) {
    	 CollisionInfo collisionInfo = detectFireballBarrierCollision(fireball, barrier);
    	 //System.out.printf("Velocity before collision: %f %f \n", fireball.getVelocity().getX(),fireball.getVelocity().getY());
         // Get the collision type
         CollisionType collisionType = collisionInfo.getCollisionType();
         Vector objectMovementDirection = barrier.getVelocity();
         // Handle different collision types
         switch (collisionType) {
         
             case CORNER:
                 // Implement corner collision resolution logic
                 // For example, reflect the fireball velocity based on the collision normal
                 Vector collisionNormal = collisionInfo.getCollisionNormal();
                 System.out.printf("Barrier Corner Normal Vector: %f %f \n", collisionNormal.getX(), collisionNormal.getY());
                 
                 fireball.reflect(collisionNormal,objectMovementDirection);
                 break;
             case SIDE:
                 // Implement side collision resolution logic
                 // For example, reflect the fireball velocity based on the collision normal
                 Vector sideCollisionNormal = collisionInfo.getCollisionNormal();
                 System.out.printf("Barrier Side Normal Vector: %f %f \n", sideCollisionNormal.getX(), sideCollisionNormal.getY());
                 
                 fireball.reflect(sideCollisionNormal,objectMovementDirection);
                 break;
             case NONE:
                 // No collision detected, do nothing
                 break;
         }
         //System.out.printf("Velocity after collision: %f %f \n", fireball.getVelocity().getX(),fireball.getVelocity().getY());
    }
}
