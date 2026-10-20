/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package info.iut.sae2.graphs.tests;

import info.iut.sae2.graphs.Color;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author sfoussatferr and astyzinski
 *
 * Test for the Color class
 */
public class ColorTest {

    // Default constructor (also tests getters)
    
    @Test
    public void defaultConstructor_initializesToZero() {
        Color c = new Color();
        assertEquals(0, c.getR());
        assertEquals(0, c.getG());
        assertEquals(0, c.getB());
        assertEquals(0, c.getA());
    }

    // RGBA constructor
    // catch assignment bugs : this.g = b instead of this.g = g
    // This bug was found in the original setG implementation 

    @Test
    public void rgbaConstructor_storesValues() {
        Color c = new Color(10, 20, 30, 40);
        assertEquals(10, c.getR());
        assertEquals(20, c.getG());
        assertEquals(30, c.getB());
        assertEquals(40, c.getA());
    }

    @Test
    public void rgbaConstructor_zeroIsValid() {
        Color c = new Color(0, 0, 0, 0);
        assertEquals(0, c.getR());
        assertEquals(0, c.getG());
        assertEquals(0, c.getB());
        assertEquals(0, c.getA());
    }

    @Test
    public void rgbaConstructor_255IsValid() {
        Color c = new Color(255, 255, 255, 255);
        assertEquals(255, c.getR());
        assertEquals(255, c.getG());
        assertEquals(255, c.getB());
        assertEquals(255, c.getA());
    }

    // Copy constructor

    @Test
    public void copyConstructor_copiesAllChannels() {
        Color original = new Color(1, 2, 3, 4);
        Color copy = new Color(original);
        assertEquals(1, copy.getR());
        assertEquals(2, copy.getG());
        assertEquals(3, copy.getB());
        assertEquals(4, copy.getA());
    }

    @Test
    public void copyConstructor_changingCopyDoesNotAffectOriginal() {
        Color original = new Color(100, 100, 100, 100);
        Color copy = new Color(original);
        copy.setR(0);
        copy.setG(0);
        copy.setB(0);
        copy.setA(0);
        assertEquals(100, original.getR());
        assertEquals(100, original.getG());
        assertEquals(100, original.getB());
        assertEquals(100, original.getA());
    }

    @Test
    public void copyConstructor_isDistinctInstance() {
        Color original = new Color(1, 2, 3, 4);
        assertNotEquals(original, new Color(original));
    }

    // Setters

    @Test
    public void setR_updatesROnly() {
        Color c = new Color(0, 50, 50, 50);
        c.setR(99);
        assertEquals(99, c.getR());
        assertEquals(50, c.getG());
        assertEquals(50, c.getB());
        assertEquals(50, c.getA());
    }

    @Test
    public void setG_updatesGOnly() {
        Color c = new Color(50, 0, 50, 50);
        c.setG(99);
        assertEquals(50, c.getR());
        assertEquals(99, c.getG());
        assertEquals(50, c.getB());
        assertEquals(50, c.getA());
    }

    @Test
    public void setB_updatesBOnly() {
        Color c = new Color(50, 50, 0, 50);
        c.setB(99);
        assertEquals(50, c.getR());
        assertEquals(50, c.getG());
        assertEquals(99, c.getB());
        assertEquals(50, c.getA());
    }

    @Test
    public void setA_updatesAOnly() {
        Color c = new Color(50, 50, 50, 0);
        c.setA(99);
        assertEquals(50, c.getR());
        assertEquals(50, c.getG());
        assertEquals(50, c.getB());
        assertEquals(99, c.getA());
    }

    // interpolate

    @Test
    public void interpolate_step0_equalsStart() {
        Color start = new Color(0, 0, 0, 0);
        Color end = new Color(100, 100, 100, 100);
        Color result = Color.interpolate(start, end, 0, 10);
        
        assertEquals(0, result.getR());
        assertEquals(0, result.getG());
        assertEquals(0, result.getB());
        assertEquals(0, result.getA());
    }

