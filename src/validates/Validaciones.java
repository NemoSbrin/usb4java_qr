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
	 * M�todo privado para validar el formato correcto en una cadena de caracteres al formato de hexadecimal:<br>
	 * 0xffff o 0xFFFF
	 * @param arg - Es una cadena de carateres que se va a verificar
	 * @return response - booleano que indica si el formato es el correcto*/	
	private boolean validateInput(String arg) {
		boolean response = false;
		
		if (arg.matches("^0x[0-9a-fA-F]{4}"))
			response = true;
		
		return response;
	}
	
	/**
	 * M�todo privado para contar la cantidad de variables necesitadas si devolver un boolean en para cuando sea correcto<br>
	 * 0xffff o 0xFFFF
	 * @return response - booleano que indica si el formato es el correcto*/
	private boolean countInput() {		
		return this.inputs.length == this.limit;
	}
	
	
	/**
	 * M�todo privado para validar si el formato es correcto en un array de cadena de caracteres al formato de hexadecimal:<br>
	 * 0xffff o 0xFFFF
	 * @return response - booleano que indica si el formato es el correcto*/
	public boolean validateInputs() {
		boolean formatCorrect = true;
		
		for(String a : this.inputs) {
			if (validateInput(a)) {
				formatCorrect = false;
				break;
			}				
		}
		
		if (!formatCorrect)
			System.out.println("la variable ingresada no tiene el formato hexadecimal (0xF0F0)");
		
		if (!countInput())
			System.out.println("Se necesita 2 variables: PID y VID");
		
		return formatCorrect&countInput();
	}
	
	public Short getShort(String number) throws NumberFormatException {
		int aux = Integer.parseInt(number);
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
