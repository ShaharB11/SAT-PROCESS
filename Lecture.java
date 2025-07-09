import java.util.Vector;
public class Lecture implements Runnable{
	private String name;
	private Vector <Integer> best_students;
	private Queue<Test> tests;
	private  IsOver is_over;
	private int num_of_students;
	private double total_wage;
	private total_metargelim tm;
	public Lecture(String name,Queue<Test> tests,int num_of_students,IsOver io,total_metargelim tm) {
		this.name=name;
		this.best_students = new Vector<Integer>();
		this.tests=tests;
		this.num_of_students=num_of_students;
		this.total_wage=0;
		this.is_over=io;
		this.tm=tm;
	}
	public void run() {//The main function of the Thread runs all the lecture program.
		int num_students_print=num_of_students;
		double sum_before_factor=0;
		double sum_after_factor=0;
		while(this.num_of_students>0) {
			try {
				Thread.sleep(1000);
			}catch(Exception e) {
				continue;
			}
			Test current_test=tests.extract();
			if(current_test!=null)
			{
				sum_before_factor+=current_test.get_init_grade();
				set_factor(current_test);
				sum_after_factor+=current_test.get_final_grade();
				this.num_of_students--;
				current_test.setStatus("Checked By Lecture");
				if(current_test.get_final_grade()>95) {
					best_students.add(current_test.get_id());
				}
			}
			else
			{
				break;
			}
		}
		is_over.setIsOver(true);
		Print_end_tests(num_students_print,sum_before_factor,sum_after_factor);
	}
	private void Print_end_tests(int num_students_print,double sum_before_factor,double sum_after_factor ) {//Print all the final exam data.
		System.out.println("Test is over! Grades are published, and here are the results:");
		System.out.println("The number of students is: "+ num_students_print);
		System.out.println("the avrage before factor is: "+ (sum_before_factor/num_students_print));
		System.out.println("the avrage after factor is: "+ (sum_after_factor/num_students_print));
		print_bset_students();
		total_wage = tm.get_total_wage();
		System.out.println("The total wage is: "+ this.total_wage);

	}
	public double get_total_wage()
	{
		return this.total_wage;
	}
	private void print_bset_students() {//Print the id of the best students(96 degree and above).
		for(int i=0;i<best_students.size();i++)
		{
			System.out.println("Id: " + best_students.elementAt(i));
		}
	}
	private void set_factor(Test current_test) {//Set factor to students who need one.
		if((current_test.get_init_grade()>=50)&&(current_test.get_init_grade()<=55)) {
			current_test.set_final_grade(current_test.get_init_grade());
		}else if(current_test.get_init_grade()>56) {
			current_test.set_final_grade_with_bonus(current_test.get_init_grade());
		}else {
			current_test.set_final_grade_without_bonus(current_test.get_init_grade());
		}
	}
}