    @Test
    public void interpolate_stepN_equalsEnd() {
        Color start = new Color(0, 0, 0, 0);
        Color end = new Color(100, 100, 100, 100);
        Color result = Color.interpolate(start, end, 10, 10);
        
        assertEquals(100, result.getR());
        assertEquals(100, result.getG());
        assertEquals(100, result.getB());
        assertEquals(100, result.getA());
    }

    @Test
    public void interpolate_midpoint_isCorrect() {
        Color start = new Color(0, 0, 0, 0);
        Color end = new Color(100, 100, 100, 100);
        Color result = Color.interpolate(start, end, 1, 2);
        
        assertEquals(50, result.getR());
        assertEquals(50, result.getG());
        assertEquals(50, result.getB());
        assertEquals(50, result.getA());
    }

    @Test
    public void interpolate_returnsNewInstance() {
        Color start = new Color(0, 0, 0, 0);
        Color end   = new Color(255, 255, 255, 255);
        Color result = Color.interpolate(start, end, 1, 2);
        assertNotEquals(start, result);
        assertNotEquals(end, result);
    }

    // HSVtoRGB - all 6 sectors 

    @Test
    public void hsvToRgb_sector0_pureRed() {
        // H=0: R=v, G=rising, B=floor -> red
        Color c = Color.HSVtoRGB(0, 1.0, 1.0, 255);
        assertEquals(255, c.getR());
        assertEquals(0,   c.getG());
        assertEquals(0,   c.getB());
    }

    @Test
    public void hsvToRgb_sector1_pureGreen() {
        // H=120: R=falling, G=v, B=floor -> green
        Color c = Color.HSVtoRGB(120, 1.0, 1.0, 255);
        assertEquals(0,   c.getR());
        assertEquals(255, c.getG());
        assertEquals(0,   c.getB());
    }

    @Test
    public void hsvToRgb_sector2_pureCyan() {
        // H=180: R=floor, G=v, B=rising -> cyan
        Color c = Color.HSVtoRGB(180, 1.0, 1.0, 255);
        assertEquals(0,   c.getR());
        assertEquals(255, c.getG());
        assertEquals(255, c.getB());
    }

    @Test
    public void hsvToRgb_sector3_pureBlue() {
        // H=240: R=floor, G=falling, B=v -> blue
        Color c = Color.HSVtoRGB(240, 1.0, 1.0, 255);
        assertEquals(0,   c.getR());
        assertEquals(0,   c.getG());
        assertEquals(255, c.getB());
    }

    @Test
    public void hsvToRgb_sector4_pureMagenta() {
        // H=300: R=rising, G=floor, B=v -> magenta
        Color c = Color.HSVtoRGB(300, 1.0, 1.0, 255);
        assertEquals(255, c.getR());
        assertEquals(0,   c.getG());
        assertEquals(255, c.getB());
    }

    @Test
    public void hsvToRgb_sector5_backToRed() {
        // H=300: close to red
        Color c = Color.HSVtoRGB(359, 1.0, 1.0, 255);
        assertEquals(255, c.getR());
        assertEquals(4, c.getB());
    }

    @Test
    public void hsvToRgb_yellow() {
        Color c = Color.HSVtoRGB(60, 1.0, 1.0, 255);
        assertEquals(255, c.getR());
        assertEquals(255, c.getG());
        assertEquals(0, c.getB());
    }

    // HSVtoRGB - saturation boundary

    @Test
    public void hsvToRgb_saturationZero_isGrey() {
        // S=0: all channels equal value
        Color c = Color.HSVtoRGB(180, 0.0, 0.5, 255);
        assertEquals(c.getR(), c.getG());
        assertEquals(c.getG(), c.getB());
    }

    // HSVtoRGB - value boundary


    @Test
    public void hsvToRgb_white() {
        // S=0, V=1 -> white regardless of hue
        Color c = Color.HSVtoRGB(0, 0.0, 1.0, 255);
        assertEquals(255, c.getR());
        assertEquals(255, c.getG());
        assertEquals(255, c.getB());
    }

    @Test
    public void hsvToRgb_black() {
        // V=0 -> black regardless of hue and saturation
        Color c = Color.HSVtoRGB(0, 0.0, 0.0, 255);
        assertEquals(0, c.getR());
        assertEquals(0, c.getG());
        assertEquals(0, c.getB());
    }

