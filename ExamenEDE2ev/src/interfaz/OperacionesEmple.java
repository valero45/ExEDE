package interfaz;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import logica.Departamento;
import logica.Empleado;

import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBFactory;
import org.neodatis.odb.core.query.IQuery;
import org.neodatis.odb.core.query.criteria.Where;
import org.neodatis.odb.impl.core.query.criteria.CriteriaQuery;

@SuppressWarnings("serial")
public class OperacionesEmple extends JDialog {
	private final class InsertarEmple implements ActionListener {
		// Insertar un registro a la BD
		public void actionPerformed(ActionEvent arg0) {
			int numEmple, numDepar;
			String nombre, oficio, pobla;
			double salario;
			ODB odb=ODBFactory.open(BBDD);
			
			try{
				numEmple=Integer.parseInt(data.txNumEmple.getText());
				if(numEmple>0){
					IQuery query=new CriteriaQuery(Empleado.class, Where.equal("emp_no", numEmple));
					if(odb.getObjects(query).isEmpty()){
						if(!data.txNombre.getText().isEmpty()){
							if(!data.txPoblacion.getText().isEmpty()){
								if(!data.txOficio.getText().isEmpty()){
									try{
										salario=Double.parseDouble(data.txSalario.getText());
										if(salario>0){
											try{
												numDepar=Integer.parseInt(data.txNumDepart.getText());
												query=new CriteriaQuery(Departamento.class, Where.equal("dept_no", numDepar));
												if(!odb.getObjects(query).isEmpty()){
													Departamento dep=(Departamento)odb.getObjects(query).getFirst();
													nombre=data.txNombre.getText();
													pobla=data.txPoblacion.getText();
													oficio=data.txOficio.getText();
													odb.store(new Empleado(numEmple, nombre, pobla, oficio, salario, dep));
													data.lblResultado.setText("Empleado insertado correctamente");
												}
												else
													data.lblResultado.setText("Error, el departamento no existe");
											}
											catch(NumberFormatException e){
												data.lblResultado.setText("Error, numero de departamento erroneo");
											}
										}
										else
											data.lblResultado.setText("Error, el salario debe ser positivo");
									}
									catch(NumberFormatException e){
										data.lblResultado.setText("Error, salario erroneo");
									}
								}
								else
									data.lblResultado.setText("Error, oficio vacio");
							}
							else
								data.lblResultado.setText("Error, poblacion vacia");
						}
						else
							data.lblResultado.setText("Error, nombre vacio");
					}
					else
						data.lblResultado.setText("Error, numero de empleado duplicado");
				}
				else
					data.lblResultado.setText("Error, numero de empleado no permitido");
			}catch(NumberFormatException e){
			data.lblResultado.setText("Error, numero de empleado incorrecto");
			}
			finally{
				odb.close();
			}
		}
	}

	private final class ConsultarEmple implements ActionListener {
		// Consultar un registro de la BD
		public void actionPerformed(ActionEvent arg0) {
			int numEmple;
			ODB odb=ODBFactory.open(BBDD);
			
			try{
				numEmple=Integer.parseInt(data.txNumEmple.getText());
				IQuery query=new CriteriaQuery(Empleado.class, Where.equal("emp_no", numEmple));
				if(!odb.getObjects(query).isEmpty()){
					Empleado emple=(Empleado)odb.getObjects(query).getFirst();
					data.txNombre.setText(emple.getNombre());
					data.txPoblacion.setText(emple.getPobla());
					data.txOficio.setText(emple.getOficio());
					data.txSalario.setText(String.valueOf(emple.getSalario()));
					if(emple.getDept()!=null)
						data.txNumDepart.setText(String.valueOf(emple.getDept().getDept_no()));
					else
						data.txNumDepart.setText("No tiene deparamento asignado");
					data.lblResultado.setText("Consulta correcta");
				}
				else
					data.lblResultado.setText("Error, el empleado no existe");
			}catch(NumberFormatException e){
				data.lblResultado.setText("Error, numero de empleado incorrecto");
			}
			finally{
				odb.close();
			}
		}
	}

