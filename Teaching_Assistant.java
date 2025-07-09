
public class Teaching_Assistant implements Runnable {
	private String name;
	private double wage;
	private Queue<Test> Teaching_Assistant_tests;
	private  Queue<Test> Total_tests;
	private double P_error;
	private IsOver io;
	private total_metargelim tm;
	public Teaching_Assistant(String name,double wage, double P_error, Queue<Test> Teaching_Assistant_tests,Queue<Test> Total_tests,IsOver io,total_metargelim tm) {//Constructor.
		this.name=name;
		this.wage=wage;
		this.Teaching_Assistant_tests=Teaching_Assistant_tests;
		this.tm=tm;
		tm.set_correct_answers();
		this.Total_tests = Total_tests;
		this.P_error=P_error;
		this.io = io;
	}
	
	
	public void run()//The main function of the thread.
	{
		while(!this.io.is_over()) {
			double rand=Math.max(Math.random()*2.5,1.5);
			try {
				Thread.sleep((long)rand*1000);
			}catch(Exception e) {
				continue;
			}
			if(this.io.is_over())
			{
				break;
			}
			Test t =check_test();
			if(t==null)
			{
				break;
			}
			double add_to_wage=3*rand;
			this.wage+=add_to_wage;
			tm.setTotalWage(add_to_wage);
			Total_tests.insert(t);
		}

	}
	
	private Test check_test() {//Check the test of the student that has been pull out from the queue.
		int count_correct_answers=0;
		Test t=Teaching_Assistant_tests.extract();
		if(t!=null)
		{
			for(int i=0;i<=19;i++) {
				if((tm.getRightAnswerByI(i)==t.get_answers(i))||this.P_error>=Math.random()) {
					count_correct_answers++;
				}
			}
			t.set_init_grade(count_correct_answers);
			return t;
		}
		return null;
	}
}
