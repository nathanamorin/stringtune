package com.nathanmorin.stringtuner

import org.junit.Test

import org.junit.Assert.*

/**
 * Test Note Conversion
 *
 */
class TestNoteConvert {
    @Test
    fun noteConvertTest() {
        assertEquals(880.00, convertNoteToFrequency("A5"), 0.001)
        assertEquals(233.08, convertNoteToFrequency("A3#"), 0.01)
        assertEquals(932.33, convertNoteToFrequency("B5b"), 0.01)
        assertEquals(440.00, convertNoteToFrequency("A4"), 0.01)

    }
}
