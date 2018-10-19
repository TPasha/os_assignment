
public class Fcfs {

	// n => [process][burst][arrival]
	
	private int[][] process;
	private int values;
	
	Fcfs(int processes) {
		this.process = new int[processes][3];
		this.values = 0;
	}
	
	public int[][] calculate() {
		
		if(process.length > 0) {
			
			int[][] process = this.sortProcess();
			
			// Time Taken is an array
				// n => 0[process_number] 1[arrivalTime] 2[waitingTime] 3[completionTime] 4[burst]
			
			int[][] timeTaken = new int[process.length][5];
			
			for(int i = 0; i < process.length; i++) {
				
				if(i == 0) {
					timeTaken[i][0] = process[i][0];
					timeTaken[i][1] = process[i][2];
					timeTaken[i][2] = 0;
					timeTaken[i][3] = process[i][1];
					
					timeTaken[i][4] = process[i][1];
				} else {
							
					timeTaken[i][0] = process[i][0];
					timeTaken[i][1] = process[i][2];
					timeTaken[i][2] = timeTaken[i - 1][3] - process[i][2];
					timeTaken[i][3] = timeTaken[i][2] + process[i][2] + process[i][1];
					timeTaken[i][4] = process[i][1];
				}
				
			}
								
			return timeTaken;
		}
		
		return null;
	}
	
	public int[][] sortProcess() {
		
		for(int i = 0; i < this.process.length - 1; i++) {
			for(int j = 0; j < this.process.length - 1; j++) {
				
				int temp[] = new int[3];
				if(this.process[j][2] > this.process[j+1][2]) {
					temp = this.process[j];
					this.process[j] = this.process[j+1];
					this.process[j + 1] = temp;
				}
				
			}
		}
		
		return this.process;
	}
	
	public void addProcess(int arrivalTime, int burstTime) {
		if(values < process.length) {
			this.process[this.values][0] = this.values + 1;
			this.process[this.values][1] = burstTime;
			this.process[this.values][2] = arrivalTime;
			this.values++;
		} else {
			System.out.println("Can't add more process");
		}
	}
	
	
	@Override
	public String toString() {
		
		for(int i = 0; i < process.length; i++) {
			System.out.println("Process [" + process[i][0] + "] - Burst time [" + process[i][1] + "] - Arrival time [" + process[i][2] + "]");
		}
		
		return "That's it";
	}
			
			
}
