import java.util.Vector;
public class Proctor implements Runnable{
	private String name;
	private int age;
	private  int num_of_students_p;
	private  Queue<Test> tests;
	private  Queue<Test> tests2;
	private  Queue<Student> line_of_students;

	public Proctor(String name, int age, int num_of_students, Queue<Student> line_of_students,Queue<Test> tests, Queue<Test> tests2) {//Constructor.
		this.name=name;
		this.age=age;
		this.num_of_students_p=num_of_students;
		this.line_of_students= line_of_students;
		this.tests=tests;
		this.tests2=tests2;

	}
	public void run() {//The main function of the thread Proctor runs the thread until end of work.
		Student s=line_of_students.extract();
		if(s!=null)
		{
			this.num_of_students_p--;
			s.getTest().setStatus("Checked By Proctor");
			s.getTest().set_class_num(s.get_class_num());
			insert_test(s);
			while(num_of_students_p>0) {
				long rand=(long)Math.random()*300;
				try {
					Thread.sleep(rand*1000);
				}catch(Exception e) {

					continue;
				}
				s=line_of_students.extract();
				if(s!=null)
				{
					s.getTest().setStatus("Checked By Proctor");
					s.getTest().set_class_num(s.get_class_num());
					this.num_of_students_p--;
					insert_test(s);
				}
				else
				{
					break;
				}
			}
		}

	}
	private void insert_test(Student s) {//Insert the test to the metargel queue who has the list number of tests.
		if(tests.get_size()<tests2.get_size()) {
			tests.insert(s.getTest());
		}else {
			tests2.insert(s.getTest());
		}


	}
}
