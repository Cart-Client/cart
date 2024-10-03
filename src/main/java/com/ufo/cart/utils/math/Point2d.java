package com.ufo.cart.utils.math;

import org.joml.Vector2d;
import org.joml.Vector2f;

public class Point2d {
    public double x;
    public double y;
    public Point2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Point2d{" + this.x + ", " + this.y + "}";
    }

    public Vector2d toVec2d() {
        return new Vector2d(this.x, this.y);
    }

    public void subtract(Point2d min) {
        this.x = this.x - min.x;
        this.y = this.y - min.y;
    }

    public Point2d _subtract(Point2d min) {
        double sx = this.x - min.x;
        double sy = this.y - min.y;
        return new Point2d(sx, sy);
    }

    public void difference(Point2d min) {
        this.x = Math.abs(this.x - min.x);
        this.y = Math.abs(this.y - min.y);
    }

    public Point2d _difference(Point2d min) {
        double sx = Math.abs(this.x - min.x);
        double sy = Math.abs(this.y - min.y);
        return new Point2d(sx, sy);
    }

    public void divide(Point2d min) {
        this.x = this.x / min.x;
        this.y = this.y / min.y;
    }

    public Point2d _divide(Point2d min) {
        double sx = this.x / min.x;
        double sy = this.y / min.y;
        return new Point2d(sx, sy);
    }

    public void multiply(Point2d min) {
        this.x = this.x / min.x;
        this.y = this.y / min.y;
    }

    public Point2d _multiply(Point2d min) {
        double sx = this.x / min.x;
        double sy = this.y / min.y;
        return new Point2d(sx, sy);
    }

    public void add(Point2d min) {
        this.x = this.x + min.x;
        this.y = this.y + min.y;
    }

    public Point2d _add(Point2d min) {
        double sx = this.x + min.x;
        double sy = this.y + min.y;
        return new Point2d(sx, sy);
    }

    public Point2d newPos() {
        return new Point2d(this.x, this.y);
    }
}
