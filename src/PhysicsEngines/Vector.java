package PhysicsEngines;

public class Vector {
	 private double x;
	 private double y;

	    public Vector(double x, double y) {
	        this.x = x;
	        this.y = y;
	    }

	    public double getX() {
	        return x;
	    }

	    public double getY() {
	        return y;
	    }

	    public double getMagnitude() {
	        return Math.sqrt(x * x + y * y);
	    }

	    public void setMagnitude(double magnitude) {
	        double currentMagnitude = getMagnitude();
	        if (currentMagnitude != 0) {
	            double scaleFactor = magnitude / currentMagnitude;
	            x *= scaleFactor;
	            y *= scaleFactor;
	        }
	    }

	    public Vector normalize() {
	        double magnitude = getMagnitude();
	        if (magnitude != 0) {
	            x /= magnitude;
	            y /= magnitude;
	        }
	        return this;
	    }

	    public void reflect(Vector normal) {
	        // Reflect the vector about the given normal vector
	        double dotProduct = dotProduct(normal);
	        x -= 2 * dotProduct * normal.getX();
	        y -= 2 * dotProduct * normal.getY();
	    }
	 // Method to add another vector to this vector
	    public Vector add(Vector other) {
	        double newX = this.x + other.getX();
	        double newY = this.y + other.getY();
	        return new Vector(newX, newY);
	    }

	    // Method to subtract another vector from this vector
	    public Vector subtract(Vector other) {
	        double newX = this.x - other.getX();
	        double newY = this.y - other.getY();
	        return new Vector(newX, newY);
	    }

	    public Vector projectOnto(Vector other) {
	        // Calculate the scalar projection of this vector onto the other vector
	        double scalarProjection = dotProduct(other) / other.getMagnitude();

	        // Calculate the projected vector
	        double projectedX = (scalarProjection * other.getX()) / other.getMagnitude();
	        double projectedY = (scalarProjection * other.getY()) / other.getMagnitude();

	        return new Vector(projectedX, projectedY);
	    }

	    // Method to multiply this vector by a scalar value
	    public Vector multiply(double scalar) {
	        return new Vector(x * scalar, y * scalar);
	    }
	    public double dotProduct(Vector other) {
	        return x * other.getX() + y * other.getY();
	    }

		public void setX(double d) {
			this.x = d;
			
		}
		public void setY(double d) {
			this.y = d;
			
		}
}
