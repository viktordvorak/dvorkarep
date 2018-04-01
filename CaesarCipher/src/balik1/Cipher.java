package balik1;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class Cipher extends JFrame{
  private int key;
  private String input;
  private String output;
  private char znak;
  private StringBuffer sb;
  private JFrame frame;
  private JPanel contentPane;
  private JPanel panel1;
  private JPanel panel2;
  private JPanel panel3;
  private JTextField field1;
  private JTextField field2;
  private JLabel label1;
  private JLabel label2;
  private JLabel label3;
  private JButton button1;
  private JComboBox cb1;
  

  
  public void inicializaceFrame(){
	JFrame window = new JFrame();
	window.setBounds(100, 100, 550, 550);
	window.setVisible(true);
	window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
	
	contentPane = new JPanel();
	contentPane.setBackground(Color.GRAY);
	contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	window.setContentPane(contentPane);
	contentPane.setLayout(new BorderLayout(0, 0));

	panel1 = new JPanel();
	panel1.setBorder(new EmptyBorder(50,50, 50, 50));
	contentPane.add(panel1, BorderLayout.NORTH);
	
	panel2 = new JPanel();
	panel2.setBorder(new EmptyBorder(50, 50, 50, 50));
	contentPane.add(panel2, BorderLayout.SOUTH);
	
	panel3 = new JPanel();
	panel3.setBorder(new EmptyBorder(50, 50, 50, 50));
	contentPane.add(panel3, BorderLayout.CENTER);
	
	label1 =  new JLabel("input");
	panel1.add(label1);
	
	field1 = new JTextField();
	field1.setHorizontalAlignment(SwingConstants.RIGHT);
	field1.setColumns(20);
	panel1.add(field1);
	
	label2 =  new JLabel("output");
	panel2.add(label2);
	
	field2 = new JTextField();
	field2.setHorizontalAlignment(SwingConstants.RIGHT);
	field2.setColumns(20);
	panel2.add(field2);
	
	button1 =  new JButton();
	button1.setText("Run");
	panel3.add(button1);
	
	label3 = new JLabel("key");
	panel3.add(label3);
	
	
	cb1 = new JComboBox();
	cb1.addItem(1);
	cb1.addItem(2);
	cb1.addItem(3);
	panel3.add(cb1);
	
	ActionListener actionListener = new ActionListener(){
		public void actionPerformed(ActionEvent e) {
	      cb1 = (JComboBox)e.getSource();
	      key = (int) cb1.getSelectedItem();
	      
	    }	
		
	};
	
	cb1.addActionListener(actionListener);
	
	MouseListener mouseListener = new MouseListener(){
	  public void mousePressed(MouseEvent e){
		if(e.getButton() == MouseEvent.BUTTON1){
			
		  System.out.println("Zmackl jsi tlaèítko");
		  zasifruj();	
			
		}  
	  }

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
		
	};
	
	button1.addMouseListener(mouseListener);
			
  }

  public Cipher() {
	super();
	sb=new StringBuffer();
}
  
 public String zasifruj(){
   input = field1.getText(); 
   System.out.println("key:"+key);
   for(int i=0;i<input.length();i++){
	   
	 int hodnota = (int) input.charAt(i) + key;
	 znak = (char) hodnota;
	 if(znak > 'z'){
	  znak = (char) (znak - 26);
	 }else if(znak=='!'){
	  znak = ' '; 
	 }
	 sb.append(znak);
   } 
   System.out.println(sb.toString());
   output=sb.toString();
   field2.setText("");
   field2.setText(output);
	 
   return sb.toString();	 
 } 
 
 public String desifruj(){
  input=output;
  for (int i=0; i<input.length();i++){
	int value = input.charAt(i);
	znak = (char) (value - key);
	if (znak < 'a'){
	znak = (char) (znak + 26); 	
	}
	sb.append(znak);
  }
  System.out.println(sb.toString());
  output=sb.toString();
  field2.setText(output);
  
  return sb.toString();
 
 }
 
 public static void main(String[] args){
	 Cipher sifra = new Cipher();
	 sifra.inicializaceFrame();
 }
 
  
  
  
  
	

}
