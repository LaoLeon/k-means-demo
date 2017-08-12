import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.awt.*;

public class mainFrame extends JFrame implements ActionListener{

	public static void main(String[] args){
		new mainFrame();
	}
	DottedPane mainPanel = new DottedPane();
	public static int width = 500,height = 500;
	public mainFrame(){
		
		setSize(width,height);
		setTitle("K-means demo");
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		//setLayout(null);
		
		
		JButton button1 = new JButton("start K-means clustering");
		button1.setLocation(0,0);
		button1.setSize(500,30);
		button1.addActionListener(this);
		
		add(button1);
		add(mainPanel);
		
		setVisible(true);
		
	}

	public void actionPerformed(ActionEvent ae){
		
		if(!mainPanel.p.isEmpty()){
			mainPanel.kMeansClustering();
			for(int i = 0 ; i < mainPanel.p.size() ; i++){
			System.out.println("("+mainPanel.p.get(i).getX() + "," + mainPanel.p.get(i).getY()+")"+" group : " + mainPanel.p.get(i).groupID);
			}
		}
		else{
			System.out.println("there are no points assigned ");
		}
	}
	
}

class DottedPane extends JPanel implements MouseListener{
	
	boolean mousePaint = false,centeroidPaint = false ;
	int k = 3;// at least a group exists
	Point point ;
	ArrayList<myPoint> p = new ArrayList<myPoint>();
	myPoint centroid[] = new myPoint[k];
	
	public DottedPane(){
		
		addMouseListener(this);
		//setBackground(Color.YELLOW);
		this.repaint();
	}
	public void mousePressed(MouseEvent e) {
	      
		point = e.getPoint();
		System.out.println("X = " + point.getX() + " Y = " + point.getY());
		p.add(new myPoint((int )point.getX(),(int )point.getY()));
		mousePaint = true ;
		repaint();
	}
	public void paint(Graphics g){
		
		if(mousePaint){
		g.setColor(Color.blue);
		try{
		g.fillRect((int )point.getX(),(int )point.getY(), 3, 3);}
		catch(NullPointerException ex){}
		mousePaint = false ;
		}
		
		if(centeroidPaint){
			//System.out.println("centeroidPaint is entered");
			g.setColor(Color.red);
			for(myPoint center : centroid){
				//g.fillRect(200,100, 20, 20);
				System.out.println(center.getX()+""+center.getY());
				g.fillRect((int )center.getX(),(int )center.getY(), 5, 5);
			}
			centeroidPaint = false ;
		}
		
		//try{
		//g.fillRect(centroid[0].x,centroid[0].y,3,3);}
		//catch(NullPointerException ex){System.out.println("hello");}
	}
	public void kMeansClustering(){
		
		int groupSzie[] = new int[k];
		for(int i = 0 ; i != groupSzie.length ; i++){
			groupSzie[i] = 0 ;
		}
		//initialize centroid randomly,
		for(int i =0 ; i < centroid.length ; i++){
			centroid[i] = new myPoint((int )(Math.random()*500) ,(int )(Math.random()*470+30));
		}
		//clustering
		for(Point pp : centroid){
			System.out.println("centroid :"+pp.x+","+pp.y);
		}
		//repaint();
		//while(true)
		for(int m = 0 ; m < 10000 ; m++)
		{
			double tempDis = 0;
			for(int i = 0 ; i < p.size() ; i++){
				tempDis = 2*Math.hypot(mainFrame.width,mainFrame.height);
				for(int j =0 ; j < centroid.length ; j++){
					//System.out.println("tempDis = "+tempDis+", distence = "+distance(p.get(i), centroid[j]));
					if( tempDis > distance(p.get(i), centroid[j])){
						
						tempDis = distance(p.get(i), centroid[j]);
						p.get(i).groupID = j ;
					}					
				}
			}
			for(int i = 0 ; i < p.size() ; i++){
				groupSzie[p.get(i).groupID]++;
			}
			
			for(int j =0 ; j < centroid.length ; j++){
				centroid[j].x = 0;
				centroid[j].y = 0;
			}
			for(int i = 0 ; i < p.size() ; i++){
				centroid[p.get(i).groupID].x += p.get(i).getX()/groupSzie[p.get(i).groupID];
				centroid[p.get(i).groupID].y += p.get(i).getY()/groupSzie[p.get(i).groupID];
			}
			for(int i = 0 ; i != groupSzie.length ; i++){
				groupSzie[i] = 0 ;
			}
		}
		centeroidPaint = true ;
		repaint();
		
	}
	
	double distance(Point p1,Point p2){
		return Math.hypot(p1.getX()-p2.getX(),p1.getY()-p2.getY());
	}
	myPoint meanPoint(myPoint[] p){
		double sumX = 0,sumY =0;
		
		for(int i = 0 ; i < p.length ; i++){
			sumX += p[i].getX();
			sumY += p[i].getY();
		}
		return new myPoint((int )sumX,(int )sumY);
	}
	
	public void mouseReleased(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mouseClicked(MouseEvent e){}
	
}

class myPoint extends Point{
	
	int groupID = 0 ;// group id starts from """0"""
	public myPoint(int x , int y){
		super(x,y);
	}
}
