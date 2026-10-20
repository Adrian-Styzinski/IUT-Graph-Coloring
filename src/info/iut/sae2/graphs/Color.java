/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package info.iut.sae2.graphs;

import java.util.HashMap;
/**
 *
 * @author rbourqui
 */
public class Color {
    
    public int r, g, b, a;
    
    /**
     * Default constructor.
     */
    public Color(){
        this(0,0,0,0);
    }
    
    /**
     * Copy constructor.
     *
     * @param c the Color to be copied
     */
    public Color(Color c){
        this(c.r, c.g, c.b, c.a);
    }
    
    /**
     * Constructor with given red, green, blue and alpha channels.
     *
     * @param r red channel of the Color
     * @param g green channel of the Color
     * @param b blue channel of the Color
     * @param a alpha channel of the Color
     */
    public Color(int r, int g, int b, int a){
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }
    
    /**
     * Returns the red channel of the Color.
     * 
     * @return the red channnel
     */
    public int getR(){
        return this.r;
    }
    
    /**
     * Returns the green channel of the Color.
     * 
     * @return the green channnel
     */
    public int getG(){
        return this.g;
    }
    
    /**
     * Returns the blue channel of the Color.
     * 
     * @return the blue channnel
     */
    public int getB(){
        return this.b;
    }
    
    /**
     * Returns the alpha channel of the Color.
     * 
     * @return the alpha channnel
     */
    public int getA(){
        return this.a;
    }
    
    /**
     * Set the red channel of the Color to the given value.
     * 
     * @param r the red channnel
     */
    public void setR(int r){
        this.r = r;
    }
    
    /**
     * Set the green channel of the Color to the given value.
     * 
     * @param g the green channnel
     */
    public void setG(int g){
        this.g = g;
    }
    
    /**
     * Set the blue channel of the Color to the given value.
     * 
     * @param b the blue channnel
     */
    public void setB(int b){
        this.b = b;
    }
    
    /**
     * Set the alpha channel of the Color to the given value.
     * 
     * @param a the alpha channnel
     */
    public void setA(int a){
        this.a = a;
    }
    
    /**
     * Returns a new Color result of a linear interpolation between the start and end colors given the current step and the number of steps.
     * 
     * @param start start color of the interpolation
     * @param end end color of the interpolation
     * @param step current step
     * @param nbSteps total number of steps
     * @return the interpolated color
     */
    public static Color interpolate(Color start, Color end, int step, int nbSteps){
        return new Color(start.r + (end.r-start.r)*step / nbSteps, start.g + (end.g-start.g)*step / nbSteps, start.b + (end.b-start.b)*step / nbSteps, start.a + (end.a-start.a)*step / nbSteps);
    }
    
    @Override 
    public String toString(){
        return "("+ r + ", " + g + ", " + b + ", " + a + ")";
    }
    
    /**
    *
    * @author sfoussatferr and astyzinski
    */
    /**
     * Converts a HSV color to a Color object (RGBA)
     *
     * @param hue the hue in degrees from 0 included to 360 not included
     * @param saturation the saturation level from 0.0 to 1.0 both included
     * @param value the value or brightness from 0.0 to 1.0 both included
     * @param alpha the alpha from 0 to 255 both included
     * @return Color object in r, g, b, a between 0 and 255
     */
    public static Color HSVtoRGB(double hue, double saturation, double value, int alpha) {
        if (hue < 0 || hue >= 360) {
            throw new IllegalArgumentException("Hue must be in [0, 360), but got " + hue);
        }
        if (saturation < 0.0 || saturation > 1.0) {
            throw new IllegalArgumentException("Saturation must be in [0, 1], but got " + saturation);
        }
        if (value < 0.0 || value > 1.0) {
            throw new IllegalArgumentException("Value must be in [0, 1], but got " + value);
        }
        if (alpha < 0 || alpha > 255){
            throw new IllegalArgumentException("Alpha must be in [0, 255], but got " + alpha);
        }

        
        double zone = hue / 60.0;
        int currentSector = (int) Math.floor(zone);
        double distanceFromSector = zone - currentSector;
        double floor = value * (1.0 - saturation);
        double falling = value * (1.0 - saturation * distanceFromSector);
        double rising = value * (1.0 - saturation * (1.0 - distanceFromSector));

        double r, g, b;
        switch (currentSector) {
            case 0 -> {
                r = value;
                g = rising;
                b = floor;
            }
            case 1 -> {
                r = falling;
                g = value;
                b = floor;
            }
            case 2 -> {
                r = floor;
                g = value;
                b = rising;
            }
            
            case 3 -> {
                r = floor;
                g = falling;
                b = value;
            }
            case 4 ->{
                r = rising;
                g = floor;
                b = value;
            }
            default ->{
                r = value;
                g = floor;
                b = falling;
            }


        }

        return new Color(
            (int) Math.round(r * 255),
            (int) Math.round(g * 255),
            (int) Math.round(b * 255),
            alpha
        );
    }

    /**
    * A set of {saturation, value} pairs defining visually distinct color on the HSV cylinder
    * Each row is cycled through as colors are generated so that even when two colors land on a similar hue, they still look different due to their brightness and saturation.
    */
    private static final double[][] SV_LEVELS = {
        { 0.85, 0.95 },
        { 0.60, 0.70 },
        { 1.00, 0.65 },
        { 0.40, 0.90 },
        { 0.90, 0.45 },
        { 0.50, 0.55 },
    };

    /**
    * The golden angle in degrees (~137.508).
    * Used to generate color since, as it is irrational relative to 360, consecutive colors are always guaranteed to have maximal perceptual spread whitout there being a visual order
    */
    private static final double GOLDEN_ANGLE = 137.50776405003785;
    
    /**
    * The hue in degrees at which the sequence starts (in simple terms the hue of color #1).
    * changing this value rotates every generated color around the color wheel
    */
    private static final double HUE_OFFSET   = 30.0;

    /**
    * Returns a unique, perceptually distributed color for the given id
    * If the color has already been generated, it is returned from the cache
    * Otherwise it is computed, stored in the cache, and returned
     *
     * @param id color index, must be >= 1
     * @return a distinct Color (alpha = 255)
     */
    public static Color generateColor(int id) {
        if (id < 1){
            throw new IllegalArgumentException("index must be >= 1, got " + id);
        }
        if (!COLOR_CACHE.containsKey(id)){
            double hue = (HUE_OFFSET + (id - 1) * GOLDEN_ANGLE) % 360.0;
            int svIndex = (id - 1) % SV_LEVELS.length;
            COLOR_CACHE.put(id, HSVtoRGB(hue, SV_LEVELS[svIndex][0], SV_LEVELS[svIndex][1], 255));
        }
        
        return COLOR_CACHE.get(id);
    }
    
    /**
    * Cache of already-computed colors, keyed by color id.
    * Avoids recomputing the same color multiple times during rendering.
    */
    private static final HashMap<Integer, Color> COLOR_CACHE = new HashMap<>();
   
    /**
    * Pre-populates the color cache with the first 6 colors
    * The first 6 colors are pre-computed since our coloring algorithms (5-color, 6-color) will always need at most those.
    */
    public static void populate() {
        for (int i = 1; i <= 6; i++) {
            double hue = (HUE_OFFSET + (i - 1) * GOLDEN_ANGLE) % 360.0;
            int svIndex = (i - 1) % SV_LEVELS.length;
            COLOR_CACHE.put(i, HSVtoRGB(hue, SV_LEVELS[svIndex][0], SV_LEVELS[svIndex][1], 255));
        }
    }
}
