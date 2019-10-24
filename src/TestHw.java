import java.io.IOException;
import java.util.ArrayList;

import com.sun.javafx.css.PseudoClassState;

import javafx.animation.RotateTransition;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.layout.StackPane;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.animation.SequentialTransition;

public class TestHw extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws IOException {
		Scene scene = new Scene(new StackPane());
		FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
		scene.setRoot(loader.load());
		primaryStage.setTitle("Draw");
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	@FXML
	Pane draw;
	@FXML
	ImageView picture;
	@FXML
	VBox cmd;
	@FXML
	TextField text;
	@FXML
	Button enter, clear, save_cmd, start, save_draw;
	ArrayList<Integer> cmd_word = new ArrayList<>();
	Canvas canvas;
	Location oldLocation = null;
	Path path;
	double[] turtle = { 268, 158 };
	double[] finalxy = { 0, 0 };
	int count_num = 0;
	Timeline time;
	int repeat_count = 0;
	int count_repeat_time = 100;
	int count = 100;
	int rotate =0;

	public void initialize() {
		enter.setOnAction(e -> send());
		start.setOnAction(e -> go());
	}

	@SuppressWarnings("restriction")
	public void send() {
		text.pseudoClassStateChanged(PseudoClassState.getPseudoClass("color"), false);
		String tem = text.getText();
		text.setText("");

		String[] commend = tem.split(" ");

		switch (commend[0].toLowerCase()) {
		case "load":
			TextField load = new TextField(tem);
			cmd_word.add(count_num);
			load.pseudoClassStateChanged(PseudoClassState.getPseudoClass("color_red"), true);
			cmd.getChildren().add(load);
			count_num++;
			break;
		case "cs":
			TextField cs = new TextField(tem);
			cmd_word.add(count_num);
			cmd.getChildren().add(cs);
			count_num++;
			break;
		case "rp":
			TextField rp = new TextField(tem);
			cmd_word.add(count_num);
			cmd.getChildren().add(rp);
			count_num++;
			break;
		case "fd":
			TextField fd = new TextField(tem);
			cmd_word.add(count_num);
			cmd.getChildren().add(fd);
			count_num++;
			break;
		case "rt":
			TextField rt = new TextField(tem);
			cmd_word.add(count_num);
			cmd.getChildren().add(rt);
			count_num++;
			break;
		case "lt":
			TextField lt = new TextField(tem);
			cmd_word.add(count_num);
			cmd.getChildren().add(lt);
			count_num++;
			break;
		case "bk":
			TextField bk = new TextField(tem);
			cmd_word.add(count_num);
			cmd.getChildren().add(bk);
			count_num++;
			break;
		case "delay":
			TextField delay = new TextField(tem);
			cmd_word.add(count_num);
			cmd.getChildren().add(delay);
			count_num++;
			break;
		case "pd":
			break;
		case "pu":
			break;
		case "cast":
			break;
		default:
			text.pseudoClassStateChanged(PseudoClassState.getPseudoClass("color"), true);
		}
	}

