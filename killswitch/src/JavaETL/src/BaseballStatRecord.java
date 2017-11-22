/**
 * This class is specifically for a baseball stat record
 */
public class BaseballStatRecord extends AbstractRecord{
	private float salary = 0;
	private float average= 0;
	private int games = 0;

	public BaseballStatRecord(){
	}

	public BaseballStatRecord(String n, float s, int g, float a){
		super(n);
		salary = s;
		average = a;
		games = g;
	}	

	public void setSalary(float s){
		salary = s;
	}

	public float getSalary(){
		return salary;
	}

	public void setGames(int g){
		games = g;
	}

	public int getGames(){
		return games;
	}

	public void setAverage(float a){
		average = a;
	}

	public float getAverage(){
		return average;
	}

	@Override
	public String toString2(){
		return String.format("BaseballStatRecord(%s,%.2f,%d,%.3f)",name,salary,games,average);
	}
}
