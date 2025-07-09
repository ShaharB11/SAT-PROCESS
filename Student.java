public class Student implements Runnable  {
	private int id;
	private String name;
	private int class_num;
	private double knowledge;
	private double rhythm;
	private Test test;
	private Queue<Student> line_of_students;
	private total_metargelim tm;
	public Student(int id,String name,int class_num,double rhythm ,double knowledge,Queue<Student> qs,total_metargelim tm) {//Constructor
		this.id=id;
		this.name=name;
		this.class_num=class_num;
		this.knowledge=knowledge;
		this.rhythm=rhythm;
		this.test=new Test(id, "251");
		this.line_of_students =qs;
		this.tm=tm;
	}
	public void run() {//The main function of the student thread.
		for(int i=0;i<=19;i++) {
			try {
				//Thread.sleep((long)rhythm*1000);
			}
			catch(Exception e) {
				i--;
				continue;
			}
			double ProbabilityCorrect=Math.random();
			if(knowledge>=ProbabilityCorrect) {
				test.setAnswer(i, tm.getRightAnswerByI(i));
			}
			else
			{
				test.setAnswer(i, !tm.getRightAnswerByI(i));
			}
		}
		line_of_students.insert(this);
		
	}
	public Test getTest() {//Get functions.
		return this.test;
	}
	public int get_class_num() {
		return this.class_num;
	}



}