	public void go() {
		int skip = 0;
		int newRotate = 0;
		int delay = 0;
		int repeat_time = 0;
		count = 100;
		finalxy[0] = 0;
		finalxy[1] = 0;
		path = new Path();
		time = new Timeline();
		path.getElements().add(new MoveTo(turtle[0], turtle[1]));
		path.setStroke(Color.TRANSPARENT);
		canvas = new Canvas(532, 313);
		draw.getChildren().addAll(path, canvas);

		for (int a = 0; a < cmd_word.size(); a++) {
			String[] commend = ((TextInputControl) cmd.getChildren().get(cmd_word.get(a))).getText().split(" ");
			for (int b = 0; b < commend.length; b += 2) {
				skip = 0;
				switch (commend[b].toLowerCase()) {
				case "load":
					load();
					cmd.getChildren().get(cmd_word.get(a)).setDisable(true);
					break;
				case "cs":
					turtle[0] = 268;
					turtle[1] = 158;
					skip = 5;
					cmd.getChildren().get(cmd_word.get(a)).setDisable(true);
					break;
				case "rp":
					repeat_count++;
					repeat_time = Integer.valueOf(commend[b + 1]);
					int start = b+2, end = 0;
					int in = -1;
					for (int c = b+2; c < commend.length; c++) {
						if(commend[c].equals("[")) {
							if(in == -1) {
								in = 0;
							}
							in++;
						}
						if (commend[c].equals("]")) {
							end = c;
							in--;
						}
						if(in == 0) break;
					}
					ArrayList<String> re = new ArrayList<>();
					for (int c = start + 1; c < end; c++) {
						re.add(commend[c]);
					}
					
					repeat(re, time, repeat_time);

					b = end -1;
					skip = 3;
					cmd.getChildren().get(cmd_word.get(a)).setDisable(true);
					break;
				case "fd":

					finalxy[1] = Integer.valueOf(commend[b + 1]);
					if (rotate == 0) {
						turtle[1] = turtle[1] - finalxy[1];
						path.getElements().add(new LineTo(turtle[0], turtle[1]));
					} else {
						double x = 0;
						double y = 0;
						double tem = Math.toRadians(rotate % 360);
						if (rotate % 360 == 0) {
							y = -finalxy[1];
						} else if (rotate % 270 == 0) {
							x = -finalxy[1];
						} else if (rotate % 180 == 0) {
							y = finalxy[1];
						} else if (rotate % 90 == 0) {
							x = finalxy[1];
						} else if (rotate % 360 < 90) {
							x = finalxy[1] * Math.sin(tem);
							y = -finalxy[1] * Math.cos(tem);
						} else if (rotate % 360 < 180) {
							x = finalxy[1] * Math.sin(tem);
							y = -finalxy[1] * Math.cos(tem);
						} else if (rotate % 360 < 270) {
							x = finalxy[1] * Math.sin(tem);
							y = -finalxy[1] * Math.cos(tem);
						} else {
							x = finalxy[1] * Math.sin(tem);
							y = -finalxy[1] * Math.cos(tem);
						}

						turtle[0] = turtle[0] + x;
						turtle[1] = turtle[1] + y;
						path.getElements().add(new LineTo(turtle[0], turtle[1]));
					}
					cmd.getChildren().get(cmd_word.get(a)).setDisable(true);

					break;
				case "rt":
					newRotate = Integer.valueOf(commend[b + 1]);
					rotate = rotate + newRotate;
					cmd.getChildren().get(cmd_word.get(a)).setDisable(true);
					skip = 1;
					break;
				case "lt":
					newRotate = -Integer.valueOf(commend[b + 1]);
					rotate = rotate + newRotate;
					cmd.getChildren().get(cmd_word.get(a)).setDisable(true);
					skip = 1;
					break;
				case "bk":
					finalxy[1] = Integer.valueOf(commend[b + 1]);
					if (rotate == 0) {
						turtle[1] = turtle[1] + finalxy[1];
						path.getElements().add(new LineTo(turtle[0], turtle[1]));
					} else {
						double x = 0;
						double y = 0;
						double tem = Math.toRadians(rotate % 360);
						if (rotate % 360 == 0) {
							y = finalxy[1];
						} else if (rotate % 270 == 0) {
							x = finalxy[1];
						} else if (rotate % 180 == 0) {
							y = -finalxy[1];
						} else if (rotate % 90 == 0) {
							x = -finalxy[1];
						} else if (rotate % 360 < 90) {
							x = -finalxy[1] * Math.sin(tem);
							y = finalxy[1] * Math.cos(tem);
						} else if (rotate % 360 < 180) {
							x = -finalxy[1] * Math.sin(tem);
							y = finalxy[1] * Math.cos(tem);
						} else if (rotate % 360 < 270) {
							x = -finalxy[1] * Math.sin(tem);
							y = finalxy[1] * Math.cos(tem);
						} else {
							x = -finalxy[1] * Math.sin(tem);
							y = finalxy[1] * Math.cos(tem);
						}

						turtle[0] = turtle[0] + x;
						turtle[1] = turtle[1] + y;
						path.getElements().add(new LineTo(turtle[0], turtle[1]));
					}
					cmd.getChildren().get(cmd_word.get(a)).setDisable(true);

					break;
				case "delay":
					skip = 2;
					delay = Integer.valueOf(commend[b + 1]);
					cmd.getChildren().get(cmd_word.get(a)).setDisable(true);
					break;
				case "pd":
					cmd.getChildren().get(cmd_word.get(a)).setDisable(true);
					break;
				case "pu":
					cmd.getChildren().get(cmd_word.get(a)).setDisable(true);
					break;
				case "cast":
					cmd.getChildren().get(cmd_word.get(a)).setDisable(true);
					break;
				default:
					skip = 4;
					break;
				}

				if (skip == 1) {
					RotateTransition rt = new RotateTransition(Duration.millis(500), picture);
					rt.setFromAngle(rotate - newRotate);
					rt.setToAngle(rotate);

					time.getKeyFrames().add(new KeyFrame(new Duration(count), e -> rt.play()));
					count += 500;
				} else if (skip == 2) {
					count += delay;
				} else if (skip == 3) {
					path = new Path();
					path.getElements().add(new MoveTo(turtle[0], turtle[1]));
				}else if(skip == 4) {
					
				} else if(skip == 5){
					draw.getChildren().clear();
					draw.getChildren().add(picture);
					picture.setX(243);
					picture.setY(133);
					picture.setRotate(0);
					rotate = 0;
					path = new Path();
					path.getElements().add(new MoveTo(turtle[0], turtle[1]));
				}else {
					PathTransition pathTransition = new PathTransition();
					pathTransition.setPath(path);
					pathTransition.setNode(picture);
					pathTransition.setDuration(Duration.seconds(1.5));
					Animation animation2 = createPathAnimation(path, Duration.seconds(1.5));
					ParallelTransition pt = new ParallelTransition(pathTransition, animation2);
					time.getKeyFrames().add(new KeyFrame(new Duration(count), e -> pt.play()));
					path = new Path();
					path.getElements().add(new MoveTo(turtle[0], turtle[1]));
					count += 1500;
				}

			}
		}
		cmd_word.clear();
		if(!time.getKeyFrames().isEmpty()) {
			time.play();
		}
		

	}

