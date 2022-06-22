package validates;

import java.util.Iterator;

@SuppressWarnings("unused")
public class Validaciones {
	
	private String[] inputs;
	private int limit;
	
	public Validaciones(String[] inputs) {
		this.inputs = inputs;
		this.limit = -1;
	}
	
	public Validaciones(String[] inputs, int limit) {
		this.inputs = inputs;
		this.limit = limit;
	}
	
	/**
	 * Método privado para validar el formato correcto en una cadena de caracteres al formato de hexadecimal:<br>
	 * 0xffff o 0xFFFF
	 * @param arg - Es una cadena de carateres que se va a verificar
	 * @return response - booleano que indica si el formato es el correcto*/	
	private boolean validateInput(String arg) {
		boolean response = true;
		
		if (arg.matches("^0x[0-9a-fA-F]{4}"))
			response = false;
		
		return response;
	}
	
	/**
	 * Método privado para contar la cantidad de variables necesitadas si devolver un boolean en para cuando sea correcto<br>
	 * 0xffff o 0xFFFF
	 * @return response - booleano que indica si el formato es el correcto*/
	private boolean countInput() {		
		return !(this.inputs.length == this.limit);
	}
	
	
	/**
	 * Método privado para validar si el formato es correcto en un array de cadena de caracteres al formato de hexadecimal:<br>
	 * 0xffff o 0xFFFF
	 * @return response - booleano que indica si el formato es el correcto*/
	public boolean validateInputs() {
		
		if (countInput()) {
			System.out.println("Se necesita 2 variables: VID y PID");
			return false;
		}
		
		for(String a : this.inputs) {
			if (validateInput(a)) {
				System.out.println("la variable ingresada: "+a+" No tiene el formato hexadecimal (0xF0F0)");
				return false;
			}				
		}		
		
		
		return true;
	}
	
	public Short getShort(String number) throws NumberFormatException {
		String newNumber = number.split("0x")[1];
		int aux = Integer.parseInt(newNumber, 16);
		return (short) aux;
	}
	
	public Short[] getShorts(){
		Short[] aux = new Short[this.limit];
		for (int i = 0; i < aux.length; i++) {
			aux[i] = getShort(this.inputs[i]);
		}
		return aux;
	}
	

}
