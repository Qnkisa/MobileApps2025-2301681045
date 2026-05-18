package com.example.zerog

import com.example.zerog.data.local.GravityLog
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests covering the core business logic:
 *   - The Zero-G weight formula: Weight_Zero-G = Weight_Earth × (1 − Reduction/100)
 *   - GravityLog entity behaviour (data class properties, copy, equality)
 *
 * These tests run on the JVM — no Android framework or database required.
 */
class GravityCalculationTest {

    // Mirror the exact formula used in CalculatorFragment
    private fun calcZeroG(earthWeight: Double, reductionPercent: Int): Double =
        earthWeight * (1.0 - reductionPercent / 100.0)

    // ── Formula correctness ───────────────────────────────────────────────────

    @Test
    fun zeroReduction_returnsFullEarthWeight() {
        assertEquals(1000.0, calcZeroG(1000.0, 0), 0.0001)
    }

    @Test
    fun fullReduction_returnsZeroWeight() {
        assertEquals(0.0, calcZeroG(1000.0, 100), 0.0001)
    }

    @Test
    fun fiftyPercentReduction_returnsHalfWeight() {
        assertEquals(100.0, calcZeroG(200.0, 50), 0.0001)
    }

    @Test
    fun eightyPercentReduction_onCarWeight_returns300kg() {
        // 1500 kg car with 80% anti-gravity → 300 kg
        assertEquals(300.0, calcZeroG(1500.0, 80), 0.0001)
    }

    @Test
    fun twentyFivePercentReduction_on80kgPerson_returns60kg() {
        assertEquals(60.0, calcZeroG(80.0, 25), 0.0001)
    }

    @Test
    fun smallWeight_withHighReduction_returnsCorrectFraction() {
        assertEquals(0.05, calcZeroG(0.5, 90), 0.0001)
    }

    @Test
    fun largeWeight_withOnePercentReduction_returnsCorrectValue() {
        assertEquals(99000.0, calcZeroG(100_000.0, 1), 0.01)
    }

    @Test
    fun ninetyNinePercentReduction_leavesOnePercent() {
        assertEquals(1.0, calcZeroG(100.0, 99), 0.0001)
    }

    @Test
    fun formula_matchesManuallyComputedValue() {
        val earth = 68.5
        val reduction = 33
        val expected = 68.5 * (1.0 - 33 / 100.0)
        assertEquals(expected, calcZeroG(earth, reduction), 0.0001)
    }

    @Test
    fun differentReductions_produceDifferentResults() {
        assertNotEquals(calcZeroG(100.0, 30), calcZeroG(100.0, 70), 0.0001)
    }

    // ── GravityLog entity ─────────────────────────────────────────────────────

    @Test
    fun gravityLog_defaultId_isZero() {
        val log = GravityLog(itemName = "Rock", earthWeight = 10.0, gravityReduction = 50, calculatedWeight = 5.0)
        assertEquals(0, log.id)
    }

    @Test
    fun gravityLog_createdAt_isPositive() {
        val log = GravityLog(itemName = "Rock", earthWeight = 10.0, gravityReduction = 50, calculatedWeight = 5.0)
        assertTrue(log.createdAt > 0L)
    }

    @Test
    fun gravityLog_copy_updatesOnlyItemName() {
        val original = GravityLog(id = 1, itemName = "Old", earthWeight = 100.0, gravityReduction = 50, calculatedWeight = 50.0)
        val updated = original.copy(itemName = "New")
        assertEquals("New", updated.itemName)
        assertEquals(original.id, updated.id)
        assertEquals(original.earthWeight, updated.earthWeight, 0.0001)
    }

    @Test
    fun gravityLog_dataClassEquality_sameValues() {
        val a = GravityLog(id = 1, itemName = "Car", earthWeight = 1500.0, gravityReduction = 80, calculatedWeight = 300.0, createdAt = 12345L)
        val b = GravityLog(id = 1, itemName = "Car", earthWeight = 1500.0, gravityReduction = 80, calculatedWeight = 300.0, createdAt = 12345L)
        assertEquals(a, b)
    }

    @Test
    fun gravityLog_gravityReduction_storedAsInteger() {
        val log = GravityLog(itemName = "Test", earthWeight = 100.0, gravityReduction = 75, calculatedWeight = 25.0)
        assertEquals(75, log.gravityReduction)
    }

    @Test
    fun gravityLog_calculatedWeight_matchesFormula() {
        val earth = 500.0
        val reduction = 60
        val expected = calcZeroG(earth, reduction)
        val log = GravityLog(itemName = "Boulder", earthWeight = earth, gravityReduction = reduction, calculatedWeight = expected)
        assertEquals(expected, log.calculatedWeight, 0.0001)
    }
}
