package cse2311.week2;
import java.util.Arrays;

import cse2311.week2.HelloWorld;
import cse2311.week2.Student;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.*;


import org.junit.matchers.JUnitMatchers.*;
import org.hamcrest.core.CombinableMatcher;
import org.junit.Test;

public class HelloWorldTest {

	HelloWorld hi;
	
	@Test
	public void testSay() {
		hi = new HelloWorld();
		assertEquals("Hello World!", hi.say());
	}
	
	@Test
	public void testAdd() {
		hi = new HelloWorld();
		assertEquals(5, hi.add(2, 3));
	}
	
	@Test
	public void testArray() {
		int[] a = {1, 2, 3};
		int[] b = {1, 2, 3};
		assertArrayEquals("failure - arrays are not the same", a, b);
	}

	@Test
	public void testFalse() {
		int a = 1;
		int b = 2;
		assertFalse("failure - should be false", a==b);
	}
	
	@Test
	public void testNull() {
		String a = null;
		assertNull("faulure - should be null", a);
	}
	
	@Test
	public void testNotNull() {
		String a = "hi";
		assertNotNull("failure - should not be null", a);
	}
	
	@Test
	public void alwaysPasses() {
		
	}
	
	@Test
	public void testSame() {
		String a = "hello";
		String b = "hello";
		assertSame("failure - should be same", a, b);
	}
	
	@Test
	public void testEquals2() {
		String a = "hello";
		String b = "hello";
		assertEquals("failure - should be equal", a, b);
	}
	
	@Test
	public void testNotSame() {
		String a = "3";
		String b = "4";
		assertNotSame("failure - should be not same", a, b);
	}
	
	@Test
	public void studentSame() {
		Student a = new Student(19, 90);
		Student b = a;
		assertSame("failure - should be the same", a, b);
	}
	
	@Test
	public void studentEqual() {
		Student a = new Student(19, 90);
		Student b = new Student(19, 90);
		assertEquals("failure - should be equal", a, b);
	}
	
	@Test
	public void assertChars() {
		String a = "hello world";
		assertThat(a, containsString("hello"));
	}
	
	
}
