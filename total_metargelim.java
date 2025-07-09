
public class total_metargelim {//Support class to the data of the metargelim.
	private double total_wage;
	private boolean []correct_answers;
	public total_metargelim()//Constructor.
	{
		total_wage=0;
		correct_answers = new boolean [20];
	}
	public  double get_total_wage()//Get the total wage.
	{
		return total_wage;
	}
	public void setTotalWage(double wage)//Set the total wage.
	{
		total_wage+=wage;
	}
	public  boolean getRightAnswerByI(int i)//Get the right_answer by the question index.
	{
		return this.correct_answers[i];
	}
	public void set_correct_answers() {//Set the array value of the correct answers.
		for(int i=0;i<=19;i++) {
			double chanses=Math.random();
			if(chanses<=0.5) {
				correct_answers[i]=true;
			}
		}
	}

}
