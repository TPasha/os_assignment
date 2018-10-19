import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Gui extends Application {
	
	final int allowedMaxProcess = 10;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		primaryStage.setTitle("FCFS GUI");
		primaryStage.setWidth(500);
		primaryStage.setHeight(140);
				
		Group root = new Group();

		
		VBox rows = new VBox(10);
		rows.setPadding(new Insets(20, 20, 20, 20));
		
		HBox row1Content = new HBox();
		
		Label lbl_inputp = new Label("NO. OF PROCESSES");
		lbl_inputp.setFont(Font.font("Calibri", FontWeight.NORMAL, 16));
		TextField inputProcess = new TextField();
		inputProcess.setPrefWidth(200);
		
		Button inputProcessSubmit = new Button("SUBMIT");
		
		
		
		VBox rowsInput = new VBox(10);
		VBox rowsMakeChart = new VBox(10);
		
		inputProcessSubmit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				
				rowsInput.getChildren().clear();
				rowsMakeChart.getChildren().clear();
				
				if(!inputProcess.getText().trim().isEmpty()) {
					try {
						int process = Integer.parseInt(inputProcess.getText());
						
						if(process <= allowedMaxProcess && process > 0) {
							
							primaryStage.setHeight((process * 35) + 140);
							
							Object[][] fields = generateInputs(process);
							
							for(int i = 0; i < fields.length; i++) {
								rowsInput.getChildren().add((HBox)fields[i][0]);
							}
							
							Button makeChart = new Button("Make Chart");
							makeChart.setOnAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent event) {
									
									rowsInput.getChildren().clear();
									
									Fcfs f = new Fcfs(process);
									
									for(int i = 0; i < fields.length; i++) {
										
										try {
											int arrivalTime = Integer.parseInt(((TextField)fields[i][3]).getText());
											int burstTime = Integer.parseInt(((TextField)fields[i][5]).getText());
											f.addProcess(arrivalTime, burstTime);
										}
										catch (NumberFormatException e) {
											System.out.println("Please fill all fields");
											primaryStage.setHeight(140);
											return;
										}
										
									}
									
									
									primaryStage.setWidth(550);
									primaryStage.setHeight(550);
									rowsMakeChart.getChildren().add(makeChart(f.calculate()));
									
								}
							});
							
							
							rowsInput.getChildren().add(makeChart);
							
						} else {
							System.out.println("Max. allowed processes are: "+ allowedMaxProcess);
						}
					} catch(NumberFormatException e) {
						System.out.println("Please enter integer value!");
					}
				}
				
			}
		});
		
		row1Content.setMargin(lbl_inputp, new Insets(4, 10, 0, 0));
		
		row1Content.getChildren().addAll(lbl_inputp, inputProcess, inputProcessSubmit);
		
		rows.getChildren().addAll(row1Content,rowsInput, rowsMakeChart);
		root.getChildren().addAll(rows);
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public Object[][] generateInputs(int noProcess) {
		
		Object[][] inputRows = new Object[noProcess][6];
		
		for(int i = 0; i < noProcess; i++) {
			inputRows[i][0] = new HBox(5);

			inputRows[i][1] = new Label("P"+(i+1)+" ");
			((Label)inputRows[i][1]).setFont(Font.font("Calibri", FontWeight.BOLD, 16));
			
			inputRows[i][2] = new Label("Arrival Time ");
			((Label)inputRows[i][2]).setFont(Font.font("Calibri", FontWeight.NORMAL, 16));
			
			inputRows[i][3] = new TextField();
			((TextField)inputRows[i][3]).setPrefWidth(40);
			
			inputRows[i][4] = new Label(" Burst Time ");
			((Label)inputRows[i][4]).setFont(Font.font("Calibri", FontWeight.NORMAL, 16));
			
			inputRows[i][5] = new TextField();
			((TextField)inputRows[i][5]).setPrefWidth(200);
			
			((HBox)inputRows[i][0]).getChildren().addAll((Label)inputRows[i][1],(Label)inputRows[i][2], (TextField)inputRows[i][3], (Label)inputRows[i][4], (TextField)inputRows[i][5]);
		}
		
		return inputRows;
	}
	
	
	public static StackedBarChart makeChart(int[][] timeTaken) {
		
		CategoryAxis xAxis = new CategoryAxis();
	    NumberAxis yAxis = new NumberAxis();
		StackedBarChart<String, Number> chart = new StackedBarChart<String, Number>(xAxis, yAxis);
		
		ObservableList<String> names = FXCollections.observableArrayList();
		
		XYChart.Series<String, Number> series1 = new XYChart.Series<String, Number>();
		series1.setName("Arrival Time");
		
		XYChart.Series<String, Number> series2 = new XYChart.Series<String, Number>();
		series2.setName("Waiting Time");
		
		XYChart.Series<String, Number> series3 = new XYChart.Series<String, Number>();
		series3.setName("Completion Time");
		
		for(int i = 0; i < timeTaken.length; i++) {
			String processName = "Process " + timeTaken[i][0];
			names.add(processName);
			
			series1.getData().add(new XYChart.Data<String, Number>(processName, timeTaken[i][1]));
			series2.getData().add(new XYChart.Data<String, Number>(processName, timeTaken[i][2]));
			series3.getData().add(new XYChart.Data<String, Number>(processName, timeTaken[i][3] - (timeTaken[i][1] + timeTaken[i][2])));
			
		}
		
		xAxis.setLabel("Processes");
		xAxis.setCategories(names);
		
		yAxis.setLabel("Time Taken");
		
		chart.getData().addAll(series1, series2, series3);

		return chart;
	}
	
}
