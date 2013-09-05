package com.example

import spock.lang.Shared
import spock.lang.Specification

/**
 * Created with IntelliJ IDEA.
 * User: ryan
 * Date: 8/5/13
 * Time: 11:06 AM
 */
class IntegrationTest extends Specification {
    @Shared def application = new Application()
    @Shared def jdbc = new JdbcAccessor()
	def jms = new JmsAccessor()
	def logic = new Logic()

    def setupSpec() {
        application.start()
    }

    def cleanupSpec() {
        application.stop()
    }

    def 'sanity check of overall system for first prime'() {
        when:
        jms.sendToNumberInput(2)

        then:
        jms.receiveFromNumberOutput() == 3
    }

    def 'sanity check of overall system for first non-prime'() {
        when:
        jms.sendToNumberInput(4)

        then:
        jms.receiveFromNumberOutput() == 5
    }

    def 'sanity check of database storage'() {
        when:
        jms.sendToNumberInput(2)

        then:
        jms.receiveFromNumberOutput() // wait for processing
        jdbc.insertedNumbers.contains(2L)
    }
	
	/*
	 * Checking to receive next prime number (which is 2), from the queue if we send a negative number
	 * First prime number being '2'.
	 */
	def 'sanity check of overall system for when we send negative number'() {
		when:
		jms.sendToNumberInput(-1)

		then:
		jms.receiveFromNumberOutput() == 2
	}

	/*
	 * Checking to receive next prime number (which is 2), from the queue if we send ZERO in the first queue
	 * First prime number being '2'.
	 */
	def 'sanity check of overall system for when we send zero number'() {
		when:
		jms.sendToNumberInput(0)

		then:
		jms.receiveFromNumberOutput() == 2
	}

	/*
	 * As per requirement all the inserted values from the application should be Primes.
	 * Checking to see if all the insert data from the application are primes or not.
	 */
	def 'sanity check of overall system to see inserted values in DB are primes only'() {
		expect:
		logic.isPrime(loaded)

		where:
		loaded << jdbc.getInsertedNumbers()
		
	}

	/*
	 *  This test case is to check the complete business flow. Case-1: sending '2' a prime as input
	 */
	def ' Sanity check for overall System to check if the business logic is executed as per requirements with input as prime'(){
		when:
		application.jmsAccessor.receiveMessagesFromQueueToMethod("NumberOutput", application, "printMessageText");
		application.jmsAccessor.sendToNumberInput(2);
		Thread.sleep(2500);
		
		then:
		application.jdbcAccessor.getInsertedNumbers().contains(2L)
	}
	
	/*
	 *  This test case is to check the complete business flow. Case-2: sending '4' a non- prime as input
	 */
	def ' Sanity check for overall System to check if the business logic is executed as per requirements with input as non-prime'(){
		when:
		application.jmsAccessor.receiveMessagesFromQueueToMethod("NumberOutput", application, "printMessageText");
		application.jmsAccessor.sendToNumberInput(4);
		Thread.sleep(2500);
		
		then:
		application.jdbcAccessor.getInsertedNumbers().contains(5L)

	}


}
