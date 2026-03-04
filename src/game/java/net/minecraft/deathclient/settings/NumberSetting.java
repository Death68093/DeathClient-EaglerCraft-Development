package net.minecraft.deathclient.settings;

public class NumberSetting extends Setting {
    private double value, minimum, maximum, increment;

    public NumberSetting(String name, double defaultValue, double minimum, double maximum, double increment) {
        super(name);
        this.value = defaultValue;
        this.minimum = minimum;
        this.maximum = maximum;
        this.increment = increment;
    }

    public double getValue() { return value; }
    
    public void setValue(double value) { 
        // Snap to increment and clamp between min and max
        double precision = 1.0D / this.increment;
        this.value = Math.round(Math.max(this.minimum, Math.min(this.maximum, value)) * precision) / precision;
    }

    public double getMinimum() { return minimum; }
    public double getMaximum() { return maximum; }
}