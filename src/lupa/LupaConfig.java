package lupa;

import javax.swing.*;
import javax.swing.event.*;

import java.awt.Dialog;
import java.awt.Dimension;

import layout.*;

public class LupaConfig {
	public int ancho;
	public int alto;
	public int amplia;
	public int refresco;
	
	private JLabel lbl1;
	private JLabel lbl2;
	private JLabel lbl3;
	private JLabel lbl4;
	
	private JSpinner spinner1;
	private JSpinner spinner2;
	private JSpinner spinner3;
	private JSpinner spinner4;
	
	private JDialog dialogo;

	public LupaConfig(JFrame jframe, int screenWidth, int screenHeight, int lancho, int lalto, int lamplia, int lrefresco)
	{
		ancho = lancho;
		alto = lalto;
		amplia = lamplia;
		refresco = lrefresco;
		
		lbl1 = new JLabel("Ancho", JLabel.CENTER);  
		lbl2 = new JLabel("Alto", JLabel.CENTER);
		lbl3 = new JLabel("Ampliación", JLabel.CENTER);  
		lbl4 = new JLabel("Refresco", JLabel.CENTER);  
		
		SpinnerModel model1 = new SpinnerNumberModel(lancho, 100, screenWidth, 10);
		spinner1 = new JSpinner(model1);

		SpinnerModel model2 = new SpinnerNumberModel(lalto, 100, screenHeight, 10);
		spinner2 = new JSpinner(model2);

		SpinnerModel model3 = new SpinnerNumberModel(lamplia, 1, 99, 1);
		spinner3 = new JSpinner(model3);

		SpinnerModel model4 = new SpinnerNumberModel(lrefresco, 0, 10000, 250);
		spinner4 = new JSpinner(model4);
		
		spinner1.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				ancho = (int) spinner1.getValue();
			}
		
		});
		spinner2.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				alto = (int) spinner2.getValue();
			}
		
		});
		spinner3.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				amplia = (int) spinner3.getValue();
			}
		
		});
		spinner4.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				refresco = (int) spinner4.getValue();
			}
		
		});

		dialogo = new JDialog(jframe, Dialog.ModalityType.DOCUMENT_MODAL);
		dialogo.setLocationRelativeTo(jframe);
		dialogo.setTitle("Configuración");
		JPanel panel = new JPanel(new SpringLayout());
		
		panel.add(lbl1);
		panel.add(spinner1);
		panel.add(lbl2);
		panel.add(spinner2);
		panel.add(lbl3);
		panel.add(spinner3);
		panel.add(lbl4);
		panel.add(spinner4);
		
		SpringUtilities.makeCompactGrid(panel,
		                                4, 2, 			//rows, cols
		                                15, 15,        	//initX, initY
		                                15, 15);       	//xPad, yPad		
		
		dialogo.getContentPane().add(panel);
		dialogo.pack();
		dialogo.setVisible(true);
		dialogo.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}
}