	private final class ModificarEmple implements ActionListener {
		// Modificar registro de la BD.
		public void actionPerformed(ActionEvent arg0) {
			int numEmple, numDepar;
			double salario;
			ODB odb=ODBFactory.open(BBDD);
			
			try{
				numEmple=Integer.parseInt(data.txNumEmple.getText());
				IQuery query=new CriteriaQuery(Empleado.class, Where.equal("emp_no", numEmple));
				if(!odb.getObjects(query).isEmpty()){
					Empleado emple=(Empleado)odb.getObjects(query).getFirst();
					if(!data.txNombre.getText().isEmpty()){
						if(!data.txPoblacion.getText().isEmpty()){
							if(!data.txOficio.getText().isEmpty()){
								try{
									salario=Double.parseDouble(data.txSalario.getText());
									if(salario>0){
										try{
											numDepar=Integer.parseInt(data.txNumDepart.getText());
											query=new CriteriaQuery(Departamento.class, Where.equal("dept_no", numDepar));
											if(!odb.getObjects(query).isEmpty()){
												Departamento dep=(Departamento)odb.getObjects(query).getFirst();
												emple.setNombre(data.txNombre.getText());
												emple.setPobla(data.txPoblacion.getText());
												emple.setOficio(data.txOficio.getText());
												emple.setSalario(salario);
												emple.setDept(dep);
												odb.store(emple);
												data.lblResultado.setText("Empleado modificado correctamente");
											}
											else
												data.lblResultado.setText("Error, el departamento no existe");
										}
										catch(NumberFormatException e){
											data.lblResultado.setText("Error, numero de departamento erroneo");
										}
									}
									else
										data.lblResultado.setText("Error, el salario debe ser positivo");
								}
								catch(NumberFormatException e){
									data.lblResultado.setText("Error, salario erroneo");
								}
							}
							else
								data.lblResultado.setText("Error, oficio vacio");
						}
						else
							data.lblResultado.setText("Error, poblacion vacia");
					}
					else
						data.lblResultado.setText("Error, nombre vacio");
				}
				else
					data.lblResultado.setText("Error, el empleado no existe");
			}catch(NumberFormatException e){
			data.lblResultado.setText("Error, numero de empleado incorrecto");
			}
			finally{
				odb.close();
			}
		}
	}

	private final class BorrarEmple implements ActionListener {
		// Borrar registro de la BD
		public void actionPerformed(ActionEvent arg0) {
			int numEmple;
			ODB odb=ODBFactory.open(BBDD);
			
			try{
				numEmple=Integer.parseInt(data.txNumEmple.getText());
				IQuery query=new CriteriaQuery(Empleado.class, Where.equal("emp_no", numEmple));
				if(!odb.getObjects(query).isEmpty()){
					Empleado emple=(Empleado)odb.getObjects(query).getFirst();
					odb.delete(emple);
					data.lblResultado.setText("Empleado borrado correctamente");
				}
				else
					data.lblResultado.setText("Error, el empleado no existe");
			}catch(NumberFormatException e){
				data.lblResultado.setText("Error, numero de empleado incorrecto");
			}
			finally{
				odb.close();
			}
		}
	}

	private static final String BBDD="Empleados.dat";
	private Etiqueta data = new Etiqueta();

