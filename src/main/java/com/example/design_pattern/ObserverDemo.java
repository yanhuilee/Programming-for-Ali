package com.example.design_pattern;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Lee
 * @Date: 2019/04/15 17:39
 * java.util.Observer Observable
 */
public class ObserverDemo {
    public static void main(String[] args) {
        WeatherData weatherData = new WeatherData();

        CurrentConditionsDisplay currentDisplay = new CurrentConditionsDisplay(weatherData);

        weatherData.setMeasurements(80, 65, 30.4F);
    }
}

interface Observer {
    void update(float temperature, float humidity, float pressure);
}

interface Subject {
    void registerObserver(Observer observer);

    void removeObserver(Observer observer);

    void notifyObservers();
}

interface DisplayElement {
    void display();
}

class WeatherData implements Subject {
    List<Observer> observers;
    float temperature;
    float humidity;
    float pressure;

    public WeatherData() {
        this.observers = new ArrayList<>();
    }

    @Override
    public void registerObserver(Observer observer) {
        if (observers != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void removeObserver(Observer observer) {
        int i = observers.indexOf(observer);
        if (i >= 0) {
            observers.remove(i);
        }
    }

    @Override
    public void notifyObservers() {
        observers.forEach(observer -> observer.update(temperature, humidity, pressure));
    }

    void setMeasurements(float temperature, float humidity, float pressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        // 当从气象站得到更新观测值时，通知观察者
        notifyObservers();
    }
}

/**
 * 布告板
 */
class CurrentConditionsDisplay implements Observer, DisplayElement {
    float temperature;
    float humidity;
    Subject weatherData;

    public CurrentConditionsDisplay(Subject weatherData) {
        this.weatherData = weatherData;
        weatherData.registerObserver(this);
    }

    @Override
    public void update(float temperature, float humidity, float pressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        display();
    }

    @Override
    public void display() {
        System.out.println("CurrentConditionsDisplay {" +
                "temperature=" + temperature +
                "F degrees, humidity=" + humidity +
                "%}");
    }

}

class ForecastDisplay implements Observer, DisplayElement {
    float currentPressure = 29.92F;
    float lastPressure;

    @Override
    public void update(float temperature, float humidity, float pressure) {

    }

    @Override
    public void display() {

    }
}