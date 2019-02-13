package com.disco.audioprocess;

/**
 * Class for complex number object.
 */

public class Complex {
    private double real;
    private double image;


    public Complex() {
        this(0.0, 0.0);
    }


    public Complex(double real, double image) {
        this.real = real;
        this.image = image;
    }

    public double getReal() {
        return real;
    }

    public double getImage() {
        return image;
    }


    @Override
    public String toString() {
        return "Complex{" +
                "real=" + real +
                ", image=" + image +
                '}';
    }

    public Complex plus(Complex other) {
        return new Complex(getReal() + other.getReal(), getImage() + other.getImage());
    }


    public Complex multiple(Complex other) {
        return new Complex(getReal() * other.getReal() - getImage() * other.getImage(), getReal() * other.getImage() + getImage() * other.getReal());
    }


    public Complex minus(Complex other) {

        return new Complex(getReal() - other.getReal(), getImage() - other.getImage());
    }

    public final double getMod() {
        return Math.sqrt(this.getReal() * this.getReal() + this.getImage() * this.getImage());
    }


}