	public OperacionesEmple() {
		setTitle("Operaciones con Empleados");
		setModal(true);
		setBounds(100, 100, 470, 361);
		data.contentPane = new JPanel();
		data.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(data.contentPane);
		data.contentPane.setLayout(null);
		
		JLabel lblEmpleados = new JLabel("Operaciones EMPLEADOS");
		lblEmpleados.setForeground(Color.BLUE);
		lblEmpleados.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblEmpleados.setBounds(120, 11, 232, 35);
		data.contentPane.add(lblEmpleados);
		
		JLabel lblNumEmple = new JLabel("Numero empleado");
		lblNumEmple.setBounds(37, 74, 115, 14);
		data.contentPane.add(lblNumEmple);
		
		JLabel lblNombre = new JLabel("Nombre");
		lblNombre.setBounds(37, 101, 46, 14);
		data.contentPane.add(lblNombre);
		
		JLabel lblPoblacion = new JLabel("Población");
		lblPoblacion.setBounds(37, 126, 58, 14);
		data.contentPane.add(lblPoblacion);
		
		JLabel lblOficio = new JLabel("Oficio");
		lblOficio.setBounds(37, 151, 46, 14);
		data.contentPane.add(lblOficio);
		
		JLabel lblSalario = new JLabel("Salario");
		lblSalario.setBounds(37, 176, 46, 14);
		data.contentPane.add(lblSalario);
		
		JLabel lblNumDepart = new JLabel("Num Departamento");
		lblNumDepart.setBounds(37, 199, 115, 14);
		data.contentPane.add(lblNumDepart);
		
		data.txNumEmple = new JTextField();
		data.txNumEmple.setBounds(164, 71, 98, 20);
		data.contentPane.add(data.txNumEmple);
		data.txNumEmple.setColumns(10);
		
		data.txNombre = new JTextField();
		data.txNombre.setBounds(164, 96, 218, 20);
		data.contentPane.add(data.txNombre);
		data.txNombre.setColumns(10);
		
		data.txPoblacion = new JTextField();
		data.txPoblacion.setBounds(164, 121, 218, 20);
		data.contentPane.add(data.txPoblacion);
		data.txPoblacion.setColumns(10);
		
		data.txOficio = new JTextField();
		data.txOficio.setBounds(164, 146, 218, 20);
		data.contentPane.add(data.txOficio);
		data.txOficio.setColumns(10);
		
		data.txSalario = new JTextField();
		data.txSalario.setBounds(164, 171, 218, 20);
		data.contentPane.add(data.txSalario);
		data.txSalario.setColumns(10);
		
		data.txNumDepart = new JTextField();
		data.txNumDepart.setBounds(164, 196, 218, 20);
		data.contentPane.add(data.txNumDepart);
		data.txNumDepart.setColumns(10);
		
		JButton btnConsultar = new JButton("Consultar");
		btnConsultar.setFont(new Font("Dialog", Font.BOLD, 10));
		btnConsultar.setBounds(293, 70, 89, 23);
		data.contentPane.add(btnConsultar);
		
		data.lblResultado = new JLabel("---------------------------------------------------------------------");
		data.lblResultado.setFont(new Font("Dialog", Font.BOLD, 14));
		data.lblResultado.setForeground(Color.RED);
		data.lblResultado.setBounds(37, 230, 345, 14);
		data.contentPane.add(data.lblResultado);
		
		JButton btnInsertar = new JButton("Insertar Empleado");
		btnInsertar.setFont(new Font("Dialog", Font.BOLD, 10));
		btnInsertar.setBounds(21, 274, 130, 26);
		data.contentPane.add(btnInsertar);
		
		JButton btnBorrar = new JButton("Borrar Empleado");
		btnBorrar.setFont(new Font("Dialog", Font.BOLD, 10));
		btnBorrar.setBounds(163, 274, 124, 26);
		data.contentPane.add(btnBorrar);
		
		JButton btnModificar = new JButton("Modifcar Empleado");
		btnModificar.setFont(new Font("Dialog", Font.BOLD, 10));
		btnModificar.setBounds(302, 274, 129, 26);
		data.contentPane.add(btnModificar);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 182, 193));
		panel.setBounds(21, 46, 398, 211);
		data.contentPane.add(panel);
		
		//Listeners botones
		//Accion boton insertar
		btnInsertar.addActionListener(new InsertarEmple());
		
		//Accion boton consultar
		btnConsultar.addActionListener(new ConsultarEmple());
		
		//Accion boton borrar
		btnBorrar.addActionListener(new BorrarEmple());
		
		//Accion boton modificar
		btnModificar.addActionListener(new ModificarEmple());
	}
}
