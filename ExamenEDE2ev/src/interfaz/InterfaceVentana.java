package interfaz;

import javax.swing.JButton;

public interface InterfaceVentana {

	void modificarDep(JButton btnModifcarDepartamento);

	String consultarDep(JButton btnConsultar, int parametro1, int parametro2);

	void borrarDep(JButton btnBorrarDepartamento);

	void insertarDep(JButton btnInsertarDepartamento);

}