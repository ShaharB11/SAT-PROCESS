
public class Test {
	private int id;
	private String date;
	private boolean answers[];
	private int init_grade;
	private int final_grade;
	private String status;
	private int class_num;

	public Test(int id,String date) {//Constructor.
		this.id=id;
		this.date=date;
		this.answers=new boolean[20];

	}
	public void setAnswer(int num_question, boolean answer) {//Set an answer given by the student.(By the question number and the answer.
		this.answers[num_question]=answer;
		
	}
	public void setStatus(String status) {//Set the status of the text.
		this.status=status;
	}
	public void set_class_num(int class_num) {//Get and Set functions.
		this.class_num=class_num;
	}
	public boolean get_answers(int i) {
		return this.answers[i];
	}
	public void set_init_grade(int correct_answers) {
		this.init_grade=correct_answers*5;
	}
	public int get_init_grade() {
		return this.init_grade;
	}
	public void set_final_grade(int init_grade) {
		this.final_grade=56;
	}
public void	set_final_grade_with_bonus(int init_grade) {
	this.final_grade=this.init_grade+5;
	if(this.final_grade>100) {
		this.final_grade=100;
	}
}
public int get_final_grade() {
	return this.final_grade;
}
public int get_id() {
	return this.id;
}
public void set_final_grade_without_bonus(int init_grade) {
	this.final_grade=this.init_grade;
}
}