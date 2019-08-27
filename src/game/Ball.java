package game;

import processing.core.PApplet;
import processing.core.PImage;

public class Ball {
	static GameManager gm = GameManager.gm;
	
	public Transform transform;
	public Vector velocity;
	public static float friction = 0.99f;
	public float angularVelocity;
	static String ballPath = "data/cannonBall.png";
	private PImage cannonBall;
	static public Vector gravity = Vector.mul(Vector.down(), 1f/2000f*Transform.UPH);
	
	/** Constructor for a ball */
	Ball(Vector pos, Vector scale, float angle, Vector vel, float angularVel) {
		transform = new Transform(pos, scale, angle);
		velocity = vel;
		angularVelocity = angularVel;
		this.cannonBall = gm.loadImage(ballPath);
	}
	
	/** Constructor for a ball */
	Ball(Vector pos, Vector vel, float angularVel) {
		this(pos, Vector.one(), 0f, vel, angularVel);
	}

	void update() {
		
		//changing position and speed
		velocity.mul(friction);
		velocity.add(gravity);
		/*if (Math.abs(velocity.x) + Math.abs(velocity.y) < 0.1) {
			velocity.mul(0);
			rotation = 0;
			
		} */
		transform.position.add(velocity);
		transform.rotation += angularVelocity;
		
		/** wall bounds */
		if (transform.position.x < 0 + AngleCollision()) {
			velocity.x *= -1;
			transform.position.x = 0 + AngleCollision();
			doCollision();
		}
		if (transform.position.x > Transform.UPW - AngleCollision()) {
			velocity.x *= -1;
			transform.position.x = Transform.UPW - AngleCollision();
			doCollision();
		}
		if (transform.position.y < 0 + AngleCollision()) {
			velocity.y *= -1;
			transform.position.y = 0 + AngleCollision();
			doCollision();
		}
		if (transform.position.y > Transform.UPH - AngleCollision()) {
			velocity.y *= -1;
			transform.position.y = Transform.UPH - AngleCollision();
			doCollision();
		}
		
		//Drawing the ball and rotation
		gm.imageMode(PApplet.CENTER);
		cannonBall.resize((int) transform.toScreenScale().x, (int) -transform.toScreenScale().y);
		gm.pushMatrix();
		gm.translate(transform.toScreenPoint().x, transform.toScreenPoint().y);
		gm.rotate(transform.rotation);
		gm.image(cannonBall,0,0);
		gm.popMatrix();
	}

	void doCollision () {
		angularVelocity *= (float) -Math.cos(transform.rotation);
	}
	
	/** makes the hitbox as a function of the angle */
	float AngleCollision() {
		return (float) (-Math.abs (Math.sin (transform.rotation * 2 + Math.PI / 2)) * 0.4 + 1.4) * transform.scale.x/2f;
	}
}