/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package info.iut.sae2.graphs.tests;

import info.iut.sae2.graphs.Coord;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author sfoussatferr and astyzinski
 *
 * Test for the Coord class
 */
public class CoordTest {

    private static final double DELTA = 0.0000001;

    // Constructors & Getters

    @Test
    public void defaultConstructor_initializesToZero() {
        Coord c = new Coord();
        assertEquals(0.0, c.getX(), DELTA);
        assertEquals(0.0, c.getY(), DELTA);
    }

    @Test
    public void xyConstructor_storesValuesCorrectly() {
        Coord c = new Coord(3.0, 4.0);
        assertEquals(3.0, c.getX(), DELTA);
        assertEquals(4.0, c.getY(), DELTA);

        Coord negative = new Coord(-5.0, -10.0);
        assertEquals(-5.0,  negative.getX(), DELTA);
        assertEquals(-10.0, negative.getY(), DELTA);
    }

    @Test
    public void copyConstructor_copiesValuesAndIsDistinct() {
        Coord original = new Coord(1.0, 2.0);
        Coord copy = new Coord(original);
        
        assertEquals(1.0, copy.getX(), DELTA);
        assertEquals(2.0, copy.getY(), DELTA);
        assertNotEquals(original, copy);

        // Ensure changing the copy does not affect the original
        copy.setX(99.0);
        copy.setY(99.0);
        assertEquals(1.0, original.getX(), DELTA);
        assertEquals(2.0, original.getY(), DELTA);
    }

    // Setters

    @Test
    public void setX_updatesXOnly() {
        Coord c = new Coord(0.0, 4.0);
        c.setX(5.0);
        assertEquals(5.0, c.getX(), DELTA);
        assertEquals(4.0, c.getY(), DELTA); // Unchanged

        c.setX(-3.0);
        assertEquals(-3.0, c.getX(), DELTA);
    }

    @Test
    public void setY_updatesYOnly() {
        Coord c = new Coord(4.0, 0.0);
        c.setY(5.0);
        assertEquals(4.0, c.getX(), DELTA); // Unaffected
        assertEquals(5.0, c.getY(), DELTA);

        c.setY(-3.0);
        assertEquals(-3.0, c.getY(), DELTA);
    }

    // dist & norm

    @Test
    public void dist_calculationsAreCorrect() {
        Coord c = new Coord(3.0, 4.0);
        assertEquals(0.0, c.dist(c), DELTA);
        assertEquals(5.0, c.dist(new Coord(0.0, 0.0)), DELTA); // Pythagor

        Coord b = new Coord(4.0, 6.0);
        assertEquals(c.dist(b), b.dist(c), DELTA);

        assertEquals(5.0, new Coord(0.0, 0.0).dist(new Coord(5.0, 0.0)), DELTA);
        assertEquals(5.0, new Coord(0.0, 0.0).dist(new Coord(0.0, 5.0)), DELTA);
        assertEquals(5.0, new Coord(-3.0, -4.0).dist(new Coord(0.0, 0.0)), DELTA);
    }

    @Test
    public void norm_calculationsAreCorrect() {
        assertEquals(0.0, new Coord(0.0, 0.0).norm(), DELTA);
        assertEquals(5.0, new Coord(3.0, 4.0).norm(), DELTA);
        assertEquals(5.0, new Coord(-3.0, -4.0).norm(), DELTA);
        
        Coord c = new Coord(6.0, 8.0);
        assertEquals(c.norm(), c.dist(new Coord(0.0, 0.0)), DELTA);
    }

    // mult (instance method)

    @Test
    public void mult_scalesBothCoordinates() {
        Coord c = new Coord(3.0, 4.0);
        c.mult(2.0);
        assertEquals(6.0, c.getX(), DELTA);
        assertEquals(8.0, c.getY(), DELTA);
    }

    @Test
    public void mult_boundaryEdgeCases() {
        Coord c0 = new Coord(5.0, 7.0);
        c0.mult(0.0);
        assertEquals(0.0, c0.getX(), DELTA);
        assertEquals(0.0, c0.getY(), DELTA);

        Coord c1 = new Coord(3.0, 4.0);
        c1.mult(1.0);
        assertEquals(3.0, c1.getX(), DELTA);
        assertEquals(4.0, c1.getY(), DELTA);

        Coord cNeg = new Coord(3.0, 4.0);
        cNeg.mult(-1.0);
        assertEquals(-3.0, cNeg.getX(), DELTA);
        assertEquals(-4.0, cNeg.getY(), DELTA);
    }

    // add(double, double)

    @Test
    public void addDoubles_updatesCoordinatesInPlace() {
        Coord c = new Coord(1.0, 2.0);
        c.add(3.0, 3.0);
        assertEquals(4.0, c.getX(), DELTA);
        assertEquals(5.0, c.getY(), DELTA);

        c.add(-2.0, -3.0);
        assertEquals(2.0, c.getX(), DELTA);
        assertEquals(2.0, c.getY(), DELTA);
    }

    // add(Coord)

    @Test
    public void addCoord_updatesCoordinatesAndPreservesArgument() {
        Coord c = new Coord(1.0, 2.0);
        Coord arg = new Coord(3.0, 3.0);
        c.add(arg);
        assertEquals(4.0, c.getX(), DELTA);
        assertEquals(5.0, c.getY(), DELTA);

        // Verify passed argument not modified
        assertEquals(3.0, arg.getX(), DELTA);
        assertEquals(3.0, arg.getY(), DELTA);
    }

    // static add(Coord, Coord)

    @Test
    public void staticAdd_returnsCorrectNewInstance() {
        Coord a = new Coord(1.0, 3.0);
        Coord b = new Coord(3.0, 4.0);
        Coord result = Coord.add(a, b);

        assertEquals(4.0, result.getX(), DELTA);
        assertEquals(7.0, result.getY(), DELTA);
        assertNotEquals(a, result);
        assertNotEquals(b, result);

        // Inputs unchanged
        assertEquals(1.0, a.getX(), DELTA);
        assertEquals(4.0, b.getY(), DELTA);
    }

    // static minus

    @Test
    public void staticMinus_returnsCorrectNewInstance() {
        Coord a = new Coord(5.0, 4.0);
        Coord b = new Coord(3.0, 3.0);
        Coord result = Coord.minus(a, b);

        assertEquals(2.0, result.getX(), DELTA);
        assertEquals(1.0, result.getY(), DELTA);
        assertNotEquals(a, result);
        assertNotEquals(b, result);

        assertEquals(0.0, Coord.minus(a, a).getX(), DELTA);
    }

    // static mult(Coord, double)

    @Test
    public void staticMult_returnsCorrectNewInstance() {
        Coord a = new Coord(3.0, 4.0);
        Coord result = Coord.mult(a, 2.0);

        assertEquals(6.0, result.getX(), DELTA);
        assertEquals(8.0, result.getY(), DELTA);
        assertNotEquals(a, result);
        assertEquals(3.0, a.getX(), DELTA); // unchanged input

        assertEquals(0.0, Coord.mult(a, 0.0).getX(), DELTA);
        assertEquals(-3.0, Coord.mult(a, -1.0).getX(), DELTA);
    }

    // toString

    @Test
    public void toString_formatsCorrectly() {
        assertEquals("(1.0, 2.0)", new Coord(1.0, 2.0).toString());
        assertEquals("(0.0, 0.0)", new Coord().toString());
        assertTrue(new Coord(-1.0, -2.0).toString().contains("-1.0"));
    }
}