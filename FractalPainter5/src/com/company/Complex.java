package com.company;

/*
Класс для описания комплексных чисел, имеет действительную и мнимую часть,
добавлены поля x и y для хранения координаты этого числа
присутствуют некоторые базовые методы для работы с комплексными числами
и главный метод Iteration(), который
*/
public class Complex {
    private double x;
    private double y;
    private double real;
    private double imaginary;

    public Complex(double x, double y) {
        this.x = x;
        this.y = y;

        this.real = 0;
        this.imaginary = 0;
    }
    public Complex conj() { //conjugate -сопряженное
        return new Complex(real, -imaginary);
    }
    public Complex sub(Complex b) { //возвращает разность this и числа b
        return new Complex(real - b.real, imaginary - b.imaginary);
    }
    public Complex add(Complex b) { //возвращает результат сложения
        return new Complex(real + b.real, imaginary + b.imaginary);
    }

    public Complex mul(Complex b) { //возвращает результат умножения this на b
        return new Complex(real * b.real - imaginary * b.imaginary, real * b.imaginary + imaginary * b.real);
    }
    public Complex mul(double b) { //умножение на рациональное число
        return new Complex(real * b, imaginary * b);
    }
    public double abs() { //модуль числа
        return Math.sqrt(real * real + imaginary * imaginary);
    }
    public double abs2() { //модуль числа в квадрате
        return real * real + imaginary * imaginary;
    }
    public double getReal() {
        return real;
    }
    public double getImaginary() {
        return imaginary;
    }

    public void setReal(double real) {
        this.real = real;
    }
    public void setImaginary(double imaginary) {
        this.imaginary = imaginary;
    }

    public void mandelbrotIteration() {
        double realUpdated = (real * real) - (imaginary * imaginary) + x;
        double imaginaryUpdated = 2 * real * imaginary + y;
        real = realUpdated;
        imaginary = imaginaryUpdated;
    }

    public void tricornIteration() {
        double realUpdated = (real * real) - (imaginary * imaginary) + x;
        double imaginaryUpdated = -2 * real * imaginary + y;
        real = realUpdated;
        imaginary = imaginaryUpdated;
    }

    public void burningShipIteration() {
        double realUpdated = (real * real) - (imaginary * imaginary) + x;
        double imaginaryUpdated = 2 * Math.abs(real) * Math.abs(imaginary) + y;
        real = realUpdated;
        imaginary = imaginaryUpdated;
    }
}
