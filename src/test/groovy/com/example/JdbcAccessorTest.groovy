package com.example

import spock.lang.Specification

/**
 * Created with IntelliJ IDEA.
 * User: ryan
 * Date: 8/5/13
 * Time: 11:06 AM
 */
class JdbcAccessorTest extends Specification {
    def accessor = new JdbcAccessor()

    def setup() {
        accessor.init()
    }

    def cleanup() {
        accessor.destroy()
    }

    def 'insert and get works'() {
        when:
        accessor.insert(1)
        accessor.insert(2)
        accessor.insert(3)
        accessor.insert(4)

        and:
        def loaded = accessor.getInsertedNumbers()

        then:
        loaded == [1, 2, 3, 4]
    }

	/*
	 *  inserting negative values and zero to check 
	 */
	def 'insert and get works with negative values and zero'() {
		when:
		accessor.insert(-1)
		accessor.insert(-2)
		accessor.insert(-3)
		accessor.insert(-4)
		accessor.insert(0)

		and:
		def loaded = accessor.getInsertedNumbers()

		then:
		loaded == [-1, -2, -3, -4, 0]
	}

	/*
	 * Checking delays if we insert negative values
	 */
	def 'insert has artificial delay built in for Negative values '() {
		when:
		long start = System.currentTimeMillis()
		accessor.insert(-2001)
		long elapsed = System.currentTimeMillis() - start

		then:
		elapsed >= 500
	}
	
    def 'insert has artificial delay built in'() {
        when:
        long start = System.currentTimeMillis()
        accessor.insert(1234)
        long elapsed = System.currentTimeMillis() - start

        then:
        elapsed >= 500
    }
}
