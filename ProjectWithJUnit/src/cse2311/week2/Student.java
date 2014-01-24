package cse2311.week2;

public class Student {
	public int age;
	public int score;
	
	public Student(int age, int score) {
		this.age = age;
		this.score = score;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null) return false;
		if (other == this) return true;
		if (!(other instanceof Student)) return false;
		Student a = (Student) other;
		if ((this.age == a.age) && (this.score == a.score)) {
			return true;
		} else
			return false;
	}
}
