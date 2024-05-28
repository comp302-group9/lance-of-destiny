package domain.controllers;

import java.awt.Rectangle;
import java.awt.geom.Area;
import java.util.ArrayList;

import domain.models.RunningModeModel;
import domain.objects.Fireball;
import domain.objects.GameObject;
import domain.objects.Paddle;
import domain.objects.Barrier.Barrier;

public class CollisionHandler {

    private static CollisionHandler instance;

    private CollisionHandler() {}

    public static CollisionHandler getInstance() {
        if (instance == null) {
            instance = new CollisionHandler();
        }
        return instance;
    }

    public static boolean CollisionCheck(GameObject obj1, GameObject obj2) {
        Area area1 = new Area(obj1.getBounds());
        Area area2 = new Area(obj2.getBounds());
        area1.intersect(area2);
        return !area1.isEmpty();
    }

    public static void reflectFromPaddle(Fireball fireball, Paddle paddle) {
        double dx = fireball.getVelocityX();
        double dy = fireball.getVelocityY();

        double v_ball_initial = Math.sqrt(dx * dx + dy * dy);
        double theta_wall = Math.toRadians(paddle.getRotationAngle());
        double theta_ball = Math.atan2(dy, dx);
        double alpha = theta_wall - theta_ball;

        dx = v_ball_initial * Math.cos(theta_wall + alpha);
        dy = v_ball_initial * Math.sin(theta_wall + alpha);

        if ((dx < 0 && paddle.getDirection() > 0) || (dx > 0 && paddle.getDirection() < 0)) {
            dx *= -1;
        }

        fireball.setVelocity(dx, dy);
    }

    public static void validateSpeed(Fireball fireball, Paddle paddle) {
        double dx = fireball.getVelocityX();
        double dy = fireball.getVelocityY();

        float increase = 5 / 64.0f;
        if ((dx > 0 && paddle.getDirection() > 0) || (dx < 0 && paddle.getDirection() < 0)) {
            double currentSpeed = Math.sqrt(dx * dx + dy * dy);
            double ratio = (currentSpeed + increase) / currentSpeed;

            fireball.setVelocity(dx * ratio, dy * ratio);
        }
    }

    public static void checkWallCollision(Fireball fireball, int screenWidth, int screenHeight) {
        int x = fireball.getX();
        int y = fireball.getY();
        int width = fireball.getWidth();
        int height = fireball.getHeight();

        if (x <= 0 || x + width >= screenWidth) {
            fireball.reflectVertical();
        }
        if (y <= 0) {
            fireball.reflectHorizontal();
        }
    }
}