	public SequentialTransition repeat(ArrayList<String> list, Timeline time, int repeat) {
		int newRotate = 0;
		int delay = 0;
		int skip = 0;
		int repeat_time;
		SequentialTransition st_se = new SequentialTransition();
		for (int b = 0; b < repeat; b++) {
			System.out.println(b);
			SequentialTransition st = new SequentialTransition();
			for (int a = 0; a < list.size(); a += 2) {
				skip = 0;
				switch (list.get(a).toLowerCase()) {
				case "load":
					load();
					break;
				case "cs":

					break;
				case "rp":
					repeat_count++;
					repeat_time = Integer.valueOf(list.get(a + 1));
					int start = a+2, end = 0;
					int in = -1;
					for (int c = a+2; c < list.size(); c++) {
						if(list.get(c).equals("[")) {
							if(in == -1) {
								in = 0;
							}
							in++;
						}
						if (list.get(c).equals("]")) {
							end = c;
							in--;
						}
						if(in == 0) break;
					}
					ArrayList<String> re = new ArrayList<>();
					for (int c = start + 1; c < end; c++) {
						re.add(list.get(c));
					}
					st.getChildren().add(repeat(re, time, repeat_time));

					a = end -1;
					skip = 3;
					break;
				case "fd":
					finalxy[1] = Integer.valueOf(list.get(a + 1));
					if (rotate == 0) {
						turtle[1] = turtle[1] - finalxy[1];
						path.getElements().add(new LineTo(turtle[0], turtle[1]));
					} else {
						double x = 0;
						double y = 0;
						double tem = Math.toRadians(rotate % 360);
						if (rotate % 360 == 0) {
							y = -finalxy[1];
						} else if (rotate % 270 == 0) {
							x = -finalxy[1];
						} else if (rotate % 180 == 0) {
							y = finalxy[1];
						} else if (rotate % 90 == 0) {
							x = finalxy[1];
						} else if (rotate % 360 < 90) {
							x = finalxy[1] * Math.sin(tem);
							y = -finalxy[1] * Math.cos(tem);
						} else if (rotate % 360 < 180) {
							x = finalxy[1] * Math.sin(tem);
							y = -finalxy[1] * Math.cos(tem);
						} else if (rotate % 360 < 270) {
							x = finalxy[1] * Math.sin(tem);
							y = -finalxy[1] * Math.cos(tem);
						} else {
							x = finalxy[1] * Math.sin(tem);
							y = -finalxy[1] * Math.cos(tem);
						}
						System.out.println(turtle[0]);
						System.out.println(turtle[1]);
						turtle[0] = turtle[0] + x;
						turtle[1] = turtle[1] + y;
						path.getElements().add(new LineTo(turtle[0], turtle[1]));
					}

					break;
				case "rt":
					newRotate = Integer.valueOf(list.get(a + 1));
					rotate = rotate + newRotate;
					skip = 1;
					break;
				case "lt":
					newRotate = -Integer.valueOf(list.get(a + 1));
					rotate = rotate + newRotate;
					skip = 1;
					break;
				case "bk":
					finalxy[1] = Integer.valueOf(list.get(a + 1));
					if (rotate == 0) {
						turtle[1] = turtle[1] + finalxy[1];
						path.getElements().add(new LineTo(turtle[0], turtle[1]));
					} else {
						double x = 0;
						double y = 0;
						double tem = Math.toRadians(rotate % 360);
						if (rotate % 360 == 0) {
							y = finalxy[1];
						} else if (rotate % 270 == 0) {
							x = finalxy[1];
						} else if (rotate % 180 == 0) {
							y = -finalxy[1];
						} else if (rotate % 90 == 0) {
							x = -finalxy[1];
						} else if (rotate % 360 < 90) {
							x = -finalxy[1] * Math.sin(tem);
							y = finalxy[1] * Math.cos(tem);
						} else if (rotate % 360 < 180) {
							x = -finalxy[1] * Math.sin(tem);
							y = finalxy[1] * Math.cos(tem);
						} else if (rotate % 360 < 270) {
							x = -finalxy[1] * Math.sin(tem);
							y = finalxy[1] * Math.cos(tem);
						} else {
							x = -finalxy[1] * Math.sin(tem);
							y = finalxy[1] * Math.cos(tem);
						}

						turtle[0] = turtle[0] + x;
						turtle[1] = turtle[1] + y;
						path.getElements().add(new LineTo(turtle[0], turtle[1]));
					}

					break;
				case "delay":
					skip = 2;
					delay = Integer.valueOf(list.get(a + 1));
					break;
				case "pd":
					break;
				case "pu":
					break;
				case "cast":
					break;
				default:
					skip = 4;
				}
				if (skip == 1) {
					RotateTransition rt = new RotateTransition(Duration.millis(500), picture);
					rt.setFromAngle(rotate - newRotate);
					rt.setToAngle(rotate);
					System.out.println(rotate);
					st.getChildren().add(rt);
					count_repeat_time += 500;
				} else if (skip == 2) {
					count_repeat_time += delay;
				} else if (skip == 3) {
					path = new Path();
					path.getElements().add(new MoveTo(turtle[0], turtle[1]));
				}else if(skip == 4) {
					
				} else {
					PathTransition pathTransition = new PathTransition();
					pathTransition.setPath(path);
					pathTransition.setNode(picture);
					pathTransition.setDuration(Duration.seconds(1.5));
					Animation animation2 = createPathAnimation(path, Duration.seconds(1.5));
					ParallelTransition pt = new ParallelTransition(pathTransition, animation2);
					st.getChildren().add(pt);
					path = new Path();
					path.getElements().add(new MoveTo(turtle[0], turtle[1]));
					count_repeat_time += 1500;
				}
			}
			st_se.getChildren().add(st);
		}
		repeat_count--;
		if(repeat_count == 0) {
				time.getKeyFrames().add(new KeyFrame(new Duration(count), e -> st_se.play()));
				count = count + count_repeat_time;
				count_repeat_time = 0;
			
		}
		return st_se;
	}

	public void load() {

	}

	private Animation createPathAnimation(Path path, Duration duration) {

		GraphicsContext gc = canvas.getGraphicsContext2D();

		Circle pen = new Circle(0, 0, 4);

		PathTransition pathTransition = new PathTransition(duration, path, pen);
		pathTransition.currentTimeProperty().addListener(new ChangeListener<Duration>() {
			Location oldLocation = null;

			@Override
			public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {

				if (oldValue == Duration.ZERO)
					return;

				double x = pen.getTranslateX();
				double y = pen.getTranslateY();

				if (oldLocation == null) {
					oldLocation = new Location();
					oldLocation.x = x;
					oldLocation.y = y;
					return;
				}

				gc.setStroke(Color.BLACK);
				gc.setFill(Color.YELLOW);
				gc.setLineWidth(2);
				gc.strokeLine(oldLocation.x, oldLocation.y, x, y);

				oldLocation.x = x;
				oldLocation.y = y;

			}
		});

		return pathTransition;
	}

	public static class Location {
		double x;
		double y;
	}

}