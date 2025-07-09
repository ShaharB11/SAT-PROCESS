import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class SAT_APP implements ActionListener {//Gui.
	private JFrame frame;
	private JPanel panel;
	private JButton startButton;
	private JButton endButton;
	private JButton pErrorButton;
	private JLabel pErrorLabel;
	private JTextField pErrorField;
	private int counter_p1;
	private int counter_p2;
	private int counter_p3;
	private double pErrorValue = 0.2; // Default value for P error
	private boolean inRound = false; // Flag to track whether the application is running
	private int roundCounter = 0; // Counter for rounds

	private IsOver os; // Instance of IsOver for controlling thread termination

	// Variables to hold state between rounds
	private total_metargelim tm;
	private Queue<Student> students;
	private Vector<Student> studentList;
	private Queue<Test> teachingA1;
	private Queue<Test> teachingA2;
	private Queue<Test> teachingTotal;

	public SAT_APP() {//Constructor.
		frame = new JFrame("GUI Example");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		panel = new JPanel();
		panel.setLayout(new GridLayout(4, 2, 10, 10)); // 4 rows, 2 columns with gaps

		startButton = new JButton("Start");
		startButton.addActionListener(this);

		endButton = new JButton("End");
		endButton.addActionListener(this);

		pErrorButton = new JButton("Set P Error");
		pErrorButton.addActionListener(this);

		pErrorLabel = new JLabel("Current P Error: " + pErrorValue);

		pErrorField = new JTextField(10);

		panel.add(new JLabel()); // empty label for spacing
		panel.add(new JLabel());

		panel.add(startButton);
		panel.add(endButton);

		panel.add(pErrorButton);
		panel.add(pErrorField);

		panel.add(pErrorLabel);
		panel.add(new JLabel()); // empty label for spacing

		frame.add(panel, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
		this.counter_p1 = 0;
		this.counter_p2=0;
		this.counter_p3=0;
	}

	public void actionPerformed(ActionEvent e) {//When a button is push.
		if (e.getSource() == startButton) {
			if (!inRound) {
				inRound = true;
				roundCounter++;
				JOptionPane.showMessageDialog(frame, "Starting Round " + roundCounter);
				start_app();
				inRound=false;
			} else {
				JOptionPane.showMessageDialog(frame, "Application is already running!");
			}
		} else if (e.getSource() == endButton) {
			System.exit(1);
		} else if (e.getSource() == pErrorButton) {
			try {
				double newPError = Double.parseDouble(pErrorField.getText());
				newPError*=100;
				pErrorValue*=100;
				if (newPError >= 10 && newPError <= 90 && Math.abs(pErrorValue-newPError)==1)
				{
					newPError/=100;
					pErrorValue/=100;
					pErrorValue = newPError;
					pErrorLabel.setText("P Error: " + pErrorValue);
					pErrorField.setText(""); // Clear the text field
				} else {
					pErrorValue/=100;
					JOptionPane.showMessageDialog(frame, "Invalid input for P Error");
				}
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(frame, "Invalid input for P Error");
			}
		}
	}

	public void start_app() {//Start the application.
		// Reset variables for a new round
		os = new IsOver();
		tm = new total_metargelim();
		students = new Queue<>(os);
		teachingA1 = new Queue<>(os);
		teachingA2 = new Queue<>(os);
		teachingTotal = new Queue<>(os);
		studentList = importStudents("C:\\Users\\Admin\\Desktop\\students.txt", students, tm);

		Lecture l = start_students_and_lecture(os);
		JOptionPane.showMessageDialog(frame, "total wage : "+l.get_total_wage());
	}

	public void end_app() {
		os = null; // Release IsOver instance
		// Reset all state variables for a new round
		tm = null;
		students = null;
		teachingA1 = null;
		teachingA2 = null;
		teachingTotal = null;
		studentList = null;
		roundCounter = 0;
	}

	public Lecture start_students_and_lecture(IsOver os) {//Start the all the threads.
		// Start student threads
		Vector <Student> s = importStudents("C:\\Users\\Admin\\Desktop\\students.txt",students,tm);
		add_proctor_counters(s);
		for (int i=0;i<s.size();i++)
		{
			Student s1 = s.elementAt(i);
			Thread t1 = new Thread(s1);
			t1.start();
		}

		// Start teaching assistants
		start_teaching_assistants(teachingA1, teachingA2, teachingTotal, os, tm);

		// Start proctors
		start_proctors(students, teachingA1, teachingA2);

		// Start lecture
		Lecture lecture = new Lecture("roi zivan", teachingTotal, studentList.size(), os, tm);
		Thread lectureThread = new Thread(lecture);
		lectureThread.start();

		// Wait for lecture thread to complete
		try {
			lectureThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return lecture;
	}

	public void start_proctors(Queue<Student> s, Queue<Test> t1, Queue<Test> t2) {//Start the proctor.
		Proctor p1 = new Proctor("idan", 72, this.counter_p1, s, t1, t2);
		Proctor p2 = new Proctor("yoav", 43, this.counter_p2, s, t1, t2);
		Proctor p3 = new Proctor("pod", 25, this.counter_p3, s, t1, t2);
		Thread th1 = new Thread(p1);
		Thread th2 = new Thread(p2);
		Thread th3 = new Thread(p3);
		th1.start();
		th2.start();
		th3.start();
	}

	public void start_teaching_assistants(Queue<Test> t1, Queue<Test> t2, Queue<Test> totalTests, IsOver os, total_metargelim tm) {//Start the teaching_assistants.
		Teaching_Assistant ta1 = new Teaching_Assistant("amit", 1, pErrorValue, t1, totalTests, os, tm);
		Teaching_Assistant ta2 = new Teaching_Assistant("shahar", 3, pErrorValue, t2, totalTests, os, tm);
		Thread thread1 = new Thread(ta1);
		Thread thread2 = new Thread(ta2);
		thread1.start();
		thread2.start();
	}

	public Vector<Student> importStudents(String importQuestions, Queue<Student> qs, total_metargelim tm) {//Import the students from the file.
		Vector<Student> students = new Vector<>();
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(importQuestions));
			reader.readLine(); // Skip the header line

			String line;
			while ((line = reader.readLine()) != null) {
				String[] row = line.split("\t");
				if (row.length < 10) {
					students.add(addStudent(row, qs, tm));
				} else {
					throw new RuntimeException("Wrong file format");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return students;
	}

	public Student addStudent(String[] row, Queue<Student> qs, total_metargelim tm) {//Add student to the vector.
		int id = Integer.parseInt(row[0]);
		String name = row[1];
		int classNum = Integer.parseInt(row[2]);
		double questionAnswerTime = Double.parseDouble(row[3]);
		double probabilityCorrect = Double.parseDouble(row[4]);
		return new Student(id, name, classNum, questionAnswerTime, probabilityCorrect, qs, tm);
	}

	private void add_proctor_counters(Vector<Student> qs) {
		for(int i=0;i<qs.size();i++)
		{
			if(i%2==0)
			{
				this.counter_p1++;
			}
			else if(i%3==0)
			{
				this.counter_p2++;
			}
			else
			{
				this.counter_p3++;
			}
		}
	}

	public static void main(String[] args) {//Main.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new SAT_APP();
			}
		});
	}
}