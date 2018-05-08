package visualize;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class VisualizePath {

	public static int WIDTH = 300;
	public static int HEIGHT = 300;
	
	public int[][] field = new int[VisualizePath.WIDTH/30][VisualizePath.HEIGHT/30];
	
	public Square[][] squares = new Square[VisualizePath.WIDTH/30][VisualizePath.HEIGHT/30];
	
	public GraphicsPanel gp;
	
	public VisualizePath() {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		gp = new GraphicsPanel();
		gp.addMouseListener(new MAdapter());
		
		frame.getContentPane().add(gp);
		
		frame.setVisible(true);
		frame.setSize(VisualizePath.WIDTH + 15, VisualizePath.HEIGHT + 40);
	}
	
	private void displayPath(Square from) {
		if (from.previous == null) return;
		
		System.out.println(from.x + " " + from.y);
		
		Rectangle shape = new Rectangle();
		shape.setBounds(from.x * VisualizePath.WIDTH/10,
						from.y * VisualizePath.HEIGHT/10,
				        VisualizePath.WIDTH/10,
				        VisualizePath.HEIGHT/10);
		
		gp.path.add(shape);
		displayPath(from.previous);
	}
	
	private void calculatePath() {
		List<Integer> x = new ArrayList<Integer>();
		List<Integer> y = new ArrayList<Integer>();
		
		for (int i = 0; i < field.length; i++) {
			for (int j = 0; j < field.length; j++) {
				int a = field[j][i];
				squares[j][i] = new Square(j, i);
				squares[j][i].distance = Integer.MAX_VALUE;
				
				if (a == 1) squares[j][i].baricade = true;
				
				if (a == 2) {
					x.add(j);
					y.add(i);
				}
			}
			System.out.println();
		}
		
		PriorityQueue<Square> squareQueue = new PriorityQueue<Square>();
		
		Square cSquare = squares[x.get(0)][y.get(0)];
		cSquare.distance = 0;
		
		squareQueue.add(cSquare);
		
		while (!squareQueue.isEmpty()) {
			
			cSquare = squareQueue.poll();
			
			for (int i = cSquare.x-1; i <= cSquare.x+1; i++) {
				for (int j = cSquare.y-1; j <= cSquare.y+1; j++) {
					if ((i < 0 || j < 0) || (i >= 10 || j >= 10)) continue;
					if (cSquare.x == j && cSquare.y == i) continue;
					
					Square actualSquare = squares[i][j];
					
					if (actualSquare.baricade) continue;
					
					if (actualSquare.distance > cSquare.distance+1) {
						actualSquare.previous = cSquare;
						actualSquare.distance = cSquare.distance+1;						
						squareQueue.add(actualSquare);
					}
				}
			}
		}
		
		for (int i = 0; i < field.length; i++) {
			for (int j = 0; j < field.length; j++) {
				if (squares[j][i].distance > 1000)
					System.out.print("0 ");
				else 
					System.out.print(squares[j][i].distance + " ");
			}
			System.out.println();
		}
		
		displayPath(squares[x.get(1)][y.get(1)].previous);
		
		gp.repaint();
	}
	
	class Square implements Comparable<Square>{
		public int x , y, distance;
		public boolean visited;
		public boolean baricade;
		
		public Square previous;
		
		public Square(int x, int y) {
			this.x = x;
			this.y = y;
			
			this.baricade = false;
			this.visited = false;
			this.previous = null;
		}

		@Override
		public int compareTo(Square o) {
			return Integer.compare(this.distance, o.distance);
		}
	}
	
	class MAdapter extends MouseAdapter {
		boolean placeBaricades = true;
		
		@Override
		public void mouseClicked(MouseEvent mEvent) {
			if ((mEvent.getModifiers() & InputEvent.BUTTON2_MASK) != 0) {
				placeBaricades = !placeBaricades;
				return;
			}
			
			if ((mEvent.getModifiers() & InputEvent.BUTTON3_MASK) != 0) {
				gp.baricades.clear();
				gp.startEnd.clear();
				gp.path.clear();
			} else if ((mEvent.getModifiers() & InputEvent.BUTTON1_MASK) != 0) {
				Rectangle shape = new Rectangle();
				
				int xId = (mEvent.getX() / (VisualizePath.WIDTH/10));
				int yId = (mEvent.getY() / (VisualizePath.HEIGHT/10));
				
				shape.setBounds(xId * VisualizePath.WIDTH/10,
						        yId * VisualizePath.HEIGHT/10,
						        VisualizePath.WIDTH/10,
						        VisualizePath.HEIGHT/10);
				if(placeBaricades) {
					gp.baricades.add(shape);
					field[xId][yId] = 1;
				} else {
					gp.startEnd.add(shape);
					
					field[xId][yId] = 2;
				}
			}
			gp.repaint();
			
			if (gp.startEnd.size() == 2) {
				calculatePath();
			}
		}
	}
	
	class GraphicsPanel extends JComponent {
		private static final long serialVersionUID = 7887880822082140409L;

		public List<Shape> baricades = new ArrayList<Shape>();
		public List<Shape> startEnd = new ArrayList<Shape>();
		public List<Shape> path = new ArrayList<Shape>();
		
		@Override
		public void paint(Graphics g) {
			Graphics2D canvas = (Graphics2D)g;
			canvas.setColor(new Color(0));
			
			for (int i = VisualizePath.WIDTH/10; i < VisualizePath.WIDTH; i += (VisualizePath.WIDTH/10)) {
				canvas.drawLine(i, 0, i, VisualizePath.HEIGHT);
			}
			
			for (int i = VisualizePath.HEIGHT/10; i < VisualizePath.HEIGHT; i += (VisualizePath.HEIGHT/10)) {
				canvas.drawLine(0, i, VisualizePath.WIDTH, i);
			}
			
			for (Shape s : baricades) {
				canvas.setColor(new Color(0));
				canvas.fill(s);
			}
			
			for (Shape s : startEnd) {
				canvas.setColor(new Color(65280));
				canvas.fill(s);
			}
			
			for (Shape s : path) {
				canvas.setColor(new Color(16711680));
				canvas.fill(s);
			} 
		}
	}
}
