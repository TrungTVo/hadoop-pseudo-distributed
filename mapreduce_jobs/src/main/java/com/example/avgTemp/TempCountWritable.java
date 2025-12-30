package com.example.avgTemp;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class TempCountWritable implements Writable {

    private float temperature;
    private int count;

    // Required: no-arg constructor
    public TempCountWritable() {
    }

    public TempCountWritable(float temperature, int count) {
        this.temperature = temperature;
        this.count = count;
    }

    public float getTemperature() {
        return temperature;
    }

    public int getCount() {
        return count;
    }

    public void setTemp(float temperature) {
        this.temperature = temperature;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeFloat(temperature);
        out.writeInt(count);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        temperature = in.readFloat();
        count = in.readInt();
    }

    @Override
    public String toString() {
        return temperature + "," + count;
    }
}