    // HSVtoRGB - alpha boundary

    @Test
    public void hsvToRgb_alphaZeroIsValid() {
        assertEquals(0, Color.HSVtoRGB(0, 1.0, 1.0, 0).getA());
    }

    @Test
    public void hsvToRgb_alpha255IsValid() {
        assertEquals(255, Color.HSVtoRGB(0, 1.0, 1.0, 255).getA());
    }

    @Test
    public void hsvToRgb_alphaMidIsPreserved() {
        assertEquals(128, Color.HSVtoRGB(0, 1.0, 1.0, 128).getA());
    }

    // HSVtoRGB - exceptions

    @Test(expected = IllegalArgumentException.class)
    public void hsvToRgb_hueNegativeThrows() {
        Color.HSVtoRGB(-1, 1.0, 1.0, 255);
    }

    @Test(expected = IllegalArgumentException.class)
    public void hsvToRgb_hue360Throws() {
        Color.HSVtoRGB(360, 1.0, 1.0, 255);
    }

    @Test(expected = IllegalArgumentException.class)
    public void hsvToRgb_saturationNegativeThrows() {
        Color.HSVtoRGB(0, -0.1, 1.0, 255);
    }

    @Test(expected = IllegalArgumentException.class)
    public void hsvToRgb_saturationAbove1Throws() {
        Color.HSVtoRGB(0, 1.1, 1.0, 255);
    }

    @Test(expected = IllegalArgumentException.class)
    public void hsvToRgb_valueNegativeThrows() {
        Color.HSVtoRGB(0, 1.0, -0.1, 255);
    }

    @Test(expected = IllegalArgumentException.class)
    public void hsvToRgb_valueAbove1Throws() {
        Color.HSVtoRGB(0, 1.0, 1.1, 255);
    }

    @Test(expected = IllegalArgumentException.class)
    public void hsvToRgb_alphaNegativeThrows() {
        Color.HSVtoRGB(0, 1.0, 1.0, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void hsvToRgb_alphaAbove255Throws() {
        Color.HSVtoRGB(0, 1.0, 1.0, 256);
    }

    // generateColor

    @Test
    public void generateColor_returnsNonNull() {
        assertNotNull(Color.generateColor(1));
    }

    @Test
    public void generateColor_alphaIsAlways255() {
        for (int i = 1; i <= 12; i++) {
            assertEquals(255, Color.generateColor(i).getA());
        }
    }

    @Test
    public void generateColor_consecutiveIdsAreDifferent() {
        for (int i = 1; i < 12; i++) {
            Color a = Color.generateColor(i);
            Color b = Color.generateColor(i + 1);
            assertFalse(
                "Colors " + i + " and " + (i+1) + " should differ",
                a.getR() == b.getR() && a.getG() == b.getG() && a.getB() == b.getB()
            );
        }
    }

    @Test
    public void generateColor_largeIdIsValid() {
        assertNotNull(Color.generateColor(15631335));
    }

    @Test(expected = IllegalArgumentException.class)
    public void generateColor_zeroThrows() {
        Color.generateColor(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void generateColor_negativeThrows() {
        Color.generateColor(-1);
    }

    // populate

    @Test
    public void populate_calledTwiceDoesNotChangeColors() {
        Color.populate();
        Color first = Color.generateColor(1);
        Color.populate();
        Color second = Color.generateColor(1);
        assertEquals(first.getR(), second.getR());
        assertEquals(first.getG(), second.getG());
        assertEquals(first.getB(), second.getB());
    }

    @Test
    public void populate_firstSixAreCached() {
        Color.populate();
        for (int i = 1; i <= 6; i++) {
            assertEquals(Color.generateColor(i), Color.generateColor(i));
        }
    }

    // toString

    @Test
    public void toString_correctFormat() {
        assertEquals("(67, 69, 69, 67)", new Color(67, 69, 69, 67).toString());
    }

    @Test
    public void toString_allZeros() {
        assertEquals("(0, 0, 0, 0)", new Color().toString());
    }

    @Test
    public void toString_allMax() {
        assertEquals("(255, 255, 255, 255)", new Color(255, 255, 255, 255).toString());
    }
}
